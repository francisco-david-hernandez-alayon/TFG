<template>
    <v-container class="list-workflow-container fill-height bg-background">

        <h1 class="text-h4 text-primary ma-5">
            {{ t('workflows.listWorkflowsTitle') }}
        </h1>

        <v-row>
            <v-table dense fixed-header class="mt-2 bg-background">
                <thead>
                    <tr>
                        <th class="text-center text-secondary bg-background">{{ t('workflows.workflowId') }}</th>
                        <th class="text-center text-secondary bg-background">{{ t('workflows.workFlowName') }}</th>
                        <th class="text-center text-secondary bg-background">{{ t('workflows.operationCount') }}</th>
                        <th class="text-center text-secondary bg-background">{{ t('workflows.workFlowCreationDate') }}
                        </th>
                        <th class="text-center text-secondary bg-background">{{ t('workflows.workflowEnabled') }}</th>
                        <th class="text-center text-secondary bg-background">{{ t('workflows.workflowActions') }}</th>

                    </tr>
                </thead>


                <tbody>
                    <tr v-for="workflow in workflows" :key="workflow.getId().toString()">
                        <td class="text-center">{{ workflow.getId().getValue() }}</td>
                        <td class="text-center">{{ workflow.getName().getName() }}</td>
                        <td class="text-center">{{ workflow.getNodes().length }}</td>
                        <td class="text-center">{{ formatDate(workflow.getCreationDate()) }}</td>
                        <td class="text-center">
                            <div class="d-flex justify-center align-center">
                                <v-switch :model-value="workflow.isEnabled()"
                                    @update:model-value="toggleEnabled(workflow as WorkFlow)" color="primary"
                                    hide-details inset density="compact" />
                            </div>
                        </td>

                        <td>
                            <div class="d-flex flex-wrap justify-center align-center">

                                <ExecuteWorkflowDialog :workflow="workflow as WorkFlow"
                                    :enabled="workflow.isEnabled()" />

                                <v-btn size="small" color="primary" class="ma-2 text-white"
                                    :to="`/workflows/${workflow.getId().getValue()}/design`">
                                    {{ t('workflows.editWorkFlow') }}
                                </v-btn>

                                <v-btn size="small" color="secondary" class="ma-2 text-white"
                                    @click="exportWorkFlow(workflow as WorkFlow)">
                                    {{ t('workflows.exportWorkFlow') }}
                                </v-btn>

                                <v-btn size="small" color="error" class="ma-2 text-white"
                                    @click="deleteFlow(workflow.getId().toString())">
                                    {{ t('workflows.deleteWorkFlow') }}
                                </v-btn>
                            </div>
                        </td>

                    </tr>
                </tbody>
            </v-table>
        </v-row>

        <!-- WORKFLOW BUTTONS -->
        <v-row class="d-flex align-center justify-start mt-4" dense>
            <v-col cols="auto">
                <v-btn color="primary" :to="'/workflows/design'">
                    {{ t('workflows.createFlow') }}
                </v-btn>
            </v-col>
        </v-row>

    </v-container>
</template>


<script setup lang="ts">
import ExecuteWorkflowDialog from '@/adapter/vuejs/components/layout/ExecuteWorkflowDialog.vue';
import { ref, onMounted } from 'vue';
import { UllUUID } from '@ull-tfg/ull-tfg-typescript';
import { useI18n } from 'vue-i18n';
import { inject } from 'vue';
import { WorkFlowHttpRepository } from '@/adapter/http/repository/workflow-http-repository';
import { WorkFlowExportJsonRequest } from '@/adapter/http/request/workflow-export-json-request';
import { WorkFlow } from '@/domain/entity/workflow';

const { t } = useI18n();

const repoWorkFlows = new WorkFlowHttpRepository();
const workflows = ref<WorkFlow[]>([]);


// FETCH FLOWS
const fetchFlows = async () => {
    const result = await repoWorkFlows.getWorkFlows();
    if (result.isRight()) {
        workflows.value = result.get();

    } else {
        showNotif("error", t('errorFetchingFlows') + result.getLeft());
    }
};

// DELETE FLOWS
const deleteFlow = async (id: string) => {
    const result = await repoWorkFlows.deleteWorkFlow(new UllUUID(id));
    if (result.isRight()) {
        workflows.value = workflows.value.filter((f) => f.getId().toString() !== id);
        showNotif("success", t('flowDeleted'));

    } else {
        showNotif("error", t('flowDeleteFailed') + ": " + result.getLeft());
    }
};

// UPDATE FLOWS
const toggleEnabled = async (workflow: WorkFlow) => {
    let updatedWorkflow: WorkFlow;

    if (workflow.isEnabled()) {
        updatedWorkflow = workflow.disactivate();
    } else {
        updatedWorkflow = workflow.activate();
    }

    try {
        const result = await repoWorkFlows.updateWorkFlow(updatedWorkflow.getId(), updatedWorkflow);

        if (result.isRight()) {
            workflows.value = workflows.value.map(f =>
                f.getId().toString() === updatedWorkflow.getId().toString() ? updatedWorkflow : f
            );

        } else {
            showNotif('error', t('workflows.statusUpdateFailed') + ": " + result.getLeft());
        }
    } catch (error) {
        showNotif('error', t('workflows.statusUpdateFailed') + ": " + error);
    }
};


// EXPORT workflow
const exportWorkFlow = async (workFlow: WorkFlow) => {
    try {
        const result = await repoWorkFlows.exportWorkFlow(workFlow.getId());
        const flowExportJson = WorkFlowExportJsonRequest.toRequest(result.get().getWorkFlow());

        if (result.isRight()) {
            const blob = new Blob([JSON.stringify(flowExportJson, null, 2)], { type: 'application/json' });
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `${workFlow.getName().getName()}.json`;
            a.click();
            URL.revokeObjectURL(url);

            showNotif('success', t('export.success') + ": " + workFlow.getName().getName());

        } else {
            showNotif("error", t('export.error') + ": " + result.getLeft());
        }


    } catch (error) {
        showNotif('error', t('export.error') + ": " + error);
    }
}


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


// MESSAGE NOTIFICATION
const showNotif = inject('showNotif') as (color: string, message: string) => void

onMounted(() => {
    fetchFlows();
});

</script>

<style>
.list-workflow-container {
    display: flex;
    justify-content: center;
    flex-direction: column;
}
</style>
