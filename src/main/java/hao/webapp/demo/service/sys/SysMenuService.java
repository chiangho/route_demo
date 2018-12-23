package hao.webapp.demo.service.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hao.framework.core.expression.HaoException;
import hao.framework.core.seq.Sequence;
import hao.framework.core.utils.StringUtils;
import hao.framework.database.dao.JDBCDao;
import hao.framework.database.dao.sql.SQLCommon.SqlOperator;
import hao.framework.database.dao.sql.SQLRead;
import hao.framework.database.dao.sql.SQLWrite;
import hao.framework.database.dao.sql.SQLWrite.SQLWriteAction;
import hao.webapp.demo.model.sys.AccountInfo;
import hao.webapp.demo.model.sys.SysMenu;
import hao.webapp.demo.model.sys.SysRoleMenu;
import hao.webapp.demo.utils.MenuUtil;

/**
 * 菜单
 * @author chianghao
 *
 */
@Service
public class SysMenuService {

	@Autowired
	private JDBCDao dao;
	
	@Autowired
	private SysRouterService routerService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	SysRoleService roleService;
	
	@Autowired
	AccountService accountService;
	/**
	 * 编辑和保存
	 * @param id
	 * @param name
	 * @throws HaoException
	 */
	@Transactional(rollbackFor=Throwable.class)
	public void save(
			String id,
			String parentId,
			String name,
			String url,
			String sort
		) throws HaoException {
		//校验必填项
		if(name==null||name.equals("")) {
			throw new HaoException("999999", "菜单名称不能为空！");
		}
		if(parentId==null||parentId.equals("")) {
			throw new HaoException("999999", "菜单的父节点不能为空！");
		}
		
		boolean isInsert = true;
		if(id!=null&&!id.equals("")) {
			//通过查询获取数据库中是否含有此id
			int count = dao.queryCount(SysMenu.class,"id",id);
			if(count==1) {
				isInsert = false;
			}
		}else {
			id =  Sequence.getNextId()+"";
		}
		
		//菜单同一级不能重名
		int nameSize = 0;
		if(isInsert) {
			SQLRead read = new SQLRead(SysMenu.class);
			read.addCondition("name", name);
			read.addCondition("parentId", parentId);
			nameSize = dao.queryCount(read);
		}else{
			SQLRead searchName = new SQLRead(SysMenu.class);
			searchName.addCondition("name", name);
			searchName.addCondition("id", SqlOperator.notEqual, id);
			searchName.addCondition("parentId", parentId);
			nameSize = dao.queryCount(searchName);
		}
		if(nameSize!=0) {
			throw new HaoException("999999", "同一级菜单内名称不能相同！");
		}
		
		SQLWrite write  = null;
		Map<String,Object> fieldMap = new HashMap<String,Object>();
		fieldMap.put("id",id);
		fieldMap.put("name",name);
		fieldMap.put("parentId",parentId);
		fieldMap.put("url",url);
		fieldMap.put("sort",sort);
		if(isInsert) {
			write = new SQLWrite(SysMenu.class,SQLWriteAction.insert);
		}else {
			write = new SQLWrite(SysMenu.class,SQLWriteAction.update);
			write.addCondition("id", id);
		}
		write.setFields(fieldMap);
		dao.execute(write);
		if(!StringUtils.isEmpty(url)) {
			routerService.createPath(url);
		}
	}
	
	/**
	 * 删除菜单
	 * @param id
	 * @throws HaoException
	 */
	@Transactional(rollbackFor=Throwable.class)
	public void del(String id) throws HaoException {
		//查询菜单信息
		SysMenu menu = this.dao.queryBean(SysMenu.class, "id", id);
		dao.deleteById(SysMenu.class,id);
		dao.delete(SysRoleMenu.class,"menuId",id);
		if(!StringUtils.isEmpty(menu.getUrl())) {
			//如果路由存在则删除路由信息
			routerService.delRouterByPath(menu.getUrl());
		}
		dao.delete(SysRoleMenu.class,"menuId", id);
	}
	
	
	/**
	 * 传入父亲节点查询其直接点
	 * @throws HaoException 
	 */
	public List<SysMenu> queryMenu(String parentId) throws HaoException{
		//查询获取所以菜单的数据
		List<SysMenu> allMenu = dao.queryList(SysMenu.class);
		List<SysMenu> tagMenus = MenuUtil.getChildrenMenu(allMenu,parentId);
		return tagMenus;
	}
	
	/**
	 * 查询某一个菜单的明细
	 * @param id
	 * @return
	 * @throws HaoException
	 */
	public SysMenu queryBean(String id) throws HaoException {
		return dao.queryBean(SysMenu.class, "id",id);
	}

	public List<SysMenu> queryMenuByUserId(String currentUserId) throws HaoException {
		Set<String> sysroles  = userInfoService.getRoles(currentUserId);
		AccountInfo account   = accountService.queryAccount(currentUserId);
		List<SysMenu> menus   = roleService.queryMenus(sysroles,account.getIsSuperAdmin());
		return menus;
	}
	
	
}
