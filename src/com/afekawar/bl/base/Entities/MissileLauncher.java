package com.afekawar.bl.base.Entities;

import com.afekawar.bl.base.Interface.Communication.MissileEvent;
import com.afekawar.bl.base.Interface.Communication.MissileEventListener;
import com.afekawar.bl.base.Interface.Communication.WarEvent;
import com.afekawar.bl.base.Interface.Communication.WarEventListener;
import com.afekawar.bl.base.Interface.Time.SystemTime;
import javafx.geometry.Point2D;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import com.afekawar.bl.base.Interface.Communication.WarEvent.*;

public class MissileLauncher implements Runnable {
    /* *************************************************************
     * ******************** Fields and Properties ******************
     * ************************************************************* */
    private String id;
  //  private Logger logger;             // TODO - implement Logger
    private Queue<Missile> missiles;
    private boolean isAlive;
    private boolean isHidden;
    private boolean alwaysVisible;
    private Thread activeMissileThread;            // TODO - Implement proper Stop Thread to Missile Class and replace this with Reference to the Runnable Object
    private Missile activeMissileEntity;
    private Point2D coordinates;
    private SystemTime time;
    private Set<WarEventListener> warEventListeners;
    private Set<MissileEventListener> missileEventListeners;

    public MissileLauncher(String id, boolean isHidden, SystemTime time){

        warEventListeners = new HashSet<>();
        missileEventListeners = new HashSet<>();

        int randomNumbersMinX[] = {680,740,610,450};
        int randomNumbersMaxX[] = {760,870,660,620};

        int randomNumbersMinY[] = {140,40,220,400};
        int randomNumbersMaxY[] = {210,125,300,500};

        int index = ThreadLocalRandom.current().nextInt(0,4);

        this.id = id;
        if(isHidden) {
            this.alwaysVisible = false;
            this.isHidden = true;
        }
        else {
            this.alwaysVisible = true;
            this.isHidden = false;
        }
        this.isAlive = true;
        this.time = time;
        missiles = new PriorityQueue<>();
        activeMissileThread = null;
        activeMissileEntity = null;
        coordinates = new Point2D(ThreadLocalRandom.current().nextInt(randomNumbersMinX[index], randomNumbersMaxX[index] + 1),ThreadLocalRandom.current().nextInt(randomNumbersMinY[index], randomNumbersMaxY[index] + 1));  // Set Random coordinate within Gaza Strip

    }

    /* *************************************************************
     * ******************** Getters and Setters ********************
     * ************************************************************* */
    public String getId() {
        return id;
    }
    boolean getHidden() {
        return isHidden;
    }
    boolean getAlive() {
        return isAlive;
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
    public void addMissile(Missile temp){
        this.missiles.offer(temp);
    }



    void stopThread(){                                                       // Missile launcher destroy func
        System.out.println("Missile Launcher n` " + id + " Got destroyed at " + time.getTime() + " seconds");
        if(activeMissileThread != null) {
            activeMissileEntity.stopThread();
        }
        isAlive = false;
        fireDestroyMissileLauncherEvent();
        missiles.clear();
    }






    /* *************************************************************
     * ******************** Run Logic ******************************
     * ************************************************************* */
    @Override
    public void run() {
            System.out.println("Missile Launcher n` " + id + " Started...");

        fireCreateMissileLauncherEvent();
            while (!missiles.isEmpty()) {
                if (activeMissileThread != null) {
                    try {
                        activeMissileThread.join();               // Wait for previous missile finish it's work
                        Thread.sleep(20);                   // To let time for graphics to update..
                        fireDestroyMissileEvent();
                    } catch (InterruptedException e) {
                 //       e.printStackTrace();
                    }
                }
                if (isAlive && !missiles.isEmpty()) {
                    Missile m = missiles.poll();
                    if (m != null){
                        int waitTime = m.getLaunchTime() - time.getTime();     // Check if the next missile's launch time is later than earlier missile finished it's fly...
                    if (waitTime > 0)
                        try {

                        if(!alwaysVisible){
                            isHidden = true;
                            fireHideMissileLauncherEvent();
                        }


                            System.out.println("Launcher n` " + id + " Waiting " + waitTime + " seconds till next Missile..");
                            Thread.sleep(waitTime * 1000);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    isHidden = false;
                    int launchTime = time.getTime();           // Missile's actual launch time might change, if the launcher was busy with another missile.
                    m.setLaunchTime(launchTime);
                    m.setCoordinates(coordinates);
                    System.out.println("Missile n` " + m.getId() + " From Launcher n` " + id + " Launched at " + launchTime + " seconds");
                    Thread missileThread = new Thread(m);
                    missileThread.setName(m.getId());
                    missileThread.start();
                    activeMissileThread = missileThread;                // Keep the missile thread reference.
                    activeMissileEntity = m;
                    m.setWarEventListeners(warEventListeners);
                    fireLaunchMissileEvent();
                }
                }
            }

            if(isAlive)
                if(activeMissileThread != null)
                    try{
                        activeMissileThread.join();
                        Thread.sleep(20);
                        fireDestroyMissileEvent();
                    }
                    catch (InterruptedException e){
                     //   e.printStackTrace();
                    }
                System.out.println("Missile Launcher n` " + id + " All missiles out after " + time.getTime() + " seconds");
        }

        public synchronized void addWarEventListener(WarEventListener warEventListener){
            warEventListeners.add(warEventListener);
        }
        public synchronized void addMissileEventListeners(MissileEventListener missileEventListener){
            missileEventListeners.add(missileEventListener);
        }

    private synchronized void fireCreateMissileLauncherEvent(){
            WarEvent e = new WarEvent(id);
            e.setEventType(Event_Type.CREATE_LAUNCHER);
            e.setCoordinates(coordinates);
            e.setHidden(isHidden);
            for(WarEventListener listener: warEventListeners){
                listener.handleWarEvent(e);
            }
        }


    private synchronized void fireDestroyMissileLauncherEvent(){
        WarEvent e = new WarEvent(id);
        e.setEventType(Event_Type.DESTROY_LAUNCHER);
        for(WarEventListener listener : warEventListeners){
            listener.handleWarEvent(e);
        }
    }

    private synchronized void fireLaunchMissileEvent(){
            MissileEvent m = new MissileEvent();
            m.setMissile(activeMissileEntity);
            for(MissileEventListener missileEventListener : missileEventListeners){
                missileEventListener.handleMissileLaunch(m);
            }


            WarEvent e = new WarEvent(id);
            e.setEventType(Event_Type.LAUNCH_MISSILE);
            e.setCoordinates(coordinates);
            e.setTargetCoordinates(activeMissileEntity.getTargetCoordinates());
            e.setMissileId(activeMissileEntity.getId());
            for(WarEventListener listener: warEventListeners){
                listener.handleWarEvent(e);
            }
        }

    private synchronized void fireDestroyMissileEvent(){
        WarEvent e = new WarEvent(id);
        e.setEventType(Event_Type.DESTROY_MISSILE);
        e.setMissileId(activeMissileEntity.getId());
            for(WarEventListener listener: warEventListeners){
                listener.handleWarEvent(e);
            }
        }

    private synchronized void fireHideMissileLauncherEvent(){
        WarEvent e = new WarEvent(id);
        e.setEventType(Event_Type.HIDE_LAUNCHER);
        for(WarEventListener listener : warEventListeners){
            listener.handleWarEvent(e);
        }
        }






}
