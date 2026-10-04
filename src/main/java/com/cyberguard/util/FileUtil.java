package com.cyberguard.util;

import com.cyberguard.exception.FileValidationException;
import com.cyberguard.model.Evidence;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Safe evidence storage: whitelist, size limit, content-signature check, random on-disk names. */
public final class FileUtil {
    public static final long MAX_SIZE = 5L * 1024 * 1024;
    private static final Map<String, String> TYPES = Map.of(
            "jpg", "image/jpeg", "jpeg", "image/jpeg", "png", "image/png", "pdf", "application/pdf", "txt", "text/plain");
    private static final Path BASE = Paths.get(DBUtil.cfg("UPLOAD_DIR",
            System.getProperty("user.home") + "/cyberguard_uploads")).toAbsolutePath().normalize();

    private FileUtil() { }

    public static Evidence store(Part part) throws FileValidationException {
        String safe = sanitize(part.getSubmittedFileName());
        String ext = extension(safe);
        if (!TYPES.containsKey(ext))
            throw new FileValidationException("\"" + safe + "\" is not allowed. Upload JPG, PNG, PDF or TXT files only.");
        if (part.getSize() > MAX_SIZE)
            throw new FileValidationException("\"" + safe + "\" is larger than 5 MB.");
        try {
            byte[] head;
            try (InputStream in = part.getInputStream()) { head = in.readNBytes(512); }
            if (!signatureMatches(ext, head))
                throw new FileValidationException("\"" + safe + "\" does not look like a real ." + ext + " file.");
            Files.createDirectories(BASE);
            String stored = UUID.randomUUID() + "." + ext;
            try (InputStream in = part.getInputStream()) { Files.copy(in, BASE.resolve(stored)); }
            return new Evidence(safe, TYPES.get(ext), stored);
        } catch (IOException e) {
            throw new FileValidationException("The file \"" + safe + "\" could not be saved. Please try again.", e);
        }
    }

    /** Resolves a stored file name; refuses anything that would escape the upload folder. */
    public static Path resolve(String storedName) throws FileValidationException {
        Path p = BASE.resolve(storedName).normalize();
        if (!p.startsWith(BASE)) throw new FileValidationException("Invalid file path.");
        return p;
    }

    public static void deleteQuietly(List<Evidence> files) {
        for (Evidence e : files) {
            try { Files.deleteIfExists(resolve(e.getFilePath())); } catch (Exception ignored) { }
        }
    }

    static String sanitize(String name) {
        String n = name == null ? "" : name;
        n = n.substring(Math.max(n.lastIndexOf('/'), n.lastIndexOf('\\')) + 1).replaceAll("[^A-Za-z0-9._-]", "_");
        while (n.startsWith(".")) n = n.substring(1);
        if (n.length() > 80) n = n.substring(n.length() - 80);
        return n.isEmpty() ? "file" : n;
    }

    private static String extension(String n) {
        int i = n.lastIndexOf('.');
        return i < 0 ? "" : n.substring(i + 1).toLowerCase();
    }

    static boolean signatureMatches(String ext, byte[] h) {
        switch (ext) {
            case "jpg": case "jpeg": return h.length > 3 && (h[0] & 0xFF) == 0xFF && (h[1] & 0xFF) == 0xD8 && (h[2] & 0xFF) == 0xFF;
            case "png": return h.length > 3 && (h[0] & 0xFF) == 0x89 && h[1] == 'P' && h[2] == 'N' && h[3] == 'G';
            case "pdf": return h.length > 3 && h[0] == '%' && h[1] == 'P' && h[2] == 'D' && h[3] == 'F';
            case "txt": for (byte b : h) if (b == 0) return false; return true;
            default: return false;
        }
    }
}
