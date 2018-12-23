package hao.webapp.demo.service.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hao.framework.core.expression.HaoException;
import hao.framework.core.seq.Sequence;
import hao.framework.database.dao.JDBCDao;
import hao.framework.database.dao.sql.SQLCommon.SqlOperator;
import hao.framework.database.dao.sql.SQLRead;
import hao.framework.database.dao.sql.SQLWrite;
import hao.framework.database.dao.sql.SQLWrite.SQLWriteAction;
import hao.webapp.demo.dao.sys.menu.MenuDao;
import hao.webapp.demo.dao.sys.role.SysRoleDao;
import hao.webapp.demo.model.sys.SysInterface;
import hao.webapp.demo.model.sys.SysMenu;
import hao.webapp.demo.model.sys.SysRole;
import hao.webapp.demo.model.sys.SysRoleInterface;
import hao.webapp.demo.model.sys.SysRoleMenu;
import hao.webapp.demo.model.sys.SysRoleRouter;
import hao.webapp.demo.model.sys.SysRouter;
import hao.webapp.demo.utils.MenuUtil;

/**
 * 角色服务
 * @author chianghao
 *
 */
@Service
public class SysRoleService {

	@Autowired
	private JDBCDao dao;
	
	@Autowired
	private SysRoleDao roleDao;
	
	@Autowired
	private SysRouterService routerService;
	
	@Autowired
	private MenuDao menuDao;
	
//	@Autowired
//	private AccountService accountService;
	
	/**
	 * 编辑和保存
	 * @param id
	 * @param name
	 * @throws HaoException
	 */
	@Transactional(rollbackFor=Throwable.class)
	public void save(String id,String name) throws HaoException {
		//校验必填项
		if(name==null||name.equals("")) {
			throw new HaoException("999999", "角色不能为空！");
		}
		
		boolean isInsert = true;
		if(id!=null&&!id.equals("")) {
			//通过查询获取数据库中是否含有此id
			int count = dao.queryCount(SysRole.class,"id",id);
			if(count==1) {
				isInsert = false;
			}
		}else {
			id =  Sequence.getNextId()+"";
		}
		
		//校验名称是否重复，名称不能重复
		int nameSize = 0;
		if(isInsert) {
			nameSize = dao.queryCount(SysRole.class,"name",name);
		}else{
			SQLRead searchName = new SQLRead(SysRole.class);
			searchName.addCondition("name", name);
			searchName.addCondition("id", SqlOperator.notEqual, id);
			nameSize = dao.queryCount(searchName);
		}
		if(nameSize!=0) {
			throw new HaoException("999999", "角色名称不可重复！");
		}
		
		SQLWrite write  = null;
		Map<String,Object> fieldMap = new HashMap<String,Object>();
		fieldMap.put("id",id);
		fieldMap.put("name",name);
		if(isInsert) {
			write = new SQLWrite(SysRole.class,SQLWriteAction.insert);
		}else {
			write = new SQLWrite(SysRole.class,SQLWriteAction.update);
			write.addCondition("id", id);
		}
		write.setFields(fieldMap);
		dao.execute(write);
	}
	
	/**
	 * 删除角色
	 * @param id
	 * @throws HaoException
	 */
	@Transactional(rollbackFor=Throwable.class)
	public void del(String id) throws HaoException {
		dao.deleteById(SysRole.class,id);
		dao.delete(SysRoleMenu.class,"roleId",id);
		dao.delete(SysRoleRouter.class,"roleId",id);
	}
	
	/**
	 * 查询角色信息
	 * @param name
	 * @return
	 * @throws HaoException
	 */
	public List<SysRole> queryList(String name) throws HaoException{
		SQLRead search = new SQLRead(SysRole.class);
		if(!StringUtils.isEmpty(name)) {
			search.addCondition("name", SqlOperator.like, name);
		}
		return dao.queryList(search);
	}
	
	public SysRole queryBean(String id) throws HaoException {
		return dao.queryBean(SysRole.class, "id",id);
	}
	
