# Tasks schema

# --- !Ups

CREATE TABLE HashToUrl  (
	hash integer,
	url  varchar(255)
);

# --- !Downs

DROP TABLE HashToUrl;