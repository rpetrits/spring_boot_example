INSERT INTO employee (first_name, last_name, phone, hire_date, current_branch_id) VALUES ('Max', 'Mustermann', '+43 664 1234567', '2020-01-15', NULL);
INSERT INTO employee (first_name, last_name, phone, hire_date, current_branch_id) VALUES ('Anna', 'Muster', '+43 664 7654321', '2019-03-01', NULL);
INSERT INTO employee (first_name, last_name, phone, hire_date, current_branch_id) VALUES ('Peter', 'Schmidt', '+43 676 9876543', '2021-06-01', NULL);

INSERT INTO branch (branch_name, address, manager_id) VALUES ('Wien Zentrale', 'Stephansplatz 1, 1010 Wien', 1);
INSERT INTO branch (branch_name, address, manager_id) VALUES ('Graz Filiale', 'Hauptplatz 5, 8010 Graz', 2);

UPDATE employee SET current_branch_id = 1 WHERE id IN (1, 2);
UPDATE employee SET current_branch_id = 2 WHERE id = 3;
