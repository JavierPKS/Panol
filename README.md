# Sistema de Gestión de Pañol - Duoc UC 🛠️📚

Este proyecto consiste en una solución integral para la administración, control y trazabilidad de los recursos, equipos y herramientas de la **Escuela de Informática y Telecomunicaciones del Duoc UC**.

El sistema moderniza la gestión manual actual, permitiendo un control eficiente del inventario, préstamos, devoluciones y solicitudes mediante una arquitectura de microservicios.

## 📋 Tabla de Contenidos

  - [Contexto del Proyecto](https://www.google.com/search?q=%23-contexto-del-proyecto)
  - [Arquitectura y Tecnologías](https://www.google.com/search?q=%23-arquitectura-y-tecnolog%C3%ADas)
  - [Estructura del Repositorio](https://www.google.com/search?q=%23-estructura-del-repositorio)
  - [Pre-requisitos](https://www.google.com/search?q=%23-pre-requisitos)
  - [Instalación y Configuración](https://www.google.com/search?q=%23-instalaci%C3%B3n-y-configuraci%C3%B3n)
      - [1. Base de Datos](https://www.google.com/search?q=%231-base-de-datos)
      - [2. Backend (Microservicios)](https://www.google.com/search?q=%232-backend-microservicios)
      - [3. Frontend](https://www.google.com/search?q=%233-frontend)
  - [Funcionalidades Principales](https://www.google.com/search?q=%23-funcionalidades-principales)
  - [Equipo](https://www.google.com/search?q=%23-equipo)

-----

## 📄 Contexto del Proyecto

Basado en el **Acta de Constitución**, este proyecto nace de la necesidad de optimizar los tiempos y reducir las pérdidas de material en el pañol.

  * **Propósito:** Desarrollar un sistema informático que gestione la trazabilidad de préstamos y el control de stock.
  * **Usuarios:** Pañoleros (Administradores), Docentes y Alumnos (Solicitantes).
  * **Alcance:** Gestión de inventario, solicitudes vía web/tótem, validación de préstamos y reportes históricos.

-----

## 🚀 Arquitectura y Tecnologías

El sistema utiliza una arquitectura de **Microservicios** para el backend y una aplicación web ligera para el frontend.

### Backend (Java / Spring Boot)

  * **Lenguaje:** Java 17
  * **Framework:** Spring Boot 3.x
  * **Gestión de Dependencias:** Maven
  * **Persistencia:** JPA / Hibernate
  * **Base de Datos:** MySQL

### Frontend (Web)

  * **Tecnologías:** HTML5, CSS3, JavaScript (Vanilla ES6)
  * **Estilos:** CSS personalizado (Variables CSS, Flexbox)

-----

## 📂 Estructura del Repositorio

El proyecto se divide en las siguientes carpetas principales:

```text
/
├── BaseDeDatos.sql          # Script de creación de tablas y relaciones
├── backend/                 # Código fuente de los microservicios
│   ├── api-historial/       # Servicio de logs e historial (Puerto 8085)
│   ├── api-inventario/      # Gestión de productos y stock
│   ├── api-prestamos/       # Lógica de prestar y devolver (Puerto 8082)
│   ├── api-solicitudes/     # Gestión de solicitudes de alumnos (Puerto 8083)
│   └── api-usuarios/        # Gestión de usuarios y roles
└── frontend/                # Interfaz de usuario web
    ├── assets/
    │   ├── css/             # Hoja de estilos (styles.css)
    │   └── js/              # Lógica del cliente (app.js)
    └── index.html           # Vista principal (SPA simple)
```

-----

## ⚙️ Pre-requisitos

  * **Java JDK 17** o superior.
  * **XAMPP MySQL Server** (corriendo en el puerto 3306).
  * **Navegador Web** moderno.
  * (Opcional) **Maven** instalado (o usar el wrapper `mvnw` incluido).

-----

## 🔧 Instalación y Configuración

### 1\. Base de Datos

1.  Crea una base de datos en MySQL llamada `bdpanol`.
2.  Ejecuta el script `BaseDeDatos.sql` proporcionado en la raíz del proyecto para generar las tablas (`PRODUCTO`, `STOCK`, `USUARIO`, etc.).

<!-- end list -->

```sql
CREATE DATABASE bdpanol;
USE bdpanol;
-- Copiar y pegar contenido de BaseDeDatos.sql
```

### 2\. Backend (Microservicios)

Cada microservicio es una aplicación Spring Boot independiente. Debes configurar las credenciales de base de datos en el archivo `application.properties` de cada API si tu usuario no es `root` o tienes contraseña.

**Ubicación de config:** `backend/api-{nombre}/src/main/resources/application.properties`

Ejemplo de configuración:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bdpanol?useSSL=false&...
spring.datasource.username=root
spring.datasource.password=TU_CONTRASEÑA
```

**Ejecución:**
Abre una terminal en la carpeta de cada microservicio y ejecuta:

```bash
./mvnw spring-boot:run
```

*Nota: Asegúrate de levantar los servicios necesarios (Inventario, Préstamos, Solicitudes, etc.).*

| Servicio | Puerto Configurado |
| :--- | :--- |
| API Inventario | 8081 |
| API Préstamos | 8082 |
| API Solicitudes | 8083 |
| API Usuarios | 8084 |
| API Historial | 8085 |

### 3\. Frontend

1.  Navega a la carpeta `frontend/`.
2.  Abre el archivo `assets/js/app.js`.
3.  Verifica la constante `API_BASE_URL`.
      * *Nota:* Actualmente apunta a `localhost:3000`. Si no estás usando un API Gateway, deberás apuntar directamente a los puertos de los microservicios según la funcionalidad que estés probando o configurar un proxy.
4.  Abre `index.html` en tu navegador.

-----

## ✨ Funcionalidades Principales

1.  **Gestión de Inventario:**

      * Alta, baja y modificación de productos.
      * Clasificación por Categoría, Marca y Ubicación física (Pasillo/Estante).
      * Visualización de stock total, disponible y prestado.

2.  **Solicitudes y Préstamos:**

      * Creación de solicitudes de material por parte de alumnos/docentes.
      * Validación de disponibilidad de stock.
      * Registro de fecha de inicio y retorno esperado.

3.  **Devoluciones:**

      * Registro de devoluciones que libera automáticamente el stock reservado.

4.  **Usuarios y Roles:**

      * Roles definidos: Jefe de Carrera, Pañolero, Docentes, Alumnos.

-----

## 👥 Equipo

Proyecto desarrollado para la **Escuela de Informática y Telecomunicaciones**.

  * **Patrocinador:** Director de Escuela.
  * **Gerente de Proyecto:** Jefe de Carrera.
  * **Desarrollo:** Javier Parra / Abigail Maripan.
  * **QA / Testing:** Equipo de validación.

-----
