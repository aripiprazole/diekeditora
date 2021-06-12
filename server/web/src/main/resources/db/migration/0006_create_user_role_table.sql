create table if not exists "user_role"
(
    user_id uuid not null,
    role_id uuid not null,
    primary key (user_id, role_id),

    constraint fk_user_id
        foreign key (user_id)
            references "user" (id),

    constraint fk_role_id
        foreign key (role_id)
            references role (id)
);
