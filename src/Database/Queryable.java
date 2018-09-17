package Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Queryable {
	
	////////////add entities to DB table methods //////////
	public void addMissileLauncherToTable(DBInfo_MissileLauncher laucher);
	public void addMissileDestructorToTable(DBInfo_MissileDestructor destructor);
	public void addMissileLauncherDestructorToTable(DBInfo_MissileLauncherDestructor launcherDestructor); 
	public void addNewLaunchToTable(DBLaunch_Info launch); 
	public void addNewDestructionAttemptToTable(DBDestructionAttempt_Info destruction_attempt);
	public void addNewHitToTable(DBHits_Info hit);
	////////////update entities to DB table methods //////////
	public void updateMissileLauncherInTable(DBInfo_MissileLauncher launcher); 
	public void updateMissileDestructorInTable(DBInfo_MissileDestructor destructor);
	public void updateMissileLauncherDestructorInTable(DBInfo_MissileLauncherDestructor mldestructor);
	public void updateLaunchesInTable(DBLaunch_Info launch) throws SQLException; 
	public void updateDestructionAttemptsInTable(DBDestructionAttempt_Info destruction_attempt);
	public void updateHitsInTable(DBHits_Info hit);
	//////////// clear DB table methods //////////
	public default  void clearAllTables() {
		clearMissileLauncherTable();
		clearMissileDestructorTable();
		clearMissileLauncherDestructorTable();
		clearLaunchesTable();
		clearDestructionAttemptsTable();
		clearHitsTable();
	}
	public void clearMissileLauncherTable();
	public void clearMissileDestructorTable();
	public void clearMissileLauncherDestructorTable();
	public void clearLaunchesTable();
	public void clearDestructionAttemptsTable();
	public void clearHitsTable();
	public void clearDBTable(String table_name); //generic method
	////////////close DB connection method //////////
	public void closeDB();
	
	public void showAllCollections();
	
}
