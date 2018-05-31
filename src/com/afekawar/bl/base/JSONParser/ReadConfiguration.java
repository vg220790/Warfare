package com.afekawar.bl.base.JSONParser;

import GraphicsContent.WarApplication;
import com.afekawar.bl.base.Entities.*;
import com.afekawar.bl.base.Interface.SystemInterface;
import com.afekawar.bl.base.Interface.Time.SystemTime;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class ReadConfiguration {


    public static void loadConfig(SystemInterface data, SystemTime time, WarApplication app, File configuration) {


        System.out.println("Data Load Starts..");
        StaticTargets targets = new StaticTargets();

        try {
            Map<String, Object> war = new Gson().fromJson(new JsonReader(new FileReader(configuration)), Map.class);
            System.out.println(war);


            if (war.containsKey("war")) {
                Map<String, Object> initialConfiguration = (Map) war.get("war");
                if (initialConfiguration.containsKey("missileLaunchers")) {
                    Map<String, Object> missileLaunchers = (Map) initialConfiguration.get("missileLaunchers");
                    if (missileLaunchers.containsKey("launcher")) {
                        ArrayList<Object> arr = (ArrayList) missileLaunchers.get("launcher");
                        for (int i = 0; i < arr.size(); i++) {
                            String id = (String) ((Map) arr.get(i)).get("id");
                            String hidden = (String) ((Map) arr.get(i)).get("isHidden");
                            boolean isHidden;
                            if (hidden.equals("false"))
                                isHidden = false;
                            else
                                isHidden = true;
                            Queue<Missile> missiles = new PriorityQueue<>();

                            List<Object> mis = (List) ((Map) arr.get(i)).get("missile");
                            for (int j = 0; j < mis.size(); j++) {
                                String missileId = (String) ((Map) mis.get(j)).get("id");
                                String destination = (String) ((Map) mis.get(j)).get("destination");



                                Target target = targets.getTargetByName(destination);



                                int launchTime = Integer.parseInt((String) ((Map) mis.get(j)).get("launchTime"));
                                int flyTime = Integer.parseInt((String) ((Map) mis.get(j)).get("flyTime"));
                                int damage = Integer.parseInt((String) ((Map) mis.get(j)).get("damage"));

                                Missile missile = new Missile(missileId, target.getCoordinates(), launchTime, flyTime, damage,id,time);
                              //  data.addMissile(missileId, missile);
                                missiles.offer(missile);
                            }

                            MissileLauncher missileLauncher = new MissileLauncher(id, isHidden,time);
                            missileLauncher.addWarEventListener(app);
                            missileLauncher.setMissiles(missiles);
                            data.addMissileLauncher(id, missileLauncher);






                        }
                    }

                }
                if (initialConfiguration.containsKey("missileDestructors")) {
                    Map<String, Object> ob = (Map) initialConfiguration.get("missileDestructors");
                    if (ob.containsKey("destructor")) {
                        ArrayList<Object> arr = (ArrayList) ob.get("destructor");
                        for (int i = 0; i < arr.size(); i++) {
                            String id = (String) ((Map) arr.get(i)).get("id");

                            MissileDestructor destructor = new MissileDestructor(id,data,time);
                            List<Object> mis = (List) ((Map) arr.get(i)).get("destructdMissile");
                            for (int j = 0; j < mis.size(); j++) {
                                String missileId = (String) ((Map) mis.get(j)).get("id");
                                int destructAfterLaunch = Integer.parseInt((String) ((Map) mis.get(j)).get("destructAfterLaunch"));

                                destructor.addTargetMissile(destructAfterLaunch, (Missile)data.getEntityById(missileId));
                            }
                            destructor.addWarEventListener(app);
                            data.addMissileDestructor(id, destructor);
                        }
                    }
                }
                if (initialConfiguration.containsKey("missileLauncherDestructors")) {
                    Map<String, Object> ob = (Map) initialConfiguration.get("missileLauncherDestructors");
                    if (ob.containsKey("destructor")) {
                        ArrayList<Object> arr = (ArrayList) ob.get("destructor");
                        for (int i = 0; i < arr.size(); i++) {
                            String type = (String) ((Map) arr.get(i)).get("type");

                            MissileLauncherDestructor missileLauncherDestructor = new MissileLauncherDestructor(type, data,time);

                            List<Object> mis = (List) ((Map) arr.get(i)).get("destructedLanucher");
                            for (int j = 0; j < mis.size(); j++) {
                                String launcherId = (String) ((Map) mis.get(j)).get("id");
                                int destructTime = Integer.parseInt((String) ((Map) mis.get(j)).get("destructTime"));

                                missileLauncherDestructor.addDestructedLauncher(destructTime, (MissileLauncher)data.getEntityById(launcherId));

                            }
                            missileLauncherDestructor.addWarEventListener(app);
                            data.addMissileLauncherDestructor(missileLauncherDestructor.getId(), missileLauncherDestructor);


                        }
                    }
                }
            }

            System.out.println("Data Load Complete..");


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

// TODO - Fix a bug where the parser does read only arrays, and not single objects ( currently I've changed the provided .json to make it work).