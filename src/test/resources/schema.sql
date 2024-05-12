DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

CREATE TABLE users
(
    login text PRIMARY KEY,
    hashed_password text NOT NULL,
    is_admin boolean NOT NULL,
    email text UNIQUE NOT NULL,
    name text,
    surname text
);

CREATE TABLE groups (
                        id serial PRIMARY KEY,
                        name text NOT NULL
);

CREATE TABLE schedules (
                           id serial PRIMARY KEY,
                           tag text NOT NULL
);

CREATE TABLE timeslots(
                          id serial PRIMARY KEY,
                          start_date date NOT NULL,
                          end_date date NOT NULL
);

CREATE TABLE tradeOffers(
                            id serial PRIMARY KEY,
                            price integer NOT NULL,
                            can_offer boolean NOT NULL,
                            timestamp date NOT NULL
);

CREATE TABLE trades(
                       id serial PRIMARY KEY,
                       timestamp date NOT NULL
);


ALTER TABLE users ADD COLUMN group_id serial NOT NULL ;
ALTER TABLE users ADD CONSTRAINT group_fk FOREIGN KEY (group_id) REFERENCES groups(id);

ALTER TABLE schedules ADD COLUMN group_id serial NOT NULL ;
ALTER TABLE schedules ADD CONSTRAINT group_fk FOREIGN KEY (group_id) REFERENCES groups(id);

ALTER TABLE timeslots ADD COLUMN schedule_id serial NOT NULL ;
ALTER TABLE timeslots ADD CONSTRAINT schedule_fk FOREIGN KEY (schedule_id) REFERENCES schedules(id);

ALTER TABLE timeslots ADD COLUMN user_login text NOT NULL DEFAULT '';
ALTER TABLE timeslots ADD CONSTRAINT user_fk FOREIGN KEY (user_login) REFERENCES users(login);

ALTER TABLE tradeOffers ADD COLUMN timeslot_id serial NOT NULL;
ALTER TABLE tradeOffers ADD CONSTRAINT timeslot_fk FOREIGN KEY (timeslot_id) REFERENCES timeslots(id);

ALTER TABLE tradeOffers ADD COLUMN user_login text NOT NULL DEFAULT '';
ALTER TABLE tradeOffers ADD CONSTRAINT user_fk FOREIGN KEY (user_login) REFERENCES users(login);

ALTER TABLE trades ADD COLUMN user_login text NOT NULL DEFAULT '';
ALTER TABLE trades ADD CONSTRAINT user_fk FOREIGN KEY (user_login) REFERENCES users(login);