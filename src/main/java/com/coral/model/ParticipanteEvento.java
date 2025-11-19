package com.coral.model;

public class ParticipanteEvento {

        private int idAgenda;
        private Integer idCorista;
        private Integer idMusico;

    public void setIdAgenda(int idAgenda) {
        this.idAgenda = idAgenda;
    }

    public void setIdCorista(Integer idCorista) {
        this.idCorista = idCorista;
    }

    public void setIdMusico(Integer idMusico) {
        this.idMusico = idMusico;
    }

    public int getIdAgenda() {
        return idAgenda;
    }

    public Integer getIdCorista() {
        return idCorista;
    }

    public Integer getIdMusico() {
        return idMusico;
    }
}
