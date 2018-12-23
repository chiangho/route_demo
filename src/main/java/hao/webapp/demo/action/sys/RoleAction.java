package hao.webapp.demo.action.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hao.framework.core.expression.HaoException;
import hao.framework.web.context.RequestContext;
import hao.framework.web.view.JSONView;
import hao.webapp.demo.model.sys.SysMenu;
import hao.webapp.demo.model.sys.SysRole;
import hao.webapp.demo.model.sys.SysRouter;
import hao.webapp.demo.service.sys.SysRoleService;

/**
 * 角色管理
 * @author chianghao
 *
 */
@Controller
@RequestMapping("sys/role")
public class RoleAction {
	
	@Autowired
	SysRoleService roleService;
	
	
	
	
	/**
	 * 创建角色
	 * @param id
	 * @param name
	 * @return
	 * @throws HaoException
	 */
	@RequestMapping("save")
	public JSONView save(
			@RequestParam(required=false,value="id",defaultValue="") String id,
			@RequestParam("name") String name
			) throws HaoException {
		roleService.save(id, name);
		return RequestContext.getJSONView();
	}
	
	/**
	 * 删除角色
	 * @param id
	 * @return
	 * @throws HaoException
	 */
	@RequestMapping("del")
	public JSONView del(
			@RequestParam("id") String id
			) throws HaoException {
		roleService.del(id);
		return RequestContext.getJSONView();
	}
	
	/**
	 * 查询所有角色
	 * @return
	 * @throws HaoException
	 */
	@RequestMapping("query")
	public JSONView query() throws HaoException {
		List<SysRole> list = roleService.queryList("");
		RequestContext.setModelData("list", list);
		return RequestContext.getJSONView();
	}
	
	/**
	 * 查询角色的菜单
	 * @param roleId
	 * @return
	 * @throws HaoException
	 */
	@RequestMapping("query_role_menu")
	public JSONView queryRoleMenu(
			@RequestParam("roleId") String roleId
			) throws HaoException {
		List<SysMenu> list = roleService.queryRoleMenu(roleId);
		RequestContext.setModelData("list", list);
		return RequestContext.getJSONView();
	}
	
	/**
	 * 保存角色选中的菜单
	 * @param roleId
	 * @param menuIds
	 * @return
	 * @throws HaoException
	 */
	@RequestMapping("save_role_menu")
	public JSONView saveRoleMenu(
			@RequestParam("roleId") String roleId,
			@RequestParam("menuIds") String[] menuIds
			) throws HaoException {
		roleService.saveRoleMenu(roleId, menuIds);
		return RequestContext.getJSONView();
	}
	
	/**
	 * 查询角色路由
	 * @param roleId
	 * @return
	 * @throws HaoException
	 */
	@RequestMapping("query_role_router")
	public JSONView queryRoleRouter(
			@RequestParam("roleId") String roleId
			) throws HaoException {
		List<SysRouter> list = roleService.queryRoleRouter(roleId);
		RequestContext.setModelData("list", list);
		return RequestContext.getJSONView();
	}
	
	/**
	 * 保存角色路由
	 * @param roleId
	 * @param paths
	 * @return
	 * @throws HaoException
	 */
	@RequestMapping("save_role_router")
	public JSONView saveRoleRouter(
			@RequestParam("roleId") String roleId,
			@RequestParam("paths") String paths
			) throws HaoException {
		roleService.saveRoleRouter(roleId, paths);
		return RequestContext.getJSONView();
	}
	
	/**
	 * 校验路由权限
	 * @return
	 * @throws HaoException 
	 */
//	@RequestMapping("check_router")
//	public JSONView checkRouterRole(
//			@RequestParam(value="path",required=false) String path
//			) throws HaoException {
//		String userId = "";
//		int end = roleService.checkRouterRole(path,RequestContext.getCurrentUserId());
//		//0 表示可通行  -1 需要登录  -2 没有权限
//		RequestContext.setModelData("result", end);
//		return  RequestContext.getJSONView();
//	}
}
