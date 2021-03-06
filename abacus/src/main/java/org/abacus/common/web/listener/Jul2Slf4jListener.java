package org.abacus.common.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.bridge.SLF4JBridgeHandler;

public class Jul2Slf4jListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
	}
 
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
