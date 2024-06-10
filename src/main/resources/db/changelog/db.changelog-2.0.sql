--liquibase formatted sql

--changeset belonogov:1
insert into company (name) values ('Gazprom');
insert into company (name) values ('TatNeft');
insert into company (name) values ('SurgutNefteGaz');
insert into company (name) values ('Rosseti');
insert into company (name) values ('Rosteh');

--changeset belonogov:2
insert into employees (first_name, last_name, rating, company_id) values ('Ivan', 'Petrov', 7, 1);
insert into employees (first_name, last_name, rating, company_id) values ('Petr', 'Ivanov', 9, 1);
insert into employees (first_name, last_name, rating, company_id) values ('Timur', 'Fedotov', 5, 1);

insert into employees (first_name, last_name, rating, company_id) values ('Ivan', 'Denisov', 8, 2);
insert into employees (first_name, last_name, rating, company_id) values ('Petr', 'Petrov', 4, 2);
insert into employees (first_name, last_name, rating, company_id) values ('Timur', 'Kurochkin', 7, 2);

insert into employees (first_name, last_name, rating, company_id) values ('Ivan', 'Serggev', 6, 3);
insert into employees (first_name, last_name, rating, company_id) values ('Petr', 'Kuimov', 8, 3);
insert into employees (first_name, last_name, rating, company_id) values ('Timur', 'Glebov', 2, 3);

--changeset belonogov:3
insert into tasks (name, description, rating, task_status)
values ('Обустройство территории', 'требуется выполнить земельные работы : S = 1000m2', 3, 'IN_PROGRESS');
insert into tasks (name, description, rating, task_status)
values ('Электромонтажные работы', 'произвести заделку концевых муфт в количестве 10 шт.', 7, 'SEARCH_FOR_EMPLOYEES');
insert into tasks (name, description, rating, task_status)
values ('Заливка фундамента', 'требуется произвести заливку ленточного фундамента : V = 10m3', 5, 'IN_PROGRESS');
insert into tasks (name, description, rating, task_status)
values ('Штукатурные работы', 'требуется произвести штукатурные работы гипсовой штукатуркой : S = 100m2', 6, 'IN_PROGRESS');
insert into tasks (name, description, rating, task_status)
values ('Кровельные работы', 'требуется произвести кровельные работы по сборке стропильной системы : S = 300m2', 7, 'SEARCH_FOR_EMPLOYEES');

--changeset belonogov:4
insert into tasks_employee (task_id, employee_id) values (1, 5);
insert into tasks_employee (task_id, employee_id) values (1, 3);

insert into tasks_employee (task_id, employee_id) values (3, 7);
insert into tasks_employee (task_id, employee_id) values (3, 1);

insert into tasks_employee (task_id, employee_id) values (4, 4);
insert into tasks_employee (task_id, employee_id) values (4, 8);
insert into tasks_employee (task_id, employee_id) values (4, 1);
