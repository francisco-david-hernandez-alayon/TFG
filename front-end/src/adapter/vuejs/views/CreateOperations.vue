<template>
    <v-container class="fill-height d-flex align-center justify-center bg-background">
        <v-card class="pa-10 bg-surface text-on-surface" style="width: 50%;">
            <v-card-title class="text-h4 pa-4 justify-center text-center text-primary">
                {{ t('operations.createOperation') }}
            </v-card-title>

            <v-card-text class="pt-6">
                <v-text-field v-model="newOperationName" :label="t('operations.operationName')" class="mb-4"
                    color="primary" variant="outlined" />
                <v-text-field v-model="newDockerImage" :label="t('operations.dockerImage')" class="mb-4" color="primary"
                    variant="outlined" />
            </v-card-text>

            <v-card-actions class="justify-center pt-4">
                <v-btn variant="tonal" color="primary" @click="resetFields">
                    {{ t('operations.resetFields') }}
                </v-btn>
                <v-btn color="primary" @click="createOperation">
                    {{ t('operations.createOperation') }}
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-container>
</template>


<script setup lang="ts">
import { ref, inject } from 'vue'
import { useI18n } from 'vue-i18n'
import { Operation } from '@/domain/value-object/operation'
import { OperationHttpRepository } from '@/adapter/http/repository/operation-http-repository'
import { OperationName } from '@/domain/value-object/operation-name'
import { UllDockerImageName } from '@ull-tfg/ull-tfg-typescript/dist/ull-docker-image-name'
import { NodeIcon } from '@/domain/enumerate/node-icon'

const { t } = useI18n()
const repo = new OperationHttpRepository();
const operations = ref<Operation[]>([]);

const newOperationName = ref('');
const newDockerImage = ref('');
const nodeIconOptions = Object.values(NodeIcon);


// FETCH OPERATIONS
const fetchOperations = async () => {
    const result = await repo.getOperations();
    if (result.isRight()) {
        operations.value = result.get();

    } else {
        showNotif('error', t('operations.errorFetchingOperations') + ": " + result.getLeft());
    }
};

const isDuplicateOperationName = (operations: Operation[], newName: string): boolean => {
    const nameToCheck = newName;
    return operations.some(op => {
        const existingName = op.getName().getName();
        return existingName === nameToCheck;
    });
};

// CREATE OPERATION
const createOperation = async () => {
    try {

        // Check for duplicate operation name
        await fetchOperations();
        if (isDuplicateOperationName(operations.value as Operation[], newOperationName.value)) {
            showNotif("error", t('operations.duplicateOperationName'));
            return;
        }

        const op = new Operation(
            new OperationName(newOperationName.value),
            new UllDockerImageName(newDockerImage.value),
        )


        const result = await repo.saveOperation(op)
        if (result.isRight()) {
            showNotif("success", t('operations.operationCreated'));
            resetFields();

        } else {
            showNotif("error", t('operations.operationCreateFailed') + ": " + result.getLeft());
        }

    } catch (error) {
        showNotif("error", t('operations.operationCreateFailed') + ": " + error)
    }
}

// RESET FIELDS
const resetFields = () => {
    newOperationName.value = '';
    newDockerImage.value = '';
}

// MESSAGE NOTIFICATION
const showNotif = inject('showNotif') as (color: string, message: string) => void
</script>
