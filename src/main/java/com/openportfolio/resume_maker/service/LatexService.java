package com.openportfolio.resume_maker.service;

import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LatexService {

    public byte[] compile(String texContent) throws IOException, InterruptedException {
        // Create a unique temporary directory for this compilation
        Path tempDir = Files.createTempDirectory("latex-" + UUID.randomUUID());
        Path texFile = tempDir.resolve("resume.tex");
        Path pdfFile = tempDir.resolve("resume.pdf");

        try {
            // Write the LaTeX content to a file
            Files.write(texFile, texContent.getBytes(StandardCharsets.UTF_8));

            // Execute xelatex (better font support)
            // We run it twice to ensure cross-references and table layouts are correct
            for (int i = 0; i < 2; i++) {
                ProcessBuilder pb = new ProcessBuilder(
                        "pdflatex",
                        "-interaction=nonstopmode",
                        "-halt-on-error",
                        "resume.tex");
                pb.directory(tempDir.toFile());
                pb.redirectErrorStream(true);

                Process process = pb.start();

                // Read output for debugging if needed (or just wait)
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    while (reader.readLine() != null) {
                        // Consuming output to prevent process hang
                    }
                }

                boolean finished = process.waitFor(30, TimeUnit.SECONDS);
                if (!finished) {
                    process.destroyForcibly();
                    throw new IOException("LaTeX compilation timed out");
                }

                if (process.exitValue() != 0 && i == 0) {
                    // On first run, we might tolerate some errors if it's just about references
                } else if (process.exitValue() != 0) {
                    throw new IOException("LaTeX compilation failed with exit code " + process.exitValue());
                }
            }

            if (!Files.exists(pdfFile)) {
                throw new IOException("PDF file was not generated");
            }

            return Files.readAllBytes(pdfFile);

        } finally {
            // Cleanup: delete the temporary directory and all its contents
            deleteDirectory(tempDir);
        }
    }

    public byte[] compileDirectory(Path sourceDir, String texFileName, String parsedTexContent,
            java.util.Map<String, byte[]> additionalFiles)
            throws IOException, InterruptedException {
        // Create a unique temporary directory for this compilation
        Path tempDir = Files.createTempDirectory("latex-" + UUID.randomUUID());
        Path pdfFile = tempDir.resolve(texFileName.replace(".tex", ".pdf"));

        try {
            // Copy all files from source directory to temp directory
            if (Files.exists(sourceDir)) {
                Files.walk(sourceDir).forEach(source -> {
                    Path destination = tempDir.resolve(sourceDir.relativize(source));
                    try {
                        if (Files.isDirectory(source)) {
                            if (!Files.exists(destination)) {
                                Files.createDirectory(destination);
                            }
                        } else {
                            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                        }
                    } catch (IOException e) {
                        // Log or handle error
                        e.printStackTrace();
                    }
                });
            }

            // Write additional files (e.g., profile picture)
            if (additionalFiles != null) {
                for (java.util.Map.Entry<String, byte[]> entry : additionalFiles.entrySet()) {
                    Files.write(tempDir.resolve(entry.getKey()), entry.getValue());
                }
            }

            // Overwrite the specific tex file with the parsed Thymeleaf content
            Path texFile = tempDir.resolve(texFileName);
            Files.write(texFile, parsedTexContent.getBytes(StandardCharsets.UTF_8));

            // Execute xelatex since this specific template might use fontspec or modern
            // layouts
            for (int i = 0; i < 2; i++) {
                ProcessBuilder pb = new ProcessBuilder(
                        "pdflatex",
                        "-interaction=nonstopmode",
                        "-halt-on-error",
                        texFileName);
                pb.directory(tempDir.toFile());
                pb.redirectErrorStream(true);

                Process process = pb.start();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.err.println("[LATEX-OUT] " + line);
                    }
                }

                boolean finished = process.waitFor(30, TimeUnit.SECONDS);
                if (!finished) {
                    process.destroyForcibly();
                    throw new IOException("LaTeX compilation timed out");
                }

                if (process.exitValue() != 0 && i == 0) {
                    // On first run, tolerate some errors (cross-references)
                } else if (process.exitValue() != 0) {
                    throw new IOException("LaTeX compilation failed with exit code " + process.exitValue());
                }
            }

            if (!Files.exists(pdfFile)) {
                throw new IOException("PDF file was not generated");
            }

            return Files.readAllBytes(pdfFile);

        } finally {
            // Cleanup
            deleteDirectory(tempDir);
        }
    }

    private void deleteDirectory(Path directory) throws IOException {
        Files.walk(directory)
                .sorted((p1, p2) -> p2.compareTo(p1)) // Delete files before directories
                .map(Path::toFile)
                .forEach(File::delete);
    }

    /**
     * Helper to escape common LaTeX special characters.
     * Should be used on dynamic data before inserting into the template.
     */
    public String escapeLatex(String text) {
        if (text == null)
            return "";
        return text.replace("\\", "\\textbackslash{}")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("$", "\\$")
                .replace("&", "\\&")
                .replace("#", "\\#")
                .replace("^", "\\textasciicircum{}")
                .replace("_", "\\_")
                .replace("~", "\\textasciitilde{}")
                .replace("%", "\\%");
    }
}
