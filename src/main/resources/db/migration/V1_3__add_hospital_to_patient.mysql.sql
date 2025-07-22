alter table `patient` modify column doctor varchar(128) null;
alter table `patient` modify column hospital varchar(256) null;
alter table `patient` add column medicalCondition varchar(256) null;