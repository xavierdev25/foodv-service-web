# 🧪 Guía Completa de Pruebas de Integración para FoodV Backend

> **Guía de Testing para el Sistema de Delivery FoodV - Universidad César Vallejo**

Esta guía te permitirá verificar que todo el backend funciona correctamente mediante **pruebas de integración** y **pruebas de API** usando Postman.

## 📋 Índice

- [Prerrequisitos](#-prerrequisitos)
- [Configuración Inicial](#-configuración-inicial)
- [Pruebas de Autenticación](#-pruebas-de-autenticación)
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
- ✅ **Maven 3.6+**
- ✅ **PostgreSQL 12+** ejecutándose en puerto 5432
- ✅ **Postman** (para pruebas de API)
- ✅ **Navegador web** (para Swagger UI)

### Base de Datos
- ✅ Base de datos `foodv_db` creada en PostgreSQL
- ✅ Usuario con permisos de acceso a la base de datos

---

## 🚀 Configuración Inicial

### Paso 1: Verificar que la Aplicación se Ejecute

```bash
# Navegar al directorio del proyecto
cd C:\Users\David\Desktop\foodv

# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

**✅ Verificación exitosa si ves en la consola:**
```
Started FoodVApplication in X.XXX seconds (JVM running for X.XXX)
Usuarios creados exitosamente
Tiendas creadas exitosamente
Productos creados exitosamente
```

### Paso 2: Configurar Postman

#### 2.1 Crear Nueva Colección
1. Abre Postman
2. Crea una nueva colección llamada **"FoodV API"**
3. Crea un entorno llamado **"FoodV Local"**

#### 2.2 Configurar Variables de Entorno
En Postman, ve a **Environments** → **Create Environment**:

| Variable | Valor Inicial | Descripción |
|----------|---------------|-------------|
| `base_url` | `http://localhost:8080` | URL base de la API |
| `token` | _(vacío)_ | Token JWT (se llenará automáticamente) |

---

## 🔐 Pruebas de Autenticación

### 3.1 Registro de Usuario

**Endpoint:** `POST /api/auth/register`

**Request:**
```http
POST {{base_url}}/api/auth/register
Content-Type: application/json

{
    "name": "Usuario de Prueba",
    "email": "test@ucv.edu.pe",
    "password": "password123"
}
```

**✅ Respuesta esperada:**
```json
{
    "success": true,
    "message": "Registro exitoso",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9...",
        "type": "Bearer",
        "id": 4,
        "name": "Usuario de Prueba",
        "email": "test@ucv.edu.pe",
        "role": "CLIENTE"
    }
}
```

**⚠️ IMPORTANTE:** Copia el `token` y guárdalo en la variable `{{token}}` del entorno.

### 3.2 Login con Usuario Existente

**Endpoint:** `POST /api/auth/login`

**Request:**
```http
POST {{base_url}}/api/auth/login
Content-Type: application/json

{
    "email": "juan.perez@ucv.edu.pe",
    "password": "password123"
}
```

**✅ Respuesta esperada:**
```json
{
    "success": true,
    "message": "Login exitoso",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9...",
        "type": "Bearer",
        "id": 2,
        "name": "Juan Pérez",
        "email": "juan.perez@ucv.edu.pe",
        "role": "CLIENTE"
    }
}
```

---

## 🏪 Pruebas de Tiendas (Endpoints Públicos)

### 4.1 Listar Todas las Tiendas

**Endpoint:** `GET /api/stores`

**Request:**
```http
GET {{base_url}}/api/stores
```

**✅ Respuesta esperada:**
```json
{
    "success": true,
    "message": "Tiendas obtenidas exitosamente",
    "data": [
        {
            "id": 1,
            "name": "Listo",
            "description": "Minimarket con productos básicos",
            "type": "MINIMARKET",
            "active": true
        },
        {
            "id": 2,
            "name": "Fresco",
            "description": "Dulcería con snacks y golosinas",
            "type": "DULCERIA",
            "active": true
        }
    ]
}
```

### 4.2 Obtener Tienda por ID

**Endpoint:** `GET /api/stores/{id}`

**Request:**
```http
GET {{base_url}}/api/stores/1
```

### 4.3 Listar Tiendas por Tipo

**Endpoint:** `GET /api/stores/type/{type}`

**Request:**
```http
GET {{base_url}}/api/stores/type/MINIMARKET
```

**Tipos disponibles:** `MINIMARKET`, `DULCERIA`, `EMPRENDEDOR`

---

## 🛍️ Pruebas de Productos (Endpoints Públicos)

### 5.1 Listar Todos los Productos

**Endpoint:** `GET /api/products`

**Request:**
```http
GET {{base_url}}/api/products
```

### 5.2 Buscar Productos

**Endpoint:** `GET /api/products/search`

**Request:**
```http
GET {{base_url}}/api/products/search?q=agua
```

### 5.3 Productos de una Tienda Específica

**Endpoint:** `GET /api/products/store/{storeId}`

**Request:**
```http
GET {{base_url}}/api/products/store/1
```

### 5.4 Buscar en Tienda Específica

**Endpoint:** `GET /api/products/store/{storeId}/search`

**Request:**
```http
GET {{base_url}}/api/products/store/1/search?q=café
```

---

## 👤 Pruebas de Usuario (Endpoints Protegidos)

### 6.1 Obtener Perfil del Usuario

**Endpoint:** `GET /api/users/profile`

**Request:**
```http
GET {{base_url}}/api/users/profile
Authorization: Bearer {{token}}
```

**✅ Respuesta esperada:**
```json
{
    "success": true,
    "message": "Perfil obtenido exitosamente",
    "data": {
        "id": 2,
        "name": "Juan Pérez",
        "email": "juan.perez@ucv.edu.pe",
        "role": "CLIENTE"
    }
}
```

---

## 🛒 Pruebas del Carrito (Endpoints Protegidos)

### 7.1 Ver Carrito Vacío

**Endpoint:** `GET /api/cart`

**Request:**
```http
GET {{base_url}}/api/cart
Authorization: Bearer {{token}}
```

### 7.2 Agregar Producto al Carrito

**Endpoint:** `POST /api/cart/add`

**Request:**
```http
POST {{base_url}}/api/cart/add
Authorization: Bearer {{token}}
Content-Type: application/json

{
    "productId": 1,
    "quantity": 2
}
```

**✅ Respuesta esperada:**
```json
{
    "success": true,
    "message": "Producto agregado al carrito exitosamente",
    "data": {
        "id": 1,
        "userId": 2,
        "items": [
            {
                "id": 1,
                "product": {
                    "id": 1,
                    "name": "Agua",
                    "price": 1.50,
                    "imageUrl": "https://example.com/agua.jpg"
                },
                "quantity": 2,
                "subtotal": 3.00
            }
        ],
        "total": 3.00
    }
}
```

### 7.3 Ver Carrito con Productos

**Endpoint:** `GET /api/cart`

**Request:**
```http
GET {{base_url}}/api/cart
Authorization: Bearer {{token}}
```

### 7.4 Agregar Más Productos

**Request:**
```http
POST {{base_url}}/api/cart/add
Authorization: Bearer {{token}}
Content-Type: application/json

{
    "productId": 2,
    "quantity": 1
}
```

### 7.5 Actualizar Cantidad de Item

**Endpoint:** `PUT /api/cart/items/{itemId}`

**Request:**
```http
PUT {{base_url}}/api/cart/items/1
Authorization: Bearer {{token}}
Content-Type: application/json

{
    "quantity": 3
}
```

### 7.6 Eliminar Item del Carrito

**Endpoint:** `DELETE /api/cart/items/{itemId}`

**Request:**
```http
DELETE {{base_url}}/api/cart/items/2
Authorization: Bearer {{token}}
```

### 7.7 Vaciar Carrito

**Endpoint:** `DELETE /api/cart/clear`

**Request:**
```http
DELETE {{base_url}}/api/cart/clear
Authorization: Bearer {{token}}
```

---

## 📦 Pruebas de Pedidos (Endpoints Protegidos)

### 8.1 Agregar Productos al Carrito Primero

```http
POST {{base_url}}/api/cart/add
Authorization: Bearer {{token}}
Content-Type: application/json

{
    "productId": 1,
    "quantity": 2
}
```

### 8.2 Crear Pedido

**Endpoint:** `POST /api/orders`

**Request:**
```http
POST {{base_url}}/api/orders
Authorization: Bearer {{token}}
Content-Type: application/json

{
    "pabellon": "A",
    "piso": "2",
    "salon": "201",
    "notes": "Entregar en la puerta del salón"
}
```

**✅ Respuesta esperada:**
```json
{
    "success": true,
    "message": "Pedido creado exitosamente",
    "data": {
        "id": 1,
        "userId": 2,
        "status": "PENDIENTE",
        "pabellon": "A",
        "piso": "2",
        "salon": "201",
        "notes": "Entregar en la puerta del salón",
        "total": 3.00,
        "items": [
            {
                "id": 1,
                "product": {
                    "id": 1,
                    "name": "Agua",
                    "price": 1.50
                },
                "quantity": 2,
                "subtotal": 3.00
            }
        ],
        "createdAt": "2024-01-15T10:30:00"
    }
}
```

### 8.3 Ver Historial de Pedidos

**Endpoint:** `GET /api/orders`

**Request:**
```http
GET {{base_url}}/api/orders
Authorization: Bearer {{token}}
```

### 8.4 Obtener Pedido por ID

**Endpoint:** `GET /api/orders/{orderId}`

**Request:**
```http
GET {{base_url}}/api/orders/1
Authorization: Bearer {{token}}
```

### 8.5 Filtrar Pedidos por Estado

**Endpoint:** `GET /api/orders/status/{status}`

**Request:**
```http
GET {{base_url}}/api/orders/status/PENDIENTE
Authorization: Bearer {{token}}
```

**Estados disponibles:** `PENDIENTE`, `PREPARANDO`, `EN_CAMINO`, `ENTREGADO`, `CANCELADO`

---

## 🔍 Pruebas de Swagger UI

### 9.1 Acceder a Swagger UI

Abre en el navegador: **`http://localhost:8080/swagger-ui.html`**

**✅ Verificación:** Deberías ver la documentación interactiva de la API con todos los endpoints disponibles.

### 9.2 Probar Endpoints desde Swagger

1. **Autenticación:** Usa el botón "Try it out" en `/api/auth/login`
2. **Autorización:** Copia el token y úsalo en el botón "Authorize" (🔒)
3. **Prueba endpoints protegidos** como `/api/cart` o `/api/orders`

---

## 🧪 Pruebas de Casos de Error

### 10.1 Login con Credenciales Incorrectas

**Request:**
```http
POST {{base_url}}/api/auth/login
Content-Type: application/json

{
    "email": "test@ucv.edu.pe",
    "password": "wrongpassword"
}
```

**✅ Respuesta esperada:**
```json
{
    "success": false,
    "message": "Credenciales inválidas"
}
```

### 10.2 Acceso sin Token

**Request:**
```http
GET {{base_url}}/api/cart
```

**✅ Respuesta esperada:**
```json
{
    "success": false,
    "message": "Acceso denegado"
}
```

### 10.3 Token Inválido

**Request:**
```http
GET {{base_url}}/api/cart
Authorization: Bearer token_invalido
```

### 10.4 Producto No Encontrado

**Request:**
```http
GET {{base_url}}/api/products/999
```

**✅ Respuesta esperada:**
```json
{
    "success": false,
    "message": "Producto no encontrado"
}
```

### 10.5 Agregar Producto Inexistente al Carrito

**Request:**
```http
POST {{base_url}}/api/cart/add
Authorization: Bearer {{token}}
Content-Type: application/json

{
    "productId": 999,
    "quantity": 1
}
```

---

## 📊 Verificación de Datos de Prueba

### 11.1 Usuarios Creados Automáticamente

Los siguientes usuarios deberían existir automáticamente al iniciar la aplicación:

| Email | Contraseña | Rol | Descripción |
|-------|------------|-----|-------------|
| `admin@ucv.edu.pe` | `admin123` | ADMIN | Administrador del sistema |
| `juan.perez@ucv.edu.pe` | `password123` | CLIENTE | Usuario de prueba 1 |
| `maria.garcia@ucv.edu.pe` | `password123` | CLIENTE | Usuario de prueba 2 |

### 11.2 Tiendas Creadas Automáticamente

| Nombre | Tipo | Descripción |
|--------|------|-------------|
| **Listo** | MINIMARKET | Productos básicos para estudiantes |
| **Fresco** | DULCERIA | Snacks y golosinas |
| **Café del Estudiante** | EMPRENDEDOR | Café y snacks preparados |
| **Snacks Saludables** | EMPRENDEDOR | Productos orgánicos y saludables |

### 11.3 Productos Creados Automáticamente

Cada tienda debería tener varios productos con:
- ✅ Nombres descriptivos
- ✅ Precios realistas
- ✅ Stock disponible
- ✅ URLs de imágenes (ejemplo)

---

## 🎯 Secuencia de Pruebas Recomendada

### Fase 1: Verificación Básica
1. ✅ **Ejecutar la aplicación** y verificar que inicie correctamente
2. ✅ **Verificar logs** de creación de datos (usuarios, tiendas, productos)
3. ✅ **Acceder a Swagger UI** y verificar documentación

### Fase 2: Endpoints Públicos
4. ✅ **Probar listado de tiendas** (`GET /api/stores`)
5. ✅ **Probar listado de productos** (`GET /api/products`)
6. ✅ **Probar búsqueda de productos** (`GET /api/products/search?q=agua`)
7. ✅ **Probar filtros por tienda** (`GET /api/products/store/1`)

### Fase 3: Autenticación
8. ✅ **Registrar un nuevo usuario** (`POST /api/auth/register`)
9. ✅ **Hacer login** y obtener token (`POST /api/auth/login`)
10. ✅ **Verificar perfil de usuario** (`GET /api/users/profile`)

### Fase 4: Funcionalidades Protegidas
11. ✅ **Probar carrito vacío** (`GET /api/cart`)
12. ✅ **Agregar productos al carrito** (`POST /api/cart/add`)
13. ✅ **Ver carrito con productos** (`GET /api/cart`)
14. ✅ **Actualizar cantidades** (`PUT /api/cart/items/{id}`)

### Fase 5: Gestión de Pedidos
15. ✅ **Crear pedido completo** (carrito → pedido)
16. ✅ **Ver historial de pedidos** (`GET /api/orders`)
17. ✅ **Filtrar pedidos por estado** (`GET /api/orders/status/PENDIENTE`)

### Fase 6: Casos de Error
18. ✅ **Probar credenciales incorrectas**
19. ✅ **Probar acceso sin token**
20. ✅ **Probar endpoints inexistentes**

---

## 🚨 Solución de Problemas Comunes

### Error de Conexión a Base de Datos
```
org.postgresql.util.PSQLException: FATAL: password authentication failed
```

**🔧 Solución:**
1. Verificar que PostgreSQL esté ejecutándose
2. Verificar credenciales en `application.properties`
3. Verificar que la base de datos `foodv_db` exista
4. Verificar permisos del usuario de base de datos

### Error 401 Unauthorized
```json
{"success":false,"message":"Acceso denegado"}
```

**🔧 Solución:**
1. Verificar que el token JWT sea válido
2. Verificar que el header `Authorization: Bearer {token}` esté presente
3. Verificar que el token no haya expirado
4. Hacer login nuevamente para obtener un token fresco

### Error 500 Internal Server Error
```json
{"success":false,"message":"Error interno del servidor"}
```

**🔧 Solución:**
1. Revisar logs en la consola para más detalles
2. Verificar que la base de datos esté accesible
3. Verificar que todas las dependencias estén instaladas
4. Verificar configuración de JWT

### Error de Puerto en Uso
```
Port 8080 was already in use
```

**🔧 Solución:**
1. Cambiar el puerto en `application.properties`: `server.port=8081`
2. O matar el proceso que usa el puerto 8080:
   ```bash
   # Windows
   netstat -ano | findstr :8080
   taskkill /PID <PID> /F
   
   # Linux/Mac
   lsof -ti:8080 | xargs kill -9
   ```

### Error de Compilación
```
java: cannot find symbol
```

**🔧 Solución:**
1. Verificar que Java 21+ esté configurado
2. Ejecutar `mvn clean install`
3. Verificar dependencias en `pom.xml`
4. Limpiar cache de IDE: File → Invalidate Caches and Restart

---

## 📈 Métricas de Éxito

### ✅ Pruebas Exitosas si:
- ✅ La aplicación inicia sin errores
- ✅ Todos los endpoints públicos responden correctamente
- ✅ La autenticación funciona (registro y login)
- ✅ Se pueden crear y gestionar carritos
- ✅ Se pueden crear y consultar pedidos
- ✅ Swagger UI está accesible y funcional
- ✅ Los casos de error se manejan correctamente
- ✅ Los datos de prueba se crean automáticamente

### 📊 Cobertura de Pruebas
- **Endpoints Públicos:** 100% (tiendas, productos)
- **Autenticación:** 100% (registro, login)
- **Funcionalidades Protegidas:** 100% (carrito, pedidos)
- **Manejo de Errores:** 100% (casos de error comunes)
- **Documentación:** 100% (Swagger UI)

---

## 🎉 ¡Felicitaciones!

Si has completado todas las pruebas exitosamente, tu backend FoodV está funcionando perfectamente y listo para ser usado por la aplicación móvil.

### Próximos Pasos
1. **Integración con Frontend:** Conectar con la aplicación móvil
2. **Despliegue:** Configurar para producción
3. **Monitoreo:** Implementar logs y métricas
4. **Optimización:** Mejorar rendimiento según uso real

---

**📞 Soporte:** Si encuentras algún problema durante las pruebas, revisa la sección de "Solución de Problemas" o consulta los logs de la aplicación para más detalles.

**🔗 Recursos Adicionales:**
- [Documentación Spring Boot](https://spring.io/projects/spring-boot)
- [Postman Learning Center](https://learning.postman.com/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)

---

*Guía creada para el proyecto FoodV - Sistema de Delivery para la Universidad César Vallejo* 🍔✨

