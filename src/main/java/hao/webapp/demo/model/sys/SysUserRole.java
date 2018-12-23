package hao.webapp.demo.model.sys;

import hao.framework.database.entity.annotation.Column;
import hao.framework.database.entity.annotation.Entity;

/**
 * 用户权限
 * @author chianghao
 *
 */
@Entity(title="用户权限")
public class SysUserRole {

	/**
	 * 用户主键
	 */
	@Column(title="用户")
	private long userId;
	
	/**
	 * 权限主键
	 */
	@Column(title="角色")
	private long roleId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	
	
	
}
