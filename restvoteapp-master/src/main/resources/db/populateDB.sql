DELETE FROM VOTE;
DELETE FROM USER_ROLES;
DELETE FROM MENU;
DELETE FROM USERS;
DELETE FROM RESTAURANT;

ALTER SEQUENCE GLOBAL_SEQ RESTART WITH 100000;

INSERT INTO USERS(NAME, EMAIL, PASSWORD)
VALUES ('user1', 'user1@example.com', '{noop}user1'),
       ('user2', 'user2@example.com', '{noop}user2'),
       ('user3', 'user3@example.com', '{noop}user3'),
       ('user4', 'user4@example.com', '{noop}user4'),
       ('user5', 'user5@example.com', '{noop}user5'),
       ('user6', 'user6@example.com', '{noop}user6'),
       ('admin1', 'admin1@example.com', '{noop}admin1');

INSERT INTO USER_ROLES (role, user_id)
VALUES ('USER', 100000),
       ('USER', 100001),
       ('USER', 100002),
       ('USER', 100003),
       ('USER', 100004),
       ('USER', 100005),
       ('USER', 100006),
       ('ADMIN', 100006);

INSERT INTO RESTAURANT(NAME)
VALUES ('REST1'),
       ('REST2'),
       ('REST3');

INSERT INTO MENU(MENUDATE, RESTAURANT_ID, DISHES)
VALUES (current_date, 100007 ,'{"dishes":[{"description": "soup","price": "150"},{"description": "main dish","price":"300"},{"description": "drink","price": "150"}]}'),
       (current_date, 100008 ,'{"dishes":[{"description": "soup","price": "100"},{"description": "main dish","price":"200"},{"description": "drink","price": "100"}]}'),
       (current_date, 100009 ,'{"dishes":[{"description": "soup","price": "200"},{"description": "main dish","price":"350"},{"description": "drink","price": "200"}]}');

INSERT INTO VOTE (VOTEDATE, VOTETIME, RESTAURANT_ID, USER_ID)
VALUES (current_date, '10:00:00', 100008, 100000 ),
       (current_date, '09:00:00', 100007, 100001 ),
       (current_date, '10:00:00', 100008, 100002 ),
       (current_date, '10:00:00', 100008, 100003 ),
       (current_date, '10:00:00', 100007, 100004 ),
       (current_date, '10:00:00', 100007, 100005 );