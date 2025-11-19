package com.coral.model;

public class Presenca {
    private int id;
    private int idCorista;
    private int idAgenda;
    private boolean presente;

    public Presenca() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCorista() {
        return idCorista;
    }

    public void setIdCorista(int idCorista) {
        this.idCorista = idCorista;
    }

    public int getIdAgenda() {
        return idAgenda;
    }

    public void setIdAgenda(int idAgenda) {
        this.idAgenda = idAgenda;
    }

    public boolean isPresente() {
        return presente;
    }

    public void setPresente(boolean presente) {
        this.presente = presente;
    }
}
