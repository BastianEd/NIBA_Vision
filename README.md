# Proyecto: ZONALIBROS - Aplicación Android Nativa

## Descripción del Proyecto

Como desarrollador, he construido esta aplicación nativa para Android como parte de mi formación en Desarrollo de Aplicaciones Móviles. El proyecto, denominado **ZONALIBROS**, implementa un sistema completo de autenticación de usuarios y una interfaz de usuario adaptativa, utilizando las tecnologías más modernas del ecosistema Android.

El objetivo principal fue aplicar mis conocimientos en **Kotlin** y **Jetpack Compose** para resolver un caso práctico, enfocándome en la validación de datos, la gestión de estado de la UI y la creación de una experiencia de usuario fluida que se adapta a distintos factores de forma, desde teléfonos móviles hasta tablets.

## Funcionalidades Implementadas

He implementado dos módulos funcionales clave en esta aplicación:

### 1. Sistema de Autenticación de Usuarios

Desarrollé un flujo de autenticación robusto que incluye las siguientes pantallas y validaciones, tal como se especificaba en los requerimientos:

* **Pantalla de Registro**:
    * **Nombre Completo**: Validación para aceptar solo caracteres alfabéticos y espacios, con una longitud máxima de 100 caracteres.
    * **Correo Electrónico**: Restringido exclusivamente al dominio `@duoc.cl` y verificado para ser único en el sistema.
    * **Contraseña**: Implementé una política de seguridad que requiere al menos 10 caracteres, una mayúscula, una minúscula, un número y un carácter especial (`@`, `#`, `$`, `%`).
    * **Confirmación de Contraseña**: Verificación en tiempo real para asegurar que coincida con la contraseña ingresada previamente.
    * **Géneros Favoritos**: Mediante `Checkbox`, permití la selección múltiple, con la validación de que al menos un género sea seleccionado.
* **Pantalla de Login**:
    * Formulario de inicio de sesión que autentica las credenciales del usuario contra un repositorio de datos en memoria (`UserRepository`).
    * Gestión de errores para credenciales incorrectas, mostrando retroalimentación clara al usuario.
* **Pantalla de Recuperación de Contraseña**:
    * Un flujo simulado donde el usuario ingresa su correo para recibir instrucciones, validando el formato del mismo.

### 2. Interfaz de Usuario Adaptativa

[cite_start]Siguiendo las directrices de diseño para múltiples dispositivos[cite: 112, 113], he implementado una pantalla de inicio que adapta su diseño (`layout`) dinámicamente según el ancho del dispositivo:

* [cite_start]**Detección de Tamaño**: Utilicé la librería `material3-window-size-class` para obtener la clase de tamaño de la ventana (`WindowWidthSizeClass`) en tiempo de ejecución[cite: 83, 106].
* [cite_start]**Diseño Compacto (Móviles)**: Para anchos pequeños, presenté una interfaz de una sola columna, optimizada para la visualización vertical[cite: 104].
* [cite_start]**Diseño Mediano (Tablets pequeñas)**: En anchos intermedios, el `layout` cambia a un diseño de dos columnas, mostrando contenido lado a lado para un mejor aprovechamiento del espacio horizontal[cite: 105].
* [cite_start]**Diseño Expandido (Tablets grandes)**: Para pantallas anchas, implementé un `layout` de tipo "Master-Detail", con un panel de navegación lateral persistente y un área de contenido principal[cite: 107].

## Arquitectura y Tecnologías

Para la construcción de este proyecto, me he basado en las siguientes herramientas y patrones de arquitectura:

* **Lenguaje**: **Kotlin**
* **Toolkit de UI**: **Jetpack Compose** para una UI declarativa y moderna.
* **Arquitectura**: Adopté una separación de responsabilidades clara:
    * **`data`**: Contiene los modelos (ej. `User`) y el `UserRepository` que simula la capa de acceso a datos.
    * **`ui`**: Alberga todos los Composables de las pantallas (`screens`) y la lógica de navegación.
    * **`navigation`**: Clases selladas (`sealed class`) para definir las rutas de navegación de forma segura.
    * **`util`**: Clases de utilidad como `Validators` para la lógica de validación desacoplada.
* **Navegación**: Utilicé **Navigation Compose** (`androidx.navigation:navigation-compose`) para gestionar el grafo de navegación y las transiciones entre pantallas.
* **Diseño Adaptativo**: La librería **`material3-window-size-class`** es el núcleo de la adaptabilidad de la interfaz.

## Cómo Ejecutar el Proyecto

1.  Clonar este repositorio en su máquina local.
2.  Abrir el proyecto utilizando Android Studio.
3.  Permitir que Gradle sincronice todas las dependencias del proyecto especificadas en el archivo `build.gradle.kts`.
4.  Ejecutar la aplicación en un emulador o dispositivo físico. Para probar los diseños adaptativos, recomiendo crear emuladores con diferentes tamaños de pantalla (móvil, tablet pequeña y tablet grande).

## Desarrolladores

* **Nicolás Fonseca** - *Desarrollador Principal*
* **Bastián Rubio** - *Desarrollador Secundario*

---
