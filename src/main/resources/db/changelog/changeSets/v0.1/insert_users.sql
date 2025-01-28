-- liquibase formatted sql

-- changeset konstantin:insert_users
insert into Users (Name, Email, PhoneNumber, PasswordHash, Role)
values
    ('Alice Smith', 'alice.smith@example.com', '1234567890', 'hashpassword1',  'CLIENT'),
    ('Bob Johnson', 'bob.johnson@example.com', '1234567891', 'hashpassword2', 'ADMINISTRATOR'),
    ('Charlie Brown', 'charlie.brown@example.com', '1234567892', 'hashpassword3',   'CLIENT'),
    ('David Wilson', 'david.wilson@example.com', '1234567893', 'hashpassword4',   'CLIENT'),
    ('Emma Davis', 'emma.davis@example.com', '1234567894', 'hashpassword5',  'CLIENT'),
    ('Frank Clark', 'frank.clark@example.com', '1234567895', 'hashpassword6',   'ADMINISTRATOR'),
    ('Grace Hall', 'grace.hall@example.com', '1234567896', 'hashpassword7',   'CLIENT'),
    ('Henry Lewis', 'henry.lewis@example.com', '1234567897', 'hashpassword8', 'CLIENT'),
    ('Isabel Young', 'isabel.young@example.com', '1234567898', 'hashpassword9',  'CLIENT'),
    ('Jack Allen', 'jack.allen@example.com', '1234567899', 'hashpassword10',   'CLIENT'),
    ('Karen King', 'karen.king@example.com', '1234567800', 'hashpassword11',   'CLIENT'),
    ('Larry Wright', 'larry.wright@example.com', '1234567801', 'hashpassword12',  'CLIENT'),
    ('Mona Scott', 'mona.scott@example.com', '1234567802', 'hashpassword13',   'CLIENT'),
    ('Nina Green', 'nina.green@example.com', '1234567803', 'hashpassword14',   'CLIENT'),
    ('Oscar Baker', 'oscar.baker@example.com', '1234567804', 'hashpassword15',  'ADMINISTRATOR'),
    ('Paula Adams', 'paula.adams@example.com', '1234567805', 'hashpassword16',  'CLIENT'),
    ('Quincy Turner', 'quincy.turner@example.com', '1234567806', 'hashpassword17',  'CLIENT'),
    ('Rachel Miller', 'rachel.miller@example.com', '1234567807', 'hashpassword18',  'CLIENT'),
    ('Steve Carter', 'steve.carter@example.com', '1234567808', 'hashpassword19',   'CLIENT'),
    ('Tina Martinez', 'tina.martinez@example.com', '1234567809', 'hashpassword20', 'CLIENT');