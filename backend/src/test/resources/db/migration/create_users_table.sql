CREATE TABLE IF NOT EXISTS "user"
(
    id                UUID                     NOT NULL DEFAULT random_uuid(),
    name              VARCHAR(120)             NOT NULL,
    username          VARCHAR(50) UNIQUE       NOT NULL,
    email             VARCHAR(120) UNIQUE      NOT NULL,
    password          VARCHAR(255)             NOT NULL,
    birthday          TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at        TIMESTAMP WITH TIME ZONE          DEFAULT NULL,
    email_verified_at TIMESTAMP WITH TIME ZONE          DEFAULT NULL,
    deleted_at        TIMESTAMP WITH TIME ZONE          DEFAULT NULL,
    created_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);