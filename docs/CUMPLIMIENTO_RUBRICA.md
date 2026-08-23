# Cumplimiento de la rúbrica

Esta tabla sirve como lista de verificación antes de entregar y como guía rápida durante la sustentación.

| Criterio | Implementación en Taller Recepción | Dónde mostrarlo |
|---|---|---|
| Arquitectura y buenas prácticas | Capas `ui`, `domain` y `data`; ViewModels con StateFlow; contrato e implementación de repositorio; corrutinas con `viewModelScope`. | `TallerNavHost`, ViewModels, `TallerRepository` y `TallerRepositoryImpl`. |
| Room | Entidad `OrdenTrabajoEntity`, DAO reactivo, base de datos, inventario de recepción y mapeo a dominio. | Crear una orden, cerrar y volver a abrir la app. |
| DataStore | Guarda el interruptor de modo oscuro mediante un Flow. | Activar modo oscuro, cerrar y abrir la app. |
| Retrofit | Consulta marcas reales en vPIC y transforma la respuesta. | Pantalla Marcas. |
| Carga, éxito y error | Indicador mientras consulta, listado al recibir datos, mensaje con botón Reintentar al fallar. | Abrir Marcas con y sin conexión. |
| Cámara y permiso | `RequestPermission`, `TakePicture` y `FileProvider`; rechazo no bloqueante. | Nueva recepción > Tomar fotografía. |
| UI/UX Compose | Cinco pantallas, Navigation, búsqueda, LazyColumn, LazyRow, tarjetas, modo claro/oscuro y estados reactivos. | Recorrido general de la app. |
| Imagen desde internet | Banner del inicio cargado con Coil. | Inicio con conexión. |
| Despliegue | APK de prueba y AAB release firmado. | Carpeta de entregables y Android Studio. |
| Documentación | README, diagramas, guía del código y guion de 15 minutos. | Archivos en la raíz y `docs`. |

## Lista final antes de subir

- [ ] Abrir el proyecto en Android Studio sin errores de sincronización.
- [ ] Ejecutar las pruebas unitarias.
- [ ] Instalar el APK en un teléfono o emulador.
- [ ] Crear una orden con foto.
- [ ] Cerrar y abrir la app para comprobar Room.
- [ ] Cambiar el modo oscuro y comprobar DataStore.
- [ ] Probar Marcas con conexión y luego sin conexión.
- [ ] Conservar una copia privada de la clave usada para firmar.
- [ ] Revisar que el AAB seleccionado sea el de `release`.
- [ ] Grabar el video siguiendo el guion.

## Punto que depende del proceso del estudiante

El enunciado solicita historial de Git distribuido durante cinco semanas. Un historial debe reflejar trabajo real: no conviene alterar fechas ni simular avances anteriores. Desde este momento se pueden hacer commits pequeños y claros por cada mejora, prueba o documento que realmente se complete.
