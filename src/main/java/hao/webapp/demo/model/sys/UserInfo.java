package hao.webapp.demo.model.sys;

import java.io.Serializable;

import hao.framework.database.entity.annotation.Column;
import hao.framework.database.entity.annotation.Entity;

/**
 * 用户信息
 * @author chianghao
 */
@Entity(title="用户信息")
public class UserInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(title="主键",primary=true,length=20)
	private long id;
	
	@Column(title="邮箱")
	private String email;
	
	@Column(title="手机")
	private String mobilPhone;
	
	@Column(title="姓名")
	private String name;
	
	@Column(title="昵称")
	private String nickname;
	
	@Column(title="性别")
	private String sex;
	
	@Column(title="身份证")
	private String idCard;
	
	@Column(title="用户类型")
	private int userType;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilPhone() {
		return mobilPhone;
	}

	public void setMobilPhone(String mobilPhone) {
		this.mobilPhone = mobilPhone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	
}
