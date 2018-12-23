package hao.webapp.demo.dao.user_info;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import hao.framework.core.expression.HaoException;
import hao.framework.database.dao.JDBCDao;
import hao.framework.database.dao.sql.SQLWrite;
import hao.framework.database.dao.sql.SQLWrite.SQLWriteAction;
import hao.webapp.demo.model.sys.SysUserRole;
import hao.webapp.demo.model.sys.UserInfo;

@Repository
public class UserInfoDao {
	
	@Autowired
	private JDBCDao dao;

	/**
	 * 插入一个新账号
	 * @param id
	 * @param account
	 * @throws HaoException 
	 */
	public void insertNewUserInfo(long id, String account,String name) throws HaoException {
		SQLWrite write = new SQLWrite(UserInfo.class,SQLWriteAction.insert);
		write.addField("id",id);
		write.addField("nickname", account);//账号默认是昵称
		write.addField("name", name);//账号默认是昵称
		dao.execute(write);
	}

	/**
	 * 删除用户
	 * @param id
	 * @throws HaoException 
	 */
	public void delUserInfo(String id) throws HaoException {
		this.dao.delete(UserInfo.class, "id", id);
	}

	/**
	 * 查询用户的角色
	 * @param userId
	 * @return
	 * @throws HaoException 
	 */
	public List<SysUserRole> queryUserRole(String userId) throws HaoException {
		return this.dao.queryBean(SysUserRole.class, "userId", userId);
	}

	
	/**
	 * 删除用户角色
	 * @param userId
	 * @throws HaoException 
	 */
	public void delUserRole(String userId) throws HaoException {
		this.dao.delete(SysUserRole.class, "userId", userId);
	}
	
	/**
	 * 插入用户角色
	 * @param userId
	 * @param roles
	 */
	public void inserUserRole(String userId, String[] roles) {
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for(String r:roles) {
			Object[] arg = new Object[2];
			arg[0]=userId;
			arg[1]=r;
			batchArgs.add(arg);
		}
		this.dao.batchInsert(SysUserRole.class, new String[] {"userId","roleId"}, batchArgs);
	}
	
	
	
}
