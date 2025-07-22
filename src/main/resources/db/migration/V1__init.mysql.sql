drop table if exists `patient` cascade ;
drop table if exists `sample` cascade ;

create table if not exists `patient` (
    version integer,
    created_at timestamp(6),
    update_at timestamp(6),
    id varchar(36) not null,
    name varchar(128) not null,
    primary key (id),
    blood_type smallint not null,
    gender smallint not null,
    medical_condition varchar(256) null,
    age integer not null
);

create table if not exists `sample` (
    sample_code varchar(4) not null,
    status smallint not null check,
    type smallint not null check,
    version integer,
    collected_at timestamp(6) not null,
    created_at timestamp(6),
    update_at timestamp(6),
    id varchar(36) not null,
    primary key (id)
);