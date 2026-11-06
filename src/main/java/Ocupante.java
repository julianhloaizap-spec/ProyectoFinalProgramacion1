import java.time.LocalDate;

public class Ocupante {
    private String nombreOcupante;
    private LocalDate fechaNacOcupante;

    public Ocupante(String nombreOcupante, LocalDate fechaNacOcupante) {
        this.nombreOcupante = nombreOcupante;
        this.fechaNacOcupante = fechaNacOcupante;
    }

    public String getNombreOcupante() {
        return nombreOcupante;
    }

    public void setNombreOcupante(String nombreOcupante) {
        this.nombreOcupante = nombreOcupante;
    }

    public LocalDate getFechaNacOcupante() {
        return fechaNacOcupante;
    }

    public void setFechaNacOcupante(LocalDate fechaNacOcupante) {
        this.fechaNacOcupante = fechaNacOcupante;
    }
}
