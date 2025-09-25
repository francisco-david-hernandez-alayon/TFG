package es.ull.project.application.usecase.workflow;

import java.util.Optional;
import java.util.UUID;

import es.ull.project.domain.entity.WorkFlow;

public interface WorkFlowDeleteUseCase {
    
    /**
     * Delete an workflow
     * 
     * @param workflow
     * @return workflow deleted
     */
    Optional<WorkFlow> deleteWorkFlow(UUID workflow);
}
