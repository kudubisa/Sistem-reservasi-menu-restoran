package org.menu.balerasa;

/**
 * Created by Andrya on 8/26/2015.
 */
public class EntitasCart {
    String idMenu;
    String namaMenu = "";
    String qtyMenu = "";
    String jumHrg = "";
    String picMenu = "";

    public void setIdMenu(String id){
        this.idMenu = id;
    }

    public String getIdMenu(){
        return idMenu;
    }

    public void setNamaMenu(String nama){
        this.namaMenu = nama;
    }

    public String getNamaMenu(){
        return namaMenu;
    }

    public void setQtyMenu(String qty){
        this.qtyMenu = qty;
    }

    public String getQtyMenu(){
        return  qtyMenu;
    }

    public void setJumHrg(String jum){
        this.jumHrg = jum;
    }

    public String getJumHrg(){
        return jumHrg;
    }

    public void setPicMenu(String pic){
        this.picMenu = pic;
    }

    public String getPicMenu(){
        return picMenu;
    }

}
