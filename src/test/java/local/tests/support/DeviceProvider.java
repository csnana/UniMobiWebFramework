package local.tests.support;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DeviceProvider {
    private static final Pattern IOS_SIMULATOR =
            Pattern.compile("^\\s*(.*?)\\s+\\(([0-9A-Fa-f-]+)\\)\\s+\\((Booted|Shutdown)\\)\\s*$");
    private static final Pattern IOS_DEVICE_IDENTIFIER =
            Pattern.compile("([0-9A-Fa-f-]{20,})\\s+\\(UDID\\)");

    public DeviceTarget select(
            String platform,
            String runnerSerial,
            String configuredSerial,
            String restrictedDevices) throws IOException, InterruptedException {

        String normalizedPlatform = normalizePlatform(platform);
        Set<String> restricted = Arrays.stream(restrictedDevices.split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .map(value -> value.toLowerCase(Locale.ROOT))
                .collect(java.util.stream.Collectors.toSet());

        String serial = firstNonBlank(runnerSerial, configuredSerial);
        if (!serial.isEmpty()) {
            rejectIfRestricted(serial, restricted);
            return new DeviceTarget(serial, serial);
        }

        List<DeviceTarget> available = normalizedPlatform.equals("Android")
                ? findAndroidDevices()
                : findIosDevices();

        return available.stream()
                .filter(device -> !restricted.contains(device.udid().toLowerCase(Locale.ROOT)))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No available " + normalizedPlatform + " device found. "
                                + "Connect/start a device or set deviceSN in cloud.properties."));
    }

    private List<DeviceTarget> findAndroidDevices() throws IOException, InterruptedException {
        String output = run("adb", "devices");
        List<DeviceTarget> devices = new ArrayList<>();

        for (String line : output.lines().skip(1).toList()) {
            String[] columns = line.trim().split("\\s+");
            if (columns.length >= 2 && columns[1].equals("device")) {
                devices.add(new DeviceTarget(columns[0], columns[0]));
            }
        }
        return devices;
    }

    private List<DeviceTarget> findIosDevices() throws IOException, InterruptedException {
        List<DeviceTarget> devices = findConnectedIosDevices();
        devices.addAll(findIosSimulators());
        return devices;
    }

    private List<DeviceTarget> findConnectedIosDevices() throws IOException, InterruptedException {
        String output = run("xcrun", "devicectl", "list", "devices");
        List<DeviceTarget> devices = new ArrayList<>();

        for (String line : output.lines().toList()) {
            String[] columns = line.trim().split("\\s{2,}");
            if (columns.length < 3) {
                continue;
            }

            for (int i = 1; i < columns.length - 1; i++) {
                Matcher identifier = IOS_DEVICE_IDENTIFIER.matcher(columns[i]);
                if (!identifier.matches()) {
                    continue;
                }

                String state = columns[i + 1].toLowerCase(Locale.ROOT);
                String reality = columns[columns.length - 1];
                if (!reality.equalsIgnoreCase("simulated")
                        && (state.equals("connected") || state.equals("available (paired)"))) {
                    devices.add(new DeviceTarget(columns[0], identifier.group(1)));
                }
                break;
            }
        }
        return devices;
    }

    private List<DeviceTarget> findIosSimulators() throws IOException, InterruptedException {
        String output = run("xcrun", "simctl", "list", "devices", "available");
        List<DeviceTarget> devices = new ArrayList<>();

        for (String line : output.lines().toList()) {
            Matcher matcher = IOS_SIMULATOR.matcher(line);
            if (matcher.matches()) {
                devices.add(new DeviceTarget(matcher.group(1).trim(), matcher.group(2)));
            }
        }

        return devices;
    }

    private String run(String... command) throws IOException, InterruptedException {
        Process process = new ProcessBuilder(command)
                .redirectErrorStream(true)
                .start();

        if (!process.waitFor(15, TimeUnit.SECONDS)) {
            process.destroyForcibly();
            throw new IllegalStateException(
                    "Timed out running device discovery command: " + String.join(" ", command));
        }

        String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        if (process.exitValue() != 0) {
            throw new IllegalStateException(
                    "Device discovery command failed: " + String.join(" ", command)
                            + System.lineSeparator() + output);
        }
        return output;
    }

    private String normalizePlatform(String platform) {
        if (platform == null) {
            throw new IllegalStateException("Runner must configure device.platform as Android or iOS");
        }
        if (platform.equalsIgnoreCase("Android")) {
            return "Android";
        }
        if (platform.equalsIgnoreCase("iOS")) {
            return "iOS";
        }
        throw new IllegalArgumentException("Unsupported device.platform: " + platform);
    }

    private String firstNonBlank(String preferred, String fallback) {
        if (preferred != null && !preferred.isBlank()) {
            return preferred.trim();
        }
        return fallback == null ? "" : fallback.trim();
    }

    private void rejectIfRestricted(String serial, Set<String> restricted) {
        if (restricted.contains(serial.toLowerCase(Locale.ROOT))) {
            throw new IllegalStateException(
                    "Configured device " + serial + " is listed in device.restricted");
        }
    }

    public record DeviceTarget(String name, String udid) {
    }
}