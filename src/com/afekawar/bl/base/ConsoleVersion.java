package com.afekawar.bl.base;

import com.afekawar.bl.base.Interface.InterfaceImp;
import com.afekawar.bl.base.JSONParser.ReadConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ConsoleVersion implements Runnable{
    public Map<String,Runnable> entities = new HashMap<>();
    public Map<String,Thread> threads = new HashMap<>();

    
    @Override
    public void run() {
        System.out.println("System starts");

        entities = new HashMap<>();
        threads = new HashMap<>();

        InterfaceImp data = new InterfaceImp(entities,threads);





        ReadConfiguration.loadConfig(data);                                   // Load data from config file.

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
