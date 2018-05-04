package com.warsim.test;

import java.util.ArrayList;
import java.util.List;

public class MissileLauncher {
    private String id;
    private boolean isHidden;
    private List<Missile> missiles;

    public MissileLauncher(String id, boolean isHidden){
        this.id = id;
        this.isHidden = isHidden;
        missiles = new ArrayList<>();
    }

    public void addMissiles(List<Missile> missiles){
        this.missiles = missiles;
    }

    public boolean IsHidden(){
        return isHidden;
    }

    public void addMissile(Missile m){
        missiles.add(m);
    }

}
