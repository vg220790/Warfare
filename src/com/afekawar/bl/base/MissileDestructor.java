package com.afekawar.bl.base;

import java.util.logging.Logger;

public class MissileDestructor implements Runnable {
    /* *************************************************************
     * ******************** Fields and Properties ******************
     * ************************************************************* */
    private String id;
    private Logger logger;
    private Missile targetMissile; // Will try to destroy target missile if not null
    private boolean isAlive;

    /* *************************************************************
     * ******************** Getters and Setters ********************
     * ************************************************************* */
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Logger getLogger() {
        return logger;
    }
    public void setLogger(Logger logger) {
        this.logger = logger;
    }
    public Missile getTargetMissile() {
        return targetMissile;
    }
    public void setTargetMissile(Missile targetMissile) {
        this.targetMissile = targetMissile;
    }
    public boolean isAlive() {
        return isAlive;
    }
    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    /* *************************************************************
     * ******************** Run Logic ******************************
     * ************************************************************* */
    @Override
    public void run() {

    }


}
