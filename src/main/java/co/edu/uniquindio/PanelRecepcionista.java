package co.edu.uniquindio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PanelRecepcionista extends JPanel {

    private final Alojamiento alojamiento;
    private final VentanaPrincipal ventanaPrincipal;
    private Recepcionista recepcionistaActual;

    private JLabel lblBienvenida;

    private JTable tablaReservas;
    private DefaultTableModel modeloReservas;
    private JTextField txtDocTitular, txtCodApto, txtEntrada, txtSalida, txtCodExterno, txtCantAdultos, txtCantMenores;
    private JComboBox<Canal> cbCanal;

    private JTextField txtFolioIdReserva, txtServicioNombre, txtServicioValor, txtServicioCantidad, txtPagoMonto;
    private JComboBox<MedioPago> cbMedioPago;
    private JTextArea txtAreaFolio;

    private JTable tablaAptos;
    private DefaultTableModel modeloAptos;
    private JComboBox<EstadoOperativo> cbEstadoApto;
    private JTextField txtDescripcionNovedad;
    private JTextArea txtHistorialNovedades;

    public PanelRecepcionista(Alojamiento alojamiento, VentanaPrincipal ventanaPrincipal) {
        this.alojamiento = alojamiento;
        this.ventanaPrincipal = ventanaPrincipal;
        construirInterfaz();
    }

    public void cargarDatosRecepcionista(Recepcionista recepcionista) {
        this.recepcionistaActual = recepcionista;
        lblBienvenida.setText("Panel de Recepción — Operador(a): " + recepcionista.getNombreUsuario());
        refrescarTodo();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new BorderLayout());
        lblBienvenida = new JLabel("Panel de Recepción");
        lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 16));
        JButton btnSalir = new JButton("Cerrar Sesión");
        btnSalir.addActionListener(e -> ventanaPrincipal.cerrarSesion());
        top.add(lblBienvenida, BorderLayout.WEST);
        top.add(btnSalir, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("1. Reservas (Directa / Externa) y Check-In/Out", crearTabReservas());
        tabs.addTab("2. Folios, Servicios y Pagos", crearTabFolios());
        tabs.addTab("3. Estado Apartamentos y Novedades", crearTabApartamentos());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel crearTabReservas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] cols = {"ID", "Titular", "Apto", "Entrada", "Salida", "Canal", "Cód. Ext", "Estado", "Total", "Saldo"};
        modeloReservas = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaReservas = new JTable(modeloReservas);
        panel.add(new JScrollPane(tablaReservas), BorderLayout.CENTER);

        JPanel barraAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnConfirmar = new JButton("Confirmar Reserva");
        btnConfirmar.addActionListener(e -> ejecutarAccionEstado("CONFIRMAR"));
        JButton btnCheckIn = new JButton("Check-In (3:00 p. m.)");
        btnCheckIn.addActionListener(e -> ejecutarAccionEstado("CHECKIN"));
        JButton btnCheckOut = new JButton("Check-Out (11:00 a. m.)");
        btnCheckOut.addActionListener(e -> ejecutarAccionEstado("CHECKOUT"));
        JButton btnNoShow = new JButton("Declarar No-Show (>10:00 p. m.)");
        btnNoShow.addActionListener(e -> ejecutarAccionEstado("NOSHOW"));
        JButton btnCancelar = new JButton("Cancelar Reserva");
        btnCancelar.addActionListener(e -> cancelarReservaRecepcion());

        barraAcciones.add(btnConfirmar);
        barraAcciones.add(btnCheckIn);
        barraAcciones.add(btnCheckOut);
        barraAcciones.add(btnNoShow);
        barraAcciones.add(btnCancelar);

        JPanel form = new JPanel(new GridLayout(5, 4, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("Cotizar y Registrar Reserva Presencial / Telefónica / Externa (ID Automático)"));

        txtDocTitular = new JTextField("1094123456");
        txtCodApto = new JTextField("APT-001");
        cbCanal = new JComboBox<>(new Canal[]{Canal.DIRECTA, Canal.EXTERNA});
        txtEntrada = new JTextField(LocalDate.now().toString());
        txtSalida = new JTextField(LocalDate.now().plusDays(2).toString());
        txtCodExterno = new JTextField();
        txtCantAdultos = new JTextField("2");
        txtCantMenores = new JTextField("0");

        JButton btnNuevoTitular = new JButton("+ Registrar Titular Rápido");
        btnNuevoTitular.addActionListener(e -> dialogoRegistroRapidoTitular());

        JButton btnCotizar = new JButton("Cotizar Estancia");
        btnCotizar.addActionListener(e -> cotizarEstanciaRecepcion());

        JButton btnCrear = new JButton("Crear Reserva");
        btnCrear.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCrear.addActionListener(e -> crearReservaRecepcion(true));

        form.add(new JLabel("Doc. Titular:")); form.add(txtDocTitular);
        form.add(new JLabel("Código Apto:")); form.add(txtCodApto);
        form.add(new JLabel("Canal:")); form.add(cbCanal);
        form.add(new JLabel("Cód. Plataforma Externa:")); form.add(txtCodExterno);
        form.add(new JLabel("Entrada (YYYY-MM-DD):")); form.add(txtEntrada);
        form.add(new JLabel("Salida (YYYY-MM-DD):")); form.add(txtSalida);
        form.add(new JLabel("Adultos (>=6 años):")); form.add(txtCantAdultos);
        form.add(new JLabel("Menores (<6 años):")); form.add(txtCantMenores);
        form.add(btnNuevoTitular); form.add(btnCotizar);
        form.add(btnCrear); form.add(new JLabel(""));

        JPanel sur = new JPanel(new BorderLayout(5, 5));
        sur.add(barraAcciones, BorderLayout.NORTH);
        sur.add(form, BorderLayout.CENTER);
        panel.add(sur, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearTabFolios() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel sup = new JPanel(new GridLayout(4, 4, 6, 6));
        sup.setBorder(BorderFactory.createTitledBorder("Gestión de Folio (Cargos Adicionales y Pagos)"));

        txtFolioIdReserva = new JTextField("RES-001");
        txtServicioNombre = new JTextField();
        txtServicioCantidad = new JTextField();
        txtServicioValor = new JTextField();
        txtPagoMonto = new JTextField();
        cbMedioPago = new JComboBox<>(MedioPago.values());

        JButton btnVerFolio = new JButton("Consultar Folio");
        btnVerFolio.addActionListener(e -> consultarFolio());
        JButton btnAgregarCargo = new JButton("Cargar Servicio Adicional");
        btnAgregarCargo.addActionListener(e -> cargarServicioAdicional());
        JButton btnRegistrarPago = new JButton("Registrar Pago");
        btnRegistrarPago.addActionListener(e -> registrarPago());

        sup.add(new JLabel("ID Reserva:")); sup.add(txtFolioIdReserva);
        sup.add(btnVerFolio); sup.add(new JLabel(""));

        sup.add(new JLabel("Servicio / Concepto:")); sup.add(txtServicioNombre);
        sup.add(new JLabel("Cantidad:")); sup.add(txtServicioCantidad);

        sup.add(new JLabel("Valor Unitario ($):")); sup.add(txtServicioValor);
        sup.add(new JLabel("")); sup.add(new JLabel(""));

        sup.add(new JLabel("Monto Pago ($):")); sup.add(txtPagoMonto);
        sup.add(new JLabel("Medio Pago:")); sup.add(cbMedioPago);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnAgregarCargo);
        botones.add(btnRegistrarPago);

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(sup, BorderLayout.CENTER);
        norte.add(botones, BorderLayout.SOUTH);

        txtAreaFolio = new JTextArea();
        txtAreaFolio.setEditable(false);
        txtAreaFolio.setFont(new Font("Monospaced", Font.PLAIN, 12));

        panel.add(norte, BorderLayout.NORTH);
        panel.add(new JScrollPane(txtAreaFolio), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearTabApartamentos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] cols = {"Código", "Nombre", "Capacidad", "Estado Operativo", "Motivo Fuera Servicio", "Novedades"};
        modeloAptos = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaAptos = new JTable(modeloAptos);
        tablaAptos.getSelectionModel().addListSelectionListener(e -> mostrarNovedadesApto());
        panel.add(new JScrollPane(tablaAptos), BorderLayout.CENTER);

        JPanel sur = new JPanel(new BorderLayout(10, 10));
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        cbEstadoApto = new JComboBox<>(EstadoOperativo.values());
        JButton btnCambiarEstado = new JButton("Cambiar Estado");
        btnCambiarEstado.addActionListener(e -> cambiarEstadoApartamento());

        txtDescripcionNovedad = new JTextField(22);
        JButton btnAgregarNovedad = new JButton("Reportar Novedad");
        btnAgregarNovedad.addActionListener(e -> reportarNovedad());

        controles.add(new JLabel("Nuevo Estado:")); controles.add(cbEstadoApto);
        controles.add(btnCambiarEstado);
        controles.add(new JLabel("Novedad:")); controles.add(txtDescripcionNovedad);
        controles.add(btnAgregarNovedad);

        txtHistorialNovedades = new JTextArea(6, 40);
        txtHistorialNovedades.setEditable(false);
        txtHistorialNovedades.setBorder(BorderFactory.createTitledBorder("Historial de Novedades del Apartamento Seleccionado"));

        sur.add(controles, BorderLayout.NORTH);
        sur.add(new JScrollPane(txtHistorialNovedades), BorderLayout.CENTER);
        panel.add(sur, BorderLayout.SOUTH);

        return panel;
    }

    private void dialogoRegistroRapidoTitular() {
        JTextField doc = new JTextField();
        JTextField nombre = new JTextField();
        JTextField tel = new JTextField();
        JTextField correo = new JTextField();
        JTextField nac = new JTextField("1992-01-01");

        Object[] campos = {
                "Documento:", doc, "Nombre completo:", nombre,
                "Teléfono:", tel, "Correo:", correo, "Fecha Nacimiento (YYYY-MM-DD):", nac
        };

        int op = JOptionPane.showConfirmDialog(this, campos, "Registro Rápido de Titular en Recepción", JOptionPane.OK_CANCEL_OPTION);
        if (op == JOptionPane.OK_OPTION) {
            try {
                Titular t = new Titular(nombre.getText(), doc.getText(), correo.getText(), tel.getText(), "1234", LocalDate.parse(nac.getText().trim()), "");
                if (t.esValido() && alojamiento.registrarUsuario(t)) {
                    txtDocTitular.setText(t.getIdUsuario());
                    JOptionPane.showMessageDialog(this, "Titular registrado (clave por defecto: 1234).");
                } else {
                    JOptionPane.showMessageDialog(this, "Datos inválidos o documento ya existente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Fecha inválida.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private ArrayList<Ocupante> construirListaOcupantesRecepcion(int adultos, int menores) {
        ArrayList<Ocupante> ocupantes = new ArrayList<>();
        for (int i = 1; i <= adultos; i++) {
            ocupantes.add(new Ocupante("Adulto " + i, LocalDate.of(1990, 1, 1)));
        }
        for (int i = 1; i <= menores; i++) {
            ocupantes.add(new Ocupante("Menor " + i, LocalDate.now().minusYears(3)));
        }
        return ocupantes;
    }

    private String construirTextoCotizacionRecepcion(Apartamento apto, LocalDate entrada, LocalDate salida,
                                                     ArrayList<Ocupante> ocupantes, Canal canal, boolean disponible) {
        String desglose = alojamiento.cotizarEstancia(apto.getIdApartamento(), entrada, salida, ocupantes);
        desglose += "Canal de reserva: " + canal + "\n";
        if (canal == Canal.EXTERNA) {
            int total = new Folio("TMP").calcularPrecioAlojamiento(apto, alojamiento.getListTemporadas(), entrada, salida, ocupantes);
            int comision = (int) Math.round(total * 0.15);
            desglose += String.format("Comisión plataforma externa (15%%): $%d COP\n", comision);
        }
        desglose += "Disponibilidad en esas fechas: " + (disponible ? "DISPONIBLE" : "NO DISPONIBLE (ocupado o bloqueado)") + "\n";
        return desglose;
    }

    private void cotizarEstanciaRecepcion() {
        try {
            String codApto = txtCodApto.getText().trim();
            Apartamento apto = alojamiento.buscarApartamento(codApto);
            if (apto == null) {
                JOptionPane.showMessageDialog(this, "Apartamento no encontrado o inactivo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate entrada = LocalDate.parse(txtEntrada.getText().trim());
            LocalDate salida = LocalDate.parse(txtSalida.getText().trim());
            if (entrada.isBefore(LocalDate.now()) || !salida.isAfter(entrada)) {
                JOptionPane.showMessageDialog(this, "Las fechas son inválidas (la entrada no puede ser pasada y la salida debe ser posterior).", "Fechas Inválidas", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int adultos = Integer.parseInt(txtCantAdultos.getText().trim());
            int menores = Integer.parseInt(txtCantMenores.getText().trim());
            if (adultos < 0 || menores < 0 || (adultos + menores) == 0) {
                JOptionPane.showMessageDialog(this, "Debe indicar al menos un ocupante válido.", "Ocupantes Inválidos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if ((adultos + menores) > apto.getCapacidaMaxPersonas()) {
                JOptionPane.showMessageDialog(this, "La cantidad de ocupantes (" + (adultos + menores)
                        + ") excede la capacidad máxima del apartamento (" + apto.getCapacidaMaxPersonas() + ").", "Capacidad Excedida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Canal canal = (Canal) cbCanal.getSelectedItem();
            ArrayList<Ocupante> ocupantes = construirListaOcupantesRecepcion(adultos, menores);
            boolean disponible = alojamiento.validarDisponibilidad(codApto, entrada, salida);
            String textoCotizacion = construirTextoCotizacionRecepcion(apto, entrada, salida, ocupantes, canal, disponible)
                    + "\n¿El huésped acepta la cotización para crear la reserva?";

            JTextArea area = new JTextArea(textoCotizacion, 14, 58);
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 12));

            if (disponible) {
                Object[] opciones = {"Confirmar y Crear Reserva", "No Acepta / Cerrar"};
                int eleccion = JOptionPane.showOptionDialog(this, new JScrollPane(area),
                        "Cotización de Estancia — " + apto.getNombreApartamento(),
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

                if (eleccion == JOptionPane.YES_OPTION) {
                    crearReservaRecepcion(false);
                }
            } else {
                JOptionPane.showMessageDialog(this, new JScrollPane(area),
                        "Cotización — Apartamento No Disponible", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad de adultos o menores inválida.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Revise el formato de fechas (YYYY-MM-DD): " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearReservaRecepcion(boolean solicitarConfirmacion) {
        try {
            String docTit = txtDocTitular.getText().trim();
            String codApto = txtCodApto.getText().trim();
            Canal canal = (Canal) cbCanal.getSelectedItem();
            LocalDate entrada = LocalDate.parse(txtEntrada.getText().trim());
            LocalDate salida = LocalDate.parse(txtSalida.getText().trim());
            String codExt = txtCodExterno.getText().trim();
            int adultos = Integer.parseInt(txtCantAdultos.getText().trim());
            int menores = Integer.parseInt(txtCantMenores.getText().trim());

            if (adultos < 0 || menores < 0 || (adultos + menores) == 0) {
                JOptionPane.showMessageDialog(this, "Debe registrar al menos un ocupante válido.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ArrayList<Ocupante> ocupantes = construirListaOcupantesRecepcion(adultos, menores);

            if (solicitarConfirmacion) {
                Apartamento apto = alojamiento.buscarApartamento(codApto);
                if (apto == null) {
                    JOptionPane.showMessageDialog(this, "Apartamento no encontrado o inactivo.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean disponible = alojamiento.validarDisponibilidad(codApto, entrada, salida);
                String textoConfirmacion = construirTextoCotizacionRecepcion(apto, entrada, salida, ocupantes, canal, disponible)
                        + "\n¿El huésped acepta esta cotización para confirmar la reserva?";

                JTextArea area = new JTextArea(textoConfirmacion, 14, 58);
                area.setEditable(false);
                area.setFont(new Font("Monospaced", Font.PLAIN, 12));

                int resp = JOptionPane.showConfirmDialog(this, new JScrollPane(area),
                        "Confirmar Cotización antes de Reservar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (resp != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            Reserva r = alojamiento.crearReserva(docTit, codApto, ocupantes, entrada, salida, canal, codExt);
            String msg = "Reserva creada con ID: " + r.getIdReserva() + "\nTotal congelado: $" + r.getValorTotalCongelado() + " COP";
            if (canal == Canal.EXTERNA) {
                msg += "\nComisión plataforma (15%): $" + r.getComisionPlataforma() + " COP";
            }
            JOptionPane.showMessageDialog(this, msg);
            refrescarTodo();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "No se pudo registrar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ejecutarAccionEstado(String accion) {
        int fila = tablaReservas.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String idReserva = (String) modeloReservas.getValueAt(fila, 0);
        boolean ok = switch (accion) {
            case "CONFIRMAR" -> alojamiento.confirmarReserva(idReserva);
            case "CHECKIN" -> alojamiento.realizarCheckIn(idReserva);
            case "CHECKOUT" -> alojamiento.realizarCheckOut(idReserva);
            case "NOSHOW" -> alojamiento.declararNoShow(idReserva);
            default -> false;
        };

        if (ok) {
            JOptionPane.showMessageDialog(this, "Operación [" + accion + "] ejecutada con éxito.");
            refrescarTodo();
        } else {
            JOptionPane.showMessageDialog(this, "No se cumplen las condiciones de horario/estado para ejecutar: " + accion, "Operación Rechazada", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelarReservaRecepcion() {
        int fila = tablaReservas.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idReserva = (String) modeloReservas.getValueAt(fila, 0);
        Reserva r = alojamiento.buscarReserva(idReserva);
        if (r == null) return;

        if (r.getEstadoReserva() != EstadoReserva.PENDIENTE && r.getEstadoReserva() != EstadoReserva.CONFIRMADA) {
            JOptionPane.showMessageDialog(this,
                    "Solo se pueden cancelar reservas en estado PENDIENTE o CONFIRMADA.\nEstado actual: " + r.getEstadoReserva(),
                    "Operación no permitida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int totalPagado = r.getFolio().calcularTotalPagos();
        int devolucion = r.previsualizarDevolucionCancelacion(LocalDate.now());
        int retenido = Math.max(0, totalPagado - devolucion);

        String mensaje = "Reserva: " + r.getIdReserva() + " (Estado: " + r.getEstadoReserva() + ")\n"
                + "Titular: " + r.getInfoTitular().getNombreUsuario() + "\n"
                + "Total abonado a la fecha: $" + totalPagado + " COP\n"
                + "Retención por política: $" + retenido + " COP\n"
                + "Cantidad a devolver al cliente: $" + devolucion + " COP\n\n"
                + "¿Desea confirmar la cancelación de esta reserva?";

        int confirm = JOptionPane.showConfirmDialog(this, mensaje,
                "Confirmar Cancelación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (alojamiento.cancelarReserva(idReserva, LocalDate.now())) {
                JOptionPane.showMessageDialog(this,
                        "Reserva cancelada exitosamente.\nDevolución registrada: $" + devolucion + " COP.");
                refrescarTodo();
                if (txtFolioIdReserva.getText().trim().equalsIgnoreCase(idReserva)) {
                    consultarFolio();
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo cancelar la reserva.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void consultarFolio() {
        String idRes = txtFolioIdReserva.getText().trim();
        Reserva r = alojamiento.buscarReserva(idRes);
        if (r == null) {
            txtAreaFolio.setText("Reserva no encontrada.");
            return;
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Folio f = r.getFolio();
        String detalle = "FOLIO: " + f.getIdFolio() + " | Titular: " + r.getInfoTitular().getNombreUsuario()
                + " | Estado: " + r.getEstadoReserva() + "\n";
        detalle += "--------------------------------------------------------------------------------------------\nCARGOS:\n";
        for (Cargo c : f.getListCargos()) {
            detalle += String.format("  - [%s | %s] %-45s : $%d COP\n",
                    c.getIdCargo(), c.getFechaCargo().format(fmt), c.getConcepto(), c.getValorCargo());
        }
        detalle += "PAGOS:\n";
        for (Pago p : f.getListPagos()) {
            detalle += String.format("  - [%s | %s] Pago %-39s : $%d COP\n",
                    p.idPago(), p.fechaPago().format(fmt), p.medioPago(), p.valorPago());
        }
        detalle += "--------------------------------------------------------------------------------------------\n";
        detalle += String.format("TOTAL CARGOS: $%d | TOTAL PAGOS: $%d | SALDO PENDIENTE: $%d COP\n",
                f.calcularTotalCargos(), f.calcularTotalPagos(), f.calcularSaldoPendiente());
        txtAreaFolio.setText(detalle);
    }

    private void cargarServicioAdicional() {
        try {
            String idRes = txtFolioIdReserva.getText().trim();
            String concepto = txtServicioNombre.getText().trim();
            int valorUnitario = Integer.parseInt(txtServicioValor.getText().trim());
            int cantidad = Integer.parseInt(txtServicioCantidad.getText().trim());

            ServicioAdicional srv = new ServicioAdicional(concepto, valorUnitario, cantidad);
            if (!srv.esValido()) {
                JOptionPane.showMessageDialog(this, "Ingrese un concepto válido, valor unitario mayor a $0 y cantidad mayor a 0.", "Datos Inválidos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (alojamiento.registrarCargoFacturable(idRes, srv)) {
                JOptionPane.showMessageDialog(this, "Cargo cargado al folio (" + cantidad + " x $" + valorUnitario + " COP).");
                txtServicioNombre.setText("");
                txtServicioCantidad.setText("");
                txtServicioValor.setText("");
                consultarFolio();
                refrescarTodo();
            } else {
                JOptionPane.showMessageDialog(this, "Reserva no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor unitario o cantidad numérica inválida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarPago() {
        try {
            String idRes = txtFolioIdReserva.getText().trim();
            Reserva r = alojamiento.buscarReserva(idRes);
            if (r == null) {
                JOptionPane.showMessageDialog(this, "Reserva no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (r.getEstadoReserva() == EstadoReserva.CANCELADA || r.getEstadoReserva() == EstadoReserva.NO_SHOW) {
                JOptionPane.showMessageDialog(this, "No se pueden registrar pagos sobre una reserva cancelada o en No-Show.", "Operación no permitida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int saldoPendiente = r.getFolio().calcularSaldoPendiente();
            if (saldoPendiente <= 0) {
                JOptionPane.showMessageDialog(this, "Esta reserva ya tiene saldo pendiente en $0. No se pueden agregar más pagos.", "Saldo Saldado", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int monto = Integer.parseInt(txtPagoMonto.getText().trim());
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, "El monto del pago debe ser mayor a $0.", "Monto Inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (monto > saldoPendiente) {
                JOptionPane.showMessageDialog(this, "El monto supera el saldo pendiente actual ($" + saldoPendiente + " COP).", "Monto Excedido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            MedioPago medio = (MedioPago) cbMedioPago.getSelectedItem();
            if (alojamiento.registrarPagoReserva(idRes, medio, monto)) {
                JOptionPane.showMessageDialog(this, "Pago registrado exitosamente.");
                txtPagoMonto.setText("");
                consultarFolio();
                refrescarTodo();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar el pago.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Monto inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarEstadoApartamento() {
        int fila = tablaAptos.getSelectedRow();
        if (fila < 0) return;
        String cod = (String) modeloAptos.getValueAt(fila, 0);
        Apartamento apto = alojamiento.buscarApartamento(cod);
        EstadoOperativo nuevo = (EstadoOperativo) cbEstadoApto.getSelectedItem();
        String motivo = null;
        if (nuevo == EstadoOperativo.FUERA_DE_SERVICIO) {
            motivo = JOptionPane.showInputDialog(this, "Motivo obligatorio para Fuera de Servicio:");
        }
        if (apto.cambiarEstadoOperativo(nuevo, motivo)) {
            refrescarTodo();
        } else {
            JOptionPane.showMessageDialog(this, "Debe indicar un motivo válido para Fuera de Servicio.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void reportarNovedad() {
        int fila = tablaAptos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un apartamento.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String cod = (String) modeloAptos.getValueAt(fila, 0);
        String desc = txtDescripcionNovedad.getText().trim();
        String autor = recepcionistaActual != null ? recepcionistaActual.getNombreUsuario() : "Recepción";
        if (alojamiento.registrarNovedadApartamento(cod, desc, autor)) {
            txtDescripcionNovedad.setText("");
            refrescarTodo();
            mostrarNovedadesApto();
        }
    }

    private void mostrarNovedadesApto() {
        int fila = tablaAptos.getSelectedRow();
        if (fila < 0) return;
        String cod = (String) modeloAptos.getValueAt(fila, 0);
        Apartamento a = alojamiento.buscarApartamento(cod);
        if (a == null) return;
        String historial = "";
        for (Novedad n : a.getListNovedades()) {
            historial += "[" + n.getIdNovedad() + " | " + n.getFechaRegistro().toLocalDate()
                    + " - " + n.getRegistradoPor() + "]: " + n.getDescripcion() + "\n";
        }
        txtHistorialNovedades.setText(historial);
    }

    public void refrescarTodo() {
        modeloReservas.setRowCount(0);
        for (Reserva r : alojamiento.getListReservas()) {
            modeloReservas.addRow(new Object[]{
                    r.getIdReserva(), r.getInfoTitular().getNombreUsuario(), r.getApartamento().getIdApartamento(),
                    r.getFechaEntrada(), r.getFechaSalida(), r.getCanal(), r.getCodigoExterno(),
                    r.getEstadoReserva(), "$" + r.getValorTotalCongelado(), "$" + r.getFolio().calcularSaldoPendiente()
            });
        }

        modeloAptos.setRowCount(0);
        for (Apartamento a : alojamiento.getListApartamentos()) {
            if (a.isActivo()) {
                modeloAptos.addRow(new Object[]{
                        a.getIdApartamento(), a.getNombreApartamento(), a.getCapacidaMaxPersonas(),
                        a.getEstadoOperativo(), a.getMotivoFueraServicio(), a.getListNovedades().size()
                });
            }
        }
    }
}