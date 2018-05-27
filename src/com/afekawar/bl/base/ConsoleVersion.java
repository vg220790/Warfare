package com.afekawar.bl.base;

import com.afekawar.bl.base.Interface.Time.MyTime;
import com.afekawar.bl.base.Interface.Time.SystemTime;
import com.afekawar.bl.base.Interface.InterfaceImp;
import com.afekawar.bl.base.Interface.SystemInterface;
import com.afekawar.bl.base.JSONParser.ReadConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ConsoleVersion implements Runnable{
    public Map<String,Runnable> entities = new HashMap<>();
    private Map<String,Thread> threads = new HashMap<>();
    private SystemTime time;


    public ConsoleVersion(SystemTime time){
        this.time = time;
    }

    @Override
    public void run() {
        System.out.println("System starts");
        entities = new HashMap<>();
        threads = new HashMap<>();

        SystemInterface data = new InterfaceImp(entities,threads);





        ReadConfiguration.loadConfig(data,time);                                   // Load data from config file.

        for(Thread th : threads.values()){
            try {
                th.join();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }


        System.out.println("System Halts");
    }
}
