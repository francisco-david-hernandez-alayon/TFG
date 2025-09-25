package es.ull.project.application.usecase.workflow;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import es.ull.project.domain.entity.WorkFlow;
import es.ull.project.domain.valueobject.WorkFlowName;
import es.ull.project.domain.valueobject.Node;

public interface WorkFlowUpdateUseCase {

    /**
     * Update an workflow
     * 
     * @param workflow
     * @param sourceName
     * @param sourceType
     * @return workflow updated
     */
    Optional<WorkFlow> updateWorkFlow(
            UUID workflow,
            WorkFlowName name,
            List<Node> nodes,
            boolean enabled);
}
