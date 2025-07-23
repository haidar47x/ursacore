create table if not exists `test_category` (
    id              varchar(36) not null,
    type            smallint    not null,
    categoryName    varchar(64) not null,
    primary key (id)
) engine=InnoDB;