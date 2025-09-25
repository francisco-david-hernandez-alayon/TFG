import { UllUUID } from "@ull-tfg/ull-tfg-typescript";
import { WorkFlowName } from "../value-object/workflow-name";
import { Node } from "../value-object/node";
import { NodeName } from "../value-object/node-name";

export class WorkFlow {
  
  private static readonly ERROR_NAME_NOT_DEFINED = "Workflow name not defined";
  private static readonly ERROR_NODES_NOT_DEFINED = "Nodes not defined";
  private static readonly ERROR_NODES_EMPTY = "Nodes cannot be empty";
  private static readonly ERROR_FIRST_NODE_NOT_FOUND = "No initial node found: every node is a destination of at least one link.";
  private static readonly ERROR_MULTIPLE_FIRST_NODES = "Multiple initial nodes found: more than one node has no incoming links.";
  private static readonly ERROR_ID_NOT_DEFINED = "ID not defined";
  private static readonly ERROR_ID_INVALID = "ID must be an instance of UllUUID";
  private static readonly ERROR_CREATION_DATE_INVALID = "Invalid creation date";

  private readonly id: UllUUID;
  private readonly name: WorkFlowName;
  private readonly creationDate: Date;
  private readonly nodes: Node[];
  private readonly firstNode: Node;
  private readonly enabled: boolean;

  constructor(name: WorkFlowName, nodes: Node[], enabled: boolean, creationDate: Date | string = new Date(), id?: UllUUID) {
    this.validateName(name);
    this.validateNodes(nodes);

    if (id) {
      this.validateId(id);
      this.id = id;
    } else {
      this.id = UllUUID.random();
    }

    this.name = name;
    this.nodes = [...nodes];
    this.enabled = enabled;
    this.creationDate = new Date(creationDate);
    this.firstNode = this.computeFirstNode();
  }

  public static from(id: UllUUID, name: WorkFlowName, nodes: Node[], enabled: boolean, creationDate: Date | string = new Date(),
  ): WorkFlow {
    return new WorkFlow(name, nodes, enabled, creationDate, id);
  }

  private validateId(id: UllUUID): void {
    if (!id) throw new Error(WorkFlow.ERROR_ID_NOT_DEFINED);
    if (!(id instanceof UllUUID)) throw new Error(WorkFlow.ERROR_ID_INVALID);
  }

  private validateName(name: WorkFlowName): void {
    if (!name) throw new Error(WorkFlow.ERROR_NAME_NOT_DEFINED);
  }

  private validateNodes(nodes: Node[]): void {
    if (!nodes) throw new Error(WorkFlow.ERROR_NODES_NOT_DEFINED);
    if (nodes.length === 0) throw new Error(WorkFlow.ERROR_NODES_EMPTY);
  }

  private validateCreationDate(creationDate: Date | string): void {
    const date = new Date(creationDate);
    if (isNaN(date.getTime())) {
      throw new Error(WorkFlow.ERROR_CREATION_DATE_INVALID);
    }
  }

  /**
   * @brief compute the first node of the workflow.
   * @returns 
   */
  private computeFirstNode(): Node {
    // Get all node names
    const allNodeNames = new Set(this.nodes.map(node => node.getName()));

    // Get all final nodes from links
    for (const node of this.nodes) {
      for (const link of node.getLinks()) {
        allNodeNames.delete(link.getFinalNode());
      }
    }

    // After removing all final nodes, the remaining names should be the first nodes
    const remainingNames = Array.from(allNodeNames);

    if (remainingNames.length === 0) {
      throw new Error(WorkFlow.ERROR_FIRST_NODE_NOT_FOUND);
    }

    if (remainingNames.length > 1) {
      throw new Error(WorkFlow.ERROR_MULTIPLE_FIRST_NODES);
    }

    const firstNodeName = remainingNames[0];

    const firstNode = this.nodes.find(node => node.getName().equals(firstNodeName));
    if (!firstNode) {
      throw new Error(WorkFlow.ERROR_FIRST_NODE_NOT_FOUND);
    }

    return firstNode;
  }



  getFirstNode(): Node {
    return this.firstNode;
  }

  getId(): UllUUID {
    return this.id;
  }

  getName(): WorkFlowName {
    return this.name;
  }


  getCreationDate(): Date {
    return this.creationDate;
  }

  getNodes(): ReadonlyArray<Node> {
    return this.nodes;
  }


  isEnabled(): boolean {
    return this.enabled;
  }


  setName(name: WorkFlowName): WorkFlow {
    return new WorkFlow(name, this.nodes, this.enabled, this.creationDate, this.id);
  }

  setCreationDate(date: Date): WorkFlow {
    return new WorkFlow(this.name, this.nodes, this.enabled, date, this.id);
  }

  setNodes(nodes: Node[]): WorkFlow {
    return new WorkFlow(this.name, nodes, this.enabled, this.creationDate, this.id);
  }

  activate(): WorkFlow {
    return new WorkFlow(this.name, this.nodes, true, this.creationDate, this.id);
  }

  disactivate(): WorkFlow {
    return new WorkFlow(this.name, this.nodes, false, this.creationDate, this.id);
  }


  toString(): string {
    return `WorkFlow={id=${this.id}, name=${this.name.toString()}, enabled=${this.enabled}, creationDate=${this.creationDate.toISOString()}, nodes=[${this.nodes.map(n => n.toString()).join(', ')}], firstNode=${this.firstNode.toString()}}`;
  }
}
