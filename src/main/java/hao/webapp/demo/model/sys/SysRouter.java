package hao.webapp.demo.model.sys;

import hao.framework.database.entity.annotation.Column;
import hao.framework.database.entity.annotation.Entity;

/**
 * 路由权限
 * @author chianghao
 */
@Entity(title="路由及权限")
public class SysRouter {

	@Column(title="路由")
	private String  path;//路由。不可重复唯一
	
	@Column(title="是否需要认证")
	private boolean auth;//是否需要认证
	
	private boolean isSelected;//是否选中
	
	

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isAuth() {
		return auth;
	}

	public void setAuth(boolean auth) {
		this.auth = auth;
	}
	
}
