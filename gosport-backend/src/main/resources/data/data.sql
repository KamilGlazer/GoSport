-- User 1
INSERT IGNORE INTO user_credentials (id, city, headline, mobile, postal_code, profile_image)
VALUES (1, 'Warszawa', 'Trener z wieloletnim doświadczeniem – zapraszam do kontaktu!', '+48 600123456', '00-001', 'sbcf-default-avatar.png');

INSERT IGNORE INTO user (id, email, first_name, last_name, password, role, credentials_id)
VALUES (
    1,
    'k.glazer@test.pl',
    'Kamil',
    'Glazer',
    '$2a$10$NEVHvNM2RyBu9csf8Dvaeu.qj0xPd5gZQ8UOaaVEg14dBb9NmKydK',
    'USER',
    1
);


INSERT IGNORE INTO user_credentials (id, city, headline, mobile, postal_code, profile_image)
VALUES (2, 'Kraków', 'Specjalizuję się w przygotowaniu motorycznym – zapraszam do kontaktu!', '+48 600111222', '31-123', 'trainer1.jpg');

INSERT IGNORE INTO user (id, email, first_name, last_name, password, role, credentials_id)
VALUES (
    2,
    'm.jankowski@test.pl',
    'Marek',
    'Jankowski',
    '$2a$10$NEVHvNM2RyBu9csf8Dvaeu.qj0xPd5gZQ8UOaaVEg14dBb9NmKydK',
    'USER',
    2
);

INSERT IGNORE INTO user_credentials (id, city, headline, mobile, postal_code, profile_image)
VALUES (3, 'Warszawa', 'Pomagam zawodnikom osiągać ich cele – napisz na priv.', '+48 600222333', '00-002', 'trainer2.jpg');

INSERT IGNORE INTO user (id, email, first_name, last_name, password, role, credentials_id)
VALUES (
    3,
    'a.kowalski@test.pl',
    'Adam',
    'Kowalski',
    '$2a$10$NEVHvNM2RyBu9csf8Dvaeu.qj0xPd5gZQ8UOaaVEg14dBb9NmKydK',
    'USER',
    3
);


INSERT IGNORE INTO user_credentials (id, city, headline, mobile, postal_code, profile_image)
VALUES (4, 'Kraków', 'Trener personalny i motywator – pisz śmiało!', '+48 600333444', '30-456', 'trainer3.jpg');

INSERT IGNORE INTO user (id, email, first_name, last_name, password, role, credentials_id)
VALUES (
    4,
    't.nowak@test.pl',
    'Tomasz',
    'Nowak',
    '$2a$10$NEVHvNM2RyBu9csf8Dvaeu.qj0xPd5gZQ8UOaaVEg14dBb9NmKydK',
    'USER',
    4
);


INSERT IGNORE INTO user_credentials (id, city, headline, mobile, postal_code, profile_image)
VALUES (5, 'Warszawa', 'Trenerka z pasją do sportu – chcesz zacząć? Napisz!', '+48 600444555', '00-050', 'trainer4.webp');

INSERT IGNORE INTO user (id, email, first_name, last_name, password, role, credentials_id)
VALUES (
    5,
    'a.kowalczyk@test.pl',
    'Anna',
    'Kowalczyk',
    '$2a$10$NEVHvNM2RyBu9csf8Dvaeu.qj0xPd5gZQ8UOaaVEg14dBb9NmKydK',
    'USER',
    5
);


INSERT IGNORE INTO user_credentials (id, city, headline, mobile, postal_code, profile_image)
VALUES (6, 'Kraków', 'Pomagam kobietom osiągać formę życia – zapraszam!', '+48 600555666', '31-987', 'trainer5.webp');

INSERT IGNORE INTO user (id, email, first_name, last_name, password, role, credentials_id)
VALUES (
    6,
    'm.zalewska@test.pl',
    'Magdalena',
    'Zalewska',
    '$2a$10$NEVHvNM2RyBu9csf8Dvaeu.qj0xPd5gZQ8UOaaVEg14dBb9NmKydK',
    'USER',
    6
);


