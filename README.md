# Food Store

Aplicación de consola desarrollada en Java 21 para administrar categorías,
productos, usuarios y pedidos de comida. Los datos se mantienen en memoria con
colecciones durante la ejecución; no utiliza base de datos ni requiere login.

## Integrantes

- Darío Frison
- Maximiliano Agüero

UTN - Tecnicatura en Programación a Distancia - Programación 2.

## Funcionalidades

- CRUD de categorías, productos y usuarios.
- Creación, consulta, actualización y baja lógica de pedidos.
- Detalles con cantidad, precio, subtotal y total calculado.
- Control y descuento de stock.
- Filtros de pedidos por usuario.
- Filtros de productos por categoría
- Validación de entradas con `IllegalArgumentException`.
- Excepciones propias para entidades inexistentes y stock insuficiente.
- Bajas lógicas que preservan el historial.

## Arquitectura

```text
src/foodstore/
├── entities/     Modelo de dominio
├── enums/        Rol, Estado y FormaPago
├── exception/    Excepciones propias
├── interfaces/   Calculable e Identificable
├── service/      Casos de uso, colecciones y validaciones
├── ui/           Menú principal, submenús e InputHelper
├── util/         Búsqueda genérica y ordenamiento
├── DatosIniciales.java
└── Main.java
```

`Main` crea los servicios y abre la interfaz. Cada servicio encapsula su
`ArrayList`; los menús solo leen datos, invocan casos de uso y muestran el
resultado. Las entidades heredan de `Base`, y `Pedido` implementa `Calculable`.

## Requisitos

- JDK 21 o posterior
- NetBeans con soporte para proyectos Java Ant
- No se requieren librerías externas.

## Ejecución con NetBeans

1. Abrir la carpeta como proyecto.
2. Ejecutar **Clean and Build**.
3. Ejecutar el proyecto; la clase principal es `foodstore.Main`.

Cada ejecución comienza con datos demostrativos. Los cambios viven únicamente
en memoria y se reinician al cerrar la aplicación.

## Link Video demo: https://www.youtube.com/watch?v=1HPOnDzVsns
