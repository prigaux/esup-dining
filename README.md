# ESUP-Restaurant

This portlet aims to deliver to student the meals available in dining halls.

## Datas

You will need to insert JSON data for this portlet to work, you can watch the [JSON schema](https://github.com/gsouquet/RestaurantPortlet/blob/master/restaurantportlet-web-springmvc-portlet/src/main/resources/schema/portlet-schema.json) to see how you will need to format your datas.

## Testing

You can test this portlet with Pluto portlet prototyping which is a jetty plugin.

Please use HSQL for test purpose, in `restaurantportlet-web-springmvc-portlet/src/main/resources/defaults.properties` use this configuration

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
cd restaurantportlet-web-springmvc-portlet/
mvn clean package portlet-prototyping:run
```

The server will be launched and available at `http://localhost:8080/pluto`