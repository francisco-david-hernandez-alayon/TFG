package es.ull.project.adapter.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.ull.project.adapter.rest.request.OperationPostRequestBody;
import es.ull.project.adapter.rest.request.OperationPutRequestBody;
import es.ull.project.adapter.rest.response.OperationResponseBody;
import es.ull.project.application.usecase.operation.OperationCreateUseCase;
import es.ull.project.application.usecase.operation.OperationDeleteUseCase;
import es.ull.project.application.usecase.operation.OperationGetUseCase;
import es.ull.project.application.usecase.operation.OperationUpdateUseCase;
import es.ull.project.application.usecase.workflow.WorkFlowGetUseCase;
import es.ull.project.domain.entity.WorkFlow;
import es.ull.project.domain.enumerate.NodeContentType;
import es.ull.project.domain.valueobject.Node;
import es.ull.project.domain.valueobject.Operation;
import es.ull.project.domain.valueobject.OperationName;
import es.ull.utils.docker.UllDockerImageName;

@RestController
@RequestMapping("/operations")
public class OperationController {

    @Autowired
    private WorkFlowGetUseCase flowGetUseCase;
    @Autowired
    private OperationGetUseCase operationGetUseCase;
    @Autowired
    private OperationCreateUseCase operationCreateUseCase;
    @Autowired
    private OperationDeleteUseCase operationDeleteUseCase;
    @Autowired
    private OperationUpdateUseCase operationUpdateUseCase;

    @GetMapping
    public ResponseEntity<List<OperationResponseBody>> getEntities() {
        List<Operation> operations = this.operationGetUseCase.getAllOperations();
        List<OperationResponseBody> operationsResponseBody = new ArrayList<>();
        for (int i = 0; i < operations.size(); i++) {
            Operation operation = operations.get(i);
            operationsResponseBody.add(OperationResponseBody.from(operation));
        }
        return ResponseEntity.ok(operationsResponseBody);
    }

    @GetMapping("/{name}")
    public ResponseEntity<OperationResponseBody> getEntity(@PathVariable String name) {
        OperationName operationName = new OperationName(name);
        Optional<Operation> operation = this.operationGetUseCase.getOperation(operationName);
        if (operation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            OperationResponseBody responseBody = OperationResponseBody.from(operation.get());
            return ResponseEntity.ok(responseBody);
        }
    }

    @PostMapping
    public ResponseEntity<OperationResponseBody> createEntity(@RequestBody OperationPostRequestBody operationRequest) {
        OperationName name = operationRequest.getOperationName();
        UllDockerImageName dockerName = operationRequest.getDockerImageName();
        Operation createdOperation = this.operationCreateUseCase.createOperation(name, dockerName);
        OperationResponseBody responseBody = OperationResponseBody.from(createdOperation);
        return ResponseEntity.ok(responseBody);
    }

    @PutMapping("/{name}")
    public ResponseEntity<OperationResponseBody> updateEntity(
            @PathVariable String name,
            @RequestBody OperationPutRequestBody operationRequest) {
        OperationName oldOperationName = new OperationName(name);
        OperationName newOperationName = operationRequest.getOperationName();
        UllDockerImageName dockerName = operationRequest.getDockerImageName();
        Optional<Operation> updatedOperation = this.operationUpdateUseCase.updateOperation(
                oldOperationName, newOperationName, dockerName);
        if (updatedOperation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            OperationResponseBody responseBody = OperationResponseBody.from(updatedOperation.get());
            return ResponseEntity.ok(responseBody);
        }
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<OperationResponseBody> deleteEntity(@PathVariable String name) {
        OperationName operationName = new OperationName(name);
        // Temporal solution for delete problem: check if operation is used in any workflow
        List<WorkFlow> workflows = this.flowGetUseCase.getAllWorkFlows();
        for (WorkFlow workflow : workflows) {
            boolean existsInNodes = false;
            for (Node node : workflow.getNodes()) {
                if (node.getType() == NodeContentType.OPERATION) {
                    Object rawContent = node.getContent();
                    if (rawContent instanceof Operation operation) {
                        if (operation.getName().equals(operationName)) {
                            existsInNodes = true;
                            break;
                        }
                    }
                }
            }
            if (existsInNodes) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        }
        Optional<Operation> deletedOperation = this.operationDeleteUseCase.deleteOperation(operationName);
        if (deletedOperation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            OperationResponseBody responseBody = OperationResponseBody.from(deletedOperation.get());
            return ResponseEntity.ok(responseBody);
        }
    }
}
