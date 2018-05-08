package com.afekawar.bl.base.Entities;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.Logger;


public class MissileLauncherDestructor implements Runnable {
    /* *************************************************************
     * ******************** Fields and Properties ******************
     * ************************************************************* */
    public static int idInc = 0;
    public enum Type { AIRCRAFT, BATTLESHIP }
    private String id;
    private Type type;
    private Logger logger;
    private boolean isAlive;
    private TreeMap<Integer,String> targetMissileLaunchers; // Will try to destroy target missile if not null

    public MissileLauncherDestructor(String type){
        this.id = "LD0" + (1 + idInc++);
        if(type.equals("plane")){
            this.type = Type.AIRCRAFT;
        }
        else{
            this.type = Type.BATTLESHIP;
        }
        isAlive = true;
        targetMissileLaunchers = new TreeMap<>();
    }

    /* *************************************************************
     * ******************** Getters and Setters ********************
     * ************************************************************* */
    public boolean isAlive() {

        return isAlive;
    }
    public void setAlive(boolean alive) {
        isAlive = alive;
    }
    public Logger getLogger() {

        return logger;
    }
    public void setLogger(Logger logger) {
        this.logger = logger;
    }
    public String getId() {

        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Type getType() {
        return type;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public void addDestructedLauncher(int time, String launcherId){
        targetMissileLaunchers.put(time,launcherId);
    }

    /* *************************************************************
     * ******************** Run Logic ******************************
     * ************************************************************* */
    @Override
    public void run() {
        System.out.println("Missile Launcher Destructor n` " + id + " Started...");
    }
}
