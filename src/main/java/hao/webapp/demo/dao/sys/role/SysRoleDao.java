package hao.webapp.demo.dao.sys.role;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import hao.framework.core.expression.HaoException;
import hao.framework.database.dao.JDBCDao;
import hao.framework.database.dao.sql.SQLRead;
import hao.webapp.demo.model.sys.SysRole;
import hao.webapp.demo.model.sys.SysRoleInterface;
import hao.webapp.demo.model.sys.SysRoleRouter;

@Repository
public class SysRoleDao {
	
	@Autowired
	private JDBCDao dao;



	/**
	 * 查询接口对应的角色
	 * @param uri
	 * @return
	 * @throws HaoException
	 */
	public List<SysRole> queryRoleByInterface(String apiId) throws HaoException {
		SQLRead read = new SQLRead(SysRole.class);
		read.join(SysRoleInterface.class, "id", "roleId");
		read.addCondition(SysRoleInterface.class,"interfaceId",apiId);
		List<SysRole> list =  dao.queryList(read);
		return list;
	}



	/**
	 * 查询路由需要的角色
	 * @param path
	 * @return
	 * @throws HaoException 
	 */
	public List<SysRole> queryRoleByRouter(String path) throws HaoException {
		SQLRead read = new SQLRead(SysRole.class);
		read.join(SysRoleRouter.class, "id","roleId");
		read.addCondition(SysRoleRouter.class,"path",path);
		return this.dao.queryList(read);
	}
	
	
	
}
