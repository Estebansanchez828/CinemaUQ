# CinemaUQ – Sistema integral de gestión de cine

Proyecto Final de Programación II – Universidad del Quindío (2026-2).
Repositorio: https://github.com/Estebansanchez828/CinemaUQ

| Integrante | Usuario GitHub |
|---|---|
| Jorge Esteban Sánchez | [Estebansanchez828](https://github.com/Estebansanchez828) |
| Julián David Amaya | [julianamaya1928](https://github.com/julianamaya1928) |

Aplicación de escritorio en **Java 17 + JavaFX** con arquitectura **MVC**, principios **SOLID** y **10 patrones GoF**
(3 creacionales, 4 estructurales y 3 de comportamiento). Sin base de datos: los datos viven en un repositorio en memoria
(Singleton) que se llena al iniciar.

## Cómo ejecutar
1. Abrir la carpeta `CinemaUQ` en IntelliJ IDEA como proyecto Maven (JDK 17 o superior) y recargar Maven.
2. Ejecutar `co.edu.uniquindio.cinemauq.Launcher` (flecha verde junto al `main`).
3. Prueba por consola de todas las reglas y los 10 patrones: ejecutar `co.edu.uniquindio.cinemauq.PruebaFlujo`.

También: `mvn javafx:run`.

## Usuarios de demostración
| Usuario | Clave | Situación |
|---|---|---|
| admin | admin123 | Administrador |
| laura | 1234 | Saldo y puntos; un lote vencido y otro a punto de vencer; compras para cancelar al 100 %, 80 % y no permitida |
| andres | 1234 | Saldo bajo, 1.132 puntos (no alcanzan para una entrada general), recarga pendiente |
| mateo | 1234 | Tarjeta inactiva |

Las funciones se generan desde la hora actual: una en menos de 2 h, una entre 2 y 24 h y otras a más de 24 h,
más funciones pasadas con ventas, recargas y reembolsos de varios meses.

## Programa de puntos CinemaUQ
- **Acumulación:** 1 punto por cada **$100** pagados con la tarjeta (las compras con puntos no acumulan).
- **Vigencia:** cada acumulación es un lote que vence **un año** después de obtenido.
- **Uso:** se consumen primero los lotes que vencen antes; nunca se usan puntos vencidos; se verifica el saldo antes de redimir.
- **Equivalencia:** redimir cuesta **precio base ÷ 10** puntos (1 punto = $10).

| Beneficio | Precio | Puntos |
|---|---|---|
| Entrada general | $12.000 | 1.200 |
| Entrada preferencial | $15.000 | 1.500 |
| Entrada accesible | $9.600 | 960 |
| Crispetas grandes | $10.000 | 1.000 |
| Combo pareja | $24.000 | 2.400 |

## Reglas de cancelación
Más de 24 h: 100 % · entre 2 y 24 h: 80 % · menos de 2 h: no se permite · función cancelada por el cine: 100 %.
El reembolso vuelve a la tarjeta y no puede repetirse (patrón State).

## Patrones de diseño
| Patrón | Tipo | Dónde |
|---|---|---|
| Singleton | Creacional | `datos/RepositorioCinema` |
| Factory Method | Creacional | `servicio/fabrica/CreadorEntrada` + 3 creadores |
| Builder | Creacional | `servicio/constructor/ConstructorCompra`, `ConstructorCompraEstandar`, `DirectorCompra` |
| Composite | Estructural | `modelo/ItemConfiteria`, `Producto`, `Combo` |
| Decorator | Estructural | `servicio/promocion/PromocionDecorador`, `DescuentoPorcentaje`, `DescuentoValorFijo` |
| Facade | Estructural | `servicio/fachada/FachadaCompra` |
| Proxy (protección) | Estructural | `servicio/admin/ProxyAdmin` |
| State | Comportamiento | `modelo/estado/EstadoCompra` + 4 estados |
| Strategy | Comportamiento | `servicio/reembolso/PoliticaReembolso` + 4 políticas |
| Observer | Comportamiento | `servicio/evento/NotificadorCompras` + 3 observadores |

## SOLID
- **SRP:** `TarjetaVirtual`, `CuentaPuntos`, `ExportadorTxt` y cada `Reporte`.
- **OCP:** `PoliticaReembolso`, `PromocionDecorador`, `Reporte`, `CreadorEntrada`.
- **LSP:** `Usuario` (Cliente/Administrador), `Entrada`, `ItemConfiteria`, `EstadoCompra`.
- **ISP:** `AdministracionCatalogo` / `AdministracionOperaciones` y servicios pequeños.
- **DIP:** `FachadaCompra`, `ServicioReembolsoImpl`, `ServicioAdmin` reciben interfaces; `Sistema` ensambla.

## Estructura
```
src/main/java/co/edu/uniquindio/cinemauq/
├── Launcher, App, PruebaFlujo
├── modelo/ (+ estado/)   entidades, enumeraciones, Composite y State
├── excepciones/          CinemaException y 13 excepciones del dominio
├── datos/                RepositorioCinema (Singleton) y DatosIniciales
├── servicio/             servicios, Sistema y paquetes de patrones
├── reporte/              7 reportes y ExportadorTxt
├── controlador/          controladores MVC
└── vista/ (+ admin/)     vistas JavaFX
```

## Reportes
Ventas por periodo, entradas por película/función, ocupación de salas, productos y combos, recargas, cancelaciones y
reembolsos, y puntos acumulados y redimidos. Se exportan a `reportes/*.txt` con título, fecha de generación, periodo,
registros y totales. Gráficas de barras, pastel y líneas en la pestaña **Gráficas** del administrador.

## Documentación
- `docs/analisis.md` – análisis y diseño (Fase 1).
- `docs/Documento-Proyecto-Final-CinemaUQ.docx` – documento final.
- `docs/diagrama-clases.pdf` – diagrama de clases (3 páginas).
- `docs/diagramas/` – un diagrama por patrón (PDF y PNG).

## Git
Repositorio: https://github.com/Estebansanchez828/CinemaUQ

Ramas `main` (versión entregada) y `develop` (integración). Cada parte del sistema quedó en un commit propio en `develop`,
que al final se fusionó a `main`.
