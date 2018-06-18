package com.afekawar.bl.base;

import GraphicsContent.GraphicsApplication;
import JSONParser.MockEntities.BaseEntities.LD;
import JSONParser.MockEntities.BaseEntities.M;
import JSONParser.MockEntities.BaseEntities.MD;
import JSONParser.MockEntities.BaseEntities.ML;
import JSONParser.MockEntities.BaseEntities.SubEntities.DestLauncher;
import JSONParser.MockEntities.BaseEntities.SubEntities.DestMissile;
import SharedInterface.WarInterface;
import JSONParser.WarParser;
import com.afekawar.bl.base.Entities.*;
import com.afekawar.bl.base.Interface.Communication.MissileEventListener;
import com.afekawar.bl.base.Interface.Time.SystemTime;

import java.util.*;

public class MainLogic implements Runnable{

    private WarInterface warInterface;
    private SystemTime time;
    private GraphicsApplication app;
    private WarParser parsedEntities;
    private Map<String,WarEntity> entities;
    private Map<String,WarEntity> missiles;
    private boolean warRunning;
    private Set<MissileEventListener> missileEventListeners;

    public MainLogic(SystemTime time, GraphicsApplication app, WarParser parsedEntities){
        this.missiles = new HashMap<>();
        this.missileEventListeners = new HashSet<>();
        this.time = time;
        this.app = app;
        this.parsedEntities = parsedEntities;
        entities = new HashMap<>();


    }

    @Override
    public void run() {
        System.out.println("System starts");
        warRunning = true;
        // Create Entities.

        createObjects(parsedEntities,entities,missiles,time);

        for(WarEntity entity : entities.values()){
            entity.addWarEventListener(app);

            if(entity instanceof MissileDestructor)
                missileEventListeners.add((MissileDestructor)entity);
            if(entity instanceof MissileLauncher)
                ((MissileLauncher) entity).setMissileEventListeners(missileEventListeners);

            Thread th = new Thread(entity);
            th.setName(entity.getId());
            th.start();
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


        System.out.println("System Halts");
    }

    public void haltSystem(){
        warRunning = false;
    }

    public void addWarEntity(Object entity){
        WarEntity newEntity;
        newEntity = createObject(entity,time);
        if(newEntity instanceof MissileLauncher)
            ((MissileLauncher) newEntity).setMissileEventListeners(missileEventListeners);
        if(newEntity instanceof MissileDestructor)
            missileEventListeners.add((MissileDestructor)newEntity);


        entities.put(newEntity.getId(),newEntity);
        Thread th = new Thread(newEntity);
        th.setName(newEntity.getId());
        th.start();
    }
    public void addMissileEntity(String launcherId, M missile){
        WarEntity newEntity;
        newEntity = createObject(missile,time);
        (newEntity).setCoordinates((entities.get(launcherId)).getCoordinates());
        missiles.put(newEntity.getId(),newEntity);
        ((MissileLauncher)entities.get(launcherId)).addMissile((Missile)newEntity);
    }
    public void addDestLauncherCommand(String destructorId, int destTime, String launcherId){
        ((MissileLauncherDestructor)entities.get(destructorId)).addDestructedLauncher(destTime,entities.get(launcherId));
    }
    public void addDestMissileCommand(String destructorId, int destTime, String missileId){
        ((MissileDestructor)entities.get(destructorId)).addTargetMissile(destTime,missiles.get(missileId));
    }

    public synchronized void createObjects(WarParser parsedElement, Map<String,WarEntity> entities,Map<String,WarEntity> missiles, SystemTime time) {
        StaticTargets targets = new StaticTargets();
        for (ML mLauncher : parsedElement.getMissileLaunchers()) {
            MissileLauncher newLauncher = new MissileLauncher(mLauncher.getId(), mLauncher.isHidden(), time);
            for (M missile : mLauncher.getMissile()) {
                Target tempTarget = targets.getTargetByName(missile.getDestination());
                int launchTime = Integer.parseInt(missile.getLaunchTime());
                int flyTime = Integer.parseInt(missile.getFlyTime());
                int damage = Integer.parseInt(missile.getDamage());
                Missile temp = new Missile(missile.getId(), tempTarget.getCoordinates(), launchTime, flyTime, damage, time);
                temp.setCoordinates(newLauncher.getCoordinates());
                missiles.put(temp.getId(),temp);
                newLauncher.addMissile(temp);
            }
            entities.put(newLauncher.getId(), newLauncher);
        }

        for (LD mLDestructor : parsedElement.getMissileLauncherDestructors()) {
            MissileLauncherDestructor newDestructor = new MissileLauncherDestructor(mLDestructor.getType(), time);
            for (DestLauncher destLauncher : mLDestructor.getDestructedLanucher()) {
                int destTime = Integer.parseInt(destLauncher.getDestructTime());
                newDestructor.addDestructedLauncher(destTime, entities.get(destLauncher.getId()));
            }
            entities.put(newDestructor.getId(), newDestructor);
        }

        for (MD mDestructor : parsedElement.getMissileDestructors()) {
            MissileDestructor newDestructor = new MissileDestructor(mDestructor.getId(), time);
            for (DestMissile destMissile : mDestructor.getDestructdMissile()) {
                int destAfterLaunch = Integer.parseInt(destMissile.getDestructAfterLaunch());
                newDestructor.addTargetMissile(destAfterLaunch, missiles.get(destMissile.getId()));
            }
            entities.put(newDestructor.getId(), newDestructor);
        }


    }

    public synchronized WarEntity createObject(Object entity, SystemTime time){
        StaticTargets targets = new StaticTargets();
        if(entity instanceof ML){
            MissileLauncher newLauncher = new MissileLauncher(((ML) entity).getId(),((ML) entity).isHidden(),time);
            return newLauncher;
        }
        if(entity instanceof LD){
            MissileLauncherDestructor newLauncherDestructor = new MissileLauncherDestructor(((LD) entity).getType(),time);
            return newLauncherDestructor;
        }
        if(entity instanceof MD){
            MissileDestructor newDestructor = new MissileDestructor(((MD) entity).getId(),time);
            return newDestructor;
        }
        if(entity instanceof M){
            Target destTarget = targets.getTargetByName(((M) entity).getDestination());
            int launchTime = Integer.parseInt(((M) entity).getLaunchTime());
            int flyTime = Integer.parseInt(((M) entity).getFlyTime());
            int damage = Integer.parseInt(((M) entity).getDamage());
            Missile newMissile = new Missile(((M) entity).getId(),destTarget.getCoordinates(),launchTime,flyTime,damage,time);
            return newMissile;
        }
        return null;
    }
}
