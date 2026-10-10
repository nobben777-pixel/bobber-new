@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem Gradle startup script for Windows
@rem ##########################################################################
setlocal
setlocal enabledelayedexpansion
for /F "usebackq delims=" %%a in ("%~dp0gradle.properties") do set "%%a"
set GRADLE_JAR=%GRADLE_USER_HOME%\wrapper\dists\gradle-%GRADLE_VERSION%-bin\gradle-%GRADLE_VERSION%\lib\gradle-launcher-*.jar
java -Dorg.gradle.launcher.useClasspathonlyBootstrap=true -classpath "%GRADLE_JAR%" org.gradle.launcher.GradleMain %*
endlocal
