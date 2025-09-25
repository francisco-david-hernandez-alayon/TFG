package es.ull.project.application.service.workflow;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import es.ull.project.application.repository.WorkFlowRepository;
import es.ull.project.application.usecase.workflow.WorkFlowDeleteUseCase;
import es.ull.project.domain.entity.WorkFlow;

public class WorkFlowDeleteService implements WorkFlowDeleteUseCase {

    @Autowired
    private WorkFlowRepository repository;

    @Override
    public Optional<WorkFlow> deleteWorkFlow(UUID id) {
        return Optional.ofNullable(repository.delete(id));
    }
}
