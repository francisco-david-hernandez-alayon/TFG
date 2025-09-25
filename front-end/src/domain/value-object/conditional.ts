import { ConditionalName } from './conditional-name';
import { NodeContent } from './node-content';
import { NodeContentType } from '../enumerate/node-content-type';
import { NodeName } from './node-name'; // Asegúrate de que exista

/**
 * @brief Represents a Conditional node with a name, execution code and a map of node link conditions
 */
export class Conditional extends NodeContent {
  private static readonly ERROR_NAME_NOT_DEFINED = 'Name for Conditional not defined';
  private static readonly ERROR_CODE_NOT_DEFINED = 'Execution code for Conditional not defined';
  private static readonly ERROR_LINKS_NOT_DEFINED = 'Links conditions map not defined';

  private readonly name: ConditionalName;
  private readonly executionCode: string;

  /**
   * @brief for each destination node, indicates whether the link condition is true or false
   */
  private readonly linksConditions: Map<NodeName, boolean>;

  constructor(
    name: ConditionalName,
    executionCode: string,
    linksConditions: Map<NodeName, boolean>
  ) {
    super();
    this.validateName(name);
    this.validateExecutionCode(executionCode);
    this.validateLinks(linksConditions);
    this.name = name;
    this.executionCode = executionCode;
    this.linksConditions = new Map(linksConditions);
  }

  private validateName(name: ConditionalName | null): void {
    if (!name) {
      throw new Error(Conditional.ERROR_NAME_NOT_DEFINED);
    }
  }

  private validateExecutionCode(code: string | null): void {
    if (!code || code.trim().length === 0) {
      throw new Error(Conditional.ERROR_CODE_NOT_DEFINED);
    }
  }

  private validateLinks(links: Map<NodeName, boolean> | null): void {
    if (!links) {
      throw new Error(Conditional.ERROR_LINKS_NOT_DEFINED);
    }
  }

  public getName(): ConditionalName {
    return this.name;
  }

  public getExecutionCode(): string {
    return this.executionCode;
  }

  public getLinksConditions(): Map<NodeName, boolean> {
    return new Map(this.linksConditions);
  }

  public setName(name: ConditionalName): Conditional {
    return new Conditional(name, this.executionCode, this.linksConditions);
  }

  public setExecutionCode(code: string): Conditional {
    return new Conditional(this.name, code, this.linksConditions);
  }

  public setLinksConditions(links: Map<NodeName, boolean>): Conditional {
    return new Conditional(this.name, this.executionCode, links);
  }

  public getType(): NodeContentType {
    return NodeContentType.CONDITIONAL;
  }

  public toString(): string {
    const linksArray = Array.from(this.linksConditions.entries()).map(
      ([nodeName, bool]) => `[${nodeName.toString()}, ${bool}]`
    ).join(', ');
    return `Conditional{name=${this.name.toString()}, executionCode="${this.executionCode}", linksConditions=[${linksArray}]}`;
  }

  public equals(other: Conditional): boolean {
    if (
      !this.name.equals(other.getName()) ||
      this.executionCode !== other.getExecutionCode()
    ) {
      return false;
    }

    const otherLinks = other.getLinksConditions();
    if (this.linksConditions.size !== otherLinks.size) {
      return false;
    }

    for (const [key, value] of this.linksConditions.entries()) {
      const otherValue = otherLinks.get(key);
      if (otherValue === undefined || otherValue !== value) {
        return false;
      }
    }

    return true;
  }
}
