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

		
        ReadConfiguration.LoadConfig(missileLaunchersDB,missilesDB,missileLauncherDestructorsDB,missileDestructorsDB);


		System.out.println("System Out..");
	}

	@Override
	public void AddMissileLauncher(String id,MissileLauncher missileLauncher) {
		missileLaunchersDB.put(id, missileLauncher);

	}

    @Override
    public void AddMissileLauncherDestructor(String id, MissileLauncherDestructor missileLauncherDestructor) {
        missileLauncherDestructorsDB.put(id, missileLauncherDestructor);
    }

    @Override
	public void AddMissile(String id, Missile missile){
        missilesDB.put(id, missile);
	}

    @Override
    public void AddMissileDestructor(String id, MissileDestructor missileDestructor) {
        missileDestructorsDB.put(id, missileDestructor);
    }

	@Override
	public void LaunchMissile(String destination, int damage, int flyTime) {

	}

	@Override
	public void DestroyMissileLauncher() {

	}

	@Override
	public void DestroyMissile() {

	}

	@Override
	public void ShowStats() {

	}

	@Override
	public void Exit() {

	}
}
