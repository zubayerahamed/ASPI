package com.zayaanit.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * Crystal Reports PDF export reads the JRE fonts from {@code <java.home>/lib/fonts} and fails with a
 * NullPointerException in OpenTypeFontManager when that folder has no font. JDK 9+ no longer ships it,
 * so create it on startup with a font copied from the operating system.
 *
 * @author Zubayer Ahamed
 * @since Oct 5, 2026
 */
@Slf4j
@Component
public class CrystalReportFontInitializer {

	private static final String[] SYSTEM_FONT_DIRS = { "C:/Windows/Fonts", "/usr/share/fonts", "/usr/local/share/fonts", "/Library/Fonts", "/System/Library/Fonts" };
	private static final String[] PREFERRED_FONTS = { "arial.ttf", "Arial.ttf", "DejaVuSans.ttf", "LiberationSans-Regular.ttf" };

	@PostConstruct
	public void ensureJreFontFolder() {
		Path fontsDir = Paths.get(System.getProperty("java.home"), "lib", "fonts");
		if (containsFont(fontsDir)) return;

		Path font = findSystemFont().orElse(null);
		if (font == null) {
			log.error("Crystal Reports PDF export needs at least one .ttf font in {}, but no system font was found to copy. Copy a .ttf font into that folder and restart.", fontsDir);
			return;
		}

		try {
			Files.createDirectories(fontsDir);
			Files.copy(font, fontsDir.resolve(font.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
			log.info("Created {} with font {} for Crystal Reports PDF export", fontsDir, font);
		} catch (IOException | SecurityException e) {
			log.error("Crystal Reports PDF export needs at least one .ttf font in {}. Could not create it automatically ({}). "
					+ "Create the folder as administrator, copy a font into it (e.g. {}) and restart.", fontsDir, e.toString(), font);
		}
	}

	private boolean containsFont(Path dir) {
		if (!Files.isDirectory(dir)) return false;
		try (Stream<Path> files = Files.list(dir)) {
			return files.anyMatch(this::isFontFile);
		} catch (IOException e) {
			return false;
		}
	}

	private Optional<Path> findSystemFont() {
		for (String dir : SYSTEM_FONT_DIRS) {
			Path fontDir = Paths.get(dir);
			if (!Files.isDirectory(fontDir)) continue;

			for (String name : PREFERRED_FONTS) {
				Path font = fontDir.resolve(name);
				if (Files.isRegularFile(font)) return Optional.of(font);
			}

			try (Stream<Path> files = Files.walk(fontDir, 3)) {
				Optional<Path> font = files.filter(Files::isRegularFile).filter(this::isFontFile).findFirst();
				if (font.isPresent()) return font;
			} catch (IOException e) {
				log.debug("Cannot read font folder {}: {}", fontDir, e.getMessage());
			}
		}
		return Optional.empty();
	}

	private boolean isFontFile(Path file) {
		String name = file.getFileName().toString().toLowerCase();
		return name.endsWith(".ttf") || name.endsWith(".ttc");
	}
}
