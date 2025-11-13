# Proyecto: ZONALIBROS - Aplicación Android Nativa con Arquitectura MVVM

# Login
![Captura de pantalla de la aplicacion](https://i.ibb.co/nsw8J81c/Login.png)

# Registro de Usuario
![Captura de pantalla de la aplicacion](https://i.ibb.co/yBkX9NS7/image.png)

# Home Pantalla
![Captura de pantalla de la aplicacion](https://i.ibb.co/5X642k5G/image.png)

# Carrito Pantalla
![Captura de pantalla de la aplicacion](https://i.ibb.co/xtWCxQQ0/Carrito.png)

# Perfil Pantalla
![Captura de pantalla de la aplicacion](https://i.ibb.co/SDmzX78S/Perfil.png)

# ZONALIBROS (NIBA_Vision)

Aplicación móvil de comercio electrónico para la venta de libros, desarrollada con **Kotlin** y **Jetpack Compose**, siguiendo la arquitectura **MVVM**.

La aplicación se conecta a una **API RESTful** (Spring Boot) para gestionar usuarios y catálogo, y utiliza persistencia local para mantener la sesión y el carrito de compras.

## 🚀 Características Principales

* **Autenticación:** Registro y Login de usuarios conectados a API REST real.
* **Catálogo en la Nube:** Carga de libros e imágenes desde servidor remoto.
* **Diseño Adaptativo:** Interfaz optimizada para Teléfonos, Foldables y Tablets usando `WindowSizeClass`.
* **Cámara:** Funcionalidad nativa para capturar foto de perfil durante el registro.
* **Persistencia:** Sesión de usuario y Carrito de compras persistentes (no se borran al cerrar la app).
* **UI Moderna:** Animaciones, Material Design 3 y Componentes reutilizables.

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Kotlin
* **UI Toolkit:** Jetpack Compose (Material 3)
* **Arquitectura:** MVVM (Model-View-ViewModel) + Clean Architecture básica.
* **Red (Networking):** Retrofit + Gson + Scalar Converter.
* **Carga de Imágenes:** Coil.
* **Navegación:** Jetpack Navigation Compose.
* **Persistencia Local:** SharedPreferences (para Sesión y Carrito).

## ⚙️ Configuración del Entorno

1.  **API Backend:** Asegúrate de que la API Spring Boot esté corriendo en tu red local (Laragon/MySQL).
2.  **Conexión:** * Averigua tu IP local (`ipconfig`).
    * Actualiza `BASE_URL` en `data/ApiService.kt`.
    * Configura el Firewall de Windows para permitir puerto 8080 y 80.
3.  **Ejecución:** Conecta tu dispositivo Android a la misma red Wi-Fi y compila el proyecto.

## 📱 Estructura del Proyecto

* `ui/screens`: Pantallas de la aplicación (Login, Home, Cart, etc.).
* `viewmodel`: Lógica de negocio y estado de la UI.
* `data`: Repositorios, Modelos y configuración de API.
* `util`: Validadores y herramientas.

---
Desarrollado para la asignatura de Desarrollo de Aplicaciones Móviles.
## Desarrolladores

* **Nicolás Fonseca** - *Desarrollador Principal*
* **Bastián Rubio** - *Desarrollador Secundario*
---