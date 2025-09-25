package es.ull.project.application.repository;

import java.util.List;
import java.util.UUID;

import es.ull.project.domain.entity.Execution;

public interface ExecutionRepository {
    public abstract Execution delete(UUID executionId);

    public abstract List<Execution> fetchAll();

    public abstract Execution fetchById(UUID executionId);

    public abstract Execution save(Execution entity);

    public abstract Execution update(UUID executionId, Execution entity);
}
