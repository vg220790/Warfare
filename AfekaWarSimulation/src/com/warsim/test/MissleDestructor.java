package com.warsim.test;

import java.util.ArrayList;
import java.util.HashMap;

public class MissleDestructor {
    private String id;
    private HashMap<String, Integer> destructMissile;

    public MissleDestructor(String id){
        this.id = id;
    }

    public void AddDestructMissle(String missleId, int destructAfterLaunch){
        destructMissile.put(missleId, destructAfterLaunch);
    }

}
