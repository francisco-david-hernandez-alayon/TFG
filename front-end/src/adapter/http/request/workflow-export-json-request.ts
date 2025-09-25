
import { Node } from "@/domain/value-object/node";
import { Link } from "@/domain/value-object/link";
import { NodeContentType } from "@/domain/enumerate/node-content-type";
import { File } from "@/domain/value-object/file";
import { Operation } from "@/domain/value-object/operation";
import { WorkFlow } from "@/domain/entity/workflow";
import { Conditional } from "@/domain/value-object/conditional";

export class WorkFlowExportJsonRequest {
    public uuid: string;
    public workFlowName: string;
    public enabled: boolean;
    public creationDate: string;
    public nodes: Array<{
        name: string;
        xPosition: number;
        yPosition: number;
        nodecolor: string;
        icon: string;
        content: any;
        links: Array<{
            linkName: string;
            initialNodeLink: string;
            finalNodeLink: string;
            linkCondition: boolean;
        }>;
    }>;

    constructor(
        uuid: string,
        workFlowName: string,
        enabled: boolean,
        creationDate: string,
        nodes: Array<{
            name: string;
            xPosition: number;
            yPosition: number;
            nodecolor: string;
            icon: string;
            content: any;
            links: Array<{
                linkName: string;
                initialNodeLink: string;
                finalNodeLink: string;
                linkCondition: boolean;
            }>;
        }>
    ) {
        this.uuid = uuid;
        this.workFlowName = workFlowName;
        this.creationDate = creationDate;
        this.nodes = nodes;
        this.enabled = enabled;
    }

    static toRequest(workFlow: WorkFlow): WorkFlowExportJsonRequest {
        const uuid = workFlow.getId().getValue();
        const workFlowName = workFlow.getName().getName();
        const enabled = workFlow.isEnabled();
        const creationDate = new Date().toISOString();

        const nodes: Node[] = Array.from(workFlow.getNodes());
        const nodeDtos = nodes.map((node: Node) => {
            const contentObj = node.getContent();
            let content: any;

            if (contentObj instanceof File) {
                content = {
                    nodeType: NodeContentType.FILE,
                    fileName: contentObj.getName().getName()
                };

                const uri = contentObj.getUri();
                if (uri !== undefined && uri !== null) {
                    content.uri = uri;
                }
            }
            else if (contentObj instanceof Operation) {
                content = {
                    nodeType: NodeContentType.OPERATION,
                    operationName: contentObj.getName().getName(),
                    dockerImageName: contentObj.getDockerImage().getValue(),
                };
            } else if (contentObj instanceof Conditional) {
                const linksConditions = Array.from(contentObj.getLinksConditions().entries()).map(
                    ([nodeName, value]) => ({
                        conditionalLinkNodeName: nodeName.getName(),
                        conditionalLinkValue: value
                    })
                );

                content = {
                    nodeType: NodeContentType.CONDITIONAL,
                    conditionalName: contentObj.getName().getName(),
                    executionCode: contentObj.getExecutionCode(),
                    linksConditions: linksConditions
                };
            }
            else {
                throw new Error("Unsupported content type in node.");
            }

            const links = node.getLinks().map((link: Link) => ({
                linkName: link.getName().getName(),
                initialNodeLink: link.getInitialNode().getName(),
                finalNodeLink: link.getFinalNode().getName(),
                linkCondition: link.getCondition(),
            }));

            return {
                name: node.getName().getName(),
                xPosition: node.getXPosition().getXPosition(),
                yPosition: node.getYPosition().getYPosition(),
                nodecolor: node.getColor(),
                icon: node.getIcon(),
                content,
                links,
            };
        });

        return new WorkFlowExportJsonRequest(uuid, workFlowName, enabled, creationDate, nodeDtos);
    }

    toString(): string {
        return JSON.stringify({
            uuid: this.uuid,
            workFlowName: this.workFlowName,
            creationDate: this.creationDate,
            nodes: this.nodes,
            enabled: this.enabled,
        }, null, 2);
    }
}
