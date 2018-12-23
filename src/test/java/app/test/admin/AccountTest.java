package app.test.admin;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import app.base.BaseTest;

/**
 * 账号管理
 * @author chianghao
 */
public class AccountTest extends BaseTest{
	
	private String baseUri = "/account/";
	
	@Test
	public void createAccount() {
		Map<String,String> params = new HashMap<String,String>();
		params.put("account","admin1");
		params.put("password","111111");
		params.put("_password","111111");
		params.put("sessionId", sessionId);
		this.request(baseUri+"create_account.action", params);
	}
	
	@Test
	public void resetPassword() {
		Map<String,String> params = new HashMap<String,String>();
		params.put("account","admin1");
		params.put("sessionId", sessionId);
		this.request(baseUri+"reset_password.action", params);
	}

	@Test
	public void change_password() {
		Map<String,String> params = new HashMap<String,String>();
		params.put("account","admin1");
		params.put("password","111111");
		params.put("old_passwrd","HelloWord");
		params.put("sessionId", sessionId);
		this.request(baseUri+"change_password.action", params);
	}
	
	@Test
	public void queryAll() {
		Map<String,String> params = new HashMap<String,String>();
		params.put("content","admi");
		params.put("sessionId", sessionId);
		this.request(baseUri+"query_all.action", params);
	}
}
