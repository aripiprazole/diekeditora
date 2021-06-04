create table if not exists "user_authority"
(
    user_id      uuid not null,
    authority_id uuid not null,
    primary key (user_id, authority_id)
);
