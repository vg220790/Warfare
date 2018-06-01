package com.afekawar.bl.base.Entities;

// import java.util.logging.Logger;

import com.afekawar.bl.base.Interface.Communication.WarEvent;
import com.afekawar.bl.base.Interface.Communication.WarEventListener;
import com.afekawar.bl.base.Interface.Time.SystemTime;
import javafx.geometry.Point2D;

import java.util.HashSet;
import java.util.Set;

public class Missile implements Runnable, Comparable<Missile> {
    /* *************************************************************
     * ******************** Fields and Properties ******************
     * ************************************************************* */
    public enum State { LOADED, INAIR, DEAD }
    private String id;
    private String launcherId;
    private int launchTime;
    private int flyTime;
    private int damage;
    private SystemTime time;
    private Point2D coordinates;
    private Point2D targetCoordinates;
   // private Logger logger;
    private State state;
    private Set<WarEventListener> listeners;
    private Point2D velocity;


    public Missile(String id,Point2D targetCoordinates, int launchTime, int flyTime,int damage, String launcherId, SystemTime time){
        this.id = id;
        this.targetCoordinates = targetCoordinates;
        this.launchTime = launchTime;
        this.flyTime = flyTime;
        this.damage = damage;
        setState(State.LOADED);
        this.launcherId = launcherId;
        this.time = time;
        this.listeners = new HashSet<>();



    }
    Missile(String id,Point2D targetCoordinates, int launchTime, int flyTime, SystemTime time){
        this.id = id;
        this.targetCoordinates = targetCoordinates;
        this.launchTime = launchTime;
        this.flyTime = flyTime;
        setState(State.LOADED);
        this.time = time;
        this.listeners = new HashSet<>();



    }

    /* *************************************************************
     * ******************** Getters and Setters ********************
     * ************************************************************* */
    public String getId() {
        return id;
    }
    Point2D getTargetCoordinates() {
        return targetCoordinates;
    }
    State getState() {
        return state;
    }
    public Point2D getCoordinates(){
        return coordinates;
    }
    int getLaunchTime(){
        return launchTime;
    }

    void stopThread(){                                                       // Missile launcher destroy func
        state = State.DEAD;
        System.out.println("Missile " + id + " died at x: " + coordinates.getX() + " y: " + coordinates.getY());
        System.out.println("Target coordinates: x: " + targetCoordinates.getX() + " y: " + targetCoordinates.getY());

    }

    /*
    public int getDamage() {
        return damage;activeMissileCoordinates
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
    void setLaunchTime(long launchTime){
        this.launchTime = Math.toIntExact(launchTime);
    }
    void setState(State state) {
        this.state = state;
    }
    public void setCoordinates(Point2D coordinates){
        this.coordinates = coordinates;
    }

    /* *************************************************************
     * ******************** Run Logic ******************************
     * ************************************************************* */


    public void update(){
        coordinates = coordinates.add(velocity);
        fireUpdateMissileEvent();
    }


    @Override
    public void run() {
        double distance = Math.sqrt((targetCoordinates.getY()-coordinates.getY())*(targetCoordinates.getY()-coordinates.getY()) + (targetCoordinates.getX()-coordinates.getX())*(targetCoordinates.getX()-coordinates.getX()) );
        double speed = distance / flyTime;

        speed/=60;                             // To be on same scale with framerate ( 60 fps ).

        double angle = Math.atan2(targetCoordinates.getY() - coordinates.getY(), targetCoordinates.getX() - coordinates.getX());
        velocity = new Point2D(Math.cos(angle) * speed, Math.sin(angle) * speed);

            state = State.INAIR;
while(state == State.INAIR){
    try{

            Thread.sleep(1000/60);
            update();
            if(time.getExactTime() >= launchTime + flyTime) {
                System.out.println(time.getExactTime());
                stopThread();
                }

    } catch (InterruptedException e){
        e.printStackTrace();
    }
}


    }

    synchronized void setWarEventListeners(Set<WarEventListener> listeners){
        this.listeners = listeners;
    }

    private synchronized void fireUpdateMissileEvent(){
        WarEvent e = new WarEvent(id);
        e.setEventType(WarEvent.Event_Type.UPDATE_COORDINATES);
        e.setCoordinates(coordinates);
        for(WarEventListener listener: listeners){
            listener.handleWarEvent(e);
        }
    }

    Point2D getVelocity(){
        return velocity;
    }

    @Override
    public int compareTo(Missile o) {
        if(this.launchTime < o.launchTime)
            return -1;
        else return 1;
    }
}
