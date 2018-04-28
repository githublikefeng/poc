@echo off
if '%1=='## goto ENVSET

SET APPHOME=%~dp0
SET LIBDIR=%APPHOME%lib
SET CLSPATH=conf/
SET SERVER_LOG=./logs/server.out
SET JAVA_OPTS=

if not exist ./logs (mkdir logs)

FOR %%c in (%LIBDIR%\*.jar) DO Call %0 ## %%c

goto RUN

:RUN
echo (Using JAVA_OPTS=%JAVA_OPTS%)...
echo StockServer is started.
java -Xmx1024m %JAVA_OPTS% -cp %CLSPATH% com.example.http.HttpClientApp > %SERVER_LOG% 2>&1 &
goto END

:ENVSET
set CLSPATH=%CLSPATH%;%2
goto END

:END