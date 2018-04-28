package com.warsim.test;

import java.util.ArrayList;
import java.util.HashMap;

public class MissleLauncher {
    private String id;
    private boolean isHidden;
    private HashMap<String, Missle> missles;

    public MissleLauncher(String id, boolean isHidden){
        this.id = id;
        this.isHidden = isHidden;
    }

    public boolean IsHidden(){
        return isHidden;
    }

    public void AddMissle(String missleId, Missle missle){
        missles.put(missleId, missle);
    }

}
