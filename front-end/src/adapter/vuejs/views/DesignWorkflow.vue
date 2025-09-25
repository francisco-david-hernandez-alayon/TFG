<template>
  <v-container fluid class="w-100 h-100 d-flex flex-column ma-0 pa-0">

    <!-- TOP BAR -->
    <v-row no-gutters align="center" justify="space-between" style="flex: 0 0 auto;" class="bg-secondary pa-2 border">
      <!-- Tabs -->
      <v-col cols="auto">
        <v-tabs v-model="tab" color="white" bg-color="secondary" height="40" density="compact">
          <v-tab value="workflow-details">
            {{ t('designWorkflow.workflowDetails') }}
          </v-tab>
          <v-tab value="designer">
            {{ t('designWorkflow.designWorkFlow') }}
          </v-tab>
        </v-tabs>
      </v-col>

      <!-- Title Centered -->
      <v-col class="d-flex justify-center" cols="auto">
        <v-text-field v-model="workFlowName" variant="underlined" color="white"
          class="text-white text-h6 font-weight-medium" hide-details style="width: 350px;" />
      </v-col>

      <!-- Save and Execute Buttons -->
      <v-col cols="auto" class="d-flex align-center gap-2 justify-end">
        <!-- Execute Workflow Dialog Button -->
        <ExecuteWorkflowDialog :workflow="workFlow as WorkFlow" :enabled="isWorkFlowSaved" />

        <!-- Save Button -->
        <v-tooltip :text="isWorkFlowSaved ? t('designWorkflow.alreadySavedWorkFlow') : t('designWorkflow.saveWorkFlow')"
          location="bottom">
          <template #activator="{ props }">
            <v-btn v-bind="props" icon size="small" color="white" variant="text" :disabled="isWorkFlowSaved"
              @click="saveWorkFlow">
              <v-icon color="white" size="28">mdi-content-save</v-icon>
            </v-btn>
          </template>
        </v-tooltip>
      </v-col>
    </v-row>




    <!-- CONTENT -->
    <v-row class="flex-grow-1 ma-0 pa-0 overflow-hidden">

      <!-- DESIGNER -->
      <template v-if="tab === 'designer'">
        <v-col class="pa-0 d-flex flex-column" cols="12">

          <!-- Toolbar -->
          <v-row class="bg-surface pa-2 border" no-gutters style="flex: 0 0 auto;">
            <!-- LEFT -->
            <v-col class="d-flex align-center" style="gap: 24px;">
              <v-tooltip location="top">
                <template #activator="{ props }">
                  <v-btn icon color="secondary" v-bind="props" @click="exportFlow">
                    <v-icon>mdi-file-export</v-icon>
                  </v-btn>
                </template>
                <span>{{ t('designWorkflow.exportFlow') }}</span>
              </v-tooltip>

              <AddOperationBar @operation-added="handleOperationAdded" />
              <AddFileBar @file-added="handleFileAdded" />
              <AddConditionalBar @conditional-added="handleConditionalAdded" />
            </v-col>

            <!-- RIGHT -->
            <v-col cols="auto" class="d-flex align-center justify-end">
              <v-btn size="small" color="primary" variant="outlined" class="ma-2 text-white" :disabled="isWorkFlowSaved"
                @click="setInitialNode">
                {{ t('designWorkflow.setInitialNode') }}
              </v-btn>
              <v-btn icon color="primary" @click="showSideCol = !showSideCol">
                <v-icon>mdi-menu</v-icon>
              </v-btn>
            </v-col>
          </v-row>



          <!-- content -->
          <v-row class="flex-grow-1 ma-0 pa-0 overflow-hidden bg-background" style="min-height: 0;">

            <!-- Vue Flow -->
            <v-col class="pa-10 fill-height border" style="min-height: 0; overflow: hidden; height: 100%;">
              <VueFlow :connection-mode="ConnectionMode.Loose" class="fill-width fill-height " v-model:nodes="nodes"
                v-model:edges="edges" :node-types="nodeTypes" :edge-types="edgeTypes" :default-viewport="{ zoom: 0.3 }"
                :min-zoom="0.2" :max-zoom="4" style="background-color: white;" @connect="handleConnect"
                @node-click="handleNodeClick" @edge-click="handleEdgeClick" @pane-click="clearSelections"
                @node-drag-stop="handleNodeDragStop">
                <Background :gap="64" pattern-color="#ccc" :line-width="1" variant="lines" />
                <Controls position="top-left">
                  <ControlButton title="Zoom In" @click="zoomIn">
                    <v-icon>mdi-magnify-plus</v-icon>
                  </ControlButton>
                  <ControlButton title="Zoom Out" @click="zoomOut">
                    <v-icon>mdi-magnify-minus</v-icon>
                  </ControlButton>
                  <ControlButton title="Reset Transform" @click="resetTransform">
                    <IconReset name="reset" />
                  </ControlButton>
                </Controls>
              </VueFlow>
            </v-col>

            <!-- Sidebar -->
            <v-col v-if="showSideCol" cols="3" class="bg-surface pa-2 d-flex flex-column align-center border body-1"
              style="min-height: 0;">

              <v-card-title class="text-h6 text-left text-primary--darken-1 w-100 bg-surface d-flex align-center"
                style="gap: 8px;">
                <v-icon color="primary">mdi-cog-outline</v-icon>
                {{ t('designWorkflow.propertiesPanel.title') }}
              </v-card-title>


              <v-divider class="my-4 mx-auto" style="width: 100%;" />

              <!-- Selected node data -->
              <div v-if="selectedNode" class="w-100 d-flex flex-column pa-3 rounded-lg" style="gap: 12px;">

                <!-- NODE fields -->
                <v-text-field :label="t('designWorkflow.propertiesPanel.nodeName')"
                  :model-value="selectedNode.data?.name" variant="outlined" density="compact" hide-details
                  @update:model-value="val => customUpdateNodeDataName(val)" />

                <v-select v-if="selectedNode && selectedNode.data" v-model="selectedNode.data.color" :items="nodeColors"
                  :label="t('designWorkflow.propertiesPanel.color')" color="success" variant="outlined"
                  density="compact" hide-details @update:modelValue="val => customUpdateNodeData('color', val)" />

                <v-select v-if="selectedNode && selectedNode.data" v-model="selectedNode.data.icon" :items="nodeIcons"
                  :label="t('designWorkflow.propertiesPanel.icon')" color="success" variant="outlined" density="compact"
                  hide-details @update:modelValue="val => customUpdateNodeData('icon', val)" />

                <v-btn color="red" variant="flat" @click="removeNode(selectedNode.id)">
                  {{ t('designWorkflow.propertiesPanel.removeNode') }}
                </v-btn>

                <v-divider class="my-4 mx-auto" style="width: 90%;" />

                <v-chip class="mx-auto mb-4 text-subtitle-1 font-weight-medium" :color="chipColor" text-color="white"
                  label>
                  <v-icon start>{{ chipIcon }}</v-icon>
                  {{ chipLabel }}
                </v-chip>

                <!-- FILE fields -->
                <template v-if="selectedNode.data?.type === NodeContentType.FILE">
                  <v-text-field :model-value="(selectedNode.data?.nodeContent as File).getName().getName()"
                    :label="t('designWorkflow.propertiesPanel.fileName')" variant="outlined" density="compact"
                    hide-details @update:model-value="val => {
                      const fileNode = (selectedNode?.data?.nodeContent as File).setName(new FileName(val));
                      customUpdateNodeData('nodeContent', fileNode);
                    }" />

                  <v-text-field :model-value="(selectedNode.data?.nodeContent as File).getUri()"
                    :label="t('designWorkflow.propertiesPanel.uri')" variant="outlined" density="compact" hide-details
                    @update:model-value="val => {
                      const fileNode = (selectedNode?.data?.nodeContent as File).setUri(val);
                      customUpdateNodeData('nodeContent', fileNode);
                    }" />
                </template>


                <!-- OPERATION fields -->
                <template v-if="selectedNode.data?.type === NodeContentType.OPERATION">
                  <v-text-field :model-value="(selectedNode.data?.nodeContent as Operation).getName().getName()"
                    :label="t('designWorkflow.propertiesPanel.operationName')" readonly variant="outlined"
                    density="compact" hide-details />

                  <v-text-field :model-value="(selectedNode.data?.nodeContent as Operation).getDockerImage()"
                    :label="t('designWorkflow.propertiesPanel.dockerImage')" readonly variant="outlined"
                    density="compact" hide-details />
                </template>


                <!-- CONDITIONAL fields -->
                <template v-if="selectedNode.data?.type === NodeContentType.CONDITIONAL">
                  <v-text-field :model-value="(selectedNode.data?.nodeContent as Conditional).getName().getName()"
                    :label="t('designWorkflow.propertiesPanel.conditionalName')" variant="outlined" density="compact"
                    hide-details @update:model-value="val => {
                      let conditional = selectedNode?.data?.nodeContent as Conditional;
                      const updated = conditional.setName(new ConditionalName(val));
                      customUpdateNodeData('nodeContent', updated);
                    }" />

                  <v-textarea :model-value="(selectedNode.data?.nodeContent as Conditional).getExecutionCode()"
                    :label="t('designWorkflow.propertiesPanel.executionCode')" variant="outlined" density="compact"
                    hide-details auto-grow rows="2" @update:model-value="val => {
                      let conditional = selectedNode?.data?.nodeContent as Conditional;
                      const updated = conditional.setExecutionCode(val);
                      customUpdateNodeData('nodeContent', updated);
                    }" />


                  <v-virtual-scroll
                    :items="Array.from((selectedNode.data?.nodeContent as Conditional).getLinksConditions().entries())"
                    item-height="48" :height="120" class="mt-2" dense>
                    <template #default="{ item }">
                      <v-list-item :key="item[0].getName()">
                        <v-list-item-title>
                          <v-row align="center" no-gutters>
                            <v-col cols="6" class="d-flex align-center">
                              <span class="text-body-1 font-weight-medium">
                                {{ item[0].getName() }}
                              </span>
                            </v-col>

                            <v-col cols="6" class="d-flex justify-end">
                              <v-select :items="linkConditionItems" v-model="item[1]" item-title="label"
                                item-value="value" hide-details dense style="max-width: 130px; min-width: 130px;"
                                @update:model-value="newVal => {
                                  let conditional = selectedNode?.data?.nodeContent as Conditional;
                                  const updatedLinks = new Map(conditional.getLinksConditions());
                                  updatedLinks.set(item[0], newVal);
                                  const updatedConditional = conditional.setLinksConditions(updatedLinks);
                                  updateNodeConditionData(updatedConditional);
                                }" :menu-props="{ maxHeight: '200px' }">
                                <template #item="{ item: selectItem, props }">
                                  <v-list-item v-bind="props">
                                    <v-list-item-title
                                      :class="(selectItem as any).color === 'success' ? 'text-success' : 'text-error'">
                                      {{ (selectItem as any).label }}
                                    </v-list-item-title>
                                  </v-list-item>
                                </template>

                                <template #selection="{ item: selectedItem }">
                                  <v-chip v-if="selectedItem"
                                    :color="linkConditionItems.find(i => i.value === selectedItem.props.value)?.color || 'grey'"
                                    text-color="white" small outlined>
                                    {{linkConditionItems.find(i => i.value === selectedItem.props.value)?.label || ''
                                    }}
                                  </v-chip>
                                </template>
                              </v-select>
                            </v-col>
                          </v-row>
                        </v-list-item-title>
                      </v-list-item>
                    </template>
                  </v-virtual-scroll>


                </template>

              </div>



              <!-- Selected link data -->
              <div v-else-if="selectedLink" class="w-100 d-flex flex-column pa-3 rounded-lg" style="gap: 12px;">
                <v-chip class="mx-auto mb-4" color="warning" text-color="white" label
                  style="font-weight: 600; font-size: 1.1rem;">
                  {{ t('designWorkflow.propertiesPanel.linkProperties') }}
                </v-chip>

                <v-text-field :label="t('designWorkflow.propertiesPanel.sourceLink')" :model-value="selectedLink.source"
                  readonly variant="outlined" density="compact" hide-details />

                <v-text-field :label="t('designWorkflow.propertiesPanel.targetLink')" :model-value="selectedLink.target"
                  readonly variant="outlined" density="compact" hide-details />

                <v-text-field :label="t('designWorkflow.propertiesPanel.conditionLink')"
                  :model-value="selectedLink.data?.condition" readonly variant="outlined" density="compact"
                  hide-details />

                <v-text-field v-model="selectedLink.label" :label="t('designWorkflow.propertiesPanel.linkName')"
                  variant="outlined" density="compact" hide-details @update:modelValue="updateLink" />

                <div class="d-flex flex-column align-center" style="gap: 0;">
                  <v-switch :model-value="selectedLink.data?.enabled"
                    :color="selectedLink.data?.enabled ? 'success' : 'error'" @change="toggleLinkEnabled" class="ma-0"
                    style="transform: scale(1.3);" />
                </div>

                <v-btn color="red" variant="flat" @click="removeLink(selectedLink.id)">
                  {{ t('designWorkflow.propertiesPanel.removeLink') }}
                </v-btn>
              </div>

            </v-col>



          </v-row>
        </v-col>
      </template>




      <!-- DETAILS -->
      <template v-if="tab === 'workflow-details'">
        <v-container class="d-flex justify-center align-center pa-10" style="min-height: 300px;">
          <v-card class="w-100" max-width="600" color="surface" elevation="2" rounded>
            <v-card-title class="text-h4 text-center text-primary">
              {{ t('designWorkflow.workflowDetails') }}
            </v-card-title>

            <v-divider></v-divider>

            <v-card-text>
              <div v-if="workFlow" class="mt-4">
                <v-row v-for="(label, key) in [
                  [t('designWorkflow.id'), workFlow?.getId().getValue()],
                  [t('designWorkflow.name'), workFlow?.getName().getName()],
                  [t('designWorkflow.creationDate'), formatDate(workFlow?.getCreationDate())],
                  [t('designWorkflow.nodes'), workFlow?.getNodes().length],
                  [t('designWorkflow.firstNode'), workFlow?.getFirstNode().getName().getName()]
                ]" :key="key" class="mb-3" align="center" justify="center">
                  <v-col cols="5" class="text-right text-subtitle-1 font-weight-medium text-secondary">
                    {{ label[0] }}:
                  </v-col>
                  <v-col cols="7" class="text-left text-body-1">
                    {{ label[1] }}
                  </v-col>
                </v-row>
              </div>

              <div v-else class="text-center mt-6">
                <v-icon color="grey" size="48">mdi-alert-circle-outline</v-icon>
                <p class="text-subtitle-1 text-grey mt-2">
                  {{ t('designWorkflow.noWorkFlowSelected') }}
                </p>
              </div>
            </v-card-text>
          </v-card>
        </v-container>
      </template>


    </v-row>

  </v-container>
