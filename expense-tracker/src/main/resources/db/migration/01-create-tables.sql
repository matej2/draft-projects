CREATE TABLE IF NOT EXISTS frequency (
     id serial primary key,
     number integer,
     description varchar(100)
);

CREATE TABLE IF NOT EXISTS expense (
    id serial primary key,
    note varchar(100),
    cost integer,
    expense_date date,
    frequency_id integer references frequency(id)
);

INSERT INTO
    frequency (number, description)
VALUES
    (1, 'Yearly'),
    (12, 'Monthly'),
    (52, 'Weekly'),
    (365, 'Daily');

-- Config for app_user
CREATE USER app_user WITH PASSWORD '${app_user}';
GRANT USAGE ON SCHEMA public TO app_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO app_user;

GRANT SELECT, INSERT, UPDATE ON expense TO app_user;
GRANT SELECT ON frequency TO app_user;
