CREATE DATABASE IF NOT EXISTS bdpanol
CHARACTER SET utf8mb4
COLLATE utf8mb4_general_ci;

USE bdpanol;

/* =========================
CREACIÓN DE TABLAS
========================= */
-- 1. ROL
CREATE TABLE ROL (
    id_rol CHAR(1) NOT NULL,
    nombre_rol VARCHAR(25) NOT NULL,
    PRIMARY KEY (id_rol)
);

-- 2. ESTADO
CREATE TABLE ESTADO (
    id_estado INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(25) NOT NULL,
    PRIMARY KEY (id_estado)
);

-- 3. DISPONIBILIDAD
CREATE TABLE DISPONIBILIDAD (
    id_disp INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(25) NOT NULL,
    PRIMARY KEY (id_disp)
);

-- 4. CATEGORIA_PROD
CREATE TABLE CATEGORIA_PROD (
    id_categoria INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(25) NOT NULL,
    PRIMARY KEY (id_categoria)
);

-- 5. MARCA
CREATE TABLE MARCA (
    id_marca INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(25) NOT NULL,
    PRIMARY KEY (id_marca)
);

-- 6. STOCK
CREATE TABLE STOCK (
    id_stock INT NOT NULL AUTO_INCREMENT,
    stock_minimo INT NOT NULL,
    stock_maximo INT NOT NULL,
    cantidad INT NOT NULL,
    PRIMARY KEY (id_stock)
);

-- 7. UBICACION_INV
CREATE TABLE UBICACION_INV (
    id_ubicacion INT NOT NULL AUTO_INCREMENT,
    nombre_sala VARCHAR(50) NOT NULL,
    estante VARCHAR(20) NOT NULL,
    nivel INT NOT NULL,
    descripcion VARCHAR(100),
    PRIMARY KEY (id_ubicacion)
);

-- 8. USUARIO
CREATE TABLE USUARIO (
    rut INT NOT NULL,
    dv_rut CHAR(1) NOT NULL,
    pnombre VARCHAR(25) NOT NULL,
    snombre VARCHAR(25),
    ap_paterno VARCHAR(25) NOT NULL,
    ap_materno VARCHAR(25),
    actividad CHAR(1) NOT NULL,
    email VARCHAR(60) NOT NULL,
    ROL_id_rol CHAR(1) NOT NULL,
    PRIMARY KEY (rut),
    UNIQUE (email),
    CONSTRAINT FK_USUARIO_ROL
        FOREIGN KEY (ROL_id_rol) REFERENCES ROL(id_rol),
    CONSTRAINT OK_DV_RUT
        CHECK (dv_rut IN ('0','1','2','3','4','5','6','7','8','9','K'))
);

-- 9. INVENTARIO
CREATE TABLE INVENTARIO (
    id_inventario INT NOT NULL AUTO_INCREMENT,
    observacion VARCHAR(100),
    fecha_actualizacion DATE NOT NULL,
    UBICACION_INV_id_ubicacion INT NOT NULL,
    STOCK_id_stock INT NOT NULL,
    PRIMARY KEY (id_inventario),
    CONSTRAINT FK_INVENTARIO_UBICACION
        FOREIGN KEY (UBICACION_INV_id_ubicacion) REFERENCES UBICACION_INV(id_ubicacion),
    CONSTRAINT FK_INVENTARIO_STOCK
        FOREIGN KEY (STOCK_id_stock) REFERENCES STOCK(id_stock)
);

-- 10. PRODUCTO
CREATE TABLE PRODUCTO (
    id_principal INT NOT NULL AUTO_INCREMENT,
    cod_interno BIGINT NOT NULL,
    estado VARCHAR(15) NOT NULL,
    nombre_producto VARCHAR(25) NOT NULL,
    marca VARCHAR(25),
    INVENTARIO_id_inventario INT NOT NULL,
    ESTADO_id_estado INT NOT NULL,
    DISPONIBILIDAD_id_disp INT NOT NULL,
    CATEGORIA_PROD_id_categoria INT NOT NULL,
    MARCA_id_marca INT NOT NULL,
    PRIMARY KEY (id_principal),
    UNIQUE (cod_interno),
    CONSTRAINT FK_PRODUCTO_INVENTARIO
        FOREIGN KEY (INVENTARIO_id_inventario) REFERENCES INVENTARIO(id_inventario),
    CONSTRAINT FK_PRODUCTO_ESTADO
        FOREIGN KEY (ESTADO_id_estado) REFERENCES ESTADO(id_estado),
    CONSTRAINT FK_PRODUCTO_DISP
        FOREIGN KEY (DISPONIBILIDAD_id_disp) REFERENCES DISPONIBILIDAD(id_disp),
    CONSTRAINT FK_PRODUCTO_CATEGORIA
        FOREIGN KEY (CATEGORIA_PROD_id_categoria) REFERENCES CATEGORIA_PROD(id_categoria),
    CONSTRAINT FK_PRODUCTO_MARCA
        FOREIGN KEY (MARCA_id_marca) REFERENCES MARCA(id_marca),
    CONSTRAINT OK_ESTADO_PRODUCTO
        CHECK (estado IN ('1'))
);

