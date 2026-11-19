# Student Management System (Java + JDBC + JFrame)

**Name:** MD. Lutfar Rahman  **ID:** 027232005101051

## Run

### NetBeans (recommended)
1. NetBeans → **File → Open Project** → select the `StudentManagementJDBC` folder (it shows a Maven icon) → Open
2. On the first open it downloads the SQLite driver (internet required)
3. Click the green **Run ▶** button (F6), or right-click the project → **Run**

### VS Code
Open the folder → install the "Extension Pack for Java" extension → open `Main.java` → **Run**

### Terminal
```bash
./run.sh
```

## Project structure
```
pom.xml                    → Maven config (SQLite JDBC dependency)
src/main/java/com/sms/
├── Main.java              → entry point, creates DB tables, opens the Welcome screen
├── util/AppInfo.java      → Name & ID (shown in every output)
├── model/Student.java     → model class (one students row)
├── model/User.java        → model class (one admins row)
├── db/DBConnection.java   → JDBC connection + CREATE TABLE (admins, students)
├── dao/StudentDAO.java    → JDBC CRUD (INSERT/SELECT/UPDATE/DELETE)
├── dao/UserDAO.java       → Admin Signup (INSERT) + Login (SELECT), password saved as SHA-256 hash
└── ui/
    ├── WelcomeFrame.java  → screen 1: Welcome, Login / Sign Up buttons
    ├── SignupFrame.java   → screen 2: Admin sign up (User Name, Phone, Email, Password)
    ├── LoginFrame.java    → screen 3: Admin login; wrong email/password → error + Login button moves away from the mouse
    └── StudentFrame.java  → screen 4: Students page (opens after login) with Admin info + Logout on top
lib/sqlite-jdbc-*.jar      → JDBC driver
students.db                → database file (created on first run)
```

## Video explanation guide (~5 minutes)
1. **Intro**: name, ID, project name. The project uses Java, JDBC, SQLite and JFrame.
2. **DBConnection.java**: the 5 JDBC steps (load driver → getConnection → statement → execute → close).
3. **Student.java**: model class with getters and setters.
4. **StudentDAO.java**:
   - `insert/update/delete` → `PreparedStatement` + `executeUpdate()`
   - `getAll/search` → `executeQuery()` + `ResultSet`, results go into a `List<Student>` (`ArrayList`, a Java Collection)
   - PreparedStatement `?` prevents SQL injection.
5. **StudentFrame.java**: JFrame, JTable, buttons, ActionListener.
6. **Auth demo**: Welcome → Sign Up → Login with a wrong password (error shows, the Login button runs away from the mouse) → fix the password → Login → Students page opens with the Admin info on top → Logout.
7. **Student demo**: on the Students page add 3–4 students → click a row and update → search → delete → show the console logs (name & ID appear in every line).
# student_management_java
