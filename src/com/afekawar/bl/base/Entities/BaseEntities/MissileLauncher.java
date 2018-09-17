package com.afekawar.bl.base.Entities.BaseEntities;

import SharedInterface.WarInterface;
import com.afekawar.bl.base.Interface.Communication.MissileEvent;
import com.afekawar.bl.base.Interface.Communication.MissileEventListener;
import com.afekawar.bl.base.Interface.Communication.WarEvent;
import com.afekawar.bl.base.Interface.Communication.WarEventListener;
import com.afekawar.bl.base.Interface.Time.SystemTime;

import Database.DBInfo_MissileLauncher;
import Database.DBLaunch_Info;
import javafx.geometry.Point2D;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import com.afekawar.bl.base.Interface.Communication.WarEvent.*;

public class MissileLauncher extends WarEntity {
    /* *************************************************************
     * ******************** Fields and Properties ******************
     * ************************************************************* */
  //  private Logger logger;             // TODO - implement Logger
    private PriorityQueue<Missile> missile;
    private boolean isAlive;
    private boolean isHidden;
    private boolean alwaysVisible;
    private Thread activeMissileThread;
    private Missile activeMissileEntity;
    private Set<MissileEventListener> missileEventListeners;
    private DBInfo_MissileLauncher infoRef;
    int missile_destruction_time;
    

	public MissileLauncher(){                        // For GSON Parser Object creation.
        super();
        missileEventListeners = new HashSet<>();

        int randomNumbersMinX[] = {680,740,610,450};
        int randomNumbersMaxX[] = {760,870,660,620};

        int randomNumbersMinY[] = {140,40,220,400};
        int randomNumbersMaxY[] = {210,125,300,500};

        int index = ThreadLocalRandom.current().nextInt(0,4);


        this.isAlive = true;
        missile = new PriorityQueue<>();
        activeMissileThread = null;
        activeMissileEntity = null;
        setCoordinates(new Point2D(ThreadLocalRandom.current().nextInt(randomNumbersMinX[index], randomNumbersMaxX[index] + 1),ThreadLocalRandom.current().nextInt(randomNumbersMinY[index], randomNumbersMaxY[index] + 1)));  // Set Random coordinate within Gaza Strip

    }

    public MissileLauncher(String id, boolean isHidden, SystemTime time){
        super(id,time);
        missileEventListeners = new HashSet<>();

        int randomNumbersMinX[] = {680,740,610,450};
        int randomNumbersMaxX[] = {760,870,660,620};

        int randomNumbersMinY[] = {140,40,220,400};
        int randomNumbersMaxY[] = {210,125,300,500};

        int index = ThreadLocalRandom.current().nextInt(0,4);

        if(isHidden) {
            this.alwaysVisible = false;
            this.isHidden = true;
        }
        else {
            this.alwaysVisible = true;
            this.isHidden = false;
        }
        this.isAlive = true;
        missile = new PriorityQueue<>();
        activeMissileThread = null;
        activeMissileEntity = null;
        setCoordinates(new Point2D(ThreadLocalRandom.current().nextInt(randomNumbersMinX[index], randomNumbersMaxX[index] + 1),ThreadLocalRandom.current().nextInt(randomNumbersMinY[index], randomNumbersMaxY[index] + 1)));  // Set Random coordinate within Gaza Strip

    }

    /* *************************************************************
     * ******************** Getters and Setters ********************
     * ************************************************************* */

    public boolean getHidden() {
        return isHidden;
    }
    public boolean getAlwaysVisible() {
        return alwaysVisible;
    }
    public boolean getAlive() {
        return isAlive;
    }

    public boolean addMissile(Missile temp){
        return this.missile.offer(temp);
    }
    public DBInfo_MissileLauncher getInfoRef() {
		return infoRef;
	}

	public void setInfoRef(DBInfo_MissileLauncher infoRef) {
		this.infoRef = infoRef;
	}


    @Override
    public void stopThread(){                                                       // Missile launcher destroy func
        this.infoRef.setIsDestroyed(true);
    	super.stopThread();
        if(activeMissileThread != null) {
        	//this.infoRef.incrementNumOfMissilesDestroyedWhenLauncherGotHit();
        	//this.infoRef.updateNumOfInterceptedMissiles();
            activeMissileEntity.stopThread();
        }
        isAlive = false;
        fireDestroyMissileLauncherEvent();
        missile.clear();
    }