</template>



<script setup lang="ts">
/* these are necessary styles for vue flow */
import '@vue-flow/core/dist/style.css';
import '@vue-flow/core/dist/theme-default.css';

import ExecuteWorkflowDialog from '@/adapter/vuejs/components/layout/ExecuteWorkflowDialog.vue';
import AddOperationBar from '@/adapter/vuejs/components/vueflow/AddOperationBar.vue';
import AddFileBar from '@/adapter/vuejs/components/vueflow/AddFileBar.vue';
import AddConditionalBar from '@/adapter/vuejs/components/vueflow/AddConditionalBar.vue';
import { watch, onMounted, ref } from 'vue';
import { ConnectionMode, VueFlow, useVueFlow, type GraphNode } from '@vue-flow/core';
import { Background } from '@vue-flow/background';
import { ControlButton, Controls } from '@vue-flow/controls';
import IconReset from '@/adapter/vuejs/components/vueflow/IconReset.vue';
import { type Node, type Edge } from '@vue-flow/core'

import { useI18n } from 'vue-i18n';
import CustomNodeVueFlow from '../components/vueflow/CustomNodeVueFlow.vue';
import EdgeVueFlow from '../components/vueflow/EdgeVueFlow.vue';
import { WorkFlowHttpRepository } from '@/adapter/http/repository/workflow-http-repository';
import { WorkFlowExportJsonRequest } from '@/adapter/http/request/workflow-export-json-request';
import { WorkFlow } from '@/domain/entity/workflow';
import { NodeColor } from '@/domain/enumerate/node-color';
import { NodeIcon } from '@/domain/enumerate/node-icon';
import { nextTick } from 'vue';
import { Link } from '@/domain/value-object/link';
import { LinkName } from '@/domain/value-object/link-name';
import { Operation } from '@/domain/value-object/operation';
import { inject } from 'vue';
import { XPosition } from '@/domain/value-object/x-position';
import { YPosition } from '@/domain/value-object/y-position';
import { Node as CustomNode } from '@/domain/value-object/node';
import { NodeName } from '@/domain/value-object/node-name';
import { NodeContent } from '@/domain/value-object/node-content';
import { File } from '@/domain/value-object/file';
import { useRoute } from 'vue-router';
import { UllUUID } from "@ull-tfg/ull-tfg-typescript";
import { WorkFlowName } from '@/domain/value-object/workflow-name';
import { FileName } from '@/domain/value-object/file-name';
import { Conditional } from '@/domain/value-object/conditional';
import { ConditionalName } from '@/domain/value-object/conditional-name';
import { NodeContentType } from '@/domain/enumerate/node-content-type';
import { computed } from 'vue';


