package es.ull.project.application.usecase.workflow;

import java.util.List;

import es.ull.project.domain.entity.WorkFlow;
import es.ull.project.domain.valueobject.WorkFlowName;
import es.ull.project.domain.valueobject.Node;

public interface WorkFlowCreateUseCase {

    /**
     * Create an workflow
     * 
     * @param sourceName
     * @param sourceType
     * @return workflow created
     */
    WorkFlow createWorkFlow(WorkFlowName name, List<Node> nodes, boolean enabled);
}
