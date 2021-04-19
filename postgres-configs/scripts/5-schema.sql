-- --################################################################
-- --###                           VUKA WIRELESS SCHEMA 
-- --################################################################

-- -- CREATE EXTENSION IF NOT EXISTS "pgcrypto" WITH SCHEMA vuka_wireless;
-- -- CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA vuka_wireless;

-- create table vuka_wireless.mpesa_express(
--         id                      bigserial, 
--         account_reference       varchar(255) not null,
--         amount                  float not null, 
--         checkout_request_id     varchar(255) not null unique,
--         customer_message        varchar(255),
--         merchant_request_id     varchar(255) not null unique,
--         mpesa_receipt_number    varchar(255),
--         msisdn                  varchar(255) not null,
--         payment_status          varchar(255),
--         request_type            varchar(255),   --nullable ?
--         response_code           integer not null,
--         response_description    varchar(255),
--         result_code             integer,
--         result_description      varchar(255),
--         service_payment_status  varchar(255),
--         service_request_status  varchar(255),
--         service_type            varchar(255),
--         short_code              integer not null,

--         wallet_id               bigint not null,

--         transaction_type        varchar(255) not null,
--         transaction_description varchar(255) not null,
--         transaction_date        timestamp default CURRENT_TIMESTAMP,
        
--         created_at              timestamp default CURRENT_TIMESTAMP,
--         update_at               timestamp default CURRENT_TIMESTAMP,
--         version                 bigint default 0

-- );

