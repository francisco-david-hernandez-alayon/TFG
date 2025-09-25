import { NodeContentType } from "../enumerate/node-content-type";


export abstract class NodeContent {
  abstract getType(): NodeContentType;
}
