# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table Contact (
  id                        bigint auto_increment not null,
  producer_id               bigint,
  name                      varchar(255),
  email                     varchar(255),
  phone_sms                 varchar(255),
  phone_call                varchar(255),
  email_alert               tinyint(1) default 0,
  call_alert                tinyint(1) default 0,
  sms_alert                 tinyint(1) default 0,
  constraint pk_Contact primary key (id))
;

create table Event (
  id                        bigint auto_increment not null,
  producer_id               bigint,
  rfid                      bigint,
  sheep_id                  bigint,
  type                      integer,
  time_sent                 integer,
  time_received             integer,
  pulse                     integer,
  temperature               double,
  constraint ck_Event_type check (type in (0,1,2)),
  constraint pk_Event primary key (id))
;

create table Sheep (
  id                        bigint auto_increment not null,
  producer_id               bigint,
  sheep_id                  bigint,
  rfid                      bigint,
  name                      varchar(255),
  time_of_birth             bigint,
  birth_weight              double,
  notes                     varchar(255),
  attacked                  tinyint(1) default 0,
  time_added                bigint,
  constraint pk_Sheep primary key (id))
;

create table User (
  id                        bigint auto_increment not null,
  producer_id               bigint,
  username                  varchar(255),
  password                  varchar(255),
  time_created              integer,
  is_admin                  tinyint(1) default 0,
  constraint pk_User primary key (id))
;

alter table Event add constraint fk_Event_producer_1 foreign key (producer_id) references User (id) on delete restrict on update restrict;
create index ix_Event_producer_1 on Event (producer_id);
alter table Event add constraint fk_Event_sheep_2 foreign key (sheep_id) references Sheep (id) on delete restrict on update restrict;
create index ix_Event_sheep_2 on Event (sheep_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table Contact;

drop table Event;

drop table Sheep;

drop table User;

SET FOREIGN_KEY_CHECKS=1;

