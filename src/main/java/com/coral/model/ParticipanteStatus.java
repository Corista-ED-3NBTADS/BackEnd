package com.coral.model;

public class ParticipanteStatus {

    private int id;
    private String nome;
    private String tipo;
    private boolean presente;

    public ParticipanteStatus(int id, String nome, String tipo, boolean presente) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.presente = presente;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setPresente(boolean presente) {
        this.presente = presente;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean isPresente() {
        return presente;
    }
}
