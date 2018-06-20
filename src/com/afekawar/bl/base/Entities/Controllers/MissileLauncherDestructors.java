package com.afekawar.bl.base.Entities.Controllers;

import com.afekawar.bl.base.Entities.BaseEntities.MissileLauncherDestructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MissileLauncherDestructors {
    private Set<MissileLauncherDestructor> destructor;

    public MissileLauncherDestructors(){
        this.destructor = new HashSet<>();
    }



    public List<MissileLauncherDestructor> getDestructor() {
        return new ArrayList<>(destructor);
    }

    public boolean addMissileLauncherDestructor(MissileLauncherDestructor temp){
        int prevSize = destructor.size();
        destructor.add(temp);
        return !(destructor.size() == prevSize);
    }


}



