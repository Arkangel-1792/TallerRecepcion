# Guía paso a paso

## 1. Abrir el proyecto

1. Descomprime el archivo recibido.
2. Abre Android Studio.
3. Pulsa **Open** y selecciona la carpeta `TallerRecepcion` que contiene `settings.gradle.kts`.
4. Si Android Studio pregunta por el JDK, selecciona **Embedded JDK 17**.
5. Espera a que aparezca el mensaje de sincronización completada.

No abras solamente la carpeta `app`; Android Studio necesita la raíz completa del proyecto.

## 2. Ejecutar la aplicación

1. Conecta un teléfono con depuración USB o crea un emulador API 26 o superior.
2. En la barra superior selecciona la configuración `app`.
3. Selecciona el dispositivo.
4. Pulsa el triángulo verde **Run**.

## 3. Recorrido de prueba recomendado

1. En Inicio pulsa **Nueva recepción**.
2. Registra un cliente y un vehículo.
3. Pulsa **Consultar catálogo público de marcas** para comprobar Retrofit.
4. Regresa, completa kilometraje y motivo.
5. Pulsa **Tomar fotografía** y concede el permiso.
6. Guarda la recepción.
7. Abre la orden y avanza su estado.
8. Ve a Ajustes y activa el modo oscuro.
9. Cierra completamente la app y vuelve a abrirla.

Al regresar deben mantenerse tanto la orden como el modo oscuro.

## 4. Probar el rechazo del permiso

1. Ve a los ajustes de Android para la aplicación.
2. Revoca el permiso de cámara.
3. Abre una nueva recepción y pulsa **Tomar fotografía**.
4. Niega el permiso.

La app debe mostrar un aviso y permitir guardar el formulario sin evidencia fotográfica.

## 5. Probar el estado de error de Retrofit

1. Desactiva Wi-Fi y datos móviles.
2. Abre la pantalla Marcas.
3. Comprueba el mensaje de error y el botón **Reintentar**.
4. Reactiva la conexión y pulsa **Reintentar**.

## 6. Ejecutar las pruebas unitarias

Desde Android Studio:

1. Abre `app/src/test`.
2. Haz clic derecho sobre la carpeta del paquete.
3. Elige **Run Tests**.

Desde la terminal de Android Studio:

```bash
./gradlew testDebugUnitTest
```

## 7. Generar el APK de prueba

En Android Studio usa:

**Build > Build Bundle(s) / APK(s) > Build APK(s)**

El resultado normalmente queda en:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## 8. Crear tu firma y generar el AAB

Aunque el paquete de entrega ya puede contener un AAB firmado para la evaluación, debes entender el proceso y conservar tu clave privada.

1. Abre **Build > Generate Signed Bundle / APK**.
2. Selecciona **Android App Bundle**.
3. Pulsa **Create new** para crear un keystore si todavía no tienes uno.
4. Elige una ruta privada fuera del repositorio.
5. Define alias y contraseñas que puedas conservar.
6. Selecciona la variante `release`.
7. Finaliza la generación.

El archivo suele quedar en:

```text
app/release/app-release.aab
```

No publiques el keystore ni sus contraseñas en Git. Si en el futuro actualizas la misma app, necesitarás la misma clave.

## 9. Si aparece un error frecuente

| Situación | Revisión rápida |
|---|---|
| Gradle usa otro Java | Selecciona JDK 17 en Settings > Build Tools > Gradle. |
| Falta SDK 35 | Instálalo desde SDK Manager. |
| La API no carga | Comprueba internet y pulsa Reintentar. |
| La cámara no abre en emulador | Configura una cámara virtual o usa un teléfono físico. |
| No se ve la foto anterior | No borres los datos de la aplicación entre pruebas. |

