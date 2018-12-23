package hao.webapp.demo.model.sys;

import hao.framework.database.entity.annotation.Column;
import hao.framework.database.entity.annotation.Entity;

/**
 * 角色和路由
 * @author chianghao
 */
@Entity(title="角色路由")
public class SysRoleRouter {

	@Column(title="角色")
	private long roleId;
	
	@Column(title="路由")
	private String path;
	
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
	
}
