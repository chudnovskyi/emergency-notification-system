INSERT INTO clients (id, role, email)
VALUES (1, 'USER', 'authenitcated@gmail.com'),
       (2, 'USER', 'dummy@gmail.com');
SELECT SETVAL('clients_id_seq', (SELECT MAX(id) FROM clients));
