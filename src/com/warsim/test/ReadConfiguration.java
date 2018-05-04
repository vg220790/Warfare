package com.warsim.test;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadConfiguration {
    public static HashMap<String, MissileLauncher> missileLaunchersDB;
    public static HashMap<String, Missile> missilesDB;
    public static HashMap<String, MissileLauncherDestructor> missileLauncherDestructorsDB;
    public static HashMap<String, MissileDestructor> missileDestructorsDB;

    public static void LoadConfig() {
        missileLauncherDestructorsDB = new HashMap<>();
        missilesDB = new HashMap<>();
        missileLaunchersDB = new HashMap<>();
        missileDestructorsDB = new HashMap<>();

        System.out.println("System Starts..");


        try {
            Map<String, Object> war = new Gson().fromJson(new JsonReader(new FileReader("Configuration.json")), Map.class);
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
                            List<Missile> missiles = new ArrayList<>();
                            List<Object> mis = (List) ((Map) arr.get(i)).get("missile");
                            for (int j = 0; j < mis.size(); j++) {
                                String missileId = (String) ((Map) mis.get(j)).get("id");
                                String destination = (String) ((Map) mis.get(j)).get("destination");

                                int launchTime = Integer.parseInt((String) ((Map) mis.get(j)).get("launchTime"));
                                int flyTime = Integer.parseInt((String) ((Map) mis.get(j)).get("flyTime"));
                                int damage = Integer.parseInt((String) ((Map) mis.get(j)).get("damage"));

                                Missile missile = new Missile(missileId, destination, launchTime, flyTime, damage);
                                missilesDB.put(missileId, missile);
                                missiles.add(missile);
                            }

                            MissileLauncher missileLauncher = new MissileLauncher(id, isHidden);
                            missileLauncher.addMissiles(missiles);
                            missileLaunchersDB.put(id, missileLauncher);
                            System.out.println(missileLauncher);


                        }
                    }

                }
                if (initialConfiguration.containsKey("missileDestructors")) {
                    Map<String, Object> ob = (Map) initialConfiguration.get("missileDestructors");
                    if (ob.containsKey("destructor")) {
                        ArrayList<Object> arr = (ArrayList) ob.get("destructor");
                        for (int i = 0; i < arr.size(); i++) {
                            String id = (String) ((Map) arr.get(i)).get("id");

                            MissileDestructor destructor = new MissileDestructor(id);
                            List<Object> mis = (List) ((Map) arr.get(i)).get("destructdMissile");
                            for (int j = 0; j < mis.size(); j++) {
                                String missileId = (String) ((Map) mis.get(j)).get("id");
                                int destructAfterLaunch = Integer.parseInt((String) ((Map) mis.get(j)).get("destructAfterLaunch"));

                                destructor.AddDestructMissle(missileId, destructAfterLaunch);
                                missileDestructorsDB.put(id, destructor);
                            }
                        }
                    }
                }
                if (initialConfiguration.containsKey("missileLauncherDestructors")) {
                    Map<String, Object> ob = (Map) initialConfiguration.get("missileLauncherDestructors");
                    if (ob.containsKey("destructor")) {
                        ArrayList<Object> arr = (ArrayList) ob.get("destructor");
                        for (int i = 0; i < arr.size(); i++) {
                            String type = (String) ((Map) arr.get(i)).get("type");

                            MissileLauncherDestructor missileLauncherDestructor;
                            if (type.equals("plane")) {
                                missileLauncherDestructor = new Airplane();
                            } else {
                                missileLauncherDestructor = new Ship();
                            }


                            List<Object> mis = (List) ((Map) arr.get(i)).get("destructedLanucher");
                            for (int j = 0; j < mis.size(); j++) {
                                String launcherId = (String) ((Map) mis.get(j)).get("id");
                                int destructTime = Integer.parseInt((String) ((Map) mis.get(j)).get("destructTime"));

                                missileLauncherDestructor.AddDestructedLauncher(launcherId, destructTime);
                                missileLauncherDestructorsDB.put(missileLauncherDestructor.getId(), missileLauncherDestructor);
                            }


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