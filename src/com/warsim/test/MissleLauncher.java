package com.warsim.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MissleLauncher {
    private String id;
    private boolean isHidden;
    private List<Missle> missle;

    public MissleLauncher(String id, boolean isHidden){
        this.id = id;
        this.isHidden = isHidden;
        missle = new ArrayList<>();
    }

    public void addMIssles(List<Missle> missle){
        this.missle = missle;
    }

    public boolean IsHidden(){
        return isHidden;
    }

    public void addMissle(Missle m){
        missle.add(m);
    }

}
