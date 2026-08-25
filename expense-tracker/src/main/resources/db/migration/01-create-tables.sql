-- TABLES: Domain

CREATE TABLE IF NOT EXISTS frequency (
     id serial primary key,
     number integer,
     description varchar(100)
);

CREATE TABLE IF NOT EXISTS category (
    id serial primary key,
    name varchar(100)
);


CREATE TABLE IF NOT EXISTS expense (
    id serial primary key,
    note varchar(100),
    cost integer,
    expense_date date,
    frequency integer references frequency(id),
    category integer references category(id)
);


-- TABLES: JWT

CREATE TABLE IF NOT EXISTS registereduser (
    id serial primary key,
    firstname varchar(100),
    lastname varchar(100),
    email varchar(100),
    password varchar(100),
    role varchar(100)
);

CREATE TABLE IF NOT EXISTS token (
    id serial primary key,
    token varchar(255),
    token_type varchar(100),
    revoked boolean,
    expired boolean,
    user_id integer references registereduser(id)
);

-- Read only data

INSERT INTO
    frequency (number, description)
VALUES
    (1, 'Yearly'),
    (12, 'Monthly'),
    (52, 'Weekly'),
    (365, 'Daily');

INSERT INTO
    category (name)
VALUES
    ('Groceries'),
    ('Leisure'),
    ('Electronics'),
    ('Utilities'),
    ('Clothing'),
    ('Health'),
    ('Transportation'),
    ('Others');

-- Config for app_user
CREATE USER app_user WITH PASSWORD '${app_user}';
GRANT USAGE ON SCHEMA public TO app_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO app_user;

-- Permission grants: Domain
GRANT SELECT, INSERT, UPDATE ON expense TO app_user;
GRANT SELECT ON frequency TO app_user;
GRANT SELECT ON category TO app_user;

-- Permission grants: JWT
GRANT SELECT, INSERT ON registereduser TO app_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON token to app_user;
