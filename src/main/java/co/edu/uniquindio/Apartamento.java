package co.edu.uniquindio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Apartamento {

    private static int contador = 0;

    private final String idApartamento;
    private String nombreApartamento;
    private int numeroDormitorios;
    private int capacidaMaxPersonas;
    private String caracteristicas;
    private int tarifaPersona;
    private final int[] tarifasPorTemporada;
    private EstadoOperativo estadoOperativo;
    private String motivoFueraServicio;
    private LocalDateTime ultimaHoraSalida;
    private boolean activo;
    private final ArrayList<Bloqueo> listBloqueos;
    private final ArrayList<Novedad> listNovedades;
    private final ArrayList<String> listFotos;

    public Apartamento(String nombreApartamento, int numeroDormitorios,
                       int capacidaMaxPersonas, String caracteristicas, int tarifaPersona) {
        this.idApartamento = String.format("APT-%03d", ++contador);
        this.nombreApartamento = nombreApartamento != null ? nombreApartamento.trim() : "";
        this.numeroDormitorios = numeroDormitorios;
        this.capacidaMaxPersonas = capacidaMaxPersonas;
        this.caracteristicas = caracteristicas != null ? caracteristicas.trim() : "";
        this.tarifaPersona = tarifaPersona;
        this.tarifasPorTemporada = new int[EstadoTemporada.values().length];
        recalcularTarifasPorDefecto();
        this.estadoOperativo = EstadoOperativo.PREPARADO;
        this.motivoFueraServicio = "";
        this.ultimaHoraSalida = null;
        this.activo = true;
        this.listBloqueos = new ArrayList<>();
        this.listNovedades = new ArrayList<>();
        this.listFotos = new ArrayList<>();
    }

    public Apartamento(String nombreApartamento, int numeroDormitorios,
                       int capacidaMaxPersonas, String caracteristicas, int tarifaPersona,
                       ArrayList<String> fotosIniciales) {
        this(nombreApartamento, numeroDormitorios, capacidaMaxPersonas, caracteristicas, tarifaPersona);
        if (fotosIniciales != null) {
            for (String ruta : fotosIniciales) {
                agregarFoto(ruta);
            }
        }
    }

    private void recalcularTarifasPorDefecto() {
        for (EstadoTemporada t : EstadoTemporada.values()) {
            tarifasPorTemporada[t.ordinal()] = (tarifaPersona * t.getPorcentajePorDefecto()) / 100;
        }
    }

    public void setTarifaTemporada(EstadoTemporada temporada, int valorPorPersonaNoche) {
        if (temporada != null && valorPorPersonaNoche > 0) {
            tarifasPorTemporada[temporada.ordinal()] = valorPorPersonaNoche;
            if (temporada == EstadoTemporada.BAJA) {
                this.tarifaPersona = valorPorPersonaNoche;
            }
        }
    }

    public int getTarifaPorTemporada(EstadoTemporada temporada) {
        if (temporada == null) return tarifaPersona;
        int valor = tarifasPorTemporada[temporada.ordinal()];
        return valor > 0 ? valor : tarifaPersona;
    }

    public boolean agregarFoto(String rutaFoto) {
        if (rutaFoto == null || rutaFoto.isBlank()) return false;
        String rutaLimpia = rutaFoto.trim();
        if (!listFotos.contains(rutaLimpia)) {
            listFotos.add(rutaLimpia);
            return true;
        }
        return false;
    }

    public boolean eliminarFoto(String rutaFoto) {
        if (rutaFoto == null) return false;
        return listFotos.remove(rutaFoto.trim());
    }

    public String getFotoPrincipal() {
        return listFotos.isEmpty() ? "" : listFotos.get(0);
    }

    public ArrayList<String> getListFotos() {
        return new ArrayList<>(listFotos);
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