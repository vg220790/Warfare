package com.afekawar.bl.base;

import GraphicsContent.WarApplication;
import UI.JSONParser.WarParser;
import com.afekawar.bl.base.Entities.*;
import com.afekawar.bl.base.Interface.Time.SystemTime;
import com.afekawar.bl.base.Controller.ObjectController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainLogic implements Runnable{

    private SystemTime time;
    private WarApplication app;
    private WarParser parsedElement;
    private Map<String,MissileLauncher> launchers;
    private Map<String,MissileLauncherDestructor> launcherDestructors;
    private Map<String,MissileDestructor> missileDestructors;
    private List<Thread> threads;
    private ObjectController objectController;                             // To create actual objects based on parsed element;


    public MainLogic(SystemTime time, WarApplication app, WarParser parsedElement){
        this.time = time;
        this.app = app;
        this.parsedElement = parsedElement;
        launchers = new HashMap<>();
        launcherDestructors = new HashMap<>();
        missileDestructors = new HashMap<>();
        threads = new ArrayList<>();
        objectController = new ObjectController();
    }

    @Override
    public void run() {
        System.out.println("System starts");

        objectController.createObjects(parsedElement,launchers,launcherDestructors,missileDestructors,time);               // Create Entities.


        for(MissileLauncher launcher : launchers.values()){
            launcher.addWarEventListener(app);                                    // Add Listener to be able to notify the graphics application.
            Thread th = new Thread(launcher);
            th.setName(launcher.getId());
            th.start();
            threads.add(th);                                                      // Create Thread and save it's reference.
        }
        for(MissileLauncherDestructor launcherDestructor : launcherDestructors.values()){
            launcherDestructor.addWarEventListener(app);
            Thread th = new Thread(launcherDestructor);
            th.setName(launcherDestructor.getId());
            th.start();
            threads.add(th);
        }
        for(MissileDestructor missileDestructor : missileDestructors.values()){
            missileDestructor.addWarEventListener(app);
            Thread th = new Thread(missileDestructor);
            th.setName(missileDestructor.getId());
            th.start();
            threads.add(th);
        }


        for(Thread th : threads){
            try {
                th.join();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
       }


        System.out.println("System Halts");
    }
}
