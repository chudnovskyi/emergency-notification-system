INSERT INTO clients (id, role, email, password)
VALUES (1, 'USER', 'authenitcated@gmail.com', '$2a$12$npggcybq6606zDlX5XpkAuvFUnnXeG6sQKALWfTtvtLBsRTs.cFTG'),
       (2, 'USER', 'second@gmail.com', '$2a$12$npggcybq6606zDlX5XpkAuvFUnnXeG6sQKALWfTtvtLBsRTs.cFTG');
SELECT SETVAL('clients_id_seq', (SELECT MAX(id) FROM clients));
