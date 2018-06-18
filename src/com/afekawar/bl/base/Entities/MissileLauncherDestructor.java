package com.afekawar.bl.base.Entities;

import com.afekawar.bl.base.Interface.Communication.WarEvent;
import com.afekawar.bl.base.Interface.Communication.WarEventListener;
import com.afekawar.bl.base.Interface.Time.SystemTime;
import javafx.geometry.Point2D;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
// import java.util.logging.Logger;


public class MissileLauncherDestructor extends WarEntity {
    /* *************************************************************
     * ******************** Fields and Properties ******************
     * ************************************************************* */
    private static int idInc = 0;
    public enum Type { AIRCRAFT, BATTLESHIP }
    private Type type;
    private int destructLength;           // Time takes to destroy a Missile Launcher.
   // private Logger logger;
    private TreeMap<Integer,WarEntity> targetMissileLaunchers; // Will try to destroy target Missile Launcher if not null


    private MissileLauncher activeDestLauncher;                        // To check which Missile Launcher is destroyed right now
    private Missile antiMissileLauncher;
    private float angle;

    public MissileLauncherDestructor(String type, SystemTime time){
        super("LD30" + (1 + idInc++),time);
        if(type.equals("plane")){
            this.type = Type.AIRCRAFT;
        }
        else{
            this.type = Type.BATTLESHIP;
        }
        targetMissileLaunchers = new TreeMap<>();

        setCoordinates(new Point2D(ThreadLocalRandom.current().nextInt(300, 680 + 1),ThreadLocalRandom.current().nextInt(26, 150 + 1)));  // Set Random coordinate Outside Gaza Strip Border
        destructLength = 2;
        activeDestLauncher = null;
        angle = 0;

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


    public void addDestructedLauncher(int time, WarEntity launcher){
        targetMissileLaunchers.put(time,launcher);
    }

    /* *************************************************************
     * ******************** Run Logic ******************************
     * ************************************************************* */
    @Override
    public void run() {
            System.out.println("Missile Launcher Destructor n` " + getId() + " Started...");
            fireCreateMissileLauncherDestructorEvent();

        while(isWarRunning()){
            while(!targetMissileLaunchers.keySet().isEmpty()){
                Iterator<Integer> it = targetMissileLaunchers.keySet().iterator();
                int destructTime = it.next();
                MissileLauncher launcher = (MissileLauncher)targetMissileLaunchers.get(destructTime);
                while(getTime().getTime() < destructTime - destructLength){
                    try {
                        Thread.sleep(1000 / 60);
                        update();
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                it.remove();

                if(launcher.getAlive()){
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

                    if (launcher.getAlive() && !launcher.getHidden()) {
                        activeDestLauncher.stopThread();
                    }

                }

            }




                try {
                    Thread.sleep(1000/60);
                    update();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
        }

    }

    @Override
    public void update(){
        super.update();
        if(this.type == MissileLauncherDestructor.Type.AIRCRAFT){
            angle += Math.PI/16;
            setVelocity(new Point2D(Math.cos(angle/60),  Math.sin(angle/60)));
        }
        fireUpdateMissileLauncherEvent();
    }

    private void launchAntiMissileLauncher(String id){
        Point2D collisionPoint = new Point2D(activeDestLauncher.getCoordinates().getX(), activeDestLauncher.getCoordinates().getY());

        antiMissileLauncher = new Missile("AML " + activeDestLauncher.getId(),collisionPoint, getTime().getTime(),destructLength,0,getTime());
        antiMissileLauncher.setCoordinates(getCoordinates());
        antiMissileLauncher.setTargetLauncher(activeDestLauncher);
        Thread antiMissileLauncherThread = new Thread(antiMissileLauncher);
        antiMissileLauncherThread.setName(antiMissileLauncher.getId());
        antiMissileLauncherThread.start();
        antiMissileLauncher.setWarEventListeners(getListeners());
        fireLaunchAntiMissileLauncherMissileEvent();
        System.out.println(type + " n` " + getId() + " Launched anti Launcher rocket towards Launcher n` " + id + " at " + getTime().getTime() + " seconds..");
    }

    public synchronized void addWarEventListener(WarEventListener listener){
        getListeners().add(listener);
    }

    private synchronized void fireCreateMissileLauncherDestructorEvent(){
        WarEvent e = new WarEvent(getId());
        e.setEventType(WarEvent.Event_Type.CREATE_MISSILE_LAUNCHER_DESTRUCTOR);
        e.setCoordinates(getCoordinates());
        e.setDestructorType(type);
        for(WarEventListener listener: getListeners()){
            listener.handleWarEvent(e);
        }
    }

    private synchronized void fireLaunchAntiMissileLauncherMissileEvent(){
        WarEvent e = new WarEvent(getId());
        e.setEventType(WarEvent.Event_Type.LAUNCH_ANTI_MISSILE_LAUNCHER);
        e.setMissileId(antiMissileLauncher.getId());
        e.setCoordinates(getCoordinates());
        e.setTargetCoordinates(activeDestLauncher.getCoordinates());
        for (WarEventListener listener : getListeners()){
            listener.handleWarEvent(e);
        }
    }


    private synchronized void fireDestroyAntiMissileLauncherMissileEvent(){
        WarEvent e = new WarEvent(getId());
        e.setEventType(WarEvent.Event_Type.DESTROY_ANTI_MISSILE_LAUNCHER);
        e.setMissileId(antiMissileLauncher.getId());
        for (WarEventListener listener : getListeners()){
            listener.handleWarEvent(e);
        }
    }

    private synchronized void fireUpdateMissileLauncherEvent(){
        WarEvent e = new WarEvent(getId());
        e.setEventType(WarEvent.Event_Type.UPDATE_COORDINATES);
        e.setCoordinates(getCoordinates());
        for(WarEventListener listener: getListeners()){
            listener.handleWarEvent(e);
        }
    }


}
