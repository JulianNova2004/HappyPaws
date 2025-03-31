package models;

public class Mensaje {

    private Integer id;

    private int usuarioId;

    private int paseadorId;

    private String content;

    private boolean esDeUsuario;

    public Mensaje(int usuarioId, int paseadorId, String contenido, boolean esDeUsuario) {
        this.usuarioId = usuarioId;
        this.paseadorId = paseadorId;
        this.content = contenido;
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

    public String getContent() {
        return content;
    }

    public void setContent(String contenido) {
        this.content = contenido;
    }

    public boolean isEsDeUsuario() {
        return esDeUsuario;
    }

    public void setEsDeUsuario(boolean esDeUsuario) {
        this.esDeUsuario = esDeUsuario;
    }
}
