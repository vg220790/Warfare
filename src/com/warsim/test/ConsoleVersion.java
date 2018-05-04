package com.warsim.test;


public class ConsoleVersion implements SystemInterface{


	public static void main(String[] args) {
        ReadConfiguration.LoadConfig();

	}

	@Override
	public void AddMissleLauncher(String id,MissleLauncher missleLauncher) {
		ReadConfiguration.missleLaunchersDB.put(id,missleLauncher);

	}

    @Override
    public void AddMissleLauncherDestructor(String id, MissleLauncherDestructor missleLauncherDestructor) {
        ReadConfiguration.missleLauncherDestructorsDB.put(id, missleLauncherDestructor);
    }

    @Override
	public void AddMissle(String id, Missle missle){
        ReadConfiguration.misslesDB.put(id,missle);
	}

    @Override
    public void AddMissleDestructor(String id, MissleDestructor missleDestructor) {
        ReadConfiguration.missleDestructorsDB.put(id, missleDestructor);
    }

	@Override
	public void LaunchMissle(String destination, int damage, int flyTime) {

	}

	@Override
	public void DestroyMissleLauncher() {

	}

	@Override
	public void DestroyMissle() {

	}

	@Override
	public void ShowStats() {

	}

	@Override
	public void Exit() {

	}
}
