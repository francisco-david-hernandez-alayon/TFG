package es.ull.project.application.service.workflow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import es.ull.project.application.repository.WorkFlowRepository;
import es.ull.project.application.usecase.workflow.WorkFlowCreateUseCase;
import es.ull.project.domain.entity.WorkFlow;
import es.ull.project.domain.valueobject.WorkFlowName;
import es.ull.project.domain.valueobject.Node;

public class WorkFlowCreateService implements WorkFlowCreateUseCase {

    @Autowired
    private WorkFlowRepository repository;

    @Override
    public WorkFlow createWorkFlow(WorkFlowName name, List<Node> nodes, boolean enabled) {
        final WorkFlow workflow = new WorkFlow(name, nodes, enabled);
        return repository.save(workflow);
    }
}
