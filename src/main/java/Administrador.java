import java.util.ArrayList;

public class Administrador extends Usuario {
    private ArrayList<Recepcionista> listRecepcionistas;
    private ArrayList<Apartamento> listApartamentos;
    private ArrayList<Temporada> listTemporadas;

    // Consructor
    public Administrador(String nombreUsuario, String idUsuario, String correoUsuario, String telefonoUsuario, String contraseniaUsuario,
                         ArrayList<Recepcionista> listRecepcionistas, ArrayList<Apartamento> listApartamentos, ArrayList<Temporada> listTemporadas) {
        super(nombreUsuario, idUsuario, correoUsuario, telefonoUsuario, contraseniaUsuario);
        this.listRecepcionistas = new ArrayList<>();
        this.listApartamentos = new ArrayList<>();
        this.listTemporadas = new ArrayList<>();    }

    @Override
    public boolean esValido(){return super.esValido();}

    public ArrayList<Recepcionista> getListRecepcionistas() { return new ArrayList<>(listRecepcionistas);}

    public ArrayList<Apartamento> getListApartamentos() { return new ArrayList<>(listApartamentos);}

    public ArrayList<Temporada> getListTemporadas() { return new ArrayList<>(listTemporadas);}

}
