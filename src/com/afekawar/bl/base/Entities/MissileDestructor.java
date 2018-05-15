package com.afekawar.bl.base.Entities;

import com.afekawar.bl.base.Interface.InterfaceImp;
import javafx.geometry.Point2D;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
// import java.util.logging.Logger;

public class MissileDestructor implements Runnable {
    /* *************************************************************
     * ******************** Fields and Properties ******************
     * ************************************************************* */
    private String id;
  //  private Logger logger;                      // TODO - Implement Logger
    private TreeMap<Integer, Missile> targetMissiles; // Will try to destroy target missile if not null
    private InterfaceImp data;
    private Point2D coordinates;
    private int destructLength;           // Time takes to destroy a missile.
    private Missile activeDestMissile;



    public MissileDestructor(String id,InterfaceImp data) {
        this.id = id;
        targetMissiles = new TreeMap<>();
        this.data = data;
        coordinates = new Point2D(ThreadLocalRandom.current().nextInt(800, 1100 + 1),ThreadLocalRandom.current().nextInt(200, 660 + 1));  // Set Random coordinate within Israel Defense Border
        destructLength = 2;
        activeDestMissile = null;
    }

    /* *************************************************************
     * ******************** Getters and Setters ********************
     * ************************************************************* */
    public String getId() {
        return id;
    }
    public Point2D getCoordinates(){
        return coordinates;
    }
    public int getDestructLength(){
        return destructLength;
    }
    public Missile getActiveDestMissile(){
        return activeDestMissile;
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
    public void setActiveDestMissile(Missile activeDestMissile){
        this.activeDestMissile = activeDestMissile;
    }

    public void addTargetMissile(int destrTime, Missile missile) {
        targetMissiles.put(destrTime, missile);
    }



    /* *************************************************************
     * ******************** Run Logic ******************************
     * ************************************************************* */
    @Override
    public void run() {
        System.out.println("Missile Destructor n` " + id + " Started...");
        Long startTime = System.nanoTime();

        for(int destructTime: targetMissiles.keySet()) {
            Missile m = targetMissiles.get(destructTime);
            Long currentTime = ((System.nanoTime() - startTime) / 1000000000);
            if (currentTime + destructLength < destructTime) {
                System.out.println("Missile Destructor n` " + id + " awaiting for " + (destructTime - destructLength - currentTime) + " seconds...");
                try {
                    Thread.sleep((destructTime - destructLength - currentTime) * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            if(m.getState() == Missile.State.INAIR){
                try {
                    launchAntiMissile(m.getId(), startTime);
                    activeDestMissile = m;
                    Thread.sleep(destructLength * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (m.getState() == Missile.State.INAIR) {
                    m.setState("DEAD");
                    data.destroyMissile(m.getLauncherId(), m.getId());


                }

            }





        }

        System.out.println("Missile Destructor n` " + id + " Ended...");

    }

    private void launchAntiMissile(String id, long startTime){
        System.out.println("Missile Destructor n` " + this.id + " Launched anti missile rocket towards Missile n` " + id + " at " + (System.nanoTime() - startTime)/1000000000 + " seconds..");
    }




}