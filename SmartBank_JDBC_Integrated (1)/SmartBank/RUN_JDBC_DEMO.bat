@echo off
setlocal
echo SmartBank JDBC CRUD demonstration
where mvn >nul 2>nul
if errorlevel 1 (
  echo Maven was not found. Install Maven or open this project in VS Code and use Maven support.
  pause
  exit /b 1
)
call mvn -q -DskipTests compile
if errorlevel 1 (
  echo Build failed. Check the error messages above.
  pause
  exit /b 1
)
call mvn -q exec:java -Dexec.mainClass=com.smartbank.main.JdbcCrudDemo
pause
