package com.afekawar.bl.base.Entities;

import com.afekawar.bl.base.Interface.Communication.MissileEvent;
import com.afekawar.bl.base.Interface.Communication.MissileEventListener;
import com.afekawar.bl.base.Interface.Communication.WarEvent;
import com.afekawar.bl.base.Interface.Communication.WarEventListener;
import com.afekawar.bl.base.Interface.Time.SystemTime;
import javafx.geometry.Point2D;
import jdk.nashorn.api.tree.Tree;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
// import java.util.logging.Logger;

public class MissileDestructor implements Runnable, MissileEventListener {
    /* *************************************************************
     * ******************** Fields and Properties ******************
     * ************************************************************* */
    enum Status {WAITING_MISSILE, PREPARING, DESTROYING}
    private Status status;
    private String id;
    private boolean warRunning;
  //  private Logger logger;                      // TODO - Implement Logger
    //private TreeMap<Integer, Missile> targetMissiles; // Will try to destroy target missile if not null TODO - Change data Structure ( Perhaps HashMap<Missile, SortedList<int>> ?? )
    private HashMap<Missile, TreeSet<Integer>> targetMissiles;
    private SystemTime time;
    private Point2D coordinates;
    private int destructLength;           // Time takes to destroy a missile.
    private Missile activeDestMissile;     // Missile that the destructor currently trying to take down.
    private TreeMap<Integer, Missile> missilestoDestroy;
    private Missile antiMissile;
    private Thread antiMissileThread;
    private Set<WarEventListener> listeners;


    public MissileDestructor(String id, SystemTime time) {
        warRunning = true;
        status = Status.WAITING_MISSILE;
        this.id = id;
        targetMissiles = new HashMap<>();
        this.time = time;
        coordinates = new Point2D(ThreadLocalRandom.current().nextInt(800, 1100 + 1),ThreadLocalRandom.current().nextInt(200, 660 + 1));  // Set Random coordinate within Israel Defense Border
        destructLength = 2;
        activeDestMissile = null;
        antiMissile = null;
        listeners = new HashSet<>();
        missilestoDestroy = new TreeMap<>();
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
        if(targetMissiles.get(missile) == null){
            TreeSet<Integer> temp = new TreeSet<>();
            targetMissiles.put(missile,temp);
        }
        targetMissiles.get(missile).add(destrTime);
    }



    /* *************************************************************
     * ******************** Run Logic ******************************
     * ************************************************************* */
    @Override
    public void run() {
        System.out.println("Missile Destructor n` " + id + " Started...");
        fireCreateMissileDestructorEvent();

        while(warRunning){
            switch (status) {
                case WAITING_MISSILE:
                    try {
                        Thread.sleep(1000 / 60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(!missilestoDestroy.isEmpty()){
                        status = Status.PREPARING;
                    }


                    break;
                case PREPARING:


                        if(missilestoDestroy.firstKey() == time.getTime() + destructLength ) {
                        if(missilestoDestroy.firstEntry().getValue().getState() == Missile.State.INAIR) {
                            activeDestMissile = missilestoDestroy.pollFirstEntry().getValue();
                            status = Status.DESTROYING;
                        }
                        else{
                            missilestoDestroy.pollFirstEntry();
                            status = Status.WAITING_MISSILE;
                        }

                    }
                    else {
                        try {
                            Thread.sleep(1000 / 60);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        }

            break;
            case DESTROYING:
                if (activeDestMissile.getState() == Missile.State.INAIR) {
                    try {
                        launchAntiMissile(activeDestMissile.getId());
                        antiMissileThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (activeDestMissile.getState() == Missile.State.INAIR) {
                    activeDestMissile.setState(Missile.State.DEAD);
                    activeDestMissile.stopThread();



                }
                fireDestroyAntiMissileEvent();
                status = Status.WAITING_MISSILE;
                break;
        }



                    /*
                    if (targetMissiles.containsKey(activeDestMissile)){
                        long waitTime = targetMissiles.get(activeDestMissile).pollFirst();
                    System.out.println("Missile Destructor n` " + id + " awaiting for " + waitTime + " seconds...");
                    try {
                        Thread.sleep(waitTime * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (activeDestMissile.getState() == Missile.State.INAIR) {
                        try {
                            launchAntiMissile(activeDestMissile.getId());
                            antiMissileThread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (activeDestMissile.getState() == Missile.State.INAIR) {
                        activeDestMissile.setState(Missile.State.DEAD);
                        fireDestroyAntiMissileEvent();
                        activeDestMissile.stopThread();
                        status = Status.WAITING_MISSILE;


                    }
            } else {
                        status = Status.WAITING_MISSILE;
                    }

*/


        }


        System.out.println("Missile Destructor n` " + id + " Ended...");

    }

    private void launchAntiMissile(String id){
        Point2D collisionPoint = new Point2D(activeDestMissile.getCoordinates().getX() + 60*destructLength* activeDestMissile.getVelocity().getX(), activeDestMissile.getCoordinates().getY()+ 60*destructLength* activeDestMissile.getVelocity().getY());

        antiMissile = new Missile("AM " + activeDestMissile.getId(),collisionPoint, time.getTime(),destructLength,time);
        antiMissile.setCoordinates(coordinates);
        antiMissile.setTargetMissile(activeDestMissile);
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


    @Override
    public synchronized void handleMissileLaunch(MissileEvent e) {
        if(targetMissiles.containsKey(e.getMissile()))
            for(Integer destTime : targetMissiles.get(e.getMissile())){
                missilestoDestroy.put(destTime+time.getTime(),e.getMissile());
            }
    }
}