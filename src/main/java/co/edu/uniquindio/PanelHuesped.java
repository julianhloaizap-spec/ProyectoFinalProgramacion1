package co.edu.uniquindio;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class PanelHuesped extends JPanel {

    private final Alojamiento alojamiento;
    private final VentanaPrincipal ventanaPrincipal;
    private Titular titularActual;

    private JLabel lblBienvenida;

    private JTextField txtBusqEntrada, txtBusqSalida, txtBusqPersonas;
    private JTable tablaDisponibles;
    private DefaultTableModel modeloDisponibles;
    private final ArrayList<Apartamento> apartamentosDisponiblesActuales = new ArrayList<>();

    private JLabel lblNombreAptoGaleria;
    private JLabel lblFotoApto;
    private JLabel lblContadorFotosApto;
    private JButton btnFotoAnterior, btnFotoSiguiente;
    private ArrayList<String> fotosAptoSeleccionado = new ArrayList<>();
    private int indiceFotoApto = 0;

    private JTextField txtOcupNombre, txtOcupFechaNac;
    private DefaultListModel<String> modeloListaOcupantes;
    private final ArrayList<Ocupante> ocupantesReservaActual = new ArrayList<>();
    private JTextArea txtCotizacion;

    private JTable tablaMisReservas;
    private DefaultTableModel modeloMisReservas;
    private JTextField txtMontoAbono;
    private JComboBox<MedioPago> cbMedioPago;
    private JTextArea txtDetalleFolio;

    private JTextField txtPerfNombre, txtPerfCorreo, txtPerfTelefono, txtPerfFoto;
    private JPasswordField txtPerfPass;
    private JLabel lblFotoPerfil;

    public PanelHuesped(Alojamiento alojamiento, VentanaPrincipal ventanaPrincipal) {
        this.alojamiento = alojamiento;
        this.ventanaPrincipal = ventanaPrincipal;
        construirInterfaz();
    }

    public void cargarDatosTitular(Titular titular) {
        this.titularActual = titular;
        lblBienvenida.setText("Portal del Huésped — Bienvenido(a), " + titular.getNombreUsuario() + " (Doc: " + titular.getIdUsuario() + ")");

        txtPerfNombre.setText(titular.getNombreUsuario());
        txtPerfCorreo.setText(titular.getCorreoUsuario());
        txtPerfTelefono.setText(titular.getTelefonoUsuario());
        txtPerfPass.setText(titular.getContraseniaUsuario());
        txtPerfFoto.setText(titular.getFotoTitular());

        cargarImagenEnLabel(lblFotoPerfil, titular.getFotoTitular(), 140, 140, "Sin foto");

        reiniciarOcupantesConTitular();
        buscarDisponibles();
        actualizarTablaMisReservas();
    }

    private void reiniciarOcupantesConTitular() {
        ocupantesReservaActual.clear();
        modeloListaOcupantes.clear();
        if (titularActual != null) {
            Ocupante ocTitular = new Ocupante(titularActual.getNombreUsuario(), titularActual.getFechaNacTitular());
            ocupantesReservaActual.add(ocTitular);
            modeloListaOcupantes.addElement(ocTitular.getNombreOcupante() + " (" + ocTitular.getFechaNacOcupante() + ") - Titular");
        }
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel barraSuperior = new JPanel(new BorderLayout());
        lblBienvenida = new JLabel("Portal del Huésped");
        lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 16));
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.addActionListener(e -> ventanaPrincipal.cerrarSesion());

        barraSuperior.add(lblBienvenida, BorderLayout.WEST);
        barraSuperior.add(btnCerrarSesion, BorderLayout.EAST);
        add(barraSuperior, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("1. Consultar Disponibilidad y Reservar", crearTabReservar());
        tabs.addTab("2. Mis Reservas, Pagos y Cancelación", crearTabMisReservas());
        tabs.addTab("3. Mi Perfil", crearTabPerfil());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel crearTabReservar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filtro.setBorder(BorderFactory.createTitledBorder("Búsqueda de Apartamentos Disponibles"));
        txtBusqEntrada = new JTextField(LocalDate.now().plusDays(1).toString(), 10);
        txtBusqSalida = new JTextField(LocalDate.now().plusDays(3).toString(), 10);
        txtBusqPersonas = new JTextField("2", 4);
        JButton btnBuscar = new JButton("Buscar Disponibles");
        btnBuscar.addActionListener(e -> buscarDisponibles());

        filtro.add(new JLabel("Entrada (YYYY-MM-DD):")); filtro.add(txtBusqEntrada);
        filtro.add(new JLabel("Salida (YYYY-MM-DD):")); filtro.add(txtBusqSalida);
        filtro.add(new JLabel("Personas:")); filtro.add(txtBusqPersonas);
        filtro.add(btnBuscar);
        panel.add(filtro, BorderLayout.NORTH);

        String[] cols = {"Código", "Nombre", "Dormitorios", "Cap. Máx", "Características", "Tarifa Base/Pers"};
        modeloDisponibles = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaDisponibles = new JTable(modeloDisponibles);
        tablaDisponibles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaDisponibles.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarGaleriaApartamentoSeleccionado();
            }
        });
        panel.add(new JScrollPane(tablaDisponibles), BorderLayout.CENTER);

        JPanel inferior = new JPanel(new BorderLayout(10, 10));
        inferior.setPreferredSize(new Dimension(0, 290));

        JPanel panelOcupantes = new JPanel(new BorderLayout(5, 5));
        panelOcupantes.setBorder(BorderFactory.createTitledBorder("Ocupantes (< 6 años gratis, ocupan cupo)"));
        panelOcupantes.setPreferredSize(new Dimension(330, 0));

        JPanel formOcup = new JPanel(new GridLayout(3, 2, 5, 5));
        txtOcupNombre = new JTextField();
        txtOcupFechaNac = new JTextField("2022-04-10");
        JButton btnAddOcup = new JButton("Agregar Ocupante");
        btnAddOcup.addActionListener(e -> agregarOcupante());
        JButton btnResetOcup = new JButton("Reiniciar Lista");
        btnResetOcup.addActionListener(e -> reiniciarOcupantesConTitular());

        formOcup.add(new JLabel("Nombre:")); formOcup.add(txtOcupNombre);
        formOcup.add(new JLabel("Fecha Nac.:")); formOcup.add(txtOcupFechaNac);
        formOcup.add(btnAddOcup); formOcup.add(btnResetOcup);

        modeloListaOcupantes = new DefaultListModel<>();
        panelOcupantes.add(formOcup, BorderLayout.NORTH);
        panelOcupantes.add(new JScrollPane(new JList<>(modeloListaOcupantes)), BorderLayout.CENTER);

        JPanel panelCotizar = new JPanel(new BorderLayout(5, 5));
        panelCotizar.setBorder(BorderFactory.createTitledBorder("Cotización Noche por Noche y Confirmación"));
        txtCotizacion = new JTextArea();
        txtCotizacion.setEditable(false);
        txtCotizacion.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JPanel botonesAccion = new JPanel(new GridLayout(1, 2, 8, 8));
        JButton btnCotizar = new JButton("Cotizar Seleccionado");
        btnCotizar.addActionListener(e -> cotizarSeleccionado());
        JButton btnReservar = new JButton("Reservar (Canal Portal)");
        btnReservar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnReservar.addActionListener(e -> crearReservaPortal());
        botonesAccion.add(btnCotizar);
        botonesAccion.add(btnReservar);

        panelCotizar.add(new JScrollPane(txtCotizacion), BorderLayout.CENTER);
        panelCotizar.add(botonesAccion, BorderLayout.SOUTH);

        JPanel panelGaleria = new JPanel(new BorderLayout(5, 5));
        panelGaleria.setBorder(BorderFactory.createTitledBorder("Fotos del Apartamento"));
        panelGaleria.setPreferredSize(new Dimension(300, 0));

        lblNombreAptoGaleria = new JLabel("Ningún apartamento seleccionado", SwingConstants.CENTER);
        lblNombreAptoGaleria.setFont(new Font("SansSerif", Font.BOLD, 12));

        lblFotoApto = new JLabel("Seleccione un apartamento", SwingConstants.CENTER);
        lblFotoApto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        lblContadorFotosApto = new JLabel("0 / 0", SwingConstants.CENTER);

        JPanel controlesGaleria = new JPanel(new BorderLayout(5, 0));
        btnFotoAnterior = new JButton("< Anterior");
        btnFotoSiguiente = new JButton("Siguiente >");
        btnFotoAnterior.setEnabled(false);
        btnFotoSiguiente.setEnabled(false);

        btnFotoAnterior.addActionListener(e -> cambiarFotoApartamento(-1));
        btnFotoSiguiente.addActionListener(e -> cambiarFotoApartamento(1));

        controlesGaleria.add(btnFotoAnterior, BorderLayout.WEST);
        controlesGaleria.add(lblContadorFotosApto, BorderLayout.CENTER);
        controlesGaleria.add(btnFotoSiguiente, BorderLayout.EAST);

        panelGaleria.add(lblNombreAptoGaleria, BorderLayout.NORTH);
        panelGaleria.add(lblFotoApto, BorderLayout.CENTER);
        panelGaleria.add(controlesGaleria, BorderLayout.SOUTH);

        inferior.add(panelOcupantes, BorderLayout.WEST);
        inferior.add(panelCotizar, BorderLayout.CENTER);
        inferior.add(panelGaleria, BorderLayout.EAST);
        panel.add(inferior, BorderLayout.SOUTH);

        return panel;
    }

    private void cargarGaleriaApartamentoSeleccionado() {
        int fila = tablaDisponibles.getSelectedRow();
        if (fila < 0 || fila >= apartamentosDisponiblesActuales.size()) {
            fotosAptoSeleccionado.clear();
            indiceFotoApto = 0;
            lblNombreAptoGaleria.setText("Ningún apartamento seleccionado");
            lblFotoApto.setIcon(null);
            lblFotoApto.setText("Seleccione un apartamento");
            lblContadorFotosApto.setText("0 / 0");
            btnFotoAnterior.setEnabled(false);
            btnFotoSiguiente.setEnabled(false);
            return;
        }

        Apartamento apto = apartamentosDisponiblesActuales.get(fila);
        lblNombreAptoGaleria.setText(apto.getNombreApartamento() + " (" + apto.getIdApartamento() + ")");
        fotosAptoSeleccionado = apto.getListFotos();
        indiceFotoApto = 0;
        actualizarVisorFotoApartamento();
    }

    private void cambiarFotoApartamento(int desplazamiento) {
        if (fotosAptoSeleccionado.isEmpty()) return;
        indiceFotoApto = (indiceFotoApto + desplazamiento + fotosAptoSeleccionado.size()) % fotosAptoSeleccionado.size();
        actualizarVisorFotoApartamento();
    }

    private void actualizarVisorFotoApartamento() {
        if (fotosAptoSeleccionado.isEmpty()) {
            lblFotoApto.setIcon(null);
            lblFotoApto.setText("Sin fotos registradas");
            lblContadorFotosApto.setText("0 / 0");
            btnFotoAnterior.setEnabled(false);
            btnFotoSiguiente.setEnabled(false);
            return;
        }

        String ruta = fotosAptoSeleccionado.get(indiceFotoApto);
        cargarImagenEnLabel(lblFotoApto, ruta, 265, 185, "Imagen no encontrada");
        lblContadorFotosApto.setText("Foto " + (indiceFotoApto + 1) + " de " + fotosAptoSeleccionado.size());
        boolean tieneVarias = fotosAptoSeleccionado.size() > 1;
        btnFotoAnterior.setEnabled(tieneVarias);
        btnFotoSiguiente.setEnabled(tieneVarias);
    }

    private JPanel crearTabMisReservas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] cols = {"ID Reserva", "Apartamento", "Entrada", "Salida", "Estado", "Valor Total", "Pagado", "Saldo", "Devuelto"};
        modeloMisReservas = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaMisReservas = new JTable(modeloMisReservas);
        tablaMisReservas.getSelectionModel().addListSelectionListener(e -> mostrarDetalleFolioSeleccionado());
        panel.add(new JScrollPane(tablaMisReservas), BorderLayout.CENTER);

        JPanel sur = new JPanel(new BorderLayout(10, 10));
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        controles.setBorder(BorderFactory.createTitledBorder("Pagos / Anticipo (30%) y Cancelación"));

        txtMontoAbono = new JTextField(10);
        cbMedioPago = new JComboBox<>(MedioPago.values());
        JButton btnPagar = new JButton("Registrar Abono");
        btnPagar.addActionListener(e -> abonarReservaSeleccionada());

        JButton btnCancelar = new JButton("Cancelar Reserva");
        btnCancelar.addActionListener(e -> cancelarReservaSeleccionada());

        controles.add(new JLabel("Monto ($ COP):")); controles.add(txtMontoAbono);
        controles.add(new JLabel("Medio:")); controles.add(cbMedioPago);
        controles.add(btnPagar);
        controles.add(btnCancelar);

        txtDetalleFolio = new JTextArea(8, 40);
        txtDetalleFolio.setEditable(false);
        txtDetalleFolio.setFont(new Font("Monospaced", Font.PLAIN, 12));

        sur.add(controles, BorderLayout.NORTH);
        sur.add(new JScrollPane(txtDetalleFolio), BorderLayout.CENTER);
        panel.add(sur, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearTabPerfil() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblFotoPerfil = new JLabel("Sin foto", SwingConstants.CENTER);
        lblFotoPerfil.setPreferredSize(new Dimension(170, 170));
        lblFotoPerfil.setBorder(BorderFactory.createTitledBorder("Foto de Perfil"));

        JPanel panelFoto = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelFoto.add(lblFotoPerfil);

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Actualizar Datos Personales"));

        txtPerfNombre = new JTextField();
        txtPerfCorreo = new JTextField();
        txtPerfTelefono = new JTextField();
        txtPerfPass = new JPasswordField();

        JPanel panelRutaFoto = new JPanel(new BorderLayout(5, 0));
        txtPerfFoto = new JTextField();
        JButton btnExaminarFoto = new JButton("Examinar...");
        btnExaminarFoto.addActionListener(e -> seleccionarFotoPerfil());
        panelRutaFoto.add(txtPerfFoto, BorderLayout.CENTER);
        panelRutaFoto.add(btnExaminarFoto, BorderLayout.EAST);

        form.add(new JLabel("Nombre Completo:")); form.add(txtPerfNombre);
        form.add(new JLabel("Correo:")); form.add(txtPerfCorreo);
        form.add(new JLabel("Teléfono:")); form.add(txtPerfTelefono);
        form.add(new JLabel("Contraseña:")); form.add(txtPerfPass);
        form.add(new JLabel("Fotografía:")); form.add(panelRutaFoto);

        JPanel contenedorSuperior = new JPanel(new BorderLayout(15, 10));
        contenedorSuperior.add(panelFoto, BorderLayout.WEST);
        contenedorSuperior.add(form, BorderLayout.CENTER);

        JButton btnGuardarPerfil = new JButton("Guardar Cambios de Perfil");
        btnGuardarPerfil.addActionListener(e -> {
            titularActual.setNombreUsuario(txtPerfNombre.getText());
            titularActual.setCorreoUsuario(txtPerfCorreo.getText());
            titularActual.setTelefonoUsuario(txtPerfTelefono.getText());
            titularActual.setContraseniaUsuario(new String(txtPerfPass.getPassword()));
            titularActual.setFotoTitular(txtPerfFoto.getText().trim());

            cargarImagenEnLabel(lblFotoPerfil, titularActual.getFotoTitular(), 140, 140, "Sin foto");
            lblBienvenida.setText("Portal del Huésped — Bienvenido(a), " + titularActual.getNombreUsuario() + " (Doc: " + titularActual.getIdUsuario() + ")");
            JOptionPane.showMessageDialog(this, "Perfil actualizado correctamente.");
        });

        panel.add(contenedorSuperior, BorderLayout.NORTH);
        panel.add(btnGuardarPerfil, BorderLayout.SOUTH);
        return panel;
    }

    private void seleccionarFotoPerfil() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar fotografía de perfil");
        chooser.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String ruta = chooser.getSelectedFile().getAbsolutePath();
            txtPerfFoto.setText(ruta);
            cargarImagenEnLabel(lblFotoPerfil, ruta, 140, 140, "Sin foto");
        }
    }

    private void cargarImagenEnLabel(JLabel label, String rutaFoto, int ancho, int alto, String mensajeVacio) {
        if (rutaFoto == null || rutaFoto.isBlank()) {
            label.setIcon(null);
            label.setText(mensajeVacio);
            return;
        }

        ImageIcon icono = null;
        File archivo = new File(rutaFoto);

        if (archivo.exists()) {
            icono = new ImageIcon(archivo.getAbsolutePath());
        } else {
            URL recurso = getClass().getResource("/" + rutaFoto.replaceFirst("^/", ""));
            if (recurso != null) {
                icono = new ImageIcon(recurso);
            }
        }

        if (icono != null && icono.getIconWidth() > 0) {
            Image imgEscalada = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(imgEscalada));
            label.setText("");
        } else {
            label.setIcon(null);
            label.setText("Imagen no encontrada");
        }
    }

    private void buscarDisponibles() {
        try {
            LocalDate entrada = LocalDate.parse(txtBusqEntrada.getText().trim());
            LocalDate salida = LocalDate.parse(txtBusqSalida.getText().trim());
            int personas = Integer.parseInt(txtBusqPersonas.getText().trim());

            modeloDisponibles.setRowCount(0);
            apartamentosDisponiblesActuales.clear();

            for (Apartamento a : alojamiento.buscarApartamentosDisponibles(entrada, salida, personas)) {
                apartamentosDisponiblesActuales.add(a);
                modeloDisponibles.addRow(new Object[]{
                        a.getIdApartamento(), a.getNombreApartamento(), a.getNumeroDormitorios(),
                        a.getCapacidaMaxPersonas(), a.getCaracteristicas(), "$" + a.getTarifaPersona()
                });
            }

            if (!apartamentosDisponiblesActuales.isEmpty()) {
                tablaDisponibles.setRowSelectionInterval(0, 0);
            } else {
                cargarGaleriaApartamentoSeleccionado();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Revise las fechas (YYYY-MM-DD) y cantidad de personas.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarOcupante() {
        try {
            String nombre = txtOcupNombre.getText().trim();
            LocalDate fechaNac = LocalDate.parse(txtOcupFechaNac.getText().trim());
            Ocupante oc = new Ocupante(nombre, fechaNac);
            if (!oc.esValido()) {
                JOptionPane.showMessageDialog(this, "Datos del ocupante inválidos.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            ocupantesReservaActual.add(oc);
            LocalDate fechaEntrada = LocalDate.parse(txtBusqEntrada.getText().trim());
            String nota = oc.pagaTarifa(fechaEntrada) ? "Paga tarifa" : "Menor < 6 años (Gratis)";
            modeloListaOcupantes.addElement(oc.getNombreOcupante() + " (" + fechaNac + ") - " + nota);
            txtOcupNombre.setText("");
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Fecha de nacimiento inválida (use YYYY-MM-DD).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cotizarSeleccionado() {
        int fila = tablaDisponibles.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un apartamento de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            cargarGaleriaApartamentoSeleccionado();
            String codApto = (String) modeloDisponibles.getValueAt(fila, 0);
            LocalDate entrada = LocalDate.parse(txtBusqEntrada.getText().trim());
            LocalDate salida = LocalDate.parse(txtBusqSalida.getText().trim());
            String desglose = alojamiento.cotizarEstancia(codApto, entrada, salida, ocupantesReservaActual);
            txtCotizacion.setText(desglose);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cotizar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearReservaPortal() {
        int fila = tablaDisponibles.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un apartamento disponible.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String codApto = (String) modeloDisponibles.getValueAt(fila, 0);
            LocalDate entrada = LocalDate.parse(txtBusqEntrada.getText().trim());
            LocalDate salida = LocalDate.parse(txtBusqSalida.getText().trim());

            Reserva r = alojamiento.crearReserva(titularActual.getIdUsuario(), codApto,
                    ocupantesReservaActual, entrada, salida, Canal.PORTAL, "");

            JOptionPane.showMessageDialog(this,
                    "Reserva creada en estado PENDIENTE.\n" +
                            "ID Generado: " + r.getIdReserva() + "\n" +
                            "Valor Total: $" + r.getValorTotalCongelado() + " COP\n" +
                            "Anticipo mínimo (30%) para confirmar antes de 24h: $" + r.calcularAnticipoMinimoPortal() + " COP");

            buscarDisponibles();
            actualizarTablaMisReservas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo reservar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTablaMisReservas() {
        modeloMisReservas.setRowCount(0);
        if (titularActual == null) return;
        for (Reserva r : titularActual.getListReservas()) {
            modeloMisReservas.addRow(new Object[]{
                    r.getIdReserva(), r.getApartamento().getIdApartamento(), r.getFechaEntrada(), r.getFechaSalida(),
                    r.getEstadoReserva(), "$" + r.getValorTotalCongelado(),
                    "$" + r.getFolio().calcularTotalPagos(), "$" + r.getFolio().calcularSaldoPendiente(),
                    "$" + r.getValorDevueltoCancelacion()
            });
        }
    }

    private void mostrarDetalleFolioSeleccionado() {
        int fila = tablaMisReservas.getSelectedRow();
        if (fila < 0) return;
        String idReserva = (String) modeloMisReservas.getValueAt(fila, 0);
        Reserva r = alojamiento.buscarReserva(idReserva);
        if (r == null) return;

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String detalle = "Folio: " + r.getFolio().getIdFolio() + " | Estado: " + r.getEstadoReserva() + "\n";
        detalle += "Cargos:\n";
        for (Cargo c : r.getFolio().getListCargos()) {
            detalle += "  - [" + c.getIdCargo() + " | " + c.getFechaCargo().format(fmt) + "] "
                    + c.getConcepto() + ": $" + c.getValorCargo() + "\n";
        }
        detalle += "Pagos:\n";
        for (Pago p : r.getFolio().getListPagos()) {
            detalle += "  - [" + p.idPago() + " | " + p.fechaPago().format(fmt) + "] ["
                    + p.medioPago() + "] $" + p.valorPago() + "\n";
        }
        detalle += "-------------------------------------------------------------------------------\n";
        detalle += "Total Cargos: $" + r.getFolio().calcularTotalCargos()
                + " | Total Pagado: $" + r.getFolio().calcularTotalPagos()
                + " | Saldo Pendiente: $" + r.getFolio().calcularSaldoPendiente() + " COP\n";
        txtDetalleFolio.setText(detalle);
    }

    private void abonarReservaSeleccionada() {
        int fila = tablaMisReservas.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String idReserva = (String) modeloMisReservas.getValueAt(fila, 0);
            Reserva r = alojamiento.buscarReserva(idReserva);
            if (r == null) return;

            if (r.getEstadoReserva() == EstadoReserva.CANCELADA || r.getEstadoReserva() == EstadoReserva.NO_SHOW) {
                JOptionPane.showMessageDialog(this, "No se pueden registrar pagos sobre una reserva cancelada o en No-Show.", "Operación no permitida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int saldoPendiente = r.getFolio().calcularSaldoPendiente();
            if (saldoPendiente <= 0) {
                JOptionPane.showMessageDialog(this, "Esta reserva ya tiene saldo pendiente en $0. No se pueden agregar más pagos.", "Saldo Saldado", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int monto = Integer.parseInt(txtMontoAbono.getText().trim());
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, "El monto del abono debe ser mayor a $0.", "Monto Inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (monto > saldoPendiente) {
                JOptionPane.showMessageDialog(this, "El monto supera el saldo pendiente actual ($" + saldoPendiente + " COP).", "Monto Excedido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            MedioPago medio = (MedioPago) cbMedioPago.getSelectedItem();

            if (alojamiento.registrarPagoReserva(idReserva, medio, monto)) {
                JOptionPane.showMessageDialog(this, "Pago registrado exitosamente.");
                txtMontoAbono.setText("");
                actualizarTablaMisReservas();
                mostrarDetalleFolioSeleccionado();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar el pago.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto válido en pesos sin decimales.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelarReservaSeleccionada() {
        int fila = tablaMisReservas.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva para cancelar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String idReserva = (String) modeloMisReservas.getValueAt(fila, 0);
        Reserva r = alojamiento.buscarReserva(idReserva);
        if (r == null) return;

        if (r.getEstadoReserva() != EstadoReserva.PENDIENTE && r.getEstadoReserva() != EstadoReserva.CONFIRMADA) {
            JOptionPane.showMessageDialog(this,
                    "Solo puede cancelar reservas en estado PENDIENTE o CONFIRMADA.\nEstado actual: " + r.getEstadoReserva(),
                    "Cancelación no disponible", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int totalPagado = r.getFolio().calcularTotalPagos();
        int devolucion = r.previsualizarDevolucionCancelacion(LocalDate.now());
        int retenido = Math.max(0, totalPagado - devolucion);

        String mensaje = "Reserva: " + r.getIdReserva() + " (Estado: " + r.getEstadoReserva() + ")\n"
                + "Total pagado a la fecha: $" + totalPagado + " COP\n"
                + "Retención según política vigente: $" + retenido + " COP\n"
                + "Cantidad a devolver si cancela hoy: $" + devolucion + " COP\n\n"
                + "¿Desea confirmar la cancelación?";

        int confirm = JOptionPane.showConfirmDialog(this, mensaje,
                "Confirmar Cancelación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (alojamiento.cancelarReserva(idReserva, LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Reserva cancelada. Devolución registrada: $" + devolucion + " COP.");
                actualizarTablaMisReservas();
                mostrarDetalleFolioSeleccionado();
                buscarDisponibles();
            } else {
                JOptionPane.showMessageDialog(this, "Esta reserva no puede cancelarse en su estado actual.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}