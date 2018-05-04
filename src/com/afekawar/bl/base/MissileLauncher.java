package com.afekawar.bl.base;

import java.util.Vector;
import java.util.logging.Logger;

public class MissileLauncher implements Runnable {
    /* *************************************************************
     * ******************** Fields and Properties ******************
     * ************************************************************* */
    private String id;
    private Logger logger;
    private Vector<Missile> missiles;
    private boolean isAlive;
    private boolean isHidden;

    /* *************************************************************
     * ******************** Getters and Setters ********************
     * ************************************************************* */
    public boolean isHidden() {
        return isHidden;
    }
    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }
    public boolean isAlive() {

        return isAlive;
    }
    public void setAlive(boolean alive) {
        isAlive = alive;
    }
    public Vector<Missile> getMissiles() {

        return missiles;
    }
    public void setMissiles(Vector<Missile> missiles) {
        this.missiles = missiles;
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

    /* *************************************************************
     * ******************** Run Logic ******************************
     * ************************************************************* */
    @Override
    public void run() {

    }
}
