create table if not exists "role_authority"
(
    role_id      uuid not null,
    authority_id uuid not null,
    primary key (role_id, authority_id)
);