package Database;

import java.util.ArrayList;
import com.afekawar.bl.base.Entities.BaseEntities.MissileLauncherDestructor;

public class DBInfo_MissileLauncherDestructor {
	
	private String id;
	private String type;
	private int totalNumOfDestructionAttempts;
	private int numOfDestructedLaunchers;
	private ArrayList<String> launchers;
	private DBDestructionAttempt_Info current_destruction_attempt;;
	private ArrayList<DBDestructionAttempt_Info> destruction_attempts;
	
	public DBInfo_MissileLauncherDestructor(MissileLauncherDestructor ref) {
		this.id = ref.getId();
		this.type = ref.getStrType();
		this.totalNumOfDestructionAttempts=0;
		this.numOfDestructedLaunchers=0;
		this.launchers = new ArrayList<String>();
		this.destruction_attempts = new ArrayList<DBDestructionAttempt_Info>();
		ref.setInfoRef(this);
	}
	
	public String getId() {
		return id;
	}
	
	public String getType() {
		return type;
	}

	public int getTotalNumOfDestructionAttempts() {
		return totalNumOfDestructionAttempts;
	}

	public int getNumOfDestructedLaunchers() {
		return numOfDestructedLaunchers;
	}
	public ArrayList<String> getLaunchers() {
		return launchers;
	}
	
	public void incrementTotalNumOfDestructionAttempts() {
		this.totalNumOfDestructionAttempts ++;
	}
	public void incrementNumOfDestructedLaunchers() {
		this.numOfDestructedLaunchers ++;
	}

	public void incrementFiredMissiles(String newLauncher) {
		this.launchers.add(newLauncher);
	}
	public DBDestructionAttempt_Info getCurrent_destruction_attempt() {
		return current_destruction_attempt;
	}

	public ArrayList<DBDestructionAttempt_Info> getAllDestructionAttempts() {
		return destruction_attempts;
	}
	
	public void setCurrentDestructionAttempt(DBDestructionAttempt_Info newAttempt) {
		if(current_destruction_attempt!=null) {
			endCurrentDestructionAttempt();
		}
		this.current_destruction_attempt = newAttempt;
	}
	
	public void endCurrentDestructionAttempt() {
		if(current_destruction_attempt!=null) {
			this.destruction_attempts.add(current_destruction_attempt);
			this.current_destruction_attempt = null;
		}
		
	}

}
