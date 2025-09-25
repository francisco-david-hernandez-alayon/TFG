package es.ull.project.application.repository;

import java.util.List;
import java.util.UUID;

import es.ull.project.domain.entity.WorkFlow;

public interface WorkFlowRepository {

    public abstract WorkFlow delete(UUID entity);

    public abstract List<WorkFlow> fetchAll();

    public abstract WorkFlow fetchById(UUID flowId);

    public abstract WorkFlow save(WorkFlow entity);

    public abstract WorkFlow update(WorkFlow entity);
}
