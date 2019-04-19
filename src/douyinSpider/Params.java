package douyinSpider;

/**
 * url中的所有参数
 * @author echo
 *
 */
public class Params {
	long iid;
	String idfa;
	String vid;
	String version_code = "5.2.0";
	String mas;
	String as;
	String ts;
	String app_name = "aweme";
	long device_id;
	String openudid;
	String device_type;
	String os_version;
	String os_api;
	String device_platform;
	String channel = "App%20Store";
	String build_number = "50006";
	String app_version = "5.2.0";
	String aid = "1128";
	String ac = "WIFI";
	String count = "12";

	String max_cursor = "0";
	String min_cursor = "0";
	String pull_type = "2";
	String type = "0";
	String volume = "0.00";
	long install_id;
	String uuid;
	String  android_id;

	public String getAndroid_id() {
		return android_id;
	}

	public void setAndroid_id(String android_id) {
		this.android_id = android_id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void intial() {
		
		channel = "App%20Store";
		build_number = "52007";
		app_version = "5.2.0";
		aid = "1128";
		ac = "WIFI";
		count = "12";
		app_name = "aweme";
		version_code = "5.2.0";
		max_cursor = "0";
		min_cursor = "0";
		pull_type = "2";
		type = "0";
		volume = "0.00";
	}

	public long getInstall_id() {
		return install_id;
	}

	public void setInstall_id(long install_id) {
		this.install_id = install_id;
	}

	public long getIid() {
		return iid;
	}

	public void setIid(long iid) {
		this.iid = iid;
	}

	public String getIdfa() {
		return idfa;
	}

	public void setIdfa(String idfa) {
		this.idfa = idfa;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getVersion_code() {
		return version_code;
	}

	public void setVersion_code(String version_code) {
		this.version_code = version_code;
	}

	public String getMas() {
		return mas;
	}

	public void setMas(String mas) {
		this.mas = mas;
	}

	public String getAs() {
		return as;
	}

	public void setAs(String as) {
		this.as = as;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public long getDevice_id() {
		return device_id;
	}

	public void setDevice_id(long device_id) {
		this.device_id = device_id;
	}

	public String getOpenudid() {
		return openudid;
	}

	public void setOpenudid(String openudid) {
		this.openudid = openudid;
	}

	public String getDevice_type() {
		return device_type;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}

	public String getOs_version() {
		return os_version;
	}

	public void setOs_version(String os_version) {
		this.os_version = os_version;
	}

	public String getOs_api() {
		return os_api;
	}

	public void setOs_api(String os_api) {
		this.os_api = os_api;
	}


	public String getDevice_platform() {
		return device_platform;
	}

	public void setDevice_platform(String device_platform) {
		this.device_platform = device_platform;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getBuild_number() {
		return build_number;
	}

	public void setBuild_number(String build_number) {
		this.build_number = build_number;
	}

	public String getApp_version() {
		return app_version;
	}

	public void setApp_version(String app_version) {
		this.app_version = app_version;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getAc() {
		return ac;
	}

	public void setAc(String ac) {
		this.ac = ac;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}


	public String getMax_cursor() {
		return max_cursor;
	}

	public void setMax_cursor(String max_cursor) {
		this.max_cursor = max_cursor;
	}

	public String getMin_cursor() {
		return min_cursor;
	}

	public void setMin_cursor(String min_cursor) {
		this.min_cursor = min_cursor;
	}

	public String getPull_type() {
		return pull_type;
	}

	public void setPull_type(String pull_type) {
		this.pull_type = pull_type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

}