const { t } = useI18n();
const tab = ref('designer');
const route = useRoute();

// CUSTOM TYPES
// custom type to avoid type loops
type SimpleEdge = {
  id: string;
  label: string;
  type: string;
  source: string;
  target: string;
  data: {
    enabled: boolean;
    condition: string;
  };
};
type SimpleNode = {
  id: string;
  type: string;
  position: { x: number; y: number };
  data: CustomVueFlowNodeData;
};

type CustomVueFlowEdge = Edge<{ enabled: boolean; condition: string }>;
type CustomVueFlowNodeData = {
  name: string;
  color: string;
  icon: string;
  type: string;
  isInitial: Boolean;
  nodeContent: NodeContent;
};
type CustomVueFlowNode = Node<CustomVueFlowNodeData>;


// ConditionLink colors and labels
type LinkConditionItem = {
  label: string;
  value: boolean;
  color: 'success' | 'error';
}
const linkConditionItems: LinkConditionItem[] = [
  { label: 'True', value: true, color: 'success' },
  { label: 'False', value: false, color: 'error' }
]


//--------------------------------------GENERAL DATA--------------------------------------//

// Current workflow data
const repoWorkFlows = new WorkFlowHttpRepository();
const isWorkFlowCreated = ref<boolean>(false);
const isWorkFlowSaved = ref<boolean>(false);
const workflowIdFromRoute = route.params.id as string;
const nodeColors = Object.keys(NodeColor);
const nodeIcons = Object.keys(NodeIcon);

