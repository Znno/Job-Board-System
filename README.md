# Job Board System

## Overview  
The **Job Board System** is an application designed to connect job seekers with employers. Employers can post job openings, while job seekers can browse available positions and apply by submitting their resumes.

# JavaFX Project Setup and Execution with Maven

## System Dependencies

Before running this project, ensure the following dependencies are installed on your system:

1. **Java Development Kit (JDK)**  
   - Install JDK 17 or higher (JavaFX requires at least Java 11, but JDK 17 is recommended).  
   - [Download JDK](https://www.oracle.com/java/technologies/javase-downloads.html) or use [OpenJDK](https://adoptopenjdk.net/).  
   - Verify the installation:  
     ```bash
     java -version
     ```

2. **JavaFX SDK**  
   - Download the JavaFX SDK matching your JDK version from [OpenJFX Downloads](https://openjfx.io/).  
   - Extract the SDK to a directory on your system (e.g., `/path/to/javafx-sdk/`).

3. **Set Environment Variables**  
   - Add the path to the JavaFX SDK `lib` directory to an environment variable:  
     ```bash
     export PATH_TO_FX=/path/to/javafx-sdk/lib
     ```
   - To make it persistent, add it to your shell configuration file (e.g., `.bashrc`, `.zshrc`).

## Updating `pom.xml`

Ensure the `pom.xml` includes the `javafx-maven-plugin` and specifies the path to the JavaFX SDK:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-maven-plugin</artifactId>
            <version>0.0.14</version>
            <configuration>
                <mainClass>com.example.Main</mainClass>
                <options>
                    <option>--module-path</option>
                    <option>${env.PATH_TO_FX}</option>
                    <option>--add-modules</option>
                    <option>javafx.controls,javafx.fxml</option>
                </options>
            </configuration>
        </plugin>
    </plugins>
</build>
```
## Running the Project

1. **Clone the Repository**  
   Clone the GitHub repository to your local machine:  
   ```bash
   git clone https://github.com/Znno/Job-Board-System.git
   ```
   ```bash
   cd "Job Board System"
2. **Import the database to your DBMS**
3. **Build and Run**  
  ```bash
  mvn clean javafx:run
```
##### Note:
to sign in with admin role

use these credentials

username: admin

password: admin

## System features
#### Authentication and User Management
● Users can register as either Job Seekers or Employers. 

● Job seekers can create and edit their profiles. 

● Employers can create and edit company profiles (like adding company name and history).

#### Job Management
● Employers can view job postings (their company posts).

● Employers can create job postings.

● Employers can update their job postings.

● Employers can delete their job postings.

### Application Process
● Job Seekers can apply with a resume.

● Job Seekers can check application status.

● Employers can accept or reject applications.

### Administrative Functions
● Administrators can activate and deactivate user accounts.

● Administrators can delete user accounts.

● Administrators can update user accounts.

● Administrators can view user accounts.

● Administrators can view job postings.

● Administrators can delete job postings.

● Administrators can update job postings.

### Security
o Hash passwords for all users (we use double hashing (1D hash function)).

o Different roles will be made, so each user will be given a certain rule that gives them
certain authority.

### Reliability
● Data integrity during failures, as we will consider this in implementation makes most
of the operations ATOMIC.
 
