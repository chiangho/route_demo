package hao.webapp.demo.action;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hao.framework.core.expression.HaoException;
import hao.framework.web.auth.AuthInterface;
import hao.framework.web.context.RequestContext;
import hao.framework.web.jwt.JwtTokenManageInterface;
import hao.framework.web.jwt.LoginToken;
import hao.framework.web.view.JSONView;

@Scope("prototype")
@Controller
public class TokenController {
	private static final Logger logger = LoggerFactory.getLogger(TokenController.class);
	
	@Autowired
	private AuthInterface authInterface;
	
	@Autowired
	private JwtTokenManageInterface jwtTokenRedisManage;

	@RequestMapping(value = "/login")
	public JSONView login(@RequestParam String account, @RequestParam String password) throws HaoException {
		logger.info("获取TOKEN[{}]" , account);
		// 验证
		if (StringUtils.isEmpty(account)) {
			throw new HaoException("999999", "用户账号不能为空!");
		}
		// 验证
		if (StringUtils.isEmpty(account)) {
			throw new HaoException("999999", "用户密码不能为空!");
		}
		Assert.notNull(account, "username can not be empty");
		Assert.notNull(password, "password can not be empty");

		String userId = authInterface.doAuthenticationInfo(new LoginToken(account, password));
		// 生成一个token，保存用户登录状态
		String token = jwtTokenRedisManage.createToken(userId,null);
		RequestContext.setModelData("token", token);
		return RequestContext.getJSONView();
	}

	@RequestMapping(value = "/logout")
	public JSONView logout() throws HaoException {
		logger.info("deleteToken[{}]" , RequestContext.getCurrentUserId());
		// 验证
		if (StringUtils.isEmpty(RequestContext.getCurrentUserId())) {
			throw new HaoException("999999", "用户账号不能为空!!");
		}
		try {
			jwtTokenRedisManage.deleteToken(RequestContext.getCurrentUserId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new HaoException("999999", e.getMessage());
		}
		return RequestContext.getJSONView();
	}

}
