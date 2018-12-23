package app.test.admin;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import app.base.BaseTest;

public class SystemTest extends BaseTest{

	@Test
	public void initSystem() {
		Map<String,String> params = new HashMap<String,String>();
		this.request("/sys/setting/init_system.action", params);
	}
	

}
