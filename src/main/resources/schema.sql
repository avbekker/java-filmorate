create table USERS
(
    USER_ID   BIGINT auto_increment
        primary key,
    EMAIL     CHARACTER VARYING(255) not null,
    LOGIN     CHARACTER VARYING(50)  not null,
    USER_NAME CHARACTER VARYING(50)  not null,
    BIRTHDAY  DATE
);

create table FRIENDS
(
    USER_ID   INTEGER,
    FRIEND_ID INTEGER,
    constraint FRIENDS_FRIENDS__ID
        foreign key (FRIEND_ID) references USERS,
    constraint FRIENDS_USER_ID
        foreign key (USER_ID) references USERS
);

create table MPA
(
    MPA_ID INTEGER auto_increment,
    NAME   CHARACTER VARYING(25) not null,
    constraint MPA_PK
        primary key (MPA_ID)
);

create table FILMS
(
    FILM_ID      BIGINT auto_increment
        primary key,
    NAME         CHARACTER VARYING(50)  not null,
    DESCRIPTION  CHARACTER VARYING(200) not null,
    RELEASE_DATE DATE                   not null,
    DURATION     INTEGER,
    MPA_ID       INTEGER,
    constraint MPA_ID
        foreign key (MPA_ID) references MPA
);

create table GENRE
(
    GENRE_ID INTEGER auto_increment,
    NAME     CHARACTER VARYING(50) not null,
    constraint GENRE_PK
        primary key (GENRE_ID)
);

create table FILM_GENRE
(
    FILM_ID  INTEGER,
    GENRE_ID INTEGER,
    constraint FILM_GENRE_ID
        foreign key (GENRE_ID) references GENRE,
    constraint GENRE_FILM_ID
        foreign key (FILM_ID) references FILMS
);

create table FILM_LIKES
(
    FILM_ID INTEGER,
    USER_ID INTEGER,
    constraint FILM_ID
        foreign key (FILM_ID) references FILMS,
    constraint USER_ID
        foreign key (USER_ID) references USERS
);



