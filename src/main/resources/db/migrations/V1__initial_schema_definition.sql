CREATE TABLE players
(
  player_id serial PRIMARY KEY NOT NULL,
  first_name varchar(255),
  is_active boolean NOT NULL,
  last_name varchar(255),
  nickname varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  role varchar(255),
  side integer,
  type varchar(255)
);

alter table players add constraint players_nickname unique (nickname)