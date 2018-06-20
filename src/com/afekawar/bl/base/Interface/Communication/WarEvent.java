package com.afekawar.bl.base.Interface.Communication;

import com.afekawar.bl.base.Entities.BaseEntities.MissileLauncherDestructor;
import javafx.geometry.Point2D;

public class WarEvent {
    public enum Event_Type {CREATE_LAUNCHER, DESTROY_LAUNCHER, LAUNCH_MISSILE, DESTROY_MISSILE, HIDE_LAUNCHER, CREATE_MISSILE_DESTRUCTOR, LAUNCH_ANTI_MISSILE, DESTROY_ANTI_MISSILE, CREATE_MISSILE_LAUNCHER_DESTRUCTOR, LAUNCH_ANTI_MISSILE_LAUNCHER, DESTROY_ANTI_MISSILE_LAUNCHER, UPDATE_COORDINATES}
    private Event_Type eventType;
    private String id;
    private String missileId;
    private Point2D coordinates;
    private Point2D targetCoordinates;
    private boolean isHidden;
    private MissileLauncherDestructor.Type destructorType;

    public void setEventType(Event_Type eventType){
        this.eventType = eventType;
    }
    public void setTargetCoordinates(Point2D targetCoordinates){
        this.targetCoordinates = targetCoordinates;
    }

    public Point2D getTargetCoordinates() {
        return targetCoordinates;
    }


    public Event_Type getEventType() {
        return eventType;
    }

    public void setMissileId(String missileId){
        this.missileId = missileId;
    }
    public String getMissileId(){
        return missileId;
    }
    public void setCoordinates(Point2D coordinates){
        this.coordinates = coordinates;
    }
    public Point2D getCoordinates(){
        return coordinates;
    }
    public String getId(){
        return id;
    }

    public WarEvent(String id){
        this.id = id;
    }
    public boolean getHidden(){
        return isHidden;
    }
    public void setHidden(boolean isHidden){
        this.isHidden = isHidden;
    }
    public void setDestructorType(MissileLauncherDestructor.Type destructorType){
        this.destructorType = destructorType;
    }
    public MissileLauncherDestructor.Type getDestructorType() {
        return destructorType;
    }


}
