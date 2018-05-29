insert into user_state (id, name) values (1, 'NORMAL');
insert into user_state (id, name) values (2, 'WITHDRAW');
insert into user_state (id, name) values (3, 'NORMAL');

insert into roles (id, name) values (1, 'ADMIN');
insert into roles (id, name) values (2, 'USER');

insert into users (id, email, password, nickname, summoner, state_edit_date, user_state_id) values (1, 'test1@test.com', '{noop}12345', '개고수', '페이커', current_timestamp, 1);
insert into users (id, email, password, nickname, summoner, state_edit_date, user_state_id) values (2, 'test2@test.com', '{noop}12345', '초고수', '전국일등', current_timestamp, 1);
insert into users (id, email, password, nickname, summoner, state_edit_date, user_state_id) values (3, 'test3@test.com', '{noop}12345', '롤짱짱맨', '내가짱이다', current_timestamp, 1);
insert into users (id, email, password, nickname, summoner, state_edit_date, user_state_id) values (4, 'test4@test.com', '{noop}12345', '내가챌린저다', '개초보', current_timestamp, 1);
insert into users (id, email, password, nickname, summoner, state_edit_date, user_state_id) values (5, 'test5@test.com', '{noop}12345', '트롤러', '트롤러', current_timestamp, 1);

