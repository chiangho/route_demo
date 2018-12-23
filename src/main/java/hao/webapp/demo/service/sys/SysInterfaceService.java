package hao.webapp.demo.service.sys;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.sql.ast.SQLOrderingSpecification;

import hao.framework.core.expression.HaoException;
import hao.framework.core.seq.Sequence;
import hao.framework.core.utils.StringUtils;
import hao.framework.database.dao.JDBCDao;
import hao.framework.database.dao.sql.SQLCommon.SqlLinkOperator;
import hao.framework.database.dao.sql.SQLCommon.SqlOperator;
import hao.framework.database.dao.sql.SQLCommon.SqlSort;
import hao.framework.database.dao.sql.SQLRead;
import hao.framework.database.dao.sql.SQLWrite;
import hao.framework.database.dao.sql.SQLWrite.SQLWriteAction;
import hao.framework.database.page.Page;
import hao.webapp.demo.model.sys.SysInterface;
import hao.webapp.demo.model.sys.SysRoleInterface;

/**
 * 提供给前端的接口
 * @author chianghao
 *
 */
@Service
public class SysInterfaceService {

	@Autowired
	private JDBCDao dao;
	
	
	/**
	 * 删除接口
	 * @param id
	 * @throws HaoException
	 */
	@Transactional(rollbackFor=Throwable.class)
	public void del(String id) throws HaoException {
		dao.deleteById(SysInterface.class,id);
		dao.delete(SysRoleInterface.class,"interfaceId",id);
	}
	
	
	/**
	 * 查询分组下面的接口
	 * @throws HaoException 
	 */
	public List<SysInterface> queryGroupInterface(String groupName) throws HaoException{
		List<SysInterface> list = dao.queryList(SysInterface.class,"groupName",groupName);
		return list;
	}
	
	/**
	 * 查询接口明细
	 * @param id
	 * @return
	 * @throws HaoException
	 */
	public SysInterface queryBean(String id) throws HaoException {
		return dao.queryBean(SysInterface.class, "id",id);
	}

	/**
	 * 遍历查询功能组信息
	 * @return
	 * @throws HaoException
	 */
	public List<Map<String,Object>> queryGroupNames() throws HaoException{
		SQLRead  sqlread = new SQLRead(SysInterface.class);
		sqlread.setField("groupName");
		sqlread.group("groupName");
		sqlread.order("groupName");
		List<Map<String,Object>> groupNames = this.dao.queryForList(sqlread);
		return groupNames;
	}

	/**
	 * 创建接口
	 * @param id
	 * @param isAuth
	 * @param className
	 * @param methodName
	 * @param string
	 * @throws HaoException 
	 */
	@Transactional(rollbackFor=Throwable.class)
	public void edit(
			String id, 
			String isAuth, 
			String className, 
			String methodName, 
			String remark,
			String requestMapping,
			String httpMethod
			) throws HaoException {
		if(StringUtils.isEmpty(className)) {
			throw new HaoException("000001","接口实现类不能为空！");
		}
		if(StringUtils.isEmpty(methodName)) {
			throw new HaoException("000002","接口实现方法不能为空！");
		}
		if(StringUtils.isEmpty(isAuth)||(!isAuth.equals("1")&&!isAuth.equals("0"))) {
			throw new HaoException("000002","请填写正确的验证状态");
		}
		boolean isInsert = false;
		if(StringUtils.isEmpty(id)) {
			isInsert = true;
		}else {
			//查询主键是否存在。如果存在则做新增操作
			int interfaceCount = this.dao.queryCount(SysInterface.class, "id",id);
			if(interfaceCount==0) {
				isInsert = true;
			}
		}
		SQLWrite write = null; 
		if(isInsert) {
			//验证 class method 是否存在。这个必须唯一
			SQLRead queryClassAndMethodCount = new SQLRead(SysInterface.class);
			queryClassAndMethodCount.addCondition("className", className);
			queryClassAndMethodCount.addCondition("methodName", methodName);
			if(dao.queryCount(queryClassAndMethodCount)>0) {
				throw new HaoException("000003", "接口类和方法重复！");
			}
			
			write = new SQLWrite(SysInterface.class,SQLWriteAction.insert);
			write.addField("id",Sequence.getNextId());
			write.addField("className",className);
			write.addField("methodName",methodName);
			write.addField("authStatus",isAuth);
			write.addField("remark",remark);
			write.addField("requestMapping",requestMapping);
			write.addField("httpMethod",httpMethod);
		}else {
			//验证 class method 是否存在。这个必须唯一
			SQLRead queryClassAndMethodCount = new SQLRead(SysInterface.class);
			queryClassAndMethodCount.addCondition("className", className);
			queryClassAndMethodCount.addCondition("methodName", methodName);
			queryClassAndMethodCount.addCondition("id",SqlOperator.notEqual, id);
			if(dao.queryCount(queryClassAndMethodCount)>0) {
				throw new HaoException("000003", "接口类和方法重复！");
			}
			write = new SQLWrite(SysInterface.class,SQLWriteAction.update);
			write.addField("className",className);
			write.addField("methodName",methodName);
			write.addField("authStatus",isAuth);
			write.addField("remark",remark);
			write.addField("requestMapping",requestMapping);
			write.addField("httpMethod",httpMethod);
			write.addCondition("id", id);
		}
		this.dao.execute(write);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param searchKey
	 * @throws HaoException 
	 */
	public List<SysInterface> queryPageList(Page page, String searchKey) throws HaoException {
		SQLRead read = new SQLRead(SysInterface.class);
		if(!StringUtils.isEmpty(searchKey)) {
			read.addCondition("className",SqlOperator.like, searchKey,SqlLinkOperator.or);
			read.addCondition("methodName", SqlOperator.like, searchKey,SqlLinkOperator.or);
			read.addCondition("remark", SqlOperator.like, searchKey,SqlLinkOperator.or);
		}
		read.order("className",SqlSort.asc);
		read.order("methodName",SqlSort.asc);
		return dao.queryPageList(page, read);
	}


	public SysInterface queryBeanClassAndMethod(String className, String methodName) throws HaoException {
		SQLRead read = new SQLRead(SysInterface.class);
		read.addCondition("className",className);
		read.addCondition("methodName", methodName);
		return this.dao.queryBean(read);
	}
	
}
