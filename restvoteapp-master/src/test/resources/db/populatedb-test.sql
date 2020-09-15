DELETE FROM VOTE;
DELETE FROM USER_ROLES;
DELETE FROM MENU;
DELETE FROM USERS;
DELETE FROM RESTAURANT;

ALTER SEQUENCE GLOBAL_SEQ RESTART WITH 100000;

INSERT INTO USERS(NAME, EMAIL, PASSWORD)
VALUES ('user1', 'user1@example.com', '{noop}user1'),       --100000
       ('user2', 'user2@example.com', '{noop}user2'),       --100001
       ('admin1', 'admin1@example.com', '{noop}admin1');    --100002

INSERT INTO USER_ROLES (role, user_id)
VALUES ('USER', 100000),
       ('USER', 100001),
       ('USER', 100002),
       ('ADMIN', 100002);

INSERT INTO RESTAURANT (NAME)
VALUES ('REST1'),       --100003
       ('REST2');       --100004

INSERT INTO MENU (MENUDATE, RESTAURANT_ID, DISHES)
VALUES (current_date, 100003 ,'{"data":"rest1"}'),  --100005
       (current_date, 100004 ,'{"data":"rest2"}');  --100006

INSERT INTO VOTE (VOTEDATE, VOTETIME, RESTAURANT_ID, USER_ID)
VALUES (current_date, '10:00:00', 100003, 100000), --100007
       (current_date, '10:00:00', 100004, 100002); --100008