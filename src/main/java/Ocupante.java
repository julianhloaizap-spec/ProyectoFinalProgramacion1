import java.time.LocalDate;

public class Ocupante {
    private String nombreOcupante;
    private LocalDate fechaNacOcupante;

    public Ocupante(String nombreOcupante, LocalDate fechaNacOcupante) {
        this.nombreOcupante = nombreOcupante;
        this.fechaNacOcupante = fechaNacOcupante;
    }

    public boolean esValido () {
        return nombreOcupante != null && !nombreOcupante.isBlank()
                && fechaNacOcupante != null;
    }

    public String getNombreOcupante() {
        return nombreOcupante;
    }

    public boolean setNombreOcupante(String nombreOcupante) {
        boolean nombreLimpio = nombreOcupante != null && !nombreOcupante.isBlank();
        if (nombreLimpio) {
            this.nombreOcupante = nombreOcupante.trim();
        }
        return nombreLimpio;
    }

    public LocalDate getFechaNacOcupante() {
        return fechaNacOcupante;
    }

    public boolean setFechaNacOcupante(LocalDate fechaNacOcupante) {
        boolean validado = fechaNacOcupante != null && fechaNacOcupante.isBefore(LocalDate.now());
        if (validado) {this.fechaNacOcupante = fechaNacOcupante;
        }
        return validado;
    }
}
