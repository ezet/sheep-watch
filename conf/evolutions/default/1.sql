# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table contact (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  name                      varchar(255),
  email                     varchar(255),
  phone                     varchar(255),
  email_alert               tinyint(1) default 0,
  call_alert                tinyint(1) default 0,
  sms_alert                 tinyint(1) default 0,
  cre_time                  datetime not null,
  upd_time                  datetime not null,
  constraint pk_contact primary key (id))
;

create table event (
  id                        bigint auto_increment not null,
  rfid                      bigint not null,
  sheep_id                  bigint not null,
  message_type              integer,
  time_sent                 datetime,
  latitude                  double,
  longitude                 double,
  pulse                     integer,
  temperature               double,
  time_received             datetime not null,
  constraint ck_event_message_type check (message_type in (0,1,2)),
  constraint pk_event primary key (id))
;

create table sheep (
  id                        bigint auto_increment not null,
  sheep_pid                 bigint not null,
  user_id                   bigint,
  rfid                      bigint not null,
  name                      varchar(255),
  birth_weight              double,
  date_of_birth             datetime,
  notes                     varchar(255),
  cre_time                  datetime not null,
  upd_time                  datetime not null,
  constraint uq_sheep_rfid unique (rfid),
  constraint pk_sheep primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  producer_id               bigint not null,
  username                  varchar(255) not null,
  name                      varchar(255),
  password                  varchar(255) not null,
  access_level              integer not null,
  cre_time                  datetime not null,
  upd_time                  datetime not null,
  constraint uq_user_producer_id unique (producer_id),
  constraint uq_user_username unique (username),
  constraint pk_user primary key (id))
;

alter table contact add constraint fk_contact_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_contact_user_1 on contact (user_id);
alter table event add constraint fk_event_sheep_2 foreign key (sheep_id) references sheep (id) on delete restrict on update restrict;
create index ix_event_sheep_2 on event (sheep_id);
alter table sheep add constraint fk_sheep_user_3 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_sheep_user_3 on sheep (user_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table contact;

drop table event;

drop table sheep;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

