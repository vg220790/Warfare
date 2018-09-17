package com.afekawar.bl.base.Entities.BaseEntities;

import SharedInterface.WarInterface;
import com.afekawar.bl.base.Interface.Communication.MissileEvent;
import com.afekawar.bl.base.Interface.Communication.MissileEventListener;
import com.afekawar.bl.base.Interface.Communication.WarEvent;
import com.afekawar.bl.base.Interface.Communication.WarEventListener;
import com.afekawar.bl.base.Interface.Time.SystemTime;

import Database.DBDestructionAttempt_Info;
import Database.DBInfo_MissileDestructor;
import Database.DBInfo_MissileLauncher;
import javafx.geometry.Point2D;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MissileDestructor extends WarEntity implements MissileEventListener {
    /* *************************************************************
     * ******************** Fields and Properties ******************
     * ************************************************************* */
    public enum Status {WAITING_MISSILE, PREPARING, DESTROYING}
    private Status status;
    private List<DestMissile> destructdMissile;                          // Used By GSON parser
    private HashMap<WarEntity, TreeSet<Integer>> targetMissiles;
    private int destructLength;                                             // Time takes to destroy a missile.
    private Missile activeDestMissile;                                      // Missile that the destructor currently trying to take down.
    private TreeMap<Integer, Missile> missilesToDestroy;
    private Missile antiMissile;
    private Thread antiMissileThread;
    private DBInfo_MissileDestructor infoRef;


    public MissileDestructor() {                // Needed for GSON parser
        super();
        status = Status.WAITING_MISSILE;
        targetMissiles = new HashMap<>();
        setCoordinates(new Point2D(ThreadLocalRandom.current().nextInt(800, 1100 + 1),ThreadLocalRandom.current().nextInt(200, 660 + 1)));  // Set Random coordinate within Israel Defense Border
        destructLength = 2;
        activeDestMissile = null;
        antiMissile = null;
        missilesToDestroy = new TreeMap<>();
    }

    public MissileDestructor(String id, SystemTime time) {
        super(id,time);
        status = Status.WAITING_MISSILE;
        targetMissiles = new HashMap<>();
        setCoordinates(new Point2D(ThreadLocalRandom.current().nextInt(800, 1100 + 1),ThreadLocalRandom.current().nextInt(200, 660 + 1)));  // Set Random coordinate within Israel Defense Border
        destructLength = 2;
        activeDestMissile = null;
        antiMissile = null;
        missilesToDestroy = new TreeMap<>();
    }

    /* *************************************************************
     * ******************** Getters and Setters ********************
     * ************************************************************* */



    public void addTargetMissile(int destrTime, WarEntity missile) {
        if(targetMissiles.get(missile) == null){
            TreeSet<Integer> temp = new TreeSet<>();
            targetMissiles.put(missile,temp);
        }
        targetMissiles.get(missile).add(destrTime);
    }
    
    public DBInfo_MissileDestructor getInfoRef() {
    	return this.infoRef;
    }

    public void setInfoRef(DBInfo_MissileDestructor infoRef) {
		this.infoRef = infoRef;
	}

    public List<DestMissile> getDestructdMissile(){
        return destructdMissile;
    }

    @Override
    public void init(WarInterface warInterface){
        super.init(warInterface);

        for(DestMissile destMissile : destructdMissile){
            for(Missile missile : warInterface.getAllMissiles()){
                if(missile.getId().equals(destMissile.getId())){
                    if(targetMissiles.get(missile) == null){
                        TreeSet<Integer> temp = new TreeSet<>();
                        targetMissiles.put(missile,temp);
                    }
                    targetMissiles.get(missile).add(destMissile.getDestTime());
                }
            }
        }
    }



    /* *************************************************************
     * ******************** Run Logic ******************************
     * ************************************************************* */
    @Override
    public void run() {
        super.run();
        fireCreateMissileDestructorEvent();

        while(isWarRunning()){
            switch (status) {
                case WAITING_MISSILE:
                    try {
                        Thread.sleep(1000 / 60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(!missilesToDestroy.isEmpty()){
                        status = Status.PREPARING;
                    }


                    break;
                case PREPARING:


                        if(missilesToDestroy.firstKey() == getTime().getTime() + destructLength ) {
                        if(missilesToDestroy.firstEntry().getValue().getState() == Missile.State.INAIR) {
                            activeDestMissile = missilesToDestroy.pollFirstEntry().getValue();
                            status = Status.DESTROYING;
                        }
                        else{
                            missilesToDestroy.pollFirstEntry();
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
                	///////////////////////////////////////////////////
                	////updating data for DB - attempting interception
                	this.infoRef.incrementTotalNumOfInterceptionAttempts();
                	this.infoRef.setCurrentDestructionAttempt(new DBDestructionAttempt_Info(
                			getId(), 
                			"missile destructor", 
                			activeDestMissile.getId(), 
                			"missile"));
                	///////////////////////////////////////////////////
                    try {
                        launchAntiMissile(activeDestMissile.getId());
                        antiMissileThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (activeDestMissile.getState() == Missile.State.INAIR) {
                    targetMissiles.remove(activeDestMissile);
                    activeDestMissile.setState(Missile.State.DEAD);
                    activeDestMissile.stopThread();
                    ///////////////////////////////////////////////
                    ////updating data for DB - hit target missile
                    this.infoRef.incrementNumOfInterceptedMissiles();
                    this.infoRef.updateTotalDamageAvoided(activeDestMissile.getDamage());
                    this.infoRef.getCurrent_destruction_attempt().setDestruction_successful(true);
                    ///////////////////////////////////////////////
                }
                this.infoRef.getCurrent_destruction_attempt().setEnd_time(getTime().getTime());
                this.infoRef.endCurrentDestructionAttempt();
                fireDestroyAntiMissileEvent();
                status = Status.WAITING_MISSILE;
                break;
        }

        }

    }

    private void launchAntiMissile(String id){
        Point2D collisionPoint = new Point2D(activeDestMissile.getCoordinates().getX() + 60*destructLength* activeDestMissile.getVelocity().getX(), activeDestMissile.getCoordinates().getY()+ 60*destructLength* activeDestMissile.getVelocity().getY());

        antiMissile = new Missile("AM " + activeDestMissile.getId(),collisionPoint, getTime().getTime(),destructLength,0,getTime());
        antiMissile.setCoordinates(getCoordinates());
        antiMissile.setTargetMissile(activeDestMissile);
        antiMissileThread = new Thread(antiMissile);
        antiMissileThread.setName(antiMissile.getId());
        antiMissileThread.start();
        antiMissile.setWarEventListeners(getListeners());
        fireLaunchAntiMissileEvent();
        int interception_start_time = getTime().getTime();
        this.infoRef.getCurrent_destruction_attempt().setStart_time(interception_start_time);
        getLogger().info("Missile Destructor n` " + getId() + " Launched anti missile rocket towards Missile n` " + id + " at " + interception_start_time + " seconds..");
    }

    public synchronized void addWarEventListener(WarEventListener listener){
        getListeners().add(listener);
    }

    private synchronized void fireCreateMissileDestructorEvent(){
        WarEvent e = new WarEvent(getId());
        e.setEventType(WarEvent.Event_Type.CREATE_MISSILE_DESTRUCTOR);
        e.setCoordinates(getCoordinates());
        for(WarEventListener listener: getListeners()){
            listener.handleWarEvent(e);
        }
    }

    private synchronized void fireLaunchAntiMissileEvent(){
        WarEvent e = new WarEvent(getId());
        e.setEventType(WarEvent.Event_Type.LAUNCH_ANTI_MISSILE);
        e.setMissileId(antiMissile.getId());
        e.setCoordinates(getCoordinates());
        e.setTargetCoordinates(antiMissile.getTargetCoordinates());
        for (WarEventListener listener : getListeners()){
            listener.handleWarEvent(e);
        }
    }

    private synchronized void fireDestroyAntiMissileEvent(){
        WarEvent e = new WarEvent(getId());
        e.setEventType(WarEvent.Event_Type.DESTROY_ANTI_MISSILE);
        e.setMissileId(antiMissile.getId());
        for (WarEventListener listener : getListeners()){
            listener.handleWarEvent(e);
        }
    }


    @Override
    public synchronized void handleMissileLaunch(MissileEvent e) {
        if(targetMissiles.containsKey(e.getMissile()))
            for(Integer destTime : targetMissiles.get(e.getMissile())){
                missilesToDestroy.put(destTime+getTime().getTime(),e.getMissile());     // Creates pairs of time+missileToDestruct
            }
    }

    public void setMissileToDestroy(Missile missile){
        missilesToDestroy.put(getTime().getTime() + 5, missile);
    }
}

class DestMissile {
    private String id;                                      // Needed for GSON parser
    private String destructAfterLaunch;

    public String getId(){
        return id;
    }
    int getDestTime(){
        return Integer.parseInt(destructAfterLaunch);
    }
}