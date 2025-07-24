create table if not exists `test_category_test_order` (
    primary key test_category_id        varchar(36)     not null,
    primary key order_id                varchar(36)     not null,
    primary key (category_id, order_id),
    constraint foreign key (category_id) references test_category (id),
    constraint foreign key (order_id) references test_order (id)
) engine=InnoDB;
