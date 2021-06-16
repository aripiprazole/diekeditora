create table if not exists "user_authority"
(
    user_id   uuid        not null,
    authority varchar(80) not null
);
