package hao.webapp.demo.model.sys;

import hao.framework.database.entity.annotation.Column;
import hao.framework.database.entity.annotation.Entity;

/**
 * 系统角色
 * @author chianghao
 */
@Entity(title="系统角色")
public class SysRole {
	
	@Column(title="主键",primary=true,length=20,name="id")
	private long id;
	
	@Column(title="角色")
	private String name;
	
	/**
	 * 是否被选中
	 */
	private boolean isSelected;
	

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
