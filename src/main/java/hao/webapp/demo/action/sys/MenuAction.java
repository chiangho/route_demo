package hao.webapp.demo.action.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hao.framework.core.expression.HaoException;
import hao.framework.web.context.RequestContext;
import hao.framework.web.view.JSONView;
import hao.webapp.demo.model.sys.SysMenu;
import hao.webapp.demo.service.sys.SysMenuService;

/**
 * 角色管理
 * @author chianghao
 *
 */
@Controller
@RequestMapping("menu")
public class MenuAction {
	
	@Autowired
	SysMenuService menuservice;
	
	private List<Map<String,Object>> changeMenus(List<SysMenu> menuslist){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(SysMenu sm:menuslist) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id", sm.getId());
			map.put("label",sm.getUrl()==null?sm.getName():(sm.getName()+"     "+sm.getUrl()));
			map.put("parentId",sm.getParentId());
			if(sm.getSubMenus()!=null&&sm.getSubMenus().size()>0) {
				map.put("children", changeMenus(sm.getSubMenus()));
			}
			list.add(map);
		}
		return list;
	}
	
	@RequestMapping("query_all")
	public JSONView queryMenus() throws HaoException {
		List<SysMenu> menuslist =  menuservice.queryMenu(SysMenu.RootMenuId+"");
		List<Map<String,Object>> menus = changeMenus(menuslist);
		Map<String,Object> root = new HashMap<String,Object>();
		root.put("id", SysMenu.RootMenuId+"");
		root.put("label","系统菜单");
		root.put("parentId","");
		root.put("children", menus);
		List<Map<String,Object>> rootList = new ArrayList<Map<String,Object>>();
		rootList.add(root);
		RequestContext.setModelData("menus",rootList);
		//将其转换成前端需要的格式
		return RequestContext.getJSONView();
	}
	
	
	/**
	 * 创建菜单
	 * @param id
	 * @param name
	 * @param parentId
	 * @param url
	 * @return
	 * @throws HaoException
	 */
	@RequestMapping("save")
	public JSONView save(
			@RequestParam(required=false,value="id",defaultValue="") String id,
			@RequestParam(required=true,value="name") String name,
			@RequestParam(required=true,value="parentId") String parentId,
			@RequestParam(required=false,value="url",defaultValue="") String url
			) throws HaoException {
		this.menuservice.save(id, parentId, name, url,"0");
		return RequestContext.getJSONView();
	}
	
	@RequestMapping("query_menu")
	public JSONView queryMenu(
			@RequestParam(required=true,value="id") String id
			) throws HaoException {
		RequestContext.setModelData("menu", menuservice.queryBean(id));
		return RequestContext.getJSONView();
	}
	
	@RequestMapping("del")
	public JSONView delMenu(
			@RequestParam(required=true,value="id") String id
			) throws HaoException {
		menuservice.del(id);
		return RequestContext.getJSONView();
	}
	
}
