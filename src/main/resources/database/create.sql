-- create database tables

-- Table : favoriteRestaurant
CREATE TABLE IF NOT EXISTS favoriteRestaurant(USERNAME VARCHAR(100), 
											  RESTAURANTID VARCHAR(100), 
											  CONSTRAINT pk_fav PRIMARY KEY (USERNAME, RESTAURANTID));

-- Table : userArea
CREATE TABLE IF NOT EXISTS userArea (USERNAME VARCHAR(500), 
									 AREANAME VARCHAR(200), 
									 CONSTRAINT pk_userarea PRIMARY KEY (USERNAME, AREANAME));

-- Table : pathFlux
CREATE TABLE IF NOT EXISTS pathFlux (URLFLUX VARCHAR(500) PRIMARY KEY, 
									 AREANAME VARCHAR(200));

-- Table : nutritionPreferences
CREATE TABLE IF NOT EXISTS nutritionPreferences (USERNAME varchar(100), 
												 NUTRITIONCODE INTEGER, 
												 CONSTRAINT pk_nutrition PRIMARY KEY (USERNAME, NUTRITIONCODE));