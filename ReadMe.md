<H5>App Run commands via command line</H5>
<ol>
<li>go to the root of the project</li>
<li>execute the following commands</li>
<li>mvn clean install</li>
<li>mvn spring-boot:run -Dspring-boot.run.profiles=sit -Dspring-boot.run.arguments=--secret-key=1234,--csv-download-path=C:\Users\developer\Documents\Downloads\
</li>
</ol>
<br>
<H5>App Run commands via IntelliJ IDE</H5>
<ol><li>go to the Run/Debug Configurations of the project</li>
<li>go to Templates >> Application </li>
<li>Enter the following values</li>
<li>Main class:{select your spring boot main class}</li>
<li>Environment variables:{add the following one-by-one}
<ol><li>spring.profiles.active=sit</li>
<li>secret-key=1234</li>
<li>csv-download-path=C:\Users\developer\Documents\Downloads\</li></ol>
</li></ol>
<br>
<H5>API Endpoints</H5>
http://localhost:8080/api/read/files
