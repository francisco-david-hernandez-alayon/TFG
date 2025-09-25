package es.ull.project.application.usecase.workflow;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import es.ull.project.domain.entity.Execution;
import es.ull.project.domain.valueobject.NodeName;

public interface WorkFlowExecuteUseCase {

    /**
     * Executes the workflow according to the given SourceFlow input file and return the
     * output of the last node.
     * 
     * @param workflow
     * @param pendingUriFiles map of pending URI files to be processed by the
     *                        workflow nodes
     * @return execute data
     */
    public Optional<Execution> executeWorkFlow(UUID workFlowId, Map<NodeName, URI> pendingUriFiles);
}
