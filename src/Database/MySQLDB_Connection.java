package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLDB_Connection implements Queryable {
	
	private static Connection   sql_connection;
	private static String       sql_connectionURL;
	private static ResultSet    rs;
	private static PreparedStatement statement;
	
	static {
		sql_connectionURL = "jdbc:mysql://localhost:3306/warfare_mysqldb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&verifyServerCertificate=false&useSSL=true";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			sql_connection = DriverManager.getConnection(sql_connectionURL, "root", "vg220790");
			System.out.println("SQL DB connection successful!!!!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			while (e != null) {
				System.out.println(e.getMessage());
				e = e.getNextException();
			}
		}
	}
	
	@Override
	public void addMissileLauncherToTable(DBInfo_MissileLauncher laucher) {
		
		try {
			
			String query = "INSERT INTO warfare_mysqldb.missile_launchers (id, always_visible, total_launched_missiles, total_destroyed_missiles, total_hits, total_damage, launcher_destroyed) VALUES (?, ?, ?, ?, ?, ?, ?)";
			//System.out.println(query);
			statement = (PreparedStatement) sql_connection.prepareStatement(query);
			statement.setString(1, laucher.getId());
			statement.setBoolean(2, laucher.getAlwaysVisible());
			statement.setInt(3, laucher.getTotalNumOfFiredMissiles());
			statement.setInt(4, laucher.getTotalNumOfDestroyedMissiles());
			//statement.setInt(5, laucher.getNumOfInterceptedMissiles());
			//statement.setInt(6, laucher.getNumOfMissilesDestroyedWhenLauncherGotHit());
			statement.setInt(5, laucher.getTotalNumOfHits());
			statement.setInt(6, laucher.getTotalDamage());
			statement.setBoolean(7, laucher.getIsDestroyed());
			statement.executeUpdate();				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void addMissileDestructorToTable(DBInfo_MissileDestructor destructor){
		
		try {
			
			String query = "INSERT INTO warfare_mysqldb.missile_destructors (id, total_interception_attempts, intercepted_missiles, total_damage_avoided) VALUES (?, ?, ?, ?)";
			//System.out.println(query);
			statement = (PreparedStatement) sql_connection.prepareStatement(query);
			statement.setString(1, destructor.getId());
			statement.setInt(2, destructor.getTotalNumOfInterceptionAttempts());
			statement.setInt(3, destructor.getNumOfInterceptedMissiles());
			statement.setInt(4, destructor.gettotalDamageAvoided());
			statement.executeUpdate();				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void addMissileLauncherDestructorToTable(DBInfo_MissileLauncherDestructor launcherDestructor) {
		
		try {
			
			String query = "INSERT INTO warfare_mysqldb.missile_launcher_destructors (id, type, total_destruction_attempts, launchers_destroyed) VALUES (?, ?, ?, ?)";
			//System.out.println(query);
			statement = (PreparedStatement) sql_connection.prepareStatement(query);
			statement.setString(1, launcherDestructor.getId());
			statement.setString(2, launcherDestructor.getType());
			statement.setInt(3, launcherDestructor.getTotalNumOfDestructionAttempts());
			statement.setInt(4, launcherDestructor.getNumOfDestructedLaunchers());
			statement.executeUpdate();				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void addNewLaunchToTable(DBLaunch_Info launch) {
		try {
			
			String query = "INSERT INTO warfare_mysqldb.launches (count, launcher_id, missile_id, launch_time, destination, supposed_damage, reached_target, real_damage, end_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			//System.out.println(query);
			statement = (PreparedStatement) sql_connection.prepareStatement(query);
			statement.setInt(1, launch.getCount());
			statement.setString(2, launch.getLauncher_id());
			statement.setString(3, launch.getMissile_id());
			statement.setInt(4, launch.getLaunch_time());
			statement.setString(5, launch.getDestination());
			statement.setInt(6, launch.getSupposed_damage());
			statement.setBoolean(7, launch.getReached_target());
			statement.setInt(8, launch.getReal_damage());
			statement.setInt(9, launch.getEnd_time());
			statement.executeUpdate();				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void addNewDestructionAttemptToTable(DBDestructionAttempt_Info destruction_attempt) {
		try {
			String query = "INSERT INTO warfare_mysqldb.destruction_attempts (count, weapon_type, weapon_id, target_type, target_id, start_time, end_time, destruction_successful) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			//System.out.println(query);
			statement = (PreparedStatement) sql_connection.prepareStatement(query);
			statement.setInt(1, destruction_attempt.getCount());
			statement.setString(2, destruction_attempt.getWeapon_type());
			statement.setString(3, destruction_attempt.getWeapon_id());
			statement.setString(4, destruction_attempt.getTarget_type());
			statement.setString(5, destruction_attempt.getTarget_id());
			statement.setInt(6, destruction_attempt.getStart_time());
			statement.setInt(7, destruction_attempt.getEnd_time());
			statement.setBoolean(8, destruction_attempt.getDestruction_successful());
			statement.executeUpdate();				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void addNewHitToTable(DBHits_Info hit) {
		try {
			String query = "INSERT INTO warfare_mysqldb.hits (count, weapon_type, weapon_id, target_type, target_id, start_time, end_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
			//System.out.println(query);
			statement = (PreparedStatement) sql_connection.prepareStatement(query);
			statement.setInt(1, hit.getCount());
			statement.setString(2, hit.getWeapon_type());
			statement.setString(3, hit.getWeapon_id());
			statement.setString(4, hit.getTarget_type());
			statement.setString(5, hit.getTarget_id());
			statement.setInt(6, hit.getStart_time());
			statement.setInt(7, hit.getEnd_time());
			statement.executeUpdate();				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateMissileLauncherInTable(DBInfo_MissileLauncher launcher){
        
        try {
		
        	rs = null;           
    		String check = "select id from warfare_mysqldb.missile_launchers where id=?";
    		statement = (PreparedStatement) sql_connection.prepareStatement(check);
    		statement.setString(1, launcher.getId());
            rs = statement.executeQuery();
            if(!rs.isBeforeFirst()){
               //data not exist
            	addMissileLauncherToTable(launcher);
            }
           else{
              // data exist
        	   try {
       			String query = "update warfare_mysqldb.missile_launchers set total_launched_missiles = ?, total_destroyed_missiles = ?, total_hits = ?, total_damage = ?, launcher_destroyed = ? where id = ?";
       			statement = (PreparedStatement) sql_connection.prepareStatement(query);
       			statement.setInt(1, launcher.getTotalNumOfFiredMissiles());
       			statement.setInt(2, launcher.getTotalNumOfDestroyedMissiles());
       			//statement.setInt(3, launcher.getNumOfInterceptedMissiles());
       			//statement.setInt(4, launcher.getNumOfMissilesDestroyedWhenLauncherGotHit());
       			statement.setInt(3, launcher.getTotalNumOfHits());
       			statement.setInt(4, launcher.getTotalDamage());
       			statement.setBoolean(5, launcher.getIsDestroyed());
       			statement.setString(6, launcher.getId());
       			statement.executeUpdate();	
       		} catch (SQLException e) {
       			// TODO Auto-generated catch block
       			e.printStackTrace();
       		}
         
           }
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
        
	}
	
	@Override
	public void updateMissileDestructorInTable(DBInfo_MissileDestructor destructor)  {
		
		try {
			
			rs = null;           
			String check = "select id from warfare_mysqldb.missile_destructors where id=?";
			statement = (PreparedStatement) sql_connection.prepareStatement(check);
			statement.setString(1, destructor.getId());
	        rs = statement.executeQuery();
	        if(!rs.isBeforeFirst()){
	           //data not exist
	        	addMissileDestructorToTable(destructor);
	        }
	       else {
	    	// data exist
	    	   try {
	   			String query = "update warfare_mysqldb.missile_destructors set total_interception_attempts = ?, intercepted_missiles = ?, total_damage_avoided = ? where id = ?";
	   			statement = (PreparedStatement) sql_connection.prepareStatement(query);
	   			statement.setInt(1, destructor.getTotalNumOfInterceptionAttempts());
	   			statement.setInt(2, destructor.getNumOfInterceptedMissiles());
	   			statement.setInt(3, destructor.gettotalDamageAvoided());
	   			statement.setString(4, destructor.getId());
	   			statement.executeUpdate();	
	   		} catch (SQLException e) {
	   			// TODO Auto-generated catch block
	   			e.printStackTrace();
	   		}
	       }
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	@Override
	public void updateMissileLauncherDestructorInTable(DBInfo_MissileLauncherDestructor mldestructor) {
	
        try {
		
        	rs = null;           
    		String check = "select id from warfare_mysqldb.missile_launcher_destructors where id=?";
    		statement = (PreparedStatement) sql_connection.prepareStatement(check);
    		statement.setString(1, mldestructor.getId());
            rs = statement.executeQuery();
            if(!rs.isBeforeFirst()){
               //data not exist
            	addMissileLauncherDestructorToTable(mldestructor);
            }
           else {
        	// data exist
        	   try {
       			String query = "update warfare_mysqldb.missile_launcher_destructors set total_destruction_attempts = ?, launchers_destroyed = ? where id = ?";
       			statement = (PreparedStatement) sql_connection.prepareStatement(query);
       			statement.setInt(1, mldestructor.getTotalNumOfDestructionAttempts());
       			statement.setInt(2, mldestructor.getNumOfDestructedLaunchers());
       			statement.setString(3, mldestructor.getId());
       			statement.executeUpdate();	
       		} catch (SQLException e) {
       			// TODO Auto-generated catch block
       			e.printStackTrace();
       		}
           }
        	
		} catch (SQLException e1) {
			
			e1.printStackTrace();
		}
        
	}
	
	@Override
	public void updateLaunchesInTable(DBLaunch_Info launch){

        try {
    		
        	rs = null;           
    		String check = "select count from warfare_mysqldb.launches where missile_id=?";
    		statement = (PreparedStatement) sql_connection.prepareStatement(check);
    		statement.setString(1, launch.getMissile_id());
            rs = statement.executeQuery();
            if(!rs.isBeforeFirst()){
               //data not exist
            	addNewLaunchToTable(launch);
            }
        	
		} catch (SQLException e1) {
			
			e1.printStackTrace();
		}
	}
	
	@Override
	public void updateDestructionAttemptsInTable(DBDestructionAttempt_Info destruction_attempt){
		try {
			rs = null;           
			String check = "select count from warfare_mysqldb.destruction_attempts where count=?";
			statement = (PreparedStatement) sql_connection.prepareStatement(check);
			statement.setInt(1, destruction_attempt.getCount());
	        rs = statement.executeQuery();
	        if(!rs.isBeforeFirst()){
	           //data not exist
	        	addNewDestructionAttemptToTable(destruction_attempt);
	        }
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateHitsInTable(DBHits_Info hit) {
		try {
			rs = null;           
			String check = "select count from warfare_mysqldb.hits where count=?";
			statement = (PreparedStatement) sql_connection.prepareStatement(check);
			statement.setInt(1, hit.getCount());
	        rs = statement.executeQuery();
	        if(!rs.isBeforeFirst()){
	           //data not exist
	        	addNewHitToTable(hit);
	        }
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//not used
//	public void getHitsDataFromDestructionAttemptsTable() {
//		try {
//			rs = null;           
//			String query = "select * from warfare_mysqldb.destruction_attempts where destruction_successful=1";
//			statement = (PreparedStatement) sql_connection.prepareStatement(query);
//	        rs = statement.executeQuery();
//	        if(rs.next()){
//	        	int res_count =rs.getInt("count");//this will get the two value in each row.
//	        	String res_weapon_type =rs.getString("weapon_type");
//	        	String res_weapon_id =rs.getString("weapon_id");
//	        	String res_target_type =rs.getString("target_type");
//	        	String res_target_id =rs.getString("target_id");
//	        	int res_start_time = rs.getInt("start_time");
//	        	int res_end_time = rs.getInt("end_time");
//	        	Boolean res_destruction_successful = rs.getBoolean("destruction_successful");
//	        }
//		}catch(SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
//	public  void clearAllTables() {
//		clearMissileLauncherTable();
//		clearMissileDestructorTable();
//		clearMissileLauncherDestructorTable();
//		clearLaunchesTable();
//		clearDestructionAttemptsTable();
//		clearHitsTable();
//	}
	
	//////////// clear DB table methods //////////
	@Override
	public void clearMissileLauncherTable() {
		clearDBTable("missile_launchers");
	}
	@Override
	public void clearMissileDestructorTable() {
		clearDBTable("missile_destructors");
	}
	@Override
	public void clearMissileLauncherDestructorTable() {
		clearDBTable("missile_launcher_destructors");
	}
	
	@Override
	public void clearLaunchesTable() {
		clearDBTable("launches");
	}
	
	@Override
	public void clearDestructionAttemptsTable() {
		clearDBTable("destruction_attempts");
	}
	
	@Override
	public void clearHitsTable() {
		clearDBTable("hits");
	}
	
	@Override
	public void clearDBTable(String table_name) {
		try {
			String query = "DELETE FROM warfare_mysqldb."+table_name;
			statement = (PreparedStatement) sql_connection.prepareStatement(query);
			statement.executeUpdate();				
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void closeDB() {
		try {
			
			if (sql_connection != null) {
				sql_connection.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			System.out.println("Could not close the current connection.");
			e.printStackTrace();
		}
	}
	
	public void showAllCollections() {
		
	}
}
