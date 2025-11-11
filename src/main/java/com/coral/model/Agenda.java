package com.coral.model;
import java.sql.Date;
public class Agenda {
    private int id;
    private Date data;
    private String local;
    private String descricao;
    public Agenda() {}
    public int getId(){return id;}
    public void setId(int id){this.id=id;}
    public Date getData(){return data;}
    public void setData(Date data){this.data=data;}
    public String getLocal(){return local;}
    public void setLocal(String local){this.local=local;}
    public String getDescricao(){return descricao;}
    public void setDescricao(String descricao){this.descricao=descricao;}
    @Override
    public String toString(){
        String d = data!=null ? data.toString() : "(sem data)";
        return id+" - "+d+" - "+(local!=null?local:"");
    }
}
