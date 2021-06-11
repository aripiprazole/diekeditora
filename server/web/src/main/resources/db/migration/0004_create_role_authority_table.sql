create table if not exists "role_authority"
(
    role_id   uuid        not null,
    authority varchar(80) not null,
    primary key (role_id),

    constraint fk_role_id
        foreign key (role_id)
            references role (id)
);
