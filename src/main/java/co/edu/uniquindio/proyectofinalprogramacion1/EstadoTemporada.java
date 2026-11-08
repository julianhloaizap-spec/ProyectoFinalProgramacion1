package co.edu.uniquindio.proyectofinalprogramacion1;

public enum EstadoTemporada {
    BAJA(100),
    MEDIA(130),
    ALTA(150);

    /*Decidimos asignar un porcentaje a los estados de temporada para poder asi asignar un  valor al apartamento*/

    private int porcentajeTemporada;

    // constructor
    EstadoTemporada (int porcentajeTemporada){
        this.porcentajeTemporada = porcentajeTemporada;
    }
    // getter and setter
    public int getPorcentajeTemporada() {
        return porcentajeTemporada;
    }
    public void setPorcentajeTemporada(int porcentajeTemporada) {
        this.porcentajeTemporada = porcentajeTemporada;
    }
}
