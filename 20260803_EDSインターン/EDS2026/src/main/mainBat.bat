@echo off
setlocal enabledelayedexpansion

ren txtToJava.txxxt txtToJava.bat
ren txxtToJsp.txxxt txxtToJsp.bat

pause
call txxtToJsp.bat
pause
call txtToJava.bat
pause

echo Done!
