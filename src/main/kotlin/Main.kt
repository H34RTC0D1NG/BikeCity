package org.example

import Model.*
import Service.BikeCityService
import java.time.LocalDateTime

suspend fun main() {

    val sistema = BikeCityService("BikeCity", 10)

    println("==============================================")
    println("          Bienvenido a BikeCity               ")
    println("==============================================\n")

    println("=== Prueba de código inválido ===")

    sistema.registrarEntrada(BiciCiudad("123ABC", "Prueba", "Error", LocalDateTime.now(), TipoCliente.TURISTA))
    println()

    println("=== Slot en mantención ===")

    sistema.ponerSlotEnMantencion(10, "Sensor dañado")
    println()

    println("=== Registrando entradas ===")

    sistema.registrarEntrada(BiciCiudad(
        "BC12CD",
        "Trek",
        "FX3",
        LocalDateTime.now(),
        TipoCliente.ABONADO)
    )

    sistema.registrarEntrada(BiciCiudad(
        "BC99ZA",
        "Giant",
        "Escape",
        LocalDateTime.now(),
        TipoCliente.TURISTA)
    )

    sistema.registrarEntrada(BiciElectrica(
        "BE44RG",
        "Specialized",
        "Vado",
        LocalDateTime.now(),
        TipoCliente.DISCAPACITADO,
        largaAutonomia = true)
    )

    sistema.registrarEntrada(BiciElectrica(
        "BE77RG",
        "Trek",
        "Allant",
        LocalDateTime.now(),
        TipoCliente.TURISTA,
        largaAutonomia = false)
    )

    println()

    println("Slots disponibles ahora: ${sistema.slotsDisponibles()} de 10")
    sistema.mostrarSlots()
    println()


    println("=== Registrando salidas ===")

    sistema.registrarSalida("BC12CD", 75)
    sistema.registrarSalida("BC99ZA", 180)
    sistema.registrarSalida("BM22TO", 18)
    sistema.registrarSalida("BE44RG", 120)
    sistema.registrarSalida("BE77RG", 45)

    println("\n=== Prueba de bicicleta no encontrada ===")

    sistema.registrarSalida("XX00XX", 60)
    println()

    println("=== Consultas de negocio ===")

    println("Slots disponibles: ${sistema.slotsDisponibles()}")
    println("Bicicletas de abonados: ${sistema.bicicletasAbonadas().map { it.codigo }}")
    println("Ingreso promedio por bicicleta: $${"%,.0f".format(sistema.ingresoPromedio())}")
    println("Códigos finalizados: ${sistema.codigosFinalizados()}")

    val mayor = sistema.bicicletaMayorTiempo()
    println("Mayor tiempo de uso: ${mayor?.bicicleta?.codigo ?: "N/A"} — ${mayor?.minutosUso ?: 0} min")
    sistema.generarReporteCierre()

}