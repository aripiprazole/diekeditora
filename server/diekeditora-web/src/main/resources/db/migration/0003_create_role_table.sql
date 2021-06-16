create table if not exists "role"
(
    id         uuid                     not null default gen_random_uuid(),
    name       varchar(80)              not null,
    deleted_at timestamp with time zone          default null,
    created_at timestamp with time zone not null default current_timestamp,
    primary key (id)
);
