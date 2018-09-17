package Database;

public class DBLaunch_Info {

	static int launchCounter = 0;
	
	private int count;
	private String launcher_id;
	private String missile_id;
	private int launch_time;
	private String destination;
	private int supposed_damage;
	private Boolean reached_target;
	private int real_damage;
	private int end_time;
	
	public DBLaunch_Info(String launcher_id, String missile_id, int launch_time, String destination, int supposed_damage) {
		this.count = ++launchCounter;
		this.launcher_id = launcher_id;
		this.missile_id = missile_id;
		this.launch_time = launch_time;
		this.destination = destination;
		this.supposed_damage = supposed_damage;
		this.reached_target = false;
		this.real_damage = 0;
	}
	

	public static int getLaunchCounter() {
		return launchCounter;
	}

	public int getCount() {
		return count;
	}

	public String getLauncher_id() {
		return launcher_id;
	}

	public String getMissile_id() {
		return missile_id;
	}

	public int getLaunch_time() {
		return launch_time;
	}

	public String getDestination() {
		return destination;
	}

	public int getSupposed_damage() {
		return supposed_damage;
	}

	public Boolean getReached_target() {
		return reached_target;
	}

	public int getReal_damage() {
		return real_damage;
	}

	public int getEnd_time() {
		return end_time;
	}
	
	public void setReached_target(Boolean reached_target) {
		this.reached_target = reached_target;
	}

	public void setReal_damage(int real_damage) {
		this.real_damage = real_damage;
	}

	public void setEnd_time(int end_time) {
		this.end_time = end_time;
	}

	
}
