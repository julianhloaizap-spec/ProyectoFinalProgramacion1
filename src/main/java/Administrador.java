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

    public ArrayList<Recepcionista> getListRecepcionistas() {
        return listRecepcionistas;
    }



    public ArrayList<Apartamento> getListApartamentos() {
        return listApartamentos;
    }

    public void setListApartamentos(ArrayList<Apartamento> listApartamentos) {
        this.listApartamentos = listApartamentos;
    }

    public ArrayList<Temporada> getListTemporadas() {
        return listTemporadas;
    }

    public void setListTemporadas(ArrayList<Temporada> listTemporadas) {
        this.listTemporadas = listTemporadas;
    }
}
