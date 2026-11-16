package co.edu.uniquindio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class PanelAdministrador extends JPanel {

    private final Alojamiento alojamiento;
    private final VentanaPrincipal ventanaPrincipal;
    private Administrador adminActual;

    private JLabel lblBienvenida;

    private JTable tablaEmpleados;
    private DefaultTableModel modeloEmpleados;
    private JTextField txtEmpId, txtEmpNombre, txtEmpCorreo, txtEmpTel, txtEmpPass;

    private JTable tablaAptos;
    private DefaultTableModel modeloAptos;
    private JTextField txtAptoNombre, txtAptoDorm, txtAptoCap, txtAptoCaract, txtTarifaBaja, txtTarifaMedia, txtTarifaAlta;

    private JTextField txtTempNombre, txtTempInicio, txtTempFin;
    private JComboBox<EstadoTemporada> cbTipoTemp;
    private JTextField txtBloqApto, txtBloqInicio, txtBloqFin, txtBloqMotivo;
    private JTextField txtPolDiasGratis, txtPolDiasMedia, txtPolRetMedia, txtPolRetCorta;

    private JTextField txtRepInicio, txtRepFin;
    private JTextArea txtAreaReportes;

    public PanelAdministrador(Alojamiento alojamiento, VentanaPrincipal ventanaPrincipal) {
        this.alojamiento = alojamiento;
        this.ventanaPrincipal = ventanaPrincipal;
        construirInterfaz();
    }

    public void cargarDatosAdmin(Administrador admin) {
        this.adminActual = admin;
        lblBienvenida.setText("Panel de Administración — " + admin.getNombreUsuario());
        refrescarTablas();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new BorderLayout());
        lblBienvenida = new JLabel("Panel de Administración");
        lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 16));
        JButton btnSalir = new JButton("Cerrar Sesión");
        btnSalir.addActionListener(e -> ventanaPrincipal.cerrarSesion());
        top.add(lblBienvenida, BorderLayout.WEST);
        top.add(btnSalir, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("1. Recepcionistas", crearTabEmpleados());
        tabs.addTab("2. Apartamentos y Tarifas", crearTabApartamentos());
        tabs.addTab("3. Temporadas, Bloqueos y Política", crearTabConfiguracion());
        tabs.addTab("4. Reportes Gerenciales", crearTabReportes());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel crearTabEmpleados() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] cols = {"Documento/ID", "Nombre", "Correo", "Teléfono", "Activo"};
        modeloEmpleados = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaEmpleados = new JTable(modeloEmpleados);
        panel.add(new JScrollPane(tablaEmpleados), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(4, 4, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("Gestión de Recepcionistas"));

        txtEmpId = new JTextField();
        txtEmpNombre = new JTextField();
        txtEmpCorreo = new JTextField();
        txtEmpTel = new JTextField();
        txtEmpPass = new JTextField("1234");

        JButton btnContratar = new JButton("Contratar");
        btnContratar.addActionListener(e -> {
            Recepcionista r = new Recepcionista(txtEmpNombre.getText(), txtEmpId.getText(), txtEmpCorreo.getText(), txtEmpTel.getText(), txtEmpPass.getText());
            if (adminActual.contratarRecepcionista(alojamiento, r)) {
                JOptionPane.showMessageDialog(this, "Recepcionista contratado.");
                refrescarTablas();
            } else {
                JOptionPane.showMessageDialog(this, "Datos inválidos o ID duplicado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnEditar = new JButton("Editar Datos");
        btnEditar.addActionListener(e -> {
            if (adminActual.editarRecepcionista(alojamiento, txtEmpId.getText(), txtEmpNombre.getText(), txtEmpCorreo.getText(), txtEmpTel.getText(), txtEmpPass.getText())) {
                JOptionPane.showMessageDialog(this, "Datos actualizados.");
                refrescarTablas();
            }
        });

        JButton btnDesvincular = new JButton("Desvincular (Baja Lógica)");
        btnDesvincular.addActionListener(e -> {
            int fila = tablaEmpleados.getSelectedRow();
            String id = fila >= 0 ? (String) modeloEmpleados.getValueAt(fila, 0) : txtEmpId.getText();
            if (adminActual.desvincularRecepcionista(alojamiento, id)) {
                JOptionPane.showMessageDialog(this, "Recepcionista desvinculado.");
                refrescarTablas();
            }
        });

        form.add(new JLabel("ID / Documento:")); form.add(txtEmpId);
        form.add(new JLabel("Nombre:")); form.add(txtEmpNombre);
        form.add(new JLabel("Correo:")); form.add(txtEmpCorreo);
        form.add(new JLabel("Teléfono:")); form.add(txtEmpTel);
        form.add(new JLabel("Contraseña:")); form.add(txtEmpPass);
        form.add(btnContratar); form.add(btnEditar);
        form.add(btnDesvincular);

        panel.add(form, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearTabApartamentos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] cols = {"Código", "Nombre", "Dorm.", "Cap.", "Tarifa Baja", "Tarifa Media", "Tarifa Alta", "Activo"};
        modeloAptos = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaAptos = new JTable(modeloAptos);
        panel.add(new JScrollPane(tablaAptos), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(4, 4, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("Crear / Configurar Apartamentos (Código Automático)"));

        txtAptoNombre = new JTextField();
        txtAptoDorm = new JTextField("2");
        txtAptoCap = new JTextField("4");
        txtAptoCaract = new JTextField("Balcón, Vista a montaña");
        txtTarifaBaja = new JTextField("100000");
        txtTarifaMedia = new JTextField("130000");
        txtTarifaAlta = new JTextField("150000");

        JButton btnCrear = new JButton("Crear Apartamento");
        btnCrear.addActionListener(e -> {
            try {
                Apartamento a = new Apartamento(
                        txtAptoNombre.getText(),
                        Integer.parseInt(txtAptoDorm.getText().trim()),
                        Integer.parseInt(txtAptoCap.getText().trim()),
                        txtAptoCaract.getText(),
                        Integer.parseInt(txtTarifaBaja.getText().trim())
                );
                a.setTarifaTemporada(EstadoTemporada.MEDIA, Integer.parseInt(txtTarifaMedia.getText().trim()));
                a.setTarifaTemporada(EstadoTemporada.ALTA, Integer.parseInt(txtTarifaAlta.getText().trim()));

                if (adminActual.crearApartamento(alojamiento, a)) {
                    JOptionPane.showMessageDialog(this, "Apartamento creado con código: " + a.getIdApartamento());
                    refrescarTablas();
                } else {
                    JOptionPane.showMessageDialog(this, "Datos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Revise los valores numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnEliminar = new JButton("Eliminación Lógica");
        btnEliminar.addActionListener(e -> {
            int fila = tablaAptos.getSelectedRow();
            if (fila >= 0) {
                String cod = (String) modeloAptos.getValueAt(fila, 0);
                if (adminActual.eliminarApartamentoLogico(alojamiento, cod)) {
                    JOptionPane.showMessageDialog(this, "Apartamento desactivado lógicamente.");
                    refrescarTablas();
                } else {
                    JOptionPane.showMessageDialog(this, "No se puede eliminar si tiene reservas activas.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        form.add(new JLabel("Nombre:")); form.add(txtAptoNombre);
        form.add(new JLabel("Dormitorios:")); form.add(txtAptoDorm);
        form.add(new JLabel("Capacidad Máx:")); form.add(txtAptoCap);
        form.add(new JLabel("Características:")); form.add(txtAptoCaract);
        form.add(new JLabel("Tarifa Temp. Baja ($):")); form.add(txtTarifaBaja);
        form.add(new JLabel("Tarifa Temp. Media ($):")); form.add(txtTarifaMedia);
        form.add(new JLabel("Tarifa Temp. Alta ($):")); form.add(txtTarifaAlta);
        form.add(btnCrear); form.add(btnEliminar);

        panel.add(form, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearTabConfiguracion() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

        JPanel pTemp = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pTemp.setBorder(BorderFactory.createTitledBorder("Definir Fechas de Temporada"));
        txtTempNombre = new JTextField("Semana Santa", 12);
        cbTipoTemp = new JComboBox<>(EstadoTemporada.values());
        txtTempInicio = new JTextField(LocalDate.now().plusDays(10).toString(), 9);
        txtTempFin = new JTextField(LocalDate.now().plusDays(14).toString(), 9);
        JButton btnAddTemp = new JButton("Guardar Temporada");
        btnAddTemp.addActionListener(e -> {
            try {
                Temporada t = new Temporada(txtTempNombre.getText(), (EstadoTemporada) cbTipoTemp.getSelectedItem(),
                        LocalDate.parse(txtTempInicio.getText().trim()), LocalDate.parse(txtTempFin.getText().trim()));
                if (alojamiento.registrarTemporada(t)) {
                    JOptionPane.showMessageDialog(this, "Temporada registrada.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Fechas inválidas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        pTemp.add(new JLabel("Nombre:")); pTemp.add(txtTempNombre);
        pTemp.add(new JLabel("Tipo:")); pTemp.add(cbTipoTemp);
        pTemp.add(new JLabel("Inicio:")); pTemp.add(txtTempInicio);
        pTemp.add(new JLabel("Fin:")); pTemp.add(txtTempFin);
        pTemp.add(btnAddTemp);

        JPanel pBloq = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pBloq.setBorder(BorderFactory.createTitledBorder("Registrar Bloqueo de Apartamento"));
        txtBloqApto = new JTextField("APT-001", 8);
        txtBloqInicio = new JTextField(LocalDate.now().plusDays(20).toString(), 9);
        txtBloqFin = new JTextField(LocalDate.now().plusDays(22).toString(), 9);
        txtBloqMotivo = new JTextField("Mantenimiento preventivo", 15);
        JButton btnAddBloq = new JButton("Bloquear Fechas");
        btnAddBloq.addActionListener(e -> {
            try {
                Bloqueo b = new Bloqueo(
                        LocalDate.parse(txtBloqInicio.getText().trim()),
                        LocalDate.parse(txtBloqFin.getText().trim()),
                        txtBloqMotivo.getText());
                if (alojamiento.registrarBloqueoApartamento(txtBloqApto.getText().trim(), b)) {
                    JOptionPane.showMessageDialog(this, "Bloqueo registrado con ID: " + b.getIdBloqueo());
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo bloquear (verifique disponibilidad o datos).", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Datos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        pBloq.add(new JLabel("Apto:")); pBloq.add(txtBloqApto);
        pBloq.add(new JLabel("Inicio:")); pBloq.add(txtBloqInicio);
        pBloq.add(new JLabel("Fin:")); pBloq.add(txtBloqFin);
        pBloq.add(new JLabel("Motivo:")); pBloq.add(txtBloqMotivo);
        pBloq.add(btnAddBloq);

        JPanel pPol = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pPol.setBorder(BorderFactory.createTitledBorder("Configurar Política de Cancelación"));
        txtPolDiasGratis = new JTextField("7", 3);
        txtPolDiasMedia = new JTextField("2", 3);
        txtPolRetMedia = new JTextField("50", 3);
        txtPolRetCorta = new JTextField("100", 3);
        JButton btnGuardarPol = new JButton("Actualizar Política");
        btnGuardarPol.addActionListener(e -> {
            try {
                alojamiento.setPoliticaCancelacion(new PoliticaCancelacion(
                        Integer.parseInt(txtPolDiasGratis.getText().trim()),
                        Integer.parseInt(txtPolDiasMedia.getText().trim()),
                        Integer.parseInt(txtPolRetMedia.getText().trim()),
                        Integer.parseInt(txtPolRetCorta.getText().trim())
                ));
                JOptionPane.showMessageDialog(this, "Política de cancelación actualizada.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valores numéricos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        pPol.add(new JLabel("Días sin penalidad (>):")); pPol.add(txtPolDiasGratis);
        pPol.add(new JLabel("Días pen. media (>=):")); pPol.add(txtPolDiasMedia);
        pPol.add(new JLabel("% Retención media:")); pPol.add(txtPolRetMedia);
        pPol.add(new JLabel("% Retención <48h:")); pPol.add(txtPolRetCorta);
        pPol.add(btnGuardarPol);

        panel.add(pTemp);
        panel.add(pBloq);
        panel.add(pPol);
        return panel;
    }

    private JPanel crearTabReportes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        txtRepInicio = new JTextField(LocalDate.now().minusMonths(1).toString(), 9);
        txtRepFin = new JTextField(LocalDate.now().plusMonths(2).toString(), 9);

        JButton btnOcup = new JButton("Ocupación");
        btnOcup.addActionListener(e -> txtAreaReportes.setText(alojamiento.generarReporteOcupacion()));

        JButton btnIng = new JButton("Ingresos Periodo");
        btnIng.addActionListener(e -> {
            try {
                txtAreaReportes.setText(alojamiento.generarReporteIngresosPeriodo(
                        LocalDate.parse(txtRepInicio.getText().trim()), LocalDate.parse(txtRepFin.getText().trim())));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Fechas inválidas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnCanal = new JButton("Reservas por Canal");
        btnCanal.addActionListener(e -> txtAreaReportes.setText(alojamiento.generarReporteReservasPorCanal()));

        JButton btnSaldos = new JButton("Saldos Pendientes");
        btnSaldos.addActionListener(e -> txtAreaReportes.setText(alojamiento.generarReporteSaldosPendientes()));

        JButton btnNov = new JButton("Aptos con más Novedades");
        btnNov.addActionListener(e -> txtAreaReportes.setText(alojamiento.generarReporteApartamentosConMasNovedades()));

        JButton btnConf = new JButton("Conflictos Canal Externo");
        btnConf.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("=== CONFLICTOS DE CANAL EXTERNO REGISTRADOS ===\n");
            for (ConflictoCanal c : alojamiento.getListConflictosCanal()) {
                sb.append(String.format("- [%s] Cód.Ext: %s | Apto: %s (%s a %s) -> %s\n",
                        c.getIdConflicto(), c.getCodigoExterno(), c.getIdApartamento(),
                        c.getFechaEntrada(), c.getFechaSalida(), c.getMotivo()));
            }
            txtAreaReportes.setText(sb.toString());
        });

        botones.add(new JLabel("Desde:")); botones.add(txtRepInicio);
        botones.add(new JLabel("Hasta:")); botones.add(txtRepFin);
        botones.add(btnOcup);
        botones.add(btnIng);
        botones.add(btnCanal);
        botones.add(btnSaldos);
        botones.add(btnNov);
        botones.add(btnConf);

        txtAreaReportes = new JTextArea();
        txtAreaReportes.setEditable(false);
        txtAreaReportes.setFont(new Font("Monospaced", Font.PLAIN, 13));

        panel.add(botones, BorderLayout.NORTH);
        panel.add(new JScrollPane(txtAreaReportes), BorderLayout.CENTER);
        return panel;
    }

    private void refrescarTablas() {
        modeloEmpleados.setRowCount(0);
        for (Recepcionista r : alojamiento.getListRecepcionistas()) {
            modeloEmpleados.addRow(new Object[]{
                    r.getIdUsuario(), r.getNombreUsuario(), r.getCorreoUsuario(), r.getTelefonoUsuario(), r.isActivo() ? "Sí" : "No"
            });
        }

        modeloAptos.setRowCount(0);
        for (Apartamento a : alojamiento.getListApartamentos()) {
            modeloAptos.addRow(new Object[]{
                    a.getIdApartamento(), a.getNombreApartamento(), a.getNumeroDormitorios(), a.getCapacidaMaxPersonas(),
                    "$" + a.getTarifaPorTemporada(EstadoTemporada.BAJA),
                    "$" + a.getTarifaPorTemporada(EstadoTemporada.MEDIA),
                    "$" + a.getTarifaPorTemporada(EstadoTemporada.ALTA),
                    a.isActivo() ? "Sí" : "No"
            });
        }
    }
}