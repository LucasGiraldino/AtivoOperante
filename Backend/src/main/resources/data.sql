INSERT INTO usuario (usu_cpf, usu_email, usu_senha, usu_nivel)
SELECT 12111158963, 'admin@pm.br', 123321, 1
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE usu_email = 'admin@pm.br');

INSERT INTO usuario (usu_cpf, usu_email, usu_senha, usu_nivel)
SELECT 5488889915, 'ze@cidadao.com.br', 123, 2
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE usu_email = 'ze@cidadao.com.br');

INSERT INTO orgaos (org_nome)
SELECT 'SEDUC' WHERE NOT EXISTS (SELECT 1 FROM orgaos WHERE org_nome = 'SEDUC');

INSERT INTO orgaos (org_nome)
SELECT 'Policia militar' WHERE NOT EXISTS (SELECT 1 FROM orgaos WHERE org_nome = 'Policia militar');

INSERT INTO orgaos (org_nome)
SELECT 'Policia Civil' WHERE NOT EXISTS (SELECT 1 FROM orgaos WHERE org_nome = 'Policia Civil');

INSERT INTO orgaos (org_nome)
SELECT 'SETRAN' WHERE NOT EXISTS (SELECT 1 FROM orgaos WHERE org_nome = 'SETRAN');

INSERT INTO tipo (tip_nome)
SELECT 'transito' WHERE NOT EXISTS (SELECT 1 FROM tipo WHERE tip_nome = 'transito');

INSERT INTO tipo (tip_nome)
SELECT 'educacao' WHERE NOT EXISTS (SELECT 1 FROM tipo WHERE tip_nome = 'educacao');

INSERT INTO tipo (tip_nome)
SELECT 'ambiental' WHERE NOT EXISTS (SELECT 1 FROM tipo WHERE tip_nome = 'ambiental');

INSERT INTO tipo (tip_nome)
SELECT 'seguranca' WHERE NOT EXISTS (SELECT 1 FROM tipo WHERE tip_nome = 'seguranca');
