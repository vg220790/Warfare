package com.afekawar.bl.base.Entities.BaseEntities;

import com.afekawar.bl.base.Interface.Communication.WarEvent;
import com.afekawar.bl.base.Interface.Communication.WarEventListener;
import com.afekawar.bl.base.Interface.Time.SystemTime;
import javafx.geometry.Point2D;

public class Missile extends WarEntity implements Comparable<Missile> {
	/*
	 * *************************************************************
	 * ******************** Fields and Properties ******************
	 * *************************************************************
	 */
	public enum State {
		LOADED, INAIR, DEAD
	}

	private int launchTime;
	private int flyTime;
	private int damage;
	private Point2D targetCoordinates;
	private Missile targetMissile;
	private MissileLauncher targetLauncher;
	private String destination;
	private State state;

	void setTargetMissile(Missile targetMissile) { // If the missile is anti-missile
		this.targetMissile = targetMissile;
	}

	void setTargetLauncher(MissileLauncher targetLauncher) { // If the missile is alti-launcher missile
		this.targetLauncher = targetLauncher;
	}

	public Missile() { // For gson parser
		super();
		setState(State.LOADED);
	}

	public Missile(String id, Point2D targetCoordinates, int launchTime, int flyTime, int damage, SystemTime time) {
		super(id, time);
		this.targetCoordinates = targetCoordinates;
		this.launchTime = launchTime;
		this.flyTime = flyTime;
		this.damage = damage;
		setState(State.LOADED);

	}

	/*
	 * *************************************************************
	 * ******************** Getters and Setters ********************
	 * *************************************************************
	 */

	Point2D getTargetCoordinates() {
		return targetCoordinates;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public void setTargetCoordinates(Point2D targetCoordinates) {
		this.targetCoordinates = targetCoordinates;
	}

	public State getState() {
		return state;
	}

	public int getLaunchTime() {
		return launchTime;
	}

	public int getFlyTime() {
		return flyTime;
	}

	public int getDamage() {
		return damage;
	}

	@Override
	public void stopThread() { // Missile launcher destroy func
		super.stopThread();
		state = State.DEAD;
	}

	void setLaunchTime(long launchTime) {
		this.launchTime = Math.toIntExact(launchTime);
	}

	void setState(State state) {
		this.state = state;
	}

	/*
	 * *************************************************************
	 * ******************** Run Logic ******************************
	 * *************************************************************
	 */

	@Override
	public void update() {
		super.update();
		fireUpdateMissileEvent();

		if (targetLauncher != null)
			if (!targetLauncher.getAlive()) {
				stopThread();
				fireDestroyAntiMissileLauncherMissileEvent();
			}
		if (targetMissile != null)
			if (targetMissile.getState() == State.DEAD) {
				stopThread();
				fireDestroyAntiMissileEvent();
			}
	}

	@Override
	public void run() {
		super.run();
		double distance = Math.sqrt((targetCoordinates.getY() - getCoordinates().getY())
				* (targetCoordinates.getY() - getCoordinates().getY())
				+ (targetCoordinates.getX() - getCoordinates().getX())
						* (targetCoordinates.getX() - getCoordinates().getX()));
		double speed = distance / flyTime;
		speed /= 60; // To be on same scale with framerate ( 60 fps ).

		double angle = Math.atan2(targetCoordinates.getY() - getCoordinates().getY(),
				targetCoordinates.getX() - getCoordinates().getX());
		setVelocity(new Point2D(Math.cos(angle) * speed, Math.sin(angle) * speed));

		state = State.INAIR;
		while (state == State.INAIR) {
			try {

				Thread.sleep(1000 / 60); // Update 60 times per sec
				update();

				if (getTime().getExactTime() >= launchTime + flyTime) {
					stopThread();
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private synchronized void fireUpdateMissileEvent() {
		WarEvent e = new WarEvent(getId());
		e.setEventType(WarEvent.Event_Type.UPDATE_COORDINATES);
		e.setCoordinates(getCoordinates());
		for (WarEventListener listener : getListeners()) {
			listener.handleWarEvent(e);
		}
	}

	private synchronized void fireDestroyAntiMissileEvent() {
		WarEvent e = new WarEvent(getId());
		e.setEventType(WarEvent.Event_Type.DESTROY_ANTI_MISSILE);
		e.setMissileId(getId());
		for (WarEventListener listener : getListeners()) {
			listener.handleWarEvent(e);
		}
	}

	private synchronized void fireDestroyAntiMissileLauncherMissileEvent() {
		WarEvent e = new WarEvent(getId());
		e.setEventType(WarEvent.Event_Type.DESTROY_ANTI_MISSILE_LAUNCHER);
		e.setMissileId(getId());
		for (WarEventListener listener : getListeners()) {
			listener.handleWarEvent(e);
		}
	}

	@Override
	public int compareTo(Missile o) {
		if (this.launchTime < o.launchTime)
			return -1;
		else
			return 1;
	}

}
