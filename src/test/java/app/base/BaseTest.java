package app.base;

import org.junit.Before;

import hao.framework.core.spring.Property;
import hao.framework.database.DBType;
import hao.framework.database.HaoDB;
import hao.framework.test.AbstractContextControllerTest;

public class BaseTest extends AbstractContextControllerTest{
	
	protected final String sessionId = "2c09dda6-e97d-4fcb-915a-b6b9d9f6ef9c";
	
	@Before
	public void init() {
		HaoDB.init(Property.getValue("ModelPackage"));
		// 自动创建表
		HaoDB.createTable(DBType.mysql, Property.getValue("jdbc.host"),
				Integer.parseInt(Property.getValue("jdbc.port")), Property.getValue("jdbc.db_name"),
				Property.getValue("jdbc.account"), Property.getValue("jdbc.password"));
	}
	
	
}
