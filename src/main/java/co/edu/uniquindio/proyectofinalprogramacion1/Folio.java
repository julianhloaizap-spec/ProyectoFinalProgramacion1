package co.edu.uniquindio.proyectofinalprogramacion1;


import java.time.LocalDate;
import java.util.ArrayList;

public class Folio {
    //atributos
    private String idFolio;
    private ArrayList <ServicioAdicional> listServiciosAdicionales;
    private ArrayList <Penalidad> listPenalidades;
    private ArrayList <Temporada> listTemporada;
    private ArrayList <Pago> listPagos;

    //Constructor
    public Folio(String idFolio) {
        if (idFolio == null){
            this.idFolio = "";
        } else {
            this.idFolio = idFolio.trim();
        }
        this.listServiciosAdicionales = new ArrayList<>();
        this.listPenalidades = new ArrayList<>();
        this.listTemporada = new ArrayList<>();
        this.listPagos = new ArrayList<>();

    }
    // Los datos del constructor se revisan antes de realizar el folio.
    public boolean esValido() {
        return idFolio != null && !idFolio.isBlank();
    }

    //getters and setters
    public String getIdFolio() {
        return idFolio;
    }
    public void setIdFolio(String idFolio) {
        this.idFolio = idFolio;
    }
    public ArrayList<ServicioAdicional> getListServiciosAdicionales() {
        return listServiciosAdicionales;
    }
    public ArrayList<Penalidad> getListPenalidades() {
        return listPenalidades;
    }
    public ArrayList<Temporada> getListTemporada() {
        return listTemporada;
    }
    public ArrayList<Pago> getListPagos() {
        return listPagos;
    }

    //metodos
    public int calcularPrecioAlojamiento (Apartamento apartamento, LocalDate fechaEntrada, LocalDate fechaSalida,ArrayList<Ocupante> listOcupantes, Administrador administrador) {
        int precioAlojamiento;
        return 0;
    }

    public int calcularServiciosAdicionales (){
        return 0;


    }
    public int calcularPenalidades(){
        return 0;

    }
    public int registrarPagos (Pago pago){
        return 0;

    }
    public int calcularTotalCargos(){
        return 0;

    }
    public int calcularTotalPagos(){
        return 0;

    }
    public int calcularSaldoPendiente(){
        return 0;

    }

}
