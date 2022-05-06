-- Password are hashed using a BCrypt hash with a cost of 10
insert into user(id, email, password, authorities, enabled) values(1, 'user', '$2a$10$KoxHNdhOe6OY88Ybq6T2d.SGp6lVfj5ynY/QwaO5SRk998TgnYayi', 'USER', true); -- password = 'password' BCrypt 10 hashed
insert into user(id, email, password, authorities, enabled) values(2, 'admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'ADMIN, ROOT', true);
insert into user(id, email, password, authorities, enabled) values(3, 'expired', '$2a$10$PZ.A0IuNG958aHnKDzILyeD9k44EOi1Ny0VlAn.ygrGcgmVcg8PRK', 'USER', true);
