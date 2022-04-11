create table if not exists supply_center(
 supply_center varchar(20) not null primary key,
 director varchar(20) not null
);

create table if not exists contract(
  contract_number varchar(12) not null primary key,
  contract_date varchar(12) unique not null,
  client_enterprise varchar(35) unique not null,
  supply_center varchar(20) not null,
  constraint fk_contract_supply_center foreign key (supply_center) references
supply_center(supply_center)
);

create table if not exists client(
 client_enterprise varchar(35) not null primary key,
 industry varchar(20) unique not null,
 country varchar(20) not null,
 city varchar(12),
 supply_center varchar(20) unique not null,
 constraint fk_client_contract foreign key (client_enterprise) references
contract(client_enterprise),
 constraint fk_client_supply_center foreign key (supply_center) references
supply_center(supply_center)
);

create table if not exists salesman(
 salesman_number integer not null primary key,
 salesman varchar(20) unique not null,
 age integer not null,
 gender varchar(8) not null,
 mobile_phone varchar(10) not null,
 supply_center varchar(20) not null,
 constraint fk_salesman_supply_center foreign key (supply_center) references
supply_center(supply_center)
);

create table if not exists orders(
 orders_id serial primary key,
 contract_number varchar(12) unique not null,
 product_model varchar(40) unique not null,
 salesman_number integer not null,
 quantity integer not null,
 estimated_delivery_date varchar(12),
 lodgement_date varchar(12),
 constraint fk_orders_contract foreign key (contract_number) references
contract(contract_number),
constraint fk_orders_salesman foreign key (salesman_number) references
salesman(salesman_number)
);

create table if not exists product_model(
 product_model varchar(40) not null primary key,
 product_code varchar(9) not null,
 product_name varchar(40) not null,
 unit_price integer not null,
 constraint fk_product_model_orders foreign key (product_model) references
orders(product_model)
);

drop table product_model;
drop table orders;
drop table salesman;
drop table client;
drop table contract;
drop table supply_center;
