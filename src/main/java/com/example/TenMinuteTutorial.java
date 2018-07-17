package com.example;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.task.Task;

/**
 * 10分钟的教程
 * 
 * @author 刘宏强
 *
 */
public class TenMinuteTutorial {

	public static void main(String[] args) {

		// 创建Activiti进程引擎
		ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
				.buildProcessEngine();

		// 获取Activiti服务
		RepositoryService repositoryService = processEngine.getRepositoryService();
		RuntimeService runtimeService = processEngine.getRuntimeService();

		// 部署流程定义
		repositoryService.createDeployment().addClasspathResource("FinancialReportProcess.bpmn20.xml").deploy();

		// 启动流程实例
		String procId = runtimeService.startProcessInstanceByKey("financialReport").getId();

		// 得到第一个任务
		TaskService taskService = processEngine.getTaskService();
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("accountancy").list();
		for (Task task : tasks) {
			System.out.println("以下是会计组的任务: " + task.getName());

			// 认领它
			taskService.claim(task.getId(), "fozzie");
		}

		// 验证fozzie现在可以检索任务
		tasks = taskService.createTaskQuery().taskAssignee("fozzie").list();
		for (Task task : tasks) {
			System.out.println("fozzie的任务: " + task.getName());

			// 完成任务
			taskService.complete(task.getId());
		}

		System.out.println("fozzie的任务数: " + taskService.createTaskQuery().taskAssignee("fozzie").count());

		// 检索和要求第二个任务
		tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
		for (Task task : tasks) {
			System.out.println("以下是管理组的任务: " + task.getName());
			taskService.claim(task.getId(), "kermit");
		}

		// 完成第二个任务结束过程
		for (Task task : tasks) {
			System.out.println("kermit的任务: " + task.getName());

			// 完成任务
			taskService.complete(task.getId());
		}

		// 验证流程是否已完成
		HistoryService historyService = processEngine.getHistoryService();
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(procId).singleResult();
		System.out.println("流程实例结束时间: " + historicProcessInstance.getEndTime());
	}

}