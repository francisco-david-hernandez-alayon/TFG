<template>
    <div>
        <!-- DIALOG BUTTON -->
        <v-tooltip :text="t('conditionals.addConditional')" location="bottom">
            <template #activator="{ props }">
                <v-btn v-bind="props" icon color="primary" variant="tonal" @click="dialog = true">
                    <v-icon>mdi-source-branch</v-icon>
                </v-btn>
            </template>
        </v-tooltip>

        <!-- DIALOG -->
        <v-dialog v-model="dialog" max-width="600px">
            <v-card>
                <v-card-title class="text-h6 text-center text-secondary font-weight-bold mt-4">
                    {{ t('conditionals.addConditional') }}
                </v-card-title>

                <v-card-text>
                    <v-text-field v-model="conditionalName" :label="t('conditionals.conditionalName')" outlined dense
                        clearable />
                    <v-textarea v-model="executionCode" :label="t('conditionals.executionCode')" outlined dense
                        clearable rows="4" />

                    <div class="text-caption text-grey mt-n3 mb-3">
                        {{ t('conditionals.optionalLinksHint') }}
                    </div>

                    <v-text-field v-model="nodeName" :label="t('conditionals.nodeName')" outlined dense clearable />
                </v-card-text>

                <v-card-actions>
                    <v-spacer />
                    <v-btn text @click="dialog = false">{{ t('close') }}</v-btn>
                    <v-btn color="primary"
                        :disabled="!conditionalName.trim() || !executionCode.trim() || !nodeName.trim()"
                        @click="submitConditional">
                        {{ t('conditionals.addConditional') }}
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { Conditional } from '@/domain/value-object/conditional';
import { ConditionalName } from '@/domain/value-object/conditional-name';
import { NodeName } from '@/domain/value-object/node-name';

const { t } = useI18n();

const dialog = ref(false);
const conditionalName = ref('');
const executionCode = ref('');
const nodeName = ref('');

const emit = defineEmits<{
    (e: 'conditional-added', payload: { conditional: Conditional; name: NodeName }): void;
}>();

const submitConditional = () => {
    try {
        const name = new ConditionalName(conditionalName.value.trim());
        const code = executionCode.value.trim();
        const node = new NodeName(nodeName.value.trim());

        // Empty default conditions map
        const linksConditions = new Map<NodeName, boolean>();

        const conditional = new Conditional(name, code, linksConditions);

        emit('conditional-added', { conditional, name: node });
        resetDialog();
    } catch (error) {
        console.error(t('conditionals.errorAddingConditional') + ": " + error);
    }
};

const resetDialog = () => {
    conditionalName.value = '';
    executionCode.value = '';
    nodeName.value = '';
    dialog.value = false;
};
</script>