const workFlow = ref<WorkFlow | null>(null);
const workFlowId = ref();
const workFlowName = ref<string>('');
const nodes = ref<CustomVueFlowNode[]>([]);
const edges = ref<CustomVueFlowEdge[]>([]);

// selected data
const selectedNode = ref<CustomVueFlowNode | null>(null);
const selectedLink = ref<CustomVueFlowEdge | null>(null);

// sidevar visibility
const showSideCol = ref(true)

// DATE
function formatDate(date: Date | string): string {
  const d = new Date(date);
  if (isNaN(d.getTime())) return "Invalid date";

  return d.toLocaleString('es-ES', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false,
  });
}

//-----------------------------------VUE-FLOW-DATA------------------------------------//
const {
  onInit,
  addEdges,
  addNodes,
  updateNode,
  updateNodeData,
  findNode,
  removeNodes,
  removeEdges,
  fitView,
  zoomIn,
  zoomOut,
} = useVueFlow()

const nodeTypes = {
  'custom-node': CustomNodeVueFlow,
}

const edgeTypes = {
  'custom-edge': EdgeVueFlow,
}



//-----------------------------------GET-FLOW----------------------------------//

// FECTH WORKFLOW BY ID
const fetchWorkFlow = async (idWorkFlow: UllUUID) => {
  // Clean values
  nodes.value = [];
  edges.value = [];
  await nextTick();

  const result = await repoWorkFlows.getWorkFlow(idWorkFlow);

  if (result.isRight()) {
    workFlow.value = result.get();

    // Get workflow data
    workFlowName.value = workFlow.value.getName().getName();
    const firstNode = workFlow.value.getFirstNode();
    const nodeMap = workFlow.value.getNodes();

    // Prepare arrays to hold VueFlow nodes and edges
    const vueFlowNodes: CustomVueFlowNode[] = [];
    const vueFlowEdges: CustomVueFlowEdge[] = [];

    for (const node of nodeMap.values()) {
      const isInitial = firstNode.getName().equals(node.getName()) ?? false;

      // Add node
      vueFlowNodes.push(nodeToVueFlowNode(node, isInitial));

      // Add links
      const links = node.getLinks();
      for (const link of links) {
        let conditionValue = "no-condition";

        // Check if node is CONDITIONAL to determine condition value on links
        if (node.getType() === NodeContentType.CONDITIONAL) {
          const conditionalContent = node.getContent() as Conditional;
          const linksConditions = conditionalContent.getLinksConditions();
          const targetName = link.getFinalNode();

          const matchingEntry = Array.from(linksConditions.entries())
            .find(([nodeName, _]) => nodeName.equals(targetName));

          if (matchingEntry) {
            conditionValue = matchingEntry[1] ? "true" : "false";
          }

        }

        // Create VueFlow edge for this link
        const edge: CustomVueFlowEdge = linkToVueFlowEdge(link, conditionValue);

        vueFlowEdges.push(edge);
      }
    }

    // Assign prepared arrays to reactive refs
    nodes.value = vueFlowNodes;
    edges.value = vueFlowEdges;

    workFlowId.value = workFlow.value.getId();  // Store workflow ID

    // Reset view
    setTimeout(() => {
      resetTransform();
    }, 50);

  } else {
    console.error("Error fetching workflow", workFlowId.value);
    showNotif("error", "Failed to fetch workflow");
  }
};

