package com.afekawar.bl.base.Entities;

import com.afekawar.bl.base.Interface.Communication.WarEvent;
import com.afekawar.bl.base.Interface.Communication.WarEventListener;
import com.afekawar.bl.base.Interface.Time.SystemTime;
import javafx.geometry.Point2D;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
// import java.util.logging.Logger;

public class MissileDestructor implements Runnable {
    /* *************************************************************
     * ******************** Fields and Properties ******************
     * ************************************************************* */
    private String id;
  //  private Logger logger;                      // TODO - Implement Logger
    private TreeMap<Integer, Missile> targetMissiles; // Will try to destroy target missile if not null TODO - Change data Structure ( Perhaps HashMap<Missile, SortedList<int>> ?? )
    private SystemTime time;
    private Point2D coordinates;
    private int destructLength;           // Time takes to destroy a missile.
    private Missile activeDestMissile;      // Can attempt to destroy multiple missiles at same time????   // Missile that the destructor currently trying to take down.
    private Missile antiMissile;
    private Thread antiMissileThread;
    private Set<WarEventListener> listeners;


    public MissileDestructor(String id, SystemTime time) {
        this.id = id;
        targetMissiles = new TreeMap<>();
        this.time = time;
        coordinates = new Point2D(ThreadLocalRandom.current().nextInt(800, 1100 + 1),ThreadLocalRandom.current().nextInt(200, 660 + 1));  // Set Random coordinate within Israel Defense Border
        destructLength = 2;
        activeDestMissile = null;
        antiMissile = null;
        listeners = new HashSet<>();
    }

    /* *************************************************************
     * ******************** Getters and Setters ********************
     * ************************************************************* */
    public String getId() {
        return id;
    }
    public Point2D getCoordinates(){
        return coordinates;
    }
    /*
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
    public void addTargetMissile(int destrTime, Missile missile) {
        targetMissiles.put(destrTime, missile);
    }



    /* *************************************************************
     * ******************** Run Logic ******************************
     * ************************************************************* */
    @Override
    public void run() {
        System.out.println("Missile Destructor n` " + id + " Started...");
        fireCreateMissileDestructorEvent();

        for(int destructTime: targetMissiles.keySet()) {
            Missile m = targetMissiles.get(destructTime);
            long waitTime = destructTime - destructLength - time.getTime();
            if (waitTime > 0) {
                System.out.println("Missile Destructor n` " + id + " awaiting for " + waitTime + " seconds...");
                try {
                    Thread.sleep(waitTime * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            if(m.getState() == Missile.State.INAIR){
                try {
                    activeDestMissile = m;
                    launchAntiMissile(m.getId());
                    antiMissileThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (m.getState() == Missile.State.INAIR) {
                    m.setState(Missile.State.DEAD);
                    fireDestroyAntiMissileEvent();
                    activeDestMissile.stopThread();


                }

            }





        }

        System.out.println("Missile Destructor n` " + id + " Ended...");

    }

    private void launchAntiMissile(String id){
        Point2D collisionPoint = new Point2D(activeDestMissile.getCoordinates().getX() + 60*destructLength* activeDestMissile.getVelocity().getX(), activeDestMissile.getCoordinates().getY()+ 60*destructLength* activeDestMissile.getVelocity().getY());

        antiMissile = new Missile("AM " + activeDestMissile.getId(),collisionPoint, time.getTime(),destructLength,time);
        antiMissile.setCoordinates(coordinates);
        antiMissileThread = new Thread(antiMissile);
        antiMissileThread.setName(antiMissile.getId());
        antiMissileThread.start();
        antiMissile.setWarEventListeners(listeners);
        fireLaunchAntiMissileEvent();
        System.out.println("Missile Destructor n` " + this.id + " Launched anti missile rocket towards Missile n` " + id + " at " + time.getTime() + " seconds..");
    }

    public synchronized void addWarEventListener(WarEventListener listener){
        listeners.add(listener);
    }

    private synchronized void fireCreateMissileDestructorEvent(){
        WarEvent e = new WarEvent(id);
        e.setEventType(WarEvent.Event_Type.CREATE_MISSILE_DESTRUCTOR);
        e.setCoordinates(coordinates);
        for(WarEventListener listener: listeners){
            listener.handleWarEvent(e);
        }
    }

    private synchronized void fireLaunchAntiMissileEvent(){
        WarEvent e = new WarEvent(id);
        e.setEventType(WarEvent.Event_Type.LAUNCH_ANTI_MISSILE);
        e.setMissileId(antiMissile.getId());
        e.setCoordinates(coordinates);
        e.setTargetCoordinates(antiMissile.getTargetCoordinates());
        for (WarEventListener listener : listeners){
            listener.handleWarEvent(e);
        }
    }

    private synchronized void fireDestroyAntiMissileEvent(){
        WarEvent e = new WarEvent(id);
        e.setEventType(WarEvent.Event_Type.DESTROY_ANTI_MISSILE);
        e.setMissileId(antiMissile.getId());
        for (WarEventListener listener : listeners){
            listener.handleWarEvent(e);
        }
    }


}