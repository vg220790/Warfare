package com.warsim.test;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class MissileLauncherDestructor {
    private static int idInc = 0;
    private String id;
    private HashMap<Integer, String> destructedLaunches;

    public MissileLauncherDestructor(){
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
