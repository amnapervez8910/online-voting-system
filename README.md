# 🗳️ Online Voting System

A desktop-based **Online Voting System** developed using **Java Swing** and **Oracle Database** as an Object Oriented Programming project.  
The system provides a user-friendly interface for voter registration, login, vote casting, and voter information management.

---

## ✨ Features

- ✅ Voter signup and login system
- ✅ Secure voter authentication
- ✅ Vote casting functionality
- ✅ Voter information management
- ✅ Interactive GUI using Java Swing
- ✅ Database connectivity with Oracle
- ✅ Input handling and validation

---

## 🛠 Technologies Used

- **Programming Language:** Java
- **GUI Library:** Java Swing
- **Database:** Oracle Database
- **Concepts:** Object Oriented Programming (OOP)

---

## ▶️ How to Run

### 🔹 Step 1: Setup Oracle Database

Run the SQL file:

```sql
database_setup.sql
```

to create the required database tables.

---

### 🔹 Step 2: Compile the Java Program

```bash
javac OnlineVotingSystem.java
```

---

### 🔹 Step 3: Run the Program

```bash
java OnlineVotingSystem
```

---

## 🎯 System Functionalities

### 🔐 Login System
- Users can log in using their Voter ID and password

### 📝 Signup System
- New users can register by entering their details

### 🗳️ Vote Casting
- Logged-in users can cast their votes

### 👤 Voter Information
- Users can view voter-related information and details

---

## 🖥 User Interaction Flow

1. User opens the application
2. Welcome screen appears with Login and Signup options
3. New users create an account through Signup
4. Existing users log in using credentials
5. Logged-in users access the main menu
6. Users can cast votes and access system functionalities

---

## 📦 Key Components

### Important Methods

- `loadImageIcon()` → Loads and resizes background images
- `initializeNextVoterId()` → Fetches the next voter ID from the database
- `setCurrentVoterId()` → Stores logged-in voter ID
- `clearFrame()` → Clears frame contents
- `showVoteButtons()` → Displays voting options and main menu

---

## ⚠️ Requirements

- Java JDK installed
- Oracle Database installed and configured
- JDBC Driver configured properly
- IDE recommended: IntelliJ IDEA / NetBeans / Eclipse

---
