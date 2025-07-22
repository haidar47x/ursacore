drop table if exists `test_order`;
drop table if exists `test_order_line`;

create table `test_order` (
    id            varchar(36)     not null,
    created_at    timestamp(6)    default null,
    updated_at    timestamp(6)    default null,
    patient_ref   varchar(255)    default null,
    version       bigint          default null,
    patient_id    varchar(36)     not null,
    primary key (id),
    constraint foreign key (patient_id) references patient (id)
) engine=InnoDB;

create table `test_order_line` (
    id            varchar(36)     not null,
    sample_id     varchar(36)     not null,
    created_at    timestamp(6)    default null,
    updated_at    timestamp(6)    default null,
    version       bigint          default null,
    test_order_id varchar(36)     not null,
    test_type     smallint        not null,
    primary key (id),
    constraint foreign key (test_order_id) references test_order (id),
    constraint foreign key (sample_id) references sample (id)
) engine=InnoDB;