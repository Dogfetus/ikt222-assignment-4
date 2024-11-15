# Patient Records System - README

## Overview
This assignment focuses on identifying and fixing web application vulnerabilities in a Java-based patient records system. The application simulates an initial attempt at building a system for General Practitioners (GPs) to log in and search for patient records. The application utilizes the Jetty server and SQLite database, with templates rendered via Freemarker.

## Requirements
- **Java Development Kit (JDK)**: The application requires **JDK 8 or later**. For compatibility, please install [Java SE Development Kit 11.0.18](https://www.oracle.com/za/java/technologies/javase/jdk11-archive-downloads.html).
- **Operating System**: Compatible with Linux, macOS, and Windows.

## Setup Instructions
1. **Download the Project Files**:
   - Download the `patients.zip` file containing the application's source code.
   - Extract the contents of the ZIP file to your desired directory.

2. **Database Access**:
   - Access the application's SQLite database to review login credentials and patient details.
   - You can use the SQLite CLI (`sqlite3`) or a GUI tool such as [DB Browser](https://sqlitebrowser.org/) to interact with the database.
   - Use `.schema` in the CLI to view the database structure and SQL queries for detailed data examination.

3. **Run the Application**:
   - **Linux/macOS**: Open a terminal, navigate to the project directory, and run:
     ```bash
     ./gradlew run
     ```
   - **Windows**: Use the following command without the leading `./`:
     ```bash
     gradlew run
     ```
   - Open a web browser and navigate to `http://localhost:8080` to access the application interface.

## Assignment Tasks

### 1. Analyzing Security Vulnerabilities
   - **Database Inspection**: Start by exploring the database to understand its schema and inspect data related to user credentials and patient information.
   - **Security Testing**: Use the application to identify at least three security vulnerabilities via the web interface.
   - **Code Review**: Examine the source code to uncover three additional security issues that are not accessible via the web interface.
   - **Documentation**: For each vulnerability, document the details, identification steps, and supporting screenshots.

### 2. Fixing Security Vulnerabilities
   - Select two identified vulnerabilities (one from each source: the web interface and code review).
   - Modify the source code and database as needed to address these issues.
   - Document the changes made and explain how they mitigate the identified vulnerabilities, including screenshots for clarification.

### 3. Report and Submission
   - Write a report (`report.pdf`) summarizing the identified vulnerabilities and the solutions implemented. Limit the report to 1000 words (excluding cover page and references).
   - Place the `report.pdf` in the same directory as the `build.gradle` file.
   - Run the following command to prepare the project for submission:
     ```bash
     ./gradlew submission
     ```
   - This command will create a `source_code.zip` file containing all necessary materials for submission.

## Deliverables
- **report.pdf**: A PDF file detailing the vulnerability analysis and fixes.
- **source_code.zip**: A ZIP file containing the modified source code and report.

Submit both `report.pdf` and `source_code.zip` separately on the Canvas submission page.

## Technical Details

- **Database**: SQLite 3 accessed via JDBC.
- **Template Engine**: Freemarker for dynamic HTML generation.
- **Web Server**: Jetty for handling HTTP requests.

## Important Commands
- **Run Application**: `./gradlew run` (or `gradlew run` on Windows)
- **Submit Assignment**: `./gradlew submission`

## Notes
- Ensure that `report.pdf` is included in the generated `source_code.zip`.
- Document vulnerabilities clearly and test each fix thoroughly to confirm successful resolution.
