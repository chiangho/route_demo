package hao.webapp.demo.dao.sys.menu;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import hao.framework.core.expression.HaoException;
import hao.framework.database.dao.JDBCDao;
import hao.framework.database.dao.sql.SQLCommon.SqlOperator;
import hao.framework.database.dao.sql.SQLRead;
import hao.framework.database.dao.sql.SQLWrite;
import hao.framework.database.dao.sql.SQLWrite.SQLWriteAction;
import hao.webapp.demo.model.sys.SysMenu;
import hao.webapp.demo.model.sys.SysRoleMenu;

@Repository
public class MenuDao {

	@Autowired
	private JDBCDao dao;
	
	/**
	 * 查询菜单依据角色
	 * @param roleId
	 * @return
	 * @throws HaoException 
	 */
	public List<SysMenu> querySysMenuByRoleId(String roleId) throws HaoException{
		SQLRead read = new SQLRead(SysMenu.class);
		read.join(SysRoleMenu.class, "id", "menuId");
		read.addCondition(SysRoleMenu.class, "roleId", roleId);
		return dao.queryList(read);
	}

	/**
	 * 删除角色和菜单
	 * @param roleId
	 * @param id
	 * @return 
	 * @throws HaoException 
	 */
	public void delRoleMenu(String roleId, String menuId) throws HaoException {
		SQLWrite write = new SQLWrite(SysRoleMenu.class,SQLWriteAction.delete);
		write.addCondition("menuId", menuId);
		write.addCondition("roleId", roleId);
		dao.execute(write);
	}

	/**
	 * 根据路径查询菜单
	 * @param path
	 * @return
	 * @throws HaoException 
	 */
	public SysMenu queryMenuByPath(String path) throws HaoException {
		return this.dao.queryBean(SysMenu.class,"url", path);
	}

	/**
	 * 查询上级目录
	 * @param id
	 * @return
	 * @throws HaoException 
	 */
	public List<SysMenu> queryFatherMenu(long id) throws HaoException {
		List<SysMenu> menus = new ArrayList<SysMenu>();
		SysMenu menu = this.dao.queryBean(SysMenu.class,"id", id);
		while(menu!=null&&menu.getParentId()!=SysMenu.RootMenuId) {
			 menu = this.dao.queryBean(SysMenu.class,"id", menu.getParentId());
			 menus.add(menu);
		}
		return menus;
	}

	
	public int queryRoleMenuCount(String roleId, long menuId) throws HaoException {
		SQLRead read = new SQLRead(SysRoleMenu.class);
		read.addCondition("roleId", roleId);
		read.addCondition("menuId", menuId);
		return this.dao.queryCount(read);
	}

	public void insertRoleMenu(String roleId, long menuId) throws HaoException {
		SQLWrite write = new SQLWrite(SysRoleMenu.class,SQLWriteAction.insert);
		write.addCondition("roleId", roleId);
		write.addCondition("menuId", menuId);
		dao.execute(write);
	}

	public SysMenu queryMenuById(String menuId) throws HaoException {
		return dao.queryBean(SysMenu.class, "id",menuId);
	}

	/**
	 * 查询子节点
	 * @param parentId
	 * @return
	 * @throws HaoException 
	 */
	public List<SysMenu> queryChildMenu(long parentId) throws HaoException {
		return dao.queryList(SysMenu.class, "parentId", parentId);
	}

	/**
	 * 查询角色，菜单被选中的数量
	 * @param roleId
	 * @param menuIds
	 * @return
	 * @throws HaoException
	 */
	public int querySelectedRoleMenu(String roleId, String[] menuIds) throws HaoException {
		SQLRead read = new SQLRead(SysRoleMenu.class);
		read.addCondition("roleId", roleId);
		read.addCondition("menuId", SqlOperator.in,menuIds);
		return dao.queryCount(read);
	}
	
}
