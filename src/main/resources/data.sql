-- SQL -Script for testing.

-- Ids start at 100 for Testing, JPA will start at Id 1.

-- DEV MAIN APP Start
--INSERT INTO Author (id, first_name, last_name) VALUES (100, 'John','Doha');
--INSERT INTO Author (id, first_name, last_name) VALUES (200, 'Daniel','Smith');
--INSERT INTO Author (id, first_name, last_name) VALUES (300, 'Anna','Muller');

--INSERT INTO Document (id, body, title) VALUES (100, 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.','Lorem Ipsum');
--INSERT INTO Document (id, body, title) VALUES (200, 'It always seems impossible until it''s done.','Poem');
--INSERT INTO Document (id, body, title) VALUES (300, 'Build web, including RESTful, applications using Spring MVC. Uses Apache Tomcat as the default embedded container.','Spring Web');
--INSERT INTO Document (id, body, title) VALUES (400, 'Persist data in SQL stores with Java Persistence API using Spring Data and Hibernate.','Spring Data JPA');
--INSERT INTO Document (id, body, title) VALUES (500, '','Book 4');

--INSERT INTO Document_Author (document_id, author_id) VALUES (100, 100);
--INSERT INTO Document_Author (document_id, author_id) VALUES (200, 200);
--INSERT INTO Document_Author (document_id, author_id) VALUES (300, 300);
--INSERT INTO Document_Author (document_id, author_id) VALUES (400, 100);
--INSERT INTO Document_Author (document_id, author_id) VALUES (100, 200);
--INSERT INTO Document_Author (document_id, author_id) VALUES (200, 300);

--INSERT INTO DOCUMENT_REFERENCE (document_id, reference_id) VALUES (100, 200);
--INSERT INTO DOCUMENT_REFERENCE (document_id, reference_id) VALUES (200, 300);
--INSERT INTO DOCUMENT_REFERENCE (document_id, reference_id) VALUES (200, 400);
-- DEV MAIN APP END

-- for spring security, only for testing purpose!!
-- create users table
CREATE TABLE USERS (
    USERNAME VARCHAR(50) UNIQUE PRIMARY KEY,
    PASSWORD VARCHAR(255) NOT NULL,
    ENABLED TINYINT NOT NULL
);
-- insert users for authentication
INSERT INTO USERS (USERNAME, PASSWORD, ENABLED) VALUES ('daniel','{noop}test123',1);
INSERT INTO USERS (USERNAME, PASSWORD, ENABLED) VALUES ('anna','{noop}test123',1);

-- create table for "roles"
CREATE TABLE AUTHORITIES (
    USERNAME VARCHAR(50) NOT NULL,
    AUTHORITY VARCHAR(50) NOT NULL,
    UNIQUE (USERNAME, AUTHORITY),
    FOREIGN KEY (USERNAME) REFERENCES USERS(USERNAME)
);

-- insert roles for users
INSERT INTO AUTHORITIES (USERNAME,AUTHORITY) VALUES ('daniel','ROLE_ADMIN');
INSERT INTO AUTHORITIES (USERNAME,AUTHORITY) VALUES ('daniel','ROLE_AUTHOR');
INSERT INTO AUTHORITIES (USERNAME,AUTHORITY) VALUES ('anna','ROLE_AUTHOR');
