# ASPI ERP Solution
### Modules: Accounting, Purchase, Sales, Inventory

```sh
ZAYAAN IT
DEVELOPED BY : ZUBAYER AHAMED
```

## Requirements
- JDK 25
- Apache Tomcat 11 (Jakarta EE 11; Tomcat 9/10 will not work)
- MsSQL Server 2019 or later
- Maven 3.9+

## Build
```sh
mvn clean package
```
Deploy `target/aspi-1.0.0.war` (rename it to `aspi.war` to keep the `/aspi` context path).

## Deployment/Installation Process
```sh
1. Install jdk-25
2. Set JAVA_HOME and the java environment variable
3. Crystal Reports PDF export needs at least one .ttf font in <JDK 25 folder>\lib\fonts,
   which JDK 9+ no longer ships. The application creates it on startup (copying a font from
   C:\Windows\Fonts). If the log says it could not, create the folder as administrator and
   copy a font into it (for example C:\Windows\Fonts\arial.ttf), then restart Tomcat.
4. Install MsSQL Server 2019
5. Install Apache Tomcat 11 and select the JDK 25 folder during installation
6. Change the default port of Tomcat 11 during install or after install
7. Add these Java options to Tomcat
   (Tomcat11w.exe > Java tab > Java Options, one per line, or CATALINA_OPTS in bin\setenv.bat):
      --enable-native-access=ALL-UNNAMED
      --add-exports=java.desktop/sun.awt.image=ALL-UNNAMED
8. Deploy aspi.war file into Tomcat 11
9. Expose tomcat port for publicly access
```

## Crystal Reports
- Report files (`.rpt`) are bundled in `src/main/resources/static/cr/v2`.
- A business can override them with its own folder (`xrptpath` / `xrptdefautl` in `zbusiness`);
  the bundled file is used when the file is not found there.
- The Crystal Reports runtime jars are in `src/main/webapp/WEB-INF/lib` (packaged into the war)
  and in `libs/` (to install into the local Maven repository for compiling).
- The SQL Server JDBC URL needs `encrypt=false` (or a trusted certificate), because
  mssql-jdbc 10+ encrypts connections by default. Crystal uses the same URL.
