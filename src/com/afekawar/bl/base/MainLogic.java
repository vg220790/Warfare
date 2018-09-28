package com.afekawar.bl.base;

// Max Golotsvan - 314148123
// Maksim Lyashenko - 317914877

import GraphicsContent.GraphicsApplication;
import Logging.MyLogger;
import Server.Server;
import SharedInterface.WarInterface;
import javafx.geometry.Point2D;

import com.afekawar.bl.base.Entities.BaseEntities.*;
import com.afekawar.bl.base.Interface.Communication.MissileEventListener;
import com.afekawar.bl.base.Interface.Time.SystemTime;

import Database.DBManager;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.logging.Handler;

public class MainLogic implements Runnable{

    private WarInterface warInterface;
    private SystemTime time;
    private GraphicsApplication app;
    private Map<String,WarEntity> entities;
    private Map<String,WarEntity> missiles;
    private List<Thread> threads;
    private boolean warRunning;
    private Set<MissileEventListener> missileEventListeners;
    private MyLogger logger;
    private Statistics statistics;

    ////////////////////// 
    // added DB Manager //
    //////////////////////
    private DBManager dbManager;
    
    //////////////////
    // added Server //
    //////////////////
    private static Server server_instance = null; 
	ServerSocket server = null;
	Socket socket = null;
	DataInputStream inputStream;
	PrintStream outputStream;
	String line = ""; 

    public MainLogic(SystemTime time, GraphicsApplication app, WarInterface warInterface){
        this.missiles = new HashMap<>();
        this.missileEventListeners = new HashSet<>();
        this.time = time;
        this.app = app;
        this.warInterface = warInterface;
        this.warInterface.setMainProgram(this);
        entities = new HashMap<>();
        threads = new ArrayList<>();
        logger = new MyLogger("SystemLog");
        logger.addHandler("SystemLog.txt",getClass().getName());
        statistics = new Statistics();
        /////////////////////////////////////
        dbManager = DBManager.getInstance();
        /////////////////////////////////////
        try {
			server = new ServerSocket(7000);
		} catch (IOException e) {
			e.printStackTrace();
		}
        /////////////////////////////////////

    }

