INSERT IGNORE INTO user_credentials (id, city, headline, mobile, postal_code, profile_image)
VALUES (1, NULL, NULL, NULL, NULL, 'sbcf-default-avatar.png');

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