package co.edu.uniquindio.proyectofinalprogramacion1;

public class Apartamento {
    //Atributos
    private String idApartamento;
    private String nombreApartamento;
    private int numeroDormitorios;
    private int capacidaMaxPersonas;
    private String caracteristicas;
    private int tarifaPersona;
    private EstadoOperativo estadoOperativo;

    //Constructor
    public Apartamento(String idApartamento, String nombreApartamento, int numeroDormitorios, int capacidaMaxPersonas, String caracteristicas, int tarifaPersona, EstadoOperativo estadoOperativo) {
        this.idApartamento = idApartamento;
        this.nombreApartamento = nombreApartamento;
        this.numeroDormitorios = numeroDormitorios;
        this.capacidaMaxPersonas = capacidaMaxPersonas;
        this.caracteristicas = caracteristicas;
        this.tarifaPersona = tarifaPersona;
        this.estadoOperativo = estadoOperativo.PREPARADO;
    }
    //Getters and Setters
    public String getIdApartamento() {
        return idApartamento;
    }
    public void setIdApartamento(String idApartamento) {
        this.idApartamento = idApartamento;
    }
    public String getNombreApartamento() {
        return nombreApartamento;
    }
    public void setNombreApartamento(String nombreApartamento) {
        this.nombreApartamento = nombreApartamento;
    }
    public int getNumeroDormitorios() {
        return numeroDormitorios;
    }
    public void setNumeroDormitorios(int numeroDormitorios) {
        this.numeroDormitorios = numeroDormitorios;
    }
    public int getCapacidaMaxPersonas() {
        return capacidaMaxPersonas;
    }
    public void setCapacidaMaxPersonas(int capacidaMaxPersonas) {
        this.capacidaMaxPersonas = capacidaMaxPersonas;
    }
    public String getCaracteristicas() {
        return caracteristicas;
    }
    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }
    public int getTarifaPersona() {
        return tarifaPersona;
    }
    public void setTarifaPersona(int tarifaPersona) {
        this.tarifaPersona = tarifaPersona;
    }
    public EstadoOperativo getEstadoOperativo() {
        return estadoOperativo;
    }
    public void setEstadoOperativo(EstadoOperativo estadoOperativo) {
        this.estadoOperativo = estadoOperativo;
    }
}
