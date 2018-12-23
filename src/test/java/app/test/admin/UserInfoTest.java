package app.test.admin;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import app.base.BaseTest;

public class UserInfoTest extends BaseTest{
	
	private String baseUri = "/role/";
	
	@Test
	public void login() {
		Map<String,String> params = new HashMap<String,String>();
		params.put("userId", "488828297921302528");
		params.put("roles","489483653689249792");
		params.put("sessionId", sessionId);
		this.request("/user_info/save_user_role.action", params);
	}
}
