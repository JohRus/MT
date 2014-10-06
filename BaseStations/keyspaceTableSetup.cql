CREATE KEYSPACE if not exists celltowers WITH REPLICATION = {'class':'SimpleStrategy', 'replication_factor':1};

use celltowers;

CREATE TABLE if not exists measurements (
//id uuid PRIMARY KEY,
mcc int,
net int,
area int,
cell bigint,
lon double,
lat double,
signal int,
measured timestamp,
created timestamp,
rating double,
speed double,
direction double,
radio text,
ta int,
rnc int,
cid int,
psc int,
tac int,
pci int,
sid int,
nid int,
bid int,
frommeasurements int,
PRIMARY KEY (mcc, net, area, cell, lon, lat, measured)
);