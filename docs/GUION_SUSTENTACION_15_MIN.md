# Guion de sustentación — 15 minutos

El guion está escrito como apoyo, no para leerlo de forma rígida. Conviene ensayarlo dos veces con un cronómetro.

## 0:00–1:00 · Presentación del problema

**En pantalla:** Inicio de la app.

**Qué explicar:**

“Mi proyecto se llama Taller Recepción. Resuelve el registro de vehículos que ingresan a un taller. Quise reunir en una sola orden los datos del cliente, vehículo, kilometraje, motivo, fotografía y estado del trabajo. La aplicación es independiente y está enfocada en un proceso corto de recepción.”

## 1:00–3:30 · Demostración de una nueva recepción

**En pantalla:** Botón Nueva recepción y formulario.

**Qué hacer:**

1. Escribir un cliente de prueba.
2. Registrar placa, marca, modelo, serie, color y kilometraje.
3. Elegir servicio y combustible.
4. Agregar motivo, observación y algunos elementos del inventario.

**Qué explicar:**

“La pantalla está dividida en bloques para no presentar todos los datos sin contexto. Adapté los campos de una orden que se usa en el trabajo real, pero eliminé presupuestos, pagos y valores para mantener el alcance del proyecto. El ViewModel conserva el estado del formulario y valida los campos necesarios antes de guardar.”

## 3:30–5:00 · Cámara y permiso

**En pantalla:** Tomar fotografía.

**Qué hacer:** Conceder el permiso y tomar una foto.

**Qué explicar:**

“El permiso se pide en tiempo de ejecución, únicamente cuando hace falta. La cámara escribe en una URI creada con FileProvider. Si el usuario niega el permiso, muestro un aviso y permito continuar porque la foto es opcional.”

## 5:00–6:30 · Room y lista reactiva

**En pantalla:** Guardar, volver al inicio y abrir el detalle.

**Qué explicar:**

“Al guardar, el ViewModel llama al repositorio dentro de una corrutina. El repositorio convierte el modelo de dominio a entidad y Room lo inserta. El inicio observa un Flow, por eso la nueva orden aparece automáticamente sin recargar la pantalla.”

## 6:30–7:45 · Flujo de estados

**En pantalla:** Detalle de orden.

**Qué hacer:** Avanzar de recepción a diagnóstico y mostrar la secuencia.

**Qué explicar:**

“La orden tiene cinco estados definidos en un enum. La función `siguiente` controla el avance y evita estados inválidos. Cada cambio se actualiza en Room y se refleja en el resumen del inicio.”

## 7:45–9:30 · Retrofit

**En pantalla:** Marcas.

**Qué hacer:** Esperar la carga y buscar una marca.

**Qué explicar:**

“Retrofit consulta la API pública vPIC. El repositorio limpia y ordena la respuesta. La pantalla muestra carga, éxito y error. El filtro es local para no repetir solicitudes en cada letra.”

Si es posible, muestra brevemente el modo avión y el botón Reintentar.

## 9:30–10:30 · DataStore

**En pantalla:** Ajustes.

**Qué hacer:** Activar modo oscuro.

**Qué explicar:**

“Uso DataStore para una preferencia pequeña: el modo oscuro. Se expone como Flow y se mantiene después de cerrar la aplicación.”

## 10:30–12:45 · Arquitectura

**En pantalla:** Diagrama del README y estructura de carpetas.

**Qué explicar:**

“La interfaz envía acciones al ViewModel. El ViewModel publica StateFlow y solo conoce el contrato TallerRepository. La implementación del repositorio decide si usa Room o Retrofit. DataStore tiene un repositorio separado porque maneja preferencias de la aplicación. Usé inyección manual con AppContainer para que la creación de dependencias sea visible y sencilla.”

Abre estos archivos en este orden:

1. `TallerNavHost.kt`.
2. `NuevaOrdenViewModel.kt`.
3. `TallerRepository.kt`.
4. `TallerRepositoryImpl.kt`.
5. `OrdenTrabajoDao.kt`.

## 12:45–13:45 · Pruebas

**En pantalla:** Carpeta `app/src/test` y resultado verde.

**Qué explicar:**

“Incluí una prueba para la progresión de estados y otra para verificar que la conversión entre dominio y Room conserve los datos. Elegí esas partes porque contienen reglas fáciles de comprobar sin depender de Android.”

## 13:45–14:30 · Despliegue

**En pantalla:** APK y AAB.

**Qué explicar:**

“El APK sirve para instalación directa y pruebas. El AAB corresponde a la variante release firmada y es el formato solicitado para distribución. La clave debe mantenerse privada para futuras actualizaciones.”

## 14:30–15:00 · Cierre

**Qué explicar:**

“El resultado integra Compose, Navigation, MVVM, Repository, Room, DataStore, Retrofit, corrutinas, StateFlow, cámara y permisos. Como mejora futura agregaría clientes reutilizables, firma del cliente y sincronización en la nube.”

## Preparación antes de grabar

- Tener una orden ya creada y otra lista para registrar.
- Limpiar notificaciones y datos sensibles del teléfono.
- Probar internet, cámara y micrófono.
- Abrir previamente el README y los cinco archivos de código.
- Mostrar la hora del sistema solo si el profesor la solicita.
- Grabar en horizontal si también se mostrará Android Studio.
