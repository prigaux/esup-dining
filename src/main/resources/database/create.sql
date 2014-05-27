--
-- Licensed to ESUP-Portail under one or more contributor license
-- agreements. See the NOTICE file distributed with this work for
-- additional information regarding copyright ownership.
--
-- ESUP-Portail licenses this file to you under the Apache License,
-- Version 2.0 (the "License"); you may not use this file except in
-- compliance with the License. You may obtain a copy of the License at:
--
-- http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

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