-- ORDER Service
DROP TABLE IF EXISTS public.order_request;
CREATE TABLE IF NOT EXISTS public.order_request (
    requestId VARCHAR NOT NULL,
    orderHash VARCHAR NULL,
    UNIQUE (requestId)
);

DROP TABLE IF EXISTS public.order;
CREATE TABLE IF NOT EXISTS public.order (
    id VARCHAR PRIMARY KEY,
    consumer VARCHAR NOT NULL,
    itemIds TEXT NOT NULL,
    status VARCHAR NOT NULL,
    status VARCHAR NOT NULL,
    commStatus VARCHAR NOT NULL,
    totalPrice FLOAT NOT NULL,
    requestId VARCHAR NOT NULL,
    UNIQUE (id, requestId, consumer)
);

DROP TABLE IF EXISTS public.short_item;
CREATE TABLE IF NOT EXISTS public.short_item (
    id VARCHAR PRIMARY KEY,
    quantity INTEGER NOT NULL
);

DROP TABLE IF EXISTS public.ordered_item;
CREATE TABLE IF NOT EXISTS public.ordered_item (
   id VARCHAR PRIMARY KEY,
   name VARCHAR NOT NULL,
   price FLOAT NOT NULL,
   quantity INTEGER NOT NULL,
   UNIQUE (name)
);


-- STORAGE Service
DROP TABLE IF EXISTS public.item;
CREATE TABLE IF NOT EXISTS public.item (
    id VARCHAR PRIMARY KEY,
    name VARCHAR NOT NULL,
    price FLOAT NOT NULL,
    quantity INTEGER NOT NULL,
    UNIQUE (name)
);

DROP TABLE IF EXISTS public.reserved_item;
CREATE TABLE IF NOT EXISTS public.reserved_item (
   id VARCHAR PRIMARY KEY,
   name VARCHAR NOT NULL,
   price FLOAT NOT NULL,
   quantity INTEGER NOT NULL,
   UNIQUE (name)
);

-- PAYMENT Service
DROP TABLE IF EXISTS public.consumer_account;
CREATE TABLE IF NOT EXISTS public.item (
   consumer_id VARCHAR PRIMARY KEY,
   balance VARCHAR NOT NULL,
   status VARCHAR NOT NULL
);

-- DELIVERY Service