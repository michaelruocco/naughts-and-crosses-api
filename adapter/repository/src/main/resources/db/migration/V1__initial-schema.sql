create table game(
	id bigint not null,
	game_json jsonb not null,
	complete boolean not null,

	primary key(id)
);

create sequence game_id as bigint;

create table user_record(
    username varchar(50) not null,
    id varchar(100) not null,
    name varchar(200),
    first_name varchar(100),
    last_name varchar(100),
    email varchar(100) not null,
    email_verified boolean not null,
    status varchar(100) not null,

    primary key(username)
);

create table user_group(
    username varchar(50) not null,
    group_name varchar(100) not null,

    primary key(username, group_name),

    foreign key(username) references user_record(username) on delete cascade
);

create table user_batch(
    id varchar(50) not null,
    created_at timestamp not null,
    batch_json jsonb,

    primary key(id)
);