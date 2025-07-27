drop table if exists test_order_line;
drop table if exists test_order;
drop table if exists test_result;
drop table if exists sample;
drop table if exists patient;
drop table if exists lab_test;
drop table if exists laboratory;

create table laboratory
(
  id                  varchar(36)   not null,
  name                varchar(256)  not null unique,
  address             varchar(512)  not null unique,
  phone_number        varchar(64)   not null unique,
  email               varchar(255),
  website             varchar(255),
  social_media_handle varchar(255),
  description         varchar(1024),
  primary key (id)
) engine=InnoDB;

create table lab_test
(
  id                          varchar(36)       not null,
  code                        varchar(16)       not null unique,
  name                        varchar(128)      not null unique,
  description                 varchar(512),
  price                       decimal(10, 2)    not null,
  test_category               smallint          not null,
  reference_range_low         float(53)         not null,
  reference_range_high        float(53)         not null,
  reference_range_unit        varchar(255),
  reference_range_description varchar(1024),
  version                     bigint,
  created_at                  timestamp(6)      default CURRENT_TIMESTAMP(6),
  updated_at                  timestamp(6),
  primary key (id)
) engine=InnoDB;

create table patient
(
  id                varchar(36)         not null,
  name              varchar(128)        not null,
  age               integer             not null check (age <= 128),
  gender            smallint            not null,
  blood_type        smallint            not null,
  medical_condition varchar(256)        not null,
  doctor            varchar(128),
  hospital          varchar(256),
  version           bigint,
  created_at        timestamp(6)        default CURRENT_TIMESTAMP(6),
  updated_at        timestamp(6)        default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
  primary key (id)
) engine=InnoDB;

create table sample
(
  id            varchar(36)     not null,
  sample_code   varchar(4)      not null,
  sample_type   smallint        not null,
  collected_at  timestamp(6)    not null,
  version       bigint,
  created_at    timestamp(6)    default CURRENT_TIMESTAMP(6),
  updated_at    timestamp(6)    default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
  primary key (id)
) engine=InnoDB;

create table test_result
(
  id            varchar(36)  not null,
  result_value  varchar(255) not null,
  primary key (id)
) engine=InnoDB;

create table test_order
(
  id          varchar(36)   not null,
  patient_id  varchar(36),
  patient_ref varchar(255),
  version     bigint,
  created_at  timestamp(6)  default CURRENT_TIMESTAMP(6),
  updated_at  timestamp(6)  default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
  primary key (id),
  constraint fk_test_order_patient foreign key (patient_id) references patient (id)
) engine=InnoDB;

create table test_order_line
(
  id             varchar(36)    not null,
  test_order_id  varchar(36)    not null,
  lab_test_id    varchar(36)    not null,
  sample_id      varchar(36)    not null,
  test_result_id varchar(36)    unique,
  version        bigint,
  created_at     timestamp(6)   default CURRENT_TIMESTAMP(6),
  updated_at     timestamp(6)   default CURRENT_TIMESTAMP(6) on update CURRENT_TIMESTAMP(6),
  primary key (id),
  constraint fk_order_line_to_order     foreign key (test_order_id)     references test_order (id),
  constraint fk_order_line_to_lab_test  foreign key (lab_test_id)       references lab_test (id),
  constraint fk_order_line_to_sample    foreign key (sample_id)         references sample (id),
  constraint fk_order_line_to_result    foreign key (test_result_id)    references test_result (id)
) engine=InnoDB;