package com.afekawar.bl.base.Entities.BaseEntities;

import SharedInterface.WarInterface;
import com.afekawar.bl.base.Interface.Communication.WarEventListener;
import com.afekawar.bl.base.Interface.Time.SystemTime;
import javafx.geometry.Point2D;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public abstract class WarEntity implements Runnable {
    private boolean warRunning;
    private SystemTime time;
    private Point2D velocity;
    private Point2D coordinates;
    private String id;
    private Set<WarEventListener> warEventListeners;

    public void setCoordinates(Point2D coordinates) {
        this.coordinates = coordinates;
    }

    public WarEntity(){                      // Created before War is started
        this.warRunning = false;
        this.velocity = new Point2D(0,0);
        this.warEventListeners = new HashSet<>();
    }

    public WarEntity(String id, SystemTime time){
        this.warRunning = true;
        this.velocity = new Point2D(0,0);
        this.warEventListeners = new HashSet<>();
        this.id = id;
        this.time = time;
    }
    public WarEntity(String id){                      // Created before War is started
        this.warRunning = false;
        this.velocity = new Point2D(0,0);
        this.warEventListeners = new HashSet<>();
        this.id = id;
    }


    public void stopThread(){                                                       // Missile launcher destroy func
        warRunning = false;
        System.out.println(this.getClass().getSimpleName() + " " + getId() + " Ended at " + getTime().getTime() + " seconds");
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

    public void setVelocity(Point2D velocity){
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

    public Set<WarEventListener> getListeners(){
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
    public boolean isWarRunning() {
        return warRunning;
    }


    @Override
    public void run(){

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




}
