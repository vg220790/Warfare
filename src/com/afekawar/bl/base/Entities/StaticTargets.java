package com.afekawar.bl.base.Entities;

import java.util.ArrayList;
import java.util.List;

public class StaticTargets {
   private List<Target> targets;

    public StaticTargets(){
        targets = new ArrayList<>();

        Target beerSheva = new Target("Beer-Sheva", 1212,554);
        Target ofakim = new Target("Ofakim",985,456);
        Target sderot = new Target("Sderot",948,100);
        Target netivot = new Target("Netivot", 940,270);
        Target rahat = new Target("Rahat", 1180,322);

        targets.add(beerSheva);
        targets.add(ofakim);
        targets.add(sderot);
        targets.add(netivot);
        targets.add(rahat);
    }



    public Target getTargetByName(String name){
        for(Target target:targets) {
            if(target.getName().equals(name))
                    return target;
        }
        return null;
    }

    public List<Target> getTargets(){
        return targets;
    }
}
