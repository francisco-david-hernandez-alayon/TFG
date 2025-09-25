import { LinkName } from './link-name.ts';
import { NodeName } from './node-name.ts';

export class Link {
    private static readonly ERROR_INITIAL_NODE_NOT_DEFINED = "Initial node not defined";
    private static readonly ERROR_FINAL_NODE_NOT_DEFINED = "Final node not defined";
    private static readonly ERROR_LINK_NAME_NOT_DEFINED = "Name not defined";

    private readonly name: LinkName;
    private readonly initialNode: NodeName;
    private readonly finalNode: NodeName;
    private readonly conditionLink: boolean;

    constructor(name: LinkName, initialNode: NodeName, finalNode: NodeName, conditionLink: boolean) {
        this.validateNode(initialNode);
        this.validateFinalNode(finalNode);
        this.validateName(name);
        this.initialNode = initialNode;
        this.name = name;
        this.finalNode = finalNode;
        this.conditionLink = conditionLink;
    }

    private validateName(name: LinkName): void {
        if (!name) {
            throw new Error(Link.ERROR_LINK_NAME_NOT_DEFINED);
        }
    }

    private validateNode(node: NodeName): void {
        if (!node) {
            throw new Error(Link.ERROR_INITIAL_NODE_NOT_DEFINED);
        }
    }

    private validateFinalNode(finalNode: NodeName): void {
        if (!finalNode) {
            throw new Error(Link.ERROR_FINAL_NODE_NOT_DEFINED);
        }
    }

    getName(): LinkName {
        return this.name;
    }

    getInitialNode(): NodeName {
        return this.initialNode;
    }

    getFinalNode(): NodeName {
        return this.finalNode;
    }

    getCondition(): boolean {
        return this.conditionLink;
    }

    updateFinalNode(finalNode: NodeName): Link {
        this.validateFinalNode(finalNode);
        return new Link(this.name, this.initialNode, finalNode, this.conditionLink);
    }

    updateCondition(conditionLink: boolean): Link {
        return new Link(this.name, this.initialNode, this.finalNode, conditionLink);
    }

    updateName(name: LinkName): Link {
        this.validateName(name);
        return new Link(name, this.initialNode, this.finalNode, this.conditionLink);
    }

    updateInitialNode(initialNode: NodeName): Link {
        this.validateNode(initialNode);
        return new Link(this.name, initialNode, this.finalNode, this.conditionLink);
    }


    toString(): string {
        return `Link={name=${this.name}: initialNode=${this.initialNode}, finalNode=${this.finalNode}}`;
    }
}
