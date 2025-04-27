package models;

public class Request {

    private Integer id;

    private Paseador paseador;

    private User usuario;

    //0 enviada, 1 aceptada, -1 rechazada
    private int estado;

    private String contenido;

    public Request(Paseador paseador, User usuario, int estado, String contenido) {
        this.paseador = paseador;
        this.usuario = usuario;
        this.estado = estado;
        this.contenido = contenido;
    }

    public Request() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Paseador getPaseador() {
        return paseador;
    }

    public void setPaseador(Paseador paseador) {
        this.paseador = paseador;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public int getEstado(){
        return estado;
    }

    public void setEstado(int estado){
        this.estado = estado;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

}
