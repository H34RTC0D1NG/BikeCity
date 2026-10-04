package Model

data class Ticket(

    val numero: Int,
    val bicicleta: Bicicleta,
    val minutosUso: Long,
    val montoTotal: Double

) {

    fun mostrar() {

        println("============ TICKET #$numero ============")
        println("Tipo         : ${bicicleta.detalle()}")
        println("Código       : ${bicicleta.codigo}")
        println("Bicicleta    : ${bicicleta.marca} ${bicicleta.modelo}")
        println("Cliente      : ${bicicleta.tipoCliente}")
        println("Tiempo de uso: $minutosUso minutos")
        println("Monto pagado : $${"%,.0f".format(montoTotal)}")
        println("========================================")

    }
}