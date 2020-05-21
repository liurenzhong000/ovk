import com.leyou.user.UserApplication;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.application.conf.ApplicationAutoConfiguration;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.TaskQuery;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.apache.catalina.security.SecurityUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

//@Configuration//声名为配置类，继承Activiti抽象配置类
@SpringBootTest(classes = UserApplication.class)
@RunWith(SpringRunner.class)
public class ActivitiConfig/* extends AbstractProcessEngineAutoConfiguration*/ {

    @Autowired
    private SpringProcessEngineConfiguration springProcessEngineConfiguration;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    ProcessRuntime processRuntime;

    @Autowired
    private TaskRuntime taskRuntime;

    /*@Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource activitiDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(
            PlatformTransactionManager transactionManager,
            SpringAsyncExecutor springAsyncExecutor) throws IOException {


        return baseSpringProcessEngineConfiguration(
                activitiDataSource(),
                transactionManager,
                springAsyncExecutor);
    }*/

   /* @Bean
    public ProcessEngine getEngine(){
      return   springProcessEngineConfiguration.buildProcessEngine();
    }*/

    @Test
    public void test(){
       // System.out.println(getEngine());
       /* Deployment deploy = processEngine.getRepositoryService()
                .createDeployment()
                .addClasspathResource("holiday.bpmn").addClasspathResource("holiday.png")
                .name("请假流程").deploy();

        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("holiday");*/
       /* List<Task> list =
                processEngine.getTaskService().createTaskQuery().processDefinitionKey("holiday").taskAssignee("zhansan").list();
         for (Task task : list) {
            System.out.println(task.getProcessInstanceId());
            System.out.println(task.getId());
            System.out.println(task.getAssignee());
            System.out.println(task.getName());

        }*/
        //processEngine.getTaskService().complete("2b0108b7-7a7d-11ea-818c-eef2c7489dff");
       // System.out.println(processInstance);
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("holiday");
//        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey().singleResult();
//        processRuntime.start(ProcessPayloadBuilder.start().withProcessDefinitionKey().)
        Task complete = taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(null).build());
    }
}