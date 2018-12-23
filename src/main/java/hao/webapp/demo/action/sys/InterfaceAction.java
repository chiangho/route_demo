package hao.webapp.demo.action.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hao.framework.core.expression.HaoException;
import hao.framework.database.page.Page;
import hao.framework.web.context.RequestContext;
import hao.framework.web.view.JSONView;
import hao.webapp.demo.service.sys.SysInterfaceService;

@Scope("prototype")
@Controller
@RequestMapping("interface")
public class InterfaceAction {

	@Autowired
	private SysInterfaceService interfaceService;
	/**
	 * 创建或者修改接口
	 * @return
	 * @throws HaoException 
	 */
	@RequestMapping("save")
	public JSONView saveInterface(
			@RequestParam String id,
			@RequestParam String isAuth,
			@RequestParam String className,
			@RequestParam String methodName,
			@RequestParam(required=false) String requestMapping,
			@RequestParam(required=false) String httpMethod,
			@RequestParam(required=false) String remark
			) throws HaoException {
		interfaceService.edit(id,isAuth,className,methodName,remark,requestMapping,httpMethod);
		return RequestContext.getJSONView();
	}
	
	/**
	 * 删除接口
	 * @return
	 * @throws HaoException 
	 */
	@RequestMapping("del")
	public JSONView delInterface(
			@RequestParam String id
			) throws HaoException {
		interfaceService.del(id);
		return RequestContext.getJSONView();
	}
	
	/**
	 * 查询所有接口信息，使用分页的形式
	 * @return
	 * @throws HaoException 
	 */
	@RequestMapping("query_page_list")
	public JSONView queryInterfaceList(
			@RequestParam(defaultValue="0",required=false,value=Page.PAGE_NO) int pageNo,
			@RequestParam(defaultValue="10",required=false,value=Page.PAGE_SIZE) int pageSize,
			@RequestParam(defaultValue="",required=false) String searchKey
			) throws HaoException {
		Page page = new Page(pageNo,pageSize);
		RequestContext.setModelData("list", interfaceService.queryPageList(page,searchKey));
		RequestContext.setModelData("page", page);
		return RequestContext.getJSONView();
	}
	
	/**
	 * 根据主键查询一条接口的详细信息
	 * @return
	 * @throws HaoException 
	 */
	@RequestMapping("query_bean")
	public JSONView queryInterface(
			@RequestParam String id
			) throws HaoException {
		RequestContext.setModelData("interface", interfaceService.queryBean(id));
		return RequestContext.getJSONView();
	}
	
}
