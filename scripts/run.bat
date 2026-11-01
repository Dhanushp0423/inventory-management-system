@echo off
REM Starts the Library Inventory Manager on Windows with persistent storage.
cd /d "%~dp0\.."
if "%LIBRARY_DATA_FILE%"=="" set LIBRARY_DATA_FILE=data\books.csv
set JAR=target\library-inventory-cli.jar
if not exist "%JAR%" set JAR=library-inventory-cli.jar
if not exist "%JAR%" (
  echo JAR not found. Build it first with: mvn clean package
  exit /b 1
)
java -jar "%JAR%" %*
