CREATE TABLE task_status
(
    status_id INT PRIMARY KEY NOT NULL,
    name      VARCHAR(20)     NOT NULL,
    CONSTRAINT task_status_primary_key PRIMARY KEY (status_id)
);

CREATE TABLE task
(
    task_id      INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    task_date    DATE                           NOT NULL,
    first_name   VARCHAR(20)                    NOT NULL,
    name         VARCHAR(20)                    NOT NULL,
    middle_name  VARCHAR(20)                    NOT NULL,
    nationality  VARCHAR(2)                     NOT NULL,
    email        VARCHAR(30),
    phone_number VARCHAR(11),
    status_id    INT                            NOT NULL,
    CONSTRAINT task_primary_key PRIMARY KEY (task_id),
    CONSTRAINT task_status_foreign_key FOREIGN KEY (status_id) references task_status (status_id)
);