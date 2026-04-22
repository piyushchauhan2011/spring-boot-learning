CREATE TABLE users (
    id             BIGSERIAL PRIMARY KEY,
    username       VARCHAR(100) NOT NULL UNIQUE,
    password_hash  VARCHAR(255) NOT NULL,
    enabled        BOOLEAN      NOT NULL DEFAULT true,
    created_at     TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE user_roles (
    user_id BIGINT       NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    role    VARCHAR(50)  NOT NULL,
    PRIMARY KEY (user_id, role)
);
