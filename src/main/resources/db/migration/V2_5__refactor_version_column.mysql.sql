alter table `test_category` add column version bigint default not null;
alter table `sample` change column version version bigint default not null;
alter table `patient` change column version version bigint default not null;