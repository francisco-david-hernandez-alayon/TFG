package es.ull.project.application.service.operation;

import es.ull.project.application.repository.OperationRepository;
import es.ull.project.application.repository.WorkFlowRepository;
import es.ull.project.application.usecase.operation.OperationUpdateUseCase;
import es.ull.project.domain.entity.WorkFlow;
import es.ull.project.domain.enumerate.NodeContentType;
import es.ull.project.domain.valueobject.Node;
import es.ull.project.domain.valueobject.NodeContent;
import es.ull.project.domain.valueobject.Operation;
import es.ull.project.domain.valueobject.OperationName;
import es.ull.utils.docker.UllDockerImageName;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

public class OperationUpdateService implements OperationUpdateUseCase {

    @Autowired
    private OperationRepository repository;

    @Autowired
    private WorkFlowRepository flowRepository;

    @Override
    public Optional<Operation> updateOperation(
            OperationName oldOperationName,
            OperationName newOperationName,
            UllDockerImageName dockerImage) {

        // Check if an operation with the new name already exists
        if (!oldOperationName.equals(newOperationName)) {
            Operation operationWithNewName = repository.fetchByName(newOperationName);
            if (operationWithNewName != null) {
                return Optional.empty(); // Abort if name conflict
            }
        }

        // Create the updated operation (we'll save it later)
        final Operation updatedOperation = new Operation(newOperationName, dockerImage);

        // First, update all workflows that reference the old operation
        List<WorkFlow> allFlows = flowRepository.fetchAll();
        for (WorkFlow flow : allFlows) {
            boolean modified = false;
            List<Node> updatedNodes = new ArrayList<>();

            for (Node node : flow.getNodes()) {
                NodeContent content = node.getContent();

                // Check if the node contains an OPERATION-type content
                if (content.getType() == NodeContentType.OPERATION) {
                    Operation opContent = (Operation) content;

                    // Replace old operation with the updated one
                    if (opContent.getName().equals(oldOperationName)) {
                        Node updatedNode = new Node(
                                node.getName(),
                                node.getXPosition(),
                                node.getYPosition(),
                                node.getColor(),
                                node.getIcon(),
                                updatedOperation,
                                node.getLinks());
                        updatedNodes.add(updatedNode);
                        modified = true;
                    } else {
                        updatedNodes.add(node);
                    }
                } else {
                    updatedNodes.add(node); // Keep node unchanged
                }
            }

            // If any node was modified, update the workflow in the repository
            if (modified) {
                WorkFlow updatedFlow = new WorkFlow(
                        flow.getId(),
                        flow.getName(),
                        updatedNodes,
                        flow.getCreationDate(),
                        flow.isEnabled());
                flowRepository.update(updatedFlow);
            }
        }

        // Now update the operation in the repository
        Operation updated = repository.update(oldOperationName, updatedOperation);
        if (updated == null) {
            return Optional.empty(); // Update failed
        }

        return Optional.of(updatedOperation);
    }

}
