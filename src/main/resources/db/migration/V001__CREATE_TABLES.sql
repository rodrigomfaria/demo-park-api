create table costumers (
	dh_created timestamp(6),
	dh_updated timestamp(6),
	id bigint generated by default as identity,
	id_user bigint not null unique,
	identification_number varchar(11) not null unique,
	name varchar(100) not null,
	created_by varchar(255),
	updated_by varchar(255),
	primary key (id)
);

create table costumers_vacancies (
	discount decimal(7,2),
	total decimal(7,2),
	dh_created timestamp(6),
	dh_entry timestamp(6) not null,
	dh_exit timestamp(6),
	dh_updated timestamp(6),
	id bigint generated by default as identity,
	id_costumer bigint not null,
	id_vacancy bigint not null,
	plate varchar(8) not null,
	receipt_number varchar(15) not null unique,
	brand varchar(45) not null,
	color varchar(45) not null,
	model varchar(45) not null,
	created_by varchar(255),
	updated_by varchar(255),
	primary key (id)
);

create table users (
	dh_created timestamp(6),
	dh_updated timestamp(6),
	id bigint generated by default as identity,
	role varchar(25) not null check (role in ('ROLE_ADMIN','ROLE_COSTUMER')),
	username varchar(100) not null unique,
	password varchar(200) not null,
	created_by varchar(255),
	updated_by varchar(255),
	primary key (id)
);

create table vacancy (
	code varchar(4) not null unique,
	dh_created timestamp(6),
	dh_updated timestamp(6),
	id bigint generated by default as identity,
	created_by varchar(255),
	status varchar(255) not null check (status in ('FREE','BUSY')),
	updated_by varchar(255),
	primary key (id)
);

alter table if exists costumers
   add constraint FK78sejfgfl3bhbdnjm4m7iqqmn
   foreign key (id_user)
   references users;

alter table if exists costumers_vacancies
   add constraint FKird050f4ux7iq57u7llwld6nr
   foreign key (id_costumer)
   references costumers;

alter table if exists costumers_vacancies
   add constraint FK93hgfi6ohj76qmj5wrgnveguu
   foreign key (id_vacancy)
   references vacancy;