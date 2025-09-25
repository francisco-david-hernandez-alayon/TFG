import { createRouter, createWebHistory } from 'vue-router';
import HomePage from '../views/HomePage.vue';
import ListWorkflows from '../views/ListWorkflows.vue';
import DesignWorkflow from '../views/DesignWorkflow.vue';
import CreateOperations from '../views/CreateOperations.vue';
import ListOperations from '../views/ListOperations.vue';
import ImportWorkflow from '../views/ImportWorkFlow.vue';
import ListExecutions from '../views/ListExecutions.vue';
import ExecutionDetails from '../views/ExecutionDetails.vue';


const routes = [
    { path: '/', name: 'Home', component: HomePage },
    { path: '/workflows', name: 'ListWorkflows', component: ListWorkflows },
    { path: '/operations', name: 'ListOperations', component: ListOperations },
    { path: '/executions', name: 'ListExecutions', component: ListExecutions },
    { path: '/executions/:id', name: 'ExecutionDetails', component: ExecutionDetails },
    { path: '/operations/create', name: 'CreateOperations', component: CreateOperations },
    { path: '/workflows/import', name: 'ImportWorkflow', component: ImportWorkflow },
    { path: '/workflows/:id?/design', name: 'DesignWorkFlow', component: DesignWorkflow },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router;


