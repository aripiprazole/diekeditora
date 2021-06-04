create table if not exists "authority"
(
    id    uuid         not null default gen_random_uuid(),
    value varchar(120) not null,
    primary key (id)
);
