INSERT INTO task_status(status_id, name) VALUES (1, 'CREATED');
INSERT INTO task_status(status_id, name) VALUES (2, 'INPROGRESS');
INSERT INTO task_status(status_id, name) VALUES (3, 'ERROR');
INSERT INTO task_status(status_id, name) VALUES (4, 'CLOSED');

INSERT INTO task(task_date, first_name, name, middle_name, nationality, status_id) VALUES ('2019-04-19', 'Ivanov', 'Ivan', 'Ivanovich', 'RU', 1);
INSERT INTO task(task_date, first_name, name, middle_name, nationality, status_id) VALUES ('2019-04-19', 'Sidorov', 'Ivan', 'Ivanovich', 'BG', 2);
INSERT INTO task(task_date, first_name, name, middle_name, nationality, email, status_id) VALUES ('2019-04-19', 'Vanov', 'Van', 'Vanovich', 'RU', 'vanov@van.ov', 3);
INSERT INTO task(task_date, first_name, name, middle_name, nationality, email, phone_number, status_id) VALUES ('2019-04-19', 'Baranov', 'Ivan', 'Ivanovich', 'US', 'baranov@ivan.off', '77777777777', 4);
INSERT INTO task(task_date, first_name, name, middle_name, nationality, phone_number, status_id) VALUES ('2019-04-19', 'Dorov', 'Ivan', 'Ivanovich', 'GB', '88888888888', 1);
INSERT INTO task(task_date, first_name, name, middle_name, nationality, status_id) VALUES ('2019-04-19', 'Ivanov', 'Ivan', 'Ivanovich', 'RU', 2);