// FECTH DEFAULT WORKFLOW
const fetchDefaultWorkflow = async () => {
  // Clean values
  nodes.value = [];
  edges.value = [];
  await nextTick();

  // create default workflow
  const defaultNameWorkFlow = t('designWorkflow.noTitleWorkFlow');
  const defaultNodeName = new NodeName('Node 1');
  const defaultNodeContent = new File(new FileName('File 1'));
  const defaultNodePosition = new XPosition(0);
  const defaultNodeYPosition = new YPosition(0);
  const defaultNode = new CustomNode(defaultNodeName, defaultNodePosition, defaultNodeYPosition, NodeColor.BLUE, NodeIcon.START, defaultNodeContent, []);

  workFlow.value = new WorkFlow(new WorkFlowName(defaultNameWorkFlow), [defaultNode], true);
  workFlowName.value = defaultNameWorkFlow;


  // get first node
  const firstNode = workFlow.value.getFirstNode();

  // Get all CustomNodes
  const nodeMap = workFlow.value.getNodes();

  // Set First VueFlow node
  const vueFlowNodes = Array.from(nodeMap.values()).map(node => {
    const isInitial = firstNode.getName().equals(node.getName()) ?? false;
    return nodeToVueFlowNode(node, isInitial);
  });

  nodes.value = vueFlowNodes;

  // Reset view
  setTimeout(() => {
    resetTransform();
  }, 50);
}





//-----------------------------------NODE-CONVERSORS------------------------------------//
// Node to VueFlow Node
function nodeToVueFlowNode(node: CustomNode, isInitial: Boolean): CustomVueFlowNode {
  const position = {
    x: node.getXPosition().getXPosition(),
    y: node.getYPosition().getYPosition(),
  };

  const content = node.getContent();
  const type = content.getType();

  return {
    id: node.getName().getName(),
    type: 'custom-node',
    position,
    data: {
      name: node.getName().getName(),
      color: node.getColor(),
      icon: node.getIcon(),
      type,
      isInitial,
      nodeContent: content,
    }
  };
}


// VueFlow Node to node
function vueFlowNodeToNode(workFlowNode: CustomVueFlowNode): CustomNode {
  const name = new NodeName(workFlowNode.id);
  const x = new XPosition(workFlowNode.position.x);
  const y = new YPosition(workFlowNode.position.y);
  const color = workFlowNode.data?.color as NodeColor;
  const icon = workFlowNode.data?.icon as NodeIcon;

  const content = workFlowNode.data?.nodeContent as NodeContent;

  return new CustomNode(name, x, y, color, icon, content, []);
}




//---------------------------LINK-CONVERSORS---------------------------//
// link to VueFlow Edge
function linkToVueFlowEdge(link: Link, conditionValue: string): CustomVueFlowEdge {
  const labelValue = link.getName().getName();

  return {
    id: `${link.getInitialNode().getName()}-${link.getFinalNode().getName()}`,
    source: link.getInitialNode().getName(),
    target: link.getFinalNode().getName(),
    label: labelValue.length > 0 ? labelValue : 'link',
    type: 'custom-edge',
    data: {
      enabled: link.getCondition(),
      condition: conditionValue
    }
  };
}






//---------------------------VUE-FLOW-FUNCTIONS---------------------------//

// RESET VIEWPORT
function resetTransform() {
  fitView({ padding: 0.7, includeHiddenNodes: false });
}

watch(tab, async (newVal) => {
  if (newVal === 'designer') {
    await nextTick(); // time to wait for the DOM to update
    setTimeout(() => {
      resetTransform();
    }, 50);
  }
});


// CREATE NODE
function handleOperationAdded({ operation, name }: { operation: Operation; name: NodeName }) {
  const existingNode = findNode(name.getName());

  const node = new CustomNode(
    name,
    new XPosition(0),
    new YPosition(0),
    NodeColor.BLUE,
    NodeIcon.START,
    operation,
    []
  );

  const isInitial = false;

  const newNode = nodeToVueFlowNode(node, isInitial);

  if (existingNode) {
    showNotif('error', t('designWorkflow.errorNodeNameExists') + ": " + name.getName());
  } else {
    addNodes([newNode]);
    showNotif('success', t('designWorkflow.nodeAdded') + ": " + name.getName());
  }
}

function handleFileAdded({ file, name }: { file: File; name: NodeName }) {
  const existingNode = findNode(name.getName());

  const node = new CustomNode(
    name,
    new XPosition(0),
    new YPosition(0),
    NodeColor.GRAY,
    NodeIcon.FOLDER,
    file,
    []
  );

  const isInitial = false;

  const newNode = nodeToVueFlowNode(node, isInitial);

  if (existingNode) {
    showNotif('error', t('designWorkflow.errorNodeNameExists') + ": " + name.getName());
  } else {
    addNodes([newNode]);
    showNotif('success', t('designWorkflow.nodeAdded') + ": " + name.getName());
  }
}


function handleConditionalAdded({ conditional, name }: { conditional: Conditional; name: NodeName }) {
  const existingNode = findNode(name.getName());

  const node = new CustomNode(
    name,
    new XPosition(0),
    new YPosition(0),
    NodeColor.YELLOW,
    NodeIcon.GEAR,
    conditional,
    []
  );

  const isInitial = false;

  const newNode = nodeToVueFlowNode(node, isInitial);

  if (existingNode) {
    showNotif('error', t('designWorkflow.errorNodeNameExists') + ": " + name.getName());

  } else {
    addNodes([newNode]);
    showNotif('success', t('designWorkflow.nodeAdded') + ": " + name.getName());
  }
}





// REMOVE NODE AND ASSOCIATED LINKS
function removeNode(nodeId: string) {
  // Delete node
  removeNodes([nodeId]);

  // Search edges to delete
  const relatedEdges = (edges.value as SimpleEdge[]).filter(
    e => e.source === nodeId || e.target === nodeId
  );

  // delete edges
  relatedEdges.forEach(edge => {
    removeLink(edge.id);
  });

  isWorkFlowSaved.value = false;
}


