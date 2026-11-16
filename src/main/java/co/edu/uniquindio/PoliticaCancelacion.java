package co.edu.uniquindio;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PoliticaCancelacion {
    private int diasSinPenalidad;
    private int diasPenalidadMedia;
    private int porcentajeRetencionMedia;
    private int porcentajeRetencionCorta;

    public PoliticaCancelacion() {
        this(7, 2, 50, 100);
    }

    public PoliticaCancelacion(int diasSinPenalidad, int diasPenalidadMedia,
                               int porcentajeRetencionMedia, int porcentajeRetencionCorta) {
        this.diasSinPenalidad = diasSinPenalidad;
        this.diasPenalidadMedia = diasPenalidadMedia;
        this.porcentajeRetencionMedia = porcentajeRetencionMedia;
        this.porcentajeRetencionCorta = porcentajeRetencionCorta;
    }

    public PoliticaCancelacion clonar() {
        return new PoliticaCancelacion(diasSinPenalidad, diasPenalidadMedia,
                porcentajeRetencionMedia, porcentajeRetencionCorta);
    }

    public int calcularDevolucion(int valorEstanciaCongelado, int totalPagado,
                                  LocalDate fechaCancelacion, LocalDate fechaEntrada) {
        if (fechaCancelacion == null || fechaEntrada == null || totalPagado <= 0) {
            return 0;
        }

        long diasAnticipacion = ChronoUnit.DAYS.between(fechaCancelacion, fechaEntrada);

        int retencion;
        if (diasAnticipacion > diasSinPenalidad) {
            retencion = 0;
        } else if (diasAnticipacion >= diasPenalidadMedia) {
            retencion = (int) Math.round(valorEstanciaCongelado * (porcentajeRetencionMedia / 100.0));
        } else {
            retencion = (int) Math.round(valorEstanciaCongelado * (porcentajeRetencionCorta / 100.0));
        }
        return Math.max(0, totalPagado - retencion);
    }

    public int getDiasSinPenalidad() { return diasSinPenalidad; }
    public void setDiasSinPenalidad(int diasSinPenalidad) { this.diasSinPenalidad = diasSinPenalidad; }
    public int getDiasPenalidadMedia() { return diasPenalidadMedia; }
    public void setDiasPenalidadMedia(int diasPenalidadMedia) { this.diasPenalidadMedia = diasPenalidadMedia; }
    public int getPorcentajeRetencionMedia() { return porcentajeRetencionMedia; }
    public void setPorcentajeRetencionMedia(int porcentajeRetencionMedia) { this.porcentajeRetencionMedia = porcentajeRetencionMedia; }
    public int getPorcentajeRetencionCorta() { return porcentajeRetencionCorta; }
    public void setPorcentajeRetencionCorta(int porcentajeRetencionCorta) { this.porcentajeRetencionCorta = porcentajeRetencionCorta; }
}