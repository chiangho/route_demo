package hao.webapp.demo.model.sys;

import java.io.Serializable;

import hao.framework.database.entity.annotation.Column;
import hao.framework.database.entity.annotation.Entity;

@Entity(title="系统接口")
public class SysInterface implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(title="主键",length=20,primary=true)
	private long id;
	
	@Column(title="备注说明",length=4000,isNull=true)
	private String remark;

	@Column(title="名称")
	private String name;
	
	@Column(title="验证状态",remark="1表示是，0表示否")
	private int authStatus;
	
	@Column(title="接口类")
	private String className;
	
	@Column(title="接口方法")
	private String methodName;
	
	@Column(title="请求映射")
	private String requestMapping;

	@Column(title="协议方式")
	private String httpMethod;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(int authStatus) {
		this.authStatus = authStatus;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getRequestMapping() {
		return requestMapping;
	}

	public void setRequestMapping(String requestMapping) {
		this.requestMapping = requestMapping;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}
	
}
