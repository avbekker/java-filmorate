CREATE TABLE IF NOT EXISTS USERS
(
    USER_ID   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    EMAIL     CHARACTER VARYING(255) NOT NULL,
    LOGIN     CHARACTER VARYING(50)  NOT NULL,
    USER_NAME CHARACTER VARYING(50)  NOT NULL,
    BIRTHDAY  DATE
);

CREATE UNIQUE INDEX IF NOT EXISTS USER_EMAIL_UINDEX ON USERS (EMAIL);
CREATE UNIQUE INDEX IF NOT EXISTS USER_LOGIN_UINDEX ON USERS (LOGIN);

CREATE TABLE IF NOT EXISTS FRIENDS
(
    USER_ID   INTEGER,
    FRIEND_ID INTEGER,
    CONSTRAINT FRIENDS_FRIENDS__ID
        FOREIGN KEY (FRIEND_ID) REFERENCES USERS,
    CONSTRAINT FRIENDS_USER_ID
        FOREIGN KEY (USER_ID) REFERENCES USERS,
    CONSTRAINT PK_FRIEND
        PRIMARY KEY (USER_ID, FRIEND_ID)
);

CREATE TABLE IF NOT EXISTS MPA
(
    MPA_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME   CHARACTER VARYING(25) NOT NULL,
    CONSTRAINT MPA_PK
        PRIMARY KEY (MPA_ID)
);

CREATE TABLE IF NOT EXISTS FILMS
(
    FILM_ID      BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME         CHARACTER VARYING(50)  NOT NULL,
    DESCRIPTION  CHARACTER VARYING(200) NOT NULL,
    RELEASE_DATE DATE                   NOT NULL,
    DURATION     INTEGER,
    MPA_ID       INTEGER,
    CONSTRAINT MPA_ID
        FOREIGN KEY (MPA_ID) REFERENCES MPA
);

CREATE TABLE IF NOT EXISTS GENRE
(
    GENRE_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME     CHARACTER VARYING(50) NOT NULL,
    CONSTRAINT GENRE_PK
        PRIMARY KEY (GENRE_ID)
);

CREATE TABLE IF NOT EXISTS FILM_GENRE
(
    FILM_ID  INTEGER,
    GENRE_ID INTEGER,
    CONSTRAINT FILM_GENRE_ID
        FOREIGN KEY (GENRE_ID) REFERENCES GENRE,
    CONSTRAINT GENRE_FILM_ID
        FOREIGN KEY (FILM_ID) REFERENCES FILMS,
    CONSTRAINT PK_FILM_GENRE
        PRIMARY KEY (FILM_ID, GENRE_ID)
);

CREATE TABLE IF NOT EXISTS FILM_LIKES
(
    FILM_ID INTEGER,
    USER_ID INTEGER,
    CONSTRAINT PK_FILM_LIKE
        PRIMARY KEY (FILM_ID, USER_ID),
    CONSTRAINT FILM_ID
        FOREIGN KEY (FILM_ID) REFERENCES FILMS,
    CONSTRAINT USER_ID
        FOREIGN KEY (USER_ID) REFERENCES USERS
);


