package es.ull.project.adapter.rest.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

import es.ull.project.adapter.rest.request.WorkFlowExecuteRequestBody;
import es.ull.project.adapter.rest.request.WorkFlowImportRequestBody;
import es.ull.project.adapter.rest.request.WorkFlowPostRequestBody;
import es.ull.project.adapter.rest.request.WorkFlowPutRequestBody;
import es.ull.project.adapter.rest.response.WorkFlowExecuteResponseBody;
import es.ull.project.adapter.rest.response.WorkFlowResponseBody;
import es.ull.project.application.usecase.workflow.WorkFlowCreateUseCase;
import es.ull.project.application.usecase.workflow.WorkFlowDeleteUseCase;
import es.ull.project.application.usecase.workflow.WorkFlowExecuteUseCase;
import es.ull.project.application.usecase.workflow.WorkFlowExportUseCase;
import es.ull.project.application.usecase.workflow.WorkFlowGetUseCase;
import es.ull.project.application.usecase.workflow.WorkFlowImportUseCase;
import es.ull.project.application.usecase.workflow.WorkFlowUpdateUseCase;
import es.ull.project.domain.entity.Execution;
import es.ull.project.domain.entity.WorkFlow;
import es.ull.project.domain.valueobject.ExportFile;
import es.ull.project.domain.valueobject.WorkFlowName;
import es.ull.project.domain.valueobject.Node;
import es.ull.project.domain.valueobject.NodeName;
import es.ull.utils.lang.UllEither;
import es.ull.utils.rest.error.ApiSubError;
import es.ull.utils.rest.exception.UllBadRequestException;
import es.ull.utils.rest.serialization.UllStringValidator;

@RestController
@RequestMapping("/flows")
public class WorkFlowController {

    @Autowired
    private WorkFlowGetUseCase flowGetUseCase;
    @Autowired
    private WorkFlowCreateUseCase flowCreateUseCase;
    @Autowired
    private WorkFlowDeleteUseCase flowDeleteUseCase;
    @Autowired
    private WorkFlowUpdateUseCase flowUpdateUseCase;
    @Autowired
    private WorkFlowExecuteUseCase flowExecuteUseCase;
    @Autowired
    private WorkFlowExportUseCase flowExportUseCase;
    @Autowired
    private WorkFlowImportUseCase flowImportUseCase;

