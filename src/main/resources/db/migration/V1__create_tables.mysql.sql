drop table if exists laboratory cascade ;
drop table if exists lab_test cascade ;
drop table if exists patient cascade ;
drop table if exists sample cascade ;
drop table if exists test_order cascade ;
drop table if exists test_order_line cascade ;
drop table if exists test_result cascade ;

create table laboratory
    (
      address varchar(512) not null unique,
      description varchar(1024),
      email varchar(255),
      id varchar(36) not null,
      name varchar(256) not null unique,
      phone_number varchar(64) not null unique,
      social_media_handle varchar(255),
      website varchar(255),
      primary key (id)
    ) engine=InnoDB;

create table lab_test
    (
      code varchar(16) not null unique,
      created_at timestamp(6),
      description varchar(512),
      id varchar(36) not null,
      name varchar(128) not null unique,
      price float(53) not null,
      reference_range_description varchar(1024),
      reference_range_high float(53) not null,
      reference_range_low float(53) not null,
      reference_range_unit varchar(255),
      test_category smallint not null,
      updated_at timestamp(6),
      version bigint,
      primary key (id)
    ) engine=InnoDB;

create table patient
    (
      age integer not null check (age <= 128),
      blood_type smallint not null,
      created_at timestamp(6),
      doctor varchar(128),
      gender smallint not null,
      hospital varchar(256),
      id varchar(36) not null,
      medical_condition varchar(256) not null,
      name varchar(128) not null,
      update_at timestamp(6),
      version bigint,
      primary key (id)
    ) engine=InnoDB;

create table sample
    (
      collected_at timestamp(6) not null,
      created_at timestamp(6),
      id varchar(36) not null,
      sample_code varchar(4) not null,
      sample_type smallint not null,
      update_at timestamp(6),
      version bigint,
      primary key (id)
    ) engine=InnoDB;

create table test_order
    (
      created_at timestamp(6),
      id varchar(36) not null,
      patient_id varchar(36),
      patient_ref varchar(255),
      updated_at timestamp(6),
      version bigint,
      primary key (id)
    ) engine=InnoDB;

create table test_order_line
    (
      created_at timestamp(6),
      id varchar(36) not null,
      lab_test_id varchar(36) not null,
      sample_id varchar(36) not null,
      test_order_id varchar(36) not null,
      test_result_id varchar(36) unique,
      updated_at timestamp(6),
      version bigint,
      primary key (id)
    ) engine=InnoDB;

create table test_result
    (
      id varchar(36) not null,
      result_value varchar(255) not null,
      primary key (id)
    ) engine=InnoDB;

alter table if exists test_order add constraint FKp24g0fd1vr8gfd1ryjq6oa6lf foreign key (patient_id) references patient;
alter table if exists test_order_line add constraint FKfulyncdaqlkhg42eihesox4y7 foreign key (lab_test_id) references lab_test;
alter table if exists test_order_line add constraint FK46e0b6v07rucjhyplyc9235to foreign key (sample_id) references sample;
alter table if exists test_order_line add constraint FK783q9radf832syb8omdy4uych foreign key (test_order_id) references test_order;
alter table if exists test_order_line add constraint FK5jmheefh57ydotpn07kg6ruqk foreign key (test_result_id) references test_result;

