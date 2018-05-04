package com.warsim.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MissleDestructor {
    private String id;
    private Map<String, Integer> destructMissile;

    public MissleDestructor(String id){
        this.id = id;
        destructMissile = new HashMap<>();
    }

    public void AddDestructMissle(String missleId, int destructAfterLaunch){
        destructMissile.put(missleId, destructAfterLaunch);
    }

}
