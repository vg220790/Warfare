package com.afekawar.bl.base.Entities.BaseEntities;

import javafx.geometry.Point2D;

public class Target{
    private String name;
    private Point2D coordinates;

    Target(String name, int x, int y){

        this.name = name;
        coordinates = new Point2D(x,y);
    }

    String getName(){
        return name;
    }
    public Point2D getCoordinates(){
        return coordinates;
    }

    @Override
    public String toString(){
        return name;
    }
}
