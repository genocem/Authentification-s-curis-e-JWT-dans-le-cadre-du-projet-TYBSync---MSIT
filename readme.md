## Authentication Routes

### Authentication Endpoints
- **POST** `/auth/register` → Register with name,role,email and password, returns JWT token
  - example: {
    "name": "John Doe",
    "role": "USER",
    "email": "john.doe@example.com",
    "password": "yourPassword123"
    }

- **POST** `/auth/login` → Authenticate with email/password, returns JWT token
- **POST** `/auth/check` → Validate JWT token and return user details

---

## Default Configuration
- Default port: `8080`
- Test credentials:
  - **email:** `admin@admin.com`
  - **Password:** `123`

---
## Requirements to Run

   #### A MySQL database must be running with the following configuration:

##### name=Tp
##### username=root
##### password=1122

you may also change the file resourses/application.properties if you have an existing database that you would rather use