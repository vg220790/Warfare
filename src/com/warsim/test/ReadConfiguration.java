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
    public static HashMap<String, MissleLauncher> missleLaunchersDB;
    public static HashMap<String, Missle> misslesDB;
    public static HashMap<String, MissleLauncherDestructor> missleLauncherDestructorsDB;
    public static HashMap<String, MissleDestructor> missleDestructorsDB;

    public static void LoadConfig() {
        missleLauncherDestructorsDB = new HashMap<>();
        misslesDB = new HashMap<>();
        missleLaunchersDB = new HashMap<>();
        missleDestructorsDB = new HashMap<>();

        System.out.println("System Starts..");


        try {
            Map<String, Object> war = new Gson().fromJson(new JsonReader(new FileReader("Configuration.json")), Map.class);
            System.out.println(war);


            if (war.containsKey("war")) {
                Map<String, Object> initialConfiguration = (Map) war.get("war");
                if (initialConfiguration.containsKey("missleLaunchers")) {
                    Map<String, Object> missleLaunchers = (Map) initialConfiguration.get("missleLaunchers");
                    if (missleLaunchers.containsKey("launcher")) {
                        ArrayList<Object> arr = (ArrayList) missleLaunchers.get("launcher");
                        for (int i = 0; i < arr.size(); i++) {
                            String id = (String) ((Map) arr.get(i)).get("id");
                            String hidden = (String) ((Map) arr.get(i)).get("isHidden");
                            boolean isHidden;
                            if (hidden.equals("false"))
                                isHidden = false;
                            else
                                isHidden = true;
                            List<Missle> missles = new ArrayList<>();
                            List<Object> mis = (List) ((Map) arr.get(i)).get("missle");
                            for (int j = 0; j < mis.size(); j++) {
                                String missleId = (String) ((Map) mis.get(j)).get("id");
                                String destination = (String) ((Map) mis.get(j)).get("destination");

                                int launchTime = Integer.parseInt((String) ((Map) mis.get(j)).get("launchTime"));
                                int flyTime = Integer.parseInt((String) ((Map) mis.get(j)).get("flyTime"));
                                int damage = Integer.parseInt((String) ((Map) mis.get(j)).get("damage"));

                                Missle missle = new Missle(missleId, destination, launchTime, flyTime, damage);
                                misslesDB.put(missleId, missle);
                                missles.add(missle);
                            }

                            MissleLauncher missleLauncher = new MissleLauncher(id, isHidden);
                            missleLauncher.addMIssles(missles);
                            missleLaunchersDB.put(id, missleLauncher);
                            System.out.println(missleLauncher);


                        }
                    }

                }
                if (initialConfiguration.containsKey("missleDestructors")) {
                    Map<String, Object> ob = (Map) initialConfiguration.get("missleDestructors");
                    if (ob.containsKey("destructor")) {
                        ArrayList<Object> arr = (ArrayList) ob.get("destructor");
                        for (int i = 0; i < arr.size(); i++) {
                            String id = (String) ((Map) arr.get(i)).get("id");

                            MissleDestructor destructor = new MissleDestructor(id);
                            List<Object> mis = (List) ((Map) arr.get(i)).get("destructedMissle");
                            for (int j = 0; j < mis.size(); j++) {
                                String missleId = (String) ((Map) mis.get(j)).get("id");
                                int destructAfterLaunch = Integer.parseInt((String) ((Map) mis.get(j)).get("destructAfterLaunch"));

                                destructor.AddDestructMissle(missleId, destructAfterLaunch);
                                missleDestructorsDB.put(id, destructor);
                            }
                        }
                    }
                }
                if (initialConfiguration.containsKey("missleLauncherDestructors")) {
                    Map<String, Object> ob = (Map) initialConfiguration.get("missleLauncherDestructors");
                    if (ob.containsKey("destructor")) {
                        ArrayList<Object> arr = (ArrayList) ob.get("destructor");
                        for (int i = 0; i < arr.size(); i++) {
                            String type = (String) ((Map) arr.get(i)).get("type");

                            MissleLauncherDestructor missleLauncherDestructor;
                            if (type.equals("plane")) {
                                missleLauncherDestructor = new Airplane();
                            } else {
                                missleLauncherDestructor = new Ship();
                            }


                            List<Object> mis = (List) ((Map) arr.get(i)).get("destructedLauncher");
                            for (int j = 0; j < mis.size(); j++) {
                                String launcherId = (String) ((Map) mis.get(j)).get("id");
                                int destructTime = Integer.parseInt((String) ((Map) mis.get(j)).get("destructTime"));

                                missleLauncherDestructor.AddDestructedLauncher(launcherId, destructTime);
                                missleLauncherDestructorsDB.put(missleLauncherDestructor.getId(), missleLauncherDestructor);
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