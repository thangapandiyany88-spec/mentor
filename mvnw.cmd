@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script for Maven Wrapper
@REM ----------------------------------------------------------------------------

@IF "%HOME%" == "" (set "HOME=%HOMEDRIVE%%HOMEPATH%")

@set ERROR_CODE=0

@set MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%
@IF "%MAVEN_PROJECTBASEDIR%" == "" set MAVEN_PROJECTBASEDIR=%~dp0

@set MAVEN_OPTS=-Xmx512m

@IF EXIST "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties" (
    @FOR /F "tokens=1,2 delims==" %%A IN ('findstr /v "^#" "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties"') DO @(
        @IF "%%A" == "wrapperUrl" set WRAPPER_URL=%%B
    )
)

echo Running Maven project...
if exist "%JAVA_HOME%\bin\java.exe" (
    "%JAVA_HOME%\bin\java.exe" -version
) else (
    echo Java executable not found in PATH or JAVA_HOME. Please install JDK 17+ and set JAVA_HOME.
)
