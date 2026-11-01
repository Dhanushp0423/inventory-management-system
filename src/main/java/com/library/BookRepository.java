package com.library;

import java.util.Collection;
import java.util.List;

/**
 * Storage abstraction (Week 5). BookService depends on this interface instead of a
 * concrete storage technology, so a database-backed implementation can be added
 * later without touching the service, the CLI or the existing tests.
 */
public interface BookRepository {

    /** Returns every stored book in insertion order (empty list if nothing is stored yet). */
    List<Book> loadAll();

    /** Replaces the stored data with exactly the given books. */
    void saveAll(Collection<Book> books);

    /** A repository that stores nothing: data lives only in memory (Week 2-4 behaviour). */
    static BookRepository inMemory() {
        return new BookRepository() {
            @Override
            public List<Book> loadAll() {
                return List.of();
            }

            @Override
            public void saveAll(Collection<Book> books) {
                // intentionally empty - nothing is persisted
            }
        };
    }
}
