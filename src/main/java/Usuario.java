package co.edu.uniquindio;

import java.util.Objects;

public abstract class Usuario {
    protected String nombreUsuario;
    protected String idUsuario;
    protected String correoUsuario;
    protected String telefonoUsuario;
    protected String contraseniaUsuario;
    protected boolean activo;

    public Usuario(String nombreUsuario, String idUsuario, String correoUsuario,
                   String telefonoUsuario, String contraseniaUsuario) {
        this.nombreUsuario = nombreUsuario != null ? nombreUsuario.trim() : "";
        this.idUsuario = idUsuario != null ? idUsuario.trim() : "";
        this.correoUsuario = correoUsuario != null ? correoUsuario.trim() : "";
        this.telefonoUsuario = telefonoUsuario != null ? telefonoUsuario.trim() : "";
        this.contraseniaUsuario = contraseniaUsuario != null ? contraseniaUsuario.trim() : "";
        this.activo = true;
    }

    public abstract String getRol();

    public boolean esValido() {
        return !nombreUsuario.isBlank() && !idUsuario.isBlank()
                && !correoUsuario.isBlank() && !contraseniaUsuario.isBlank();
    }

    public boolean validarCredenciales(String id, String contrasenia) {
        return activo && id != null && contrasenia != null
                && this.idUsuario.equals(id.trim())
                && this.contraseniaUsuario.equals(contrasenia);
    }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario != null ? nombreUsuario.trim() : ""; }

    public String getIdUsuario() { return idUsuario; }
    public String getCorreoUsuario() { return correoUsuario; }
    public void setCorreoUsuario(String correoUsuario) { this.correoUsuario = correoUsuario != null ? correoUsuario.trim() : ""; }

    public String getTelefonoUsuario() { return telefonoUsuario; }
    public void setTelefonoUsuario(String telefonoUsuario) { this.telefonoUsuario = telefonoUsuario != null ? telefonoUsuario.trim() : ""; }

    public String getContraseniaUsuario() { return contraseniaUsuario; }
    public void setContraseniaUsuario(String contraseniaUsuario) { this.contraseniaUsuario = contraseniaUsuario != null ? contraseniaUsuario.trim() : ""; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Usuario usuario)) return false;
        return Objects.equals(idUsuario, usuario.idUsuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario);
    }
}







