package app.test.admin;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import app.base.BaseTest;

/**
 * 登录测试
 * @author chianghao
 *
 */
public class LoginTest extends BaseTest{


	@Test
	public void login() {
		Map<String,String> params = new HashMap<String,String>();
		params.put("account", "admin");
		params.put("password","111111");
		this.request("/login.action", params);
	}
	
}
