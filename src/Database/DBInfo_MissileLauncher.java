package Database;

import java.util.ArrayList;

import com.afekawar.bl.base.Entities.BaseEntities.MissileLauncher;

public class DBInfo_MissileLauncher {

	private String id;
	private Boolean alwaysVisible;
	private Boolean isDestroyed;
	private int totalNumOfFiredMissiles;
	private int totalNumOfDestroyedMissiles;
	//private int numOfInterceptedMissiles;
	//private int numOfMissilesDestroyedWhenLauncherGotHit;
	private int totalNumOfHits;
	private int totalDamage;
	
	private DBLaunch_Info current_launch;
	private ArrayList<DBLaunch_Info> launches;
	
	private ArrayList<String> firedMissiles;
	
	public DBInfo_MissileLauncher(MissileLauncher ref) {
		this.id = ref.getId();
		this.alwaysVisible = ref.getAlwaysVisible();
		this.totalNumOfFiredMissiles=0;
		//this.numOfInterceptedMissiles=0;
		//this.numOfMissilesDestroyedWhenLauncherGotHit=0;
		this.totalNumOfHits=0;
		this.totalDamage=0;
		this.current_launch = null;
		this.launches = new ArrayList<DBLaunch_Info>();
		this.firedMissiles = new ArrayList<String>();
		if(ref.getAlive())
			this.isDestroyed = false;
		else
			this.isDestroyed = true;
		ref.setInfoRef(this);
	}
	
	public String getId() {
		return id;
	}

	public Boolean getAlwaysVisible() {
		return alwaysVisible;
	}

	public Boolean getIsDestroyed() {
		return isDestroyed;
	}

	public int getTotalNumOfFiredMissiles() {
		return totalNumOfFiredMissiles;
	}
	public int getTotalNumOfDestroyedMissiles() {
		return totalNumOfDestroyedMissiles;
	}
//	public int getNumOfInterceptedMissiles() {
//		return numOfInterceptedMissiles;
//	}
//	public int getNumOfMissilesDestroyedWhenLauncherGotHit() {
//		return numOfMissilesDestroyedWhenLauncherGotHit;
//	}

	public int getTotalNumOfHits() {
		return totalNumOfHits;
	}
	
	public ArrayList<String> getFiredMissiles() {
		return firedMissiles;
	}
	public int getTotalDamage() {
		return totalDamage;
	}
	public DBLaunch_Info getCurrent_launch() {
		return current_launch;
	}

	public ArrayList<DBLaunch_Info> getAllLaunches() {
		return launches;
	}

	
	public void setIsDestroyed(Boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}
	
	public void incrementTotalNumOfFiredMissiles() {
		this.totalNumOfFiredMissiles ++;
	}
	public void incrementTotalNumOfDestroyedMissiles() {
		this.totalNumOfDestroyedMissiles ++;
	}
	public void updateTotalDamage(int amount_to_add) {
		this.totalDamage += amount_to_add;
	}
//	public void updateNumOfInterceptedMissiles() {
//		this.numOfInterceptedMissiles = getTotalNumOfDestroyedMissiles() - getNumOfMissilesDestroyedWhenLauncherGotHit();
//	}
//	public void incrementNumOfMissilesDestroyedWhenLauncherGotHit() {
//		this.numOfMissilesDestroyedWhenLauncherGotHit ++;
//	}
	public void incrementTotalNumOfHits() {
		this.totalNumOfHits ++;
	}

	public void updateFiredMissilesList(String newFiredMissile) {
		this.firedMissiles.add(newFiredMissile);
	}
	
	public void setCurrentLaunch(DBLaunch_Info newlauch) {
		if(current_launch!=null) {
			endCurrentLaunch();
		}
		this.current_launch = newlauch;
	}
	
	public void endCurrentLaunch() {
		if(current_launch!=null) {
			this.launches.add(current_launch);
			this.current_launch = null;
		}
		
	}

	

}
