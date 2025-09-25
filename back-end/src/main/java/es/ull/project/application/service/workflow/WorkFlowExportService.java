package es.ull.project.application.service.workflow;

import es.ull.project.domain.valueobject.ExportFile;
import es.ull.project.application.usecase.workflow.WorkFlowExportUseCase;
import es.ull.project.domain.entity.WorkFlow;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class WorkFlowExportService implements WorkFlowExportUseCase {

    @Override
    public Optional<ExportFile> exportWorkFlow(WorkFlow workflow) {
        try {
            ExportFile exportFile = new ExportFile(workflow);
            return Optional.of(exportFile);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
