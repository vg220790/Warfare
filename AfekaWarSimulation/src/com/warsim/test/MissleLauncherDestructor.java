package com.warsim.test;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class MissleLauncherDestructor {
    private String id;
    private HashMap<String, Integer> destructedLaunches;

    public MissleLauncherDestructor(String id){
        this.id = id;
    }

    public void AddDestructedLauncher(String launcherId, int destructTime){
        destructedLaunches.put(launcherId, destructTime);
    }
}
