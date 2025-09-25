package es.ull.project.application.usecase.workflow;

import es.ull.project.domain.valueobject.ExportFile;
import es.ull.project.domain.entity.WorkFlow;

import java.util.Optional;


public interface WorkFlowExportUseCase {

    /**
     * Export a workflow entity as a JSON file and return its binary representation and name.
     *
     * @param workflow the workflow to export
     * @return an ExportFile object with filename and byte data
     */
    Optional<ExportFile> exportWorkFlow(WorkFlow workflow);
}