	/**
	 * 保存角色拥有的菜单
	 * @param menuIds
	 * @throws HaoException 
	 */
	@Transactional(rollbackFor=Throwable.class)
	public void saveRoleMenu(String roleId,String[] menuIds) throws HaoException {
		SQLWrite del = new SQLWrite(SysRoleMenu.class,SQLWriteAction.delete);
		del.addCondition("roleId",roleId);
		dao.execute(del);
		//添加新的关系
		List<Object[]> writeList = new ArrayList<Object[]>();
		for(String menuId:menuIds) {
			Object item[] = new Object[2];
			item[0] = roleId;
			item[1] = menuId;
			writeList.add(item);
		}
		dao.batchInsert(SysRoleMenu.class,new String[] {"roleId","menuId"},writeList);
		
		//获取所有菜单信息
		List<SysMenu> menuList = dao.queryList(SysMenu.class);
		Set<String> selectPath = new HashSet<String>();
		Set<String> allPath    = new HashSet<String>();
		Set<String> menuIdSet    = new HashSet<String>();
		for(String s:menuIds) {
			menuIdSet.add(s);
		}
		for(SysMenu menu:menuList) {
			if(!StringUtils.isEmpty(menu.getUrl())) {
				allPath.add(menu.getUrl());
				if(menuIdSet.contains(menu.getId()+"")) {
					selectPath.add(menu.getUrl());
				}
			}
		}
		
		//删除原来角色和路由匹配
		SQLWrite delRoleRouter = new SQLWrite(SysRoleRouter.class,SQLWriteAction.delete);
		delRoleRouter.addCondition("path",SqlOperator.in ,allPath.toArray(new String[allPath.size()]));
		this.dao.execute(delRoleRouter);
		//建立新的角色路由匹配
		this.routerService.saveRoleRouter(roleId, selectPath.toArray(new String[selectPath.size()]));
		
	}
	
	/**
	 * 保存用户角色和用户接口
	 * @param roleId
	 * @param interfaceIds
	 * @throws HaoException 
	 */
	public void saveRoleInterface(String roleId,String[] interfaceIds) throws HaoException {
		//dao.delete(SysRoleInterface.class,"roleId",roleId);
		SQLWrite del = new SQLWrite(SysRoleInterface.class,SQLWriteAction.delete);
		del.addCondition("roleId",roleId);
		del.addCondition("interfaceId",SqlOperator.in,interfaceIds);
		dao.execute(del);
		
		//添加新的关系
		List<Object[]> writeList = new ArrayList<Object[]>();
		for(String id:interfaceIds) {
			Object item[] = new Object[2];
			item[0] = roleId;
			item[1] = id;
			writeList.add(item);
		}
		dao.batchInsert(SysRoleInterface.class,new String[] {"roleId","interfaceId"},writeList);
	}
	
	
	
	
	

	/**
	 * 查询角色对应的菜单
	 * @param sysroles
	 * @return
	 * @throws HaoException 
	 */
	public List<SysMenu> queryMenus(Set<String> sysroles,int isSuperAdmin) throws HaoException {
		SQLRead read = new SQLRead(SysMenu.class);
		if(isSuperAdmin==0) {
			//非超级账号通过权限获取菜单
			read.join(SysRoleMenu.class,"id", "menuId");
			read.addCondition(SysRoleMenu.class,"roleId", SqlOperator.in,sysroles.toArray(new Object[sysroles.size()]));
		}
		List<SysMenu> list = dao.queryList(read);
		List<SysMenu> tagMenus = MenuUtil.getChildrenMenu(list,SysMenu.RootMenuId+"");
		return tagMenus;
	}

	/**
	 * 查询角色对应的接口
	 * @param sysroles
	 * @return
	 * @throws HaoException 
	 */
	public List<SysInterface> queryInterfaces(Set<String> sysroles,int isSuperAdmin) throws HaoException {
		SQLRead read = new SQLRead(SysInterface.class);
		if(isSuperAdmin==0) {
			read.join(SysRoleInterface.class,"id", "interfaceId");
			read.addCondition(SysRoleInterface.class,"roleId", SqlOperator.in,sysroles.toArray(new Object[sysroles.size()]));
		}
		List<SysInterface> list = dao.queryList(read);
		return list;
	}

	/***
	 * 根据接口地址获取对应的角色
	 * @param uri
	 * @return
	 * @throws HaoException
	 */
	public Set<String> queryApiRole(String apiId) throws HaoException {
		List<SysRole> roleList  = this.roleDao.queryRoleByInterface(apiId);
		Set<String> set = new HashSet<String>();
		for(SysRole r:roleList) {
			set.add(r.getName());
		}
		return set;
	}

	/**
	 * 查询角色的路由
	 * @param roleId
	 * @return
	 * @throws HaoException
	 */
	public List<SysRouter> queryRoleRouter(String roleId) throws HaoException{
		List<SysRouter> routers = this.routerService.queryList();
		List<SysRoleRouter> roleRouters = this.routerService.queryRoleRouter(roleId);
		Set<String> selectPaths = new HashSet<String>();
		for(SysRoleRouter e:roleRouters) {
			selectPaths.add(e.getPath());
		}
		for(SysRouter sr:routers) {
			if(selectPaths.contains(sr.getPath())) {
				sr.isSelected();
			}
		}
		return routers;
	}
	
	/**
	 * 保存路由
	 * @param roleId
	 * @param routerIds
	 * @throws HaoException
	 */
	@Transactional(rollbackFor=Throwable.class)
	public void saveRoleRouter(String roleId,String paths) throws HaoException {
		if(StringUtils.isEmpty(paths)) {
			throw new HaoException("999999","路由缺失，不可操作！");
		}
		this.routerService.saveRoleRouter(roleId,paths.split(","));
	}

