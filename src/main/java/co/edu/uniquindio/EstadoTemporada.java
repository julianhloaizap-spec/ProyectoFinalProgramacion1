package co.edu.uniquindio;

public enum EstadoTemporada {
    BAJA(100),
    MEDIA(130),
    ALTA(150);

    /*Decidimos asignar un porcentaje a los estados de temporada para poder asi asignar un  valor al apartamento*/

    private final int porcentajePorDefecto;

    // constructor
    EstadoTemporada(int porcentajePorDefecto) {
        this.porcentajePorDefecto = porcentajePorDefecto;
    }
    //getter
    public int getPorcentajePorDefecto() {
        return porcentajePorDefecto;
    }
}