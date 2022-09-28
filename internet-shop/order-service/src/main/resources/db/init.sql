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
    customer VARCHAR NOT NULL,
    itemIds TEXT NOT NULL,
    status VARCHAR NOT NULL,
    commStatus VARCHAR NOT NULL,
    totalPrice FLOAT NOT NULL,
    requestId VARCHAR NOT NULL,
    deliveryTime VARCHAR NOT NULL,
    UNIQUE (id, requestId, customer)
);

DROP TABLE IF EXISTS public.short_item;
CREATE TABLE IF NOT EXISTS public.short_item (
    id VARCHAR NOT NULL,
    quantity INTEGER NOT NULL,
    orderid VARCHAR NOT NULL,
    UNIQUE(id, orderid)
);

DROP TABLE IF EXISTS public.ordered_item;
CREATE TABLE IF NOT EXISTS public.ordered_item (
   id VARCHAR NOT NULL ,
   name VARCHAR NOT NULL,
   price FLOAT NOT NULL,
   quantity INTEGER NOT NULL,
   orderid VARCHAR NOT NULL,
   UNIQUE (id, orderid)
);