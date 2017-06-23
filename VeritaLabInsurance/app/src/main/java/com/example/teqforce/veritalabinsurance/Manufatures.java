package com.example.teqforce.veritalabinsurance;

/**
 * Created by vikash.verma on 06/22/2017.
 */

public class Manufatures {
    private int id;
    private String name;

    public Manufatures(){}

    public Manufatures(int id, String name){
        this.id = id;
        this.name = name;
    }
    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }
}
