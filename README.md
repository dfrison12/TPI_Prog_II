# Food Store

Aplicación de consola desarrollada en Java 21 para administrar categorías,
productos, usuarios y pedidos de comida. Los datos se mantienen en memoria con
colecciones durante la ejecución; no utiliza base de datos ni requiere login.

## Integrantes

- Darío Frison
- Maximiliano Agüero

UTN - Tecnicatura en Programación a Distancia - Programación 2.

## Arquitectura

```text
src/foodstore/
├── entities/
├── enums/
├── exception/
├── interfaces/
├── service/
```

## Requisitos

- JDK 21.
- NetBeans con soporte para proyectos Java Ant.
- No se requieren librerías externas.

## Ejecución con NetBeans

1. Abrir la carpeta como proyecto.
2. Ejecutar **Clean and Build**.
3. Ejecutar el proyecto; la clase principal es `foodstore.Main`.

Cada ejecución comienza con datos demostrativos. Los cambios viven únicamente
en memoria y se reinician al cerrar la aplicación.
