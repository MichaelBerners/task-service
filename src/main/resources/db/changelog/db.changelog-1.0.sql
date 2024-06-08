--liquibase formatted sql

--changeset belonogov:1

create table company
(
    id   bigserial,
    constraint company_pk primary key (id),
    name varchar(300) not null,
    constraint unique_name unique (name)
);

--rollback drop table company

--changeset belonogov:2

create table employees
(
    id         bigserial,
    constraint employees_pk primary key (id),
    first_name varchar(300) not null,
    last_name  varchar(300) not null,
    rating     int          not null,
    company_id bigint      not null,
    constraint employees_company_fk foreign key (company_id) references company (id)
);

--rollback drop table employees

--changeset belonogov:3

create table tasks
(
    id          bigserial,
    constraint tasks_pk primary key (id),
    name        varchar(500) not null,
    description varchar      not null,
    rating      int          not null,
    task_status varchar(300) not null
);

--rollback drop table tasks

--changeset belonogov:4

create table tasks_employee
(
    task_id bigint not null,
    employee_id bigint not null,
    constraint tasks_employee_pk primary key (task_id, employee_id),
    constraint tasks_fk foreign key (task_id) references tasks (id),
    constraint employee_fk foreign key (employee_id) references employees (id)

);

--rollback drop table tasks_employee


