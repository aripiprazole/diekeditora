create table if not exists "user_authority"
(
    user_id      uuid not null,
    authority_id uuid not null,

    constraint fk_user_id
        foreign key (user_id)
            references "user" (id),

    constraint fk_authority_id
        foreign key (authority_id)
            references authority (id)
);
