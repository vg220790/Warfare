package com.warsim.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MissileDestructor {
    private String id;
    private Map<String, Integer> destructMissile;

    public MissileDestructor(String id){
        this.id = id;
        destructMissile = new HashMap<>();
    }

    public void AddDestructMissle(String missleId, int destructAfterLaunch){
        destructMissile.put(missleId, destructAfterLaunch);
    }

}
