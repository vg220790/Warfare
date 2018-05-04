package com.warsim.test;


public class ConsoleVersion implements SystemInterface{


	public static void main(String[] args) {
        ReadConfiguration.LoadConfig();

	}

	@Override
	public void AddMissileLauncher(String id,MissileLauncher missileLauncher) {
		ReadConfiguration.missileLaunchersDB.put(id, missileLauncher);

	}

    @Override
    public void AddMissileLauncherDestructor(String id, MissileLauncherDestructor missileLauncherDestructor) {
        ReadConfiguration.missileLauncherDestructorsDB.put(id, missileLauncherDestructor);
    }

    @Override
	public void AddMissile(String id, Missile missile){
        ReadConfiguration.missilesDB.put(id, missile);
	}

    @Override
    public void AddMissileDestructor(String id, MissileDestructor missileDestructor) {
        ReadConfiguration.missileDestructorsDB.put(id, missileDestructor);
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
