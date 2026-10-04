package Model

import State.EstadoSlot

class Slot(

    val numero: Int

) {

    var estado: EstadoSlot = EstadoSlot.Libre
        private set

    val estaLibre: Boolean get() = estado is EstadoSlot.Libre


    val bicicleta: Bicicleta?
        get() {
            val actual = estado
            return when (actual) {
                is EstadoSlot.Arrendada -> actual.bicicleta
                is EstadoSlot.Libre -> null
                is EstadoSlot.EnProceso -> null
                is EstadoSlot.EnMantencion -> null
            }
        }


    fun iniciarProceso(motivo: String) { estado = EstadoSlot.EnProceso(motivo) }
    fun asignar(bici: Bicicleta) { estado = EstadoSlot.Arrendada(bici) }
    fun liberar() { estado = EstadoSlot.Libre }
    fun ponerEnMantencion(motivo: String) { estado = EstadoSlot.EnMantencion(motivo) }


    fun descripcionEstado(): String {
        val actual = estado
        return when (actual) {
            is EstadoSlot.Libre -> "LIBRE"
            is EstadoSlot.Arrendada -> "ARRENDADA — ${actual.bicicleta.codigo}"
            is EstadoSlot.EnProceso -> "EN PROCESO — ${actual.motivo}"
            is EstadoSlot.EnMantencion -> "EN MANTENCIÓN — ${actual.motivo}"
        }
    }

    fun mostrarEstado() = println("Slot #$numero: ${descripcionEstado()}")
}
