package es.ull.project.application.service.operation;

import org.springframework.beans.factory.annotation.Autowired;

import es.ull.project.application.repository.OperationRepository;
import es.ull.project.application.usecase.operation.OperationCreateUseCase;
import es.ull.project.domain.valueobject.Operation;
import es.ull.project.domain.valueobject.OperationName;
import es.ull.utils.docker.UllDockerImageName;

public class OperationCreateService implements OperationCreateUseCase {

    @Autowired
    private OperationRepository repository;

    @Override
    public Operation createOperation(OperationName name, UllDockerImageName dockerImage) {
        Operation existingOperation = repository.fetchByName(name);
        if (existingOperation != null) {
            throw new IllegalArgumentException("Operation with name " + name + " already exists.");
        }
        final Operation newOperation = new Operation(name, dockerImage);
        return repository.save(newOperation);
    }
}
