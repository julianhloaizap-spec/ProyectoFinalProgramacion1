package co.edu.uniquindio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class PanelRecepcionista extends JPanel {

    private final Alojamiento alojamiento;
    private final VentanaPrincipal ventanaPrincipal;
    private Recepcionista recepcionistaActual;

    private JLabel lblBienvenida;
    private boolean actualizandoTablas = false;

    private JTable tablaReservas;
    private DefaultTableModel modeloReservas;
    private JTextField txtDocTitular, txtCodApto, txtEntrada, txtSalida, txtCodExterno;
    private JComboBox<Canal> cbCanal;

    // Gestión de ocupantes al crear una reserva en recepción
    private JTextField txtOcupNombre, txtOcupFechaNac;
    private DefaultListModel<String> modeloListaOcupantes;
    private final ArrayList<Ocupante> ocupantesReservaNueva = new ArrayList<>();

    private JTextField txtFolioIdReserva, txtServicioNombre, txtServicioValor, txtServicioCantidad, txtPagoMonto;
    private JComboBox<MedioPago> cbMedioPago;
    private JTextArea txtAreaFolio;

    private JTable tablaAptos;
    private DefaultTableModel modeloAptos;
    private JComboBox<EstadoOperativo> cbEstadoApto;
    private JTextField txtDescripcionNovedad;
    private JTextArea txtHistorialNovedades;

    private JComboBox<String> cbFiltroHuespedes;
    private JTextField txtBuscarHuesped;
    private JTable tablaHuespedes;
    private DefaultTableModel modeloHuespedes;
    private JTextField txtHuespedDoc, txtHuespedNombre, txtHuespedTel, txtHuespedCorreo;
    private JLabel lblInfoEstadiaHuesped;
    private JTable tablaOcupantesHuesped;
    private DefaultTableModel modeloOcupantesHuesped;
    private JTextField txtNuevoOcupNombre, txtNuevoOcupFechaNac;

    public PanelRecepcionista(Alojamiento alojamiento, VentanaPrincipal ventanaPrincipal) {
        this.alojamiento = alojamiento;
        this.ventanaPrincipal = ventanaPrincipal;
        construirInterfaz();
    }

    public void cargarDatosRecepcionista(Recepcionista recepcionista) {
        this.recepcionistaActual = recepcionista;
        lblBienvenida.setText("Panel de Recepción — Operador(a): " + recepcionista.getNombreUsuario());
        limpiarFormularioReserva();
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
        tabs.addTab("4. Huéspedes y Ocupantes", crearTabHuespedes());
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

        // Panel izquierdo: Datos principales de la reserva
        JPanel form = new JPanel(new GridLayout(5, 4, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("Datos de la Reserva (ID Automático)"));

        txtDocTitular = new JTextField();
        txtCodApto = new JTextField();
        cbCanal = new JComboBox<>(new Canal[]{Canal.DIRECTA, Canal.EXTERNA});
        txtEntrada = new JTextField();
        txtSalida = new JTextField();
        txtCodExterno = new JTextField();

        JButton btnNuevoTitular = new JButton("+ Registrar Titular");
        btnNuevoTitular.addActionListener(e -> dialogoRegistroRapidoTitular());

        JButton btnCotizar = new JButton("Cotizar Estancia");
        btnCotizar.addActionListener(e -> cotizarEstanciaRecepcion());

        JButton btnCrear = new JButton("Crear Reserva");
        btnCrear.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCrear.addActionListener(e -> crearReservaRecepcion(true));

        JButton btnLimpiarForm = new JButton("Limpiar Todo");
        btnLimpiarForm.addActionListener(e -> limpiarFormularioReserva());

        form.add(new JLabel("Doc. Titular:")); form.add(txtDocTitular);
        form.add(new JLabel("Código Apto:")); form.add(txtCodApto);
        form.add(new JLabel("Canal:")); form.add(cbCanal);
        form.add(new JLabel("Cód. Plataforma Ext.:")); form.add(txtCodExterno);
        form.add(new JLabel("Entrada (YYYY-MM-DD):")); form.add(txtEntrada);
        form.add(new JLabel("Salida (YYYY-MM-DD):")); form.add(txtSalida);
        form.add(new JLabel("")); form.add(new JLabel(""));
        form.add(new JLabel("")); form.add(new JLabel(""));
        form.add(btnNuevoTitular); form.add(btnCotizar);
        form.add(btnCrear); form.add(btnLimpiarForm);

        // Panel derecho: Registro de ocupantes de la reserva
        JPanel panelOcupantes = new JPanel(new BorderLayout(5, 5));
        panelOcupantes.setBorder(BorderFactory.createTitledBorder("Ocupantes de la Reserva (< 6 años gratis)"));
        panelOcupantes.setPreferredSize(new Dimension(410, 0));

        JPanel formOcup = new JPanel(new GridLayout(3, 2, 5, 4));
        txtOcupNombre = new JTextField();
        txtOcupFechaNac = new JTextField();

        JButton btnAgregarOcup = new JButton("+ Agregar Ocupante");
        btnAgregarOcup.addActionListener(e -> agregarOcupanteReservaNueva());

        JButton btnIncluirTitular = new JButton("+ Incluir Titular");
        btnIncluirTitular.addActionListener(e -> incluirTitularComoOcupante());

        formOcup.add(new JLabel("Nombre Ocupante:")); formOcup.add(txtOcupNombre);
        formOcup.add(new JLabel("Fecha Nac. (YYYY-MM-DD):")); formOcup.add(txtOcupFechaNac);
        formOcup.add(btnAgregarOcup); formOcup.add(btnIncluirTitular);

        modeloListaOcupantes = new DefaultListModel<>();
        JList<String> listaOcupantes = new JList<>(modeloListaOcupantes);
        listaOcupantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel accionesListaOcup = new JPanel(new GridLayout(1, 2, 5, 0));
        JButton btnQuitarOcup = new JButton("- Quitar Seleccionado");
        btnQuitarOcup.addActionListener(e -> {
            int idx = listaOcupantes.getSelectedIndex();
            if (idx >= 0 && idx < ocupantesReservaNueva.size()) {
                ocupantesReservaNueva.remove(idx);
                modeloListaOcupantes.remove(idx);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un ocupante de la lista para quitar.", "Atención", JOptionPane.WARNING_MESSAGE);
            }
        });
        JButton btnLimpiarOcup = new JButton("Vaciar Lista");
        btnLimpiarOcup.addActionListener(e -> {
            ocupantesReservaNueva.clear();
            modeloListaOcupantes.clear();
            txtOcupNombre.setText("");
            txtOcupFechaNac.setText("");
        });
        accionesListaOcup.add(btnQuitarOcup);
        accionesListaOcup.add(btnLimpiarOcup);

        panelOcupantes.add(formOcup, BorderLayout.NORTH);
        panelOcupantes.add(new JScrollPane(listaOcupantes), BorderLayout.CENTER);
        panelOcupantes.add(accionesListaOcup, BorderLayout.SOUTH);

        JPanel contenedorFormularios = new JPanel(new BorderLayout(10, 5));
        contenedorFormularios.setPreferredSize(new Dimension(0, 235));
        contenedorFormularios.add(form, BorderLayout.CENTER);
        contenedorFormularios.add(panelOcupantes, BorderLayout.EAST);

        JPanel sur = new JPanel(new BorderLayout(5, 5));
        sur.add(barraAcciones, BorderLayout.NORTH);
        sur.add(contenedorFormularios, BorderLayout.CENTER);
        panel.add(sur, BorderLayout.SOUTH);

        return panel;
    }

    private void agregarOcupanteReservaNueva() {
        String nombre = txtOcupNombre.getText().trim();
        String fechaTexto = txtOcupFechaNac.getText().trim();

        if (nombre.isEmpty() || fechaTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre y la fecha de nacimiento del ocupante.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LocalDate fechaNac = LocalDate.parse(fechaTexto);
            Ocupante oc = new Ocupante(nombre, fechaNac);
            if (!oc.esValido()) {
                JOptionPane.showMessageDialog(this, "Los datos del ocupante no son válidos (revise que la fecha no sea futura).", "Datos Inválidos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ocupantesReservaNueva.add(oc);
            modeloListaOcupantes.addElement(formatearEtiquetaOcupante(oc, false));
            txtOcupNombre.setText("");
            txtOcupFechaNac.setText("");
            txtOcupNombre.requestFocus();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Fecha de nacimiento inválida. Use formato YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void incluirTitularComoOcupante() {
        String docTitular = txtDocTitular.getText().trim();
        if (docTitular.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese primero el documento del titular.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario u = alojamiento.buscarUsuario(docTitular);
        if (!(u instanceof Titular titular)) {
            JOptionPane.showMessageDialog(this, "No se encontró un titular registrado con ese documento.", "Titular No Encontrado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (Ocupante existente : ocupantesReservaNueva) {
            if (existente.getNombreOcupante().equalsIgnoreCase(titular.getNombreUsuario())
                    && existente.getFechaNacOcupante().equals(titular.getFechaNacTitular())) {
                JOptionPane.showMessageDialog(this, "El titular ya se encuentra en la lista de ocupantes.", "Ya Registrado", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        Ocupante ocTitular = new Ocupante(titular.getNombreUsuario(), titular.getFechaNacTitular());
        ocupantesReservaNueva.add(ocTitular);
        modeloListaOcupantes.addElement(formatearEtiquetaOcupante(ocTitular, true));
    }

    private String formatearEtiquetaOcupante(Ocupante oc, boolean esTitular) {
        String etiqueta = oc.getNombreOcupante() + " (" + oc.getFechaNacOcupante() + ")";
        String entradaTexto = txtEntrada.getText().trim();
        if (!entradaTexto.isEmpty()) {
            try {
                LocalDate entrada = LocalDate.parse(entradaTexto);
                etiqueta += oc.pagaTarifa(entrada) ? " - Paga tarifa" : " - Menor <6 años (Gratis)";
            } catch (DateTimeParseException ignored) {
            }
        }
        if (esTitular) {
            etiqueta += " [Titular]";
        }
        return etiqueta;
    }

    private void limpiarFormularioReserva() {
        txtDocTitular.setText("");
        txtCodApto.setText("");
        cbCanal.setSelectedIndex(0);
        txtCodExterno.setText("");
        txtEntrada.setText("");
        txtSalida.setText("");
        txtOcupNombre.setText("");
        txtOcupFechaNac.setText("");
        ocupantesReservaNueva.clear();
        modeloListaOcupantes.clear();
    }

    private JPanel crearTabFolios() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel sup = new JPanel(new GridLayout(4, 4, 6, 6));
        sup.setBorder(BorderFactory.createTitledBorder("Gestión de Folio (Cargos Adicionales y Pagos)"));

        txtFolioIdReserva = new JTextField();
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

    private JPanel crearTabHuespedes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel barraFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        barraFiltros.setBorder(BorderFactory.createTitledBorder("Lista de Huéspedes Activos, Hospedados e Historial"));

        cbFiltroHuespedes = new JComboBox<>(new String[]{
                "Todos (Activos e Historial)",
                "Hospedados Actualmente (En Curso)",
                "Reservas Activas (Pendiente / Confirmada / En Curso)",
                "Historial (Finalizada / Cancelada / No-Show)"
        });
        cbFiltroHuespedes.addActionListener(e -> {
            if (!actualizandoTablas) {
                actualizarTablaHuespedes();
            }
        });

        txtBuscarHuesped = new JTextField(16);
        JButton btnBuscarHuesped = new JButton("Buscar");
        btnBuscarHuesped.addActionListener(e -> actualizarTablaHuespedes());

        JButton btnVerTodos = new JButton("Limpiar Filtro");
        btnVerTodos.addActionListener(e -> {
            cbFiltroHuespedes.setSelectedIndex(0);
            txtBuscarHuesped.setText("");
            actualizarTablaHuespedes();
        });

        JButton btnRegistrarTitular = new JButton("+ Nuevo Huésped Titular");
        btnRegistrarTitular.addActionListener(e -> dialogoRegistroRapidoTitular());

        barraFiltros.add(new JLabel("Mostrar:"));
        barraFiltros.add(cbFiltroHuespedes);
        barraFiltros.add(new JLabel("Doc / Nombre / Apto:"));
        barraFiltros.add(txtBuscarHuesped);
        barraFiltros.add(btnBuscarHuesped);
        barraFiltros.add(btnVerTodos);
        barraFiltros.add(btnRegistrarTitular);

        panel.add(barraFiltros, BorderLayout.NORTH);

        String[] colsHuespedes = {
                "ID Reserva", "Doc. Titular", "Nombre Titular", "Teléfono",
                "Apartamento", "Entrada", "Salida", "Estado", "Ocupantes"
        };
        modeloHuespedes = new DefaultTableModel(colsHuespedes, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaHuespedes = new JTable(modeloHuespedes);
        tablaHuespedes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaHuespedes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !actualizandoTablas) {
                mostrarDetalleHuespedYOcupantes();
            }
        });
        panel.add(new JScrollPane(tablaHuespedes), BorderLayout.CENTER);

        JPanel inferior = new JPanel(new GridLayout(1, 2, 10, 10));
        inferior.setPreferredSize(new Dimension(0, 240));

        JPanel panelTitular = new JPanel(new BorderLayout(6, 6));
        panelTitular.setBorder(BorderFactory.createTitledBorder("Datos del Huésped Titular"));

        lblInfoEstadiaHuesped = new JLabel("Seleccione un registro de la tabla superior");
        lblInfoEstadiaHuesped.setFont(new Font("SansSerif", Font.BOLD, 11));
        panelTitular.add(lblInfoEstadiaHuesped, BorderLayout.NORTH);

        JPanel formTitular = new JPanel(new GridLayout(4, 2, 6, 6));
        txtHuespedDoc = new JTextField();
        txtHuespedDoc.setEditable(false);
        txtHuespedNombre = new JTextField();
        txtHuespedTel = new JTextField();
        txtHuespedCorreo = new JTextField();

        formTitular.add(new JLabel("Documento:")); formTitular.add(txtHuespedDoc);
        formTitular.add(new JLabel("Nombre Completo:")); formTitular.add(txtHuespedNombre);
        formTitular.add(new JLabel("Teléfono:")); formTitular.add(txtHuespedTel);
        formTitular.add(new JLabel("Correo:")); formTitular.add(txtHuespedCorreo);
        panelTitular.add(formTitular, BorderLayout.CENTER);

        JButton btnActualizarTitular = new JButton("Guardar Datos del Titular");
        btnActualizarTitular.addActionListener(e -> guardarDatosTitularSeleccionado());
        panelTitular.add(btnActualizarTitular, BorderLayout.SOUTH);

        JPanel panelOcupantes = new JPanel(new BorderLayout(6, 6));
        panelOcupantes.setBorder(BorderFactory.createTitledBorder("Ocupantes de la Estadía / Reserva"));

        String[] colsOcup = {"#", "Nombre Ocupante", "Fecha Nac.", "Edad Ingreso", "Tarifa"};
        modeloOcupantesHuesped = new DefaultTableModel(colsOcup, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaOcupantesHuesped = new JTable(modeloOcupantesHuesped);
        tablaOcupantesHuesped.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelOcupantes.add(new JScrollPane(tablaOcupantesHuesped), BorderLayout.CENTER);

        JPanel formOcupantes = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 3));
        txtNuevoOcupNombre = new JTextField(11);
        txtNuevoOcupFechaNac = new JTextField(8);
        JButton btnAddOcup = new JButton("+ Agregar");
        btnAddOcup.addActionListener(e -> agregarOcupanteAReservaSeleccionada());
        JButton btnDelOcup = new JButton("- Quitar");
        btnDelOcup.addActionListener(e -> quitarOcupanteDeReservaSeleccionada());

        formOcupantes.add(new JLabel("Nombre:"));
        formOcupantes.add(txtNuevoOcupNombre);
        formOcupantes.add(new JLabel("Nac (YYYY-MM-DD):"));
        formOcupantes.add(txtNuevoOcupFechaNac);
        formOcupantes.add(btnAddOcup);
        formOcupantes.add(btnDelOcup);
        panelOcupantes.add(formOcupantes, BorderLayout.SOUTH);

        inferior.add(panelTitular);
        inferior.add(panelOcupantes);
        panel.add(inferior, BorderLayout.SOUTH);

        return panel;
    }

    private String obtenerCodigoCategoriaHuespedes() {
        int idx = cbFiltroHuespedes.getSelectedIndex();
        return switch (idx) {
            case 1 -> "HOSPEDADOS";
            case 2 -> "ACTIVOS";
            case 3 -> "HISTORIAL";
            default -> "TODOS";
        };
    }

    private void actualizarTablaHuespedes() {
        String idSeleccionadoPrevio = null;
        int filaPrevia = tablaHuespedes.getSelectedRow();
        if (filaPrevia >= 0 && filaPrevia < modeloHuespedes.getRowCount()) {
            idSeleccionadoPrevio = (String) modeloHuespedes.getValueAt(filaPrevia, 0);
        }

        String categoria = obtenerCodigoCategoriaHuespedes();
        String busqueda = txtBuscarHuesped.getText();
        ArrayList<Reserva> lista = alojamiento.filtrarReservasHuespedes(categoria, busqueda);

        modeloHuespedes.setRowCount(0);
        int filaRestaurar = -1;

        for (Reserva r : lista) {
            Titular t = r.getInfoTitular();
            String etiquetaEstado = r.getEstadoReserva() == EstadoReserva.EN_CURSO
                    ? "HOSPEDADO (EN_CURSO)"
                    : r.getEstadoReserva().toString();

            modeloHuespedes.addRow(new Object[]{
                    r.getIdReserva(),
                    t.getIdUsuario(),
                    t.getNombreUsuario(),
                    t.getTelefonoUsuario(),
                    r.getApartamento().getIdApartamento() + " - " + r.getApartamento().getNombreApartamento(),
                    r.getFechaEntrada(),
                    r.getFechaSalida(),
                    etiquetaEstado,
                    r.getListOcupantes().size() + " / " + r.getApartamento().getCapacidaMaxPersonas()
            });

            if (idSeleccionadoPrevio != null && r.getIdReserva().equalsIgnoreCase(idSeleccionadoPrevio)) {
                filaRestaurar = modeloHuespedes.getRowCount() - 1;
            }
        }

        if (modeloHuespedes.getRowCount() > 0) {
            int filaFinal = filaRestaurar >= 0 ? filaRestaurar : 0;
            tablaHuespedes.setRowSelectionInterval(filaFinal, filaFinal);
            mostrarDetalleHuespedYOcupantes();
        } else {
            limpiarDetalleHuespedYOcupantes();
        }
    }

    private void limpiarDetalleHuespedYOcupantes() {
        lblInfoEstadiaHuesped.setText("Sin registros para mostrar");
        txtHuespedDoc.setText("");
        txtHuespedNombre.setText("");
        txtHuespedTel.setText("");
        txtHuespedCorreo.setText("");
        modeloOcupantesHuesped.setRowCount(0);
    }

    private void mostrarDetalleHuespedYOcupantes() {
        int fila = tablaHuespedes.getSelectedRow();
        if (fila < 0) {
            limpiarDetalleHuespedYOcupantes();
            return;
        }
        String idReserva = (String) modeloHuespedes.getValueAt(fila, 0);
        Reserva r = alojamiento.buscarReserva(idReserva);
        if (r == null) return;

        Titular t = r.getInfoTitular();
        String tipoRegistro = r.getEstadoReserva() == EstadoReserva.EN_CURSO
                ? "HOSPEDADO ACTUALMENTE"
                : (r.getEstadoReserva().esActiva() ? "RESERVA ACTIVA" : "HISTORIAL");

        lblInfoEstadiaHuesped.setText(tipoRegistro + " | Reserva: " + r.getIdReserva()
                + " | Apto: " + r.getApartamento().getIdApartamento()
                + " (" + r.getFechaEntrada() + " a " + r.getFechaSalida() + ")");

        txtHuespedDoc.setText(t.getIdUsuario());
        txtHuespedNombre.setText(t.getNombreUsuario());
        txtHuespedTel.setText(t.getTelefonoUsuario());
        txtHuespedCorreo.setText(t.getCorreoUsuario());

        modeloOcupantesHuesped.setRowCount(0);
        ArrayList<Ocupante> ocupantes = r.getListOcupantes();
        for (int i = 0; i < ocupantes.size(); i++) {
            Ocupante oc = ocupantes.get(i);
            int edad = Period.between(oc.getFechaNacOcupante(), r.getFechaEntrada()).getYears();
            String tarifa = oc.pagaTarifa(r.getFechaEntrada()) ? "Paga tarifa" : "Menor < 6 años (Gratis)";
            modeloOcupantesHuesped.addRow(new Object[]{
                    i + 1,
                    oc.getNombreOcupante(),
                    oc.getFechaNacOcupante(),
                    edad + " años",
                    tarifa
            });
        }
    }

    private void guardarDatosTitularSeleccionado() {
        String doc = txtHuespedDoc.getText().trim();
        if (doc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un huésped de la lista.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (alojamiento.actualizarDatosTitular(doc, txtHuespedNombre.getText(),
                txtHuespedCorreo.getText(), txtHuespedTel.getText())) {
            JOptionPane.showMessageDialog(this, "Datos del huésped titular actualizados correctamente.");
            refrescarTodo();
        } else {
            JOptionPane.showMessageDialog(this, "Verifique que nombre, teléfono y correo no estén vacíos.", "Datos Inválidos", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void agregarOcupanteAReservaSeleccionada() {
        int fila = tablaHuespedes.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva/estadía de la lista.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String idReserva = (String) modeloHuespedes.getValueAt(fila, 0);
        Reserva r = alojamiento.buscarReserva(idReserva);
        if (r == null) return;

        if (!r.getEstadoReserva().esActiva()) {
            JOptionPane.showMessageDialog(this, "No se pueden modificar ocupantes de una estadía en el historial (" + r.getEstadoReserva() + ").", "Solo Lectura", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String nombre = txtNuevoOcupNombre.getText().trim();
            LocalDate fechaNac = LocalDate.parse(txtNuevoOcupFechaNac.getText().trim());
            Ocupante nuevo = new Ocupante(nombre, fechaNac);

            if (!nuevo.esValido()) {
                JOptionPane.showMessageDialog(this, "Ingrese un nombre y fecha de nacimiento válidos.", "Datos Inválidos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (r.agregarOcupante(nuevo)) {
                txtNuevoOcupNombre.setText("");
                txtNuevoOcupFechaNac.setText("");
                actualizarTablaHuespedes();
                JOptionPane.showMessageDialog(this, "Ocupante agregado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Se alcanzó la capacidad máxima del apartamento ("
                        + r.getApartamento().getCapacidaMaxPersonas() + " personas).", "Capacidad Máxima", JOptionPane.WARNING_MESSAGE);
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Fecha de nacimiento inválida. Use formato YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void quitarOcupanteDeReservaSeleccionada() {
        int filaReserva = tablaHuespedes.getSelectedRow();
        int filaOcupante = tablaOcupantesHuesped.getSelectedRow();
        if (filaReserva < 0 || filaOcupante < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione la estadía y el ocupante que desea quitar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String idReserva = (String) modeloHuespedes.getValueAt(filaReserva, 0);
        Reserva r = alojamiento.buscarReserva(idReserva);
        if (r == null) return;

        if (!r.getEstadoReserva().esActiva()) {
            JOptionPane.showMessageDialog(this, "No se pueden modificar ocupantes de una estadía en el historial.", "Solo Lectura", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (r.eliminarOcupante(filaOcupante)) {
            actualizarTablaHuespedes();
            JOptionPane.showMessageDialog(this, "Ocupante retirado de la estadía.");
        } else {
            JOptionPane.showMessageDialog(this, "La reserva debe conservar al menos un ocupante.", "Operación no permitida", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void dialogoRegistroRapidoTitular() {
        JTextField doc = new JTextField();
        JTextField nombre = new JTextField();
        JTextField tel = new JTextField();
        JTextField correo = new JTextField();
        JTextField nac = new JTextField();

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
                    int incluir = JOptionPane.showConfirmDialog(this,
                            "Titular registrado (clave por defecto: 1234).\n¿Desea incluirlo también en la lista de ocupantes de la reserva?",
                            "Incluir Titular como Ocupante", JOptionPane.YES_NO_OPTION);
                    if (incluir == JOptionPane.YES_OPTION) {
                        incluirTitularComoOcupante();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Datos inválidos o documento ya existente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Fecha inválida (use YYYY-MM-DD).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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
            if (codApto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese el código del apartamento a cotizar.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Apartamento apto = alojamiento.buscarApartamento(codApto);
            if (apto == null) {
                JOptionPane.showMessageDialog(this, "Apartamento no encontrado o inactivo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (txtEntrada.getText().trim().isEmpty() || txtSalida.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese las fechas de entrada y salida (YYYY-MM-DD).", "Fechas Requeridas", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDate entrada = LocalDate.parse(txtEntrada.getText().trim());
            LocalDate salida = LocalDate.parse(txtSalida.getText().trim());
            if (entrada.isBefore(LocalDate.now()) || !salida.isAfter(entrada)) {
                JOptionPane.showMessageDialog(this, "Las fechas son inválidas (la entrada no puede ser pasada y la salida debe ser posterior).", "Fechas Inválidas", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (ocupantesReservaNueva.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe registrar al menos un ocupante en la lista antes de cotizar.", "Ocupantes Requeridos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (ocupantesReservaNueva.size() > apto.getCapacidaMaxPersonas()) {
                JOptionPane.showMessageDialog(this, "La cantidad de ocupantes (" + ocupantesReservaNueva.size()
                        + ") excede la capacidad máxima del apartamento (" + apto.getCapacidaMaxPersonas() + ").", "Capacidad Excedida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Canal canal = (Canal) cbCanal.getSelectedItem();
            boolean disponible = alojamiento.validarDisponibilidad(codApto, entrada, salida);
            String textoCotizacion = construirTextoCotizacionRecepcion(apto, entrada, salida, ocupantesReservaNueva, canal, disponible)
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
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Revise el formato de fechas (YYYY-MM-DD).", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cotizar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearReservaRecepcion(boolean solicitarConfirmacion) {
        try {
            String docTit = txtDocTitular.getText().trim();
            String codApto = txtCodApto.getText().trim();
            if (docTit.isEmpty() || codApto.isEmpty()
                    || txtEntrada.getText().trim().isEmpty() || txtSalida.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete documento del titular, código de apartamento y fechas.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (ocupantesReservaNueva.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe registrar al menos un ocupante en la lista de ocupantes.", "Ocupantes Requeridos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Canal canal = (Canal) cbCanal.getSelectedItem();
            LocalDate entrada = LocalDate.parse(txtEntrada.getText().trim());
            LocalDate salida = LocalDate.parse(txtSalida.getText().trim());
            String codExt = txtCodExterno.getText().trim();

            if (solicitarConfirmacion) {
                Apartamento apto = alojamiento.buscarApartamento(codApto);
                if (apto == null) {
                    JOptionPane.showMessageDialog(this, "Apartamento no encontrado o inactivo.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (ocupantesReservaNueva.size() > apto.getCapacidaMaxPersonas()) {
                    JOptionPane.showMessageDialog(this, "La cantidad de ocupantes (" + ocupantesReservaNueva.size()
                            + ") excede la capacidad máxima del apartamento (" + apto.getCapacidaMaxPersonas() + ").", "Capacidad Excedida", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                boolean disponible = alojamiento.validarDisponibilidad(codApto, entrada, salida);
                String textoConfirmacion = construirTextoCotizacionRecepcion(apto, entrada, salida, ocupantesReservaNueva, canal, disponible)
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

            Reserva r = alojamiento.crearReserva(docTit, codApto, ocupantesReservaNueva, entrada, salida, canal, codExt);
            String msg = "Reserva creada con ID: " + r.getIdReserva()
                    + "\nOcupantes registrados: " + r.getListOcupantes().size()
                    + "\nTotal congelado: $" + r.getValorTotalCongelado() + " COP";
            if (canal == Canal.EXTERNA) {
                msg += "\nComisión plataforma (15%): $" + r.getComisionPlataforma() + " COP";
            }
            JOptionPane.showMessageDialog(this, msg);
            limpiarFormularioReserva();
            refrescarTodo();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Revise el formato de fechas (YYYY-MM-DD).", "Error", JOptionPane.ERROR_MESSAGE);
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
        actualizandoTablas = true;
        try {
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

            actualizarTablaHuespedes();
        } finally {
            actualizandoTablas = false;
        }
    }
}