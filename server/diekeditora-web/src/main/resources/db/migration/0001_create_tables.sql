create table if not exists "user"
(
    id         uuid                     not null default gen_random_uuid(),
    name       varchar(120)             not null,
    username   varchar(50) unique       not null,
    email      varchar(120) unique      not null,
    birthday   timestamp with time zone          default null,
    updated_at timestamp with time zone          default null,
    deleted_at timestamp with time zone          default null,
    created_at timestamp with time zone not null default current_timestamp,
    primary key (id)
);

create table if not exists "role"
(
    id         uuid                     not null default gen_random_uuid(),
    name       varchar(80)              not null,
    deleted_at timestamp with time zone          default null,
    created_at timestamp with time zone not null default current_timestamp,
    primary key (id)
);

create table if not exists "authority"
(
    id         uuid                     not null default gen_random_uuid(),
    value      varchar(120) unique      not null,
    created_at timestamp with time zone not null default current_timestamp,
    primary key (id)
);

create table if not exists "role_authority"
(
    role_id      uuid                     not null,
    authority_id uuid                     not null,
    created_at   timestamp with time zone not null default current_timestamp,

    constraint fk_role_id
        foreign key (role_id)
            references role (id),

    constraint fk_authority_id
        foreign key (authority_id)
            references authority (id)
);

create table if not exists "user_authority"
(
    user_id      uuid                     not null,
    authority_id uuid                     not null,
    created_at   timestamp with time zone not null default current_timestamp,

    constraint fk_user_id
        foreign key (user_id)
            references "user" (id),

    constraint fk_authority_id
        foreign key (authority_id)
            references authority (id)
);

create table if not exists "user_role"
(
    user_id    uuid                     not null,
    role_id    uuid                     not null,
    created_at timestamp with time zone not null default current_timestamp,

    constraint fk_user_id
        foreign key (user_id)
            references "user" (id),

    constraint fk_role_id
        foreign key (role_id)
            references role (id)
);

do
'
begin
if not exists(select 1 from pg_type where typname = $$gender$$) then
    create type gender as enum ($$Male$$, $$Female$$, $$NonBinary$$);
    create cast (character varying as gender) with inout as assignment;
    end if;
--more types here...
end;
' language plpgsql;

create table if not exists "profile"
(
    id              uuid                     not null default gen_random_uuid(),
    gender          gender                   not null,
    uid             uuid unique              not null,
    owner_id        uuid unique              not null,
    created_at      timestamp with time zone not null default current_timestamp,
    updated_at      timestamp with time zone          default null,

    constraint fk_owner_id
        foreign key (owner_id)
            references "user" (id)
)