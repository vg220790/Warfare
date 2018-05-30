package com.afekawar.bl.base.Entities;

import com.afekawar.bl.base.Interface.Communication.MissileLauncherDestructorEvent;
import com.afekawar.bl.base.Interface.Communication.MissileLauncherDestructorListener;
import com.afekawar.bl.base.Interface.Communication.WarEvent;
import com.afekawar.bl.base.Interface.Communication.WarEventListener;
import com.afekawar.bl.base.Interface.SystemInterface;
import com.afekawar.bl.base.Interface.Time.SystemTime;
import javafx.geometry.Point2D;

import java.util.HashSet;
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

            for(int destructTime:targetMissileLaunchers.keySet()){                          // TODO - change to fit GUI interactive implementation ( We don't necessary want the thread to stop when there's no more launcher's to destroy)
                MissileLauncher launcher = targetMissileLaunchers.get(destructTime);
                int waitTime = destructTime - destructLength - time.getTime();
                if (waitTime > 0) {
                    System.out.println("Missile Launcher Destructor n` " + id + " awaiting for " + waitTime + " seconds...");
                    try {
                        Thread.sleep(waitTime * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();                    // This part shouldn't be interrupted.
                    }
                }

                if(launcher.getAlive()){                 // TODO - Move all this part of code to the interface implementation????
                    try {
                        activeDestLauncher = launcher;                             // Our trigger to let graphics content to know it should launch a Missile at target launcher.
                        launchAntiMissileLauncher(launcher.getId());
                        Thread.sleep(destructLength * 1000);
                        if(!launcher.getHidden())
                            fireDestroyAntiMissileLauncherMissileEvent();

                    } catch (InterruptedException e) {
                        e.printStackTrace();                         // This part shouldn't be interrupted.
                    }

                    if (launcher.getAlive()) {
                        data.destroyMissileLauncher(launcher.getId());
                    }

                }

            }


            System.out.println("Missile Launcher Destructor n` " + id + " Ended...");         // Remove? After interactive GUI implementation.


    }
    private void launchAntiMissileLauncher(String id){
        System.out.println(type + " n` " + this.id + " Launched anti Launcher rocket towards Launcher n` " + id + " at " + time.getTime() + " seconds..");
        fireLaunchAntiMissileLauncherMissileEvent();
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
        e.setCoordinates(coordinates);
        e.setTargetCoordinates(activeDestLauncher.getCoordinates());
        e.setDestructLength(destructLength);
        for (WarEventListener listener : listeners){
            listener.handleWarEvent(e);
        }
    }


    private synchronized void fireDestroyAntiMissileLauncherMissileEvent(){
        WarEvent e = new WarEvent(id);
        e.setEventType(WarEvent.Event_Type.DESTROY_ANTI_MISSILE_LAUNCHER);
        for (WarEventListener listener : listeners){
            listener.handleWarEvent(e);
        }
    }


}
