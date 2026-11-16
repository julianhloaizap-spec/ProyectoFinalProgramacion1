package co.edu.uniquindio.proyectofinalprogramacion1;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class Apartamento {
    //atributos
    private static int contador = 0;

    private final String idApartamento;
    private String nombreApartamento;
    private int numeroDormitorios;
    private int capacidaMaxPersonas;
    private String caracteristicas;
    private int tarifaPersona;
    private final Map<EstadoTemporada, Integer> tarifasPorTemporada;
    private EstadoOperativo estadoOperativo;
    private String motivoFueraServicio;
    private LocalDateTime ultimaHoraSalida;
    private boolean activo;
    private final ArrayList<Bloqueo> listBloqueos;
    private final ArrayList<Novedad> listNovedades;

    // constructor
    public Apartamento(String nombreApartamento, int numeroDormitorios,
                       int capacidaMaxPersonas, String caracteristicas, int tarifaPersona) {
        this.idApartamento = String.format("APT-%03d", ++contador);
        this.nombreApartamento = nombreApartamento != null ? nombreApartamento.trim() : "";
        this.numeroDormitorios = numeroDormitorios;
        this.capacidaMaxPersonas = capacidaMaxPersonas;
        this.caracteristicas = caracteristicas != null ? caracteristicas.trim() : "";
        this.tarifaPersona = tarifaPersona;
        this.tarifasPorTemporada = new EnumMap<>(EstadoTemporada.class);
        recalcularTarifasPorDefecto();
        this.estadoOperativo = EstadoOperativo.PREPARADO;
        this.motivoFueraServicio = "";
        this.ultimaHoraSalida = null;
        this.activo = true;
        this.listBloqueos = new ArrayList<>();
        this.listNovedades = new ArrayList<>();
    }

    private void recalcularTarifasPorDefecto() {
        for (EstadoTemporada t : EstadoTemporada.values()) {
            tarifasPorTemporada.put(t, (tarifaPersona * t.getPorcentajePorDefecto()) / 100);
        }
    }

    public void setTarifaTemporada(EstadoTemporada temporada, int valorPorPersonaNoche) {
        if (temporada != null && valorPorPersonaNoche > 0) {
            tarifasPorTemporada.put(temporada, valorPorPersonaNoche);
            if (temporada == EstadoTemporada.BAJA) {
                this.tarifaPersona = valorPorPersonaNoche;
            }
        }
    }

    public int getTarifaPorTemporada(EstadoTemporada temporada) {
        if (temporada == null) return tarifaPersona;
        return tarifasPorTemporada.getOrDefault(temporada, tarifaPersona);
    }

    public boolean esValido() {
        return !idApartamento.isBlank() && !nombreApartamento.isBlank()
                && numeroDormitorios > 0 && capacidaMaxPersonas > 0
                && tarifaPersona > 0 && estadoOperativo != null;
    }

    public boolean estaBloqueado(LocalDate inicio, LocalDate fin) {
        for (Bloqueo b : listBloqueos) {
            if (b.solapaCon(inicio, fin)) return true;
        }
        return false;
    }

    public boolean cambiarEstadoOperativo(EstadoOperativo nuevoEstado, String motivo) {
        if (nuevoEstado == null) return false;
        if (nuevoEstado == EstadoOperativo.FUERA_DE_SERVICIO) {
            if (motivo == null || motivo.isBlank()) return false;
            this.motivoFueraServicio = motivo.trim();
        } else {
            this.motivoFueraServicio = "";
        }
        this.estadoOperativo = nuevoEstado;
        return true;
    }

    public void registrarNovedad(Novedad novedad) {
        if (novedad != null) listNovedades.add(novedad);
    }

    public void agregarBloqueo(Bloqueo bloqueo) {
        if (bloqueo != null && bloqueo.esValido()) listBloqueos.add(bloqueo);
    }
    //getters and setters
    public String getIdApartamento() {
        return idApartamento;
    }
    public String getNombreApartamento() {
        return nombreApartamento;
    }
    public void setNombreApartamento(String nombreApartamento) {
        this.nombreApartamento = nombreApartamento;
    }
    public int getNumeroDormitorios() {
        return numeroDormitorios;
    }
    public void setNumeroDormitorios(int numeroDormitorios) {
        this.numeroDormitorios = numeroDormitorios;
    }
    public int getCapacidaMaxPersonas() {
        return capacidaMaxPersonas;
    }
    public void setCapacidaMaxPersonas(int capacidaMaxPersonas) {
        this.capacidaMaxPersonas = capacidaMaxPersonas;
    }
    public String getCaracteristicas() {
        return caracteristicas;
    }
    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }
    public int getTarifaPersona() {
        return tarifaPersona;
    }
    public EstadoOperativo getEstadoOperativo() {
        return estadoOperativo;
    }
    public String getMotivoFueraServicio() {
        return motivoFueraServicio;
    }
    public LocalDateTime getUltimaHoraSalida() {
        return ultimaHoraSalida;
    }
    public void setUltimaHoraSalida(LocalDateTime ultimaHoraSalida) {
        this.ultimaHoraSalida = ultimaHoraSalida;
    }
    public boolean isActivo() {
        return activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    public ArrayList<Bloqueo> getListBloqueos() {
        return new ArrayList<>(listBloqueos);
    }
    public ArrayList<Novedad> getListNovedades() {
        return new ArrayList<>(listNovedades);
    }
}