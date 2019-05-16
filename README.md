# ActivePivot Performance Optimisation Course

The username/password to log into ActivePivot is `admin/admin`

## Pivot
### To build
Run `mvn clean install` to build the project.

### Running
This project by default creates a fat jar file of all the dependencies, with an embedded jetty server. You can then run application with `java -jar pivot-<version>.jar`. You can specify any Java properties or arguments, for example for setting the data directory and memory options:

`java -jar -Dsource.data.root=/home/activeviam/data/ -Xms64G -Xmx64G -XX:MaxDirectMemorySize=64G pivot-<version>.jar`

From an IDE, you will need to run the class `com.activeviam.training.main.ActivePivotServer`.

ActivePivot runs on port 9090 by default.

### Profiles
There are two Spring profiles defined:
- `standard` - what we will use for the most of the exercises
- `vectorised` - loads vectorised data for the final exercise (not all data is ready yet!)

You can change profiles by calling `-Dspring.profiles.active=<profile>`

### Property Files
By default, all properties files are on the classpath and can be found in `pivot/src/main/resources`. However, it is possible to read from an external location for `app`, `local-csv`, etc properties files. More details can be found in `EnvironmentConstants`, but here is a quick example:
`-Dapp.properties.file.path=file:/home/etc/app.properties`

## UI
ActiveUI the app v4.2.8 is embedded into the project. It is available by default on `http://localhost:9090/ui`.

## Content Server
This projects uses a local content server. By default it is in-memory, however you change the settings in `hibernate.properties`.
