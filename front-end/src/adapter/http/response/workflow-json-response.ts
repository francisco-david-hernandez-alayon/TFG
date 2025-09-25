import { UllUUID } from '@ull-tfg/ull-tfg-typescript';
import { WorkFlow } from '@/domain/entity/workflow';
import { WorkFlowName } from '@/domain/value-object/workflow-name';
import { NodeName } from '@/domain/value-object/node-name';
import { XPosition } from '@/domain/value-object/x-position';
import { YPosition } from '@/domain/value-object/y-position';
import { NodeColor } from '@/domain/enumerate/node-color';
import { NodeContentType } from '@/domain/enumerate/node-content-type';
import { FileName } from '@/domain/value-object/file-name';
import { Operation } from '@/domain/value-object/operation';
import { OperationName } from '@/domain/value-object/operation-name';
import { NodeIcon } from '@/domain/enumerate/node-icon';
import { UllDockerImageName } from '@ull-tfg/ull-tfg-typescript/dist/ull-docker-image-name';
import { File } from '@/domain/value-object/file';
import { Node } from '@/domain/value-object/node';
import { Link } from '@/domain/value-object/link';
import { LinkName } from '@/domain/value-object/link-name';
import { Conditional } from '@/domain/value-object/conditional';
import { ConditionalName } from '@/domain/value-object/conditional-name';


export class WorkFlowJsonResponse {
    public uuid: string;
    public workFlowName: string;
    public creationDate: Date;
    public nodes: Array<any>;
    public enabled: boolean;

    // Constructor
    constructor(uuid: string, nodes: Array<any>, name: string, creationDate: Date, enabled: boolean) {
        this.uuid = uuid;
        this.nodes = nodes;
        this.workFlowName = name;
        this.creationDate = creationDate;
        this.enabled = enabled;
    }

    static toWorkFlow(json: WorkFlowJsonResponse): WorkFlow {
        const flowName = new WorkFlowName(json.workFlowName);
        const nodes = WorkFlowJsonResponse.toNodes(json.nodes);

        return new WorkFlow(flowName, nodes, json.enabled, json.creationDate, new UllUUID(json.uuid));
    }


    static toNodes(nodesJson: Array<any>): Node[] {
        const nodeMap: Map<string, Node> = new Map();

        // Create nodes first
        nodesJson.forEach((jsonNode: any) => {
            const name = new NodeName(jsonNode.name);
            const x = new XPosition(jsonNode.xPosition);
            const y = new YPosition(jsonNode.yPosition);
            const color = NodeColor[jsonNode.nodecolor as keyof typeof NodeColor];
            const icon = NodeIcon[jsonNode.icon as keyof typeof NodeIcon];

            // Type of node
            const contentType = NodeContentType[jsonNode.content.nodeType as keyof typeof NodeContentType];
            let content;

            switch (contentType) {
                case NodeContentType.FILE:
                    const fileName = new FileName(jsonNode.content.fileName);
                    const uri = jsonNode.content.uri ?? undefined;
                    content = uri ? new File(fileName, uri) : new File(fileName);
                    break;

                case NodeContentType.OPERATION:
                    content = new Operation(
                        new OperationName(jsonNode.content.operationName),
                        new UllDockerImageName(jsonNode.content.dockerImageName),
                    );
                    break;
                case NodeContentType.CONDITIONAL:
                    const conditionalName = new ConditionalName(jsonNode.content.conditionalName);
                    const executionCode = jsonNode.content.executionCode;

                    // Construir el Map<NodeName, boolean>
                    const linksConditions = new Map<NodeName, boolean>();
                    for (const linkCond of jsonNode.content.linksConditions) {
                        linksConditions.set(new NodeName(linkCond.conditionalLinkNodeName), linkCond.conditionalLinkValue);
                    }

                    content = new Conditional(conditionalName, executionCode, linksConditions);
                    break;


                default:
                    throw new Error(`Unsupported node content type: ${jsonNode.content.nodeType}`);
            }

            const node = new Node(name, x, y, color, icon, content, []);
            nodeMap.set(jsonNode.name, node);
        });

        // Add links to nodes
        nodesJson.forEach((jsonNode: any) => {
            const currentNode = nodeMap.get(jsonNode.name);
            if (!currentNode) return;

            const links: Link[] = jsonNode.links.map((linkJson: any) => {

                const finalNode = nodeMap.get(linkJson.finalNodeLink);
                if (!finalNode) {
                    throw new Error(`Final node '${linkJson.finalNodeLink}' not found for link '${linkJson.linkName}'`);
                }

                return new Link(
                    new LinkName(linkJson.linkName),
                    currentNode.getName(),
                    finalNode.getName(),
                    linkJson.linkCondition as boolean
                );
            });

            const updatedNode = new Node(
                currentNode.getName(),
                currentNode.getXPosition(),
                currentNode.getYPosition(),
                currentNode.getColor(),
                currentNode.getIcon(),
                currentNode.getContent(),
                links
            );

            nodeMap.set(jsonNode.name, updatedNode);
        });

        return Array.from(nodeMap.values());
    }


    // ToString
    toString(): string {
        return `FlowJsonResponse={id=${this.uuid}, nodes=${this.nodes})
        )}, name=${this.workFlowName}, creationDate=${this.creationDate.toISOString()}}`;
    }
}
