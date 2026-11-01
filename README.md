# Library Book Inventory Manager

Junior Java Developer Internship project. A menu-driven command-line Java
application that manages a library's book inventory using in-memory Java
collections. Week 3 added a JUnit 5 test suite; Week 4 refactored the
codebase to v2.0. Week 5 integrated everything into one deployable
application (v3.0.0) with file persistence, an executable JAR, deployment
scripts, Docker support and CI.

## Features

- Add a new book (title, author, ISBN, publication year)
- List all books in a formatted table
- Search a book by its ISBN
- Search by title / author keyword (case-insensitive, Week 4 addition)
- Update a book's title, author and publication year (ISBN stays fixed)
- Delete a book with a YES confirmation
- Input validation (no empty fields, valid year range, numeric menu choices)
- Custom exceptions: BookNotFoundException, DuplicateIsbnException
- Case-insensitive ISBN matching (Week 3 bug fix)

## Week 4 Refactoring Highlights

- Extracted ConsoleInput class - all keyboard input and validation now
  lives in one place (single responsibility)
- Menu dispatch is a command map instead of a switch statement
- Shared table layout lives in Book constants (list / ISBN search /
  keyword search all print identical columns)
- Publication-year bounds are named constants, not magic numbers
- New keyword search built on the streams API

## Week 5 Integration and Deployment Highlights

- BookRepository interface + FileBookRepository (CSV) - data now survives restarts
- BookService and LibraryApp are wired by constructor injection; the old
  no-argument constructors still give the in-memory behaviour used by the tests
- Storage chosen at start-up: `--data <file>` or `LIBRARY_DATA_FILE`
- Executable JAR (`java -jar target/library-inventory-cli.jar`)
- scripts/build.sh, run.sh, run.bat, package.sh -> dist/library-inventory-3.0.0.zip
- Dockerfile (multi-stage, data on a volume) and GitHub Actions CI workflow
- Storage errors are reported to the user instead of crashing the app

## Project Structure

    src/main/java/com/library/
    |-- Book.java                   - book entity + shared table layout (v2.0)
    |-- BookService.java            - CRUD + keyword search (v2.0)
    |-- ConsoleInput.java           - all input handling (new in v2.0)
    |-- BookNotFoundException.java  - thrown for unknown ISBNs
    |-- DuplicateIsbnException.java - thrown when an ISBN already exists
    |-- LibraryApp.java             - menu-driven CLI, start-up wiring (v3.0)
    |-- BookRepository.java         - storage interface (new in v3.0)
    |-- FileBookRepository.java     - CSV persistence (new in v3.0)
    |-- RepositoryException.java    - storage failures (new in v3.0)

    src/test/java/com/library/
    |-- BookServiceTest.java        - 15 unit tests for the service
    |-- BookTest.java               - 3 entity contract tests
    |-- LibraryAppTest.java         - 6 CLI tests (simulated keyboard input)
    |-- FileBookRepositoryTest.java - 5 persistence tests (new in v3.0)

    scripts/   build.sh, run.sh, run.bat, package.sh
    Dockerfile, .dockerignore, .github/workflows/ci.yml

## Requirements

- JDK 17 or newer
- Maven 3.8 or newer (check with: mvn -version)

## Build, Package and Run

    ./scripts/build.sh          # mvn clean package -> target/library-inventory-cli.jar
    ./scripts/run.sh            # runs the JAR, data saved to data/books.csv
    ./scripts/package.sh        # release bundle: dist/library-inventory-3.0.0.zip

    # or manually
    mvn clean package
    LIBRARY_DATA_FILE=data/books.csv java -jar target/library-inventory-cli.jar

    # Docker
    docker build -t library-inventory .
    docker run -it -v library-data:/data library-inventory

Without `--data` / `LIBRARY_DATA_FILE` the app runs in memory only (nothing saved).

## How to Run the Tests

    mvn test

Expected result: 29 tests run, 0 failures, 0 errors.

## Documentation

- docs/Week1_Inventory_Management_System_Design_Document.docx - architecture
- docs/Week2_Library_Inventory_Report.docx - implementation
- docs/Week3_Testing_Debugging_Report.docx - bugs and test suite
- docs/Week4_Refactoring_Report.docx - before/after refactoring analysis
- docs/Week5_Integration_Deployment_Developer_Documentation.docx - integration, deployment, maintenance
