package com.afekawar.bl.base;

import java.util.logging.Logger;

public class Missile implements Runnable {
    /* *************************************************************
     * ******************** Fields and Properties ******************
     * ************************************************************* */
    private String id;
    private Target target;
    private int flyTime;
    private int damage;
    private Logger logger;
    private boolean isAlive;

    /* *************************************************************
     * ******************** Getters and Setters ********************
     * ************************************************************* */
    public int getDamage() {
        return damage;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }
    public int getFlyTime() {

        return flyTime;
    }
    public void setFlyTime(int flyTime) {
        this.flyTime = flyTime;
    }
    public Target getTarget() {

        return target;
    }
    public void setTarget(Target target) {
        this.target = target;
    }
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
