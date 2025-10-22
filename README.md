# Proyecto: ZONALIBROS - Aplicación Android Nativa con Arquitectura MVVM

![Captura de pantalla de la aplicacion](https://i.ibb.co/Ld0RZ2rn/Portada-Aplicacion.png)

## Descripción del Proyecto

Como desarrollador, he construido esta aplicación nativa para Android como parte de mi formación en Desarrollo de Aplicaciones Móviles. El proyecto, denominado **ZONALIBROS**, implementa un sistema completo de autenticación de usuarios y una interfaz de usuario adaptativa, utilizando las tecnologías más modernas del ecosistema Android.

El objetivo principal fue aplicar mis conocimientos en **Kotlin** y **Jetpack Compose** para resolver un caso práctico. El proyecto ha sido refactorizado para seguir estrictamente la arquitectura **MVVM (Model-View-ViewModel)**, enfocándose en la separación de responsabilidades, la gestión de estado reactiva y la creación de una experiencia de usuario fluida que se adapta a distintos factores de forma, desde teléfonos móviles hasta tablets.

## Funcionalidades Implementadas

He implementado dos módulos funcionales clave en esta aplicación:

### 1. Sistema de Autenticación de Usuarios

Desarrollé un flujo de autenticación robusto, desacoplando la lógica de la interfaz de usuario:

* **Pantalla de Registro**:
  * Formulario con validación en tiempo real para nombre, correo (restringido a `@duoc.cl`), y política de contraseñas seguras.
  * Validación de coincidencia de contraseñas.
  * Selección de géneros favoritos mediante `Checkbox`, requiriendo al menos una opción.
* **Pantalla de Login**:
  * Formulario de inicio de sesión que autentica credenciales contra la capa de datos.
  * Gestión de errores para credenciales incorrectas, con retroalimentación clara.
* **Pantalla de Recuperación de Contraseña**:
  * Flujo simulado donde el usuario ingresa su correo para recibir instrucciones, validando el formato del mismo.

### 2. Interfaz de Usuario Adaptativa

Siguiendo las directrices de diseño para múltiples dispositivos, he implementado una pantalla de inicio que adapta su diseño (`layout`) dinámicamente según el ancho del dispositivo:

* **Detección de Tamaño**: Utilicé la librería `material3-window-size-class` para obtener la clase de tamaño de la ventana (`WindowWidthSizeClass`) en tiempo de ejecución.
* **Diseño Compacto (Móviles)**: Interfaz de una sola columna, optimizada para visualización vertical.
* **Diseño Mediano (Tablets pequeñas)**: El `layout` cambia para mostrar contenido lado a lado, aprovechando el espacio horizontal.
* **Diseño Expandido (Tablets grandes)**: `Layout` de tipo "Master-Detail", con un panel de navegación lateral persistente y un área de contenido principal.

## Arquitectura y Tecnologías

El proyecto está construido sobre una base sólida de arquitectura MVVM, garantizando un código limpio, escalable y fácil de mantener.

* **Lenguaje**: **Kotlin**
* **Toolkit de UI**: **Jetpack Compose** para una UI declarativa y moderna.
* **Arquitectura**: **MVVM (Model-View-ViewModel)**
  * **View (`ui/screens`)**: Composables "tontos" que solo se encargan de mostrar el estado y delegar los eventos del usuario. Se dividen en subpaquetes por funcionalidad (`auth`, `home`).
  * **ViewModel (`viewmodel`)**: Contienen toda la lógica de presentación. Gestionan el estado de la UI a través de `StateFlow` y clases `UiState`, y reaccionan a los eventos del usuario.
  * **Model (`data`)**: Incluye los modelos de datos (`User`) y el repositorio (`UserRepository`) que simula la capa de acceso a datos.
* **Gestión de Estado**: Se utiliza un **Flujo de Datos Unidireccional (UDF)**, donde el estado fluye del `ViewModel` a la `View` y los eventos fluyen de la `View` al `ViewModel`.
* **Navegación**: **Navigation Compose** (`androidx.navigation:navigation-compose`) para gestionar el grafo de navegación y las transiciones entre pantallas de forma segura mediante una `sealed class` de rutas.
* **Diseño Adaptativo**: `androidx.compose.material3:material3-window-size-class` es el núcleo de la adaptabilidad de la interfaz.
* **Lógica de Negocio**: La lógica de validación se ha extraído a una clase de utilidad (`util/Validators.kt`) para promover la reutilización y el código limpio.

## Cómo Ejecutar el Proyecto

1.  Clonar este repositorio en su máquina local.
2.  Abrir el proyecto utilizando una versión reciente de Android Studio.
3.  Permitir que Gradle sincronice todas las dependencias del proyecto especificadas en los archivos `build.gradle.kts` y `gradle/libs.versions.toml`.
4.  Ejecutar la aplicación en un emulador o dispositivo físico. Para probar los diseños adaptativos, se recomienda crear emuladores con diferentes tamaños de pantalla (móvil, tablet pequeña y tablet grande).

## Desarrolladores

* **Nicolás Fonseca** - *Desarrollador Principal*
* **Bastián Rubio** - *Desarrollador Secundario*

---