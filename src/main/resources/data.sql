insert into user_state (id, name) values (1, 'NORMAL');
insert into user_state (id, name) values (2, 'WITHDRAW');
insert into user_state (id, name) values (3, 'SUSPENSION');

insert into roles (id, name) values (1, 'ADMIN');
insert into roles (id, name) values (2, 'USER');

insert into users (id, email, password, nickname, summoner, state_edit_date, user_state_id) values (1, 'test1@test.com', '{noop}12345', '개고수', '페이커', current_timestamp, 1);
insert into users (id, email, password, nickname, summoner, state_edit_date, user_state_id) values (2, 'test2@test.com', '{noop}12345', '초고수', '전국일등', current_timestamp, 1);
insert into users (id, email, password, nickname, summoner, state_edit_date, user_state_id) values (3, 'test3@test.com', '{noop}12345', '롤짱짱맨', '내가짱이다', current_timestamp, 1);
insert into users (id, email, password, nickname, summoner, state_edit_date, user_state_id) values (4, 'test4@test.com', '{noop}12345', '내가챌린저다', '개초보', current_timestamp, 2);
insert into users (id, email, password, nickname, summoner, state_edit_date, user_state_id) values (5, 'test5@test.com', '{noop}12345', '트롤러', '트롤러', current_timestamp, 3);

insert into users_roles(users_id, roles_id) values (1, 1);
insert into users_roles(users_id, roles_id) values (1, 2);
insert into users_roles(users_id, roles_id) values (2, 2);
insert into users_roles(users_id, roles_id) values (3, 2);
insert into users_roles(users_id, roles_id) values (4, 2);
insert into users_roles(users_id, roles_id) values (5, 2);

insert into friends(id, users_id, summoner) values (1, 1, '짱짱맨');
insert into friends(id, users_id, summoner) values (2, 1, '페이커');
insert into friends(id, users_id, summoner) values (3, 1, 'Killua fans');
insert into friends(id, users_id, summoner) values (4, 1, 'GabrielCRO');
insert into friends(id, users_id, summoner) values (5, 1, '너희 정말 돼지야');
insert into friends(id, users_id, summoner) values (6, 1, 'Hide on bush');
insert into friends(id, users_id, summoner) values (7, 1, 'SKT T1 Bang');
insert into friends(id, users_id, summoner) values (8, 1, 'MVP ADD');
insert into friends(id, users_id, summoner) values (9, 1, 'DWG ShowMaker');
insert into friends(id, users_id, summoner) values (10, 1, '건들면총든다');
insert into friends(id, users_id, summoner) values (11, 1, '뉴트 주니어');
insert into friends(id, users_id, summoner) values (12, 1, 'Gen G CoreJJ');
insert into friends(id, users_id, summoner) values (13, 1, '이 차가 식기전에');