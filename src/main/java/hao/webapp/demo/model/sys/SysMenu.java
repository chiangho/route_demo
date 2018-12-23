package hao.webapp.demo.model.sys;

import java.io.Serializable;
import java.util.List;

import hao.framework.database.entity.annotation.Column;
import hao.framework.database.entity.annotation.Entity;

/**
 * 系统菜单
 * @author chianghao
 *
 */
@Entity(title="系统菜单")
public class SysMenu  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static long RootMenuId = -1;
		
	@Column(title="主键",primary=true,length=20,name="id")
	private long id;
	
	@Column(title="父菜单",length=20)
	private long parentId;
	
	@Column(title="菜单")
	private String name;
	
	@Column(title="访问地址")
	private String url;
	
	@Column(title="排序",length=20)
	private float sort;
	
	/**
	 * 是否被选中
	 */
	private int isSelect;

	/**
	 * 子菜单
	 */
	private List<SysMenu> subMenus;
	
	
	public int getIsSelect() {
		return isSelect;
	}
	public void setIsSelect(int isSelect) {
		this.isSelect = isSelect;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public float getSort() {
		return sort;
	}

	public void setSort(float sort) {
		this.sort = sort;
	}

	public static long getRootmenuid() {
		return RootMenuId;
	}

	public List<SysMenu> getSubMenus() {
		return subMenus;
	}

	public void setSubMenus(List<SysMenu> subMenus) {
		this.subMenus = subMenus;
	}
	
}
