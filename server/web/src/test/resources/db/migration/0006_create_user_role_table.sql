create table if not exists "user_role"
(
    user_id uuid not null,
    role_id uuid not null,
    primary key (user_id, role_id)
);
