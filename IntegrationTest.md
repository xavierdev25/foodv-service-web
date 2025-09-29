# 🧪 Guía Completa de Pruebas de Integración para FoodV Backend

> **Guía de Testing para el Sistema de Delivery FoodV - Universidad César Vallejo**

Esta guía te permitirá verificar que todo el backend funciona correctamente mediante **pruebas de integración** y **pruebas de API** usando Postman, incluyendo el nuevo sistema de autenticación UCV.

## 📋 Índice

- [Prerrequisitos](#-prerrequisitos)
- [Configuración Inicial](#-configuración-inicial)
- [Configuración de Email](#-configuración-de-email)
- [Pruebas de Autenticación Tradicional](#-pruebas-de-autenticación-tradicional)
- [Pruebas de Autenticación UCV](#-pruebas-de-autenticación-ucv)
- [Pruebas de Tiendas](#-pruebas-de-tiendas)
- [Pruebas de Productos](#-pruebas-de-productos)
- [Pruebas de Usuario](#-pruebas-de-usuario)
- [Pruebas del Carrito](#-pruebas-del-carrito)
- [Pruebas de Pedidos](#-pruebas-de-pedidos)
- [Pruebas de Swagger UI](#-pruebas-de-swagger-ui)
- [Pruebas de Casos de Error](#-pruebas-de-casos-de-error)
- [Verificación de Datos](#-verificación-de-datos)
- [Secuencia de Pruebas](#-secuencia-de-pruebas)
- [Solución de Problemas](#-solución-de-problemas)

---

## 🔧 Prerrequisitos

Antes de empezar, asegúrate de tener instalado y configurado:

### Software Requerido
- ✅ **Java 21** (OpenJDK o Oracle JDK)
- ✅ **Maven 3.9.11+**
- ✅ **PostgreSQL 18+** ejecutándose en puerto 5432
- ✅ **Postman** (para pruebas de API)
- ✅ **Navegador web** (para Swagger UI)
- ✅ **Cuenta de Gmail** (para envío de emails)

### Base de Datos
- ✅ Base de datos `foodv_db` creada en PostgreSQL
- ✅ Usuario con permisos de acceso a la base de datos

### Configuración de Email
- ✅ **Gmail con autenticación de 2 factores** habilitada
- ✅ **Contraseña de aplicación** generada (16 caracteres)
- ✅ **Variables de entorno** configuradas

---

## 🚀 Configuración Inicial

### Paso 1: Verificar que la Aplicación se Ejecute

```bash
# Navegar al directorio del proyecto
cd C:\Users\David\Desktop\foodv

# Compilar el proyecto
mvn clean compile

# Ejecutar la aplicación
mvn spring-boot:run
```

### Paso 2: Verificar que la Aplicación Esté Funcionando

Deberías ver en la consola:
```
Started FoodVApplication in X.XXX seconds (JVM running for X.XXX)
Usuarios creados exitosamente
Tiendas creadas exitosamente
Productos creados exitosamente
```

### Paso 3: Verificar Endpoints Básicos

```bash
# Verificar que la aplicación responda
curl http://localhost:8080/api/stores

# O usar Postman para hacer GET a:
# http://localhost:8080/api/stores
```

---

## 📧 Configuración de Email

### Paso 1: Configurar Variables de Entorno

#### Windows PowerShell:
```powershell
$env:MAIL_USERNAME="tu-email@gmail.com"
$env:MAIL_PASSWORD="tu-contraseña-de-aplicación"
```

#### Windows CMD:
```cmd
set MAIL_USERNAME=tu-email@gmail.com
set MAIL_PASSWORD=tu-contraseña-de-aplicación
```

#### Linux/Mac:
```bash
export MAIL_USERNAME="tu-email@gmail.com"
export MAIL_PASSWORD="tu-contraseña-de-aplicación"
```

### Paso 2: Verificar Configuración

```bash
# Verificar variables (Windows PowerShell)
echo $env:MAIL_USERNAME
echo $env:MAIL_PASSWORD

# Verificar variables (Linux/Mac)
echo $MAIL_USERNAME
echo $MAIL_PASSWORD
```

### Paso 3: Configurar Gmail

1. **Habilitar autenticación de 2 factores** en tu cuenta de Gmail
2. **Generar contraseña de aplicación**:
   - Ve a: https://myaccount.google.com/
   - Seguridad → Verificación en 2 pasos
   - Contraseñas de aplicaciones
   - Genera una nueva contraseña para "Mail"
   - **Usa esta contraseña de 16 caracteres como `MAIL_PASSWORD`**

---

## 🔐 Pruebas de Autenticación Tradicional

### 1. Registro de Usuario Tradicional

```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
    "name": "Juan Pérez",
    "email": "juan.perez@ucv.edu.pe",
    "password": "password123"
}
```

**Respuesta Esperada:**
```json
{
    "success": true,
    "message": "Registro exitoso",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9...",
        "type": "Bearer",
        "id": 4,
        "name": "Juan Pérez",
        "email": "juan.perez@ucv.edu.pe",
        "role": "CLIENTE"
    }
}
```

### 2. Login Tradicional

```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
    "email": "juan.perez@ucv.edu.pe",
    "password": "password123"
}
```

**Respuesta Esperada:**
```json
{
    "success": true,
    "message": "Login exitoso",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9...",
        "type": "Bearer",
        "id": 4,
        "name": "Juan Pérez",
        "email": "juan.perez@ucv.edu.pe",
        "role": "CLIENTE"
    }
}
```

---

## 🎓 Pruebas de Autenticación UCV

### 1. Iniciar Registro UCV

```http
POST http://localhost:8080/api/auth/register-ucv
Content-Type: application/json

{
    "username": "XMONTANOGA",
    "password": "MiPassword123!"
}
```

**Validaciones:**
- Username: Solo letras mayúsculas y números (ej: `XMONTANOGA`)
- Password: Mínimo 8 caracteres, debe incluir mayúscula, minúscula, número y símbolo

**Respuesta Esperada:**
```json
{
    "success": true,
    "message": "Proceso iniciado",
    "data": "Código de verificación enviado a XMONTANOGA@ucvvirtual.edu.pe"
}
```

### 2. Verificar Email y OTP

**Revisar tu email** para el código OTP de 6 dígitos.

### 3. Verificar OTP

```http
POST http://localhost:8080/api/auth/verify-otp
Content-Type: application/json

{
    "username": "XMONTANOGA",
    "otpCode": "123456"
}
```

**Respuesta Esperada:**
```json
{
    "success": true,
    "message": "Registro completado exitosamente",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9...",
        "type": "Bearer",
        "id": 5,
        "name": "XMONTANOGA",
        "email": "XMONTANOGA@ucvvirtual.edu.pe",
        "role": "CLIENTE"
    }
}
```

### 4. Login con Username UCV

```http
POST http://localhost:8080/api/auth/login-username
Content-Type: application/json

{
    "usernameOrEmail": "XMONTANOGA",
    "password": "MiPassword123!"
}
```

**También funciona con email completo:**
```http
POST http://localhost:8080/api/auth/login-username
Content-Type: application/json

{
    "usernameOrEmail": "XMONTANOGA@ucvvirtual.edu.pe",
    "password": "MiPassword123!"
}
```

### 5. Probar Validaciones de Username UCV

#### Username Inválido (debe fallar):
```http
POST http://localhost:8080/api/auth/register-ucv
Content-Type: application/json

{
    "username": "xmonto",  // Debe ser mayúsculas
    "password": "MiPassword123!"
}
```

#### Contraseña Débil (debe fallar):
```http
POST http://localhost:8080/api/auth/register-ucv
Content-Type: application/json

{
    "username": "XMONTOGA",
    "password": "123456"  // Muy débil
}
```

#### Contraseña sin Símbolos (debe fallar):
```http
POST http://localhost:8080/api/auth/register-ucv
Content-Type: application/json

{
    "username": "XMONTOGA",
    "password": "MiPassword123"  // Falta símbolo
}
```

---

## 🏪 Pruebas de Tiendas

### 1. Listar Todas las Tiendas

```http
GET http://localhost:8080/api/stores
```

**Respuesta Esperada:**
```json
{
    "success": true,
    "message": "Tiendas obtenidas exitosamente",
    "data": [
        {
            "id": 1,
            "name": "Listo",
            "type": "MINIMARKET",
            "description": "Productos básicos para estudiantes",
            "isActive": true
        },
        {
            "id": 2,
            "name": "Fresco",
            "type": "DULCERIA",
            "description": "Snacks y golosinas",
            "isActive": true
        },
        {
            "id": 3,
            "name": "Café del Estudiante",
            "type": "EMPRENDEDOR",
            "description": "Café y snacks preparados",
            "isActive": true
        },
        {
            "id": 4,
            "name": "Snacks Saludables",
            "type": "EMPRENDEDOR",
            "description": "Productos orgánicos y saludables",
            "isActive": true
        }
    ]
}
```

### 2. Obtener Tienda por ID

```http
GET http://localhost:8080/api/stores/1
```

### 3. Listar Tiendas por Tipo

```http
GET http://localhost:8080/api/stores/type/MINIMARKET
```

```http
GET http://localhost:8080/api/stores/type/DULCERIA
```

```http
GET http://localhost:8080/api/stores/type/EMPRENDEDOR
```

---

## 🛍️ Pruebas de Productos

### 1. Listar Todos los Productos

```http
GET http://localhost:8080/api/products
```

### 2. Obtener Producto por ID

```http
GET http://localhost:8080/api/products/1
```

### 3. Listar Productos de una Tienda

```http
GET http://localhost:8080/api/products/store/1
```

### 4. Buscar Productos

```http
GET http://localhost:8080/api/products/search?q=agua
```

```http
GET http://localhost:8080/api/products/search?q=chocolate
```

### 5. Buscar Productos en una Tienda

```http
GET http://localhost:8080/api/products/store/1/search?q=agua
```

---

## 👤 Pruebas de Usuario

### 1. Obtener Perfil del Usuario

```http
GET http://localhost:8080/api/users/profile
Authorization: Bearer {token}
```

**Respuesta Esperada:**
```json
{
    "success": true,
    "message": "Perfil obtenido exitosamente",
    "data": {
        "id": 5,
        "name": "XMONTANOGA",
        "email": "XMONTANOGA@ucvvirtual.edu.pe",
        "role": "CLIENTE",
        "createdAt": "2025-09-28T20:17:15"
    }
}
```

---

## 🛒 Pruebas del Carrito

### 1. Obtener Carrito (debe estar vacío inicialmente)

```http
GET http://localhost:8080/api/cart
Authorization: Bearer {token}
```

**Respuesta Esperada:**
```json
{
    "success": true,
    "message": "Carrito obtenido exitosamente",
    "data": {
        "id": 1,
        "total": 0.0,
        "items": []
    }
}
```

### 2. Agregar Producto al Carrito

```http
POST http://localhost:8080/api/cart/add
Authorization: Bearer {token}
Content-Type: application/json

{
    "productId": 1,
    "quantity": 2
}
```

**Respuesta Esperada:**
```json
{
    "success": true,
    "message": "Producto agregado al carrito exitosamente",
    "data": {
        "id": 1,
        "productId": 1,
        "productName": "Agua 500ml",
        "quantity": 2,
        "unitPrice": 1.50,
        "subtotal": 3.00
    }
}
```

### 3. Ver Carrito Actualizado

```http
GET http://localhost:8080/api/cart
Authorization: Bearer {token}
```

### 4. Agregar Más Productos

```http
POST http://localhost:8080/api/cart/add
Authorization: Bearer {token}
Content-Type: application/json

{
    "productId": 2,
    "quantity": 1
}
```

### 5. Actualizar Cantidad de un Item

```http
PUT http://localhost:8080/api/cart/items/1
Authorization: Bearer {token}
Content-Type: application/json

{
    "quantity": 3
}
```

### 6. Eliminar Item del Carrito

```http
DELETE http://localhost:8080/api/cart/items/1
Authorization: Bearer {token}
```

### 7. Vaciar Carrito

```http
DELETE http://localhost:8080/api/cart/clear
Authorization: Bearer {token}
```

---

## 📦 Pruebas de Pedidos

### 1. Crear Pedido

```http
POST http://localhost:8080/api/orders
Authorization: Bearer {token}
Content-Type: application/json

{
    "pabellon": "A",
    "piso": "2",
    "salon": "201",
    "notes": "Entregar en la puerta del salón"
}
```

**Respuesta Esperada:**
```json
{
    "success": true,
    "message": "Pedido creado exitosamente",
    "data": {
        "id": 1,
        "status": "PENDIENTE",
        "total": 4.50,
        "pabellon": "A",
        "piso": "2",
        "salon": "201",
        "notes": "Entregar en la puerta del salón",
        "createdAt": "2025-09-28T20:30:00",
        "items": [
            {
                "id": 1,
                "productName": "Agua 500ml",
                "quantity": 2,
                "unitPrice": 1.50,
                "subtotal": 3.00
            },
            {
                "id": 2,
                "productName": "Gaseosa 500ml",
                "quantity": 1,
                "unitPrice": 2.50,
                "subtotal": 2.50
            }
        ]
    }
}
```

### 2. Listar Pedidos del Usuario

```http
GET http://localhost:8080/api/orders
Authorization: Bearer {token}
```

### 3. Obtener Pedido por ID

```http
GET http://localhost:8080/api/orders/1
Authorization: Bearer {token}
```

### 4. Listar Pedidos por Estado

```http
GET http://localhost:8080/api/orders/status/PENDIENTE
Authorization: Bearer {token}
```

```http
GET http://localhost:8080/api/orders/status/PREPARANDO
Authorization: Bearer {token}
```

---

## 📚 Pruebas de Swagger UI

### 1. Acceder a Swagger UI

Abre tu navegador y ve a:
```
http://localhost:8080/swagger-ui.html
```

### 2. Verificar Documentación

- ✅ **Título**: "🍔 FoodV API"
- ✅ **Descripción**: Sistema de Delivery para la Universidad César Vallejo
- ✅ **Endpoints de Autenticación**: Con ejemplos de UCV
- ✅ **Validaciones**: Explicadas en la documentación
- ✅ **Ejemplos**: Request/Response para cada endpoint

### 3. Probar Endpoints desde Swagger

1. **Expandir sección "🔐 Autenticación"**
2. **Probar registro UCV**:
   - Click en `POST /api/auth/register-ucv`
   - Click en "Try it out"
   - Ingresar datos de prueba
   - Click en "Execute"
3. **Verificar respuesta** en la sección "Response body"

### 4. Autenticación en Swagger

1. **Hacer login** usando cualquier endpoint de autenticación
2. **Copiar el token** de la respuesta
3. **Click en "Authorize"** (botón verde en la parte superior)
4. **Pegar el token** en el campo "Value"
5. **Click en "Authorize"**
6. **Probar endpoints protegidos** (carrito, pedidos, perfil)

---

## ❌ Pruebas de Casos de Error

### 1. Autenticación UCV - Username Inválido

```http
POST http://localhost:8080/api/auth/register-ucv
Content-Type: application/json

{
    "username": "xmonto",  // Debe ser mayúsculas
    "password": "MiPassword123!"
}
```

**Respuesta Esperada:**
```json
{
    "success": false,
    "message": "Errores de validación: {username=El nombre de usuario debe contener solo letras mayúsculas y números}",
    "data": null
}
```

### 2. Autenticación UCV - Contraseña Débil

```http
POST http://localhost:8080/api/auth/register-ucv
Content-Type: application/json

{
    "username": "XMONTOGA",
    "password": "123456"  // Muy débil
}
```

**Respuesta Esperada:**
```json
{
    "success": false,
    "message": "Errores de validación: {password=La contraseña debe contener al menos: 8 caracteres, una letra minúscula, una mayúscula, un número y un símbolo}",
    "data": null
}
```

### 3. OTP Expirado

```http
POST http://localhost:8080/api/auth/verify-otp
Content-Type: application/json

{
    "username": "XMONTANOGA",
    "otpCode": "000000"  // Código inválido
}
```

**Respuesta Esperada:**
```json
{
    "success": false,
    "message": "Código OTP inválido o expirado",
    "data": null
}
```

### 4. Acceso sin Token

```http
GET http://localhost:8080/api/users/profile
```

**Respuesta Esperada:**
```json
{
    "success": false,
    "message": "Acceso denegado",
    "data": null
}
```

### 5. Token Inválido

```http
GET http://localhost:8080/api/users/profile
Authorization: Bearer token-invalido
```

**Respuesta Esperada:**
```json
{
    "success": false,
    "message": "Token JWT inválido",
    "data": null
}
```

### 6. Producto No Encontrado

```http
GET http://localhost:8080/api/products/999
```

**Respuesta Esperada:**
```json
{
    "success": false,
    "message": "Producto no encontrado",
    "data": null
}
```

### 7. Carrito Vacío al Crear Pedido

```http
POST http://localhost:8080/api/orders
Authorization: Bearer {token}
Content-Type: application/json

{
    "pabellon": "A",
    "piso": "2",
    "salon": "201"
}
```

**Respuesta Esperada:**
```json
{
    "success": false,
    "message": "El carrito está vacío",
    "data": null
}
```

---

## 🔍 Verificación de Datos

### 1. Verificar Usuarios en Base de Datos

```sql
-- Conectar a PostgreSQL
psql -U postgres -d foodv_db

-- Ver usuarios creados
SELECT id, name, username, email, role, created_at FROM users;
```

### 2. Verificar Tiendas

```sql
SELECT id, name, type, description, is_active FROM stores;
```

### 3. Verificar Productos

```sql
SELECT id, name, description, price, stock, store_id FROM products;
```

### 4. Verificar Carritos

```sql
SELECT c.id, c.total, u.name as user_name, c.created_at 
FROM carts c 
JOIN users u ON c.user_id = u.id;
```

### 5. Verificar Pedidos

```sql
SELECT o.id, o.status, o.total, o.pabellon, o.piso, o.salon, u.name as user_name, o.created_at 
FROM orders o 
JOIN users u ON o.user_id = u.id;
```

---

## 📋 Secuencia de Pruebas

### Secuencia Completa de Pruebas UCV

1. **Configurar email** (variables de entorno)
2. **Iniciar aplicación** (`mvn spring-boot:run`)
3. **Verificar endpoints básicos** (tiendas, productos)
4. **Probar registro UCV**:
   - Username válido: `XMONTANOGA`
   - Contraseña válida: `MiPassword123!`
   - Verificar email con OTP
5. **Verificar OTP** con código del email
6. **Probar login con username**:
   - Username: `XMONTANOGA`
   - Email completo: `XMONTANOGA@ucvvirtual.edu.pe`
7. **Probar funcionalidades completas**:
   - Agregar productos al carrito
   - Crear pedido
   - Ver historial

### Secuencia de Pruebas Tradicionales

1. **Registro tradicional** con email
2. **Login tradicional** con email
3. **Probar funcionalidades** (carrito, pedidos)

### Secuencia de Pruebas de Error

1. **Validaciones UCV** (username/contraseña inválidos)
2. **OTP expirado/inválido**
3. **Acceso sin token**
4. **Recursos no encontrados**

---

## 🐛 Solución de Problemas

### Error de Conexión a Base de Datos

```
org.postgresql.util.PSQLException: FATAL: password authentication failed
```

**Solución:**
1. Verificar que PostgreSQL esté ejecutándose
2. Verificar credenciales en `application.properties`
3. Verificar que la base de datos `foodv_db` exista

### Error de Autenticación de Email

```
Authentication failed
```

**Solución:**
1. Verificar variables de entorno `MAIL_USERNAME` y `MAIL_PASSWORD`
2. Verificar que la contraseña de aplicación de Gmail sea correcta (16 caracteres)
3. Verificar que la autenticación de 2 factores esté habilitada en Gmail

### Error de Validación de Username UCV

```
El nombre de usuario debe contener solo letras mayúsculas y números
```

**Solución:**
- Usar solo letras mayúsculas y números (ej: `XMONTANOGA`)
- No usar caracteres especiales o espacios
- Longitud entre 3 y 20 caracteres

### Error de Validación de Contraseña

```
La contraseña debe contener al menos: 8 caracteres, una letra minúscula, una mayúscula, un número y un símbolo
```

**Solución:**
- Mínimo 8 caracteres
- Incluir: minúscula, mayúscula, número y símbolo (@$!%*?&)
- Ejemplo válido: `MiPassword123!`

### Error de OTP Expirado

```
Código OTP inválido o expirado
```

**Solución:**
- El código OTP expira en 10 minutos
- Solicitar nuevo código con `/api/auth/register-ucv`
- Verificar que el código sea exactamente de 6 dígitos

### Error 401 Unauthorized

```
{"success":false,"message":"Acceso denegado"}
```

**Solución:**
1. Verificar que el token JWT sea válido
2. Verificar que el header `Authorization: Bearer {token}` esté presente
3. Verificar que el token no haya expirado
4. Hacer login nuevamente para obtener un token fresco

### Error 500 Internal Server Error

```
{"success":false,"message":"Error interno del servidor"}
```

**Solución:**
1. Verificar logs en la consola para más detalles
2. Verificar que la base de datos esté accesible
3. Verificar que todas las dependencias estén instaladas
4. Verificar configuración de email

### Error de Puerto en Uso

```
Port 8080 was already in use
```

**Solución:**
1. Cambiar el puerto en `application.properties`: `server.port=8081`
2. O matar el proceso que usa el puerto 8080

### Error de Compilación

```
java: cannot find symbol
```

**Solución:**
1. Verificar que Java 21+ esté configurado
2. Ejecutar `mvn clean install`
3. Verificar dependencias en `pom.xml`
4. Limpiar cache de IntelliJ: File → Invalidate Caches and Restart

---

## ✅ Checklist de Verificación

### Configuración Inicial
- [ ] Java 21+ instalado y configurado
- [ ] Maven 3.9.11+ instalado
- [ ] PostgreSQL ejecutándose en puerto 5432
- [ ] Base de datos `foodv_db` creada
- [ ] Variables de entorno de email configuradas
- [ ] Contraseña de aplicación de Gmail generada

### Aplicación
- [ ] Aplicación se ejecuta sin errores
- [ ] Logs muestran "Started FoodVApplication"
- [ ] Datos de prueba se cargan automáticamente
- [ ] Swagger UI accesible en http://localhost:8080/swagger-ui.html

### Autenticación Tradicional
- [ ] Registro de usuario funciona
- [ ] Login con email funciona
- [ ] Token JWT se genera correctamente
- [ ] Endpoints protegidos requieren token

### Autenticación UCV
- [ ] Registro UCV con username válido funciona
- [ ] Validaciones de username y contraseña funcionan
- [ ] Email se envía correctamente
- [ ] OTP se verifica correctamente
- [ ] Login con username funciona
- [ ] Login con email UCV funciona

### Endpoints Públicos
- [ ] Listar tiendas funciona
- [ ] Obtener tienda por ID funciona
- [ ] Listar tiendas por tipo funciona
- [ ] Listar productos funciona
- [ ] Buscar productos funciona
- [ ] Obtener producto por ID funciona

### Endpoints Protegidos
- [ ] Obtener perfil de usuario funciona
- [ ] Agregar productos al carrito funciona
- [ ] Actualizar cantidades funciona
- [ ] Eliminar items del carrito funciona
- [ ] Vaciar carrito funciona
- [ ] Crear pedido funciona
- [ ] Listar pedidos funciona
- [ ] Obtener pedido por ID funciona

### Casos de Error
- [ ] Validaciones de username UCV funcionan
- [ ] Validaciones de contraseña funcionan
- [ ] OTP expirado se maneja correctamente
- [ ] Acceso sin token se rechaza
- [ ] Token inválido se rechaza
- [ ] Recursos no encontrados se manejan

### Base de Datos
- [ ] Usuarios se crean correctamente
- [ ] Tiendas se cargan correctamente
- [ ] Productos se cargan correctamente
- [ ] Carritos se crean correctamente
- [ ] Pedidos se crean correctamente

---

## 🎯 Resultados Esperados

Al completar todas las pruebas, deberías tener:

1. **Sistema de autenticación UCV** funcionando completamente
2. **Envío de emails** con códigos OTP funcionando
3. **Validaciones** de username y contraseña funcionando
4. **Todos los endpoints** respondiendo correctamente
5. **Base de datos** con datos de prueba cargados
6. **Swagger UI** con documentación completa
7. **Manejo de errores** funcionando correctamente

---

## 📞 Soporte

Si encuentras problemas durante las pruebas:

1. **Revisar logs** de la aplicación en la consola
2. **Verificar configuración** de base de datos y email
3. **Comprobar variables** de entorno
4. **Consultar documentación** en Swagger UI
5. **Revisar este archivo** para soluciones comunes

---

**¡Felicitaciones! Has completado todas las pruebas de integración del sistema FoodV! 🍔✨**