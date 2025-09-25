import { createApp } from 'vue'
import { createI18n } from 'vue-i18n'

// Translate
import en from './adapter/vuejs/locales/en.json';
import es from './adapter/vuejs/locales/es.json';

// Vuetify
import 'vuetify/lib/styles/main.css'
import { aliases } from 'vuetify/iconsets/mdi';
import { createVuetify, type ThemeDefinition } from 'vuetify';
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

// Icons
import '@mdi/font/css/materialdesignicons.css';

// Components
import App from './App.vue'

// Router
import router from './adapter/vuejs/router/index';

//Vuetify-themes
const MainTheme: ThemeDefinition = {
  dark: false,
  colors: {
    background: '#F5FAFD',
    surface: '#FFFFFF',
    primary: '#1A73E8',
    'primary-darken-1': '#1558B0',
    secondary: '#4DD0E1',
    'secondary-darken-1': '#26C6DA',
    error: '#D32F2F',
    info: '#0288D1',
    success: '#43A047',
    warning: '#FB8C00',
  }
}


const SunsetTheme: ThemeDefinition = {
  dark: false,
  colors: {
    background: '#FFF8F0',
    surface: '#FFFFFF',
    primary: '#FF6F61',
    'primary-darken-1': '#C94C4C',
    secondary: '#FFB74D',
    'secondary-darken-1': '#FF9800',
    error: '#D32F2F',
    info: '#0288D1',
    success: '#388E3C',
    warning: '#F57C00',
  }
}

const ForestTheme: ThemeDefinition = {
  dark: false,
  colors: {
    background: '#F1F8F4',
    surface: '#FFFFFF',
    primary: '#388E3C',
    'primary-darken-1': '#2E7D32',
    secondary: '#A5D6A7',
    'secondary-darken-1': '#81C784',
    error: '#D32F2F',
    info: '#0288D1',
    success: '#43A047',
    warning: '#FBC02D',
  }
}

const GrapeTheme: ThemeDefinition = {
  dark: false,
  colors: {
    background: '#F8F5FB',
    surface: '#FFFFFF',
    primary: '#7B1FA2',
    'primary-darken-1': '#6A1B9A',
    secondary: '#8E24AA',
    'secondary-darken-1': '#BA68C8',
    error: '#C62828',
    info: '#5C6BC0',
    success: '#66BB6A',
    warning: '#FFA000',
  }
}




const vuetify = createVuetify({
  components,
  directives,
  icons: {
    defaultSet: 'mdi',
    aliases,
  },
  theme: {
    defaultTheme: 'MainTheme',
    themes: {
      MainTheme,
      SunsetTheme,
      ForestTheme,
      GrapeTheme,
    },
  },
})


const i18n = createI18n({
  legacy: false,
  locale: 'en',
  fallbackLocale: 'en',
  messages: {
    en,
    es,
  },
});


createApp(App)
  .use(vuetify)
  .use(router)
  .use(i18n)
  .mount('#app')