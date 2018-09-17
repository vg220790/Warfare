package Database;

import java.util.ArrayList;
import com.afekawar.bl.base.Entities.BaseEntities.MissileDestructor;

public class DBInfo_MissileDestructor {

	private String id;
	private int totalNumOfInterceptionAttempts;
	private int numOfInterceptedMissiles;
	private int totalDamageAvoided;
	private ArrayList<String> missiles;
	
	private DBDestructionAttempt_Info current_destruction_attempt;;
	private ArrayList<DBDestructionAttempt_Info> destruction_attempts;
	
	public DBInfo_MissileDestructor(MissileDestructor ref) {
		this.id = ref.getId();
		this.totalNumOfInterceptionAttempts=0;
		this.numOfInterceptedMissiles=0;
		this.totalDamageAvoided=0;
		this.missiles = new ArrayList<String>();
		this.destruction_attempts = new ArrayList<DBDestructionAttempt_Info>();
		ref.setInfoRef(this);
	}
	
	public String getId() {
		return id;
	}

	public int getTotalNumOfInterceptionAttempts() {
		return totalNumOfInterceptionAttempts;
	}

	public int getNumOfInterceptedMissiles() {
		return numOfInterceptedMissiles;
	}
	public ArrayList<String> getFiredMissiles() {
		return missiles;
	}
	public int gettotalDamageAvoided() {
		return totalDamageAvoided;
	}
	
	public void incrementTotalNumOfInterceptionAttempts() {
		this.totalNumOfInterceptionAttempts ++;
	}
	public void updateTotalDamageAvoided(int amountToAdd) {
		this.totalDamageAvoided += amountToAdd;
	}
	public void incrementNumOfInterceptedMissiles() {
		this.numOfInterceptedMissiles ++;
	}

	public void updateFiredMissiles(String newFiredMissile) {
		this.missiles.add(newFiredMissile);
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
