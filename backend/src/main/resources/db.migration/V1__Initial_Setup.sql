create table users (
    id integer not null,
    email varchar(255) not null,
    full_name varchar(255) not null,
    location varchar(255) not null,
    password varchar(255) not null,
    username varchar(255) not null unique,
    primary key (id),
    constraint user_email_unique unique (email)
);