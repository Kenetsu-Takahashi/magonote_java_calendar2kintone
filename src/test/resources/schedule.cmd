@echo off
set CONFIG_FILE=%MAGONOTE_PROGRAM_PATH%/schedule/config/log4j2.xml

set VM_OPTIONS=-Dlog4j.configurationFile=file:%CONFIG_FILE% -Dlog4j.configuratorClass=org.apache.log4j.xml.DOMConfigurator

set JAVA_JAR=-cp %MAGONOTE_PROGRAM_PATH%/schedule/jar/schedule.jar

set JAVA_MODULE=com.magonote.Main

java %VM_OPTIONS% %JAVA_JAR% %JAVA_MODULE%
