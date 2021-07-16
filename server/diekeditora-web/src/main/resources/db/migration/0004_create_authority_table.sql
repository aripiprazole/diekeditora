create table if not exists "authority"
(
    id         uuid                     not null default gen_random_uuid(),
    name       varchar(120)             not null,
    created_at timestamp with time zone not null default current_timestamp
);
