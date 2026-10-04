package Service

import kotlinx.coroutines.delay
import Model.*
import State.EstadoSlot

class BikeCityService(

    private val nombre: String,
    private val capacidad: Int

) {

    private val slots: MutableList<Slot> = MutableList(capacidad) { Slot(it + 1) }
    private val historial: MutableList<Ticket> = mutableListOf()
    private var contadorTickets = 0
    private var recaudacionTotal = 0.0
    private val recaudacionPorTipo: MutableMap<String, Double> = mutableMapOf()

    suspend fun registrarEntrada(bici: Bicicleta) {

        if (!Bicicleta.codigoValido(bici.codigo)) {
            println("[ERROR] Código de bicicleta inválido '${bici.codigo}'. Formato requerido: dos letras, dos dígitos, dos letras (ej: BC12CD).")
            return
        }

        if (slots.any { it.bicicleta?.codigo == bici.codigo }) {
            println("[ERROR] La bicicleta '${bici.codigo}' ya está registrada en un slot.")
            return
        }

        val slot = slots.firstOrNull { it.estaLibre }

        if (slot == null) {
            println("[ERROR] Sistema sin capacidad: la estación está llena. No se registró la entrada de '${bici.codigo}'.")
            return
        }

        slot.iniciarProceso("Registrando entrada de ${bici.codigo}")
        println("[INFO] Slot #${slot.numero} → registrando entrada de ${bici.codigo}...")

        delay(3_000)

        slot.asignar(bici)
        println("[OK]   Slot #${slot.numero} → entrada registrada: ${bici.detalle()} ${bici.marca} ${bici.modelo} (${bici.codigo}), cliente ${bici.tipoCliente}.")
    }

    suspend fun registrarSalida(codigo: String, minutosUso: Long) {

        val slot = slots.firstOrNull { it.bicicleta?.codigo == codigo }
        val bici = slot?.bicicleta

        if (slot == null || bici == null) {
            println("[ERROR] Bicicleta no encontrada: el código '$codigo' no está en el sistema.")
            return
        }

        slot.iniciarProceso("Calculando tarifa de $codigo")
        println("[INFO] Slot #${slot.numero} → calculando tarifa de $codigo...")

        delay(6_500)

        val total = bici.calcularTotal(minutosUso)

        if (total < 0.0 || (total == 0.0 && !bici.permiteTarifaCero(minutosUso))) {
            println("[ERROR] Resultado de tarifa inválido para '$codigo' (tiempo: $minutosUso min). No se emitió ticket.")
            slot.asignar(bici)   // la bicicleta sigue en el slot: se restaura su estado Arrendada
            return
        }

        contadorTickets++
        val ticket = Ticket(contadorTickets, bici, minutosUso, total)
        historial.add(ticket)
        recaudacionTotal += total
        recaudacionPorTipo[bici.tipoBicicleta()] = (recaudacionPorTipo[bici.tipoBicicleta()] ?: 0.0) + total
        slot.liberar()

        ticket.mostrar()
        println("[OK]   Slot #${slot.numero} → salida registrada y slot liberado.")
    }

    fun ponerSlotEnMantencion(numero: Int, motivo: String) {
        val slot = slots.firstOrNull { it.numero == numero }
        if (slot == null) {
            println("[ERROR] El slot #$numero no existe.")
            return
        }
        val actual = slot.estado
        when (actual) {
            is EstadoSlot.Libre -> {
                slot.ponerEnMantencion(motivo)
                println("[OK]   Slot #$numero → en mantención. Motivo: $motivo")
            }
            is EstadoSlot.Arrendada -> println("[ERROR] Slot #$numero tiene una bicicleta asignada; no se puede inhabilitar.")
            is EstadoSlot.EnProceso -> println("[ERROR] Slot #$numero está en proceso; no se puede inhabilitar.")
            is EstadoSlot.EnMantencion -> println("[AVISO] Slot #$numero ya está en mantención: ${actual.motivo}")
        }
    }

    fun mostrarSlots() = slots.forEach { it.mostrarEstado() }

    fun slotsDisponibles(): Int = slots.count { it.estaLibre }

    fun bicicletasAbonadas(): List<Bicicleta> =
        historial.map { it.bicicleta }.filter { it.tipoCliente == TipoCliente.ABONADO }

    fun ingresoPromedio(): Double =
        if (historial.isEmpty()) 0.0 else recaudacionTotal / historial.size

    fun codigosFinalizados(): List<String> = historial.map { it.bicicleta.codigo }
    fun bicicletaMayorTiempo(): Ticket? = historial.maxByOrNull { it.minutosUso }

    fun generarReporteCierre() {
        println("\n********************************************")
        println("*      REPORTE DE CIERRE DE TURNO          *")
        println("*      $nombre")
        println("********************************************")

        if (historial.isEmpty()) {
            println("No se atendió ninguna bicicleta en este turno.")
            return
        }

        println("\n── Detalle por bicicleta atendida ──")
        historial.forEach {
            println("Ticket #${it.numero} | ${it.bicicleta.tipoBicicleta()} | Código: ${it.bicicleta.codigo} | ${it.minutosUso} min | $${"%,.0f".format(it.montoTotal)}")
        }

        println("\n── Recaudación por tipo de bicicleta ──")
        recaudacionPorTipo.forEach { (tipo, monto) -> println("$tipo: $${"%,.0f".format(monto)}") }

        val topTipo = recaudacionPorTipo.maxByOrNull { it.value }

        println("\n── Resumen del turno ──")
        println("Total recaudado         : $${"%,.0f".format(recaudacionTotal)}")
        println("Bicicletas atendidas    : ${historial.size}")
        println("Ingreso promedio        : $${"%,.0f".format(ingresoPromedio())}")
        println("Tipo con más ingresos   : ${topTipo?.key ?: "N/A"}")
        println("Slots disponibles cierre: ${slotsDisponibles()} de $capacidad")
        println("********************************************\n")

    }
}