    @Override
    public void run() {
        logger.info("System starts");
        
        warRunning = true;
        // Create Entities.

        createObjects(warInterface,entities,missiles);

        for(WarEntity entity : missiles.values()){
            ((Missile)entity).setTargetCoordinates(warInterface.getTargetByName(((Missile)entity).getDestination()).getCoordinates());
            entity.setTime(time);
            entity.startWar();
        }

        for(WarEntity entity : entities.values()){
            entity.init(warInterface);            // Init some static variables after parsing from JSON file...
            entity.startWar();

            addWarEntity(entity);
        }
        openServer();
        dbManager.updateAllDBTables();

        while(warRunning){
            try {
            	dbManager.updateAllDBTables();
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



        for(WarEntity entity : entities.values()){
           entity.stopThread();
        }
        for(Thread th : threads){
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        dbManager.updateAllDBTables();
        dbManager.getConnection().showAllCollections();
        dbManager.getConnection().closeDB();
        logger.info("System Halts");
        for (Handler handler : logger.getMyFileHandlers().values())
            handler.close();

        System.exit(0);
    }

    public void haltSystem(){
        warRunning = false;

    }

    public void addWarEntity(WarEntity entity){
        entity.setStatistics(statistics);
        entity.setLogger(logger);
        entity.setTime(time);
        entity.addWarEventListener(app);
        if(entity instanceof MissileLauncher)
            ((MissileLauncher) entity).setMissileEventListeners(missileEventListeners);
        if(entity instanceof MissileDestructor)
            missileEventListeners.add((MissileDestructor)entity);

        if(!entities.containsKey(entity.getId()))
            entities.put(entity.getId(),entity);
        Thread th = new Thread(entity);
        th.setName(entity.getId());
        threads.add(th);
        th.start();
    }

    public void addMissileEntity(WarEntity missile){
        missiles.put(missile.getId(),missile);
    }

    public void addDestLauncherCommand(String destructorId, int destTime, String launcherId){
        ((MissileLauncherDestructor)entities.get(destructorId)).addDestructedLauncher(destTime,entities.get(launcherId));
    }
    public void addDestMissileCommand(String destructorId, int destTime, String missileId){
        ((MissileDestructor)entities.get(destructorId)).addTargetMissile(destTime,missiles.get(missileId));
    }

    private synchronized void createObjects(WarInterface parsedElement, Map<String,WarEntity> entities,Map<String,WarEntity> missiles) {
        for (MissileLauncher mLauncher : parsedElement.getMissileLaunchers()) {
            for (Missile missile : mLauncher.getMissiles()) {
                missile.setCoordinates(mLauncher.getCoordinates());
                missiles.put(missile.getId(),missile);
            }
            entities.put(mLauncher.getId(), mLauncher);
            dbManager.addNewDb_MissileLauncher(mLauncher);
        }

        for (MissileLauncherDestructor mLDestructor : parsedElement.getMissileLauncherDestructors()) {

            entities.put(mLDestructor.getId(), mLDestructor);
            dbManager.addNewDb_MissileLauncherDestructor(mLDestructor);
        }

        for (MissileDestructor mDestructor : parsedElement.getMissileDestructors()) {

            entities.put(mDestructor.getId(), mDestructor);
            dbManager.addNewDb_MissileDestructor(mDestructor);
        }
        
        dbManager.startDBTables();
    }

    public void openServer() {
    	try {
			System.out.println("SERVER: " + new Date() + " --> Server waits for client...");
			socket = server.accept(); // blocking
			System.out.println("SERVER: " + new Date() + " --> Client connected from "
					+ socket.getInetAddress() + ":" + socket.getPort());
			inputStream = new DataInputStream(socket.getInputStream());
			outputStream = new PrintStream(socket.getOutputStream());
			
			while (!line.equals("goodbye")) {
				
				outputStream.println("SERVER: " + "Welcome to server! Add missile launcher - '1', Launch missile - '2', Intercept missile - '3', Leave - 'goodbye'");
				line = inputStream.readLine();
				System.out.println("SERVER: " + new Date() + " --> Recieved from client: choice #" + line);
				
				switch(line) {
				
					//adding missile launcher
					case "1":
						outputStream.println("SERVER: Enter launcher's Id: ONLY NUMBERS ALLOWED");
						line = inputStream.readLine();
						System.out.println("SERVER: " + new Date() + " --> Recieved from client: id#" + line);
						
						while(!isNumeric(line)) {
							outputStream.println("SERVER: Enter a valid launcher's Id: ONLY NUMBERS ALLOWED");
							line = inputStream.readLine();
							System.out.println("SERVER: " + new Date() + " --> Recieved from client: id#" + line);
						}
						String launcher_id = "L"+line;
						outputStream.println("SERVER: is Hidden? Type 'yes' for hidden, anything else for not hidden");
						line = inputStream.readLine();
						System.out.println("SERVER: " + new Date() + " --> Recieved from client: isHidden = " + line);
						
						boolean isHidden = line.equals("yes");
						MissileLauncher temp = new MissileLauncher(launcher_id, isHidden, null);
						
						boolean success = warInterface.addMissileLauncher(temp);
						if (!success) {
							outputStream.println("SERVER: Couldn't add launcher! Launcher exist/wrong fields!!! Press any key to continue...");
						}else {
							outputStream.println("SERVER: Successfuly added launcher! Press any key to continue...");
						}
						line = inputStream.readLine();
						break;
						
					//launching missile	
					case "2":
						
						if (!warRunning) {
							outputStream.println("You cant do this before starting the game! Press any key to continue...");
							line = inputStream.readLine();
							System.out.println("SERVER: " + new Date() + " --> Recieved from client: id#" + line);
							break;
						}
						
						//getting the launcher details from the client
			            String launcherId = "";
			            boolean launcherExist = false;
			            outputStream.println("SERVER: Enter launcher's Id: ONLY NUMBERS ALLOWED");
						line = inputStream.readLine();
						launcherId = line;
						System.out.println("SERVER: " + new Date() + " --> Recieved from client: id#" + line);
						while(!isNumeric(line) || !launcherExist){
							
							if (!isNumeric(line)) {
								outputStream.println("SERVER: Invalid input! Launcher's Id to add missiles to: ONLY NUMBERS ALLOWED");
								line = inputStream.readLine();
								System.out.println("SERVER: " + new Date() + " --> Recieved from client: id#" + line);
							}

							launcherExist = (warInterface.getLauncherById("L"+launcherId) != null);
		                    if(!launcherExist){
		                    	outputStream.println("Launcher With ID L" + launcherId + " doesn't exists! Press any key to continue...");
		                    	line = inputStream.readLine();
		                        launcherId = "";
		                    }

		                }
		                launcherId = "L" + launcherId;
		                
		                //getting the missile details from the client
		                
		                //getting missile id
		                outputStream.println("Enter Missile Id: ONLY NUMBERS ALLOWED!!");
		                line = inputStream.readLine();
		                System.out.println("SERVER: " + new Date() + " --> Recieved from client: missile id#" + line);

	                    while(!isNumeric(line)){
	                    	outputStream.println("Invalid input! Enter Missile Id: ONLY NUMBERS ALLOWED!!");
	                    	 line = inputStream.readLine();
	                    	 System.out.println("SERVER: " + new Date() + " --> Recieved from client: missile id#" + line);

	                    }
	                    String mId = line;
	                    mId = "M" + mId;
	                    
	                    //getting missile destination
	                    outputStream.println("Enter Missile destination: type b/n/r/o/s = Beer-Sheva/Netivot/Rahat/Ofakim/Sderot");
	                    line = inputStream.readLine();
                   	 	System.out.println("SERVER: " + new Date() + " --> Recieved from client: missile id#" + line);
	                   
	                    while(!line.equals("b") && !line.equals("n") && !line.equals("r") && !line.equals("o") && !line.equals("s")){
	                    	outputStream.println("Enter Missile destination: type b/n/r/o/s = Beer-Sheva/Netivot/Rahat/Ofakim/Sderot");
	                    	line = inputStream.readLine();
	                    	System.out.println("SERVER: " + new Date() + " --> Recieved from client: missile id#" + line);
	                    }
	                    String destination = line;
	                    switch (destination){
	                        case "b":
	                            destination = "Beer-Sheva";
	                            break;
	                        case "n":
	                            destination = "Netivot";
	                            break;
	                        case "r":
	                            destination = "Rahat";
	                            break;
	                        case "o":
	                            destination = "Ofakim";
	                            break;
	                        case "s":
	                            destination = "Sderot";
	                            break;
	                    }
	                    
	                    //getting missile launch time
	                    outputStream.println("Missile launch time: ONLY NUMBERS ALLOWED!!");
	                    line = inputStream.readLine();
                    	System.out.println("SERVER: " + new Date() + " --> Recieved from client: " + line);
	                    
	                    while(!isNumeric(line)) {
	                    	outputStream.println("Invalid input! Missile launch time: ONLY NUMBERS ALLOWED!!");
	                        line = inputStream.readLine();
	                    	System.out.println("SERVER: " + new Date() + " --> Recieved from client: " + line);
	                    }
	                    String launchTimeStr = line;
	                    
	                    //getting missile fly time
	                    outputStream.println("Missile fly time: ONLY NUMBERS ALLOWED!!");
	                    line = inputStream.readLine();
                    	System.out.println("SERVER: " + new Date() + " --> Recieved from client: " + line);
	                    while(!isNumeric(line)) {
	                    	outputStream.println("Invalid input! Missile fly time: ONLY NUMBERS ALLOWED!!");
	                    	line = inputStream.readLine();
	                    }
	                    String flyTimeStr = line;
	                    
	                    //getting missile potential damage
	                    outputStream.println("Missile's potential damage: ONLY NUMBERS ALLOWED!!");
	                    line = inputStream.readLine();
                    	System.out.println("SERVER: " + new Date() + " --> Recieved from client: " + line);
	                    while(!isNumeric(flyTimeStr)) {
	                    	outputStream.println("Invalid input! Missile's potential damage: ONLY NUMBERS ALLOWED!!");
		                    line = inputStream.readLine();
	                    	System.out.println("SERVER: " + new Date() + " --> Recieved from client: " + line);
	                    }
	                    String damageStr = line;
	                    
	                    //setting the missile object
	                    Point2D coordinates = warInterface.getTargetByName(destination).getCoordinates();
	                    int launchTime = Integer.parseInt(launchTimeStr);
	                    int flyTime = Integer.parseInt(flyTimeStr);
	                    int damage = Integer.parseInt(damageStr);
	                    Missile tempMissile = new Missile(mId, coordinates, launchTime, flyTime, damage,time);
	                    tempMissile.setDestination(destination);
	                    tempMissile.setCoordinates(warInterface.getLauncherById(launcherId).getCoordinates());
	                    //success =  warInterface.addMissile(launcherId, tempMissile);
	                    if(warInterface.addMissile(launcherId, tempMissile)) {
	                    	outputStream.println(tempMissile.getId() + " Added!! Press any key to continue...");
		                    line = inputStream.readLine();
	                    	System.out.println("SERVER: " + new Date() + " --> Recieved from client: " + line);
	                    }
	                    else {
	                    	outputStream.println("Missile exist/wrong fields!!! Press any key to continue...");
	                    	line = inputStream.readLine();
	                    	System.out.println("SERVER: " + new Date() + " --> Recieved from client: " + line);
	                    }

						break;
						
					//intercepting a missile	
					case "3":
						
						if (!warRunning) {
							outputStream.println("You cant do this before starting the game! Press any key to continue...");
							line = inputStream.readLine();
							System.out.println("SERVER: " + new Date() + " --> Recieved from client: " + line);
							break;
						}
						
						//getting missile destructor id
						outputStream.println("SERVER: Missile Destructor Id to launch from: ONLY NUMBERS ALLOWED!!");
						line = inputStream.readLine();
						System.out.println("SERVER: " + new Date() + " --> Recieved from client: id#" + line);
	                    while(!isNumeric(line)) {
	                    	outputStream.println("SERVER: Invalid input! Missile Destructor Id to launch from: ONLY NUMBERS ALLOWED!!");
							line = inputStream.readLine();
							System.out.println("SERVER: " + new Date() + " --> Recieved from client: id#" + line);
	                    }
	                    String destructorId = line;
	                    destructorId = "MD" + destructorId;
	                    
	                    //getting missile id
	                    outputStream.println("SERVER: Missile Id to destroy: ONLY NUMBERS ALLOWED!!");
	                    line = inputStream.readLine();
						System.out.println("SERVER: " + new Date() + " --> Recieved from client: id#" + line);
	                    while(!isNumeric(line)) {
	                    	outputStream.println("SERVER: Invalid input! Missile Id to destroy: ONLY NUMBERS ALLOWED!!");
		                    line = inputStream.readLine();
							System.out.println("SERVER: " + new Date() + " --> Recieved from client: id#" + line);
	                    }
	                    String destId = line;
	                    destId = "M" + destId;
	                    
	                    outputStream.println("Destruct Time After Launch: ONLY NUMBERS ALLOWED!!");
	                    line = inputStream.readLine();
						System.out.println("SERVER: " + new Date() + " --> Recieved from client: Destruct Time After Launch = " + line);
	                    while(!isNumeric(line)) {
	                    	outputStream.println("SERVER: Invalid input! Destruct Time After Launch: ONLY NUMBERS ALLOWED!!");
	                    	line = inputStream.readLine();
							System.out.println("SERVER: " + new Date() + " --> Recieved from client: Destruct Time After Launch = " + line);
	                    }
	                    String destTime = line;
	                    
	                    warInterface.addDestMissile(destructorId,destId,Integer.parseInt(destTime));
	                    
	                    outputStream.println("Operation for Destructor " + destructorId + " Added!! Press any key to continue...");
	                    line = inputStream.readLine();
						System.out.println("SERVER: " + new Date() + " --> Recieved from client: " + line);
						
						break;
					case "goodbye":
						break;
				}
				
				while (!line.equals("1") && !line.equals("2") && !line.equals("3")) {
					outputStream.println(line + " isn't a valid choice! Add missile launcher - '1', Launch missile - '2', Intercept missile - '3', Leave - 'goodbye'");	
					line = inputStream.readLine();
					System.out.println("SERVER: " + new Date() + " --> Recieved from client: id#" + line);
				}
				
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			try {
				socket.close();
				server.close();
				System.out
						.println("SERVER: " + "Sever is closing after client is disconnected");
			} catch (IOException e) { }
		}
 	}

    public String getStats(){
        return statistics.toString();
    }
    
    private static boolean isNumeric(String str)
    {
        try
        {
            Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}
