-- Inserting sample data into the 'users' table
INSERT INTO groups (name) VALUES ('Group A'), ('Group B');

INSERT INTO users (login, password, is_admin, email, name, surname, group_id)
VALUES
    ('user1', 'password1', false, 'user1@example.com', 'John', 'Doe', 1),
    ('user2', 'password2', false, 'user2@example.com', 'Jane', 'Smith', 1),
    ('admin', 'adminpass', true, 'admin@example.com', 'Admin', 'User', 1);

-- Inserting sample data into the 'schedules' table
INSERT INTO schedules (tag, group_id) VALUES ('Schedule 1', 1), ('Schedule 2', 2);

-- Inserting sample data into the 'timeslots' table
INSERT INTO timeslots (start_date, end_date, schedule_id, user_login)
VALUES
    ('2024-05-11', '2024-05-12', 1, 'user1'),
    ('2024-05-12', '2024-05-13', 2, 'user2');

-- Inserting sample data into the 'tradeOffers' table
INSERT INTO tradeOffers (price, can_offer, timestamp, timeslot_id, user_login)
VALUES
    (50, true, '2024-05-11', 1, 'user1'),
    (60, true, '2024-05-12', 2, 'user2');

-- Inserting sample data into the 'trades' table
INSERT INTO trades (timestamp, user_login)
VALUES
    ('2024-05-11', 'user1'),
    ('2024-05-12', 'user2');
