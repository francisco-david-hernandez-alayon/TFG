<template>
  <v-app>
    <Header app @toggle-sidebar="isSidebarVisible = !isSidebarVisible" />

    <SideBar v-if="isSidebarVisible" app />

    <!-- GLOBAL NOTIFICATION -->
    <NotificationSnackbar
      ref="notif"
      :color="notifColor"
      :message="notifMessage"
      :timeout="3000"
      @shown="onShown"
      @hidden="onHidden"
    />

    <v-main app color="primary">
      <router-view />
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import { ref, provide } from 'vue'
import Header from '@/adapter/vuejs/components/layout/Header.vue'
import SideBar from '@/adapter/vuejs/components/layout/SideBar.vue'
import NotificationSnackbar from '@/adapter/vuejs/components/layout/NotificationSnackbar.vue'


// SHOW NOTIFICATION
const notif = ref();
const notifColor = ref('success');
const notifMessage = ref('');
const showNotif = (color: string, message: string) => {
  notifColor.value = color
  notifMessage.value = message
  notif.value?.show()
}

provide('showNotif', showNotif); // All app have access to this function

const onShown = () => {}
const onHidden = () => {}

const isSidebarVisible = ref(true)
</script>
