# Sistema de Gestión de Pañol - Duoc UC 🛠️📚

Sistema web para la administración, control y trazabilidad de los recursos, equipos y herramientas de la  
**Escuela de Informática y Telecomunicaciones del Duoc UC**.

El sistema moderniza la gestión manual del pañol, permitiendo un control eficiente del inventario, préstamos, devoluciones y solicitudes mediante una arquitectura de microservicios.

---

## 📋 Tabla de Contenidos

- [Contexto del Proyecto](#-contexto-del-proyecto)
- [Arquitectura y Tecnologías](#-arquitectura-y-tecnologías)
- [Estructura del Repositorio](#-estructura-del-repositorio)
- [Pre-requisitos](#️-pre-requisitos)
- [Instalación y Configuración](#-instalación-y-configuración)
  - [1. Base de Datos](#1-base-de-datos)
  - [2. Backend (Microservicios)](#2-backend-microservicios)
  - [3. Frontend](#3-frontend)
- [Funcionalidades Principales](#-funcionalidades-principales)
- [Estado del Proyecto](#-estado-del-proyecto)
- [Equipo](#-equipo)

---

## 📄 Contexto del Proyecto

Basado en el **Acta de Constitución**, este proyecto surge de la necesidad de optimizar los tiempos de atención y reducir la pérdida de material en el pañol.

**Propósito**
- Desarrollar un sistema informático que gestione la trazabilidad de préstamos y el control de stock.

**Usuarios**
- Pañoleros (Administradores)
- Docentes
- Alumnos (Solicitantes)

**Alcance**
- Gestión de inventario
- Solicitudes de materiales vía web
- Validación de préstamos y devoluciones
- Visualización de historial de movimientos

---

## 🚀 Arquitectura y Tecnologías

El sistema utiliza una arquitectura de **microservicios** para el backend y una aplicación web ligera para el frontend.

### Backend (Java / Spring Boot)
- **Lenguaje:** Java 17  
- **Framework:** Spring Boot 3.x  
- **Gestión de dependencias:** Maven  
- **Persistencia:** JPA / Hibernate  
- **Base de datos:** MySQL  

### Frontend (Web)
- **Tecnologías:** HTML5, CSS3, JavaScript (Vanilla ES6)
- **Estilos:** CSS personalizado (Variables CSS)

---

## 📂 Estructura del Repositorio

```text
/
├── BaseDeDatos.sql               # Script de creación de tablas y relaciones
├── backend/                      # Código fuente de los microservicios
│   ├── api-historial/            # Auditoría e historial (Puerto 8085)
│   ├── api-inventario/           # Gestión de productos y stock (Puerto 8081)
│   ├── api-prestamos/            # Préstamos y devoluciones (Puerto 8082)
│   ├── api-solicitudes/          # Solicitudes de materiales (Puerto 8083)
│   ├── api-usuarios/             # Gestión de usuarios y roles (Puerto 8084)
│   └── api-codigos-barras/       # Lectura y gestión de códigos de barras (Puerto 8086)
└── frontend/                     # Interfaz de usuario web
    ├── assets/
    │   ├── css/                  # Hojas de estilo
    │   └── js/                   # Lógica del cliente
    └── index.html                # Vista principal

```

---

## ⚙️ Pre-requisitos

* **Java JDK 17** o superior.
* **MySQL Server** (corriendo en el puerto 3306).
* **Navegador web moderno** (Chrome, Firefox, Edge).
* (Opcional) **Maven** instalado o uso del wrapper `mvnw` incluido.

---

## 🔧 Instalación y Configuración

### 1. Base de Datos

1. Crear una base de datos MySQL llamada `bdpanol`.
2. Ejecutar el script `BaseDeDatos.sql` ubicado en la raíz del proyecto.

```sql
CREATE DATABASE bdpanol;
USE bdpanol;
-- Copiar y ejecutar el contenido de BaseDeDatos.sql aquí

```

### 2. Backend (Microservicios)

Cada microservicio es una aplicación Spring Boot independiente.

1. **Ejecutar microservicios:** Navegar a la carpeta de cada servicio y ejecutar:
```bash
./mvnw spring-boot:run

```



#### Puertos Configurados

| Servicio | Puerto | Descripción |
| --- | --- | --- |
| **API Inventario** | `8081` | Productos y Stock |
| **API Préstamos** | `8082` | Flujo de préstamos |
| **API Solicitudes** | `8083` | Peticiones de alumnos |
| **API Usuarios** | `8084` | Login y Roles |
| **API Historial** | `8085` | Logs y Auditoría |
| **API Cód. Barras** | `8086` | Lectura de equipos |

### 2. Frontend

1. Ir a la carpeta `frontend/`.
2. Abrir `assets/js/app.js` y verificar que las URLs de las APIs apunten a `localhost` y los puertos correctos.
3. Ejecutar la web.

> **⚠️ Importante:** Para evitar errores de **CORS**, se recomienda no abrir el archivo `index.html` directamente con doble clic. Utiliza una extensión como **Live Server** en VS Code o levanta un servidor local simple.

---

## ✨ Funcionalidades Principales

### 📦 Gestión de Inventario

* Alta, baja y modificación de productos.
* Clasificación por categoría, marca y ubicación física.
* Control de stock total, disponible y prestado.

### 📝 Solicitudes y Préstamos

* Creación de solicitudes por alumnos y docentes.
* Validación de disponibilidad en tiempo real.
* Registro automático de fechas de préstamo y devolución.

### 🔄 Devoluciones

* Registro de devoluciones con actualización automática de stock.

### 🏷️ Códigos de Barras

* Asociación de códigos de barras a productos.
* Búsqueda rápida de productos mediante código (soporte para lector físico o manual).

### 👥 Usuarios y Roles

* **Jefe de Carrera:** Supervisión total.
* **Pañolero:** Gestión operativa.
* **Docentes/Alumnos:** Solicitud de recursos.

---

## 📌 Estado del Proyecto

* **Tipo:** Proyecto académico.
* **Estado:** Funcional en entorno local.
* **Arquitectura:** Microservicios.
* **Despliegue:** No desplegado en producción.

---

## 👥 Equipo

Proyecto desarrollado para la **Escuela de Informática y Telecomunicaciones – Duoc UC**.

* **Patrocinador:** Director de Escuela
* **Gerente de Proyecto:** Jefe de Carrera
* **Desarrollo:** Javier Parra / Abigail Maripan
* **QA / Testing:** Equipo de validación

```

```
