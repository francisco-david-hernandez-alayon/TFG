<template>
  <v-container class="list-operations-container fill-height bg-background">
    <h1 class="text-h4 text-primary ma-5">
      {{ t('operations.listOperationsTitle') }}
    </h1>

    <v-row>
      <v-table dense fixed-header class="mt-2 bg-background">
        <thead>
          <tr>
            <th class="text-center text-secondary bg-background">{{ t('operations.operationName') }}</th>
            <th class="text-center text-secondary bg-background">{{ t('operations.dockerImage') }}</th>
            <th class="text-center text-secondary bg-background">{{ t('operations.operationActions') }}</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="op in operations" :key="op.getName().getName()">
            <td class="text-center">{{ op.getName().getName() }}</td>
            <td class="text-center">{{ op.getDockerImage().toString() }}</td>
            <td>
              <div class="d-flex justify-center align-center flex-wrap">
                <v-btn size="small" color="primary" class="ma-2 text-white" @click="openEditOperation(op as Operation)">
                  {{ t('operations.editOperation') }}
                </v-btn>
                <v-btn size="small" color="error" class="ma-2 text-white"
                  @click="deleteOperation(op.getName().getName())">
                  {{ t('operations.deleteOperation') }}
                </v-btn>

              </div>
            </td>
          </tr>
        </tbody>
      </v-table>
    </v-row>

    <!-- CREATE BUTTON -->
    <v-row class="justify-center mt-4">
      <v-btn color="primary" :to="'/operations/create'">
        {{ t('operations.createOperation') }}
      </v-btn>
    </v-row>


    <!-- EDIT OPERATION DIALOG -->
    <v-dialog v-model="editDialog" width="600" persistent>
      <v-card>
        <v-card-title class="text-h5 text-secondary text-center">
          {{ t('operations.editOperation') }}
        </v-card-title>

        <v-card-text>
          <v-text-field v-model="updatedName" :label="t('operations.operationName')" class="mb-4" />
          <v-text-field v-model="updatedDockerImage" :label="t('operations.dockerImage')" class="mb-4" />
        </v-card-text>

        <v-card-actions class="justify-end">
          <v-btn variant="text" @click="editDialog = false">{{ t('operations.operationCancel') }}</v-btn>
          <v-btn color="primary" @click="UpdatedOperation">{{ t('operations.operationSave') }}</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

  </v-container>
</template>

<script setup lang="ts">
import { ref, onMounted, inject } from 'vue';
import { useI18n } from 'vue-i18n';
import { Operation } from '@/domain/value-object/operation';
import { OperationHttpRepository } from '@/adapter/http/repository/operation-http-repository';
import { OperationName } from '@/domain/value-object/operation-name';
import { UllDockerImageName } from '@ull-tfg/ull-tfg-typescript/dist/ull-docker-image-name';

const { t } = useI18n();
const repoOperations = new OperationHttpRepository();
const operations = ref<Operation[]>([]);

// FETCH OPERATIONS
const fetchOperations = async () => {
  const result = await repoOperations.getOperations();
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

// DELETE OPERATIONS
const deleteOperation = async (operationName: string) => {
  try {
    const result = await repoOperations.deleteOperation(new OperationName(operationName));
    if (result.isRight()) {
      showNotif('success', t('operations.operationDeleted'));
      fetchOperations();

    } else {
      showNotif('error', t('operations.messageOperationDeleteUnexpectedError'));
    }

  } catch (error) {
    showNotif('error', t('operations.errorDeletingOperation') + ": " + error);
    return;
  }

};


// UPDATE OPERATION
const editDialog = ref(false);
const selectedOperation = ref<Operation | null>(null);
const updatedName = ref('');
const updatedDockerImage = ref('');

const openEditOperation = (operation: Operation) => {
  selectedOperation.value = operation;
  updatedName.value = operation.getName().getName();
  updatedDockerImage.value = operation.getDockerImage().toString();
  editDialog.value = true;
};

const UpdatedOperation = async () => {
  try {
    if (!selectedOperation.value) return;

    // If the name has changed, check for duplicates
    if (updatedName.value !== selectedOperation.value.getName().getName()) {
      await fetchOperations();
      if (isDuplicateOperationName(operations.value as Operation[], updatedName.value)) {
        showNotif("error", t('operations.duplicateOperationName'));
        return;
      }

    }

    const newOp = new Operation(
      new OperationName(updatedName.value),
      new UllDockerImageName(updatedDockerImage.value),
    );

    const result = await repoOperations.updateOperation(
      selectedOperation.value.getName(),
      newOp
    );

    if (result.isRight()) {
      showNotif('success', t('operations.operationUpdated'));
      editDialog.value = false;
      fetchOperations();
    } else {
      showNotif('error', t('operations.errorUpdatingOperation') + ": " + result.getLeft());
    }

  } catch (error) {
    showNotif('error', t('operations.errorUpdatingOperation') + ": " + error);
  }
};

onMounted(fetchOperations);

// NOTIFICATIONS
const showNotif = inject('showNotif') as (color: string, message: string) => void;
</script>

<style scoped>
.list-operations-container {
  display: flex;
  flex-direction: column;
}
</style>