-- 11. IMAGEN
CREATE TABLE IMAGEN (
    id_imagen INT NOT NULL AUTO_INCREMENT,
    url VARCHAR(500) NOT NULL,
    es_img_principal CHAR(1) NOT NULL,
    PRODUCTO_id_principal INT NOT NULL,
    PRIMARY KEY (id_imagen),
    UNIQUE (url),
    CONSTRAINT FK_IMAGEN_PRODUCTO
        FOREIGN KEY (PRODUCTO_id_principal) REFERENCES PRODUCTO(id_principal)
);

-- 12. SOLI_PRESTAMO
CREATE TABLE SOLI_PRESTAMO (
    id_prestamo INT NOT NULL AUTO_INCREMENT,
    estado_solicitud VARCHAR(15) NOT NULL,
    USUARIO_rut INT NOT NULL,
    fecha_solicitud DATE NOT NULL,
    motivo_prestamo VARCHAR(100) NOT NULL,
    prioridad VARCHAR(25) NOT NULL,
    PRIMARY KEY (id_prestamo),
    CONSTRAINT FK_SOLI_USUARIO
        FOREIGN KEY (USUARIO_rut) REFERENCES USUARIO(rut),
    CONSTRAINT OK_ESTADO_SOLICITUD
        CHECK (estado_solicitud IN ('aprobado','pendiente'))
);

-- 13. DET_PRESTAMO
CREATE TABLE DET_PRESTAMO (
    id_detalle INT NOT NULL AUTO_INCREMENT,
    cantidad INT NOT NULL,
    fecha_incio_prestamo DATE NOT NULL,
    fecha_retorno_prestamo DATE NOT NULL,
    fecha_devolucion_prestamo DATE,
    SOLI_PRESTAMO_id_prestamo INT NOT NULL,
    PRODUCTO_id_principal INT NOT NULL,
    PRIMARY KEY (id_detalle),
    CONSTRAINT FK_DET_SOLI
        FOREIGN KEY (SOLI_PRESTAMO_id_prestamo) REFERENCES SOLI_PRESTAMO(id_prestamo),
    CONSTRAINT FK_DET_PRODUCTO
        FOREIGN KEY (PRODUCTO_id_principal) REFERENCES PRODUCTO(id_principal)
);

-- 14. AUTORIZACION
CREATE TABLE AUTORIZACION (
    id_autorizacion INT NOT NULL AUTO_INCREMENT,
    fecha_autorizacion DATE NOT NULL,
    estado VARCHAR(25) NOT NULL,
    comentario VARCHAR(100) NOT NULL,
    USUARIO_rut INT NOT NULL,
    SOLI_PRESTAMO_id_prestamo INT NOT NULL,
    PRIMARY KEY (id_autorizacion),
    CONSTRAINT FK_AUT_USUARIO
        FOREIGN KEY (USUARIO_rut) REFERENCES USUARIO(rut),
    CONSTRAINT FK_AUT_SOLI
        FOREIGN KEY (SOLI_PRESTAMO_id_prestamo) REFERENCES SOLI_PRESTAMO(id_prestamo)
);

-- 15. AUDITORIA_INV
CREATE TABLE AUDITORIA_INV (
    id_auditoria INT NOT NULL AUTO_INCREMENT,
    fecha_cambio DATE NOT NULL,
    tipo_movimiento VARCHAR(25) NOT NULL,
    cantidad_anterior INT NOT NULL,
    cantidad_nueva INT NOT NULL,
    motivo VARCHAR(100) NOT NULL,
    USUARIO_rut INT NOT NULL,
    INVENTARIO_id_inventario INT NOT NULL,
    PRIMARY KEY (id_auditoria),
    CONSTRAINT FK_AUD_USUARIO
        FOREIGN KEY (USUARIO_rut) REFERENCES USUARIO(rut),
    CONSTRAINT FK_AUD_INVENTARIO
        FOREIGN KEY (INVENTARIO_id_inventario) REFERENCES INVENTARIO(id_inventario)
);


 /* =========================
   DATOS DE PRUEBA PAÑOL
   ========================= */

