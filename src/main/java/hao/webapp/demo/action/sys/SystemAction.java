package hao.webapp.demo.action.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import hao.framework.core.expression.HaoException;
import hao.framework.web.context.RequestContext;
import hao.framework.web.view.JSONView;
import hao.webapp.demo.service.sys.SystemService;

/**
 * 系统管理
 * @author chianghao
 *
 */
@Controller
@RequestMapping("sys/setting")
public class SystemAction {
	
	@Autowired
	private SystemService systemService;
	/**
	 * 初始化系统
	 * @return
	 * @throws HaoException 
	 */
	@RequestMapping("init_system")
	public JSONView initSystem() throws HaoException {
		systemService.initSystem();
		return RequestContext.getJSONView();
	}
	
	
}
