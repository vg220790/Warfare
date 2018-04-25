package com.warsim.test;

import java.util.ArrayList;
import java.util.HashMap;

public class ConsoleVersion implements SystemInterface{
    HashMap<String, MissleLauncher> missleLaunchers;
    HashMap<String,MissleLauncherDestructor> missleLauncherDestructors;
    HashMap<String,MissleDestructor> missleDestructors;

	public static void main(String[] args) {


		System.out.println("System Starts..");

	}

	@Override
	public void AddMissleLauncher(String id,MissleLauncher missleLauncher) {
        missleLaunchers.put(id,missleLauncher);

	}

    @Override
    public void AddMissleLauncherDestructor(String id, MissleLauncherDestructor missleLauncherDestructor) {
        missleLauncherDestructors.put(id, missleLauncherDestructor);
    }

    @Override
    public void AddMissleDestructor(String id, MissleDestructor missleDestructor) {
        missleDestructors.put(id, missleDestructor);
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
