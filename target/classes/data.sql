-- Datos iniciales v2.0
INSERT INTO EMPRESA (nombre, sector, web, fechaRegistro) VALUES 
('NTT Data', 'IT', 'https://ntt.jobs', '2026-01-01'),
('Accenture', 'Consultoría', 'https://accenture.jobs', '2026-01-01'),
('Indra', 'IT', 'https://indra.jobs', '2026-01-01'),
('Mercadona', 'Retail', 'https://mercadona.jobs', '2026-01-01'),
('Deloitte', 'Consultoría', 'https://deloitte.jobs', '2026-01-01'),
('Telefónica', 'Telecom', 'https://telefonica.jobs', '2026-01-01'),
('TechCorp', 'IT', 'https://example.com', '2026-01-01'),
('StartupX', 'IT', 'https://startupx.com', '2026-01-01'),
('Corp', 'General', 'https://corp.com', '2026-01-01');

INSERT INTO OFERTA (id_empresa, titulo, descripcion, estado, fechaPostulacion, enlace, salario, notas) VALUES 
(1, 'Backend Software Engineer', 'API REST con Java 21', 'ACEPTADA', '2026-09-01', 'https://ntt.jobs/3', 30000.0, 'Notas NTT'),
(2, 'Fullstack Developer', 'Node.js y Angular', 'RECHAZADA', '2026-09-10', 'https://accenture.jobs/4', 26000.0, 'Notas Acc'),
(3, 'QA Automation Engineer', 'Pruebas con Selenium', 'ENTREVISTA', '2026-09-12', 'https://indra.jobs/5', 27000.0, 'Notas Indra'),
(5, 'Analista Programador Java', 'Microservicios', 'PENDIENTE', '2026-09-18', 'https://deloitte.jobs/6', 32000.0, 'Notas Del'),
(4, 'Desarrollador Mobile Flutter', 'App móvil', 'ACEPTADA', '2026-09-22', 'https://mercadona.jobs/7', 29000.0, 'Notas Merc'),
(1, 'DevOps Engineer Junior', 'Docker y Kubernetes', 'RECHAZADA', '2026-09-25', 'https://ntt.jobs/8', 28000.0, 'Notas NTT2'),
(2, 'Consultor Tecnológico', 'Consultoría IT', 'PENDIENTE', '2026-09-28', 'https://accenture.jobs/9', 31000.0, 'Notas Acc2'),
(3, 'Desarrollador Java Senior', 'Líder técnico', 'ACEPTADA', '2026-10-02', 'https://indra.jobs/10', 42000.0, 'Notas Indra2'),
(4, 'Data Analyst', 'Python y SQL', 'ENTREVISTA', '2026-10-05', 'https://mercadona.jobs/11', 27000.0, 'Notas Merc2'),
(5, 'Desarrollador Web Fullstack', 'Vue.js y Spring', 'RECHAZADA', '2026-10-08', 'https://deloitte.jobs/12', 29000.0, 'Notas Del2'),
(1, 'Ingeniero de Software', 'Backend nube', 'PENDIENTE', '2026-10-12', 'https://ntt.jobs/13', 35000.0, 'Notas NTT3'),
(3, 'Soporte Técnico N2', 'Gestión de incidencias', 'RECHAZADA', '2026-10-15', 'https://indra.jobs/14', 22000.0, 'Notas Indra3'),
(2, 'Arquitecto Software', 'Sistemas distribuidos', 'ENTREVISTA', '2026-10-20', 'https://accenture.jobs/15', 48000.0, 'Notas Acc3'),
(3, 'Dev Java Junior', 'Java Básico', 'PENDIENTE', '2026-10-25', NULL, NULL, 'Notas Indra4'),
(4, 'DevOps Engineer', 'Docker y Kubernetes', 'PENDIENTE', '2026-10-05', 'https://mercadona.jobs/101', 30000.0, 'Notas Merc3'),
(6, 'Data Analyst', 'Python y SQL', 'ENTREVISTA', '2026-10-08', 'https://telefonica.jobs/102', 29000.0, 'Notas Tele'),
(7, 'Java Developer', 'Junior role', 'PENDIENTE', '2026-10-25', 'https://example.com', 30000.0, 'Notas Tech'),
(8, 'Backend Dev', 'Backend', 'ENTREVISTA', '2026-10-26', NULL, NULL, 'Notas Start'),
(4, 'DevOps Engineer 2', 'Kubernetes', 'PENDIENTE', '2026-10-05', 'https://mercadona.jobs/101', 30000.0, 'Notas Merc4'),
(6, 'Data Analyst 2', 'Python', 'ENTREVISTA', '2026-10-08', 'https://telefonica.jobs/102', 29000.0, 'Notas Tele2'),
(9, 'Dev', 'General', 'PENDIENTE', '2026-10-25', NULL, NULL, 'Notas Corp');

INSERT INTO ENTREVISTA (id_oferta, fecha, tipo, notas, resultado) VALUES 
(3, '2026-09-20', 'Técnica', 'Buena base de Selenium', 'APTO'),
(9, '2026-10-10', 'HR', 'Perfil muy encajado', 'PENDIENTE'),
(13, '2026-10-22', 'Técnica', 'Arquitectura compleja', 'APTO'),
(16, '2026-10-12', 'Técnica', 'Python avanzado', 'PENDIENTE'),
(20, '2026-10-12', 'Técnica', 'Python avanzado', 'PENDIENTE');
