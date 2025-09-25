<template>
    <v-container class="fill-height pa-6">
        <!-- TITLE -->
        <v-row align="center">
            <v-col cols="8" class="text-center">
                <h1 class="text-h4 text-primary">
                    {{ t('executions.executionDetails') }}
                </h1>
            </v-col>
            <v-col cols="4" class="text-right">
                <v-btn color="primary" class="mt-2" @click="$router.back()">
                    <v-icon left>mdi-arrow-left</v-icon>
                    {{ t('back') }}
                </v-btn>
            </v-col>
        </v-row>


        <!-- DETAILS -->
        <v-row style="min-width: 70%;">
            <v-col cols="12">
                <v-card class="pa-6">
                    <v-card-text>

                        <v-row class="mb-6" justify="center" align="stretch">
                            <!-- Info principal -->
                            <v-col cols="12" md="4">
                                <v-card outlined class="pa-4 h-100">
                                    <div class="text-subtitle-1">
                                        <strong>{{ t('executions.workflowName') }}:</strong>
                                        {{ execution?.getWorkFlowName().getName() }}
                                    </div>
                                    <div class="text-subtitle-1">
                                        <strong>{{ t('executions.executionId') }}:</strong>
                                        {{ execution?.getId().getValue() }}
                                    </div>
                                    <div class="text-subtitle-1">
                                        <strong>{{ t('executions.creationDate') }}:</strong>
                                        {{ formatDate(execution?.getCreationDate() as Date) }}
                                    </div>
                                </v-card>
                            </v-col>

                            <v-col cols="12" md="4">
                                <v-card outlined class="pa-4 text-center h-100">
                                    <v-icon size="36" color="primary" class="mb-2">mdi-timer</v-icon>
                                    <div class="text-subtitle-1">{{ t('executions.totalTime') }}</div>
                                    <div class="text-h6 font-weight-bold">
                                        {{ formatSeconds(execution?.getTotalExecutionTimeMillis()) }}
                                    </div>
                                </v-card>
                            </v-col>

                            <v-col cols="12" md="4">
                                <v-card outlined class="pa-4 text-center h-100">
                                    <v-icon size="36" color="secondary" class="mb-2">mdi-cogs</v-icon>
                                    <div class="text-subtitle-1">{{ t('executions.operationCount') }}</div>
                                    <div class="text-h6 font-weight-bold">
                                        {{ execution?.getNumberOfOperationExecuted() }}
                                    </div>
                                </v-card>
                            </v-col>
                        </v-row>


                        <v-row class="mb-6" justify="center">
                            <v-col cols="12">
                                <v-alert :type="getStatusType(execution?.getStatus())" colored-border elevation="2"
                                    prominent outlined class="pa-4">
                                    <div class="text-subtitle-1 font-weight-bold">
                                        {{ t('executions.status') }}:
                                        <span>{{ execution?.getStatus() }}</span>
                                    </div>
                                    <div class="text-body-2">
                                        <strong>{{ t('executions.messageExecution') }}:</strong>
                                        {{ execution?.getMessage() }}
                                    </div>
                                </v-alert>
                            </v-col>
                        </v-row>

                        <v-row class="mb-4" justify="center">
                            <v-col cols="12" md="6">
                                <v-card outlined class="pa-4">
                                    <h3 class="text-subtitle-1 mb-2">{{ t('executions.outputFiles') }}</h3>
                                    <v-table dense fixed-header max-height="300">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>{{ t('files.fileName') }}</th>
                                                <th>{{ t('files.fileUri') }}</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr v-for="(file, idx) in execution?.getOutputFiles()" :key="idx">
                                                <td>{{ idx + 1 }}</td>
                                                <td>{{ file.getName().getName() }}</td>
                                                <td>{{ file.getUri() }}</td>
                                            </tr>
                                        </tbody>
                                    </v-table>
                                </v-card>
                            </v-col>

                            <v-col cols="12" md="6">
                                <v-card outlined class="pa-4">
                                    <h3 class="text-subtitle-1 mb-2">{{ t('executions.executedNodes') }}</h3>
                                    <v-table dense fixed-header max-height="300">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>{{ t('executions.executedNode') }}</th>
                                                <th>{{ t('executions.executionTimeMillis') }}</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr v-for="(node, idx) in execution?.getNodesExecuted()"
                                                :key="node.getName().getName()">
                                                <td>{{ idx + 1 }}</td>
                                                <td>{{ node.getName().getName() }}</td>
                                                <td>
                                                    <v-icon small color="primary" class="mr-1">mdi-timer-sand</v-icon>
                                                    {{ node.getNodeTimeExecutionMillis() }} ms
                                                </td>
                                            </tr>
                                        </tbody>
                                    </v-table>
                                </v-card>
                            </v-col>
                        </v-row>


                    </v-card-text>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>



<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { ExecutionHttpRepository } from '@/adapter/http/repository/execution-http-repository';
import { Execution } from '@/domain/entity/execution';
import { useI18n } from 'vue-i18n';
import { UllUUID } from '@ull-tfg/ull-tfg-typescript';

const { t } = useI18n();
const route = useRoute();
const repo = new ExecutionHttpRepository();
const execution = ref<Execution | null>(null);

const fetchExecution = async () => {
    const id = route.params.id;
    if (!id) return;
    const result = await repo.getExecution(new UllUUID(id as string));
    if (result.isRight()) {
        execution.value = result.get();
    } else {
        console.error('Error fetching execution:', result.getLeft());
    }
};

const formatSeconds = (ms: number | undefined): string => {
    if (!ms) return '-';
    return (ms / 1000).toFixed(2) + ' s';
};

const formatDate = (date: Date | string): string => {
    const d = new Date(date);
    if (isNaN(d.getTime())) return 'Fecha inválida';
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

const getStatusType = (status?: string) => {
    switch (status) {
        case 'SUCCESS': return 'success';
        case 'FAILED':
        case 'BAD_URI_REQUEST': return 'error';
        case 'EXECUTING': return 'warning';
        default: return 'info';
    }
};

onMounted(fetchExecution);
</script>
