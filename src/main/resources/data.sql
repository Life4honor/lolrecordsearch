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

insert into summoners(id, name, summoner_level, account_id) values(3710925, '겁나빠른꼬부기', 39, 2897488);
insert into match_references(id, game_id, champion, role) values(1, 3213293568, 81, 'DUO_CARRY');
insert into matches(id, match_reference_id, summoner_id) values(1,1,3710925);

insert into participants(game_id, participant_id, stats, team_id, champion_id) values(3213293568, 1, '', 100, 81);
insert into participants(game_id, participant_id, stats, team_id, champion_id) values(3213293568, 2, '', 100, 81);
insert into participants(game_id, participant_id, stats, team_id, champion_id) values(3213293568, 3, '', 100, 81);
insert into participants(game_id, participant_id, stats, team_id, champion_id) values(3213293568, 4, '', 100, 81);
insert into participants(game_id, participant_id, stats, team_id, champion_id) values(3213293568, 5, '', 100, 81);

insert into participants_identities(game_id, participant_id, player) values(3213293568, 1, '');
insert into participants_identities(game_id, participant_id, player) values(3213293568, 2, '');
insert into participants_identities(game_id, participant_id, player) values(3213293568, 3, '');
insert into participants_identities(game_id, participant_id, player) values(3213293568, 4, '');
insert into participants_identities(game_id, participant_id, player) values(3213293568, 5, '');