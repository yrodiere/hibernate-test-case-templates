-- Insert types
insert into documenttype (id, name) values (1, 'Type 1');
insert into documenttype (id, name) values (2, 'Type 2');

-- Insert documents
insert into document (title, documentType_fk) values ('Document 1_1', 1);
insert into document (title, documentType_fk) values ('Document 2_2', 2);
insert into document (title, documentType_fk) values ('Document 3_2', 2);
insert into document (title, documentType_fk) values ('Document 4_null', null);