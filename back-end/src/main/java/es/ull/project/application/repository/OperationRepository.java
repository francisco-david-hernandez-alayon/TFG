package es.ull.project.application.repository;

import java.util.List;

import es.ull.project.domain.valueobject.Operation;
import es.ull.project.domain.valueobject.OperationName;

public interface OperationRepository {

    public abstract Operation delete(OperationName operationName);

    public abstract List<Operation> fetchAll();

    public abstract Operation fetchByName(OperationName operationName);

    public abstract Operation save(Operation entity);

    public abstract Operation update(OperationName operationName, Operation entity);
}
