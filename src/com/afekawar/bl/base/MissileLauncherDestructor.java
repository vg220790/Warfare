package com.afekawar.bl.base;

import java.util.logging.Logger;


public class MissileLauncherDestructor implements Runnable {
    /* *************************************************************
     * ******************** Fields and Properties ******************
     * ************************************************************* */
    public enum Type { AIRCRAFT, BATTLESHIP }
    private String id;
    private Type type;
    private Logger logger;
    private boolean isAlive;
    private MissileLauncher targetMissileLauncher; // Will try to destroy target missile if not null

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

    /* *************************************************************
     * ******************** Run Logic ******************************
     * ************************************************************* */
    @Override
    public void run() {

    }
}
