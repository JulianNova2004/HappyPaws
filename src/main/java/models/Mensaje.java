package models;

public class Mensaje {

    private Integer id;

    private int usuarioId;

    private int paseadorId;

    private String contenido;

    private boolean esDeUsuario;

    public Mensaje(int usuarioId, int paseadorId, String contenido, boolean esDeUsuario) {
        this.usuarioId = usuarioId;
        this.paseadorId = paseadorId;
        this.contenido = contenido;
        this.esDeUsuario = esDeUsuario;
    }

    public Mensaje(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getPaseadorId() {
        return paseadorId;
    }

    public void setPaseadorId(int paseadorId) {
        this.paseadorId = paseadorId;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public boolean isEsDeUsuario() {
        return esDeUsuario;
    }

    public void setEsDeUsuario(boolean esDeUsuario) {
        this.esDeUsuario = esDeUsuario;
    }
}
