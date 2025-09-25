package es.ull.project.application.usecase.workflow;

import java.util.Optional;

import es.ull.project.domain.entity.WorkFlow;
import es.ull.project.domain.valueobject.ExportFile;

public interface WorkFlowImportUseCase {
    /**
     * Import a workflow entity from a JSON file and return the workflow object.
     *
     * @param exportFile the ExportFile containing the workflow data
     * @return an Optional containing the workflow if import is successful, or empty if it fails
     */
    Optional<WorkFlow> importWorkFlow(ExportFile exportFile);
}
