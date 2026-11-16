package co.edu.uniquindio;

import java.time.LocalDateTime;

public record Pago(String idPago, MedioPago medioPago, int valorPago, LocalDateTime fechaPago) {
    private static int contador = 0;

    public Pago {
        if (idPago == null || idPago.isBlank()) {
            idPago = String.format("PAG-%03d", ++contador);
        }
        if (fechaPago == null) {
            fechaPago = LocalDateTime.now();
        }
    }

    public Pago(MedioPago medioPago, int valorPago) {
        this(String.format("PAG-%03d", ++contador), medioPago, valorPago, LocalDateTime.now());
    }

    public Pago(MedioPago medioPago, int valorPago, LocalDateTime fechaPago) {
        this(String.format("PAG-%03d", ++contador), medioPago, valorPago, fechaPago);
    }
}