-- Password are hashed using a BCrypt hash with a cost of 10
insert into user(email, password, authorities, enabled) values('user', '$2a$10$KoxHNdhOe6OY88Ybq6T2d.SGp6lVfj5ynY/QwaO5SRk998TgnYayi', 'USER', true); -- password = 'password' BCrypt 10 hashed
insert into user(email, password, authorities, enabled) values('admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'ADMIN, ROOT', true);
insert into user(email, password, authorities, enabled) values('expired', '$2a$10$PZ.A0IuNG958aHnKDzILyeD9k44EOi1Ny0VlAn.ygrGcgmVcg8PRK', 'USER', true);
