package app.test.admin;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import app.base.BaseTest;

public class RoleTest extends BaseTest{

	private String baseUri = "/role/";
	
	@Test
	public void createRole() {
		Map<String,String> params = new HashMap<String,String>();
		params.put("name","系统管理员hello");
		params.put("id", "489483653689249792");
		params.put("sessionId", sessionId);
		this.request(baseUri+"save.action", params);
	}
	

	@Test
	public void query_role() {
		Map<String,String> params = new HashMap<String,String>();
		params.put("userId", "488828297921302528");
		params.put("sessionId", sessionId);
		this.request("/user_info/query_user_role.action", params);
	}
	
}
