package models;

import java.sql.Date;

public class Consulta {

    private Integer id;
    private String fecha;
    private String motivo;
    private String estado;
    private String veterinario;
    private String resultado;
    private Pet pet;

    public Consulta(){

    }

    public Consulta(String fecha, String motivo, String estado, String veterinario, String resultado, Pet pet) {
        this.fecha = fecha;
        this.motivo = motivo;
        this.estado = estado;
        this.veterinario = veterinario;
        this.resultado = resultado;
        this.pet = pet;
    }

    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }


    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }


    public String getEstado() {
        return estado;
    }


    public void setEstado(String estado) {
        this.estado = estado;
    }


    public String getVeterinario() {
        return veterinario;
    }


    public void setVeterinario(String veterinario) {
        this.veterinario = veterinario;
    }


    public String getResultado() {
        return resultado;
    }


    public void setResultado(String resultado) {
        this.resultado = resultado;
    }


    public Pet getPet() {
        return pet;
    }


    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
