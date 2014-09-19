# Esup-Dining

This app aims to deliver to student the meals available in dining halls.

You will be able to :
+ Display dining hall in differents area
+ See meals and dishes form each dining halls
+ Nutrition information about dishes
+ Your user can have specific configuration such as favorite dining hall or nutrition preferences

## Datas

You will need to insert JSON data for this app to work, you can watch the [JSON schema](https://github.com/gsouquet/esup-dining/blob/master/src/main/resources/schema/portlet-schema.json) to see how you will need to format your datas.

## Testing

You can test this app with the jetty plugin.

Please use HSQL for test purpose, in `src/main/resources/defaults.properties` use this configuration

```
auth.bean=OfflineFixedUserAuthenticationService

[...]

db.driver=org.hsqldb.jdbcDriver
db.infos=jdbc:hsqldb:file:restaurant
db.username=sa
db.password=
```

Then just type in a command line interface 

```
mvn clean package jetty:run
```

The server will be launched and available at `http://localhost:8080/pluto`

## Deployment and configuration

### Database

Configure your database as said in the Testing chapter. 

You can perform multiple actions on the database using a maven goal,

```
mvn exec:java -Dexec.args="database" -Dexec.args="[arg]"
```

`[arg]` can take this values `create`, `update`, `delete`, `drop`

Each of these arguments refers to a `*.sql` file located in `src/main/resources/database/*.sql`

### Configuration

Click on EDIT. To configure the app the user will to have `diningPortletAdmin` role. 
TODO

Then you will see an `Administrator configuration` link, just click it and type in the input your feed URL.
After that select the default area for your users, and that's it. All done.