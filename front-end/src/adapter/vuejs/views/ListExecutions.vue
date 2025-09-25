<template>
    <v-container class="list-execution-container fill-height bg-background">
        <h1 class="text-h4 text-primary ma-5">
            {{ t('executions.listExecutionsTitle') }}
        </h1>

        <v-row>
            <v-col cols="12" style="max-height: 700px; overflow-y: auto;">
                <v-table dense fixed-header class="mt-2 bg-background" style="min-width: 900px;">
                    <thead>
                        <tr>
                            <th class="text-center text-secondary bg-background">{{ t('executions.executionId') }}</th>
                            <th class="text-center text-secondary bg-background">{{ t('executions.workflowName') }}</th>
                            <th class="text-center text-secondary bg-background">{{ t('executions.status') }}</th>
                            <th class="text-center text-secondary bg-background">{{ t('executions.creationDate') }}</th>
                            <th class="text-center text-secondary bg-background">{{ t('executions.totalTime')
                            }}</th>
                            <th class="text-center text-secondary bg-background">{{
                                t('executions.operationCount') }}</th>
                            <th class="text-center text-secondary bg-background">{{ t('executions.actions') }}</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="execution in executions" :key="execution.getId().toString()">
                            <td class="text-center">{{ execution.getId().getValue() }}</td>
                            <td class="text-center">{{ execution.getWorkFlowName().getName() }}</td>
                            <td class="text-center">
                                <v-avatar size="28" :color="getStatusColor(execution.getStatus())">
                                    <v-icon size="18" color="white"
                                        :class="{ 'mdi-spin': execution.getStatus() === 'EXECUTING' }">
                                        {{ getStatusIcon(execution.getStatus()) }}
                                    </v-icon>
                                </v-avatar>
                            </td>
                            <td class="text-center">{{ formatDate(execution.getCreationDate()) }}</td>
                            <td class="text-center">{{ (execution.getTotalExecutionTimeMillis() / 1000).toFixed(2) }} s
                            </td>
                            <td class="text-center">{{ execution.getNumberOfOperationExecuted() }}</td>
                            <td>
                                <div class="d-flex flex-wrap justify-center align-center">
                                    <router-link :to="`/executions/${execution.getId().getValue()}`">
                                        <v-btn size="small" color="primary" class="ma-2 text-white">
                                            {{ t('executions.details') }}
                                            </v-btn>
                                        </router-link>
                                    <v-btn size="small" color="error" class="ma-2 text-white"
                                        @click="onDelete(execution.getId())">
                                        {{ t('executions.deleteExecutions') }}
                                    </v-btn>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </v-table>
            </v-col>
        </v-row>


    </v-container>
</template>


<script setup lang="ts">
import { onMounted, ref, inject, onUnmounted } from 'vue';
import { ExecutionHttpRepository } from '@/adapter/http/repository/execution-http-repository';
import { Execution } from '@/domain/entity/execution';
import { useI18n } from 'vue-i18n';
import type { UllUUID } from '@ull-tfg/ull-tfg-typescript';
import { computed } from '@vue/reactivity';

const { t } = useI18n();
const repoExecutions = new ExecutionHttpRepository();
const executions = ref<Execution[]>([]);
const executionResult = ref<Execution | null>(null);

const showNotif = inject('showNotif') as (color: string, message: string) => void;

const fetchExecutions = async () => {
    const result = await repoExecutions.getExecutions();
    if (result.isRight()) {
        // sort by creation date descending
        executions.value = result.get().sort((a, b) =>
            new Date(b.getCreationDate()).getTime() - new Date(a.getCreationDate()).getTime()
        );
    } else {
        showNotif('error', t('executions.fetchError') + ': ' + result.getLeft());
    }
};


const onDelete = async (id: UllUUID) => {
    const result = await repoExecutions.deleteExecution(id);
    if (result.isRight()) {
        showNotif('success', t('executions.deleteSuccess'));
        fetchExecutions();
    } else {
        showNotif('error', t('executions.deleteError') + ': ' + result.getLeft());
    }
};


// FORMATTERS
const formatDate = (date: Date | string): string => {
    const d = new Date(date);
    if (isNaN(d.getTime())) return 'Invalid date';
    return d.toLocaleString('es-ES', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false,
    });
};



// STATUS
const getStatusColor = (status: string): string => {
    switch (status) {
        case 'SUCCESS':
            return 'success';
        case 'FAILED':
        case 'BAD_URI_REQUEST':
            return 'error';
        case 'EXECUTING':
            return 'secondary';
        default:
            return 'primary';
    }
};

const getStatusIcon = (status: string): string => {
    switch (status) {
        case 'SUCCESS':
            return 'mdi-check';
        case 'FAILED':
        case 'BAD_URI_REQUEST':
            return 'mdi-close';
        case 'EXECUTING':
            return 'mdi-loading';
        default:
            return 'mdi-alert-circle';
    }
};



// TABLE HEADERS
const headers = [
    { key: 'id', title: t('ID'), align: 'center' },
    { key: 'name', title: t('Name'), align: 'center' },
    { key: 'status', title: t('Status'), align: 'center' },
    { key: 'creationDate', title: t('Creation Date'), align: 'center' },
    { key: 'totalTime', title: t('Total Time'), align: 'center' },
    { key: 'operations', title: t('Operations'), align: 'center' },
    { key: 'actions', title: t('Actions'), align: 'center', sortable: false }
] as const;



// ON MOUNTED
let intervalId: number | undefined;
onMounted(() => {
    fetchExecutions();
    intervalId = window.setInterval(() => {
        fetchExecutions();
    }, 500); // 500ms refresh list
});

onUnmounted(() => {
    if (intervalId) {
        clearInterval(intervalId);
    }
});
</script>


<style scoped>
.list-execution-container {
    display: flex;
    justify-content: center;
    flex-direction: column;
}
</style>
