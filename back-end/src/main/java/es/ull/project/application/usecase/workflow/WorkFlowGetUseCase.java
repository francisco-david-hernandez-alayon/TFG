package es.ull.project.application.usecase.workflow;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import es.ull.project.domain.entity.WorkFlow;

public interface WorkFlowGetUseCase {

    /**
     * Get an workflow by ID
     * 
     * @param workflow
     * @return workflow selected
     */
    Optional<WorkFlow> getWorkFlow(UUID workflow);

    /**
     * Get all flows
     * 
     * @return all flows
     */
    List<WorkFlow> getAllWorkFlows();
}
