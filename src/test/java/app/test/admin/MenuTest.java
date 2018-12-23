package app.test.admin;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import app.base.BaseTest;

public class MenuTest extends BaseTest{

	
	@Test
	public void queryUserMenu() {
		Map<String,String> params = new HashMap<String,String>();
		params.put("sessionId", sessionId);
		this.request("/sys/menu/query_user_menu.action", params);
	}
	

	@Test
	public void query_role() {
		Map<String,String> params = new HashMap<String,String>();
		params.put("userId", "488828297921302528");
		params.put("sessionId", sessionId);
		this.request("/user_info/query_user_role.action", params);
	}
	
}
