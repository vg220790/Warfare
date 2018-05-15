package com.afekawar.bl.base.Entities;

import com.afekawar.bl.base.Interface.InterfaceImp;
import javafx.geometry.Point2D;

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
    private int destructLength;           // Time takes to destroy a Missile Launcher.
   // private Logger logger;
    private TreeMap<Integer,MissileLauncher> targetMissileLaunchers; // Will try to destroy target missile if not null
    private InterfaceImp data;
    private Point2D coordinates;
    private MissileLauncher activeDestLauncher;

    public MissileLauncherDestructor(String type, InterfaceImp data){
        this.id = "LD30" + (1 + idInc++);
        if(type.equals("plane")){
            this.type = Type.AIRCRAFT;
        }
        else{
            this.type = Type.BATTLESHIP;
        }
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

            Long startTime = System.nanoTime();


            for(int destructTime:targetMissileLaunchers.keySet()){
                MissileLauncher launcher = targetMissileLaunchers.get(destructTime);
                Long currentTime = ((System.nanoTime() - startTime) / 1000000000);
                if (currentTime + destructLength < destructTime) {
                    System.out.println("Missile Launcher Destructor n` " + id + " awaiting for " + (destructTime - destructLength - currentTime) + " seconds...");
                    try {
                        Thread.sleep((destructTime - destructLength - currentTime) * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(launcher.getAlive()){
                    try {
                        launchAntiMissileLauncher(launcher.getId(), startTime);
                        activeDestLauncher = launcher;
                        Thread.sleep(destructLength * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (launcher.getAlive()) {
                        data.destroyMissileLauncher(launcher.getId());
                    }

                }

            }


            System.out.println("Missile Launcher Destructor n` " + id + " Ended...");


    }
    private void launchAntiMissileLauncher(String id, long startTime){
        System.out.println(type + " n` " + this.id + " Launched anti Launcher rocket towards Launcher n` " + id + " at " + (System.nanoTime() - startTime)/1000000000 + " seconds..");
    }

}
