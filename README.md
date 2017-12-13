##Welkom bij mijn cursistensysteem, hieronder volgt een kleine handleiding om de applicatie te gebruiken:

###Database setup:
Start SQL Server

In SQL Server configuration manager, zet TCP connections aan en zorg dat die op de juiste poort binnenkomen.
https://technet.microsoft.com/en-us/library/hh231672(v=sql.110).aspx

In SQL Server management studio, navigeer naar de connectie in object explorer en klik met de rechtermuisknop op properties.
Zorg ervoor dat onder Server authentication SQL Server authentication mode aanstaat.

Maak de database:
```
CREATE DATABASE CursistenSysteem;
```
Maak een login voor de applicatie i.e.:
```
CREATE LOGIN CursistenServiceAccount WITH PASSWORD = '{Your-secure-password}'
GO

Use CursistenSysteem;
GO

CREATE USER CursistenServiceAccount FOR LOGIN CursistenServiceAccount
EXEC sp_addrolemember N'db_owner', N'CursistenServiceAccount';
```
###Spring configuratie:
src/main/resources van het backend project, maak een application.properties aan die er alsvolgt uitziet:

```
spring.datasource.url=jdbc:sqlserver://localhost;databaseName=CursistenSysteem
spring.datasource.username=CursistenServiceAccount
spring.datasource.password={Your-secure-password}
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
spring.jpa.hibernate.ddl-auto = create-drop
```

We gebruiken hier create-drop, omdat de applicatie nog in ontwikkeling is.
Mocht de applicatie ooit in productie genomen worden, haal deze regel dan weg.

Tevens moet in de angular applicatie dan aangegeven worden dat we productiemodus aan willen:
https://stackoverflow.com/questions/35721206/how-to-enable-production-mode-in-angular-2

Veel plezier met de applicatie!