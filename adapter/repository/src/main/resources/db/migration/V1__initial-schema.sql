create table game(
	id bigint not null,
	game_json jsonb,

	primary key(id)
);

create sequence game_id as bigint;

create table user_record(
    username varchar(50) not null,
    user_json jsonb,

    primary key(username)
);

create table user_batch(
    id varchar(50) not null,
    created_at timestamp not null,
    batch_json jsonb,

    primary key(id)
);