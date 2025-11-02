# Proyecto: ZONALIBROS - Aplicación Android Nativa con Arquitectura MVVM

# Login
![Captura de pantalla de la aplicacion](https://i.ibb.co/xtWCxQQ0/Screenshot-2.png)

# Registro de Usuario
![Captura de pantalla de la aplicacion](https://i.ibb.co/yBkX9NS7/image.png)

# Home Pantalla
![Captura de pantalla de la aplicacion](https://i.ibb.co/5X642k5G/image.png)

# Carrito Pantalla
![Captura de pantalla de la aplicacion](https://i.ibb.co/xtWCxQQ0/Screenshot-2.png)

# Perfil Pantalla
![Captura de pantalla de la aplicacion](https://i.ibb.co/SDmzX78S/image.png)

## Descripción del Proyecto

**ZONALIBROS** es una aplicación de comercio electrónico nativa para Android, desarrollada como parte de mi formación en Desarrollo de Aplicaciones Móviles. El proyecto ha evolucionado para convertirse en una plataforma funcional que no solo gestiona la autenticación de usuarios, sino que también ofrece un catálogo de productos, un carrito de compras y persistencia de datos locales.

La arquitectura del proyecto sigue estrictamente el patrón **MVVM (Model-View-ViewModel)**, con un fuerte énfasis en la separación de responsabilidades, la gestión de estado reactiva con `StateFlow`, y una interfaz de usuario moderna y adaptable construida con **Jetpack Compose**. La última versión integra **Room** como base de datos local para la persistencia de usuarios.

## Funcionalidades Implementadas

La aplicación se estructura en torno a tres módulos funcionales principales:

### 1. Sistema de Autenticación y Persistencia

He desarrollado un flujo de autenticación completo con persistencia de datos a través de Room:
* **Base de Datos Local**: Integración de **Room** para almacenar los datos de los usuarios de forma persistente en el dispositivo.
* **Pantalla de Registro**:
    * Formulario con validación en tiempo real para todos los campos requeridos.
    * **Integración de Cámara**: Permite al usuario capturar y asignar una foto de perfil durante el registro.
    * Campo de dirección de despacho obligatorio.
* **Pantalla de Login**:
    * Autenticación de credenciales contra la base de datos Room.
    * Gestión de estado de carga con animaciones y retroalimentación visual clara.
* **Gestión de Sesión**: Un `SessionManager` centralizado mantiene el estado del usuario logueado a lo largo de la aplicación.

### 2. Módulo de E-Commerce

Funcionalidades clave para la experiencia de compra:
* **Catálogo de Libros**:
    * Pantalla principal que muestra una lista de libros obtenidos de un `BookRepository`.
    * Las imágenes de las portadas se cargan de forma asíncrona desde URLs utilizando la librería **Coil**.
* **Carrito de Compras**:
    * Los usuarios pueden añadir libros al carrito desde el catálogo.
    * La `CartScreen` muestra un resumen de los productos, calcula el total y permite simular el pago.
    * El estado del carrito es gestionado por un `CartRepository` y se actualiza en tiempo real.
* **Perfil de Usuario**:
    * Muestra la información del usuario logueado, incluyendo la foto de perfil, nombre, correo y dirección de despacho.
    * Incluye un botón para cerrar sesión, que limpia el `SessionManager` y redirige al login.

### 3. Interfaz de Usuario Adaptativa y Mejorada

La UI ha sido diseñada para ser moderna, intuitiva y adaptable:
* **Diseño Adaptativo**: La pantalla principal utiliza `WindowSizeClass` para ajustar su layout (barra de navegación inferior, riel lateral) en dispositivos de diferentes tamaños (móviles y tablets).
* **Animaciones**: Se han añadido animaciones sutiles en las transiciones de pantalla y en la interacción con los componentes para mejorar la experiencia de usuario.

## Arquitectura y Tecnologías

El proyecto está construido sobre una base sólida de arquitectura MVVM y tecnologías modernas de Android.

* **Lenguaje**: **Kotlin**
* **Toolkit de UI**: **Jetpack Compose**
* **Arquitectura**: **MVVM (Model-View-ViewModel)**
    * **View (`ui/screens`)**: Composables que observan el estado de los ViewModels y delegan eventos de usuario.
    * **ViewModel (`viewmodel`)**: Gestionan el estado de la UI (`UiState`) y la lógica de negocio, comunicándose con los repositorios.
    * **Model (`data`)**:
        * **Persistencia**: **Room** (`AppDatabase`, `UserDao`, `UserEntity`).
        * **Repositorios**: `UserRepository`, `BookRepository`, `CartRepository` que abstraen el origen de los datos.
        * **Gestión de Sesión**: `SessionManager`.
* **Inyección de Dependencias**: Se utiliza una `AppViewModelFactory` para proveer las instancias de los repositorios a los ViewModels, facilitando las pruebas y el desacoplamiento.
* **Gestión de Estado**: Flujo de Datos Unidireccional (UDF) con `StateFlow` para una gestión de estado predecible y reactiva.
* **Navegación**: **Navigation Compose** para una navegación segura y declarativa.
* **Carga de Imágenes**: **Coil** para cargar imágenes desde URLs de manera eficiente.
* **Asincronía**: **Coroutines** para realizar operaciones de red y base de datos en hilos de fondo sin bloquear la UI.

## Cómo Ejecutar el Proyecto

1.  Clona este repositorio en tu máquina local.
2.  Abre el proyecto con una versión reciente de Android Studio.
3.  Permite que Gradle sincronice las dependencias. El proyecto utiliza KSP para el procesador de anotaciones de Room.
4.  Ejecuta la aplicación en un emulador o dispositivo físico. Para probar los diseños adaptativos, se recomienda usar dispositivos con diferentes tamaños de pantalla.

## Desarrolladores

* **Nicolás Fonseca** - *Desarrollador Principal*
* **Bastián Rubio** - *Desarrollador Secundario*
---