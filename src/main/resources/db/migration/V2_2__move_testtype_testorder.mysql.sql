alter table `test_order_line` drop column test_type;
alter table `test_order` add column test_type smallint not null;