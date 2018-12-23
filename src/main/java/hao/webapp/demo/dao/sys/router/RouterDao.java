package hao.webapp.demo.dao.sys.router;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import hao.framework.core.expression.HaoException;
import hao.framework.database.dao.JDBCDao;
import hao.framework.database.dao.sql.SQLRead;
import hao.framework.database.dao.sql.SQLWrite;
import hao.framework.database.dao.sql.SQLWrite.SQLWriteAction;
import hao.webapp.demo.model.sys.SysRoleRouter;
import hao.webapp.demo.model.sys.SysRouter;

@Repository
public class RouterDao {

	@Autowired
	private JDBCDao dao;
	
	
	/**
	 * 统计路由数量
	 * @param path
	 * @return
	 * @throws HaoException
	 */
	public int queryPathCount(String path) throws HaoException {
		SQLRead read = new SQLRead(SysRouter.class);
		read.addCondition("path", path);
		return dao.queryCount(read);
	}
	
	public void insertRouter(String path) throws HaoException {
		SQLWrite write = new SQLWrite(SysRouter.class,SQLWriteAction.insert);
		write.addField("path", path);//路由信息
		write.addField("auth", true);//默认需要认证
		this.dao.execute(write);
	}

	/**
	 * 删除路由
	 * @param url
	 * @throws HaoException 
	 */
	public void del(String url) throws HaoException {
		this.dao.delete(SysRouter.class, "path","path");
	}

	/**
	 * 删除角色和路由
	 * @param url
	 * @throws HaoException 
	 */
	public void delRoleRouter(String url) throws HaoException {
		this.dao.delete(SysRoleRouter.class, "path","path");
	}

	/**
	 * 查询路由
	 * @return
	 * @throws HaoException 
	 */
	public List<SysRouter> queryList() throws HaoException {
		return this.dao.queryList(SysRouter.class);
	}

	public List<SysRoleRouter> queryRoleRouterList(String roleId) throws HaoException {
		return this.dao.queryList(SysRoleRouter.class, "roleId", roleId);
	}

	public void delRoleRouterByRoleId(String roleId) throws HaoException {
		this.dao.delete(SysRoleRouter.class, "roleId", roleId);
	}

	/**
	 * 批量更新
	 * @param roleId
	 * @param routerIds
	 */
	public void insertRoleRouter(String roleId, String[] paths) {
		List<Object[]> batchargs = new ArrayList<Object[]>();
		for(String s:paths) {
			Object[] item = new Object[2];
			item[0] = roleId;
			item[1] = s;
		}
		this.dao.batchInsert(SysRoleRouter.class, new String[] {"roleId","path"},batchargs);
	}

	
	public void changeAuth(String path, boolean b) throws HaoException {
		SQLWrite write = new SQLWrite(SysRouter.class,SQLWriteAction.update);
		write.addCondition("path", path);
		write.addField("auth",b);
		this.dao.execute(write);
	}

	public void insertRouter(String path, boolean isAuth) throws HaoException {
		SQLWrite write = new SQLWrite(SysRouter.class,SQLWriteAction.insert);
		write.addField("path", path);
		write.addField("auth",isAuth);
		this.dao.execute(write);
	}

	public SysRouter queryRouterByPath(String path) throws HaoException {
		return dao.queryBean(SysRouter.class,"path",path);
	}
	
}
