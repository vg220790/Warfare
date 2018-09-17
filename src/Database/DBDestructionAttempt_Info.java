package Database;

public class DBDestructionAttempt_Info {
	static int counter = 0;
	
	private int count;
	
	private String weapon_id;
	private String weapon_type;
	private String target_id;
	private String target_type;
	private int start_time;
	private int end_time;
	private Boolean destruction_successful;;
	
	
	public DBDestructionAttempt_Info(String weapon_id, String weapon_type, String target_id, String target_type) {
		this.count = ++counter;
		this.weapon_id = weapon_id;
		this.weapon_type = weapon_type;
		this.target_id = target_id;
		this.target_type = target_type;
		this.destruction_successful = false;
	}

	public int getCount() {
		return count;
	}

	public String getWeapon_id() {
		return weapon_id;
	}

	public String getWeapon_type() {
		return weapon_type;
	}
	public String getTarget_id() {
		return target_id;
	}

	public String getTarget_type() {
		return target_type;
	}

	public int getStart_time() {
		return start_time;
	}

	public Boolean getDestruction_successful() {
		return destruction_successful;
	}

	public int getEnd_time() {
		return end_time;
	}
	
	public void setDestruction_successful(Boolean destruction_successful) {
		this.destruction_successful = destruction_successful;
	}

	public void setEnd_time(int end_time) {
		this.end_time = end_time;
		
	}
	public void setStart_time(int start_time) {
		this.start_time = start_time;
	}

}
