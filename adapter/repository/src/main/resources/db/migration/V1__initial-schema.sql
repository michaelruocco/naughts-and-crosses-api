create table game(
	id bigint not null,
	game jsonb,

	primary key(id)
);

create sequence game_id as bigint;