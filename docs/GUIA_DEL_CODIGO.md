# Guía sencilla del código

El objetivo de esta guía es poder explicar el proyecto con palabras propias sin memorizar cada línea.

## 1. ¿Dónde inicia la aplicación?

`MainActivity.kt` muestra el tema y llama a `TallerNavHost`. La clase `TallerRecepcionApplication` mantiene un `AppContainer`, que crea una sola base de datos, Retrofit y los repositorios.

## 2. ¿Cómo llega una orden a la pantalla?

1. Room emite la lista desde `OrdenTrabajoDao` como `Flow`.
2. `TallerRepositoryImpl` convierte las entidades a modelos del dominio.
3. `DashboardViewModel` combina la lista con el texto de búsqueda y crea `DashboardUiState`.
4. `DashboardScreen` observa el StateFlow y Compose redibuja solamente lo necesario.

La idea importante es que la pantalla no conoce el DAO.

## 3. ¿Cómo se guarda una recepción?

`NuevaOrdenScreen` solo actualiza campos y comunica acciones. `NuevaOrdenViewModel` valida los datos, construye `OrdenTrabajo` y llama a `guardarOrden` en el repositorio dentro de `viewModelScope.launch`. La implementación convierte el modelo a `OrdenTrabajoEntity` y lo inserta con el DAO.

## 4. ¿Por qué hay modelo y entidad?

- `OrdenTrabajo` representa la información que entiende la aplicación.
- `OrdenTrabajoEntity` describe cómo Room la guarda.
- `toDomain()` y `toEntity()` aíslan una capa de la otra.

Esto evita que un detalle de base de datos se propague por todas las pantallas.

## 5. ¿Cómo trabaja Retrofit?

`VehiculosApi` declara el endpoint. `TallerRepositoryImpl.consultarMarcas()` hace la llamada suspendida, limpia los resultados y devuelve modelos simples. `MarcasViewModel` controla los tres casos visibles:

- antes de responder: `cargando = true`;
- respuesta correcta: lista de marcas;
- excepción: mensaje de error y opción de reintentar.

## 6. ¿Cómo funciona DataStore?

`AjustesRepository` expone `modoOscuro` como `Flow<Boolean>`. `AjustesViewModel` lo convierte en estado para la UI y guarda cada cambio. Por eso la preferencia se mantiene al reiniciar.

## 7. ¿Cómo funciona la cámara?

Al pulsar el botón se revisa el permiso. Si falta, `RequestPermission` abre el diálogo del sistema. Con permiso concedido se crea una URI privada mediante `FileProvider` y `TakePicture` abre la cámara. Cuando la foto termina, su URI se incorpora a la orden.

## 8. ¿Por qué se usa StateFlow?

StateFlow siempre contiene el estado más reciente y Compose puede observarlo respetando el ciclo de vida. Así se evita modificar vistas manualmente cuando cambian Room, Retrofit o el formulario.

## 9. Archivos que conviene poder explicar

| Archivo | Idea principal |
|---|---|
| `TallerNavHost.kt` | Destinos y navegación. |
| `NuevaOrdenViewModel.kt` | Estado, validación y guardado. |
| `MarcasViewModel.kt` | Carga, éxito, error y filtro. |
| `TallerRepository.kt` | Operaciones que necesita el dominio. |
| `TallerRepositoryImpl.kt` | Unión de Room y Retrofit. |
| `OrdenTrabajoDao.kt` | Consultas locales. |
| `AjustesRepository.kt` | Preferencia de modo oscuro. |
| `FotoRecepcion.kt` | URI segura para la cámara. |

## 10. Respuestas cortas para la sustentación

**¿Por qué MVVM?**  
Para separar el estado y las reglas de la interfaz, haciendo que cada pantalla sea más sencilla de mantener y probar.

**¿Por qué Repository?**  
Para que el ViewModel no dependa directamente de Room ni Retrofit y exista un único punto de acceso a datos.

**¿Qué pasa sin internet?**  
Las órdenes locales siguen disponibles. La consulta de marcas muestra un error y permite reintentar.

**¿Qué pasa si niegan la cámara?**  
Se informa al usuario y el registro continúa sin foto porque la evidencia es opcional.

**¿Qué diferencia hay entre Room y DataStore?**  
Room guarda registros estructurados; DataStore guarda una preferencia pequeña.
