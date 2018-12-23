package hao.webapp.demo.model.sys;

import java.io.Serializable;

import hao.framework.database.entity.annotation.Column;
import hao.framework.database.entity.annotation.Entity;

@Entity(title="账号信息")
public class AccountInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(title="主键",primary=true,length=20)
	private long id;
	
	@Column(title="账号",name="account")
	private String account;
	
	@Column(title="密码")
	private String password;
	
	@Column(title="邮箱")
	private String email;
	
	@Column(title="手机",name="mobil_phone")
	private String phone;
	
	@Column(title="用户",remark="默认此值与id主键相同。有些系统账号账号和用户是分离的。")
	private String userId;
	
	@Column(title="是否超级管理员",remark="0否，1是")
	private int isSuperAdmin;
	
	@Column(title="账号状态",remark="0禁用，1有效",defaultValue="1")
	private int status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(int isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
