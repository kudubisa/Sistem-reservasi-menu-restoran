package org.menu.balerasa;

/**
 * Created by Andrya on 8/18/2015.
 */
public class EntitasMakanan {
    String idmenu;
    String namamenu = "";
    String hargamenu = "";
    String status = "";
    String deskripsimenu = "";
    String picmenu = "";
    String mobDeal = "";

    public void setIDmenu(String id){
        this.idmenu = id;
    }

    public String getIDmenu(){
        return idmenu;
    }

    public void setNamaMenu(String n){
        this.namamenu = n;
    }

    public String getNamaMenu(){
        return namamenu;
    }

    public void setHargaMenu(String h){
        this.hargamenu = h;
    }

    public String getHargaMenu(){
        return hargamenu;
    }

    public void setStatus(String s){
        this.status = s;
    }

    public String getStatus(){
        return status;
    }

    public void setDeskripsiMenu(String d){
        this.deskripsimenu = d;
    }

    public String getDeskripsimenu(){
        return deskripsimenu;
    }

    public void setPicMenu(String p){
        this.picmenu = p;
    }

    public String getPicMenu(){
        return picmenu;
    }

    public void setMobDeal(String md){
        this.mobDeal = md;
    }

    public String getMobDeal(){
        return this.mobDeal;
    }

}
