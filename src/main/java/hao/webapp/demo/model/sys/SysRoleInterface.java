package hao.webapp.demo.model.sys;

import hao.framework.database.entity.annotation.Column;
import hao.framework.database.entity.annotation.Entity;

@Entity(title="角色和接口")
public class SysRoleInterface {


	@Column(title="角色",length=20)
	private long roleId;

	
	@Column(title="接口",length=20)
	private long interfaceId;

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public long getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(long interfaceId) {
		this.interfaceId = interfaceId;
	}
	
	
	
}
