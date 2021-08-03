do
'
begin
if not exists(select 1 from pg_type where typname = $$gender$$) then
    create type gender as enum ($$Male$$, $$Female$$, $$NonBinary$$);
    create type report as enum ($$Spam$$, $$Harmful$$, $$Suicide$$, $$Other$$);

    create cast (character varying as gender) with inout as assignment;
    create cast (character varying as report) with inout as assignment;
    end if;
end;
' language plpgsql;

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

create table if not exists "profile"
(
    id         uuid unique              not null default gen_random_uuid(),
    gender     gender                   not null,
    uid        uuid unique              not null,
    owner_id   uuid unique              not null,
    created_at timestamp with time zone not null default current_timestamp,
    updated_at timestamp with time zone          default null,

    constraint fk_owner_id
        foreign key (owner_id)
            references "user" (id)
);

create table if not exists "manga"
(
    id         uuid unique              not null default gen_random_uuid(),
    uid        uuid unique              not null,
    title      varchar(120)             not null,
    competing  bool                     not null,
    summary    varchar(2000)            not null,
    advisory   int                      not null,
    created_at timestamp with time zone not null default current_timestamp,
    deleted_at timestamp with time zone not null default current_timestamp,
    updated_at timestamp with time zone          default null
);

create table if not exists "comment"
(
    id         uuid unique              not null default gen_random_uuid(),
    uid        uuid unique              not null,
    content    varchar(400)             not null,
    manga_id   uuid                     not null,
    created_at timestamp with time zone not null default current_timestamp,
    deleted_at timestamp with time zone not null default current_timestamp,
    updated_at timestamp with time zone          default null,

    constraint fk_manga_id
        foreign key (manga_id)
            references manga (id)
);

create table if not exists "genre"
(
    id         uuid unique              not null default gen_random_uuid(),
    title      varchar(120)             not null,
    created_at timestamp with time zone not null default current_timestamp,
    updated_at timestamp with time zone          default null
);

create table if not exists "chapter"
(
    id          uuid unique              not null default gen_random_uuid(),
    uid         uuid unique              not null,
    title       varchar(120)             not null,
    released_at timestamp with time zone,
    created_at  timestamp with time zone not null default current_timestamp,
    updated_at  timestamp with time zone          default null
);

create table if not exists "user_profile"
(
    user_id    uuid                     not null,
    profile_id uuid                     not null,
    created_at timestamp with time zone not null default current_timestamp,

    constraint fk_user_id
        foreign key (user_id)
            references "user" (id),

    constraint fk_profile_id
        foreign key (profile_id)
            references profile (id)
);

create table if not exists "manga_chapter"
(
    manga_id   uuid                     not null,
    chapter_id uuid                     not null,
    created_at timestamp with time zone not null default current_timestamp,

    constraint fk_manga_id
        foreign key (manga_id)
            references manga (id),

    constraint fk_genre_id
        foreign key (chapter_id)
            references chapter (id)
);

create table if not exists "manga_genre"
(
    manga_id   uuid                     not null,
    genre_id   uuid                     not null,
    created_at timestamp with time zone not null default current_timestamp,

    constraint fk_manga_id
        foreign key (manga_id)
            references manga (id),

    constraint fk_genre_id
        foreign key (genre_id)
            references genre (id)
);

create table if not exists "comment_author"
(
    comment_id uuid                     not null,
    user_id    uuid                     not null,
    created_at timestamp with time zone not null default current_timestamp,

    constraint fk_comment_id
        foreign key (comment_id)
            references comment (id),

    constraint fk_user_id
        foreign key (user_id)
            references "user" (id)
);

create table if not exists "manga_author"
(
    user_id    uuid                     not null,
    manga_id   uuid                     not null,
    created_at timestamp with time zone not null default current_timestamp,

    constraint fk_user_id
        foreign key (user_id)
            references "user" (id),

    constraint fk_manga_id
        foreign key (manga_id)
            references manga (id)
);

create table if not exists "comment_like"
(
    comment_id uuid                     not null,
    user_id    uuid                     not null,
    created_at timestamp with time zone not null default current_timestamp,

    constraint fk_comment_id
        foreign key (comment_id)
            references comment (id),

    constraint fk_user_id
        foreign key (user_id)
            references "user" (id)
);

create table if not exists "comment_report"
(
    comment_id uuid         not null,
    user_id    uuid         not null,
    report     varchar(120) not null,

    constraint fk_comment_id
        foreign key (comment_id)
            references comment (id),

    constraint fk_user_id
        foreign key (user_id)
            references "user" (id)
);
