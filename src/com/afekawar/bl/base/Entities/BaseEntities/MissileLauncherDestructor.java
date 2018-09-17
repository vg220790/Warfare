package com.afekawar.bl.base.Entities.BaseEntities;

import SharedInterface.WarInterface;
import com.afekawar.bl.base.Interface.Communication.WarEvent;
import com.afekawar.bl.base.Interface.Communication.WarEventListener;
import com.afekawar.bl.base.Interface.Time.SystemTime;

import Database.DBDestructionAttempt_Info;
import Database.DBInfo_MissileLauncherDestructor;
import javafx.geometry.Point2D;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class MissileLauncherDestructor extends WarEntity {
    /* *************************************************************
     * ******************** Fields and Properties ******************
     * ************************************************************* */
    private static int idInc = 0;
    public enum Type { AIRCRAFT, BATTLESHIP }
    private Type destType;                                  // Type of launcher destructor
    private String type;
    private int destructLength;           // Time takes to destroy a Missile Launcher.
    private List<DestLauncher> destructedLanucher;                                     // Needed for GSON

    private TreeMap<Integer,WarEntity> targetMissileLaunchers; // Will try to destroy target Missile Launcher if not null


    private DBInfo_MissileLauncherDestructor infoRef;
    
    private MissileLauncher activeDestLauncher;                        // To check which Missile Launcher is destroyed right now
    private Missile antiMissileLauncher;                                // If used to destroy missiles
    private float angle;


    public MissileLauncherDestructor(){                        // Builder for GSON
        super("LD30" + (1 + idInc++));
        targetMissileLaunchers = new TreeMap<>();
        setCoordinates(new Point2D(ThreadLocalRandom.current().nextInt(300, 680 + 1),ThreadLocalRandom.current().nextInt(26, 150 + 1)));  // Set Random coordinate Outside Gaza Strip Border
        destructLength = 2;
        activeDestLauncher = null;
        angle = 0;

    }
    public MissileLauncherDestructor(String destType, SystemTime time){
        super("LD30" + (1 + idInc++),time);
        this.type = destType;
        if(destType.equals("plane")){
            this.destType = Type.AIRCRAFT;
        }
        else{
            this.destType = Type.BATTLESHIP;
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

    public void addDestructedLauncher(int time, WarEntity launcher){
        targetMissileLaunchers.put(time,launcher);
    }
    public Type getType(){
        return destType;
    }
    public String getStrType(){
        return this.type;
    }
    public DBInfo_MissileLauncherDestructor getInfoRef() {
    	return this.infoRef;
    }

    public void setInfoRef(DBInfo_MissileLauncherDestructor infoRef) {
		this.infoRef = infoRef;
	}
    
    public List<DestLauncher> getDestLauncher(){
        return destructedLanucher;
    }

    /* *************************************************************
     * ******************** Run Logic ******************************
     * ************************************************************* */
    @Override
    public void run() {
        super.run();
        fireCreateMissileLauncherDestructorEvent();
        while (isWarRunning()){
            while (!targetMissileLaunchers.keySet().isEmpty()) {
                Iterator<Integer> it = targetMissileLaunchers.keySet().iterator();
                int destructTime = it.next();
                MissileLauncher launcher = (MissileLauncher) targetMissileLaunchers.get(destructTime);
                while (getTime().getTime() < destructTime - destructLength) {
                    try {
                        Thread.sleep(1000 / 60);
                        update();
                    } catch (InterruptedException e) {
                        getLogger().severe(e.getMessage());
                    }
                }
                it.remove();

                if (launcher.getAlive()) {
                    try {
                        activeDestLauncher = launcher;             
                        
                        ///////////////////////////////////////////////////
                        ////updating data for DB - attempting destruction
                        this.infoRef.incrementTotalNumOfDestructionAttempts();
                        this.infoRef.setCurrentDestructionAttempt(new DBDestructionAttempt_Info(
                        		getId(), 
                        		"missile launcher destructor", 
                        		launcher.getId(), 
                        		"missile launcher"));
                        ///////////////////////////////////////////////////
                        
                        // Our trigger to let graphics content to know it should launch a Missile at target launcher.
                        launchAntiMissileLauncher(launcher.getId());
                        while (antiMissileLauncher.getState() == Missile.State.INAIR) {
                            Thread.sleep(1000 / 60);
                            update();
                        }

                        fireDestroyAntiMissileLauncherMissileEvent();

                    } catch (InterruptedException e) {
                        getLogger().severe(e.getMessage());
                    }

                    if (launcher.getAlive() && !launcher.getHidden()) {
                        activeDestLauncher.stopThread();
                        int destruction_attempt_end_time = getTime().getTime();
                        getLogger().info("Launcher Destructor " + getId() + " destroyed Missile Launcher n` " + launcher.getId() + " at " + destruction_attempt_end_time + " seconds.");
                        ///////////////////////////////////////////////
                        ////updating data for DB - hit target launcher
                        this.infoRef.incrementNumOfDestructedLaunchers();
                        this.infoRef.getCurrent_destruction_attempt().setDestruction_successful(true);
                        this.infoRef.getCurrent_destruction_attempt().setEnd_time(destruction_attempt_end_time);
                        ///////////////////////////////////////////////
                        getStatistics().addDestroyedLauncher();
                    }
                    else {
                    	 int destruction_attempt_end_time = getTime().getTime();
                        getLogger().info("Launcher Destructor " + getId() + " failed to destroy Missile Launcher n` " + launcher.getId() + " at " + destruction_attempt_end_time + " seconds.");
                        this.infoRef.getCurrent_destruction_attempt().setEnd_time(destruction_attempt_end_time);
                    }
                    this.infoRef.endCurrentDestructionAttempt();
                }



            }
        try {
            Thread.sleep(1000 / 60);
            update();
        } catch (InterruptedException e) {
            getLogger().severe(e.getMessage());
        }
    }

    }

    @Override
    public void init(WarInterface warInterface){
        super.init(warInterface);
        if(type.equals("plane"))
            this.destType = Type.AIRCRAFT;
        else
            this.destType = Type.BATTLESHIP;

        for(DestLauncher destLauncher : destructedLanucher){
            for(MissileLauncher missileLauncher : warInterface.getMissileLaunchers()){
                if(missileLauncher.getId().equals(destLauncher.getId())){
                    targetMissileLaunchers.put(destLauncher.getDestTime(),missileLauncher);
                }
            }
        }
    }

    @Override
    public void update(){
        super.update();
        if(this.destType == MissileLauncherDestructor.Type.AIRCRAFT){
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
        int destruction_attempt_satrt_time = getTime().getTime();
        this.infoRef.getCurrent_destruction_attempt().setStart_time(destruction_attempt_satrt_time);
        getLogger().info(destType + " n` " + getId() + " Launched anti Launcher rocket towards Launcher n` " + id + " at " + destruction_attempt_satrt_time + " seconds..");
    }

    public synchronized void addWarEventListener(WarEventListener listener){
        getListeners().add(listener);
    }

    private synchronized void fireCreateMissileLauncherDestructorEvent(){
        WarEvent e = new WarEvent(getId());
        e.setEventType(WarEvent.Event_Type.CREATE_MISSILE_LAUNCHER_DESTRUCTOR);
        e.setCoordinates(getCoordinates());
        e.setDestructorType(destType);
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

class DestLauncher{                     // Used for GSON
    private String id;
    private String destructTime;


    public String getId(){
        return id;
    }
    int getDestTime(){
        return Integer.parseInt(destructTime);
    }
}
