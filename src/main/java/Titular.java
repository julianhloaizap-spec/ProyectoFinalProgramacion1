import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;

public class Titular extends Usuario {

    private LocalDate fechaNacTitular;
    private String fotoTitular;
    private LinkedList<Ocupante> listOcupantes;

    public Titular(String nombreUsuario, String idUsuario, String correoUsuario, String telefonoUsuario, String contraseniaUsuario, LocalDate fechaNacTitular, String fotoTitular) {
        super(nombreUsuario, idUsuario, correoUsuario, telefonoUsuario, contraseniaUsuario);
        this.fechaNacTitular = fechaNacTitular;
        this.fotoTitular = fotoTitular;
    }

    public LocalDate getFechaNacTitular() {
        return fechaNacTitular;
    }

    public void setFechaNacTitular(LocalDate fechaNacTitular) {
        this.fechaNacTitular = fechaNacTitular;
    }

    public String getFotoTitular() {
        return fotoTitular;
    }

    public void setFotoTitular(String fotoTitular) {
        this.fotoTitular = fotoTitular;
    }
}
