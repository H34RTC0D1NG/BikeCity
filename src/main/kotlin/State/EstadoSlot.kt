package State

import Model.Bicicleta

sealed class EstadoSlot {

    object Libre : EstadoSlot()

    data class Arrendada(val bicicleta: Bicicleta) : EstadoSlot()
    data class EnProceso(val motivo: String) : EstadoSlot()
    data class EnMantencion(val motivo: String) : EstadoSlot()

}