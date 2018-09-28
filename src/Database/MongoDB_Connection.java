package Database;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDB_Connection implements Queryable {

	private static MongoClient mongoclient;
	private static final String DB_NAME ="afeka_warfare_db";  
	private static final String HOST = "localhost";
	private static int PORT = 27017;
	private static MongoDatabase db;
	
	MongoCollection<Document> missile_launchers_collection;
	MongoCollection<Document> missile_destructors_collection;
	MongoCollection<Document> missile_launcher_destructors_collection;
	MongoCollection<Document> launches_collection;
	MongoCollection<Document> destruction_attempts_collection;
	MongoCollection<Document> hits_collection;
	
	static {
		try {
			mongoclient = new MongoClient(HOST,PORT);
			db = mongoclient.getDatabase(DB_NAME);
			System.out.println("MongoDB connection successful!!!!");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addMissileLauncherToTable(DBInfo_MissileLauncher launcher) {
		missile_launchers_collection = db.getCollection("missile_launchers");
		 Document missile_launcher = new Document();
		
		 missile_launcher.append("object", "missile launcher");
		 missile_launcher.append("id", launcher.getId());
		 missile_launcher.append("always_visible", launcher.getAlwaysVisible());
		 missile_launcher.append("total_launched_missiles", launcher.getTotalNumOfFiredMissiles());
		 missile_launcher.append("total_destroyed_missiles", launcher.getTotalNumOfDestroyedMissiles());
		 missile_launcher.append("total_hits", launcher.getTotalNumOfHits());
		 missile_launcher.append("total_damage", launcher.getTotalDamage());
		 missile_launcher.append("launcher_destroyed", launcher.getIsDestroyed());
		 missile_launchers_collection.insertOne(missile_launcher);
		
	}

	@Override
	public void addMissileDestructorToTable(DBInfo_MissileDestructor destructor) {
		missile_destructors_collection = db.getCollection("missile_destructors");
		 Document missile_destructor = new Document();
		 missile_destructor.append("object", "missile destructor");
		 missile_destructor.append("id", destructor.getId());
		 missile_destructor.append("total_interception_attempts", destructor.getTotalNumOfInterceptionAttempts());
		 missile_destructor.append("intercepted_missiles", destructor.getNumOfInterceptedMissiles());
		 missile_destructor.append("total_damage_avoided", destructor.gettotalDamageAvoided());
		 missile_destructors_collection.insertOne(missile_destructor);
		 
//		 List<Document> documents = (List<Document>) missile_destructors_collection.find().into(
//					new ArrayList<Document>());
//	 
//	               for(Document document : documents){
//	                   System.out.println(document);
//	               }
		
	}

	@Override
	public void addMissileLauncherDestructorToTable(DBInfo_MissileLauncherDestructor launcherDestructor) {
		missile_launcher_destructors_collection = db.getCollection("missile_launcher_destructors");
		 Document missile_launcher_destructor = new Document();
		 missile_launcher_destructor.append("object", "missile launcher destructor");
		 missile_launcher_destructor.append("id", launcherDestructor.getId());
		 missile_launcher_destructor.append("type", launcherDestructor.getType());
		 missile_launcher_destructor.append("total_destruction_attempts", launcherDestructor.getTotalNumOfDestructionAttempts());
		 missile_launcher_destructor.append("launchers_destroyed", launcherDestructor.getNumOfDestructedLaunchers());
		 missile_launcher_destructors_collection.insertOne(missile_launcher_destructor);
		
	}


	@Override
	public void addNewLaunchToTable(DBLaunch_Info missile_launch) {
		
		launches_collection = db.getCollection("launches");
		 Document launch = new Document();
		 launch.append("object", "launch");
		 launch.append("count", missile_launch.getCount());
		 launch.append("launcher_id", missile_launch.getLauncher_id());
		 launch.append("missile_id", missile_launch.getMissile_id());
		 launch.append("launch_time", missile_launch.getLaunch_time());
		 launch.append("destination", missile_launch.getDestination());
		 launch.append("supposed_damage", missile_launch.getSupposed_damage());
		 launch.append("reached_target", missile_launch.getReached_target());
		 launch.append("real_damage", missile_launch.getReal_damage());
		 launch.append("end_time", missile_launch.getEnd_time());
		 launches_collection.insertOne(launch);
	}


	@Override
	public void addNewDestructionAttemptToTable(DBDestructionAttempt_Info d_attempt) {
		destruction_attempts_collection = db.getCollection("destruction_attempts");
		 Document destruction_attempt = new Document();
		 //count, weapon_type, weapon_id, target_type, target_id, start_time, end_time, destruction_successful
		 destruction_attempt.append("object", "destruction_attempt");
		 destruction_attempt.append("count", d_attempt.getCount());
		 destruction_attempt.append("weapon_type", d_attempt.getWeapon_type());
		 destruction_attempt.append("weapon_id", d_attempt.getWeapon_id());
		 destruction_attempt.append("target_type", d_attempt.getTarget_type());
		 destruction_attempt.append("target_id", d_attempt.getTarget_id());
		 destruction_attempt.append("start_time", d_attempt.getStart_time());
		 destruction_attempt.append("end_time", d_attempt.getEnd_time());
		 destruction_attempt.append("destruction_successful", d_attempt.getDestruction_successful());
		 destruction_attempts_collection.insertOne(destruction_attempt);
	}
	
	@Override
	public void addNewHitToTable(DBHits_Info hit) {
		 hits_collection = db.getCollection("hits");
		 Document hit_DBobject = new Document();
		 hit_DBobject.append("object", "hit");
		 hit_DBobject.append("count", hit.getCount());
		 hit_DBobject.append("weapon_type", hit.getWeapon_type());
		 hit_DBobject.append("weapon_id", hit.getWeapon_id());
		 hit_DBobject.append("target_type", hit.getTarget_type());
		 hit_DBobject.append("target_id", hit.getTarget_id());
		 hit_DBobject.append("start_time", hit.getStart_time());
		 hit_DBobject.append("end_time", hit.getEnd_time());
		 hits_collection.insertOne(hit_DBobject);
	}

	
	@Override
	public void updateMissileLauncherInTable(DBInfo_MissileLauncher launcher) {
		
		//checking if launcher exists in table
		  if (checkIfExistsInDB( "missile_launchers", "id", launcher.getId())) {
			 
			  //exists
			  // find id = launcher.getId(), and update the values 
			  Document newDocument = new Document();
			  newDocument.put("object", "missile launcher");
			  newDocument.put("id", launcher.getId());
			  newDocument.put("always_visible", launcher.getAlwaysVisible());
			  newDocument.put("total_launched_missiles", launcher.getTotalNumOfFiredMissiles());
			  newDocument.put("total_destroyed_missiles", launcher.getTotalNumOfDestroyedMissiles());
			  newDocument.put("total_hits", launcher.getTotalNumOfHits());
			  newDocument.put("total_damage", launcher.getTotalDamage());
			  newDocument.put("launcher_destroyed", launcher.getIsDestroyed());
			  Document searchQuery = new Document().append("id", launcher.getId());
			  missile_launchers_collection.replaceOne(searchQuery, newDocument);
			  
		  }else {
			  //doesn't exist
			  addMissileLauncherToTable(launcher);
		  }
	}
	

	@Override
	public void updateMissileDestructorInTable(DBInfo_MissileDestructor destructor)  {
		//checking if launcher exists in table
		  if (checkIfExistsInDB( "missile_destructors", "id", destructor.getId())) {
			 
			  //exists
			  Document newDocument = new Document();
			  newDocument.put("object", "missile destructor");
			  newDocument.put("id", destructor.getId());
			  newDocument.put("total_interception_attempts", destructor.getTotalNumOfInterceptionAttempts());
			  newDocument.put("intercepted_missiles", destructor.getNumOfInterceptedMissiles());
			  newDocument.put("total_damage_avoided", destructor.gettotalDamageAvoided());
			  Document searchQuery = new Document().append("id", destructor.getId());
			  missile_destructors_collection.replaceOne(searchQuery, newDocument);
			  
		  }else {
			  //doesn't exist
			  addMissileDestructorToTable(destructor);
		  }	
	}

	@Override
	public void updateMissileLauncherDestructorInTable(DBInfo_MissileLauncherDestructor mldestructor){
		  //checking if launcher exists in table
		  if (checkIfExistsInDB( "missile_launcher_destructors", "id", mldestructor.getId())) {
			 
			  //exists
			  //total_destruction_attempts = ?, launchers_destroyed
			  Document newDocument = new Document();
			  newDocument.put("object", "missile launcher destructor");
			  newDocument.put("id", mldestructor.getId());
			  newDocument.put("type", mldestructor.getType());
			  newDocument.put("total_destruction_attempts", mldestructor.getTotalNumOfDestructionAttempts());
			  newDocument.put("launchers_destroyed", mldestructor.getNumOfDestructedLaunchers());
			  
			  Document searchQuery = new Document().append("id", mldestructor.getId());
			  missile_launcher_destructors_collection.replaceOne(searchQuery, newDocument);
			  
		  }else {
			  //doesn't exist
			  addMissileLauncherDestructorToTable(mldestructor);
		  }
		
	}

	@Override
	public void updateLaunchesInTable(DBLaunch_Info launch) {
		 //checking if launcher exists in table
		  if (!checkIfExistsInDB( "launches", "missile_id", launch.getMissile_id())) 
			  addNewLaunchToTable(launch);	
	}

	@Override
	public void updateDestructionAttemptsInTable(DBDestructionAttempt_Info destruction_attempt) {
		// destruction_attempts_collection
		 if (!checkIfExistsInDB( "destruction_attempts", "count", "" + destruction_attempt.getCount() )) 
			  addNewDestructionAttemptToTable(destruction_attempt);	
	}
	
	@Override
	public void updateHitsInTable(DBHits_Info hit) {
		// hits_collection
				 if (!checkIfExistsInDB( "hits", "count", "" + hit.getCount() )) 
					  //exists
					  addNewHitToTable(hit);	
		//showAllCollections();
	}


//	@Override
//	public void clearAllTables() {
//		// TODO Auto-generated method stub
//	
//	}

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
	public void clearDBTable(String collection_name) {
		db.getCollection(collection_name).drop();
		
	}
	
	public void showAllCollections() {
		showCollection("missile_launchers");
		showCollection("missile_destructors");
		showCollection("missile_launcher_destructors");
		showCollection("launches");
		showCollection("destruction_attempts");
		showCollection("hits");
	}
	
	public void showCollection(String collection_name) {
		
		System.out.println();
		System.out.println("////////////////////////////////////////////////////////////////////");
		System.out.println("//////////////////////////"+ collection_name +"////////////////");
		System.out.println("////////////////////////////////////////////////////////////////////");
		
		List<Document> documents = (List<Document>) db.getCollection(collection_name).find().into(
				new ArrayList<Document>());
 
               for(Document document : documents){
                   System.out.println(document);
               }
	}
	
	public boolean checkIfExistsInDB(String collection_name, String key, String value) {
	
		if (collection_name.equals("destruction_attempts") || collection_name.equals("hits")) {
			@SuppressWarnings("deprecation")
			long count_int =  mongoclient.getDatabase(DB_NAME)               
	                .getCollection(collection_name)
	                .count(new Document(key, Integer.parseInt(value)));
			if(count_int>0)
				//exists
				return true;
			return false;
		}
	   @SuppressWarnings("deprecation")
	   long count =  mongoclient.getDatabase(DB_NAME)               
                .getCollection(collection_name)
                .count(new Document(key, value));
		if(count>0)
			//exists
			return true;
		return false;
}

	@Override
	public void closeDB() {
		mongoclient.close();
		
	}

	
}
