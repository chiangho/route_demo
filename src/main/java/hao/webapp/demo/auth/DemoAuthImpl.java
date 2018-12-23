package hao.webapp.demo.auth;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import hao.framework.core.expression.HaoException;
import hao.framework.core.utils.MD5;
import hao.framework.core.utils.StringUtils;
import hao.framework.web.auth.AuthInterface;
import hao.framework.web.auth.BaseToken;
import hao.framework.web.jwt.JwtConstants;
import hao.framework.web.jwt.JwtToken;
import hao.framework.web.jwt.JwtTokenManageInterface;
import hao.webapp.demo.model.sys.SysInterface;
import hao.webapp.demo.model.sys.UserInfo;
import hao.webapp.demo.service.LoginService;
import hao.webapp.demo.service.sys.SysInterfaceService;
import hao.webapp.demo.service.sys.SysRoleService;
import hao.webapp.demo.service.sys.UserInfoService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

public class DemoAuthImpl implements AuthInterface{
	
	Logger log = LogManager.getLogger(DemoAuthImpl.class);
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private SysRoleService sysRoleService;
	
	
	@Autowired
	private SysInterfaceService interfaceService;
	
	@Autowired
	private JwtTokenManageInterface jwtTokenRedisManage;
	
	
	public String queryPassword(BaseToken token) {
		String password="";
		try {
			password = loginService.queryPassword(token.getAccount(),null,null);
		} catch (HaoException e) {
			e.printStackTrace();
		}
		return password;
	}

	public Set<String> queryUserPermit(String access) {
		Set<String> set = new HashSet<String>();
		return set;
	}

	
	public Set<String> queryUserRole(String access) {
		Set<String> sysroles = new HashSet<String>();
		try {
			UserInfo userInfo = userInfoService.queryUserInfoByAccount(access);
			//依据用户信息查询用户的权限，已经权限对应的接口，菜单，视图主键等信息
			sysroles  = userInfoService.getRoles(userInfo.getId()+"");
		} catch (HaoException e) {
			e.printStackTrace();
		}
		return sysroles;
	}

	public Set<String> queryApiRole(String id) {
		//查询页面
		Set<String> roles = new HashSet<String>();
		try {
			roles = sysRoleService.queryApiRole(id);
		} catch (HaoException e) {
			e.printStackTrace();
		}
		//查询接口
		return roles;
	}

	public Set<String> queryRouterRole(String uri) {
		//查询页面
		Set<String> roles = new HashSet<String>();
		//查询接口
		return roles;
	}

	@Override
	public String doAuthenticationInfo(BaseToken token) throws HaoException {
		String password=queryPassword(token);
		if(StringUtils.isEmpty(password)) {
			throw new HaoException("999999", "认证失败，账号不存在");
		}
		String signPassword = MD5.encode(token.getPassword());
		if(!password.toUpperCase().equals(signPassword.toUpperCase())) {
			throw new HaoException("999999", "认证失败，密码错误！");
		}
		return loginService.getAccountInfo(token.getAccount(), null,null).getUserId();
	}

	private boolean checkJwtTokenOut(String userId, String authHeader){
		JwtToken model = jwtTokenRedisManage.getToken(userId,authHeader);
		if (jwtTokenRedisManage.checkToken(model)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String checkIntefacePermit(String className,String methodName, HttpServletRequest request) throws HaoException {
		String userId = "";
		SysInterface sysInterface =null;
		try {
			sysInterface = this.interfaceService.queryBeanClassAndMethod(className,methodName);
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(sysInterface==null) {
			if(log.isDebugEnabled()) {
				log.debug("后台未登记接口："+className+"."+methodName+"。请先登记");
			}
			throw new HaoException("99999","尚未配置接口信息，请联系管理员");
		}
		if(sysInterface.getAuthStatus()==1) {
			//如果等于1 则下一步验证
			//1、验证token
			String authHeader = request.getHeader(JwtConstants.get().getTOKEN_AUTHORIZATION());
			if(StringUtils.isEmpty(authHeader)) {
				throw new HaoException("invalid_token","invalid token");
			}
			Claims claims =null;
			try {
				claims = Jwts.parser().setSigningKey(JwtConstants.get().getJWT_SECRET()).parseClaimsJws(authHeader).getBody();
			}catch (final SignatureException e) {
				throw new HaoException("999999","Invalid token");
			}
			userId = claims==null?"":claims.getSubject();
			if(StringUtils.isEmpty(userId)) {
	            throw new HaoException("999999","Invalid X-AUTH-TOKEN Subject no exist userId.");
	        }
			//检查签名及签名是否过期
			if(checkJwtTokenOut(userId,authHeader)==false) {
				throw new HaoException("no_sign","账号信息过期，");
			}
			//检查接口是否含有权限限制
			Set<String> apiRoles = queryApiRole(sysInterface.getId()+"");
			if(apiRoles!=null&&apiRoles.size()>0) {
				//查询用户的角色
				boolean hasRole = false;
				Set<String> userRoles = queryUserRole(userId);
				for(String apirole:apiRoles) {
					if(userRoles.contains(apirole)) {
						hasRole = true;
						break;
					}
				}
				if(hasRole==false) {
					throw new HaoException("no_permit","not has api premit");
				}
			}
		}
		return userId;
	}

	
}
