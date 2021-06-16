create table if not exists "role"
(
    id         uuid                     not null default random_uuid(),
    name       varchar(80)              not null,
    created_at timestamp with time zone not null default current_timestamp,
    updated_at timestamp with time zone          default null,
    primary key (id)
);