INSERT IGNORE INTO user_credentials (id, city, headline, mobile, postal_code, profile_image)
VALUES (7, 'Warszawa', 'Zmotywuję Cię do działania – odezwij się!', '+48 600666777', '00-123', 'trainer6.jpg');

INSERT IGNORE INTO user (id, email, first_name, last_name, password, role, credentials_id)
VALUES (
    7,
    'p.adamczyk@test.pl',
    'Piotr',
    'Adamczyk',
    '$2a$10$NEVHvNM2RyBu9csf8Dvaeu.qj0xPd5gZQ8UOaaVEg14dBb9NmKydK',
    'USER',
    7
);


INSERT IGNORE INTO user_credentials (id, city, headline, mobile, postal_code, profile_image)
VALUES (8, 'Kraków', 'Trener siłowy – startujemy razem? Napisz.', '+48 600777888', '30-333', 'trainer7.jpg');

INSERT IGNORE INTO user (id, email, first_name, last_name, password, role, credentials_id)
VALUES (
    8,
    'd.wojciechowski@test.pl',
    'Dawid',
    'Wojciechowski',
    '$2a$10$NEVHvNM2RyBu9csf8Dvaeu.qj0xPd5gZQ8UOaaVEg14dBb9NmKydK',
    'USER',
    8
);

INSERT IGNORE INTO trainer (id, is_trainer) VALUES (2, true);
INSERT IGNORE INTO trainer (id, is_trainer) VALUES (3, true);
INSERT IGNORE INTO trainer (id, is_trainer) VALUES (4, true);
INSERT IGNORE INTO trainer (id, is_trainer) VALUES (5, true);
INSERT IGNORE INTO trainer (id, is_trainer) VALUES (6, true);
INSERT IGNORE INTO trainer (id, is_trainer) VALUES (7, true);
INSERT IGNORE INTO trainer (id, is_trainer) VALUES (8, true);


INSERT IGNORE INTO connection (sender_id, receiver_id, status) VALUES (1, 2, 'ACCEPTED');
INSERT IGNORE INTO connection (sender_id, receiver_id, status) VALUES (1, 3, 'ACCEPTED');
INSERT IGNORE INTO connection (sender_id, receiver_id, status) VALUES (2, 4, 'ACCEPTED');
INSERT IGNORE INTO connection (sender_id, receiver_id, status) VALUES (3, 5, 'ACCEPTED');
INSERT IGNORE INTO connection (sender_id, receiver_id, status) VALUES (4, 6, 'ACCEPTED');
INSERT IGNORE INTO connection (sender_id, receiver_id, status) VALUES (5, 1, 'ACCEPTED');
INSERT IGNORE INTO connection (sender_id, receiver_id, status) VALUES (6, 7, 'ACCEPTED');
INSERT IGNORE INTO connection (sender_id, receiver_id, status) VALUES (7, 8, 'ACCEPTED');
INSERT IGNORE INTO connection (sender_id, receiver_id, status) VALUES (8, 2, 'ACCEPTED');


INSERT IGNORE INTO post (content, created_at, user_id) VALUES
('Hello! I’m just starting my trainer journey 🚀', NOW(), 1),
('Contact me for personal training sessions!', NOW(), 1),

('Fit is not a destination, it’s a way of life 💪', NOW(), 2),
('Available in Kraków – write me on priv!', NOW(), 2),

('Certified strength coach with 5 years experience.', NOW(), 3),
('Join my bootcamp in Warsaw this summer!', NOW(), 3),

('I focus on injury prevention and performance.', NOW(), 4),
('Working with both amateurs and pro athletes!', NOW(), 4),

('Group fitness sessions available every weekend!', NOW(), 5),
('Message me for women-only training programs 💃', NOW(), 5),

('Helping people reach their full potential 🌟', NOW(), 6),
('Online coaching available!', NOW(), 6),

('Transform your body, transform your life.', NOW(), 7),
('Ask about my free first session in Kraków!', NOW(), 7),

('Sports are my life – I love sharing my passion.', NOW(), 8),
('Now accepting new clients from Warsaw!', NOW(), 8);