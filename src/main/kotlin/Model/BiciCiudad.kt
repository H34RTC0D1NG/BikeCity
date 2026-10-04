package Model

import java.time.LocalDateTime

class BiciCiudad (

    codigo: String,
    marca: String,
    modelo: String,
    horaIngreso: LocalDateTime,
    tipoCliente: TipoCliente

): Bicicleta(codigo, marca, modelo, horaIngreso, tipoCliente,) {

    companion object {
        const val TARIFA_BASE = 800.0
    }

    override fun calcularCosto(minutosUso: Long): Double {
        val horas = minutosUso / 60.0
        val costo = TARIFA_BASE * horas

        return if (tipoCliente == TipoCliente.ABONADO) costo * (1 - DESCUENTO_ABONADO) else costo

    }

    override fun tipoBicicleta(): String = "BiciCiudad"

}