# URL shortener service

## How to build
To build service run command in folder with sources:
```
mvn clean install
```
after that checks folder `target`: you will see artifact `shortener-0.0.1-SNAPSHOT.jar`

## Hot to run
After build `shortener-0.0.1-SNAPSHOT.jar` you can run it using command
```
java -jar shortener-0.0.1-SNAPSHOT.jar
```
## Endpoints
### Create new shortlink
To create new shortlink you need send http request below:
POST http://localhost:8090/
Request body
```
{
	"longUrl" : "https://www.google.com"
}
```
`longUrl` contains url that you want to be shortified
Response:
```
http://localhost:8090/aV084
```
### Use shortlink
To use shortlink just copy it to browser and you will be automatically redirected