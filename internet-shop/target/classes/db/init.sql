DROP TABLE IF EXISTS public.order;
CREATE TABLE IF NOT EXISTS public.order (
    id VARCHAR PRIMARY KEY,
    username VARCHAR NOT NULL,
    firstName VARCHAR NOT NULL,
    lastName VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    phone VARCHAR NOT NULL,
    UNIQUE (username, email)
)
