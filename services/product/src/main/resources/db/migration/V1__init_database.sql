CREATE TABLE IF NOT EXISTS category
(
    id integer NOT NULL primary key ,
    description varchar(255),
    name varchar(255)
);

CREATE TABLE IF NOT EXISTS product
(
    id integer NOT NULL primary key ,
    description varchar(255),
    name varchar(255),
    available_quantity double precision not null ,
    price numeric(38, 2),
    category_id integer constraint product_category_id_fk references category

);

CREATE SEQUENCE IF NOT EXISTS  category_seq increment by 50;
CREATE SEQUENCE IF NOT EXISTS  product_seq increment by 50;