// REMOVE EDGE
function removeLink(linkId: string) {
  if (!edges.value || !nodes.value) return;

  // Get and delete the edge by linkId
  const edge: CustomVueFlowEdge | undefined = (edges.value as CustomVueFlowEdge[]).find(e => e.id === linkId);

  if (!edge) return;

  removeEdges([linkId]);

  // Get source node for the edge
  let sourceNode = findNodeById(edge.source);


  if (!sourceNode) {
    isWorkFlowSaved.value = false;
    return;
  }

  if (sourceNode.data?.type === NodeContentType.CONDITIONAL) {
    const conditional = sourceNode.data.nodeContent as Conditional;
    const updatedLinks = new Map(conditional.getLinksConditions());
    const targetNodeName = new NodeName(edge.target);  // NodeName to delete

    const targetNodeNameKey = Array.from(updatedLinks.keys()).find(
      key => key.equals(targetNodeName)
    );


    if (targetNodeNameKey) {
      updatedLinks.delete(targetNodeNameKey);

      const updatedConditional = conditional.setLinksConditions(updatedLinks);

      updateNodeData(sourceNode.id, {
        ...sourceNode.data,
        nodeContent: updatedConditional,
      });
    }

  }

  isWorkFlowSaved.value = false;
}

function findNodeById(id: string): GraphNode | undefined {
  return (nodes.value as GraphNode[]).find(n => n.id === id);
}



// UPDATE NODE DATA
function customUpdateNodeData(
  key: keyof GraphNode['data'],
  value: any
) {
  if (!selectedNode.value) return;

  const nodeId = selectedNode.value.id;

  const nodesList = nodes.value as CustomVueFlowNode[];
  const node = nodesList.find(n => n.id === nodeId);

  if (!node) return;

  updateNode(nodeId, {
    data: {
      ...node.data,
      [key]: value,
    }
  });

  selectedNode.value = {
    ...node,
    data: {
      ...node.data as CustomVueFlowNodeData,
      [key]: value,
    }
  };

  isWorkFlowSaved.value = false;
}


function customUpdateNodeDataName(newName: string) {

  // Validation: check if selectedNode and nodes are defined
  if (!selectedNode.value || !nodes.value) return;

  // Validation: check if newName is a non-empty string
  const trimmedName = newName.trim();
  if (!trimmedName) {
    return;
  }

  // Validation: check if newName is a valid NodeName
  try {
    const temp: NodeName = new NodeName(trimmedName);
  } catch (error) {
    console.error(error);
    return;
  }

  const oldId = selectedNode.value.id;

  // Check for duplicate
  if ((nodes.value as GraphNode[]).some(n => n.id === newName && n.id !== oldId)) {
    showNotif('error', t('designWorkflow.errorNodeNameExists') + ": " + newName);
    return;
  }

  const oldNode = findNode(oldId) as GraphNode;
  if (!oldNode) return;

  // Deep clone edges BEFORE removal
  const connectedEdges = (edges.value as CustomVueFlowEdge[]).filter(
    (edge: CustomVueFlowEdge) => edge.source === oldId || edge.target === oldId
  );

  // Remove edges and node
  removeEdges(connectedEdges.map(e => e.id));
  removeNodes([oldId]);

  // Delay node/edge recreation to avoid internal Vue Flow state bugs
  nextTick(() => {
    // New node
    const newNode: CustomVueFlowNode = {
      ...oldNode,
      id: newName,
      data: {
        ...oldNode.data,
        name: newName,
      },
    };

    // Updated edges with new ID references and unique IDs
    const updatedEdges = connectedEdges.map(edge => {
      const newSource = edge.source === oldId ? newName : edge.source;
      const newTarget = edge.target === oldId ? newName : edge.target;

      return {
        ...edge,
        source: newSource,
        target: newTarget,
        id: `${newSource}-${newTarget}-${Date.now()}`, // ensure uniqueness
      };
    });

    addNodes([newNode]);
    addEdges(updatedEdges);


    // If in any link with this node as target, and source is Conditional, update its linksConditions
    updatedEdges.forEach(edge => {
      if (edge.target !== newName) return;

      const sourceNode = findNode(edge.source);
      if (!sourceNode || sourceNode.data?.type !== NodeContentType.CONDITIONAL) return;

      const conditional = sourceNode.data.nodeContent as Conditional;
      const oldLinks = conditional.getLinksConditions();
      const oldNodeName = new NodeName(oldId);

      // Search oldNodeName
      let value: boolean | undefined = undefined;
      for (const [key, val] of oldLinks.entries()) {
        if (key.equals(oldNodeName)) {
          value = val;
          break;
        }
      }

      if (value === undefined) return;

      // Create new linksConditions Map
      const updatedLinks = new Map<NodeName, boolean>();
      for (const [key, val] of oldLinks.entries()) {
        // Copiamos todo excepto la key vieja que queremos eliminar
        if (!key.equals(oldNodeName)) {
          updatedLinks.set(key, val);
        }
      }
      // Add new Key
      updatedLinks.set(new NodeName(newName), value);

      const updatedConditional = conditional.setLinksConditions(updatedLinks);

      updateNodeData(edge.source, {
        ...sourceNode.data,
        nodeContent: updatedConditional,
      });
    });

    selectedNode.value = { ...newNode };
    isWorkFlowSaved.value = false;
  });
}



// Update node condition data for conditional links
function updateNodeConditionData(newConditionalVal: Conditional) {
  if (!selectedNode.value || !nodes.value || !edges.value) return;
  if (selectedNode.value.data?.type !== NodeContentType.CONDITIONAL) return;

  // Get Node
  const node = (nodes.value as GraphNode[]).find(n => n.id === selectedNode.value?.id);
  if (!node) return;

  // Get current Conditional and its links
  const oldConditional = node.data?.nodeContent as Conditional;
  const oldLinks = oldConditional.getLinksConditions();
  const newLinks = newConditionalVal.getLinksConditions();

  // Update node data with new Conditional
  node.data = {
    ...node.data,
    nodeContent: newConditionalVal,
  };

  // Update selectedNode
  selectedNode.value = { ...node };

  // Update link
  for (const [nodeName, newVal] of newLinks.entries()) {
    const oldVal = oldLinks.get(nodeName);

    if (oldVal !== newVal) {
      // find edge to update
      const edge = (edges.value as CustomVueFlowEdge[]).find(
        e => e.source === selectedNode.value?.id && e.target === nodeName.getName()
      );

      if (edge) {
        edge.data = {
          ...edge.data,
          enabled: edge.data?.enabled ?? true,
          condition: newVal.toString(),
        };
      }
    }
  }

  isWorkFlowSaved.value = false;
}






