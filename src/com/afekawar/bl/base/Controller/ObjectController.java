package com.afekawar.bl.base.Controller;

import UI.JSONParser.MockEntities.BaseEntities.M;
import UI.JSONParser.MockEntities.BaseEntities.MD;
import UI.JSONParser.MockEntities.BaseEntities.ML;
import UI.JSONParser.MockEntities.BaseEntities.MLD;
import UI.JSONParser.MockEntities.BaseEntities.SubEntities.DestLauncher;
import UI.JSONParser.MockEntities.BaseEntities.SubEntities.DestMissile;
import UI.JSONParser.WarParser;
import com.afekawar.bl.base.Entities.*;
import com.afekawar.bl.base.Interface.Time.SystemTime;

import java.util.HashMap;
import java.util.Map;

public class ObjectController {                          // Gets mock objects parsed through gson, and creates real bl objects that will run as threads.

        public void createObjects(WarParser parsedElement, Map<String, MissileLauncher> launchers, Map<String, MissileLauncherDestructor> launcherDestructors, Map<String,MissileDestructor> missileDestructors, SystemTime time){
            StaticTargets targets = new StaticTargets();
            Map<String,Missile> missiles = new HashMap<>();
            for(ML mLauncher : parsedElement.getMissileLaunchers()){
                MissileLauncher newLauncher = new MissileLauncher(mLauncher.getId(),mLauncher.isHidden(),time);
                for(M missile : mLauncher.getMissile()){
                    Target tempTarget = targets.getTargetByName(missile.getDestination());
                    int launchTime = Integer.parseInt(missile.getLaunchTime());
                    int flyTime = Integer.parseInt(missile.getFlyTime());
                    int damage = Integer.parseInt(missile.getDamage());
                    Missile temp = new Missile(missile.getId(),tempTarget.getCoordinates(),launchTime,flyTime,damage,time);
                    missiles.put(temp.getId(),temp);
                    newLauncher.addMissile(temp);
                }
                launchers.put(newLauncher.getId(),newLauncher);
            }
            for(MLD mLDestructor : parsedElement.getMissileLauncherDestructors()){
                MissileLauncherDestructor newDestructor = new MissileLauncherDestructor(mLDestructor.getType(),time);
                for(DestLauncher destLauncher : mLDestructor.getDestructedLanucher()){
                    int destTime = Integer.parseInt(destLauncher.getDestructTime());
                    newDestructor.addDestructedLauncher(destTime,launchers.get(destLauncher.getId()));
                }
               launcherDestructors.put(newDestructor.getId(),newDestructor);
            }
            for(MD mDestructor : parsedElement.getMissileDestructors()){
                MissileDestructor newDestructor = new MissileDestructor(mDestructor.getId(),time);
                for(DestMissile destMissile : mDestructor.getDestructdMissile()){
                    int destAfterLaunch = Integer.parseInt(destMissile.getDestructAfterLaunch());
                    newDestructor.addTargetMissile(destAfterLaunch,missiles.get(destMissile.getId()));
                }
                missileDestructors.put(newDestructor.getId(),newDestructor);
            }

    }
}
