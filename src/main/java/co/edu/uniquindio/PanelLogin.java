package co.edu.uniquindio;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PanelLogin extends JPanel {

    private final Alojamiento alojamiento;
    private final VentanaPrincipal ventanaPrincipal;

    private JTextField txtLoginId;
    private JPasswordField txtLoginPass;

    private JTextField txtRegNombre, txtRegDoc, txtRegCorreo, txtRegTelefono, txtRegFechaNac, txtRegFoto;
    private JPasswordField txtRegPass;
    private JLabel lblPreviaFoto;

    public PanelLogin(Alojamiento alojamiento, VentanaPrincipal ventanaPrincipal) {
        this.alojamiento = alojamiento;
        this.ventanaPrincipal = ventanaPrincipal;
        construirInterfaz();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel lblTitulo = new JLabel("SGA — Aparta-Suites UQ (Salento, Quindío)", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(1, 2, 25, 10));
        centro.add(crearPanelInicioSesion());
        centro.add(crearPanelRegistroHuesped());
        add(centro, BorderLayout.CENTER);
    }

    private JPanel crearPanelInicioSesion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Iniciar Sesión (Huésped / Recepción / Admin)"));

        JPanel form = new JPanel(new GridLayout(6, 1, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtLoginId = new JTextField();
        txtLoginPass = new JPasswordField();

        form.add(new JLabel("Usuario o Documento de Identidad:"));
        form.add(txtLoginId);
        form.add(new JLabel("Contraseña:"));
        form.add(txtLoginPass);

        JButton btnIngresar = new JButton("Ingresar al Sistema");
        btnIngresar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnIngresar.addActionListener(e -> procesarLogin());
        form.add(btnIngresar);

        JTextArea infoPrueba = new JTextArea(
                "Usuarios precargados para pruebas:\n" +
                        "• Administrador : usuario = admin       | clave = 1234\n" +
                        "• Recepcionista : usuario = recep       | clave = 1234\n" +
                        "• Huésped       : usuario = 1094123456  | clave = 1234"
        );
        infoPrueba.setEditable(false);
        infoPrueba.setBackground(new Color(245, 245, 245));
        infoPrueba.setBorder(BorderFactory.createTitledBorder("Credenciales de Prueba"));

        panel.add(form, BorderLayout.NORTH);
        panel.add(infoPrueba, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelRegistroHuesped() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Registro de Nuevo Huésped (Titular)"));

        // Vista previa superior de la foto de perfil
        JPanel panelSuperiorFoto = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lblPreviaFoto = new JLabel("Sin foto", SwingConstants.CENTER);
        lblPreviaFoto.setPreferredSize(new Dimension(115, 115));
        lblPreviaFoto.setBorder(BorderFactory.createTitledBorder("Vista Previa"));
        panelSuperiorFoto.add(lblPreviaFoto);

        JPanel form = new JPanel(new GridLayout(7, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));

        txtRegNombre = new JTextField();
        txtRegDoc = new JTextField();
        txtRegFechaNac = new JTextField("1998-08-20");
        txtRegTelefono = new JTextField();
        txtRegCorreo = new JTextField();
        txtRegPass = new JPasswordField();
        txtRegFoto = new JTextField();
        txtRegFoto.setEditable(false);

        JButton btnSeleccionarFoto = new JButton("Adjuntar Foto (Opcional)");
        btnSeleccionarFoto.addActionListener(e -> seleccionarFotoRegistro());

        form.add(new JLabel("Nombre Completo:")); form.add(txtRegNombre);
        form.add(new JLabel("Documento de Identidad:")); form.add(txtRegDoc);
        form.add(new JLabel("Fecha Nacimiento (YYYY-MM-DD):")); form.add(txtRegFechaNac);
        form.add(new JLabel("Teléfono:")); form.add(txtRegTelefono);
        form.add(new JLabel("Correo Electrónico:")); form.add(txtRegCorreo);
        form.add(new JLabel("Contraseña:")); form.add(txtRegPass);
        form.add(btnSeleccionarFoto); form.add(txtRegFoto);

        JButton btnRegistrar = new JButton("Crear Cuenta de Huésped");
        btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnRegistrar.addActionListener(e -> registrarNuevoHuesped());

        panel.add(panelSuperiorFoto, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(btnRegistrar, BorderLayout.SOUTH);
        return panel;
    }

    private void seleccionarFotoRegistro() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar foto de perfil");
        chooser.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String ruta = chooser.getSelectedFile().getAbsolutePath();
            txtRegFoto.setText(ruta);
            actualizarPreviaFoto(ruta);
        }
    }

    private void actualizarPreviaFoto(String ruta) {
        if (ruta == null || ruta.isBlank()) {
            lblPreviaFoto.setIcon(null);
            lblPreviaFoto.setText("Sin foto");
            return;
        }
        File archivo = new File(ruta);
        if (archivo.exists()) {
            ImageIcon icono = new ImageIcon(archivo.getAbsolutePath());
            if (icono.getIconWidth() > 0) {
                Image imgEscalada = icono.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
                lblPreviaFoto.setIcon(new ImageIcon(imgEscalada));
                lblPreviaFoto.setText("");
                return;
            }
        }
        lblPreviaFoto.setIcon(null);
        lblPreviaFoto.setText("No válida");
    }

    private void procesarLogin() {
        String id = txtLoginId.getText().trim();
        String pass = new String(txtLoginPass.getPassword());

        Usuario u = alojamiento.autenticarUsuario(id, pass);
        if (u != null) {
            ventanaPrincipal.iniciarSesion(u);
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas o usuario inactivo.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarNuevoHuesped() {
        try {
            String nombre = txtRegNombre.getText().trim();
            String doc = txtRegDoc.getText().trim();
            LocalDate fechaNac = LocalDate.parse(txtRegFechaNac.getText().trim());
            String tel = txtRegTelefono.getText().trim();
            String correo = txtRegCorreo.getText().trim();
            String pass = new String(txtRegPass.getPassword()).trim();
            String foto = txtRegFoto.getText().trim();

            Titular nuevo = new Titular(nombre, doc, correo, tel, pass, fechaNac, foto);
            if (!nuevo.esValido()) {
                JOptionPane.showMessageDialog(this, "Datos inválidos. Recuerde que el titular debe ser mayor de 18 años y llenar los campos obligatorios.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (alojamiento.registrarUsuario(nuevo)) {
                JOptionPane.showMessageDialog(this, "Cuenta creada con éxito. Ya puede iniciar sesión con su documento.");
                txtRegNombre.setText("");
                txtRegDoc.setText("");
                txtRegTelefono.setText("");
                txtRegCorreo.setText("");
                txtRegPass.setText("");
                txtRegFoto.setText("");
                actualizarPreviaFoto("");
            } else {
                JOptionPane.showMessageDialog(this, "Ya existe un usuario registrado con ese documento.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void limpiarCampos() {
        txtLoginId.setText("");
        txtLoginPass.setText("");
    }
}