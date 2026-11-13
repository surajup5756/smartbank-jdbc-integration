# SmartBank JDBC Integration (Task 3)

This project retains the original console SmartBank application and adds a JDBC/DAO implementation for MySQL CRUD operations.

## Technology
- Java 11+
- MySQL 8.x
- JDBC with MySQL Connector/J 8.4.0
- Maven
- DAO pattern and PreparedStatement

## 1. Create the database
Start MySQL Server. In MySQL Workbench, open and execute `sql/schema.sql`.
It creates the `smartbank_db` schema, `accounts` table and `transactions` table.
The Java `DatabaseInitializer.createTables()` method also creates both tables automatically once the schema exists.

## 2. Configure credentials
Defaults are `root` with an empty password. If your MySQL root account has a password, set environment variables in PowerShell before running:

```powershell
$env:SMARTBANK_DB_USER = "root"
$env:SMARTBANK_DB_PASSWORD = "YOUR_MYSQL_PASSWORD"
$env:SMARTBANK_DB_URL = "jdbc:mysql://localhost:3306/smartbank_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
```

Do not commit real database passwords to GitHub.

## 3. Run the JDBC CRUD demonstration
From the `SmartBank` folder:

```powershell
mvn clean compile
mvn exec:java "-Dexec.mainClass=com.smartbank.main.JdbcCrudDemo"
```

Or run `RUN_JDBC_DEMO.bat` on Windows. The demo performs INSERT, SELECT, UPDATE and DELETE using a reserved test account number `JDBC-DEMO-001`. It removes only that test row before and after its run.

## 4. Verify in MySQL Workbench
Run:

```sql
USE smartbank_db;
SELECT * FROM accounts;
SELECT * FROM transactions;
```

The demo row is deleted at the end by design. To inspect intermediate steps, pause the program during execution or temporarily comment out the final `dao.deleteRecord(id)` call in `JdbcCrudDemo`.

## 5. Code map
- `database/DatabaseConnection.java`: central `getConnection()` method.
- `database/DatabaseInitializer.java`: creates tables from Java DDL.
- `dao/AccountDAO.java`: parameterized INSERT, SELECT, UPDATE, DELETE and existence check.
- `main/JdbcCrudDemo.java`: runs and reports all CRUD operations.
- `sql/schema.sql`: reproducible database schema.

## Resource management and SQL safety
Every database operation uses `PreparedStatement`; connections and result sets are closed using try-with-resources. SQLExceptions are rethrown with operation-specific context.

## Important scope note
The JDBC CRUD implementation is supplied as a separate DAO demonstration alongside the original in-memory SmartBank menu. The original banking menu has not yet been migrated to read/write its live account operations through MySQL. This keeps the original project behavior intact while providing a verifiable Task 3 JDBC implementation. A next integration step would connect the `Bank` service to the DAO so every banking action persists automatically.
