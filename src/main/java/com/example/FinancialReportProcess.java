package com.example;

import java.text.ParseException;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.task.Task;

/**
 * 财务报告过程
 * 
 * @author 刘宏强
 *
 */
public class FinancialReportProcess {
	// Activiti流程引擎和配置。
	public static void main(String[] args) throws ParseException {
// 创建Activiti进程引擎
//		ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
//				.buildProcessEngine();

		// 独立环境的配置帮助程序（例如，不使用依赖项管理器）。
		ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
				// 使用基于内存的h2嵌入式数据库创建Process Engine。
				.setJdbcUrl("jdbc:h2:tcp://localhost/~/activiti;DB_CLOSE_DELAY=1000").setJdbcUsername("sa")
				.setJdbcPassword("").setJdbcDriver("org.h2.Driver")
				.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		// 创建Activiti进程引擎
		ProcessEngine processEngine = cfg.buildProcessEngine();

		// 获取Activiti服务
		RepositoryService repositoryService = processEngine.getRepositoryService();
		RuntimeService runtimeService = processEngine.getRuntimeService();

		// 部署process定义
		repositoryService.createDeployment().addClasspathResource("FinancialReportProcess.bpmn20.xml").deploy();

		// 启动process实例
		runtimeService.startProcessInstanceByKey("financialReport");

		TaskService taskService = processEngine.getTaskService();

		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("accountancy").list();

	}
}