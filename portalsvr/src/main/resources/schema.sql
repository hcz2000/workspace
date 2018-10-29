DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS user_orgs;
DROP TABLE IF EXISTS clients;

CREATE  TABLE users (
  user_name VARCHAR(100) NOT NULL ,
  password VARCHAR(100) NOT NULL ,
  enabled boolean NOT NULL ,
  PRIMARY KEY (user_name));

CREATE TABLE user_roles (
  user_role_id SERIAL,
  user_name varchar(100) NOT NULL,
  role varchar(100) NOT NULL,
  PRIMARY KEY (user_role_id));

CREATE TABLE user_orgs (
  organization_id   VARCHAR(100)  NOT NULL,
  user_name         VARCHAR(100)   NOT NULL,
  PRIMARY KEY (user_name));
  
CREATE TABLE clients (
  client_id varchar(48) NOT NULL,
  resource_ids varchar(256) DEFAULT NULL,
  client_secret varchar(256) DEFAULT NULL,
  scope varchar(256) DEFAULT NULL,
  authorized_grant_types varchar(256) DEFAULT NULL,
  web_server_redirect_uri varchar(256) DEFAULT NULL,
  authorities varchar(256) DEFAULT NULL,
  access_token_validity int(11) DEFAULT NULL,
  refresh_token_validity int(11) DEFAULT NULL,
  additional_information varchar(4096) DEFAULT NULL,
  autoapprove varchar(256) DEFAULT NULL,
  PRIMARY KEY (`client_id`)
);

INSERT INTO users(user_name,password,enabled) VALUES ('john.carnell','$2a$04$NX3QTkBJB00upxKeaKqFBeoIVc9JHvwVnj1lItxNphRj34wNx5wlu', true);
INSERT INTO users(user_name,password,enabled) VALUES ('william.woodward','$2a$04$lM2hIsZVNYrQLi8mhvnTA.pheZtmzeivz6fyxCr9GZ6YSfP6YibCW', true);

INSERT INTO user_roles (user_name, role) VALUES ('john.carnell', 'ROLE_USER');
INSERT INTO user_roles (user_name, role) VALUES ('william.woodward', 'ROLE_ADMIN');
INSERT INTO user_roles (user_name, role) VALUES ('william.woodward', 'ROLE_USER');

INSERT INTO user_orgs (organization_id, user_name) VALUES ('d1859f1f-4bd7-4593-8654-ea6d9a6a626e', 'john.carnell');
INSERT INTO user_orgs (organization_id, user_name) VALUES ('42d3d4f5-9f33-42f4-8aca-b7519d6af1bb', 'william.woodward');

INSERT INTO clients VALUES ('eagleeye', null, 'thisissecret', 'webclient|mobileclient', 'refresh_token|password|client_credentials|authorization_code|implicit', null, null, 2592000, 2592000, null, null);