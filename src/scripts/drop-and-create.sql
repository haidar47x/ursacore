drop table if exists patient cascade ;
drop table if exists sample cascade ;
create table patient (version integer, created_at timestamp(6), update_at timestamp(6), id varchar(36) not null, name varchar(128) not null, primary key (id));
create table sample (sample_code varchar(4) not null, status tinyint not null check (status between 0 and 4), type tinyint not null check (type between 0 and 0), version integer, collected_at timestamp(6) not null, created_at timestamp(6), update_at timestamp(6), id varchar(36) not null, primary key (id));
