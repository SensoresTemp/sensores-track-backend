@echo off
setlocal

REM === CONFIGURAÇÕES DO BANCO ===
set PGUSER=admin
set PGPASSWORD=abxKnVICdETWxpnb8ckjPNZgwEqbTrD5
set PGHOST=dpg-d16655p5pdvs73bff06g-a.oregon-postgres.render.com
set PGPORT=5432
set PGDATABASE=sensoresdb2

REM === CAMINHO DO pg_dump (ajuste se necessário) ===
set PGBIN="C:\Program Files\PostgreSQL\16\bin"

REM === GERAR TIMESTAMP ===
for /f "tokens=1-4 delims=/ " %%a in ('date /t') do (
    set mydate=%%d-%%b-%%c
)
for /f "tokens=1-2 delims=: " %%a in ('time /t') do (
    set mytime=%%a-%%b
)
set mytimestamp=%mydate%_%mytime%

REM === DIRETÓRIO DO SCRIPT ===
set BACKUP_DIR=%~dp0
set BACKUP_FILE=%BACKUP_DIR%backup_%PGDATABASE%_%mytimestamp%.sql

REM === EXECUTA BACKUP ===
echo Fazendo backup do banco %PGDATABASE% para o arquivo:
echo %BACKUP_FILE%
%PGBIN%\pg_dump -h %PGHOST% -U %PGUSER% -p %PGPORT% -d %PGDATABASE% -F p -f "%BACKUP_FILE%"

if %errorlevel%==0 (
    echo Backup concluído com sucesso!
) else (
    echo ERRO no backup!
)

endlocal
pause
