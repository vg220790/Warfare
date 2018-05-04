package com.warsim.test;


import java.util.HashMap;

public class ConsoleVersion implements SystemInterface{
	private static HashMap<String, MissileLauncher> missileLaunchersDB;
	private static HashMap<String, Missile> missilesDB;
	private static HashMap<String, MissileLauncherDestructor> missileLauncherDestructorsDB;
	private static HashMap<String, MissileDestructor> missileDestructorsDB;

	public static void main(String[] args) {
		missileLauncherDestructorsDB = new HashMap<>();
		missilesDB = new HashMap<>();
		missileLaunchersDB = new HashMap<>();
		missileDestructorsDB = new HashMap<>();

		System.out.println("System Start...");


        ReadConfiguration.loadConfig(missileLaunchersDB,missilesDB,missileLauncherDestructorsDB,missileDestructorsDB);

		boolean systemRuns = true;

		//while(systemRuns){

		//}



		System.out.println("System Out..");
	}

	@Override
	public void addMissileLauncher(String id,MissileLauncher missileLauncher) {
		missileLaunchersDB.put(id, missileLauncher);

	}

    @Override
    public void addMissileLauncherDestructor(String id, MissileLauncherDestructor missileLauncherDestructor) {
        missileLauncherDestructorsDB.put(id, missileLauncherDestructor);
    }

    @Override
	public void addMissile(String id, Missile missile){
        missilesDB.put(id, missile);
	}

    @Override
    public void addMissileDestructor(String id, MissileDestructor missileDestructor) {
        missileDestructorsDB.put(id, missileDestructor);
    }

	@Override
	public void launchMissile(String destination, int damage, int flyTime) {

	}

	@Override
	public void destroyMissileLauncher() {

	}

	@Override
	public void destroyMissile() {

	}

	@Override
	public void showStats() {

	}

	@Override
	public void exit() {

	}
}
