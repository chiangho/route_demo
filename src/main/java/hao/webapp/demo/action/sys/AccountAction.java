package hao.webapp.demo.action.sys;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import hao.framework.core.expression.HaoException;
import hao.framework.web.context.RequestContext;
import hao.framework.web.view.JSONView;
import hao.webapp.demo.model.sys.AccountInfo;
import hao.webapp.demo.service.sys.AccountService;

/**
 * 账号管理
 * @author chianghao
 *
 */
@Controller
@RequestMapping("account")
public class AccountAction {
	
	@Autowired
	AccountService accountService;
	/**
	 * 查询账号
	 * @param content 检索内容
	 * @return
	 */
	@RequestMapping("query_all")
	public JSONView queryAll(
			@RequestParam(value="content",required=false,defaultValue="") String content
			) {
		List<AccountInfo> list = accountService.queryAccountList(content);
		RequestContext.setModelData("account-list", list);
		return RequestContext.getJSONView();
	}
	
	/**
	 * 根据主键查询账号信息
	 * @param id
	 * @return
	 * @throws HaoException
	 */
	@RequestMapping("query")
	public JSONView query(
			@RequestParam(value="id") String id
			) throws HaoException {
		AccountInfo accountInfo = accountService.queryAccount(id);
		accountInfo.setPassword("");//将返回的密码设置为空，防止密码进入前端导致的安全问题
		RequestContext.setModelData("account", accountInfo);
		return RequestContext.getJSONView();
	}

	/**
	 * 创建账号密码
	 * @param account
	 * @param password
	 * @return
	 * @throws HaoException 
	 */
	@RequestMapping("create_account")
	public JSONView createAccount(
			@RequestParam(value="account") String account,
			@RequestParam(value="password") String password,
			@RequestParam(value="_password") String _password
			) throws HaoException {
		accountService.createAccount(account,password,_password,false);
		return RequestContext.getJSONView();
	}
	
	/**
	 * 重置密码
	 * @param account
	 * @return
	 * @throws HaoException
	 */
	@RequestMapping("reset_password")
	public JSONView resetPassword(
			@RequestParam(value="account") String account
			) throws HaoException {
		accountService.resetPassword(account);
		return RequestContext.getJSONView();
	}
	
	/**
	 * 修改密码
	 * @param account
	 * @param password
	 * @param oldPasswrd
	 * @return
	 * @throws HaoException
	 */
	@RequestMapping("change_password")
	public JSONView changePassword(
			@RequestParam(value="account") String account,
			@RequestParam(value="password") String password,
			@RequestParam(value="old_passwrd") String oldPasswrd) throws HaoException {
		accountService.changePassword(account, oldPasswrd, password);
		return RequestContext.getJSONView();
	}
	
	/**
	 * 删除账号
	 * @param account
	 * @return
	 * @throws HaoException
	 */
	@RequestMapping("del_account")
	public JSONView delAccount(
			@RequestParam(value="id") String id) throws HaoException {
		accountService.delAccount(id);
		return RequestContext.getJSONView();
	}
	
	/**
	 * 禁用账号
	 * @param account
	 * @return
	 * @throws HaoException
	 */
	@RequestMapping("disable_account")
	public JSONView disable_account(
			@RequestParam(value="account") String account
			) throws HaoException {
		accountService.disableAccount(account);
		return RequestContext.getJSONView();
	}
	
	/**
	 * 启用账号
	 * @param account
	 * @return
	 * @throws HaoException
	 */
	@RequestMapping("enable_account")
	public JSONView enable_account(
			@RequestParam(value="account") String account
			) throws HaoException {
		accountService.enableAccount(account);
		return RequestContext.getJSONView();
	}
}