	/**
	 * 查询菜单及表中角色选中的菜单为isSelect
	 * @param roleId
	 * @return
	 * @throws HaoException 
	 */
	public List<SysMenu> queryRoleMenu(String roleId) throws HaoException {
		//查询所有菜单
		SQLRead read = new SQLRead(SysMenu.class);
		List<SysMenu> list = dao.queryList(read);
		//查询角色选中的菜单
		SQLRead queryRoleMenu = new SQLRead(SysRoleMenu.class);
		queryRoleMenu.addCondition("roleId", roleId);
		List<SysRoleMenu> roleMenuList = dao.queryList(queryRoleMenu);
		Set<Long> selectMenuIds = new HashSet<Long>();
		for(SysRoleMenu s:roleMenuList) {
			selectMenuIds.add(s.getMenuId());
		}
		for(SysMenu s:list) {
			if(selectMenuIds.contains(s.getId())) {
				s.setIsSelect(1);//设置为选中
			}
		}
		List<SysMenu> tagMenus = MenuUtil.getChildrenMenu(list,SysMenu.RootMenuId+"");
		return tagMenus;
	}
	
	/**
	 * 删除一个角色和菜单关系
	 * @param roleId
	 * @param menuId
	 * @throws HaoException 
	 */
	@Transactional(rollbackFor=Throwable.class)
	public void delRoleMenu(String roleId,String menuId) throws HaoException {
		SysMenu menu = menuDao.queryMenuById(menuId);
		//如果同级目录全部被删除取消上级选中
		menuDao.delRoleMenu(roleId, menuId);//删除当前节点不选中
		while(menu!=null&&menu.getParentId()!=SysMenu.RootMenuId) {
			 List<SysMenu> childMenus = menuDao.queryChildMenu(menu.getParentId());
			 //查询借点被选中的数目
			 String[] menuIds = new String[childMenus.size()];
			 for(int i=0;i<childMenus.size();i++) {
				 menuIds[i] = childMenus.get(i).getId()+"";
			 }
			 int count = menuDao.querySelectedRoleMenu(roleId,menuIds);
			 if(count==0) {
				 //取消选中父节点
				 menuDao.delRoleMenu(roleId, menu.getParentId()+"");
			 }
		}
		
		
	}
	
	/**
	 * 添加一个角色和菜单关系
	 * @param roleId
	 * @param path
	 * @throws HaoException 
	 */
	@Transactional(rollbackFor=Throwable.class)
	public void createRoleMenu(String roleId, String path) throws HaoException {
		SysMenu menu = menuDao.queryMenuByPath(path);
		//同步将上级勾选
		List<SysMenu> fatherMenus = menuDao.queryFatherMenu(menu.getId());
		for(SysMenu m:fatherMenus) {
			int count = this.menuDao.queryRoleMenuCount(roleId,m.getId());
			if(count==0) {
				menuDao.insertRoleMenu(roleId,m.getId());
			}
		}
	}

	/**
	 * 检查用户权限
	 * @param path
	 * @param userId
	 * @return
	 * @throws HaoException 
	 */
//	public int checkRouterRole(String path, String userId) {
//		if(path.equals("/")) {
//			//特殊处理
//			return 0;
//		}
//		try {
//			//校验路由是否存在
//			SysRouter router = this.routerService.queryRouterByPath(path);
//			if(router==null) {
//				return -4;//路由不存在
//			}
//			if(!router.isAuth()) {
//				return 0;
//			}else {
//				if(StringUtils.isEmpty(userId)) {
//					return -1;
//				}else {
//					//判断是否是超级账号
//					boolean isSuperAdmin = this.accountService.isSuperAdmin(userId);
//					if(isSuperAdmin) {
//						return 0;
//					}
//					List<SysRole> routerRoleList = this.queryRoleByRouter(path);
//					if(routerRoleList==null||routerRoleList.size()==0) {
//						return 0;
//					}
//					Set<String> userRoles = UserUtil.getRoles();
//					boolean hasRole = false;
//					for(SysRole r:routerRoleList) {
//						if(userRoles.contains(r.getName())) {
//							hasRole = true;
//							break;
//						}
//					}
//					if(hasRole) {
//						return 0;
//					}else {
//						return -2;
//					}
//				}
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//			return -3;//校验发生异常了
//		}
//	}

//	/**
//	 * 获取路由对应的角色
//	 * @param path
//	 * @return	
//	 * @throws HaoException 
//	 */
//	private List<SysRole> queryRoleByRouter(String path) throws HaoException {
//		return this.roleDao.queryRoleByRouter(path);
//	}
	
}
