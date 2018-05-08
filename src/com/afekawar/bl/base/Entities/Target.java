package com.afekawar.bl.base.Entities;

public class Target implements Runnable{
    private String name;

    public Target(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public void run() {

    }
}
