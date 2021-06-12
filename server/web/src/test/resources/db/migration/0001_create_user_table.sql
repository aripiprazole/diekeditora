create table if not exists "user"
(
    id                uuid                     not null default random_uuid(),
    name              varchar(120)             not null,
    username          varchar(50) unique       not null,
    email             varchar(120) unique      not null,
    password          varchar(255)                      default null,
    birthday          timestamp with time zone          default null,
    updated_at        timestamp with time zone          default null,
    email_verified_at timestamp with time zone          default null,
    deleted_at        timestamp with time zone          default null,
    created_at        timestamp with time zone not null default current_timestamp,
    primary key (id)
);