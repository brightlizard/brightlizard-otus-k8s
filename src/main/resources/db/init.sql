DROP TABLE IF EXISTS public.users;
CREATE TABLE IF NOT EXISTS public.users (
    id VARCHAR PRIMARY KEY,
    username VARCHAR NOT NULL,
    firstName VARCHAR NOT NULL,
    lastName VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    phone VARCHAR NOT NULL,
    UNIQUE (username, email)
)
