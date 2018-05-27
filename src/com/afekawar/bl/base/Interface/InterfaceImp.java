package com.afekawar.bl.base.Interface;

import com.afekawar.bl.base.Entities.*;

import java.util.Map;

public class InterfaceImp implements SystemInterface {
    private Map<String,Runnable> entities;
    private Map<String,Thread> threads;


    public InterfaceImp(Map<String,Runnable> entities,Map<String,Thread> threads){
        this.entities = entities;
        this.threads = threads;                                      // To control all running threads.
    }

    @Override
    public void addMissileLauncher(String id, MissileLauncher missileLauncher) {
        entities.put(id,missileLauncher);
        Thread th = new Thread(missileLauncher);
        th.setName(id);
        th.start();
        threads.put(id,th);
    }

    @Override
    public void addMissileLauncherDestructor(String id, MissileLauncherDestructor missileLauncherDestructor) {
        entities.put(id,missileLauncherDestructor);
        Thread th = new Thread(missileLauncherDestructor);
        th.setName(id);
        th.start();
        threads.put(id,th);
    }

    @Override
    public void addMissileDestructor(String id, MissileDestructor missileDestructor) {
        entities.put(id,missileDestructor);
        Thread th = new Thread(missileDestructor);
        th.setName(id);
        th.start();
        threads.put(id,th);
    }

    @Override
    public Runnable getEntityById(String id) {               // TODO - rework to avoid un-needed castings...
        return entities.get(id);
    }

    @Override
    public Thread getThreadById(String id){
        return threads.get(id);
    }



    @Override
    public void addMissile(String id, Missile missile) {
        entities.put(id,missile);
    }



    @Override
    public void launchMissile(String destination, int damage, int flyTime) {           // TODO - implement and use that interface instead of current solution

    }

    @Override
    public void destroyMissileLauncher(String id) {
        if(!((MissileLauncher)entities.get(id)).getHidden()) {
            ((MissileLauncher)entities.get(id)).stopThread();
            //entities.remove(id);
            //threads.remove(id);             // TODO - Removing thread from total thread collection, causes ConcurrentModificationException
                                              // when trying to .join threads in main loop, need a workaround.


        }
    }

    @Override
    public void destroyMissile(String launcherId, String missileId) {
        ((MissileLauncher)entities.get(launcherId)).getActiveMissileThread().interrupt();     // TODO - implement proper Stop Thread to Missile class
    }

    @Override
    public void showStats() {

    }

    @Override
    public void exit() {

    }

}
