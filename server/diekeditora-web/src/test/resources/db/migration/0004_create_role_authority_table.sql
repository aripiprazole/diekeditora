create table if not exists "role_authority"
(
    role_id   uuid        not null,
    authority varchar(80) not null
);