    public Queue<Missile> getMissiles(){
        Queue<Missile> temp = new PriorityQueue<>();
        if(activeMissileEntity != null)
            if(activeMissileEntity.getState() == Missile.State.INAIR)
                temp.add(activeMissileEntity);
        temp.addAll(missile);

        return temp;
    }

    @Override
    public void init(WarInterface warInterface){
        super.init(warInterface);

        this.alwaysVisible = !isHidden;



    }





    /* *************************************************************
     * ******************** Run Logic ******************************
     * ************************************************************* */
    @Override
    public void run() {
        super.run();

        fireCreateMissileLauncherEvent();

            while (!missile.isEmpty() || isWarRunning()) {
                if (activeMissileThread != null) {
                    try {
                        activeMissileThread.join();               // Wait for previous missile finish it's work
                        Thread.sleep(20);                   // To let time for graphics to update..
                        fireDestroyMissileEvent();
                        activeMissileThread = null;
                        missile_destruction_time = getTime().getTime();
                        if(activeMissileEntity.getLaunchTime() + activeMissileEntity.getFlyTime() > missile_destruction_time){
                            getLogger().info("Missile n` " + activeMissileEntity.getId() + " Got destroyed at: " + missile_destruction_time + " seconds.");
                            getStatistics().addDestroyedMissile();
                            ///////////////////////////////////////////////
                            ////updating data for DB - destroyed missile
                            this.infoRef.incrementTotalNumOfDestroyedMissiles();
                            this.infoRef.getCurrent_launch().setReached_target(false);
                            this.infoRef.getCurrent_launch().setReal_damage(0);
                            this.infoRef.getCurrent_launch().setEnd_time(missile_destruction_time);
                            this.infoRef.endCurrentLaunch();
                            ///////////////////////////////////////////////
                        }
                        else {
                            getLogger().info("Missile n` " + activeMissileEntity.getId() + " Reached it's destination and caused " + activeMissileEntity.getDamage() + "$ Damage!");
                            getStatistics().addMissileReachedDestination();
                            getStatistics().addDamage(activeMissileEntity.getDamage());
                            ////////////////////////////////////////////////////////////////
                            ////updating data for DB - hit target missile
                            this.infoRef.incrementTotalNumOfHits();
                            this.infoRef.updateTotalDamage(activeMissileEntity.getDamage());
                            this.infoRef.getCurrent_launch().setReached_target(true);
                            this.infoRef.getCurrent_launch().setReal_damage(this.infoRef.getCurrent_launch().getSupposed_damage());
                            this.infoRef.getCurrent_launch().setEnd_time(missile_destruction_time);
                            this.infoRef.endCurrentLaunch();
                            ////////////////////////////////////////////////////////////////
                        }



                    } catch (InterruptedException e) {
                               getLogger().severe(e.getMessage());
                    }
                }
                if (isAlive && !missile.isEmpty()) {
                    Missile m = missile.poll();

                    if (m != null) {
                        int waitTime = m.getLaunchTime() - getTime().getTime();     // Check if the next missile's launch time is later than earlier missile finished it's fly...
                        if (waitTime > 0)
                            try {

                                if (!alwaysVisible) {
                                    isHidden = true;
                                    fireHideMissileLauncherEvent();
                                }


                                getLogger().info("Launcher n` " + getId() + " Waiting " + waitTime + " seconds till next Missile..");
                                Thread.sleep(waitTime * 1000);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        isHidden = false;
                        int launchTime = getTime().getTime();           // Missile's actual launch time might change, if the launcher was busy with another missile.
                        m.setLaunchTime(launchTime);
                        getStatistics().addLaunchedMissile();
                        //////////////////////////////////////////////
                        ////////// updating info for db //////////////
                        this.infoRef.incrementTotalNumOfFiredMissiles();
                        this.infoRef.setCurrentLaunch(new DBLaunch_Info(
                        		this.getId(), 
                        		m.getId(), 
                        		m.getLaunchTime(), 
                        		m.getDestination(), 
                        		m.getDamage()));
                        //////////////////////////////////////////////
                        getLogger().info("Missile n` " + m.getId() + " From Launcher n` " + getId() + " Launched at " + launchTime + " seconds towards " + m.getDestination());
                        Thread missileThread = new Thread(m);
                        missileThread.setName(m.getId());
                        missileThread.start();
                        activeMissileThread = missileThread;                // Keep the missile thread reference.
                        activeMissileEntity = m;
                        activeMissileEntity.setWarEventListeners(getListeners());
                        fireLaunchMissileEvent();
                    }
                }
                try {
                    Thread.sleep(1000/60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            if(isAlive)
                if(activeMissileThread != null)
                    try{
                        activeMissileThread.join();
                        Thread.sleep(20);
                        fireDestroyMissileEvent();
                        missile_destruction_time = getTime().getTime();
                        if(activeMissileEntity.getLaunchTime() + activeMissileEntity.getFlyTime() > missile_destruction_time){
                        	
                            getLogger().info("Missile n` " + activeMissileEntity.getId() + " Got destroyed at: " + missile_destruction_time + " seconds.");
                            getStatistics().addDestroyedMissile();
                            ///////////////////////////////////////////////
                            ////updating data for DB - intercepted missile
                            this.infoRef.incrementTotalNumOfDestroyedMissiles();
                            this.infoRef.getCurrent_launch().setReached_target(false);
                            this.infoRef.getCurrent_launch().setReal_damage(0);
                            this.infoRef.getCurrent_launch().setEnd_time(missile_destruction_time);
                            this.infoRef.endCurrentLaunch();
                            ///////////////////////////////////////////////
                           
                        }
                        else {
                            getLogger().info("Missile n` " + activeMissileEntity.getId() + " Reached it's destination and caused " + activeMissileEntity.getDamage() + "$ Damage!");
                            getStatistics().addMissileReachedDestination();
                            getStatistics().addDamage(activeMissileEntity.getDamage());
                            ////////////////////////////////////////////////////////////////
                            ////updating data for DB - hit target missile
                            this.infoRef.incrementTotalNumOfHits();
                            this.infoRef.updateTotalDamage(activeMissileEntity.getDamage());
                            this.infoRef.getCurrent_launch().setReached_target(true);
                            this.infoRef.getCurrent_launch().setReal_damage(this.infoRef.getCurrent_launch().getSupposed_damage());
                            this.infoRef.getCurrent_launch().setEnd_time(missile_destruction_time);
                            this.infoRef.endCurrentLaunch();
                            ////////////////////////////////////////////////////////////////
                        }
                        activeMissileThread = null;
                    }
                    catch (InterruptedException e){
                        getLogger().severe(e.getMessage());
                    }



        }


        public void setMissileEventListeners(Set<MissileEventListener> missileEventListeners){
            this.missileEventListeners = missileEventListeners;
        }


    private synchronized void fireCreateMissileLauncherEvent(){
            WarEvent e = new WarEvent(getId());
            e.setEventType(Event_Type.CREATE_LAUNCHER);
            e.setCoordinates(getCoordinates());
            e.setHidden(isHidden);
            for(WarEventListener listener: getListeners()){
                listener.handleWarEvent(e);
            }
        }


    private synchronized void fireDestroyMissileLauncherEvent(){
        WarEvent e = new WarEvent(getId());
        e.setEventType(Event_Type.DESTROY_LAUNCHER);
        for(WarEventListener listener : getListeners()){
            listener.handleWarEvent(e);
        }
    }

    private synchronized void fireLaunchMissileEvent(){
            MissileEvent m = new MissileEvent();
            m.setMissile(activeMissileEntity);
            for(MissileEventListener missileEventListener : missileEventListeners){
                missileEventListener.handleMissileLaunch(m);
            }


            WarEvent e = new WarEvent(getId());
            e.setEventType(Event_Type.LAUNCH_MISSILE);
            e.setCoordinates(getCoordinates());
            e.setTargetCoordinates(activeMissileEntity.getTargetCoordinates());
            e.setMissileId(activeMissileEntity.getId());
            for(WarEventListener listener: getListeners()){
                listener.handleWarEvent(e);
            }
        }

    private synchronized void fireDestroyMissileEvent(){
        WarEvent e = new WarEvent(getId());
        e.setEventType(Event_Type.DESTROY_MISSILE);
        e.setMissileId(activeMissileEntity.getId());
            for(WarEventListener listener: getListeners()){
                listener.handleWarEvent(e);
            }
        }

    private synchronized void fireHideMissileLauncherEvent(){
        WarEvent e = new WarEvent(getId());
        e.setEventType(Event_Type.HIDE_LAUNCHER);
        for(WarEventListener listener : getListeners()){
            listener.handleWarEvent(e);
        }
        }






}
