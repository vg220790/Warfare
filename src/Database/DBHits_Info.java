package Database;

public class DBHits_Info {
	static int counter = 0;
	
	private int count;
	private int da_table_count;
	private String weapon_id;
	private String weapon_type;
	private String target_id;
	private String target_type;
	private int start_time;
	private int end_time;
	
	public DBHits_Info(int da_table_count, String weapon_id, String weapon_type, String target_id, String target_type, int start_time, int end_time) {
		this.count = ++counter;
		this.da_table_count = da_table_count;
		this.weapon_id = weapon_id;
		this.weapon_type = weapon_type;
		this.target_id = target_id;
		this.target_type = target_type;
		this.start_time = start_time;
		this.end_time = end_time;
	}

	public int getCount() {
		return count;
	}
	public int getDATableCount() {
		return da_table_count;
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

	public int getEnd_time() {
		return end_time;
	}
	
}
