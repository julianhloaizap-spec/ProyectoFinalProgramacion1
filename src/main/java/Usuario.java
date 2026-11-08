public abstract class Usuario {
    protected String nombreUsuario;
    protected final String idUsuario;
    protected String correoUsuario;
    protected String telefonoUsuario;
    protected String contraseniaUsuario;

    public Usuario(String nombreUsuario, String idUsuario, String correoUsuario,
                   String telefonoUsuario, String contraseniaUsuario) {
        if (idUsuario == null) {
            this.idUsuario = "";
        } else {
            this.idUsuario = idUsuario.trim();
        }

        this.nombreUsuario = nombreUsuario;
        this.correoUsuario = correoUsuario;
        this.telefonoUsuario = telefonoUsuario;
        this.contraseniaUsuario = contraseniaUsuario;
    }

        // Se validan los datos del constructor antes de registrar el usuario

        public boolean esValido() {
            return nombreUsuario != null && !nombreUsuario.isBlank()
                    && idUsuario == null && !idUsuario.isBlank()
                    && correoUsuario == null && !correoUsuario.isBlank()
                    && telefonoUsuario == null && !telefonoUsuario.isBlank()
                    && contraseniaUsuario == null && !contraseniaUsuario.isBlank();
        }

        public String getNombreUsuario() {return nombreUsuario;}

        public boolean setNombreUsuario(String nombreUsuario) {
            boolean validado = nombreUsuario != null && !nombreUsuario.isBlank();
            if (validado) {
                this.nombreUsuario= nombreUsuario.trim();
            }
            return validado;
        }

        public String getIdUsuario () {return idUsuario;}

        public String getCorreoUsuario () {return correoUsuario;}

        public boolean setCorreoUsuario(String correoUsuario) {
            boolean validado = correoUsuario != null && !correoUsuario.isBlank();
            if (validado) {
                this.correoUsuario = correoUsuario.trim();
            }
            return validado;
        }
        public boolean setContraseniaUsuario(String contraseniaUsuario) {
            boolean contraseniaNueva = contraseniaUsuario != null && !contraseniaUsuario.isBlank();
            if (contraseniaNueva) {
                this.contraseniaUsuario = contraseniaUsuario.trim();
            }
            return contraseniaNueva;
        }


    }







