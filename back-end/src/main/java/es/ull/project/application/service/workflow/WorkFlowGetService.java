package es.ull.project.application.service.workflow;

import es.ull.project.application.repository.WorkFlowRepository;
import es.ull.project.application.usecase.workflow.WorkFlowGetUseCase;
import es.ull.project.domain.entity.WorkFlow;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

public class WorkFlowGetService implements WorkFlowGetUseCase {

    @Autowired
    private WorkFlowRepository repository;

    @Override
    public Optional<WorkFlow> getWorkFlow(UUID workflow) {
        return Optional.ofNullable(repository.fetchById(workflow));
    }

    @Override
    public List<WorkFlow> getAllWorkFlows() {
        return this.repository.fetchAll();
    }
}
