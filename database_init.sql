-- Creates the vegobjekter table

CREATE TABLE vegobjekter (
  vegobjekt int UNIQUE NOT NULL,
  type_id int,
  versjonid int,
  strekningsbeskrivelse varchar(128),
  kommune_id int,
  fylke_id int,
  region_id int,
  vegkategori varchar(6),
  vegnummer int
);

NEWQUERY
-- Creates the region table
CREATE TABLE region (
  id int UNIQUE NOT NULL,
  name varchar(128)
);
NEWQUERY
-- Creates the region table
CREATE TABLE fylke (
  id int UNIQUE NOT NULL,
  name varchar(128)
);
NEWQUERY
-- Creates the region table
CREATE TABLE kommune (
  id int UNIQUE NOT NULL,
  name varchar(128)
);