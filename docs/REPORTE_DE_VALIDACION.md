# Reporte de validación

Fecha de compilación: 23 de agosto de 2026.

## Resultado técnico

| Comprobación | Resultado |
|---|---|
| Compilación Kotlin debug | Correcta |
| Pruebas unitarias | 3 clases de prueba aprobadas |
| Generación APK debug | Correcta |
| Compilación release | Correcta |
| Lint vital de release | Correcto |
| Generación APK release | Correcta |
| Generación AAB release | Correcta |
| Firma del APK | Verificada con APK Signature Scheme v2 |
| Firma del AAB | Verificada |
| API vPIC | Endpoint respondió correctamente |

## Datos del paquete

- Application ID release: `com.luisbarragan.tallerrecepcion`
- Versión: `1.0` (`versionCode 1`)
- SDK mínimo: 26 (Android 8.0)
- SDK objetivo: 35
- Permisos declarados: Internet y cámara

## Huellas de los entregables

```text
APK SHA-256: 118b53e4c0b7d7a2a4039177364654ecb8122a4f58deeedec5ffc5dfe15d7dc9
AAB SHA-256: 77e002dee92e4e9d60edcc2cc7c8b4fa8355c47cc7b3e9e3bdd78b5b3ca0d510
```

## Validación manual pendiente

La comprobación final en un teléfono debe cubrir creación de una orden, permiso de cámara, persistencia después de cerrar, consulta remota, modo oscuro y recorrido por el detalle. Las capturas obtenidas en esa prueba se incorporarán al README.
