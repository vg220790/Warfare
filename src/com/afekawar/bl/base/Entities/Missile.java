package com.afekawar.bl.base.Entities;

// import java.util.logging.Logger;

public class Missile implements Runnable, Comparable<Missile> {
    /* *************************************************************
     * ******************** Fields and Properties ******************
     * ************************************************************* */
    public enum State { LOADED, READY, INAIR, DEAD }
    private String id;
    private String launcherId;
    private Target target;
    private int launchTime;
    private int flyTime;
    private int damage;
   // private Logger logger;
    private State state;

    public Missile(String id,Target target, int launchTime, int flyTime,int damage, String launcherId){
        this.id = id;
        this.target = target;
        this.launchTime = launchTime;
        this.flyTime = flyTime;
        this.damage = damage;
        this.state = State.LOADED;
        this.launcherId = launcherId;



    }

    /* *************************************************************
     * ******************** Getters and Setters ********************
     * ************************************************************* */
    public String getId() {
        return id;
    }
    public int getFlyTime() {
        return flyTime;
    }
    String getLauncherId(){
        return launcherId;
    }
    public Target getTarget() {
        return target;
    }
    public State getState() {
        return state;
    }
    /*
    public int getDamage() {
        return damage;
    }
    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
    */
    public void setId(String id) {
        this.id = id;
    }
    public void setLaunchTime(int launchTime){
        this.launchTime = launchTime;
    }
    public void setState(String state) {
        this.state = State.valueOf(state);
    }
    int getLaunchTime(){
        return launchTime;
    }

    /* *************************************************************
     * ******************** Run Logic ******************************
     * ************************************************************* */
    @Override
    public void run() {


            state = State.READY;

            Long startTime = System.nanoTime() / 1000000000;
            System.out.println("Missile n` " + id + " launched towards " + target.getName() + " at " + launchTime + " seconds");
            try {
                synchronized (this) {
                    Thread.sleep(flyTime * 1000);                     // Sleep until missile reaches destination, or being destructed.
                }
            } catch (InterruptedException e) {
                this.state = State.DEAD;
                System.out.println("Missile n` " + id + " Died too early....");
            }

            Long deathTime = (System.nanoTime() / 1000000000 - startTime) + launchTime;
            System.out.println("Missile n` " + id + " died at " + deathTime + " seconds");
            if (deathTime < launchTime + flyTime) {
                System.out.println("Missile n` " + id + " has been destroyed by Missile Destructor n` ");
            } else {
                System.out.println("Missile n` " + id + " Reached it's destination (" + target.getName() + ") at " + deathTime + " seconds");
            }
            this.state = State.DEAD;



    }


    @Override
    public int compareTo(Missile o) {
        if(this.launchTime < o.launchTime)
            return -1;
        else return 1;
    }

    @Override
    public String toString(){
        return "Missile id: " + id + ", LaunchTime: " + launchTime;
    }
}
