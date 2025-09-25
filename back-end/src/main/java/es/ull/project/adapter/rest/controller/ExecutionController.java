package es.ull.project.adapter.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.ull.project.adapter.rest.response.WorkFlowExecuteResponseBody;
import es.ull.project.application.usecase.execution.ExecutionDeleteUseCase;
import es.ull.project.application.usecase.execution.ExecutionGetUseCase;
import es.ull.project.domain.entity.Execution;
import es.ull.utils.lang.UllEither;
import es.ull.utils.rest.error.ApiSubError;
import es.ull.utils.rest.exception.UllBadRequestException;
import es.ull.utils.rest.serialization.UllStringValidator;

@RestController
@RequestMapping("/executions")
public class ExecutionController {

    @Autowired
    private ExecutionGetUseCase executionGetUseCase;

    @Autowired
    private ExecutionDeleteUseCase executionDeleteUseCase;


    // -----------------------------------------------GET---------------------------------------------//
    @GetMapping
    public ResponseEntity<List<WorkFlowExecuteResponseBody>> getEntities() {
        List<Execution> executions = this.executionGetUseCase.getAllExecutions();
        List<WorkFlowExecuteResponseBody> executionsResponseBody = new ArrayList<>();
        for (int i = 0; i < executions.size(); i++) {
            Execution execution = executions.get(i);
            executionsResponseBody.add(WorkFlowExecuteResponseBody.from(execution));
        }
        return ResponseEntity.ok(executionsResponseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkFlowExecuteResponseBody> getEntity(@PathVariable String id) {
        final UllEither<ApiSubError, UUID> validation = UllStringValidator.validateUUID(id);
        final UUID uuid = validation
                .getOrThrow(() -> new UllBadRequestException().addSubError(validation.getLeft()));
 
        Optional<Execution> execution = this.executionGetUseCase.getExecution(uuid);
        if (execution.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            WorkFlowExecuteResponseBody responseBody = WorkFlowExecuteResponseBody.from(execution.get());
            return ResponseEntity.ok(responseBody);
        }
    }


    // -----------------------------------------------DELETE---------------------------------------------//
    @DeleteMapping("/{id}")
    public ResponseEntity<WorkFlowExecuteResponseBody> deleteEntity(@PathVariable String id) {
        final UllEither<ApiSubError, UUID> validation = UllStringValidator.validateUUID(id);
        final UUID uuid = validation
                .getOrThrow(() -> new UllBadRequestException().addSubError(validation.getLeft()));
        Optional<Execution> deletedFlow = this.executionDeleteUseCase.deleteExecution(uuid);
        if (deletedFlow.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            WorkFlowExecuteResponseBody responseBody = WorkFlowExecuteResponseBody.from(deletedFlow.get());
            return ResponseEntity.ok(responseBody);
        }
    }

}
