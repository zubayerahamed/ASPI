package com.zayaanit.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

/**
 * @author Zubayer Ahamed
 * @since May 23, 2023
 */
@Slf4j
public class MetatudeUtil {
	/**
	 * Binary prefixes, used in IEC Standard for naming bytes.
	 * (https://en.wikipedia.org/wiki/International_Electrotechnical_Commission)
	 *
	 * Should be used for most representations of bytes
	 */
	private static final long KIBI = 1L << 10;
	private static final long MEBI = 1L << 20;
	private static final long GIBI = 1L << 30;
	private static final long TEBI = 1L << 40;
	private static final long PEBI = 1L << 50;
	private static final long EXBI = 1L << 60;

	/**
	 * Decimal prefixes, used for Hz and other metric units and for bytes by hard
	 * drive manufacturers
	 */
	private static final long KILO = 1_000L;
	private static final long MEGA = 1_000_000L;
	private static final long GIGA = 1_000_000_000L;
	private static final long TERA = 1_000_000_000_000L;
	private static final long PETA = 1_000_000_000_000_000L;
	private static final long EXA = 1_000_000_000_000_000_000L;

	/*
	 * Two's complement reference: 2^64.
	 */
	private static final BigInteger TWOS_COMPLEMENT_REF = BigInteger.ONE.shiftLeft(64);

	/** Constant <code>HEX_ERROR="0x%08X"</code> */
	public static final String HEX_ERROR = "0x%08X";

	private MetatudeUtil() {
	}

	/**
	 * Format bytes into a rounded string representation using IEC standard (matches
	 * Mac/Linux). For hard drive capacities, use @link
	 * {@link #formatBytesDecimal(long)}. For Windows displays for KB, MB and GB, in
	 * JEDEC units, edit the returned string to remove the 'i' to display the
	 * (incorrect) JEDEC units.
	 *
	 * @param bytes Bytes.
	 * @return Rounded string representation of the byte size.
	 */
	public static String formatBytes(long bytes) {
		if (bytes == 1L) { // bytes
			return String.format("%d byte", bytes);
		} else if (bytes < KIBI) { // bytes
			return String.format("%d bytes", bytes);
		} else if (bytes < MEBI) { // KiB
			return formatUnits(bytes, KIBI, "KiB");
		} else if (bytes < GIBI) { // MiB
			return formatUnits(bytes, MEBI, "MiB");
		} else if (bytes < TEBI) { // GiB
			return formatUnits(bytes, GIBI, "GiB");
		} else if (bytes < PEBI) { // TiB
			return formatUnits(bytes, TEBI, "TiB");
		} else if (bytes < EXBI) { // PiB
			return formatUnits(bytes, PEBI, "PiB");
		} else { // EiB
			return formatUnits(bytes, EXBI, "EiB");
		}
	}

	/**
	 * Format units as exact integer or fractional decimal based on the prefix,
	 * appending the appropriate units
	 *
	 * @param value  The value to format
	 * @param prefix The divisor of the unit multiplier
	 * @param unit   A string representing the units
	 * @return A string with the value
	 */
	private static String formatUnits(long value, long prefix, String unit) {
		if (value % prefix == 0) {
			return String.format("%d %s", value / prefix, unit);
		}
		return String.format("%.1f %s", (double) value / prefix, unit);
	}

	/**
	 * Format bytes into a rounded string representation using decimal SI units.
	 * These are used by hard drive manufacturers for capacity. Most other storage
	 * should use {@link #formatBytes(long)}.
	 *
	 * @param bytes Bytes.
	 * @return Rounded string representation of the byte size.
	 */
	public static String formatBytesDecimal(long bytes) {
		if (bytes == 1L) { // bytes
			return String.format("%d byte", bytes);
		} else if (bytes < KILO) { // bytes
			return String.format("%d bytes", bytes);
		} else {
			return formatValue(bytes, "B");
		}
	}

	/**
	 * Format hertz into a string to a rounded string representation.
	 *
	 * @param hertz Hertz.
	 * @return Rounded string representation of the hertz size.
	 */
	public static String formatHertz(long hertz) {
		return formatValue(hertz, "Hz");
	}

	/**
	 * Format arbitrary units into a string to a rounded string representation.
	 *
	 * @param value The value
	 * @param unit  Units to append metric prefix to
	 * @return Rounded string representation of the value with metric prefix to
	 *         extension
	 */
	public static String formatValue(long value, String unit) {
		if (value < KILO) {
			return String.format("%d %s", value, unit).trim();
		} else if (value < MEGA) { // K
			return formatUnits(value, KILO, "K" + unit);
		} else if (value < GIGA) { // M
			return formatUnits(value, MEGA, "M" + unit);
		} else if (value < TERA) { // G
			return formatUnits(value, GIGA, "G" + unit);
		} else if (value < PETA) { // T
			return formatUnits(value, TERA, "T" + unit);
		} else if (value < EXA) { // P
			return formatUnits(value, PETA, "P" + unit);
		} else { // E
			return formatUnits(value, EXA, "E" + unit);
		}
	}

