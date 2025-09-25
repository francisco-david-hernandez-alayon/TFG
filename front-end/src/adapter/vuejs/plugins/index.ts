// Plugins
import vuetify from './vuetify'
import 'vuetify/styles'

// Types
import type { App } from 'vue'

export function registerPlugins (app: App) {
  app.use(vuetify)
}