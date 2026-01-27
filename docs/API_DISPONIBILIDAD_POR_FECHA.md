# API: Disponibilidad por Fecha

## Nuevo Endpoint

### `GET /disponibilidad/{usuarioId}/fecha/{fecha}`

Obtiene las disponibilidades **vigentes** para un profesional en una fecha específica.

---

## Request

| Parámetro | Tipo | Ubicación | Descripción |
|-----------|------|-----------|-------------|
| `usuarioId` | Long | Path | ID del profesional |
| `fecha` | String | Path | Fecha en formato `YYYY-MM-DD` |

### Ejemplo
```
GET /disponibilidad/159/fecha/2026-01-27
```

---

## Response

### Success (200 OK)

Retorna un array de disponibilidades que cumplen con:
- `fechaInicio <= fecha <= fechaFin`
- `diaSemana` coincide con el día de la semana de la fecha

```json
[
  {
    "id": 558,
    "usuario": {
      "id": 159,
      "nombre": "Dr. Juan Pérez",
      "tipoUsuario": "PROFESIONAL"
    },
    "diaSemana": "MARTES",
    "disponible": true,
    "horarioCortado": false,
    "fechaInicio": "2026-01-27",
    "fechaFin": "2026-02-27",
    "duracionTurno": 30,
    "franjasHorarias": [
      {
        "id": 781,
        "horaInicio": "09:30:00",
        "horaFin": "13:30:00"
      }
    ]
  }
]
```

### Error (400 Bad Request)

```json
{
  "timestamp": "2026-01-27T10:30:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Formato de fecha inválido. Use el formato YYYY-MM-DD"
}
```

---

## Lógica de Filtrado

El endpoint aplica estos filtros automáticamente:

1. **Por Usuario**: Solo disponibilidades del `usuarioId` especificado
2. **Por Rango de Fechas**: `fechaInicio <= fecha AND fechaFin >= fecha`
3. **Por Día de Semana**: Se calcula automáticamente desde la fecha (ej: 2026-01-27 = MARTES)

---

## Guía de Implementación Frontend

### Paso 1: Obtener Disponibilidades para una Fecha

```typescript
async function getDisponibilidadPorFecha(
  usuarioId: number,
  fecha: string // formato YYYY-MM-DD
): Promise<Disponibilidad[]> {
  const response = await fetch(
    `${API_BASE_URL}/disponibilidad/${usuarioId}/fecha/${fecha}`
  );

  if (!response.ok) {
    throw new Error('Error al obtener disponibilidades');
  }

  return response.json();
}
```

### Paso 2: Generar Slots de Turnos Disponibles

```typescript
interface Slot {
  horaInicio: string;  // HH:mm
  horaFin: string;     // HH:mm
}

function generarSlots(disponibilidades: Disponibilidad[]): Slot[] {
  const slots: Slot[] = [];

  for (const disp of disponibilidades) {
    // Solo procesar si está disponible
    if (!disp.disponible) continue;

    for (const franja of disp.franjasHorarias) {
      const slotsEnFranja = generarSlotsEnFranja(
        franja.horaInicio,
        franja.horaFin,
        disp.duracionTurno
      );
      slots.push(...slotsEnFranja);
    }
  }

  return slots;
}

function generarSlotsEnFranja(
  horaInicio: string,
  horaFin: string,
  duracionMinutos: number
): Slot[] {
  const slots: Slot[] = [];

  let inicio = parseTime(horaInicio);
  const fin = parseTime(horaFin);

  while (inicio.getTime() + duracionMinutos * 60000 <= fin.getTime()) {
    const slotFin = new Date(inicio.getTime() + duracionMinutos * 60000);

    slots.push({
      horaInicio: formatTime(inicio),
      horaFin: formatTime(slotFin)
    });

    inicio = slotFin;
  }

  return slots;
}

function parseTime(time: string): Date {
  const [hours, minutes] = time.split(':').map(Number);
  const date = new Date();
  date.setHours(hours, minutes, 0, 0);
  return date;
}

function formatTime(date: Date): string {
  return date.toTimeString().slice(0, 5);
}
```

