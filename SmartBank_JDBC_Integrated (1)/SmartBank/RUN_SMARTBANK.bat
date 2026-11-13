@echo off
setlocal
if not exist out mkdir out
echo Compiling SmartBank...
javac -encoding UTF-8 -d out src\com\smartbank\models\*.java src\com\smartbank\services\*.java src\com\smartbank\utils\*.java src\com\smartbank\main\Main.java
if errorlevel 1 (
  echo.
  echo Compilation failed. Check the Java errors above.
  pause
  exit /b 1
)
echo.
echo Starting SmartBank...
java -cp out com.smartbank.main.Main
pause
