package com.afekawar.bl.base;

// Max Golotsvan - 314148123
// Maksim Lyashenko - 317914877

import GraphicsContent.GraphicsApplication;
import Logging.MyLogger;
import SharedInterface.WarInterface;
import com.afekawar.bl.base.Entities.BaseEntities.*;
import com.afekawar.bl.base.Interface.Communication.MissileEventListener;
import com.afekawar.bl.base.Interface.Time.SystemTime;


import java.util.*;
import java.util.logging.Handler;

public class MainLogic implements Runnable{

    private WarInterface warInterface;
    private SystemTime time;
    private GraphicsApplication app;
    private Map<String,WarEntity> entities;
    private Map<String,WarEntity> missiles;
    private List<Thread> threads;
    private boolean warRunning;
    private Set<MissileEventListener> missileEventListeners;
    private MyLogger logger;
    private Statistics statistics;


    public MainLogic(SystemTime time, GraphicsApplication app, WarInterface warInterface){
        this.missiles = new HashMap<>();
        this.missileEventListeners = new HashSet<>();
        this.time = time;
        this.app = app;
        this.warInterface = warInterface;
        this.warInterface.setMainProgram(this);
        entities = new HashMap<>();
        threads = new ArrayList<>();
        logger = new MyLogger("SystemLog");
        logger.addHandler("SystemLog.txt",getClass().getName());
        statistics = new Statistics();


    }

    @Override
    public void run() {
        logger.info("System starts");

        warRunning = true;
        // Create Entities.

        createObjects(warInterface,entities,missiles);

        for(WarEntity entity : missiles.values()){
            ((Missile)entity).setTargetCoordinates(warInterface.getTargetByName(((Missile)entity).getDestination()).getCoordinates());
            entity.setTime(time);
            entity.startWar();
        }

        for(WarEntity entity : entities.values()){
            entity.init(warInterface);            // Init some static variables after parsing from JSON file...
            entity.startWar();

            addWarEntity(entity);
        }

        while(warRunning){
            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



        for(WarEntity entity : entities.values()){
           entity.stopThread();
        }
        for(Thread th : threads){
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        logger.info("System Halts");
        for (Handler handler : logger.getMyFileHandlers().values())
            handler.close();

        System.exit(0);
    }

    public void haltSystem(){
        warRunning = false;

    }

    public void addWarEntity(WarEntity entity){
        entity.setStatistics(statistics);
        entity.setLogger(logger);
        entity.setTime(time);
        entity.addWarEventListener(app);
        if(entity instanceof MissileLauncher)
            ((MissileLauncher) entity).setMissileEventListeners(missileEventListeners);
        if(entity instanceof MissileDestructor)
            missileEventListeners.add((MissileDestructor)entity);

        if(!entities.containsKey(entity.getId()))
            entities.put(entity.getId(),entity);
        Thread th = new Thread(entity);
        th.setName(entity.getId());
        threads.add(th);
        th.start();
    }

    public void addMissileEntity(WarEntity missile){
        missiles.put(missile.getId(),missile);
    }

    public void addDestLauncherCommand(String destructorId, int destTime, String launcherId){
        ((MissileLauncherDestructor)entities.get(destructorId)).addDestructedLauncher(destTime,entities.get(launcherId));
    }
    public void addDestMissileCommand(String destructorId, int destTime, String missileId){
        ((MissileDestructor)entities.get(destructorId)).addTargetMissile(destTime,missiles.get(missileId));
    }

    private synchronized void createObjects(WarInterface parsedElement, Map<String,WarEntity> entities,Map<String,WarEntity> missiles) {
        for (MissileLauncher mLauncher : parsedElement.getMissileLaunchers()) {
            for (Missile missile : mLauncher.getMissiles()) {
                missile.setCoordinates(mLauncher.getCoordinates());
                missiles.put(missile.getId(),missile);
            }
            entities.put(mLauncher.getId(), mLauncher);
        }

        for (MissileLauncherDestructor mLDestructor : parsedElement.getMissileLauncherDestructors()) {

            entities.put(mLDestructor.getId(), mLDestructor);
        }

        for (MissileDestructor mDestructor : parsedElement.getMissileDestructors()) {

            entities.put(mDestructor.getId(), mDestructor);
        }
    }


    public String getStats(){
        return statistics.toString();
    }
}
