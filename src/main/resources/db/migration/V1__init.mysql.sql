drop table if exists `patient` cascade ;
drop table if exists `sample` cascade ;

create table if not exists `patient` (
    id                  varchar(36)     not null,
    version             integer,
    created_at          timestamp(6),
    update_at           timestamp(6),
    name                varchar(128)    not null,
    blood_type          smallint        not null,
    gender              smallint        not null,
    medical_condition   varchar(256)    null,
    age                 integer         not null,
    primary key (id)
);

create table if not exists `sample` (
    id                  varchar(36)     not null,
    sample_code         varchar(4)      not null,
    status              smallint        not null,
    type                smallint        not null,
    version             integer,
    collected_at        timestamp(6)    not null,
    created_at          timestamp(6),
    update_at           timestamp(6),
    primary key (id)
);