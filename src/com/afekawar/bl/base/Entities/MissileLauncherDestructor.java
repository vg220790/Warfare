package com.afekawar.bl.base.Entities;

import com.afekawar.bl.base.Interface.InterfaceImp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Logger;


public class MissileLauncherDestructor implements Runnable {
    /* *************************************************************
     * ******************** Fields and Properties ******************
     * ************************************************************* */
    public static int idInc = 0;
    public enum Type { AIRCRAFT, BATTLESHIP }
    private String id;
    private Type type;
    private Logger logger;
    private boolean isAlive;
    private TreeMap<Integer,MissileLauncher> targetMissileLaunchers; // Will try to destroy target missile if not null
    private InterfaceImp data;

    public MissileLauncherDestructor(String type, InterfaceImp data){
        this.id = "LD30" + (1 + idInc++);
        if(type.equals("plane")){
            this.type = Type.AIRCRAFT;
        }
        else{
            this.type = Type.BATTLESHIP;
        }
        isAlive = true;
        targetMissileLaunchers = new TreeMap<>();
        this.data = data;
    }

    /* *************************************************************
     * ******************** Getters and Setters ********************
     * ************************************************************* */
    public boolean isAlive() {

        return isAlive;
    }
    public void setAlive(boolean alive) {
        isAlive = alive;
    }
    public Logger getLogger() {

        return logger;
    }
    public void setLogger(Logger logger) {
        this.logger = logger;
    }
    public String getId() {

        return id;
    }

    public void stopThread(){
        isAlive = false;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Type getType() {
        return type;
    }
    public void setType(Type type) {
        this.type = type;
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

            Long startTime = System.nanoTime();

            Iterator it = targetMissileLaunchers.keySet().iterator();

            while (it.hasNext()) {
                int destructTime = (int) it.next();
                MissileLauncher launcher = targetMissileLaunchers.get(destructTime);
                Long currentTime = ((System.nanoTime() - startTime) / 1000000000);
                if (currentTime < destructTime) {
                    System.out.println("Missile Launcher Destructor n` " + id + " awaiting for " + (destructTime - currentTime) + " seconds...");
                    try {
                        Thread.sleep((destructTime - currentTime) * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(type.toString() + " n` " + id + " Attempting to destroy Missle Launcher n` " + launcher.getId());
                data.destroyMissileLauncher(launcher.getId());


            }


            System.out.println("Missile Launcher Destructor n` " + id + " Ended...");


    }


}