// UPDATE LINK
function updateLink() {
  if (!selectedLink.value || !edges.value) return;

  const index = edges.value.findIndex(edge => edge.id === selectedLink.value!.id);
  if (index === -1) return;

  const edge = edges.value[index];
  const simpleEdge = edge as unknown as SimpleEdge;

  const updatedEdge: SimpleEdge = {
    ...simpleEdge,
    label: selectedLink.value.label as string,
    data: {
      enabled: selectedLink.value.data?.enabled ?? true,
      condition: selectedLink.value.data?.condition ?? "no-condition",
    }
  };


  edges.value.splice(index, 1, updatedEdge);

  // Change data
  selectedLink.value.label = updatedEdge.label;
  selectedLink.value.data = {
    enabled: updatedEdge.data.enabled,
    condition: updatedEdge.data.condition,
  };

  isWorkFlowSaved.value = false;
}

function toggleLinkEnabled() {
  if (selectedLink.value?.data) {
    selectedLink.value.data.enabled = !selectedLink.value.data.enabled;
    updateLink();
  }
}




// INITIAL NODE FUNCTIONS
function setInitialNode() {
  if (!nodes.value) return;

  const nodesList = nodes.value as CustomVueFlowNode[];
  const edgesList = edges.value as CustomVueFlowEdge[];

  // Get all node IDs
  const candidateInitialNodes = new Set(nodesList.map(node => node.id));

  // Remove nodes that are targets of any edge
  for (const edge of edgesList) {
    candidateInitialNodes.delete(edge.target);
  }

  // If there's exactly one initial candidate, mark it
  if (candidateInitialNodes.size === 1) {
    const initialNodeId = candidateInitialNodes.values().next().value;

    // Update each node to set isInitial
    nodesList.forEach(node => {
      updateNodeData(node.id, {
        ...node.data,
        isInitial: node.id === initialNodeId,
      });
    });

    isWorkFlowSaved.value = false;
  } else {
    showNotif('error', t('designWorkflow.errorMultipleInitialNodes'));
  }
}


// Mark the first node from the workflow as initial
function markAsInitialFromWorkFlow() {
  const nodeId = workFlow.value?.getFirstNode().getName().getName();
  if (!nodeId || !nodes.value) return;

  const simpleNodes = nodes.value as unknown as SimpleNode[];

  nodes.value = simpleNodes.map(node => ({
    ...node,
    data: {
      ...node.data,
      isInitial: node.id === nodeId,
    },
  })) as unknown as CustomVueFlowNode[];

  isWorkFlowSaved.value = false;
}



// SAVE FLOW STATE
const saveWorkFlow = async () => {
  if (!workFlow.value) {
    showNotif('error', t('unexpectedError') + ": " + "Workflow not initialized");
    return;
  }

  try {
    const nodeMap = new Map<string, CustomNode>();

    // Build nodes
    nodes.value?.forEach(vueFlowNode => {
      const customNode = vueFlowNodeToNode(vueFlowNode);
      nodeMap.set(customNode.getName().getName(), customNode);
    });

    // Build links by adding them to each source node
    edges.value?.forEach(edge => {
      let sourceNode = nodeMap.get(edge.source);
      const targetNode = nodeMap.get(edge.target);

      if (!sourceNode || !targetNode) {
        console.warn(`Invalid edge with source ${edge.source} or target ${edge.target}`);
        return;
      }

      const name = new LinkName(edge.label?.toString() ?? '');
      const enabled = edge.data?.enabled ?? true;
      //const condition = edge.data?.condition ?? "no-condition"

      const link = new Link(name, sourceNode.getName(), targetNode.getName(), enabled);

      sourceNode = sourceNode.addLink(link); // Add link to source node
      nodeMap.set(edge.source, sourceNode);  // update map
    });

    // Update workflow with the new nodes and name
    let finalFlow = workFlow.value.setNodes(Array.from(nodeMap.values()));
    finalFlow = finalFlow.setName(new WorkFlowName(workFlowName.value));

    if (isWorkFlowCreated.value) {
      // update the workflow
      await repoWorkFlows.updateWorkFlow(finalFlow.getId(), finalFlow).then(result => {
        if (result.isRight()) {
          workFlow.value = result.get();
          // showNotif('success', t('flowSaved'));
        } else {
          showNotif('error', t('flowSaveFailed') + ": " + result.getLeft());
        }
      });

    } else {
      // create the workflow
      await repoWorkFlows.saveWorkFlow(finalFlow).then(result => {
        if (result.isRight()) {
          workFlow.value = result.get();
          workFlowId.value = workFlow.value.getId();


          isWorkFlowCreated.value = true;
          showNotif('success', t('flowCreated') + ": " + workFlow.value.getName().getName());

        } else {
          showNotif('error', t('flowCreateFailed') + ": " + result.getLeft());

        }
      });

    }

    setInitialNode();
    isWorkFlowSaved.value = true;


  } catch (e: any) {
    showNotif('error', `${t('unexpectedError')}: ${e.message || e.toString()}`);
  }
}


watch(workFlowName, () => {
  isWorkFlowSaved.value = false;
});





//---------------------------VUE-FLOW-HANDLES---------------------------//

