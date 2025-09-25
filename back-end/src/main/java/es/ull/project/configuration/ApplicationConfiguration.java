package es.ull.project.configuration;

import es.ull.project.application.service.execution.ExecutionCreateService;
import es.ull.project.application.service.execution.ExecutionDeleteService;
import es.ull.project.application.service.execution.ExecutionGetService;
import es.ull.project.application.service.operation.OperationCreateService;
import es.ull.project.application.service.operation.OperationDeleteService;
import es.ull.project.application.service.operation.OperationGetService;
import es.ull.project.application.service.operation.OperationUpdateService;
import es.ull.project.application.service.workflow.WorkFlowCreateService;
import es.ull.project.application.service.workflow.WorkFlowDeleteService;
import es.ull.project.application.service.workflow.WorkFlowExecuteService;
import es.ull.project.application.service.workflow.WorkFlowGetService;
import es.ull.project.application.service.workflow.WorkFlowUpdateService;
import es.ull.project.application.usecase.execution.ExecutionCreateUseCase;
import es.ull.project.application.usecase.execution.ExecutionDeleteUseCase;
import es.ull.project.application.usecase.execution.ExecutionGetUseCase;
import es.ull.project.application.usecase.operation.OperationCreateUseCase;
import es.ull.project.application.usecase.operation.OperationUpdateUseCase;
import es.ull.project.application.usecase.workflow.WorkFlowCreateUseCase;
import es.ull.project.application.usecase.workflow.WorkFlowDeleteUseCase;
import es.ull.project.application.usecase.workflow.WorkFlowExecuteUseCase;
import es.ull.project.application.usecase.workflow.WorkFlowGetUseCase;
import es.ull.project.application.usecase.workflow.WorkFlowUpdateUseCase;
import es.ull.project.application.usecase.operation.OperationDeleteUseCase;
import es.ull.project.application.usecase.operation.OperationGetUseCase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public WorkFlowCreateUseCase flowCreateUseCase() {
        return new WorkFlowCreateService();
    }

    @Bean
    public WorkFlowDeleteUseCase flowDeleteUseCase() {
        return new WorkFlowDeleteService();
    }

    @Bean
    public WorkFlowUpdateUseCase flowUpdateUseCase() {
        return new WorkFlowUpdateService();
    }

    @Bean
    public WorkFlowGetUseCase flowGetUseCase() {
        return new WorkFlowGetService();
    }

    @Bean
    public WorkFlowExecuteUseCase flowExecuteUseCase() {
        return new WorkFlowExecuteService();
    }

    @Bean
    public OperationGetUseCase operationGetUseCase() {
        return new OperationGetService();
    }

    @Bean
    public OperationCreateUseCase operationCreateUseCase() {
        return new OperationCreateService();
    }

    @Bean
    public OperationDeleteUseCase operationDeleteUseCase() {
        return new OperationDeleteService();
    }

    @Bean
    public OperationUpdateUseCase operationUpdateUseCase() {
        return new OperationUpdateService();
    }

    @Bean
    public ExecutionGetUseCase executionGetUseCase() {
        return new ExecutionGetService();
    }

    @Bean
    public ExecutionDeleteUseCase executionDeleteUseCase() {
        return new ExecutionDeleteService();
    }

}