	/**
	 * Formats an elapsed time in seconds as days, hh:mm:ss.
	 *
	 * @param secs Elapsed seconds
	 * @return A string representation of elapsed time
	 */
	public static String formatElapsedSecs(long secs) {
		long eTime = secs;
		final long days = TimeUnit.SECONDS.toDays(eTime);
		eTime -= TimeUnit.DAYS.toSeconds(days);
		final long hr = TimeUnit.SECONDS.toHours(eTime);
		eTime -= TimeUnit.HOURS.toSeconds(hr);
		final long min = TimeUnit.SECONDS.toMinutes(eTime);
		eTime -= TimeUnit.MINUTES.toSeconds(min);
		final long sec = eTime;
		return String.format("%d days, %02d:%02d:%02d", days, hr, min, sec);
	}

	/**
	 * Convert unsigned int to signed long.
	 *
	 * @param x Signed int representing an unsigned integer
	 * @return long value of x unsigned
	 */
	public static long getUnsignedInt(int x) {
		return x & 0x0000_0000_ffff_ffffL;
	}

	/**
	 * Represent a 32 bit value as if it were an unsigned integer.
	 *
	 * This is a Java 7 implementation of Java 8's Integer.toUnsignedString.
	 *
	 * @param i a 32 bit value
	 * @return the string representation of the unsigned integer
	 */
	public static String toUnsignedString(int i) {
		if (i >= 0) {
			return Integer.toString(i);
		}
		return Long.toString(getUnsignedInt(i));
	}

	/**
	 * Represent a 64 bit value as if it were an unsigned long.
	 *
	 * This is a Java 7 implementation of Java 8's Long.toUnsignedString.
	 *
	 * @param l a 64 bit value
	 * @return the string representation of the unsigned long
	 */
	public static String toUnsignedString(long l) {
		if (l >= 0) {
			return Long.toString(l);
		}
		return BigInteger.valueOf(l).add(TWOS_COMPLEMENT_REF).toString();
	}

	/**
	 * Translate an integer error code to its hex notation
	 *
	 * @param errorCode The error code
	 * @return A string representing the error as 0x....
	 */
	public static String formatError(int errorCode) {
		return String.format(HEX_ERROR, errorCode);
	}

	/**
	 * Rounds a floating point number to the nearest integer
	 *
	 * @param x the floating point number
	 * @return the integer
	 */
	public static int roundToInt(double x) {
		return (int) Math.round(x);
	}

	public static String serviceProcessIdAdvanced(String serviceName) {
		int pid = 0;
		SystemInfo systemInfo = new SystemInfo();

		// Get the operating system object.
		OperatingSystem os = systemInfo.getOperatingSystem();

		// Get the hardware abstraction layer.
		HardwareAbstractionLayer hardware = systemInfo.getHardware();

		// Iterate through the list of running processes to find the one matching the
		// service name.
		List<OSProcess> processes = os.getProcesses();
		for (OSProcess process : processes) {
			String processName = process.getName();
			if (processName.contains(serviceName)) {
				pid = process.getProcessID();
				//System.out.println("PID of " + serviceName + ": " + pid);
			}
		}
		return String.valueOf(pid);
	}

	/**
	 * Get the service process id 
	 * 
	 * @param serviceName
	 * @return {@link String}
	 */
	public static String serviceProcessId(String serviceName) {
		if (!StringUtils.hasLength(serviceName))
			return null;

		String processId = null;

		SystemInfo systemInfo = new SystemInfo();
		OperatingSystem os = systemInfo.getOperatingSystem();

		if("Windows".equalsIgnoreCase(os.getFamily())) {
			String command = "cmd /c sc queryex \"" + serviceName + "\" | findstr /C:\"PID\"";

			Process process = null;
			try {
				process = Runtime.getRuntime().exec(command);
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

				String line;
				while ((line = reader.readLine()) != null) {
					if (line.contains("PID")) {
						String[] parts = line.split(":");
						if (parts.length > 1) {
							processId = parts[1].trim();
						}
					}
				}

				process.waitFor();

				reader.close();
				process.destroy();
				process = null;
			} catch (IOException | InterruptedException e) {
				// e.printStackTrace();
			} finally {
				if(process != null) {
					process.destroy();
					process = null;
				}
			}
		} else {
			String command = "systemctl show --property MainPID --value " + serviceName;

			Process process = null;
			try {
				process = Runtime.getRuntime().exec(command);
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

				String output = reader.readLine();

				if (output != null && !output.isEmpty()) {
					processId = output;
				}

				process.waitFor();

				reader.close();
				process.destroy();
				process = null;
			} catch (IOException | InterruptedException e) {
				// e.printStackTrace();
			} finally {
				if(process != null) {
					process.destroy();
					process = null;
				}
			}
		}

		return processId;
	}

	/**
	 * Convert current Millisecond to java.util.Date
	 * 
	 * @param currentMilli
	 * @return {@link Date}
	 */
	public static Date currentMilliSecondToDate(Long currentMilli) throws Exception {
		if (currentMilli == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(currentMilli);
		Date date = calendar.getTime();

		return date;
	}
}
