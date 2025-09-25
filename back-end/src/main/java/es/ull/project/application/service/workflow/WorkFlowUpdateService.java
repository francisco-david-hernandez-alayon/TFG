package es.ull.project.application.service.workflow;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import es.ull.project.application.repository.WorkFlowRepository;
import es.ull.project.application.usecase.workflow.WorkFlowUpdateUseCase;
import es.ull.project.domain.entity.WorkFlow;
import es.ull.project.domain.valueobject.WorkFlowName;
import es.ull.project.domain.valueobject.Node;

public class WorkFlowUpdateService implements WorkFlowUpdateUseCase {

    @Autowired
    private WorkFlowRepository repository;

    @Override
    public Optional<WorkFlow> updateWorkFlow(
            UUID id,
            WorkFlowName name,
            List<Node> nodes,
            boolean enabled) {
        WorkFlow existingFlow = repository.fetchById(id);
        final WorkFlow workflow = new WorkFlow(id, name, nodes, existingFlow.getCreationDate(), enabled);
        return Optional.ofNullable(repository.update(workflow));
    }
}
