-- liquibase formatted sql

-- changeset Konstantin:insert_users
insert into Users (Name, Email, PhoneNumber, PasswordHash, Role)
values
    ('Alice Smith', 'alice.smith@example.com', '1234567890', '$2a$10$OBp3oiPZBEoMoTXfN2IJie.GYqh1RgkFsLovoLJ1UPSLB2YMWL4Ji',  'CLIENT'),
    ('Bob Johnson', 'bob.johnson@example.com', '1234567891', '$2a$10$OBp3oiPZBEoMoTXfN2IJie.GYqh1RgkFsLovoLJ1UPSLB2YMWL4Ji', 'ADMINISTRATOR'),
    ('Charlie Brown', 'charlie.brown@example.com', '1234567892', '$2a$10$OBp3oiPZBEoMoTXfN2IJie.GYqh1RgkFsLovoLJ1UPSLB2YMWL4Ji',   'CLIENT'),
    ('David Wilson', 'david.wilson@example.com', '1234567893', '$2a$10$OBp3oiPZBEoMoTXfN2IJie.GYqh1RgkFsLovoLJ1UPSLB2YMWL4Ji',   'CLIENT'),
    ('Emma Davis', 'emma.davis@example.com', '1234567894', '$2a$10$OBp3oiPZBEoMoTXfN2IJie.GYqh1RgkFsLovoLJ1UPSLB2YMWL4Ji',  'CLIENT'),
    ('Frank Clark', 'frank.clark@example.com', '1234567895', '$2a$10$OBp3oiPZBEoMoTXfN2IJie.GYqh1RgkFsLovoLJ1UPSLB2YMWL4Ji',   'ADMINISTRATOR'),
    ('Grace Hall', 'grace.hall@example.com', '1234567896', '$2a$10$OBp3oiPZBEoMoTXfN2IJie.GYqh1RgkFsLovoLJ1UPSLB2YMWL4Ji',   'CLIENT'),
    ('Henry Lewis', 'henry.lewis@example.com', '1234567897', '$2a$10$OBp3oiPZBEoMoTXfN2IJie.GYqh1RgkFsLovoLJ1UPSLB2YMWL4Ji', 'CLIENT'),
    ('Isabel Young', 'isabel.young@example.com', '1234567898', '$2a$10$OBp3oiPZBEoMoTXfN2IJie.GYqh1RgkFsLovoLJ1UPSLB2YMWL4Ji',  'CLIENT'),
    ('Jack Allen', 'jack.allen@example.com', '1234567899', '$2a$10$OBp3oiPZBEoMoTXfN2IJie.GYqh1RgkFsLovoLJ1UPSLB2YMWL4Ji',   'CLIENT'),
    ('Karen King', 'karen.king@example.com', '1234567800', '$2a$10$OBp3oiPZBEoMoTXfN2IJie.GYqh1RgkFsLovoLJ1UPSLB2YMWL4Ji',   'CLIENT'),
    ('Larry Wright', 'larry.wright@example.com', '1234567801', '$2a$10$OBp3oiPZBEoMoTXfN2IJie.GYqh1RgkFsLovoLJ1UPSLB2YMWL4Ji',  'CLIENT'),
    ('Mona Scott', 'mona.scott@example.com', '1234567802', '$2a$10$OBp3oiPZBEoMoTXfN2IJie.GYqh1RgkFsLovoLJ1UPSLB2YMWL4Ji',   'CLIENT'),
    ('Nina Green', 'nina.green@example.com', '1234567803', '$2a$10$OBp3oiPZBEoMoTXfN2IJie.GYqh1RgkFsLovoLJ1UPSLB2YMWL4Ji',   'CLIENT'),
    ('Oscar Baker', 'oscar.baker@example.com', '1234567804', '$2a$10$OBp3oiPZBEoMoTXfN2IJie.GYqh1RgkFsLovoLJ1UPSLB2YMWL4Ji',  'ADMINISTRATOR'),
    ('Paula Adams', 'paula.adams@example.com', '1234567805', '$2a$10$OBp3oiPZBEoMoTXfN2IJie.GYqh1RgkFsLovoLJ1UPSLB2YMWL4Ji',  'CLIENT'),
    ('Quincy Turner', 'quincy.turner@example.com', '1234567806', '$2a$10$OBp3oiPZBEoMoTXfN2IJie.GYqh1RgkFsLovoLJ1UPSLB2YMWL4Ji',  'CLIENT'),
    ('Rachel Miller', 'rachel.miller@example.com', '1234567807', '$2a$10$OBp3oiPZBEoMoTXfN2IJie.GYqh1RgkFsLovoLJ1UPSLB2YMWL4Ji',  'CLIENT'),
    ('Steve Carter', 'steve.carter@example.com', '1234567808', '$2a$10$OBp3oiPZBEoMoTXfN2IJie.GYqh1RgkFsLovoLJ1UPSLB2YMWL4Ji',   'CLIENT'),
    ('Tina Martinez', 'tina.martinez@example.com', '1234567809', '$2a$10$OBp3oiPZBEoMoTXfN2IJie.GYqh1RgkFsLovoLJ1UPSLB2YMWL4Ji', 'CLIENT');