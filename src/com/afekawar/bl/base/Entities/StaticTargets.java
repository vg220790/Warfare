package com.afekawar.bl.base.Entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StaticTargets {
   private List<Target> targets;

    public StaticTargets(){
        targets = new ArrayList<>();

        Target beerSheva = new Target("Beer-Sheva", 1225,560);
        Target ofakim = new Target("Ofakim",985,456);
        Target sderot = new Target("Sderot",957,100);
        Target netivot = new Target("Netivot", 940,270);
        Target rahat = new Target("Rahat", 1180,322);

        targets.add(beerSheva);
        targets.add(ofakim);
        targets.add(sderot);
        targets.add(netivot);
        targets.add(rahat);
    }



    public Target getTargetByName(String name){
        Iterator<Target> it = targets.iterator();
        while(it.hasNext()){
            Target tmp = it.next();
            if(tmp.getName().equals(name))
                    return tmp;
        }
        return null;
    }
}
