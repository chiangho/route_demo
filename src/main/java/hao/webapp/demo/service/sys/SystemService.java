package hao.webapp.demo.service.sys;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hao.framework.core.expression.HaoException;
import hao.framework.database.dao.JDBCDao;
import hao.webapp.demo.model.sys.SysMenu;
import hao.webapp.demo.model.sys.SysRouter;

@Service
public class SystemService {

	
	@Autowired
	private JDBCDao         dao;
	
	/**
	 * 初始化系统
	 * @throws HaoException
	 */
	@Transactional(rollbackFor=Throwable.class)
	public void initSystem() throws HaoException {
		//创建菜单
		List<Object[]> batchSysMenuArgs = new ArrayList<Object[]>();
		batchSysMenuArgs.add(new Object[] {1,SysMenu.RootMenuId,"系统管理","","0"});
		batchSysMenuArgs.add(new Object[] {2,1,"账号管理","/home/sys/account","0"});
		batchSysMenuArgs.add(new Object[] {3,1,"菜单管理","/home/sys/menu","1"});
		batchSysMenuArgs.add(new Object[] {4,1,"角色管理","/home/sys/role","2"});
		batchSysMenuArgs.add(new Object[] {5,1,"接口管理","/home/sys/interface","3"});
		batchSysMenuArgs.add(new Object[] {6,1,"路由管理","/home/sys/router","4"});
		dao.batchInsert(
				SysMenu.class, 
				new String[] {"id","parentId","name","url","sort"}, 
				batchSysMenuArgs);
		
		
		//批量更新路由表
		List<Object[]> batchSysRouterArgs = new ArrayList<Object[]>();
		batchSysRouterArgs.add(new Object[] {"/home/sys/account",true});
		batchSysRouterArgs.add(new Object[] {"/home/sys/menu",true});
		batchSysRouterArgs.add(new Object[] {"/home/sys/role",true});
		batchSysRouterArgs.add(new Object[] {"/home/sys/interface",true});
		batchSysRouterArgs.add(new Object[] {"/home/sys/router",true});
		dao.batchInsert(
				SysRouter.class, 
				new String[] {"path","auth"}, 
				batchSysRouterArgs);
		
		//accountService.createAccount("admin", "111111", "111111",true);
	}
	
}
