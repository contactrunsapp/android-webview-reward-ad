@echo off
SETLOCAL

SET DIRNAME=%~dp0
IF "%DIRNAME%" == "" SET DIRNAME=.

SET APP_BASE_NAME=%~n0
SET APP_HOME=%DIRNAME%..

IF NOT DEFINED JAVA_HOME goto findJavaFromPath

SET JAVACMD=%JAVA_HOME%\bin\java.exe
IF NOT EXIST "%JAVACMD%" goto findJavaFromPath

goto init

:findJavaFromPath
SET JAVACMD=java
where java >NUL 2>&1
IF %ERRORLEVEL% NEQ 0 (
  echo Could not find java.exe in your PATH. Please set JAVA_HOME.
  exit /b 1
)

:init
SET CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar
SET ARGS=%*

"%JAVACMD%" %GRADLE_OPTS% -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %ARGS%

ENDLOCAL
