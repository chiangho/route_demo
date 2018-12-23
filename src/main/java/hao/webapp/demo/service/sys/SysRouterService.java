package hao.webapp.demo.service.sys;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hao.framework.core.expression.HaoException;
import hao.webapp.demo.dao.sys.menu.MenuDao;
import hao.webapp.demo.dao.sys.router.RouterDao;
import hao.webapp.demo.model.sys.SysMenu;
import hao.webapp.demo.model.sys.SysRoleRouter;
import hao.webapp.demo.model.sys.SysRouter;

@Service
public class SysRouterService {

	@Autowired
	private RouterDao routerDao;
	
	@Autowired
	private MenuDao menuDao;
	
	@Autowired
	private SysRoleService roleService;
	
	/**
	 * 打开认证
	 * @param path
	 * @throws HaoException 
	 */
	@Transactional(rollbackFor=Throwable.class)
	public void openAuth(String path) throws HaoException {
		this.routerDao.changeAuth(path,true);
	}
	/**
	 * 关闭认证
	 * @param path
	 * @throws HaoException 
	 */
	@Transactional(rollbackFor=Throwable.class)
	public void closeAuth(String path) throws HaoException {
		this.routerDao.changeAuth(path,false);
	}
	/**
	 * 创建路由
	 * @param url
	 * @throws HaoException
	 */
	@Transactional(rollbackFor=Throwable.class)
	public void createPath(String url) throws HaoException {
		if(StringUtils.isEmpty(url)) {
			throw new HaoException("999999","路由缺失，不可创建");
		}
		int pathCount = this.routerDao.queryPathCount(url);
		if(pathCount==0) {
			this.routerDao.insertRouter(url);
		}
	}
	
	@Transactional(rollbackFor=Throwable.class)
	public void editPath(String url,String auth) throws HaoException {
		if(StringUtils.isEmpty(url)) {
			throw new HaoException("999999","路由缺失，不可创建");
		}
		boolean isAuth = true;
		if(!StringUtils.isEmpty(auth)) {
			if(auth.equals("0")){
				isAuth = false;
			}
		}
		int pathCount = this.routerDao.queryPathCount(url);
		if(pathCount==0) {
			this.routerDao.insertRouter(url,isAuth);
		}else {
			this.routerDao.changeAuth(url,isAuth);
		}
	}
	
	/**
	 * 删除路由
	 * @param url
	 * @throws HaoException
	 */
	@Transactional(rollbackFor=Throwable.class)
	public void delRouterByPath(String url) throws HaoException {
		if(StringUtils.isEmpty(url)) {
			throw new HaoException("999999","路由缺失，不可创建");
		}
		this.routerDao.del(url);//删除路由
		this.routerDao.delRoleRouter(url);//删除路由对应的角色
	}

	/**
	 * 查询路由
	 * @return
	 * @throws HaoException 
	 */
	public List<SysRouter> queryList() throws HaoException {
		List<SysRouter> list = this.routerDao.queryList();
		return list;
	}

	/**
	 * 查询角色的路由
	 * @param roleId
	 * @return
	 * @throws HaoException
	 */
	public List<SysRoleRouter> queryRoleRouter(String roleId) throws HaoException {
		return this.routerDao.queryRoleRouterList(roleId);
	}

	/**
	 * 保存角色的路由
	 * @param roleId
	 * @param routerIds
	 * @throws HaoException 
	 */
	@Transactional(rollbackFor=Throwable.class)
	public void saveRoleRouter(String roleId, String[] paths) throws HaoException {
		if(paths==null||paths.length==0) {
			throw new HaoException("999999","路由信息缺少，不可保存");
		}
		this.routerDao.delRoleRouterByRoleId(roleId);
		this.routerDao.insertRoleRouter(roleId,paths);
		
		//获取已经选择的菜单信息
		List<SysMenu> menuList = menuDao.querySysMenuByRoleId(roleId);
		for(SysMenu m:menuList) {
			if(!StringUtils.isEmpty(m.getUrl())) {
				boolean has = false;
				for(String path:paths) {
					if(m.getUrl().equals(path)) {
						has = true;
						break;
					}
				}
				if(has==false) {
					roleService.delRoleMenu(roleId,m.getId()+"");
				}
			}
		}
		
		for(String path:paths) {
			boolean has = false;
			for(SysMenu m:menuList) {
				if(!StringUtils.isEmpty(m.getUrl())) {
					if(m.getUrl().equals(path)) {
						has = true;
						break;
					}
				}
			}
			//新增加
			if(has==false) {
				roleService.createRoleMenu(roleId,path);
			}
		}
	}
	
	/**
	 * 根据路径查询路由信息
	 * @param path
	 * @return
	 * @throws HaoException 
	 */
	public SysRouter queryRouterByPath(String path) throws HaoException {
		SysRouter router = this.routerDao.queryRouterByPath(path);
		return router;
	}
	
}
