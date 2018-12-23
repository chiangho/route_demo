package hao.webapp.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hao.framework.core.expression.HaoException;
import hao.webapp.demo.dao.account.AccountDao;
import hao.webapp.demo.model.sys.AccountInfo;

@Service
public class LoginService {

	@Autowired
	private AccountDao accountDao;
	/**
	 * 根据账号，或者手机，或者邮箱获取用户信息
	 * @param access
	 * @param phone
	 * @param email
	 * @return
	 * @throws HaoException 
	 */
	public AccountInfo getAccountInfo(String access,String phone,String email) throws HaoException {
		AccountInfo accountInfo = this.accountDao.queryAccountInfo(access,phone,email);
		return accountInfo;
	}

	/**
	 * 根据账号，手机，邮箱查询此人的密码
	 * @param username
	 * @param mobilPhone
	 * @param email
	 * @return
	 * @throws HaoException 
	 */
	public String queryPassword(String access, String phone, String email) throws HaoException {
		AccountInfo accountInfo = this.accountDao.queryAccountInfo(access,phone,email);
		if(accountInfo!=null) {
			return accountInfo.getPassword();
		}
		return null;
	}
	
}
