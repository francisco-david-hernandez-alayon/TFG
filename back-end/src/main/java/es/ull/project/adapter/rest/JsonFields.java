package es.ull.project.adapter.rest;

public class JsonFields {

    // Private constructor to prevent instantiation
    private JsonFields() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final String ID = "uuid";

    // Node Common fields
    public static final String NODE_NAME = "name";
    public static final String X_POSITION = "xPosition";
    public static final String Y_POSITION = "yPosition";
    public static final String NODE_COLOR = "nodecolor";
    public static final String LINKS = "links";
    public static final String CONTENT = "content";
    public static final String NODETYPE = "nodeType";
    public static final String IS_INITIAL = "isInitial";

    // File
    public static final String FILE_NAME = "fileName";
    public static final String FILE_URI = "uri";

    // Operation
    public static final String OPERATION_NAME = "operationName";
    public static final String DOCKER_IMAGE_NAME = "dockerImageName";
    public static final String ICON = "icon";

    // Conditional
    public static final String CONDITIONAL_NAME = "conditionalName";
    public static final String EXECUTION_CODE = "executionCode";
    public static final String LINKS_CONDITIONS = "linksConditions";
    public static final String CONDITIONAL_LINK_NODE_NAME = "conditionalLinkNodeName";
    public static final String CONDITIONAL_LINK_VALUE = "conditionalLinkValue";

    // Workflow
    public static final String WORKFLOW_NAME = "workFlowName";
    public static final String WORKFLOW_DATE = "creationDate";
    public static final String INITIAL_NODE = "initialNode";
    public static final String NODES = "nodes";
    public static final String NODE_WORKFLOW = "nodeWorkflow";
    public static final String ENABLED = "enabled";

    // Link
    public static final String LINK_NAME = "linkName";
    public static final String INITIAL_NODE_LINK = "initialNodeLink";
    public static final String FINAL_NODE_LINK = "finalNodeLink";
    public static final String LINK_CONDITION = "linkCondition";

    // export
    public static final String EXPORT_FILE_NAME = "exportFileName";
    public static final String WORKFLOW = "workflow";

    // execute
    public static final String EXECUTION_DATE = "executionDate";
    public static final String PENDING_URI_FILES = "pendingUriFiles";
    public static final String EXECUTE_STATUS = "status";
    public static final String EXECUTE_MESSAGE = "message";
    public static final String OUTPUT_FILES = "outputFiles";
    public static final String NODES_EXECUTED = "nodesExecuted";
    public static final String NODE_TIME_EXECUTION = "nodeTimeExecution";
    public static final String EXECUTION_TIME = "totalExecutionTimeMillis";
    public static final String NUMBER_OF_OPERATION_EXECUTED = "numberOfOperationExecuted";
}
