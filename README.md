# Netbeans-Cinema

## Description
Netbeans-Cinema is a Java-based project designed to manage cinema operations. It is developed using the NetBeans IDE and requires the Java Development Kit (JDK) to run.

This project is used as part of the Informatics learning curriculum at SMA Sedes Sapientiae Semarang.

## System Requirements
Before running the application, ensure your system meets the following requirements:

- Operating System: Windows 7/10/11 (64-bit recommended)
- Java Runtime Environment (JRE) version 8 or higher  
  [Download JRE](https://www.java.com)
- XAMPP/MariaDB (for database management)  
  [Download XAMPP](https://www.apachefriends.org)

---

## 🔽 Which Option Should You Use?

### ✅ Just Want to Run the App?
You can simply download the `/dist` folder (which contains the `.jar` file) if:
- You **don’t need to edit the source code**
- You **already have Java installed**
- You want a **faster setup**  
> How to run: Double-click the `.jar` file or run it from Command Prompt (see below).

### 🛠 Want to Edit or Study the Code?
Clone the full project if:
- You want to **open the project in NetBeans**
- You want to **customize or debug** the application

---

## Installation & Running Instructions

### Step 1: Install Prerequisites
- Install JRE on your computer
- Install and run XAMPP
- Open the XAMPP Control Panel and start **Apache** and **MySQL**

### Step 2: Import the Database
1. Open your browser and go to [http://localhost/phpmyadmin](http://localhost/phpmyadmin)
2. Create a new database named `bioskop_db2` (if it doesn’t already exist)
3. Click the **Import** tab
4. Choose the file `bioskop_db2.sql`
5. Click **Go** to import the database
6. Make sure there are no errors during the import

> **Database Configuration Parameters**  
> These should match the configuration file (if used by the app):
> - Host: `localhost`  
> - Port: `3306`  
> - Database name: `bioskop_db2`  
> - Username: `root`  
> - Password: *(leave blank)*

---

## How to Run the Application

### Method 1: Double-Click (Simplest)
1. Make sure JRE is installed
2. Right-click the `.jar` file (in `/dist` folder) → Open With → **Java(TM) Platform SE Binary**

### Method 2: Command Line (For Troubleshooting)
```bash
cd path_to_project_folder/dist
java -jar your_application_name.jar
````

---

## How to Open the Full Project in NetBeans

1. Clone the repository:

   ```bash
   git clone https://github.com/Nakkcad/Netbeans-Cinema.git
   cd Netbeans-Cinema
   ```

2. Open NetBeans:

   * Go to `File > Open Project`
   * Select the cloned folder and click **Open**

3. Configure the JDK:

   * Go to `Tools > Java Platforms` to ensure the JDK is detected
   * Set it as the default for the project

4. Run the project:

   * Press `F6` or go to `Run > Run Project`

---

## Troubleshooting

### Common Issues & Solutions:

* **Application won’t start**:

  * Ensure JRE is correctly installed
  * Try running from the command line to see error messages

* **Database connection failed**:

  * Make sure MySQL server is running
  * Verify credentials in configuration file
  * Ensure the `bioskop_db2` database is imported correctly

---