// HANDLE NODE CLICK
function handleNodeClick({ node }: { node: CustomVueFlowNode }) {
  selectedLink.value = null;

  selectedNode.value = node;

  if (node.data?.type === NodeContentType.CONDITIONAL) {
    const conditional = node.data.nodeContent as Conditional;
    const linksConditions = conditional.getLinksConditions();

    console.log(
      'Links Conditions:',
      Array.from(linksConditions).map(([key, value]) => [key.getName(), value])
    );
  }
}


// HANDLE EDGE CLICK
function handleEdgeClick({ edge }: { edge: CustomVueFlowEdge }) {
  selectedNode.value = null;
  selectedLink.value = edge;
}

// HANDLE PANEL CLICK
function clearSelections() {
  selectedNode.value = null;
  selectedLink.value = null;
}


// HANDLE NODE DRAG STOP
function handleNodeDragStop({ node }: { node: Node }) {
  if (!nodes.value) return;

  const idx = nodes.value.findIndex(n => n.id === node.id);
  if (idx !== -1) {
    nodes.value[idx].position = node.position;
  }

  isWorkFlowSaved.value = false;
}

// HANDLE CONNECT
function handleConnect({ source, target }: { source: string; target: string }) {
  const edgeId = `${source}-${target}`;

  // get source node to check if it exists
  const sourceNode = findNodeById(source);
  if (!sourceNode) {
    console.warn(`Source node with id ${source} not found`);
    return;
  }

  // Create new edge
  let newEdge: CustomVueFlowEdge = {
    id: edgeId,
    source,
    target,
    label: 'link',
    type: 'custom-edge',
    data: {
      enabled: true,
      condition: "no-condition",
    },
  };

  // Check if source node is CONDITIONAL
  if (sourceNode.data?.type === NodeContentType.CONDITIONAL) {
    newEdge.data!.condition = "true";

    const conditional = sourceNode.data.nodeContent as Conditional;

    const updatedLinks = new Map(conditional.getLinksConditions());
    updatedLinks.set(new NodeName(target), true);

    const updatedConditional = conditional.setLinksConditions(updatedLinks);

    updateNodeData(source, {
      ...sourceNode.data,
      nodeContent: updatedConditional,
    });
  }


  addEdges([newEdge]);
  isWorkFlowSaved.value = false;
}



// FIT ON INIT
onInit(() => {
  fitView()
})



// NODE PROPERTIES TITLE
const nodeType = computed(() => selectedNode.value?.data?.type || 'UNKNOWN');

const chipColor = computed(() => {
  switch (nodeType.value) {
    case NodeContentType.FILE: return 'secondary';
    case NodeContentType.OPERATION: return 'primary';
    case NodeContentType.CONDITIONAL: return 'warning';
    default: return 'grey';
  }
});

const chipIcon = computed(() => {
  switch (nodeType.value) {
    case NodeContentType.FILE: return 'mdi-file-outline';
    case NodeContentType.OPERATION: return 'mdi-cog-outline';
    case NodeContentType.CONDITIONAL: return 'mdi-multicast';
    default: return 'mdi-help-circle-outline';
  }
});

const chipLabel = computed(() => {
  switch (nodeType.value) {
    case NodeContentType.FILE: return t('designWorkflow.propertiesPanel.fileNode');
    case NodeContentType.OPERATION: return t('designWorkflow.propertiesPanel.operationNode');
    case NodeContentType.CONDITIONAL: return t('designWorkflow.propertiesPanel.conditionalNode');
    default: return 'Unknown Node';
  }
});


//---------------------------------EXPORT FLOW-----------------------------//
const exportFlow = async () => {
  if (!workFlow.value || !workFlowId.value) {
    showNotif('error', t('flowExportFailed') + ": " + "No workflow loaded yet");
    return;
  }
  try {
    const result = await repoWorkFlows.exportWorkFlow(workFlowId.value);
    const flowExportJson = WorkFlowExportJsonRequest.toRequest(result.get().getWorkFlow());

    if (result.isRight()) {
      const blob = new Blob([JSON.stringify(flowExportJson, null, 2)], { type: 'application/json' });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `${workFlow.value.getName().getName()}.json`;
      a.click();
      URL.revokeObjectURL(url);

      showNotif('success', t('export.success') + ": " + workFlow.value.getName().getName());

    } else {
      showNotif("error", t('export.error') + ": " + result.getLeft());
    }


  } catch (error) {
    showNotif('error', t('export.error') + ": " + error);
  }
}



//---------------------------------MESSAGE SNACKBAR-----------------------------//
const showNotif = inject('showNotif') as (color: string, message: string) => void


onMounted(async () => {
  // Workflow ID from route
  if (workflowIdFromRoute) {
    await fetchWorkFlow(new UllUUID(workflowIdFromRoute));
    await markAsInitialFromWorkFlow();
    isWorkFlowCreated.value = true;
    isWorkFlowSaved.value = true;

    // Not workflow ID in route
  } else {
    await fetchDefaultWorkflow();
    await markAsInitialFromWorkFlow();
    isWorkFlowCreated.value = false;
    isWorkFlowSaved.value = false;
  }

});
</script>



<style>
.vueflow-col {
  width: 100%;
  height: 100%;
  padding: 0 150px;
}

.v-tabs {
  position: relative;
}

.custom-tab-active {
  background-color: #1565c0 !important;
  color: white !important;
  border-top: 4px solid #ffca28;
  border-radius: 4px 4px 0 0;
  margin-top: -4px;
  transition: background-color 0.3s ease;
}

@media (max-width: 1200px) {
  .vueflow-col {
    padding: 0 50px;
  }
}

@media (max-width: 768px) {
  .vueflow-col {
    padding: 0 25px;
  }
}

@media (max-width: 480px) {
  .vueflow-col {
    padding: 0 10px;
  }
}
</style>
