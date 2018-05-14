package com.afekawar.bl.base.Entities;

import com.afekawar.bl.base.Entities.Missile;
import com.afekawar.bl.base.Interface.InterfaceImp;
import javafx.geometry.Point2D;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class MissileDestructor implements Runnable {
    /* *************************************************************
     * ******************** Fields and Properties ******************
     * ************************************************************* */
    private String id;
    private Logger logger;
    private TreeMap<Integer, Missile> targetMissiles; // Will try to destroy target missile if not null
    private boolean isAlive;
    private InterfaceImp data;
    private Point2D coordinates;



    public MissileDestructor(String id,InterfaceImp data) {
        this.id = id;
        isAlive = true;
        targetMissiles = new TreeMap<>();
        this.data = data;
        coordinates = new Point2D(ThreadLocalRandom.current().nextInt(800, 1100 + 1),ThreadLocalRandom.current().nextInt(200, 660 + 1));  // Set Random coordinate within Israel Defense Border

    }

    /* *************************************************************
     * ******************** Getters and Setters ********************
     * ************************************************************* */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    //public Missile getTargetMissile() {
    // return targetMissile;
    // }
    public Point2D getCoordinates(){
        return coordinates;
    }
    public void addTargetMissile(int destrTime, Missile missile) {
        targetMissiles.put(destrTime, missile);
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    /* *************************************************************
     * ******************** Run Logic ******************************
     * ************************************************************* */
    @Override
    public void run() {
        System.out.println("Missile Destructor n` " + id + " Started...");
        Long startTime = System.nanoTime();

        Iterator it = targetMissiles.keySet().iterator();

        while (it.hasNext()) {
            int destructTime = (int) it.next();
            Missile m = targetMissiles.get(destructTime);
            Long currentTime = ((System.nanoTime() - startTime) / 1000000000);
            if (currentTime < destructTime) {
                System.out.println("Missile Destructor n` " + id + " awaiting for " + (destructTime - currentTime) + " seconds...");
                try {
                    Thread.sleep((destructTime - currentTime) * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (m.getState() == Missile.State.INAIR) {
                m.setState("DEAD");
                data.destroyMissile(m.getLauncherId(),m.getId());



            }



        }

        System.out.println("Missile Destructor n` " + id + " Ended...");

    }



}