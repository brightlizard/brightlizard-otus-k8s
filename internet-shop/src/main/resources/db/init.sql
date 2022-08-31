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


-- STORAGE Service
DROP TABLE IF EXISTS public.item;
CREATE TABLE IF NOT EXISTS public.item (
    id VARCHAR PRIMARY KEY,
    name VARCHAR NOT NULL,
    price FLOAT NOT NULL,
    quantity INTEGER NOT NULL,
    UNIQUE (name)
);

INSERT INTO item (id, name, price, quantity) VALUES 
('cabc1e54-b694-4117-8782-cacae7a60fcc', 'Majesty Skis Warewolf 182', 43000.0, 5),
('3b2d021f-4c59-4988-bfb8-29e348ce8a84', 'Fox Youth Comp Boots', 16000.0, 7),
('f4f5b142-3d73-4b32-b876-4e9baace0dd2', 'Full Tilt 120 Alpine Boots', 27500.0, 2),
('b8e4fd90-abb4-4cb1-92bf-452c91a757a0', 'Seba Skates FRX', 14600.0, 1),
('07b676b3-5953-471d-9f20-26c48fe0bab1', 'Something Awesome', 500.0, 143),
('92e59fbf-d6e1-4636-bc3b-825cc7390be8', 'Cheapest Thing', 15.0, 1856);

DROP TABLE IF EXISTS public.reserved_item;
CREATE TABLE IF NOT EXISTS public.reserved_item (
   id VARCHAR PRIMARY KEY,
   name VARCHAR NOT NULL,
   price FLOAT NOT NULL,
   quantity INTEGER NOT NULL,
   UNIQUE (name)
);

-- PAYMENT Service


-- BILLING Service
DROP TABLE IF EXISTS public.customer CASCADE;
CREATE TABLE IF NOT EXISTS public.customer (
  id VARCHAR PRIMARY KEY,
  name VARCHAR NOT NULL,
  status VARCHAR NOT NULL
);

INSERT INTO customer(id, name, status) VALUES
('00000000-0000-0000-00000000', 'Awesome customer', 'ACTIVE');

DROP TABLE IF EXISTS public.account;
CREATE TABLE IF NOT EXISTS public.account (
   id VARCHAR PRIMARY KEY,
   customer_id VARCHAR NOT NULL references public.customer(id),
   balance FLOAT NOT NULL,
   status VARCHAR NOT NULL
);

INSERT INTO account(id, customer_id, balance, status) VALUES
('10000000-0000-0000-00000001', '00000000-0000-0000-00000000', 526000.00, 'ACTIVE');

-- DELIVERY Service