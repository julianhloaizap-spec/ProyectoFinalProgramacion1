package co.edu.uniquindio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Alojamiento {
    private final String nitAlojamiento;
    private final String nombreAlojamiento;
    private final String direccionAlojamiento;

    private final ArrayList<Usuario> listUsuarios;
    private final ArrayList<Apartamento> listApartamentos;
    private final ArrayList<Reserva> listReservas;
    private final ArrayList<Temporada> listTemporadas;
    private final ArrayList<ConflictoCanal> listConflictosCanal;
    private PoliticaCancelacion politicaCancelacion;

    public Alojamiento(String nitAlojamiento, String nombreAlojamiento, String direccionAlojamiento) {
        this.nitAlojamiento = nitAlojamiento != null ? nitAlojamiento.trim() : "";
        this.nombreAlojamiento = nombreAlojamiento != null ? nombreAlojamiento.trim() : "";
        this.direccionAlojamiento = direccionAlojamiento != null ? direccionAlojamiento.trim() : "";
        this.listUsuarios = new ArrayList<>();
        this.listApartamentos = new ArrayList<>();
        this.listReservas = new ArrayList<>();
        this.listTemporadas = new ArrayList<>();
        this.listConflictosCanal = new ArrayList<>();
        this.politicaCancelacion = new PoliticaCancelacion();
    }

    public Usuario autenticarUsuario(String idUsuario, String contrasenia) {
        for (Usuario u : listUsuarios) {
            if (u.validarCredenciales(idUsuario, contrasenia)) {
                return u;
            }
        }
        return null;
    }

    public boolean registrarUsuario(Usuario usuario) {
        if (usuario != null && usuario.esValido() && buscarUsuario(usuario.getIdUsuario()) == null) {
            return listUsuarios.add(usuario);
        }
        return false;
    }

    public Usuario buscarUsuario(String idUsuario) {
        if (idUsuario == null) return null;
        for (Usuario u : listUsuarios) {
            if (u.getIdUsuario().equals(idUsuario.trim())) return u;
        }
        return null;
    }

    public boolean actualizarRecepcionista(String idRecepcionista, String nombre, String correo,
                                           String telefono, String contrasenia) {
        Usuario u = buscarUsuario(idRecepcionista);
        if (u instanceof Recepcionista r) {
            if (nombre != null && !nombre.isBlank()) r.setNombreUsuario(nombre);
            if (correo != null && !correo.isBlank()) r.setCorreoUsuario(correo);
            if (telefono != null && !telefono.isBlank()) r.setTelefonoUsuario(telefono);
            if (contrasenia != null && !contrasenia.isBlank()) r.setContraseniaUsuario(contrasenia);
            return true;
        }
        return false;
    }

    public boolean desvincularRecepcionista(String idRecepcionista) {
        Usuario u = buscarUsuario(idRecepcionista);
        if (u instanceof Recepcionista r) {
            r.setActivo(false);
            return true;
        }
        return false;
    }

    public ArrayList<Recepcionista> getListRecepcionistas() {
        ArrayList<Recepcionista> resultado = new ArrayList<>();
        for (Usuario u : listUsuarios) {
            if (u instanceof Recepcionista r) resultado.add(r);
        }
        return resultado;
    }

    public boolean registrarApartamento(Apartamento apartamento) {
        if (apartamento != null && apartamento.esValido() && buscarApartamento(apartamento.getIdApartamento()) == null) {
            return listApartamentos.add(apartamento);
        }
        return false;
    }

    public Apartamento buscarApartamento(String idApartamento) {
        if (idApartamento == null) return null;
        for (Apartamento a : listApartamentos) {
            if (a.getIdApartamento().equalsIgnoreCase(idApartamento.trim()) && a.isActivo()) return a;
        }
        return null;
    }

    public boolean eliminarApartamentoLogico(String idApartamento) {
        Apartamento apto = buscarApartamento(idApartamento);
        if (apto == null) return false;
        for (Reserva r : listReservas) {
            if (r.getApartamento().getIdApartamento().equalsIgnoreCase(idApartamento) && r.getEstadoReserva().esActiva()) {
                return false;
            }
        }
        apto.setActivo(false);
        return true;
    }

    public boolean registrarBloqueoApartamento(String idApartamento, Bloqueo bloqueo) {
        Apartamento apto = buscarApartamento(idApartamento);
        if (apto == null || bloqueo == null || !bloqueo.esValido()) return false;
        if (!validarDisponibilidad(idApartamento, bloqueo.getFechaInicio(), bloqueo.getFechaFin())) {
            return false;
        }
        apto.agregarBloqueo(bloqueo);
        return true;
    }

    public boolean registrarNovedadApartamento(String idApartamento, String descripcion, String registradoPor) {
        Apartamento apto = buscarApartamento(idApartamento);
        if (apto == null || descripcion == null || descripcion.isBlank()) return false;
        Novedad nov = new Novedad(descripcion.trim(), registradoPor);
        apto.registrarNovedad(nov);
        return true;
    }

    public boolean registrarTemporada(Temporada temporada) {
        if (temporada != null && temporada.esValido()) {
            return listTemporadas.add(temporada);
        }
        return false;
    }

    public boolean validarDisponibilidad(String idApartamento, LocalDate fechaEntrada, LocalDate fechaSalida) {
        depurarReservasPendientesVencidas();
        Apartamento apto = buscarApartamento(idApartamento);
        if (apto == null || !apto.isActivo()) return false;
        if (fechaEntrada == null || fechaSalida == null) return false;
        if (fechaEntrada.isBefore(LocalDate.now())) return false;
        if (!fechaSalida.isAfter(fechaEntrada)) return false;
        if (apto.estaBloqueado(fechaEntrada, fechaSalida)) return false;

        for (Reserva r : listReservas) {
            if (r.getApartamento().getIdApartamento().equalsIgnoreCase(idApartamento)) {
                if (r.getEstadoReserva().esActiva() && r.solapaCon(fechaEntrada, fechaSalida)) {
                    return false;
                }
            }
        }
        return true;
    }

    public ArrayList<Apartamento> buscarApartamentosDisponibles(LocalDate entrada, LocalDate salida, int cantidadPersonas) {
        ArrayList<Apartamento> disponibles = new ArrayList<>();
        for (Apartamento a : listApartamentos) {
            if (a.isActivo() && a.getCapacidaMaxPersonas() >= cantidadPersonas
                    && validarDisponibilidad(a.getIdApartamento(), entrada, salida)) {
                disponibles.add(a);
            }
        }
        return disponibles;
    }

    public String cotizarEstancia(String idApartamento, LocalDate entrada, LocalDate salida, ArrayList<Ocupante> ocupantes) {
        Apartamento apto = buscarApartamento(idApartamento);
        if (apto == null) return "Apartamento no encontrado.";
        return Folio.generarDesgloseCotizacion(apto, listTemporadas, entrada, salida, ocupantes);
    }

    public Reserva crearReserva(String idTitular, String idApartamento,
                                LocalDate fechaEntrada, LocalDate fechaSalida,
                                Canal canal, String codigoExterno) {
        Usuario u = buscarUsuario(idTitular);
        if (!(u instanceof Titular titular)) {
            throw new IllegalArgumentException("El usuario no es un titular válido.");
        }
        return crearReserva(idTitular, idApartamento, titular.getListOcupantes(),
                fechaEntrada, fechaSalida, canal, codigoExterno);
    }

    public Reserva crearReserva(String idTitular, String idApartamento,
                                ArrayList<Ocupante> ocupantes, LocalDate fechaEntrada, LocalDate fechaSalida,
                                Canal canal, String codigoExterno) {
        Usuario u = buscarUsuario(idTitular);
        if (!(u instanceof Titular titular)) {
            throw new IllegalArgumentException("El usuario no es un titular válido.");
        }

        Apartamento apto = buscarApartamento(idApartamento);
        if (apto == null) {
            throw new IllegalArgumentException("Apartamento no encontrado o inactivo.");
        }

        if (ocupantes == null || ocupantes.isEmpty()) {
            throw new IllegalArgumentException("Debe registrar al menos un ocupante.");
        }

        if (ocupantes.size() > apto.getCapacidaMaxPersonas()) {
            throw new IllegalArgumentException("La cantidad de ocupantes (" + ocupantes.size()
                    + ") excede la capacidad máxima (" + apto.getCapacidaMaxPersonas() + ").");
        }

        if (canal == Canal.EXTERNA) {
            if (codigoExterno == null || codigoExterno.isBlank()) {
                throw new IllegalArgumentException("Las reservas externas requieren un código de plataforma.");
            }
            for (Reserva r : listReservas) {
                if (r.getCanal() == Canal.EXTERNA && r.getCodigoExterno().equalsIgnoreCase(codigoExterno.trim())) {
                    registrarConflictoCanal(codigoExterno, idApartamento, fechaEntrada, fechaSalida, "Código externo duplicado");
                    throw new IllegalArgumentException("Código externo duplicado: " + codigoExterno);
                }
            }
        }

        if (!validarDisponibilidad(idApartamento, fechaEntrada, fechaSalida)) {
            if (canal == Canal.EXTERNA) {
                registrarConflictoCanal(codigoExterno, idApartamento, fechaEntrada, fechaSalida,
                        "Choque de fechas con reserva activa o bloqueo existente");
            }
            throw new IllegalStateException("El apartamento no está disponible en las fechas solicitadas.");
        }

        Reserva nueva = titular.registrarReserva(apto, titular, ocupantes, fechaEntrada, fechaSalida, canal, codigoExterno);
        nueva.liquidarYCongelarValor(listTemporadas, politicaCancelacion);
        listReservas.add(nueva);
        return nueva;
    }

    private void registrarConflictoCanal(String codigoExterno, String idApartamento,
                                         LocalDate entrada, LocalDate salida, String motivo) {
        listConflictosCanal.add(new ConflictoCanal(codigoExterno, idApartamento, entrada, salida, motivo));
    }

    public boolean confirmarReserva(String idReserva) {
        depurarReservasPendientesVencidas();
        Reserva r = buscarReserva(idReserva);
        if (r == null || r.getEstadoReserva() != EstadoReserva.PENDIENTE) return false;
        if (r.getCanal() == Canal.PORTAL) {
            if (r.getFolio().calcularTotalPagos() < r.calcularAnticipoMinimoPortal()) {
                return false;
            }
        }
        return r.cambiarEstado(EstadoReserva.CONFIRMADA);
    }

    public boolean realizarCheckIn(String idReserva) {
        return realizarCheckIn(idReserva, LocalDateTime.now());
    }

    public boolean realizarCheckIn(String idReserva, LocalDateTime momentoCheckIn) {
        Reserva r = buscarReserva(idReserva);
        if (r == null || r.getEstadoReserva() != EstadoReserva.CONFIRMADA) return false;

        Apartamento apto = r.getApartamento();
        if (apto.getEstadoOperativo() != EstadoOperativo.PREPARADO) return false;

        LocalDateTime horaMinimaEntrada = r.getFechaEntrada().atTime(15, 0);
        if (momentoCheckIn.isBefore(horaMinimaEntrada)) return false;

        if (apto.getUltimaHoraSalida() != null) {
            long horasTranscurridas = ChronoUnit.HOURS.between(apto.getUltimaHoraSalida(), momentoCheckIn);
            if (horasTranscurridas < 3) return false;
        }

        if (r.cambiarEstado(EstadoReserva.EN_CURSO)) {
            apto.cambiarEstadoOperativo(EstadoOperativo.OCUPADO, null);
            return true;
        }
        return false;
    }

    public boolean realizarCheckOut(String idReserva) {
        return realizarCheckOut(idReserva, LocalDateTime.now());
    }

    public boolean realizarCheckOut(String idReserva, LocalDateTime momentoCheckOut) {
        Reserva r = buscarReserva(idReserva);
        if (r == null || r.getEstadoReserva() != EstadoReserva.EN_CURSO) return false;

        if (r.cambiarEstado(EstadoReserva.FINALIZADA)) {
            Apartamento apto = r.getApartamento();
            apto.cambiarEstadoOperativo(EstadoOperativo.PENDIENTE_PREPARACION, null);
            apto.setUltimaHoraSalida(momentoCheckOut);
            return true;
        }
        return false;
    }

    public boolean declararNoShow(String idReserva) {
        return declararNoShow(idReserva, LocalDateTime.now());
    }

    public boolean declararNoShow(String idReserva, LocalDateTime momentoActual) {
        Reserva r = buscarReserva(idReserva);
        if (r == null || r.getEstadoReserva() != EstadoReserva.CONFIRMADA) return false;

        LocalDateTime limiteNoShow = r.getFechaEntrada().atTime(22, 0);
        if (!momentoActual.isAfter(limiteNoShow)) return false;

        return r.cambiarEstado(EstadoReserva.NO_SHOW);
    }

    public boolean cancelarReserva(String idReserva, LocalDate fechaCancelacion) {
        Reserva r = buscarReserva(idReserva);
        if (r == null) return false;
        return r.cancelar(fechaCancelacion);
    }

    public void depurarReservasPendientesVencidas() {
        depurarReservasPendientesVencidas(LocalDateTime.now());
    }

    public void depurarReservasPendientesVencidas(LocalDateTime ahora) {
        LocalDateTime limite = ahora.minusHours(24);
        for (Reserva r : listReservas) {
            if (r.getEstadoReserva() == EstadoReserva.PENDIENTE && r.getFechaReserva().isBefore(limite)) {
                r.cambiarEstado(EstadoReserva.CANCELADA);
            }
        }
    }

    public boolean registrarPagoReserva(String idReserva, MedioPago medioPago, int monto) {
        Reserva r = buscarReserva(idReserva);
        if (r == null || monto <= 0 || medioPago == null) return false;

        Pago pago = new Pago(medioPago, monto);
        r.getFolio().registrarPago(pago);

        if (r.getCanal() == Canal.PORTAL && r.getEstadoReserva() == EstadoReserva.PENDIENTE) {
            if (r.getFolio().calcularTotalPagos() >= r.calcularAnticipoMinimoPortal()) {
                r.cambiarEstado(EstadoReserva.CONFIRMADA);
            }
        }
        return true;
    }

    public boolean registrarCargoFacturable(String idReserva, IFacturable itemFacturable) {
        Reserva r = buscarReserva(idReserva);
        if (r == null || itemFacturable == null) return false;
        Cargo cargo = itemFacturable.generarCargo();
        r.getFolio().registrarCargo(cargo);
        return true;
    }

    public String generarReporteOcupacion() {
        StringBuilder sb = new StringBuilder("=== REPORTE DE OCUPACIÓN POR APARTAMENTO ===\n");
        for (Apartamento a : listApartamentos) {
            long totalReservas = 0;
            long nochesVendidas = 0;
            for (Reserva r : listReservas) {
                if (r.getApartamento().getIdApartamento().equals(a.getIdApartamento())
                        && (r.getEstadoReserva() == EstadoReserva.CONFIRMADA
                        || r.getEstadoReserva() == EstadoReserva.EN_CURSO
                        || r.getEstadoReserva() == EstadoReserva.FINALIZADA)) {
                    totalReservas++;
                    nochesVendidas += ChronoUnit.DAYS.between(r.getFechaEntrada(), r.getFechaSalida());
                }
            }
            sb.append(String.format("- %s (%s): %d reservas efectivas | %d noches vendidas | Estado: %s\n",
                    a.getNombreApartamento(), a.getIdApartamento(), totalReservas, nochesVendidas, a.getEstadoOperativo()));
        }
        return sb.toString();
    }

    public String generarReporteIngresosPeriodo(LocalDate inicio, LocalDate fin) {
        int ingresosRecaudados = 0;
        int comisionesExternas = 0;
        for (Reserva r : listReservas) {
            if (!r.getFechaEntrada().isBefore(inicio) && !r.getFechaEntrada().isAfter(fin)) {
                ingresosRecaudados += (r.getFolio().calcularTotalPagos() - r.getValorDevueltoCancelacion());
                if (r.getCanal() == Canal.EXTERNA && r.getEstadoReserva() != EstadoReserva.CANCELADA) {
                    comisionesExternas += r.getComisionPlataforma();
                }
            }
        }
        return String.format("=== REPORTE DE INGRESOS (%s a %s) ===\n" +
                        "Ingresos Netos Recaudados: $%d COP\n" +
                        "Comisiones Plataformas Externas (15%%): $%d COP\n" +
                        "Balance Neto Operación: $%d COP\n",
                inicio, fin, ingresosRecaudados, comisionesExternas, (ingresosRecaudados - comisionesExternas));
    }

    public String generarReporteReservasPorCanal() {
        StringBuilder sb = new StringBuilder("=== REPORTE DE RESERVAS POR CANAL ===\n");
        for (Canal c : Canal.values()) {
            long total = 0, canceladas = 0, noShow = 0;
            for (Reserva r : listReservas) {
                if (r.getCanal() == c) {
                    total++;
                    if (r.getEstadoReserva() == EstadoReserva.CANCELADA) canceladas++;
                    if (r.getEstadoReserva() == EstadoReserva.NO_SHOW) noShow++;
                }
            }
            sb.append(String.format("- Canal %-8s: Total = %d | Canceladas = %d | No-Show = %d\n",
                    c, total, canceladas, noShow));
        }
        return sb.toString();
    }

    public String generarReporteSaldosPendientes() {
        StringBuilder sb = new StringBuilder("=== REPORTE DE SALDOS PENDIENTES ===\n");
        for (Reserva r : listReservas) {
            if (r.getEstadoReserva().esActiva()) {
                int saldo = r.getFolio().calcularSaldoPendiente();
                if (saldo > 0) {
                    sb.append(String.format("- Reserva %s | Titular: %s | Apto: %s | Saldo Pendiente: $%d COP\n",
                            r.getIdReserva(), r.getInfoTitular().getNombreUsuario(),
                            r.getApartamento().getIdApartamento(), saldo));
                }
            }
        }
        return sb.toString();
    }

    public String generarReporteApartamentosConMasNovedades() {
        ArrayList<Apartamento> ordenados = new ArrayList<>(listApartamentos);
        ordenados.sort((a, b) -> Integer.compare(b.getListNovedades().size(), a.getListNovedades().size()));
        StringBuilder sb = new StringBuilder("=== APARTAMENTOS CON MÁS NOVEDADES ===\n");
        for (Apartamento a : ordenados) {
            sb.append(String.format("- %s (%s): %d novedades registradas\n",
                    a.getNombreApartamento(), a.getIdApartamento(), a.getListNovedades().size()));
        }
        return sb.toString();
    }

    public Reserva buscarReserva(String idReserva) {
        if (idReserva == null) return null;
        for (Reserva r : listReservas) {
            if (r.getIdReserva().equalsIgnoreCase(idReserva.trim())) return r;
        }
        return null;
    }

    public String getNitAlojamiento() { return nitAlojamiento; }
    public String getNombreAlojamiento() { return nombreAlojamiento; }
    public String getDireccionAlojamiento() { return direccionAlojamiento; }
    public PoliticaCancelacion getPoliticaCancelacion() { return politicaCancelacion; }
    public void setPoliticaCancelacion(PoliticaCancelacion politicaCancelacion) {
        if (politicaCancelacion != null) this.politicaCancelacion = politicaCancelacion;
    }
    public ArrayList<Usuario> getListUsuarios() { return new ArrayList<>(listUsuarios); }
    public ArrayList<Apartamento> getListApartamentos() { return new ArrayList<>(listApartamentos); }
    public ArrayList<Reserva> getListReservas() { return new ArrayList<>(listReservas); }
    public ArrayList<Temporada> getListTemporadas() { return new ArrayList<>(listTemporadas); }
    public ArrayList<ConflictoCanal> getListConflictosCanal() { return new ArrayList<>(listConflictosCanal); }
}