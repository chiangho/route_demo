package hao.webapp.demo.utils;

import java.util.ArrayList;
import java.util.List;

import hao.webapp.demo.model.sys.SysMenu;

public class MenuUtil {

	/**
	 * 递归获取菜单的树形结构
	 * @param list
	 * @param parentId
	 * @return
	 */
	public static List<SysMenu> getChildrenMenu(List<SysMenu> list,String parentId){
		List<SysMenu> tagList = new ArrayList<SysMenu>();
		for(SysMenu m : list) {
			if((m.getParentId()+"").equals(parentId)) {
				List<SysMenu> subMenus = getChildrenMenu(list,m.getId()+"");
				if(subMenus!=null&&subMenus.size()!=0) {
					m.setSubMenus(subMenus);
				}
				tagList.add(m);
			}
		}
		return tagList;
	}
	
}
