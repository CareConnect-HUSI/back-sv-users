# Back-SV-Users CareConnect

## Descripción
El servicio `back-sv-users` es un componente del sistema CareConnect, desarrollado para el Hospital Universitario San Ignacio. Gestiona operaciones relacionadas con enfermeras, incluyendo registro, autenticación, actualización de datos, y consulta de información geográfica (localidades y barrios). Se integra con otros módulos a través de un API Gateway, soportando el portal web y la app móvil para la gestión de visitas domiciliarias.

## Funcionalidades
- **Gestión de Enfermeras**: Registro (`POST /enfermeras/registrar-enfermera`), actualización (`PUT /enfermeras/{id}`), y consulta paginada (`GET /enfermeras`).
- **Autenticación**: Login con JWT (`POST /enfermeras/login`).
- **Datos Geográficos**: Consulta de localidades (`GET /enfermeras/localidades`), barrios por localidad (`GET /enfermeras/barrios/{codigoLocalidad}`), y barrios por nombre (`GET /enfermeras/nombre/{nombre}`).
- **Tipos de Identificación**: Consulta de tipos disponibles (`GET /enfermeras/tipos-identificacion`).

## Tecnologías
- **Framework**: Spring Boot
- **Lenguaje**: Java 17
- **Base de Datos**: PostgreSQL
- **Seguridad**: Spring Security con JWT
- **Dependencias Clave** (asumidas):
  - `spring-boot-starter-web`
  - `spring-boot-starter-data-jpa`
  - `spring-boot-starter-security`
  - `postgresql`
  - `jjwt`

## Requisitos
- Java 17
- Maven 3.8+
- PostgreSQL (configurado en `application.properties`)
- API Gateway activo
- Archivo `application.properties` con:
  ```
  spring.datasource.url=jdbc:postgresql://localhost:5432/careconnect
  spring.datasource.username=your_user
  spring.datasource.password=your_password
  server.port=8080
  jwt.secret=your_jwt_secret
  ```

## Instalación
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/careconnect/back-sv-users.git
   cd back-sv-users
   ```

2. Configurar `application.properties` con las credenciales de la base de datos y la clave JWT.

3. Compilar e instalar dependencias:
   ```bash
   mvn clean install
   ```

4. Iniciar el servicio:
   ```bash
   mvn spring-boot:run
   ```

   Disponible en `http://localhost:8080`, accesible vía API Gateway.

## Uso
- **Endpoints** (prefijo `/enfermeras`):
  - Registrar enfermera: `POST http://localhost:8080/api/enfermeras/registrar-enfermera`
  - Login: `POST http://localhost:8080/api/enfermeras/login`
  - Consultar enfermeras: `GET http://localhost:8080/api/enfermeras?page=0&limit=10`
  - Consultar barrios: `GET http://localhost:8080/api/enfermeras/barrios/{codigoLocalidad}`
- **Autenticación**: Token JWT en el header `Authorization: Bearer <token>` para endpoints protegidos.
- **Ejemplo**:
  ```bash
  curl -X POST "http://localhost:8080/api/enfermeras/login" -H "Content-Type: application/json" -d '{"email": "enfermera@example.com", "password": "password123"}'
  ```


## Contacto
- Juan David González
- Lina María Salamanca
- Laura Alexandra Rodríguez
- Axel Nicolás Caro

**Pontificia Universidad Javeriana**  
**Mayo 26, 2025**