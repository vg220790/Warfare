package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.afekawar.bl.base.Entities.BaseEntities.MissileDestructor;
import com.afekawar.bl.base.Entities.BaseEntities.MissileLauncher;
import com.afekawar.bl.base.Entities.BaseEntities.MissileLauncherDestructor;

public class DBManager {
	
	private MySQLDB_Connection sqldb_connection;
	private Queryable connection;
	
	private HashMap<String,DBInfo_MissileLauncher> db_MissileLaunchers;
	private HashMap<String,DBInfo_MissileDestructor> db_MissileDestructors;
	private HashMap<String,DBInfo_MissileLauncherDestructor> db_MissileLauncherDestructors;
	
	private HashMap<String, DBLaunch_Info> db_launches;
	private ArrayList<DBDestructionAttempt_Info> db_destruction_attempts;
	private HashMap<Integer,DBHits_Info> db_hits;
	

	private static DBManager dbManager_instance = null; 
	
    private DBManager() { 
    	////true for sql false for mongo
    	if(chooseDB()) {
    		connection = new MySQLDB_Connection(); 
    	}
    	else {
    		connection = new MongoDB_Connection();
    	}
    	
    	db_MissileLaunchers = new HashMap<String,DBInfo_MissileLauncher>();
    	db_MissileDestructors = new HashMap<String,DBInfo_MissileDestructor>();
    	db_MissileLauncherDestructors= new HashMap<String,DBInfo_MissileLauncherDestructor>();
    	
    	db_launches = new HashMap<String, DBLaunch_Info>();
    	db_destruction_attempts = new ArrayList<DBDestructionAttempt_Info>();
    	db_hits = new HashMap<Integer, DBHits_Info>();
    	} 
    
    public static DBManager getInstance() { 
        if (dbManager_instance == null) 
        	dbManager_instance = new DBManager(); 
        return dbManager_instance; 
    } 
    
    public Queryable getConnection() { return this.connection; }
	
    ////////////// collection getters /////////////////////////////////////////
    public HashMap<String, DBInfo_MissileLauncher> getDb_MissileLaunchers() {
		return db_MissileLaunchers;
	}
    public HashMap<String, DBInfo_MissileDestructor> getDB_MissileDestructors() {
		return db_MissileDestructors;
	}
    public HashMap<String, DBInfo_MissileLauncherDestructor> getDb_MissileLauncherDestructor() {
		return db_MissileLauncherDestructors;
	}
    
    public HashMap<String, DBLaunch_Info> getDb_Launch_Info() {
		return db_launches;
	}
    
    public ArrayList<DBDestructionAttempt_Info> getDB_DestructionAttempt_Info(){
    	return db_destruction_attempts;
    }
    public HashMap<Integer, DBHits_Info> getDBHits_Info(){
    	return db_hits;
    }
    
    //////////////collection adders /////////////////////////////////////////
	public void addNewDb_MissileLauncher(MissileLauncher ref) {
		if (!this.db_MissileLaunchers.containsKey(ref.getId()))
			this.db_MissileLaunchers.put(ref.getId(), new DBInfo_MissileLauncher(ref));
	}
	public void addNewDb_MissileDestructor(MissileDestructor ref) {
		if (!this.db_MissileDestructors.containsKey(ref.getId()))
			this.db_MissileDestructors.put(ref.getId(), new DBInfo_MissileDestructor(ref));
	}
	public void addNewDb_MissileLauncherDestructor(MissileLauncherDestructor ref) {
		if (!this.db_MissileLauncherDestructors.containsKey(ref.getId()))
			this.db_MissileLauncherDestructors.put(ref.getId(), new DBInfo_MissileLauncherDestructor(ref));
	}
	
	public void addNewDBHit_Info(DBDestructionAttempt_Info destruction_attempt) {
		db_hits.put(destruction_attempt.getCount(), new DBHits_Info(
				destruction_attempt.getCount(), 
				destruction_attempt.getWeapon_id(), 
				destruction_attempt.getWeapon_type(), 
				destruction_attempt.getTarget_id(), 
				destruction_attempt.getTarget_type(), 
				destruction_attempt.getStart_time(), 
				destruction_attempt.getEnd_time()));
	}
	///////////// table update methods ////////////////////////////////////////
	
