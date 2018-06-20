package com.afekawar.bl.base.Entities.BaseEntities;

import Logging.MyLogger;
import SharedInterface.WarInterface;
import com.afekawar.bl.base.Interface.Communication.WarEventListener;
import com.afekawar.bl.base.Interface.Time.SystemTime;
import com.afekawar.bl.base.Statistics;
import javafx.geometry.Point2D;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;


public abstract class WarEntity implements Runnable {
    private boolean warRunning;
    private SystemTime time;
    private Point2D velocity;
    private Point2D coordinates;
    private String id;
    private MyLogger logger;
    private Set<WarEventListener> warEventListeners;
    private Statistics statistics;


    public void setCoordinates(Point2D coordinates) {
        this.coordinates = coordinates;
    }

    WarEntity(){                      // Created before War is started
        this.warRunning = false;
        this.velocity = new Point2D(0,0);
        this.warEventListeners = new HashSet<>();
    }

    WarEntity(String id, SystemTime time){
        this.warRunning = true;
        this.velocity = new Point2D(0,0);
        this.warEventListeners = new HashSet<>();
        this.id = id;
        this.time = time;
    }
    WarEntity(String id){                      // Created before War is started
        this.warRunning = false;
        this.velocity = new Point2D(0,0);
        this.warEventListeners = new HashSet<>();
        this.id = id;
    }
    public void setLogger(MyLogger logger){
        this.logger = logger;
        logger.addHandler(this.getClass().getSimpleName() + "Log.txt", getClass().getName());
    }

    Logger getLogger(){
        return logger;
    }


    public void stopThread(){                                                       // Missile launcher destroy func
        warRunning = false;
        if(logger != null)
            logger.info(this.getClass().getSimpleName() + " " + getId() + " Ended at " + getTime().getTime() + " seconds");
    }

    public SystemTime getTime() {
        return time;
    }

    public Point2D getCoordinates() {
        return coordinates;
    }

    public String getId() {
        return id;
    }

    public void update(){
        setCoordinates(coordinates.add(velocity));
    }

    void setVelocity(Point2D velocity){
        this.velocity = velocity;
    }
    public void setTime(SystemTime time){
        this.time = time;
    }
    Point2D getVelocity(){
        return velocity;
    }

    synchronized void setWarEventListeners(Set<WarEventListener> warEventListeners){
        this.warEventListeners = warEventListeners;
    }

    Set<WarEventListener> getListeners(){
        return warEventListeners;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WarEntity warEntity = (WarEntity) o;
        return Objects.equals(getId(), warEntity.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    public synchronized void addWarEventListener(WarEventListener warEventListener){
        warEventListeners.add(warEventListener);
    }
    boolean isWarRunning() {
        return warRunning;
    }


    @Override
    public void run(){
        if(logger != null)
            getLogger().info(getClass().getSimpleName() + " " + id + " Started...");
    }

    @Override
    public String toString(){
        return id;
    }

    public void startWar(){
        warRunning = true;
    }

    public void init(WarInterface warInterface){

    }

    public void setStatistics(Statistics statistics){
        this.statistics = statistics;
    }

    Statistics getStatistics(){
        return statistics;
    }








}
