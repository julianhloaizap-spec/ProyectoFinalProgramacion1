package co.edu.uniquindio.proyectofinalprogramacion1;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class VentanaPrincipal extends JFrame {

    private final Alojamiento alojamiento;
    private final CardLayout cardLayout;
    private final JPanel contenedorPrincipal;

    private final PanelLogin panelLogin;
    private final PanelHuesped panelHuesped;
    private final PanelRecepcionista panelRecepcionista;
    private final PanelAdministrador panelAdministrador;

    public VentanaPrincipal() {
        this.alojamiento = new Alojamiento("900123456-1", "Aparta-Suites UQ", "Salento, Quindío");
        inicializarDatosPrueba();

        setTitle("SGA — Aparta-Suites UQ (Salento, Quindío)");
        setSize(1150, 740);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.cardLayout = new CardLayout();
        this.contenedorPrincipal = new JPanel(cardLayout);

        this.panelLogin = new PanelLogin(alojamiento, this);
        this.panelHuesped = new PanelHuesped(alojamiento, this);
        this.panelRecepcionista = new PanelRecepcionista(alojamiento, this);
        this.panelAdministrador = new PanelAdministrador(alojamiento, this);

        contenedorPrincipal.add(panelLogin, "LOGIN");
        contenedorPrincipal.add(panelHuesped, "HUESPED");
        contenedorPrincipal.add(panelRecepcionista, "RECEPCIONISTA");
        contenedorPrincipal.add(panelAdministrador, "ADMINISTRADOR");

        add(contenedorPrincipal);
        cardLayout.show(contenedorPrincipal, "LOGIN");
    }

    public void iniciarSesion(Usuario usuario) {
        if (usuario instanceof Titular titular) {
            panelHuesped.cargarDatosTitular(titular);
            cardLayout.show(contenedorPrincipal, "HUESPED");
        } else if (usuario instanceof Recepcionista recepcionista) {
            panelRecepcionista.cargarDatosRecepcionista(recepcionista);
            cardLayout.show(contenedorPrincipal, "RECEPCIONISTA");
        } else if (usuario instanceof Administrador admin) {
            panelAdministrador.cargarDatosAdmin(admin);
            cardLayout.show(contenedorPrincipal, "ADMINISTRADOR");
        }
    }

    public void cerrarSesion() {
        panelLogin.limpiarCampos();
        cardLayout.show(contenedorPrincipal, "LOGIN");
    }

    private void inicializarDatosPrueba() {
        Administrador admin = new Administrador("Laura Gómez (Admin)", "admin", "admin@apartasuitesuq.com", "3101112233", "1234");
        Recepcionista recep = new Recepcionista("Andrés Rojas", "recep", "recepcion@apartasuitesuq.com", "3112223344", "1234");
        Titular titular = new Titular("Carlos Pérez", "1094123456", "carlos@gmail.com", "3001234567", "1234",
                LocalDate.of(1990, 5, 15), "verificacion_carlos.png");

        alojamiento.registrarUsuario(admin);
        alojamiento.registrarUsuario(recep);
        alojamiento.registrarUsuario(titular);

        Apartamento a1 = new Apartamento("Suite Cordillera", 2, 4, "Balcón, Vista a las montañas, WiFi", 100000);
        Apartamento a2 = new Apartamento("Suite Palma de Cera", 1, 2, "Jacuzzi privado, Chimenea", 130000);
        Apartamento a3 = new Apartamento("Suite Cafetal Familiar", 3, 6, "Terraza, Cocina integral, Parqueadero", 90000);

        alojamiento.registrarApartamento(a1);
        alojamiento.registrarApartamento(a2);
        alojamiento.registrarApartamento(a3);

        alojamiento.registrarTemporada(new Temporada("Puente Festivo", EstadoTemporada.MEDIA,
                LocalDate.now().plusDays(5), LocalDate.now().plusDays(8)));
        alojamiento.registrarTemporada(new Temporada("Temporada Alta Vacaciones", EstadoTemporada.ALTA,
                LocalDate.now().plusDays(15), LocalDate.now().plusDays(45)));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new VentanaPrincipal().setVisible(true);
        });
    }
}