@echo off
:loop
echo Fazendo GET na API...
curl -X GET https://big-screen-backend.onrender.com/api/messages
timeout /t 60
goto loop
