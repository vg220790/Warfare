package com.afekawar.bl.base.Entities;

// import java.util.logging.Logger;

import com.afekawar.bl.base.Interface.Time.SystemTime;
import javafx.geometry.Point2D;

public class Missile implements Runnable, Comparable<Missile> {
    /* *************************************************************
     * ******************** Fields and Properties ******************
     * ************************************************************* */
    public enum State { LOADED, INAIR, DEAD }
    private String id;
    private String launcherId;
    private Target target;
    private int launchTime;
    private int flyTime;
    private int damage;
    private SystemTime time;
    private Point2D coordinates;
   // private Logger logger;
    private State state;

    private double distance;
    private double speed;
    private Point2D velocity;


    public Missile(String id,Target target, int launchTime, int flyTime,int damage, String launcherId, SystemTime time){
        this.id = id;
        this.target = target;
        this.launchTime = launchTime;
        this.flyTime = flyTime;
        this.damage = damage;
        setState(State.LOADED);
        this.launcherId = launcherId;
        this.time = time;



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
    State getState() {
        return state;
    }
    public Point2D getCoordinates(){
        return coordinates;
    }
    int getLaunchTime(){
        return launchTime;
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
    }


    @Override
    public void run() {
        distance = Math.sqrt((target.getCoordinates().getY()-coordinates.getY())*(target.getCoordinates().getY()-coordinates.getY()) + (target.getCoordinates().getX()-coordinates.getX())*(target.getCoordinates().getX()-coordinates.getX()) );
        speed = distance / flyTime;

        speed/=60;                             // To be on same scale with framerate ( 60 fps ).

        double angle = Math.atan2(target.getCoordinates().getY() - coordinates.getY(), target.getCoordinates().getX() - coordinates.getX());
        velocity = new Point2D(Math.cos(angle) * speed, Math.sin(angle) * speed);

            state = State.INAIR;
while(state == State.INAIR){
    try{

            Thread.sleep(1000/60);
            update();
            if(time.getExactTime() >= launchTime + flyTime) {
                System.out.println(time.getExactTime());
                setState(State.DEAD);
                System.out.println("Missile " + id + " died at x: " + coordinates.getX() + " y: " + coordinates.getY());
                System.out.println("Target coordinates: x: " + target.getCoordinates().getX() + " y: " + target.getCoordinates().getY());
            }

    } catch (InterruptedException e){
     //   e.printStackTrace();
    }
}


            /*
            System.out.println("Missile n` " + id + " launched towards " + target.getName() + " at " + launchTime + " seconds");
            try {
                synchronized (this) {
                    Thread.sleep(flyTime * 1000);                     // Sleep until missile reaches destination, or being destructed.
                }
            } catch (InterruptedException e) {
                setState(State.DEAD);
                System.out.println("Missile n` " + id + " Died too early....");
            }

            int deathTime = time.getTime();
            System.out.println("Missile n` " + id + " died at " + deathTime + " seconds");
            if (deathTime < launchTime + flyTime) {
                System.out.println("Missile n` " + id + " has been destroyed by Missile Destructor n` ");
            } else {
                System.out.println("Missile n` " + id + " Reached it's destination (" + target.getName() + ") at " + deathTime + " seconds");
            }
        setState(State.DEAD);

*/

    }


    @Override
    public int compareTo(Missile o) {
        if(this.launchTime < o.launchTime)
            return -1;
        else return 1;
    }

}
