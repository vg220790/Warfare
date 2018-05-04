package com.warsim.test;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class MissleLauncherDestructor {
    private static int idInc = 0;
    private String id;
    private HashMap<Integer, String> destructedLaunches;

    public MissleLauncherDestructor(){
        this.id = "LD10" + (1 + idInc++);
        destructedLaunches = new HashMap<>();
    }

    public void AddDestructedLauncher(String launcherId, int destructTime){
        destructedLaunches.put(destructTime, launcherId);
    }

    public String getId(){
        return id;
    }
}
