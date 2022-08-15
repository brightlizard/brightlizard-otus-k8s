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
    totalPrice FLOAT NOT NULL,
    requestId VARCHAR NOT NULL,
    UNIQUE (id, requestId, consumer)
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

-- PAYMENT Service
DROP TABLE IF EXISTS public.consumer_account;
CREATE TABLE IF NOT EXISTS public.item (
   consumer_id VARCHAR PRIMARY KEY,
   balance VARCHAR NOT NULL,
   status VARCHAR NOT NULL
);

-- DELIVERY Service