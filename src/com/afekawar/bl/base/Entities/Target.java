package com.afekawar.bl.base.Entities;

public class Target{
    private String name;
    private int x;
    private int y;

    public Target(String name, int x, int y){

        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName(){
        return name;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}