### Paso 3: Ejemplo Completo de Uso

```typescript
// Usuario selecciona fecha: 2026-01-27 (MARTES)
const fecha = '2026-01-27';
const profesionalId = 159;

// 1. Obtener disponibilidades vigentes para esa fecha
const disponibilidades = await getDisponibilidadPorFecha(profesionalId, fecha);

// 2. Generar slots
const slotsDisponibles = generarSlots(disponibilidades);

// Resultado para 2026-01-27:
// [
//   { horaInicio: "09:30", horaFin: "10:00" },
//   { horaInicio: "10:00", horaFin: "10:30" },
//   { horaInicio: "10:30", horaFin: "11:00" },
//   { horaInicio: "11:00", horaFin: "11:30" },
//   { horaInicio: "11:30", horaFin: "12:00" },
//   { horaInicio: "12:00", horaFin: "12:30" },
//   { horaInicio: "12:30", horaFin: "13:00" },
//   { horaInicio: "13:00", horaFin: "13:30" }
// ]

// 3. Filtrar slots ya ocupados por turnos existentes
const turnosExistentes = await getTurnosPorFecha(profesionalId, fecha);
const slotsFinal = slotsDisponibles.filter(slot =>
  !turnosExistentes.some(turno =>
    turno.hora === slot.horaInicio
  )
);
```

---

## Ejemplos con los Datos Actuales

### Caso 1: Martes 2026-01-27

**Request:**
```
GET /disponibilidad/159/fecha/2026-01-27
```

**Response:**
```json
[
  {
    "id": 558,
    "diaSemana": "MARTES",
    "fechaInicio": "2026-01-27",
    "fechaFin": "2026-02-27",
    "duracionTurno": 30,
    "franjasHorarias": [
      { "horaInicio": "09:30:00", "horaFin": "13:30:00" }
    ]
  }
]
```

**Slots generados:** 09:30 - 13:30 (8 turnos de 30 min)

**NO incluye** la disponibilidad 560 (13:00-17:00) porque su `fechaInicio` es 2026-03-03.

---

### Caso 2: Martes 2026-03-10

**Request:**
```
GET /disponibilidad/159/fecha/2026-03-10
```

**Response:**
```json
[
  {
    "id": 560,
    "diaSemana": "MARTES",
    "fechaInicio": "2026-03-03",
    "fechaFin": "2026-06-06",
    "duracionTurno": 30,
    "franjasHorarias": [
      { "horaInicio": "13:00:00", "horaFin": "17:00:00" }
    ]
  }
]
```

**Slots generados:** 13:00 - 17:00 (8 turnos de 30 min)

---

### Caso 3: Lunes 2026-01-27 (día sin disponibilidad)

**Request:**
```
GET /disponibilidad/159/fecha/2026-01-26
```

**Response:**
```json
[]
```

**Slots generados:** Ninguno (array vacío = no hay atención ese día)

---

## Migración desde el Endpoint Anterior

### Antes (incorrecto)
```typescript
// Obtenía TODAS las disponibilidades sin filtrar por fecha
const todas = await fetch(`/disponibilidad/${usuarioId}`);
// El frontend intentaba filtrar, pero fallaba
```

### Ahora (correcto)
```typescript
// Obtiene SOLO las disponibilidades vigentes para la fecha seleccionada
const vigentes = await fetch(`/disponibilidad/${usuarioId}/fecha/${fechaSeleccionada}`);
// El backend ya filtra correctamente
```

---

## Notas Importantes

1. **El endpoint anterior sigue funcionando** - `GET /disponibilidad/{usuarioId}` sigue disponible para obtener todas las disponibilidades (útil para la pantalla de configuración del profesional)

2. **Array vacío = sin disponibilidad** - Si el response es `[]`, significa que el profesional no atiende en esa fecha

3. **Múltiples disponibilidades posibles** - Una fecha puede tener múltiples disponibilidades (ej: mañana y tarde con horario cortado configurado como registros separados)

4. **Validar campo `disponible`** - Aunque el backend filtra por fecha, el frontend debe verificar que `disponible: true` antes de mostrar slots
