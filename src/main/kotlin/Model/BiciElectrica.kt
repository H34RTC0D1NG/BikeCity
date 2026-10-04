package Model

import java.time.LocalDateTime

class BiciElectrica(

    codigo: String,
    marca: String,
    modelo: String,
    horaIngreso: LocalDateTime,
    tipoCliente: TipoCliente,
    val largaAutonomia: Boolean

) : Bicicleta(codigo, marca, modelo, horaIngreso, tipoCliente,) {

    companion object {
        const val TARIFA_BASE = 2_200.0
        const val RECARGO_AUTONOMIA = 0.30
    }

    override fun calcularCosto(minutosUso: Long): Double {
        val costo = TARIFA_BASE * (minutosUso / 60.0)
        return if (largaAutonomia) costo * (1 + RECARGO_AUTONOMIA) else costo
    }

    override fun tipoBicicleta(): String = "BiciElectrica"
    override fun detalle(): String = "BiciElectrica (larga autonomía: ${if (largaAutonomia) "sí" else "no"})"

}