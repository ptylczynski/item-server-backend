insert into user(account_non_expired, account_non_locked, credentials_non_expired, enabled, mail, password, username)
values (true, true, true, true, 'test@test.com','$2y$12$cygBQnl2TCjZQWIy.lmyeuYikHl0f5TazZyBtNv44y2V/hooo8/P6' , 'test');

insert into user(account_non_expired, account_non_locked, credentials_non_expired, display_name, enabled, mail, password, username)
values (true, true, true, 'Piotr 2 Tylczynski', true, 'test2@test', '$2y$12$cygBQnl2TCjZQWIy.lmyeuYikHl0f5TazZyBtNv44y2V/hooo8/P6', 'test2');

insert into item_type(description, name)
    values ('test description', 'type 1');

insert into item_type(description, name)
    values ('test description 21', 'type 3');

select * from user;

ALTER DATABASE item_server CHARACTER SET utf8mb4 COLLATE utf8_general_ci;