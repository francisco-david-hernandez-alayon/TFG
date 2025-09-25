<template>
    <div>
        <v-btn size="small" color="success" class="ma-2 text-white" :disabled="!enabled" @click="openDialog">
            {{ t('workflows.runWorkFlow') }}
        </v-btn>

        <v-dialog v-model="showExecuteDialog" max-width="900px">
            <v-card style="position: relative;">
                <v-card-title class="text-h4 pa-5 text-center text-primary">
                    {{ t('workflows.executeWorkflowTitle') }}
                </v-card-title>

                <v-card-text style="max-height: 750px; overflow-y: auto;">
                    <v-card-title v-if="missingFiles.length === 0" class="text-center text-h6">
                        {{ t('workflows.noMissingUris') }}
                    </v-card-title>

                    <v-form v-else>
                        <v-row v-for="(item, index) in missingFiles" :key="item.node.getName().toString()"
                            class="mb-4" align="center" justify="center">
                            <v-col cols="12" sm="4" class="text-center">
                                <h3>{{ item.node.getName().getName() }}</h3>
                            </v-col>
                            <v-col cols="12" sm="8">
                                <v-text-field v-model="missingFiles[index].uri" :label="t('files.fileUri')" required />
                            </v-col>
                        </v-row>
                    </v-form>
                </v-card-text>

                <v-card-actions>
                    <v-spacer />
                    <v-btn text @click="showExecuteDialog = false">{{ t('cancel') }}</v-btn>
                    <v-btn color="secondary" @click="handleExecute" :disabled="!canExecute">
                        {{ t('workflows.executeWorkflow') }}
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useI18n } from 'vue-i18n';
import { inject } from 'vue';
import { Node as CustomNode } from '@/domain/value-object/node';
import { File } from '@/domain/value-object/file';
import { NodeName } from '@/domain/value-object/node-name';
import { Execution } from '@/domain/entity/execution';
import { WorkFlow } from '@/domain/entity/workflow';
import { WorkFlowHttpRepository } from '@/adapter/http/repository/workflow-http-repository';
import { NodeContentType } from '@/domain/enumerate/node-content-type';

const props = defineProps<{
    workflow: WorkFlow;
    enabled: boolean;
}>();

const { t } = useI18n();
const showNotif = inject('showNotif') as (color: string, message: string) => void;

const repoWorkFlows = new WorkFlowHttpRepository();
const showExecuteDialog = ref(false);
const missingFiles = ref<{ node: CustomNode; uri: string }[]>([]);

const canExecute = computed(() => {
    return missingFiles.value.every(item => item.uri.trim() !== '');
});

const openDialog = async () => {
    missingFiles.value = await findMissingFiles(props.workflow.getNodes());
    showExecuteDialog.value = true;
};

const handleExecute = async () => {
    showExecuteDialog.value = false;

    try {
        const missingMap = new Map<NodeName, string>();
        for (const { node, uri } of missingFiles.value) {
            missingMap.set(node.getName(), uri);
        }

        const result = await repoWorkFlows.executeWorkFlow(props.workflow.getId(), missingMap);

        if (result.isRight()) {
            showNotif('success', t('workflows.executed') + ': ' + props.workflow.getName().getName());
        } else {
            showNotif('error', t('workflows.executionError') + ': ' + result.getLeft());
        }
    } catch (error) {
        showNotif('error', t('unexpectedError') + ': ' + error);
    }
};

const findMissingFiles = async (nodes: readonly CustomNode[]): Promise<{ node: CustomNode; uri: string }[]> => {
    return nodes
        .filter(node => node.getType() === NodeContentType.FILE)
        .filter(node => {
            const content = node.getContent() as File;
            return !content.hasUri();
        })
        .map(node => ({ node, uri: '' }));
};
</script>
