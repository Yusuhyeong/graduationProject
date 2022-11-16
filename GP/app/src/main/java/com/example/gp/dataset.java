package com.example.gp;

public class dataset {
    private String imageresourceid;
    private String ts;

    public dataset(String imageresourceid, String t) {
        this.imageresourceid = imageresourceid;
        this.ts = t;
    }

    public String getImageresourceid(){
        return imageresourceid;
    }

    public String getTitle(){
        return ts;
    }

    public void setImageresourceid(String imageresourceid){
        this.imageresourceid = imageresourceid;
    }

    public void setTitle(String title){
        this.ts = title;
    }

}
