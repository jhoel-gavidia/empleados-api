# Empleados API

API REST para la gestión de empleados y departamentos, construida con **Java 21** y **Spring Boot**. Incluye autenticación con JWT, control de acceso basado en roles, documentación OpenAPI, pruebas automatizadas con Testcontainers y un pipeline de CI/CD que publica la imagen Docker y despliega automáticamente a Render.

🔗 **API desplegada:** [empleados-api-main.onrender.com](https://empleados-api-main.onrender.com)
📄 **Documentación interactiva (Swagger):** [empleados-api-main.onrender.com/swagger-ui/index.html](https://empleados-api-main.onrender.com/swagger-ui/index.html)

> ⚠️ El servicio corre en el plan gratuito de Render, por lo que puede "dormir" tras un período de inactividad. La primera petición tras un tiempo sin uso puede tardar unos segundos en responder mientras la instancia arranca.

## Tabla de contenidos

- [Características](#características)
- [Stack tecnológico](#stack-tecnológico)
- [Arquitectura](#arquitectura)
- [Endpoints principales](#endpoints-principales)
- [Cómo ejecutar el proyecto](#cómo-ejecutar-el-proyecto)
- [Variables de entorno](#variables-de-entorno)
- [Pruebas](#pruebas)
- [CI/CD](#cicd)
- [Documentación de la API](#documentación-de-la-api)
- [Roadmap](#roadmap)

## Características

- **Gestión de empleados y departamentos**: operaciones CRUD completas, paginación y filtros por nombre, departamento y rango salarial.
- **Autenticación y autorización con JWT**: login con emisión de token y control de acceso basado en roles (`ADMIN`, `USER`, `HR`).
- **Arquitectura por capas**: controladores, servicios, repositorios, DTOs y mappers separados para mantener el código desacoplado y fácil de mantener.
- **Manejo global de excepciones**: respuestas de error consistentes ante recursos no encontrados, datos duplicados y errores de validación.
- **Documentación interactiva** con Swagger/OpenAPI, incluyendo el esquema de autenticación Bearer.
- **Pruebas automatizadas**: unitarias, de controlador y de integración con base de datos real en contenedor (Testcontainers), con cobertura medida vía JaCoCo.
- **Contenerización con Docker** (build multi-stage) y **orquestación local** con Docker Compose (API + PostgreSQL).
- **CI/CD con GitHub Actions**: ejecución de tests, build y publicación de la imagen en GitHub Container Registry, y despliegue automático a Render.
- **Monitoreo básico** vía Spring Boot Actuator (`/actuator/health`).

## Stack tecnológico

| Categoría | Tecnologías |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot, Spring MVC, Spring Data JPA, Spring Security |
| Seguridad | JWT (jjwt), BCrypt |
| Base de datos | PostgreSQL |
| Documentación | springdoc-openapi (Swagger UI) |
| Testing | JUnit 5, Mockito, Testcontainers, JaCoCo |
| DevOps | Docker, Docker Compose, GitHub Actions, GitHub Container Registry, Render |
| Build | Maven |

## Arquitectura

El proyecto sigue una arquitectura por capas típica de una API REST en Spring Boot:

```
src/main/java/com/jhoel/empleados_api
├── config/           # Configuración de seguridad, OpenAPI y datos iniciales
├── controller/        # Controladores REST (Auth, Employee, Department)
├── dto/                # Request y Response DTOs
├── entity/             # Entidades JPA (Employee, Department, User, Role)
├── exception/          # Manejo global de excepciones
├── mapper/             # Conversión entre entidades y DTOs
├── repository/         # Repositorios Spring Data JPA
├── security/           # Filtro JWT, servicio de JWT y UserDetailsService
├── Specification/      # Especificaciones JPA para filtros dinámicos
└── service/            # Interfaces e implementaciones de la lógica de negocio
```

## Endpoints principales

### Autenticación

| Método | Endpoint | Descripción | Acceso |
|---|---|---|---|
| POST | `/api/auth/login` | Autentica un usuario y devuelve un token JWT | Público |

### Empleados

| Método | Endpoint | Descripción | Acceso |
|---|---|---|---|
| POST | `/api/employees` | Crea un empleado | Autenticado |
| GET | `/api/employees` | Lista empleados (paginado, filtro por nombre) | Autenticado |
| GET | `/api/employees/{id}` | Obtiene un empleado por ID | Autenticado |
| PUT | `/api/employees/{id}` | Actualiza un empleado | Autenticado |
| DELETE | `/api/employees/{id}` | Elimina un empleado | Autenticado |
| GET | `/api/employees/filter` | Filtra empleados por nombre, departamento y rango salarial | Autenticado |

### Departamentos

| Método | Endpoint | Descripción | Acceso |
|---|---|---|---|
| POST | `/api/departments` | Crea un departamento | Autenticado |
| GET | `/api/departments` | Lista todos los departamentos | Autenticado |
| GET | `/api/departments/{id}` | Obtiene un departamento por ID | Autenticado |
| PUT | `/api/departments/{id}` | Actualiza un departamento | Autenticado |
| DELETE | `/api/departments/{id}` | Elimina un departamento | Autenticado |

> Los endpoints bajo `/api/users/**` están restringidos al rol `ADMIN`. Los endpoints públicos son `/api/auth/**`, `/swagger-ui/**`, `/v3/api-docs/**` y `/actuator/health`.

## Cómo ejecutar el proyecto

### Requisitos previos

- Java 21
- Maven (o usa el wrapper incluido `./mvnw`)
- Docker y Docker Compose (para levantar todo el stack rápidamente)

### Opción 1: Con Docker Compose (recomendado)

```bash
git clone https://github.com/jhoel-gavidia/empleados-api.git
cd empleados-api
cp .env.example .env   # completa las variables (ver sección siguiente)
docker compose up --build
```

La API quedará disponible en `http://localhost:8080`.

### Opción 2: Entorno local

```bash
git clone https://github.com/jhoel-gavidia/empleados-api.git
cd empleados-api
# Levanta una base de datos PostgreSQL local o usa una gestionada
# Configura las variables de entorno (ver sección siguiente)
./mvnw spring-boot:run
```

## Variables de entorno

| Variable | Descripción |
|---|---|
| `DB_URL` | URL de conexión JDBC a PostgreSQL |
| `DB_USERNAME` | Usuario de la base de datos |
| `DB_PASSWORD` | Contraseña de la base de datos |
| `JWT_SECRET` | Clave secreta para firmar los tokens JWT |
| `JWT_EXPIRATION` | Tiempo de expiración del token (ms) |
| `CORS_ALLOWED_ORIGINS` | Orígenes permitidos para CORS (por defecto `http://localhost:3000`) |
| `APP_ADMIN_INITIALIZE` | Si es `true`, crea un usuario administrador al iniciar la app |
| `APP_ADMIN_USERNAME` / `APP_ADMIN_EMAIL` / `APP_ADMIN_PASSWORD` | Credenciales del administrador inicial |
| `PORT` | Puerto en el que corre la aplicación (por defecto `8080`) |

Consulta `.env.example` para la plantilla usada por Docker Compose.

## Pruebas

El proyecto cuenta con **49 pruebas automatizadas** entre tests unitarios (servicios, seguridad JWT), tests de controlador y tests de integración con una base de datos PostgreSQL real levantada en contenedor mediante Testcontainers.

```bash
./mvnw test
```

El reporte de cobertura se genera automáticamente con JaCoCo en `target/site/jacoco/index.html` tras ejecutar los tests.

## CI/CD

El pipeline definido en `.github/workflows/ci.yml` se ejecuta en cada push y pull request a `main`, y:

1. Ejecuta la suite de pruebas con Maven.
2. Construye la imagen Docker de la aplicación.
3. Publica la imagen en GitHub Container Registry (GHCR).
4. Despliega automáticamente a Render cuando el push es a `main`.

## Documentación de la API

La documentación interactiva de Swagger está disponible tanto en local como en producción:

- **Local:** `http://localhost:8080/swagger-ui/index.html`
- **Producción:** [https://empleados-api-main.onrender.com/swagger-ui/index.html](https://empleados-api-main.onrender.com/swagger-ui/index.html)

Incluye el esquema de autenticación Bearer, por lo que puedes autenticarte con el token JWT obtenido en `/api/auth/login` y probar los endpoints protegidos directamente desde la interfaz.

## Estado del servicio

El endpoint de salud, expuesto vía Spring Boot Actuator, permite verificar que la API está activa:

```
GET https://empleados-api-main.onrender.com/actuator/health
```

## Roadmap

- [ ] Frontend en React/Next.js/TypeScript consumiendo esta API (login con cookies httpOnly, CRUD de empleados y departamentos).
- [ ] Endpoints de gestión de usuarios para administradores.
- [ ] Internacionalización de mensajes de error.

## Autor

**Jhoel Anderson Gavidia Calderón**
[GitHub](https://github.com/jhoel-gavidia) · [LinkedIn](https://linkedin.com/in/jhoel-anderson-gavidiacalderon-99a688401)
