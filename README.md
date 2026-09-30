# 📱 ContadorOMDb

> Aplicación Android desarrollada con Java como parte del Laboratorio 3 del curso Servicios y Aplicaciones para IoT [1TEL05] — PUCP.

## 📋 Tabla de Contenidos

- [Descripción](#-descripción-del-proyecto)
- [Tecnologías](#-tecnologías-usadas)
- [Estructura](#-estructura-principal)
- [Flujo Funcional](#-flujo-funcional-implementado)
- [Implementación del Contador](#-implementación-del-contador)
- [Consumo de la API](#-consumo-de-la-api-omdb)
- [Estado](#-estado-frente-a-la-consigna)
- [Ejecución](#-cómo-ejecutar)

---

## 📝 Descripción del Proyecto

Aplicación móvil Android que integra tres funcionalidades principales:

✅ **Contador con hilos**: cuenta ascendente de 1 a 20 en background sin bloquear la UI  
✅ **Comprobación de conexión a Internet** mediante `ConnectivityManager`  
✅ **Buscador de películas**: consulta la API de OMDb por ID de IMDb y muestra el título y año

## 💻 Tecnologías Usadas

| Tecnología | Uso |
|-----------|-----|
| Java | Lenguaje base |
| Android SDK (AppCompatActivity) | Framework de la app |
| View Binding | Enlace de vistas con el código (`ActivityMainBinding`, etc.) |
| Retrofit 2 | Cliente HTTP para consumir la API |
| Gson Converter | Deserialización de JSON a objetos (Bean `PeliculaDto`) |
| LiveData + ViewModel | Observación reactiva del estado y persistencia ante rotación |
| Material Components | Botones y campos de texto estilizados |
| ConnectivityManager | Verificación de red disponible |

## 📂 Estructura Principal

```
com.example.ContadorOMDb/
├── MainActivity.java            # Menú principal
├── ContadorActivity.java        # Pantalla del contador
├── ContadorViewModel.java       # Lógica del contador (LiveData)
├── DetalleActivity.java         # Pantalla de detalle de película
├── OmdbApi.java                 # Interfaz Retrofit (endpoint)
├── PeliculaDto.java             # Bean para mapear la respuesta JSON
└── AndroidManifest.xml          # Permisos INTERNET y ACCESS_NETWORK_STATE

res/layout/
├── activity_main.xml            # Menú principal
├── activity_contador.xml        # Vista del contador
└── activity_detalle.xml         # Vista de detalle
```

## 🔄 Flujo Funcional Implementado

```
┌─────────────────────────────────────────────────────────────┐
│                    FLUJO DE LA APLICACIÓN                   │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  MainActivity (Menú Principal)                              │
│  ├── 1. "Ir al Contador"                                    │
│  │      ↓ startActivity → ContadorActivity                  │
│  │                                                          │
│  ├── 2. "Comprobar Conexión"                                │
│  │      ↓ isNetworkAvailable() → Toast Success / Error      │
│  │                                                          │
│  └── 3. Ingresar ID de IMDb + "Buscar"                      │
│         ↓ Intent con extra "IMDB_ID" → DetalleActivity      │
│                                                             │
├─────────────────────────────────────────────────────────────┤
│  ContadorActivity                                           │
│  ├── 4. "Iniciar contador"                                  │
│  │      ↓ ViewModel lanza hilo (1s × 20)                    │
│  │      ↓ LiveData observa y actualiza el TextView          │
│  └── 5. "Regresar" → finish()                               │
│                                                             │
├─────────────────────────────────────────────────────────────┤
│  DetalleActivity                                            │
│  ├── 6. Retrofit GET → omdbapi.com/?apikey=...&i=...        │
│  │      ↓ Muestra Título y Año (o Toast si no existe)       │
│  └── 7. "Regresar" → Dialog "¿Desea volver al menú          │
│         principal?" → Sí: finish() / No: permanece          │
└─────────────────────────────────────────────────────────────┘
```

| Elemento | Tipo | Descripción |
|----------|------|-------------|
| `MainActivity → ContadorActivity` | Intent explícito | Navegación al contador |
| `MainActivity → DetalleActivity` | Intent con extra `IMDB_ID` | Navegación con dato |
| `GET https://www.omdbapi.com/` | Retrofit | `apikey=bf81d461&i={imdbId}` |
| `mostrarDialogoConfirmacion()` | AlertDialog | Confirmación para regresar |

## ⏱️ Implementación del Contador

El conteo corre en un **hilo secundario** (`new Thread()`) para no bloquear el hilo principal de la UI (regla de Android: *network/IO en background*):

```java
new Thread(() -> {
    for (int i = 1; i <= 20; i++) {
        Thread.sleep(1000);
        contador.postValue(i);  // postValue: hilo secundario → UI
    }
    isRunning.postValue(false);
}).start();
```

**Puntos clave:**

| Aspecto | Solución |
|---------|----------|
| Hilo de background | `Thread` con `sleep(1000)` |
| Actualización de UI desde otro hilo | `MutableLiveData.postValue()` |
| No reinicio al girar la pantalla | `ContadorViewModel` sobrevive a la recreación de la Activity |
| Evitar doble inicio | Flag `isRunning` deshabilita el botón mientras corre |
| Fin del conteo | Se detiene en 20 y permite reiniciar desde 0 |

## 🌐 Consumo de la API OMDb

**Endpoint:** `https://www.omdbapi.com/?apikey=bf81d461&i=tt3896198`

El Bean `PeliculaDto` mapea la respuesta JSON con `@SerializedName`:

| Campo JSON | Atributo Java |
|-----------|---------------|
| `Title` | `title` |
| `Year` | `year` |
| `Response` | `response` ("True"/"False") |

**Interfaz Retrofit:**

```java
@GET("/")
Call<PeliculaDto> getPelicula(
        @Query("apikey") String apiKey,
        @Query("i") String imdbId
);
```

**Manejo de respuesta:** si `Response = "False"` se muestra *"Película no encontrada"*; si falla la red se muestra *"Error de red"* en un Toast.

## ✅ Estado frente a la Consigna

### ✔️ Implementado

- [x] Menú principal con botón "Ir al Contador", "Comprobar Conexión" y buscador por ID de IMDb
- [x] Contador 1→20 en hilo de background sin bloquear la UI
- [x] El contador **no se detiene ni reinicia** al girar la pantalla (ViewModel + LiveData)
- [x] Botón "Regresar" en el contador
- [x] Consulta a OMDb con Retrofit + Gson y Bean de mapeo (`PeliculaDto`)
- [x] Vista de detalle mostrando **Título** y **Año**
- [x] Botón "Regresar" con **AlertDialog** de confirmación ("Sí" / "No")
- [x] Permisos `INTERNET` y `ACCESS_NETWORK_STATE` en el manifest
- [x] Uso de **View Binding** en las tres Activities

### 💡 Observaciones

⚠️ **API key visible en el código** (`bf81d461`). Es aceptable para fines académicos, pero en producción debería externalizarse (ej. `BuildConfig` o servidor intermedio).

⚠️ El estado del contador en el `ViewModel` se pierde si la Activity se destruye por completo (ej. "Don't keep activities" o cierre de la app). Para persistencia total se podría usar `SavedStateHandle`.

## 🚀 Cómo Ejecutar

### Requisitos Previos

- Android Studio (Hedgehog o superior recomendado)
- JDK 17+
- Dispositivo/emulador con **API 24+** (Android 7.0)
- Conexión a Internet activa (para la búsqueda de películas)

### Pasos

1. Clonar el repositorio:

```bash
git clone https://github.com/<usuario>/LAB3_<CodigoPUCP>.git
```

2. Abrir el proyecto en **Android Studio** y sincronizar Gradle.

3. Ejecutar en un emulador o dispositivo físico:

```bash
./gradlew installDebug
```

4. Probar el flujo:
   - ID de IMDb de prueba: `tt3896198` (*Guardianes de la Galaxia Vol. 2*)
   - Girar la pantalla durante el conteo para verificar que no se reinicia

---

## 📚 Recursos Adicionales

- [OMDb API Documentation](https://www.omdbapi.com/)
- [Retrofit — Square](https://square.github.io/retrofit/)
- [Android ViewModel + LiveData](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [Android Developers — ConnectivityManager](https://developer.android.com/reference/android/net/ConnectivityManager)

---

## 📄 Licencia

Este proyecto es de uso académico y educativo como parte del Laboratorio 3 del curso **Servicios y Aplicaciones para IoT [1TEL05]** — PUCP.
