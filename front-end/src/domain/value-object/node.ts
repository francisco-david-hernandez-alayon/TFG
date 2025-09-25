import { NodeContentType } from "../enumerate/node-content-type";
import { NodeContent } from "./node-content";
import { NodeColor } from "../enumerate/node-color";
import { XPosition } from "./x-position";
import { YPosition } from "./y-position";
import { Link } from "./link";
import { NodeName } from "./node-name";
import { NodeIcon } from "../enumerate/node-icon";


export class Node {
    private static readonly ERROR_COLOR_NOT_DEFINED = "Node color not defined.";
    private static readonly ERROR_NODE_NAME_NOT_DEFINED = "Node name not defined.";
    private static readonly ERROR_CONTENT_NOT_DEFINED = "Node content not defined.";
    private static readonly ERROR_LINKS_NOT_DEFINED = "Node links not defined.";
    private static readonly ERROR_LINK_NOT_DEFINED = "Link is not defined.";
    private static readonly ERROR_INITIAL_LINK = "Intial node name must match the link's initial node name.";
    private static readonly ERROR_POSITION_INVALID = " position is not defined";
    private static readonly ERROR_ICON_NOT_DEFINED = 'Node icon not defined';

    private readonly name: NodeName;
    private readonly xPosition: XPosition;
    private readonly yPosition: YPosition;
    private readonly color: NodeColor;
    private readonly icon: NodeIcon;
    private readonly content: NodeContent;
    private readonly links: Link[];

    constructor(
        name: NodeName,
        x: XPosition,
        y: YPosition,
        color: NodeColor,
        icon: NodeIcon,
        content: NodeContent,
        links: Link[]
    ) {
        this.validateNodeName(name);
        this.validatePositionX(x);
        this.validatePositionY(y);
        this.validateColor(color);
        this.validateIcon(icon);
        this.validateContent(content);
    
        this.name = name;

        this.validateLinks(links);
        this.xPosition = x;
        this.yPosition = y;
        this.color = color;
        this.icon = icon;
        this.content = content;
        this.links = [...links];
    }

    getName(): NodeName {
        return this.name;
    }

    getXPosition(): XPosition {
        return this.xPosition;
    }

    getYPosition(): YPosition {
        return this.yPosition;
    }

    getColor(): NodeColor {
        return this.color;
    }

    public getIcon(): NodeIcon {
        return this.icon;
    }

    getContent(): NodeContent {
        return this.content;
    }

    getType(): NodeContentType {
        return this.content.getType();
    }


    getLinks(): Link[] {
        return [...this.links];
    }


    addLink(link: Link): Node {
        this.validateLink(link);
        const newLinks = [...this.links, link];
        return new Node(this.name, this.xPosition, this.yPosition, this.color, this.icon, this.content, newLinks);
    }


    removeLink(link: Link): Node {
        const newLinks = this.links.filter(l => l !== link);
        return new Node(this.name, this.xPosition, this.yPosition, this.color, this.icon, this.content, newLinks);
    }

    setXPosition(x: XPosition): Node {
        return new Node(this.name, x, this.yPosition, this.color, this.icon, this.content, this.links);
    }

    setYPosition(y: YPosition): Node {
        return new Node(this.name, this.xPosition, y, this.color, this.icon, this.content, this.links);
    }

    setColor(color: NodeColor): Node {
        return new Node(this.name, this.xPosition, this.yPosition, color, this.icon, this.content, this.links);
    }

    setIcon(icon: NodeIcon): Node {
        return new Node(this.name, this.xPosition, this.yPosition, this.color, icon, this.content, this.links);
    }

    setLinks(links: Link[]): Node {
        return new Node(this.name, this.xPosition, this.yPosition, this.color, this.icon, this.content, links);
    }


    private validatePositionX(x: XPosition): void {
        if (!x) throw new Error("X" + Node.ERROR_POSITION_INVALID);
    }

    private validatePositionY(y: YPosition): void {
        if (!y) throw new Error("Y" + Node.ERROR_POSITION_INVALID);
    }

    private validateColor(color: NodeColor): void {
        if (!color) throw new Error(Node.ERROR_COLOR_NOT_DEFINED);
    }

    private validateIcon(icon: NodeIcon | null): void {
        if (!icon) {
            throw new Error(Node.ERROR_ICON_NOT_DEFINED);
        }
    }

    private validateNodeName(name: NodeName): void {
        if (!name) throw new Error(Node.ERROR_NODE_NAME_NOT_DEFINED);
    }

    private validateContent(content: NodeContent): void {
        if (!content) throw new Error(Node.ERROR_CONTENT_NOT_DEFINED);
    }

    private validateLinks(links: Link[]): void {
        if (!links) throw new Error(Node.ERROR_LINKS_NOT_DEFINED);

        links.forEach(link => {
            this.validateLink(link);
        });
    }

    private validateLink(link: Link): void {
        if (!link) throw new Error(Node.ERROR_LINK_NOT_DEFINED);
        if (!this.name.equals(link.getInitialNode())) {
            throw new Error(Node.ERROR_INITIAL_LINK);
        }
    }



    toString(): string {
        return `Node={name=${this.name.toString()}, x=${this.xPosition.getXPosition()}, y=${this.yPosition.getYPosition()}, color=${this.color}, icon=${this.icon}, type=${this.getType()}, links=${this.links.length}}`;
    }
}
