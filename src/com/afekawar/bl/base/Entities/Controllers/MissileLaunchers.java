package com.afekawar.bl.base.Entities.Controllers;



import com.afekawar.bl.base.Entities.BaseEntities.Missile;
import com.afekawar.bl.base.Entities.BaseEntities.MissileLauncher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MissileLaunchers {
    private Set<MissileLauncher> launcher;

    public MissileLaunchers(){
        this.launcher = new HashSet<>();
    }

    public MissileLaunchers(List<MissileLauncher> launcher) {
        this.launcher = new HashSet<>(launcher);
    }

    public List<MissileLauncher> getLauncher() {
        List<MissileLauncher> launcherList = new ArrayList<>();
        for(MissileLauncher missileLauncher : launcher){
            if(missileLauncher.getAlive())
                launcherList.add(missileLauncher);
        }
        return launcherList;
    }

    public boolean addMissileLauncher(MissileLauncher temp){
        int prevSize = launcher.size();
        launcher.add(temp);
        return !(launcher.size() == prevSize);
    }

    public boolean addMissile(String launcherId, Missile temp){
        for(MissileLauncher mLauncher : launcher ){
            if(mLauncher.getId().equals(launcherId)){
                return mLauncher.addMissile(temp);         // TODO boolean
            }
        }
        return false;
    }

    public List<Missile> getAllMissiles(){
        List<Missile> temp = new ArrayList<>();
        for(MissileLauncher ml : launcher){
            List<Missile> tempList = new ArrayList<>(ml.getMissiles());
             temp.addAll(tempList);      // TODO get all missiles from launcher
        }
        return temp;
    }
}



