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

create table if not exists "role"
(
    id         uuid                     not null default random_uuid(),
    name       varchar(80)              not null,
    created_at timestamp with time zone not null default current_timestamp,
    updated_at timestamp with time zone          default null,
    primary key (id)
);

create table if not exists "authority"
(
    id         uuid                     not null default random_uuid(),
    value      varchar(120)             not null,
    created_at timestamp with time zone not null default current_timestamp
);

create table if not exists "role_authority"
(
    role_id      uuid                     not null,
    authority_id uuid                     not null,
    created_at   timestamp with time zone not null default current_timestamp
);

create table if not exists "user_authority"
(
    user_id      uuid                     not null,
    authority_id uuid                     not null,
    created_at   timestamp with time zone not null default current_timestamp
);

create table if not exists "user_role"
(
    user_id    uuid                     not null,
    role_id    uuid                     not null,
    created_at timestamp with time zone not null default current_timestamp
);