	public void updateAllDBTables() {
		updateMissileLaunchersDBTable();
		updateMissileDestructorsDBTable();
		updateMissileLauncherDestructorsDBTable();
		
		updateLaunchesDBTable();
		updateDestructionAttemptsDBTable();
		updateHitsDBTable();
	}
	public void updateMissileLaunchersDBTable() {
		for (DBInfo_MissileLauncher launcher: db_MissileLaunchers.values()) {
			try {
				for (DBLaunch_Info launch: launcher.getAllLaunches()) {
					if(!db_launches.containsKey(launch.getMissile_id()))
						db_launches.put(launch.getMissile_id(), launch);
				}
				connection.updateMissileLauncherInTable(launcher);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void updateMissileDestructorsDBTable() {
		for (DBInfo_MissileDestructor destructor: db_MissileDestructors.values()) {
			try {
				for (DBDestructionAttempt_Info destruction_attempt: destructor.getAllDestructionAttempts()) {
					if (!db_destruction_attempts.contains(destruction_attempt))
						db_destruction_attempts.add(destruction_attempt);
				}
				connection.updateMissileDestructorInTable(destructor);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void updateMissileLauncherDestructorsDBTable() {
		for (DBInfo_MissileLauncherDestructor mldestructor: db_MissileLauncherDestructors.values()) {
			try {
				for (DBDestructionAttempt_Info destruction_attempt: mldestructor.getAllDestructionAttempts()) {
					if (!db_destruction_attempts.contains(destruction_attempt))
						db_destruction_attempts.add(destruction_attempt);
				}
				connection.updateMissileLauncherDestructorInTable(mldestructor);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void updateLaunchesDBTable() {
		for (DBLaunch_Info launch: db_launches.values()) {
			try {
				connection.updateLaunchesInTable(launch);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void updateDestructionAttemptsDBTable() {
		for (DBDestructionAttempt_Info destruction_attempt :db_destruction_attempts) {
			if (!db_hits.containsKey(destruction_attempt.getCount())) {
				addNewDBHit_Info(destruction_attempt);
			}
			try {
				connection.updateDestructionAttemptsInTable(destruction_attempt);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void updateHitsDBTable() {
		for (DBHits_Info hit: db_hits.values()) {
			try {
				connection.updateHitsInTable(hit);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	////////////// table initializers ////////////////////////////////////////
	
	public void startDBTables() {
		clearAllTables();
		startMissileLaunchersDBTable();
		startMissileDestructorsDBTable();
		startMissileLauncherDestructorsDBTable();
	}
	
	public void startMissileLaunchersDBTable() {
		for (DBInfo_MissileLauncher laucher: db_MissileLaunchers.values()) {
			connection.addMissileLauncherToTable(laucher);
		}
		
	}
	public void startMissileDestructorsDBTable() {
		for (DBInfo_MissileDestructor destructor: db_MissileDestructors.values()) {
			connection.addMissileDestructorToTable(destructor);
		}
		
	}
	public void startMissileLauncherDestructorsDBTable() {
		for (DBInfo_MissileLauncherDestructor launcherDestructor: db_MissileLauncherDestructors.values()) {
			connection.addMissileLauncherDestructorToTable(launcherDestructor);
		}
		
	}
	
	public void clearAllTables() {
		connection.clearAllTables();
		connection.showAllCollections();
	}
	
	public boolean chooseDB() {//true for sql false for mongo
		Scanner scanner = new Scanner(System.in);
		String input="";
		int choise = 0;
        while(choise != 1 && choise != 2) {
            while (!isNumeric(input)) {
                System.out.println("Choose which Database you would like to use? 1 - MySQL, 2 - MongoDB");
                input = scanner.next();
            }
            choise = Integer.parseInt(input);
            if(choise != 1 && choise != 2)
            	input = "";
        }
        if (choise == 1)
        	return true;
		return false;
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
