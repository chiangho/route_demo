package hao.webapp.demo.model.sys;

import hao.framework.database.entity.annotation.Column;
import hao.framework.database.entity.annotation.Entity;

@Entity(title="角色和菜单")
public class SysRoleMenu {

	@Column(title="角色",length=20)
	private long roleId;
	
	@Column(title="菜单",length=20)
	private long menuId;
	
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	public long getMenuId() {
		return menuId;
	}
	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}
	
	
	
}
