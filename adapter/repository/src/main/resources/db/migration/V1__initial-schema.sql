create table game(
	id bigint not null,
	game jsonb,

	primary key(id)
);

create sequence game_id as bigint;

create table user_record(
    id varchar(50) not null,
    user_json jsonb,

    primary key(id)
);