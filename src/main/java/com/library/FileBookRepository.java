package com.library;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Persists the inventory to a UTF-8 CSV file (Week 5).
 *
 * <p>Format: header line {@code isbn,title,author,year}, one book per line. Fields that
 * contain a comma, a quote or whitespace at the edges are wrapped in double quotes and
 * embedded quotes are doubled (RFC 4180 style). Writes go to a temporary file first and
 * are then moved over the real file, so a crash mid-write never corrupts existing data.
 */
public class FileBookRepository implements BookRepository {

    static final String HEADER = "isbn,title,author,year";

    private final Path file;

    public FileBookRepository(Path file) {
        this.file = file;
    }

    @Override
    public List<Book> loadAll() {
        List<Book> books = new ArrayList<>();
        if (!Files.exists(file)) {
            return books;                       // first run: nothing saved yet
        }
        try {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (i == 0 || line.isBlank()) {
                    continue;                   // skip header and blank lines
                }
                books.add(parseLine(line, i + 1));
            }
            return books;
        } catch (IOException e) {
            throw new RepositoryException("Cannot read data file " + file, e);
        }
    }

    @Override
    public void saveAll(Collection<Book> books) {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Book b : books) {
            lines.add(escape(b.getIsbn()) + "," + escape(b.getTitle()) + ","
                    + escape(b.getAuthor()) + "," + b.getPublicationYear());
        }
        try {
            Path parent = file.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Path tmp = file.resolveSibling(file.getFileName() + ".tmp");
            Files.write(tmp, lines, StandardCharsets.UTF_8);
            Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RepositoryException("Cannot write data file " + file, e);
        }
    }

    // ---------- CSV helpers ----------

    private static String escape(String value) {
        boolean needsQuotes = value.contains(",") || value.contains("\"")
                || !value.equals(value.trim());
        if (!needsQuotes) {
            return value;
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private static Book parseLine(String line, int lineNumber) {
        List<String> f = splitCsv(line);
        if (f.size() != 4) {
            throw new RepositoryException("Data file line " + lineNumber
                    + " is malformed (expected 4 fields, found " + f.size() + ")");
        }
        try {
            return new Book(f.get(1), f.get(2), f.get(0), Integer.parseInt(f.get(3).trim()));
        } catch (NumberFormatException e) {
            throw new RepositoryException("Data file line " + lineNumber
                    + " has an invalid year: " + f.get(3), e);
        }
    }

    static List<String> splitCsv(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        cur.append('"');
                        i++;                    // escaped quote
                    } else {
                        inQuotes = false;
                    }
                } else {
                    cur.append(c);
                }
            } else if (c == '"') {
                inQuotes = true;
            } else if (c == ',') {
                fields.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        fields.add(cur.toString());
        return fields;
    }
}