/* ---------- ROLES ---------- */
INSERT INTO ROL (id_rol, nombre_rol) VALUES
('A','Administrador'),
('P','Pañolero'),
('D','Docente');

/* ---------- ESTADOS ---------- */
INSERT INTO ESTADO (nombre) VALUES
('Activo'),
('Inactivo'),
('Dañado');

/* ---------- DISPONIBILIDAD ---------- */
INSERT INTO DISPONIBILIDAD (nombre) VALUES
('Disponible'),
('Prestado'),
('En mantención');

/* ---------- CATEGORÍAS ---------- */
INSERT INTO CATEGORIA_PROD (nombre) VALUES
('Herramienta'),
('Equipo'),
('Material');

/* ---------- MARCAS ---------- */
INSERT INTO MARCA (nombre) VALUES
('Bosch'),
('HP'),
('Makita'),
('Genérica');

/* ---------- USUARIOS ---------- */
INSERT INTO USUARIO
(rut, dv_rut, pnombre, snombre, ap_paterno, ap_materno, actividad, email, ROL_id_rol)
VALUES
(11111111,'1','Juan',NULL,'Pérez','Gómez','1','juan.perez@duoc.cl','P'),
(22222222,'2','María','José','Soto','López','1','maria.soto@duoc.cl','D'),
(33333333,'3','Carlos',NULL,'Ramírez','Díaz','1','carlos.ramirez@duoc.cl','A');

/* ---------- UBICACIONES ---------- */
INSERT INTO UBICACION_INV
(nombre_sala, estante, nivel, descripcion)
VALUES
('Sala Pañol','Estante A',1,'Herramientas eléctricas'),
('Sala Pañol','Estante B',2,'Equipos computacionales');

/* ---------- STOCK ---------- */
INSERT INTO STOCK (stock_minimo, stock_maximo, cantidad) VALUES
(5, 50, 30),
(2, 20, 10);

/* ---------- INVENTARIO ---------- */
INSERT INTO INVENTARIO
(observacion, fecha_actualizacion, UBICACION_INV_id_ubicacion, STOCK_id_stock)
VALUES
('Inventario herramientas','2025-12-01',1,1),
('Inventario equipos','2025-12-01',2,2);

/* ---------- PRODUCTOS ---------- */
INSERT INTO PRODUCTO
(cod_interno, estado, nombre_producto, marca,
 INVENTARIO_id_inventario, ESTADO_id_estado,
 DISPONIBILIDAD_id_disp, CATEGORIA_PROD_id_categoria, MARCA_id_marca)
VALUES
(100001,'1','Taladro','Bosch',1,1,1,1,1),
(100002,'1','Notebook','HP',2,1,1,2,2),
(100003,'1','Esmeril','Makita',1,1,2,1,3);

/* ---------- IMÁGENES ---------- */
INSERT INTO IMAGEN
(url, es_img_principal, PRODUCTO_id_principal)
VALUES
('https://img.duoc.cl/taladro.jpg','S',1),
('https://img.duoc.cl/notebook.jpg','S',2);

/* ---------- SOLICITUD DE PRÉSTAMO ---------- */
INSERT INTO SOLI_PRESTAMO
(estado_solicitud, USUARIO_rut, fecha_solicitud, motivo_prestamo, prioridad)
VALUES
('pendiente',22222222,'2025-12-10','Clase práctica de redes','Alta');

/* ---------- DETALLE PRÉSTAMO ---------- */
INSERT INTO DET_PRESTAMO
(cantidad, fecha_incio_prestamo, fecha_retorno_prestamo,
 SOLI_PRESTAMO_id_prestamo, PRODUCTO_id_principal)
VALUES
(1,'2025-12-11','2025-12-15',1,2);

/* ---------- AUTORIZACIÓN ---------- */
INSERT INTO AUTORIZACION
(fecha_autorizacion, estado, comentario, USUARIO_rut, SOLI_PRESTAMO_id_prestamo)
VALUES
('2025-12-11','Aprobado','Autorizado por pañolero',11111111,1);

/* ---------- AUDITORÍA INVENTARIO ---------- */
INSERT INTO AUDITORIA_INV
(fecha_cambio, tipo_movimiento, cantidad_anterior, cantidad_nueva,
 motivo, USUARIO_rut, INVENTARIO_id_inventario)
VALUES
('2025-12-11','Préstamo',10,9,'Préstamo a docente',11111111,2);

/* =========================
   FIN DATOS DE PRUEBA
   ========================= */