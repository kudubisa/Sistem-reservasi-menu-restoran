package org.altbeacon.service;

/**
 * Created by Andrya on 1/12/2016.
 */
public class EntitasVoucher {
    String title = "";
    String text = "";
    String pic = "";
    int minor_id = 0;
    String code = "";

    public void setTitle(String tit){
        this.title = tit;
    }
    public String getTitle(){
        return this.title;
    }
    public void setText(String tex){
        this.text = tex;
    }
    public String getText(){
        return this.text;
    }
    public void setPic(String p){
        this.pic = p;
    }
    public String getPic(){
        return this.pic;
    }
    public void setMinId(int mi){
        this.minor_id = mi;
    }
    public int getMinId(){
        return this.minor_id;
    }
    public void setCode(String co){
        this.code = co;
    }
    public String getCode(){
        return this.code;
    }
}
