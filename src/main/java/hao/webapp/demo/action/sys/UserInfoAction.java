package hao.webapp.demo.action.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hao.framework.core.expression.HaoException;
import hao.framework.web.context.RequestContext;
import hao.framework.web.view.JSONView;
import hao.webapp.demo.model.sys.SysRole;
import hao.webapp.demo.service.sys.AccountService;
import hao.webapp.demo.service.sys.SysMenuService;
import hao.webapp.demo.service.sys.UserInfoService;

/**
 * 用户信息
 * @author chianghao
 */
@Controller
@RequestMapping("user_info")
public class UserInfoAction {
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	SysMenuService menuservice;
	
	@Autowired
	AccountService accountService;
	/**
	 * 查询角色
	 * @param userId
	 * @return
	 */
	@RequestMapping("query_user_role")
	public JSONView queryUserRole(
			@RequestParam("userId") String userId
			) {
		List<SysRole> list = userInfoService.queryRoleList(userId);
		RequestContext.setModelData("roles", list);
		return RequestContext.getJSONView();
	}
	
	/**
	 * 保存用户角色
	 * @param userId
	 * @param roles
	 * @return
	 * @throws HaoException
	 */
	@RequestMapping("save_user_role")
	public JSONView saveRole(
			@RequestParam("userId") String userId,
			@RequestParam("roles") String roles
			) throws HaoException {
		userInfoService.saveUserRole(userId,roles);
		return RequestContext.getJSONView();
	}
	
	@RequestMapping("query_user")
	public JSONView queryUserInfo() throws HaoException {
		RequestContext.setModelData("userInfo",userInfoService.getUserInfo(RequestContext.getCurrentUserId()));
		RequestContext.setModelData("menus",menuservice.queryMenuByUserId(RequestContext.getCurrentUserId()));
		return RequestContext.getJSONView();
	}
	
	/**
	 * 查询用户的菜单
	 * @return
	 * @throws HaoException
	 *//*
	@RequestMapping("query_user_menu")
	public JSONView queryUserMenu() throws HaoException {
		RequestContext.setModelData("menus",menuservice.queryMenuByUserId(RequestContext.getCurrentUserId()));
		return RequestContext.getJSONView();
	}*/
	
}
