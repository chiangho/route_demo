package hao.webapp.demo.dao.account;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import hao.framework.core.expression.HaoException;
import hao.framework.core.seq.Sequence;
import hao.framework.core.utils.MD5;
import hao.framework.database.dao.JDBCDao;
import hao.framework.database.dao.sql.SQLCommon.SqlLinkOperator;
import hao.framework.database.dao.sql.SQLCommon.SqlOperator;
import hao.framework.database.dao.sql.SQLCommon.SqlSort;
import hao.framework.database.dao.sql.SQLRead;
import hao.framework.database.dao.sql.SQLWrite;
import hao.framework.database.dao.sql.SQLWrite.SQLWriteAction;
import hao.webapp.demo.model.sys.AccountInfo;

@Repository
public class AccountDao {
	
	@Autowired
	private JDBCDao dao;
	
	/**
	 * 查阅账号
	 * @param account
	 * @return
	 * @throws HaoException
	 */
	public AccountInfo queryAccount(String account) throws HaoException {
		return dao.queryBean(AccountInfo.class, "account",account);
	}

	/**
	 * 修改密码
	 * @param account
	 * @param encode
	 * @throws HaoException 
	 */
	public void changePassword(String account, String password) throws HaoException {
		SQLWrite write  = new SQLWrite(AccountInfo.class,SQLWriteAction.update);
		write.addCondition("account", account);
		write.addField("password", password);
		this.dao.execute(write);
	}

	/***
	 * 删除账号
	 * @param account
	 * @throws HaoException 
	 */
	public void delAccount(String id) throws HaoException {
		this.dao.delete(AccountInfo.class, "id", id);
	}

	/**
	 * 根据提供的字段模糊查询账号
	 * @param content
	 * @return
	 */
	public List<AccountInfo> fuzzyQueryAccount(String content) {
		SQLRead read = new SQLRead(AccountInfo.class);
		try {
			if(!StringUtils.isEmpty(content)) {
				read.addCondition("account", SqlOperator.like,content,SqlLinkOperator.or);
				read.addCondition("phone", SqlOperator.like,content,SqlLinkOperator.or);
				read.addCondition("email", SqlOperator.like,content,SqlLinkOperator.or);
			}
			read.order("status",SqlSort.desc);//账号降序排序
			List<AccountInfo> list  = this.dao.queryList(read);
			return list;
		} catch (HaoException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据主键查用用户信息
	 * @param id
	 * @return
	 */
	public AccountInfo queryById(String id) {
		try {
			return this.dao.queryBean(AccountInfo.class, "id", id);
		} catch (HaoException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询账号数量
	 * @param account
	 * @return
	 * @throws HaoException 
	 */
	public int queryAccountCount(String account) throws HaoException {
		int count = this.dao.queryCount(AccountInfo.class, "account", account);
		return count;
	}

	/**
	 * 新建一条账号纪录
	 * @param account
	 * @param password
	 * @throws HaoException 
	 */
	public long insert(String account, String password,boolean isSuperAdmin) throws HaoException {
		SQLWrite write = new SQLWrite(AccountInfo.class,SQLWriteAction.insert);
		long id  = Sequence.getNextId();
		write.addField("id",id);
		write.addField("account",account);
		write.addField("password",MD5.encode(password));
		write.addField("userId", id);
		if(isSuperAdmin) {
			write.addField("isSuperAdmin", 1);
		}else {
			write.addField("isSuperAdmin", 0);
		}
		this.dao.execute(write);
		return id;
	}

	/**
	 * 修改账号状态
	 * @param status
	 * @throws HaoException 
	 */
	public void changeAccountStatus(String account,int status) throws HaoException {
		SQLWrite write = new SQLWrite(AccountInfo.class,SQLWriteAction.update);
		write.addCondition("account", account);
		write.addField("status", status);
		this.dao.execute(write);
	}

	public AccountInfo queryAccountInfo(String access, String phone, String email) throws HaoException {
		SQLRead read = new SQLRead(AccountInfo.class);
		read.addCondition("status", 1);//查询有效账号
		if(!StringUtils.isEmpty(access)) {
			read.addCondition("account", access);
			return this.dao.queryBean(read);
		}
		if(!StringUtils.isEmpty(phone)) {
			read.addCondition("phone", phone);
			return this.dao.queryBean(read);
		}
		if(!StringUtils.isEmpty(email)) {
			read.addCondition("email", email);
			return this.dao.queryBean(read);
		}
		return null;
	}
	
}
