create table if not exists "role_authority"
(
    role_id      uuid not null,
    authority_id uuid not null,
    primary key (role_id, authority_id),

    constraint fk_role_id
        foreign key (role_id)
            references role (id),

    constraint fk_authority_id
        foreign key (authority_id)
            references authority (id)
);
