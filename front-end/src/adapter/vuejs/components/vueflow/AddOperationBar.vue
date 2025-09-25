<template>
  <div>
    <!-- DIALOG BUTTON -->
    <v-tooltip :text="t('operations.addOperation')" location="bottom">
      <template #activator="{ props }">
        <v-btn v-bind="props" icon color="primary" variant="tonal" @click="dialog = true">
          <v-icon>mdi-plus-box</v-icon>
        </v-btn>
      </template>
    </v-tooltip>


    <!-- DIALOG -->
    <v-dialog v-model="dialog" max-width="600px">
      <v-card>
        <!-- TITLE -->
        <v-card-title class="text-h6 text-center text-secondary font-weight-bold mt-4">
          {{ t('operations.selectOperations') }}
        </v-card-title>

        <!-- OPERATIONS LIST -->
        <v-card-text>
          <v-list>
            <v-list-item v-for="operation in operations" :key="operation.getName().getName()"
              @click="selectOperation(operation as Operation)"
              :class="{ 'bg-grey-lighten-4': selectedOperation?.getName().getName() === operation.getName().getName() }"
              class="rounded-lg mb-2">
              <v-list-item-title class="font-weight-medium">
                {{ operation.getName().getName() }}
              </v-list-item-title>
              <v-list-item-subtitle>
                {{ operation.getDockerImage?.() || 'N/A' }}
              </v-list-item-subtitle>
            </v-list-item>
          </v-list>

          <!-- WORKFLOW NAME INPUT -->
          <div v-if="selectedOperation" class="mt-4">
            <v-text-field v-model="workflowName" :label="t('operations.enterNodeName')"
              :placeholder="t('operations.workNodeNamePlaceholder')" outlined dense clearable />
          </div>
        </v-card-text>

        <!-- ACTIONS -->
        <v-card-actions>
          <v-spacer />
          <v-btn text @click="dialog = false">{{ t('close') }}</v-btn>
          <v-btn color="primary" :disabled="!selectedOperation || !workflowName.trim()" @click="submitOperation">
            {{ t('operations.addOperation') }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import { OperationHttpRepository } from '../../../http/repository/operation-http-repository';
import { Operation } from '../../../../domain/value-object/operation';
import { NodeName } from '@/domain/value-object/node-name';

const { t } = useI18n();

const operations = ref<Operation[]>([]);
const selectedOperation = ref<Operation | null>(null);
const workflowName = ref('');
const dialog = ref(false);

const emit = defineEmits<{
  (e: 'operation-added', payload: { operation: Operation; name: NodeName }): void;
}>();

const repo = new OperationHttpRepository();

const fetchOperations = async () => {
  const result = await repo.getOperations();
  if (result.isRight()) {
    operations.value = result.get();
  } else {
    console.error(t('operations.errorFetchingOperations'), result.getLeft());
  }
};

const selectOperation = (operation: Operation) => {
  selectedOperation.value = operation;
};

const submitOperation = () => {
  try {
    const nodeName = new NodeName(workflowName.value);

    if (selectedOperation.value && workflowName.value.trim()) {
      emit('operation-added', {
        operation: selectedOperation.value as Operation,
        name: nodeName
      });
      resetDialog();
    }

  } catch (error) {
    console.error(t('operations.errorAddingOperation') + ": " + error);
  }

};

const resetDialog = () => {
  dialog.value = false;
  selectedOperation.value = null;
  workflowName.value = '';
};

onMounted(() => {
  fetchOperations();
});
</script>

<style scoped>
.v-list-item {
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.v-list-item:hover {
  background-color: #f0f0f0;
}
</style>
