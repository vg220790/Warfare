package com.afekawar.bl.base.Entities;

import com.afekawar.bl.base.Entities.Missile;
import javafx.geometry.Point2D;

import java.util.*;
import java.util.logging.Logger;
import java.util.concurrent.ThreadLocalRandom;

public class MissileLauncher implements Runnable {
    /* *************************************************************
     * ******************** Fields and Properties ******************
     * ************************************************************* */
    private String id;
    private Logger logger;
    private Queue<Missile> missiles;
    private boolean isAlive;
    private boolean isHidden;
    private Thread activeMissileThread;
    private Point2D coordinates;


    public MissileLauncher(String id, boolean isHidden){
        int randomNumbersMinX[] = {680,740,610,450};
        int randomNumbersMaxX[] = {760,870,660,620};

        int randomNumbersMinY[] = {140,40,220,400};
        int randomNumbersMaxY[] = {210,125,300,500};

        int index = ThreadLocalRandom.current().nextInt(0,4);


        this.id = id;
        this.isHidden = isHidden;
        this.isAlive = true;
        missiles = new PriorityQueue<>();
        activeMissileThread = null;
        coordinates = new Point2D(ThreadLocalRandom.current().nextInt(randomNumbersMinX[index], randomNumbersMaxX[index] + 1),ThreadLocalRandom.current().nextInt(randomNumbersMinY[index], randomNumbersMaxY[index] + 1));  // Set Random coordinate within Gaza Strip

    }

    /* *************************************************************
     * ******************** Getters and Setters ********************
     * ************************************************************* */
    public boolean isHidden() {
        return isHidden;
    }
    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }
    public boolean getHidden(){
        return isHidden;
    }
    public boolean isAlive() {

        return isAlive;
    }
    public void setAlive(boolean alive) {
        isAlive = alive;
    }
    public Queue<Missile> getMissiles() {

        return missiles;
    }
    public void stopThread(){
        System.out.println("Missile Launcher n` " + id + " Got destroyed....");
        activeMissileThread.interrupt();
        isAlive = false;
        missiles.clear();
    }
    public Thread getActiveMissileThread(){
        return activeMissileThread;
    }
    public void setMissiles(Queue<Missile> missiles) {
        this.missiles = missiles;
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
    public Point2D getCoordinates(){
        return coordinates;
    }
    public void setId(String id) {
        this.id = id;
    }

    /* *************************************************************
     * ******************** Run Logic ******************************
     * ************************************************************* */
    @Override
    public void run() {

     //   while(isAlive) {
            Long time = System.nanoTime();                                        // Current nanosec value

            System.out.println("Missile Launcher n` " + id + " Started...");

            try {
                Thread.sleep(((Missile) missiles.peek()).getLaunchTime() * 1000);           // Check how long the launcher should wait till 1st missile
            } catch (InterruptedException e) {

                e.printStackTrace();
                isAlive = false;
                missiles.clear();
            }
            while (!missiles.isEmpty()) {
                Missile m = (Missile) missiles.peek();
                Long waitTime = m.getLaunchTime() - ((System.nanoTime() - time) / 1000000000);     // Check if the next missle's launch time is later than earlier missile finished it's fly...
                if (waitTime > 0)
                    try {
                        isHidden = true;
                        System.out.println("Launcher n` " + id + " Waiting " + waitTime + " seconds till next Missile..");
                        Thread.sleep(waitTime * 1000);
                    } catch (InterruptedException e) {
                    e.printStackTrace();
                    }
                    if(isAlive) {
                        isHidden = false;
                        System.out.println("Missile n` " + m.getId() + " From Launcher n` " + id + " Launched at " + ((System.nanoTime() - time) / 1000000000) + " seconds");
                        Thread missileThread = new Thread((Runnable) m);
                        missileThread.setName(m.getId());
                        missileThread.start();
                        activeMissileThread = missileThread;
                        // threads.put(missileThread.getName(),missileThread);

                        try {
                            synchronized (missileThread) {
                                missileThread.wait();                    // Launcher waits till Missile ends it's life - reaching it's target or being destructed..
                            }
                        } catch (InterruptedException e) {
                            // e.printStackTrace();
                            System.out.println("Missile Launcher n` " + id + " Got destroyed....");
                            System.out.println("Missile n` " + activeMissileThread.getName() + " Lost connection to launcher n` " + id + " and got destroyed...");
                            activeMissileThread.interrupt();
                            isAlive = false;
                            missiles.clear();
                        }
                    }


            }

            if(isAlive)
                System.out.println("Missile Launcher n` " + id + " All missiles out after " + ((System.nanoTime() - time) / 1000000000) + " seconds");
        }
  //  }
}
