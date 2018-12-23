package hao.webapp.demo.service.sys;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;

import hao.framework.core.expression.HaoException;
import hao.framework.database.dao.JDBCDao;
import hao.framework.database.dao.sql.SQLRead;
import hao.webapp.demo.dao.user_info.UserInfoDao;
import hao.webapp.demo.model.sys.AccountInfo;
import hao.webapp.demo.model.sys.SysRole;
import hao.webapp.demo.model.sys.SysUserRole;
import hao.webapp.demo.model.sys.UserInfo;

@Service
public class UserInfoService {

	@Autowired 
	private JDBCDao jdbcDao;
	
	@Autowired 
	private SysRoleService roleService;
	
	@Autowired 
	private UserInfoDao dao;
	
	public UserInfo getUserInfo(String id) throws HaoException {
		return jdbcDao.queryBean(UserInfo.class,"id",id);
	}
	
	/**
	 * 根据账号查询用户信息
	 * @param access
	 * @return
	 * @throws HaoException 
	 */
	public UserInfo queryUserInfoByAccount(String accountId) throws HaoException {
		SQLRead read = new SQLRead(UserInfo.class);
		read.join(AccountInfo.class,"id", "userId");
		read.addCondition(AccountInfo.class,"id", accountId);
		return jdbcDao.queryBean(read);
	}



	/**
	 * 获取用户的权限
	 * @param id
	 * @return
	 * @throws HaoException 
	 */
	public Set<String> getRoles(String userId) throws HaoException {
		// TODO Auto-generated method stub
		SQLRead read = new SQLRead(SysRole.class);
		read.join(SysUserRole.class, "id","roleId");
		read.addCondition(SysUserRole.class, "userId",userId);
		List<SysRole> list = this.jdbcDao.queryList(read);
		Set<String> sysroles = new HashSet<String>();
		for(SysRole role:list) {
			sysroles.add(role.getId()+"");
		}
		return sysroles;
	}
	
	/**
	 * 设置用户会话需要的信息
	 * 菜单，接口，角色，视图控件
	 * @throws HaoException 
	 */
//	public void initUserSessionInfo(int isSuperAdmin) throws HaoException {
//		try {
//			Set<String> sysroles  = getRoles(UserUtil.getUserId());
//			List<SysMenu> menus = roleService.queryMenus(sysroles,isSuperAdmin);
//			List<SysInterface> interfaces = roleService.queryInterfaces(sysroles,isSuperAdmin);
//			UserUtil.putRole(sysroles);//角色
//			UserUtil.putMenu(menus);//菜单
//			UserUtil.putInterfaces(interfaces);//接口
//		}catch(Throwable t) {
//			t.printStackTrace();
//			throw new HaoException("99999","初始化用户信息失败！");
//		}
//	}

	/**
	 * 根据主键获取用户的角色
	 * @param userId
	 * @return
	 */
	public List<SysRole> queryRoleList(String userId) {
		try {
			//查询所有的角色
			List<SysRole> roles = roleService.queryList(null);
			//查询已经选中的角色，然后标记为选中
			List<SysUserRole> userRole = this.dao.queryUserRole(userId);
			Set<Long> chooseRole=new HashSet<Long>();
			if(userRole!=null) {
				for(SysUserRole s:userRole) {
					chooseRole.add(s.getRoleId());
				}
			}
			for(SysRole r:roles) {
				if(chooseRole.contains(r.getId())) {
					r.setSelected(true);
				}
			}
			return roles;
		} catch (HaoException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 保存用户角色
	 * @param userId
	 * @param roles
	 * @throws HaoException
	 */
	@Transactional(rollbackFor=Throwable.class)
	public void saveUserRole(String userId, String roles) throws HaoException {
		if(StringUtils.isEmpty(roles)) {
			throw new HaoException("999999", "请选择角色后再保存");
		}
		//删除原来关系
		this.dao.delUserRole(userId);
		//插入新关系
		this.dao.inserUserRole(userId,roles.split(","));
	}

	
}
