package Model

import java.time.LocalDateTime

abstract class Bicicleta (

    val codigo: String,
    val marca: String,
    val modelo: String,
    val horaIngreso: LocalDateTime,
    val tipoCliente: TipoCliente,

) {

    companion object {

        const val IVA = 0.19
        const val DESCUENTO_ABONADO = 0.20
        const val DESCUENTO_DISCAPACITADO = 0.50

        val FORMATO_CODIGO = Regex("^[A-Za-z]{2}[0-9]{2}[A-Za-z]{2}$")

        fun codigoValido(codigo: String): Boolean = FORMATO_CODIGO.matches(codigo)

    }

    abstract fun calcularCosto(minutosUso: Long): Double
    abstract fun tipoBicicleta(): String
    open fun detalle(): String = tipoBicicleta()
    open fun permiteTarifaCero(minutosUso: Long): Boolean = false

    fun calcularTotal(minutosUso: Long): Double {

        val costo = calcularCosto(minutosUso)
        val conIva = costo * (1 + IVA)
        return if (tipoCliente == TipoCliente.DISCAPACITADO) conIva * (1 - DESCUENTO_DISCAPACITADO) else conIva

    }

}