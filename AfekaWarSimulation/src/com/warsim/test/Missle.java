package com.warsim.test;

public class Missle {
    private String id;
    private int flyTime;
    private int launchTime;
    private String destination;
    private int damage;

    public Missle(String id, int launchTime, int flyTime, String destination, int damage){
        this.id = id;
        this.launchTime = launchTime;
        this.flyTime = flyTime;
        this.destination = destination;
        this.damage = damage;
    }

    public int getDamage(){
        return damage;
    }

    public String getId(){
        return id;
    }
    public int getFlyTime(){
        return flyTime;
    }
    public String getDestination(){
        return destination;
    }
    public int getLaunchTime(){
        return launchTime;
    }
}
