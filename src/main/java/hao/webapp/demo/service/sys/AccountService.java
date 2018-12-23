package hao.webapp.demo.service.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hao.framework.core.expression.HaoException;
import hao.framework.core.spring.Property;
import hao.framework.core.utils.MD5;
import hao.framework.core.utils.StringUtils;
import hao.webapp.demo.dao.account.AccountDao;
import hao.webapp.demo.dao.user_info.UserInfoDao;
import hao.webapp.demo.model.sys.AccountInfo;

/**
 * 账号服务
 * @author chianghao
 *
 */
@Service
public class AccountService {

	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private UserInfoDao userInfoDao;
	
	//修改密码
	@Transactional(rollbackFor=Throwable.class)
	public void changePassword(String account,String oldPassword,String newPassword) throws HaoException {
		if(StringUtils.isEmpty(account)) {
			throw new HaoException("999999","请提供参数【账号】！");
		}
		if(StringUtils.isEmpty(oldPassword)) {
			throw new HaoException("999999","请提供参数【原密码】！");
		}
		if(StringUtils.isEmpty(newPassword)) {
			throw new HaoException("999999","请提供参数【新密码】！");
		}
		AccountInfo accountInfo = accountDao.queryAccount(account);
		if(accountInfo==null) {
			throw new HaoException("999999","账号不存在，请检查后再操作！");
		}
		if(!MD5.encode(oldPassword).toUpperCase().equals(accountInfo.getPassword().toUpperCase())) {
			throw new HaoException("999999", "原密码填写错误！请填写正确原密码");
		}
		accountDao.changePassword(account,MD5.encode(newPassword));
	}
	
	/**
	 * 重置密码
	 * @param account
	 * @throws HaoException 
	 */
	@Transactional(rollbackFor=Throwable.class)
	public void resetPassword(String account) throws HaoException {
		if(StringUtils.isEmpty(account)) {
			throw new HaoException("999999","请提供参数【账号】！");
		}
		AccountInfo accountInfo = accountDao.queryAccount(account);
		if(accountInfo==null) {
			throw new HaoException("999999","账号不存在，请检查后再操作！");
		}
		String password = StringUtils.isEmpty(Property.getValue("DefaultPassword"))?"111111":Property.getValue("DefaultPassword");
		accountDao.changePassword(account,MD5.encode(password));
	}
	
	//删除账号
	@Transactional(rollbackFor=Throwable.class)
	public void delAccount(String id) throws HaoException {
		this.accountDao.delAccount(id);
		this.userInfoDao.delUserInfo(id);
	}

	/**
	 * 根据内容模糊查询账号信息
	 * @param content
	 * @return
	 */
	public List<AccountInfo> queryAccountList(String content) {
		return this.accountDao.fuzzyQueryAccount(content);
	}

	public AccountInfo queryAccount(String id) throws HaoException {
		if(StringUtils.isEmpty(id)) {
			throw new HaoException("999999", "查询账号明细信息需要主键");
		}
		return this.accountDao.queryById(id);
	}

	/**
	 * 创建账号
	 * @param account
	 * @param password
	 * @param _password
	 * @throws HaoException 
	 */
	@Transactional(rollbackFor=Throwable.class)
	public long createAccount(String account, String password, String _password,boolean isSuperAdmin) throws HaoException {
		if(StringUtils.isEmpty(account)) {
			throw new HaoException("999999","请填写参数账号！");
		}
		if(StringUtils.isEmpty(password)) {
			throw new HaoException("999999","密码不能为空！");
		}
		if(StringUtils.isEmpty(_password)) {
			throw new HaoException("999999","确认密码不能为空！");
		}
		if(!password.equals(_password)) {
			throw new HaoException("999999","两次填写的密码不一致！");
		}
		int count = 0;
		try {
			count = this.accountDao.queryAccountCount(account);
		}catch(Exception e) {
			throw new HaoException("999999","查询数据库发生异常！");
		}
		if(count!=0) {
			throw new HaoException("999999",account+"已经存在，请使用其他账号！");
		}
		long id = this.accountDao.insert(account,password,isSuperAdmin);//创建账号
		this.userInfoDao.insertNewUserInfo(id,account,account);//创建用户信息
		return id;
	}

	/**
	 * 禁用账号
	 * @param account
	 * @throws HaoException 
	 */
	@Transactional(rollbackFor=Throwable.class)
	public void disableAccount(String account) throws HaoException {
		if(StringUtils.isEmpty(account)) {
			throw new HaoException("999999", "请递交参数【账号】");
		}
		this.accountDao.changeAccountStatus(account,0);
	}

	@Transactional(rollbackFor=Throwable.class)
	public void enableAccount(String account) throws HaoException {
		if(StringUtils.isEmpty(account)) {
			throw new HaoException("999999", "请递交参数【账号】");
		}
		this.accountDao.changeAccountStatus(account,1);
	}

	/**
	 * 判断是否超级账号
	 * @param userId
	 * @return
	 */
	public boolean isSuperAdmin(String userId) {
		AccountInfo  account = this.accountDao.queryById(userId);
		if(account.getIsSuperAdmin()==1) {
			return true;
		}
		return false;
	}
	
}