    // -----------------------------------------------GET---------------------------------------------//
    @GetMapping
    public ResponseEntity<List<WorkFlowResponseBody>> getEntities() {
        List<WorkFlow> workflows = this.flowGetUseCase.getAllWorkFlows();
        List<WorkFlowResponseBody> flowsResponseBody = new ArrayList<>();
        for (int i = 0; i < workflows.size(); i++) {
            WorkFlow workflow = workflows.get(i);
            flowsResponseBody.add(WorkFlowResponseBody.from(workflow));
        }
        return ResponseEntity.ok(flowsResponseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkFlowResponseBody> getEntity(@PathVariable String id) {
        final UllEither<ApiSubError, UUID> validation = UllStringValidator.validateUUID(id);
        final UUID uuid = validation
                .getOrThrow(() -> new UllBadRequestException().addSubError(validation.getLeft()));
        Optional<WorkFlow> workflow = this.flowGetUseCase.getWorkFlow(uuid);
        if (workflow.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            WorkFlowResponseBody responseBody = WorkFlowResponseBody.from(workflow.get());
            return ResponseEntity.ok(responseBody);
        }
    }

    @GetMapping("/{id}/export")
    public ResponseEntity<WorkFlowResponseBody> exportFlow(@PathVariable String id) {
        final UllEither<ApiSubError, UUID> validation = UllStringValidator.validateUUID(id);
        final UUID uuid = validation
                .getOrThrow(() -> new UllBadRequestException().addSubError(validation.getLeft()));
        Optional<WorkFlow> workflow = this.flowGetUseCase.getWorkFlow(uuid);
        if (workflow.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<ExportFile> exportFile = this.flowExportUseCase.exportWorkFlow(workflow.get());
        if (exportFile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        ExportFile file = exportFile.get();
        WorkFlowResponseBody responseBody = WorkFlowResponseBody.from(file.getData());
        return ResponseEntity.ok(responseBody);
    }

    // -----------------------------------------------POST---------------------------------------------//
    @PostMapping
    public ResponseEntity<WorkFlowResponseBody> createEntity(@RequestBody WorkFlowPostRequestBody workflowRequest) {
        WorkFlowName name = workflowRequest.getName();
        List<Node> nodes = workflowRequest.getNodes();
        boolean enabled = workflowRequest.isEnabled();
        final WorkFlow workflow = this.flowCreateUseCase.createWorkFlow(name, nodes, enabled);
        WorkFlowResponseBody responseBody = WorkFlowResponseBody.from(workflow);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/import")
    public ResponseEntity<WorkFlowResponseBody> importFlow(@RequestBody WorkFlowImportRequestBody flowRequest) {
        WorkFlow flowToImport = new WorkFlow(
                flowRequest.getId(),
                flowRequest.getName(),
                flowRequest.getNodes(),
                LocalDateTime.now(),
                flowRequest.isEnabled());
        ExportFile file = new ExportFile(flowToImport);
        Optional<WorkFlow> flowImported = this.flowImportUseCase.importWorkFlow(file);
        if (flowImported.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        WorkFlowResponseBody responseBody = WorkFlowResponseBody.from(flowImported.get());
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/{id}/execute")
    public ResponseEntity<WorkFlowExecuteResponseBody> executeEntity(
            @PathVariable String id,
            @RequestBody WorkFlowExecuteRequestBody flowRequest) {
        final UllEither<ApiSubError, UUID> validation = UllStringValidator.validateUUID(id);
        final UUID uuid = validation
                .getOrThrow(() -> new UllBadRequestException().addSubError(validation.getLeft()));
                System.out.println("Executing workflow with ID: " + uuid + " - > " + flowRequest.toString());
        Map<NodeName, URI> pendingUriFiles = flowRequest.getPendingUriFiles();
        Optional<Execution> executeResult = this.flowExecuteUseCase.executeWorkFlow(uuid, pendingUriFiles);
        if (executeResult.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            WorkFlowExecuteResponseBody responseBody = WorkFlowExecuteResponseBody.from(executeResult.get());
            return ResponseEntity.ok(responseBody);
        }
    }

    // -----------------------------------------------PUT---------------------------------------------//
    @PutMapping("/{id}")
    public ResponseEntity<WorkFlowResponseBody> updateEntity(
            @PathVariable String id,
            @RequestBody WorkFlowPutRequestBody flowRequest) {
        final UllEither<ApiSubError, UUID> validation = UllStringValidator.validateUUID(id);
        final UUID uuid = validation
                .getOrThrow(() -> new UllBadRequestException().addSubError(validation.getLeft()));
        WorkFlowName name = flowRequest.getName();
        List<Node> nodes = flowRequest.getNodes();
        boolean enabled = flowRequest.isEnabled();
        Optional<WorkFlow> updatedFlow = this.flowUpdateUseCase.updateWorkFlow(uuid, name, nodes, enabled);
        if (updatedFlow.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            WorkFlowResponseBody responseBody = WorkFlowResponseBody.from(updatedFlow.get());
            return ResponseEntity.ok(responseBody);
        }
    }

    // -----------------------------------------------DELETE---------------------------------------------//
    @DeleteMapping("/{id}")
    public ResponseEntity<WorkFlowResponseBody> deleteEntity(@PathVariable String id) {
        final UllEither<ApiSubError, UUID> validation = UllStringValidator.validateUUID(id);
        final UUID uuid = validation
                .getOrThrow(() -> new UllBadRequestException().addSubError(validation.getLeft()));
        Optional<WorkFlow> deletedFlow = this.flowDeleteUseCase.deleteWorkFlow(uuid);
        if (deletedFlow.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            WorkFlowResponseBody responseBody = WorkFlowResponseBody.from(deletedFlow.get());
            return ResponseEntity.ok(responseBody);
        }
    }
}
