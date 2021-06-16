create table if not exists "user_authority"
(
    user_id      uuid not null,
    authority varchar(80) not null,

    constraint fk_user_id
        foreign key (user_id)
            references "user" (id)
);
