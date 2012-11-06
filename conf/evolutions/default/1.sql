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
  rfid                      bigint,
  message_type              integer,
  time_sent                 datetime,
  time_received             datetime,
  latitude                  double,
  longitude                 double,
  pulse                     integer,
  temperature               double,
  constraint ck_Event_message_type check (message_type in (0,1,2)),
  constraint pk_Event primary key (id))
;

create table Producer (
  id                        bigint auto_increment not null,
  producer_id               bigint,
  username                  varchar(255),
  name                      varchar(255),
  password                  varchar(255),
  time_created              datetime,
  is_admin                  tinyint(1) default 0,
  constraint pk_Producer primary key (id))
;

create table Sheep (
  id                        bigint auto_increment not null,
  sheep_id                  bigint,
  producer_id               bigint,
  rfid                      bigint,
  name                      varchar(255),
  birth_weight              double,
  time_of_birth             datetime,
  notes                     varchar(255),
  attacked                  tinyint(1) default 0,
  time_added                datetime,
  constraint pk_Sheep primary key (id))
;

alter table Contact add constraint fk_Contact_producer_1 foreign key (producer_id) references Producer (producer_id) on delete restrict on update restrict;
create index ix_Contact_producer_1 on Contact (producer_id);
alter table Event add constraint fk_Event_rfid_2 foreign key (rfid) references Sheep (rfid) on delete restrict on update restrict;
create index ix_Event_rfid_2 on Event (rfid);
alter table Sheep add constraint fk_Sheep_producer_3 foreign key (producer_id) references Producer (producer_id) on delete restrict on update restrict;
create index ix_Sheep_producer_3 on Sheep (producer_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table Contact;

drop table Event;

drop table Producer;

drop table Sheep;

SET FOREIGN_KEY_CHECKS=1;

