package com.afekawar.bl.base.Entities;

import com.afekawar.bl.base.Interface.Communication.WarEvent;
import com.afekawar.bl.base.Interface.Communication.WarEventListener;
import com.afekawar.bl.base.Interface.SystemInterface;
import com.afekawar.bl.base.Interface.Time.SystemTime;
import javafx.geometry.Point2D;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
// import java.util.logging.Logger;


public class MissileLauncherDestructor implements Runnable {
    /* *************************************************************
     * ******************** Fields and Properties ******************
     * ************************************************************* */
    private static int idInc = 0;
    public enum Type { AIRCRAFT, BATTLESHIP }
    private String id;
    private Type type;
    private SystemTime time;
    private int destructLength;           // Time takes to destroy a Missile Launcher.
   // private Logger logger;
    private Set<WarEventListener> listeners;
    private TreeMap<Integer,MissileLauncher> targetMissileLaunchers; // Will try to destroy target Missile Launcher if not null




    private SystemInterface data;
    private Point2D coordinates;
    private MissileLauncher activeDestLauncher;                        // To check which Missile Launcher is destroyed right now
    private Missile antiMissileLauncher;
    private Thread antiMissileLauncherThread;
    private Point2D velocity;
    private float angle;

    public MissileLauncherDestructor(String type, SystemInterface data, SystemTime time){
        this.id = "LD30" + (1 + idInc++);
        if(type.equals("plane")){
            this.type = Type.AIRCRAFT;
        }
        else{
            this.type = Type.BATTLESHIP;
        }
        listeners = new HashSet<>();
        this.time = time;
        targetMissileLaunchers = new TreeMap<>();
        this.data = data;
        coordinates = new Point2D(ThreadLocalRandom.current().nextInt(300, 680 + 1),ThreadLocalRandom.current().nextInt(26, 150 + 1));  // Set Random coordinate Outside Gaza Strip Border
        destructLength = 2;
        activeDestLauncher = null;
        angle = 0;
        velocity = new Point2D(0,0);
    }

    /* *************************************************************
     * ******************** Getters and Setters ********************
     * ************************************************************* */
    /*
    public Logger getLogger() {                                         // TODO - implement Logger..
        return logger;
    }
    public void setLogger(Logger logger) {
        this.logger = logger;
    }
   */

    public String getId() {
        return id;
    }
    public Type getType() {
        return type;
    }
    public Point2D getCoordinates(){
        return coordinates;
    }
    public int getDestructLength(){
        return destructLength;
    }
    public MissileLauncher getActiveDestLauncher(){
        return activeDestLauncher;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setActiveDestLauncher(MissileLauncher activeDestLauncher) {
        this.activeDestLauncher = activeDestLauncher;
    }

    public void addDestructedLauncher(int time, MissileLauncher launcher){
        targetMissileLaunchers.put(time,launcher);
    }

    /* *************************************************************
     * ******************** Run Logic ******************************
     * ************************************************************* */
    @Override
    public void run() {
            System.out.println("Missile Launcher Destructor n` " + id + " Started...");
            fireCreateMissileLauncherDestructorEvent();


            while(!targetMissileLaunchers.keySet().isEmpty()){
                Iterator<Integer> it = targetMissileLaunchers.keySet().iterator();
                int destructTime = it.next();
                MissileLauncher launcher = targetMissileLaunchers.get(destructTime);
               // int waitTime =  destructTime - destructLength - time.getTime();
                while(time.getTime() < destructTime){
                    try {
                        Thread.sleep(1000 / 60);
                        update();
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                it.remove();

                if(launcher.getAlive()){                 // TODO - Move all this part of code to the interface implementation????
                    try {
                        activeDestLauncher = launcher;                             // Our trigger to let graphics content to know it should launch a Missile at target launcher.
                        launchAntiMissileLauncher(launcher.getId());
                        while(antiMissileLauncher.getState() == Missile.State.INAIR){
                            Thread.sleep(1000/60);
                            update();
                        }

                        fireDestroyAntiMissileLauncherMissileEvent();

                    } catch (InterruptedException e) {
                        e.printStackTrace();                         // This part shouldn't be interrupted.
                    }

                    if (launcher.getAlive()) {
                        data.destroyMissileLauncher(launcher.getId());
                    }

                }

            }



            while(true){                                         // TODO - lol
                try {
                    Thread.sleep(1000/60);
                    update();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }

            }



          //  System.out.println("Missile Launcher Destructor n` " + id + " Ended...");         // Remove? After interactive GUI implementation.


    }

    public void update(){
        if(this.type == MissileLauncherDestructor.Type.AIRCRAFT){
            angle += Math.PI/16;
            velocity = new Point2D(Math.cos(angle/60),  Math.sin(angle/60));
        }
        coordinates = coordinates.add(velocity);
        fireUpdateMissileLauncherEvent();
    }

    private void launchAntiMissileLauncher(String id){
        Point2D collisionPoint = new Point2D(activeDestLauncher.getCoordinates().getX(), activeDestLauncher.getCoordinates().getY());

        antiMissileLauncher = new Missile("AML " + activeDestLauncher.getId(),collisionPoint, time.getTime(),destructLength,time);
        antiMissileLauncher.setCoordinates(coordinates);
        antiMissileLauncherThread = new Thread(antiMissileLauncher);
        antiMissileLauncherThread.setName(antiMissileLauncher.getId());
        antiMissileLauncherThread.start();
        antiMissileLauncher.setWarEventListeners(listeners);
        fireLaunchAntiMissileLauncherMissileEvent();
        System.out.println(type + " n` " + this.id + " Launched anti Launcher rocket towards Launcher n` " + id + " at " + time.getTime() + " seconds..");
    }

    public synchronized void addWarEventListener(WarEventListener listener){
        listeners.add(listener);
    }

    private synchronized void fireCreateMissileLauncherDestructorEvent(){
        WarEvent e = new WarEvent(id);
        e.setEventType(WarEvent.Event_Type.CREATE_MISSILE_LAUNCHER_DESTRUCTOR);
        e.setCoordinates(coordinates);
        e.setDestructorType(type);
        for(WarEventListener listener: listeners){
            listener.handleWarEvent(e);
        }
    }

    private synchronized void fireLaunchAntiMissileLauncherMissileEvent(){
        WarEvent e = new WarEvent(id);
        e.setEventType(WarEvent.Event_Type.LAUNCH_ANTI_MISSILE_LAUNCHER);
        e.setMissileId(antiMissileLauncher.getId());
        e.setCoordinates(coordinates);
        e.setTargetCoordinates(activeDestLauncher.getCoordinates());
        for (WarEventListener listener : listeners){
            listener.handleWarEvent(e);
        }
    }


    private synchronized void fireDestroyAntiMissileLauncherMissileEvent(){
        WarEvent e = new WarEvent(id);
        e.setEventType(WarEvent.Event_Type.DESTROY_ANTI_MISSILE_LAUNCHER);
        e.setTargetLauncherId(activeDestLauncher.getId());
        for (WarEventListener listener : listeners){
            listener.handleWarEvent(e);
        }
    }

    private synchronized void fireUpdateMissileLauncherEvent(){
        WarEvent e = new WarEvent(id);
        e.setEventType(WarEvent.Event_Type.UPDATE_COORDINATES);
        e.setCoordinates(coordinates);
        for(WarEventListener listener: listeners){
            listener.handleWarEvent(e);
        }
    }


}
