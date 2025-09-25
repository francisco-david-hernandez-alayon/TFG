<template>
  <v-container fluid class="home-container pa-0 fill-height d-flex align-center justify-center">
    <v-card class="carousel-card" elevation="4">
      <v-carousel v-model="currentSlide" :interval="8000" hide-delimiters height="600" show-arrows cycle>

        <!-- Slide 1 -->
        <v-carousel-item>
          <div class="slide-content">
            <h1 class="text-h4 font-weight-bold text-primary mb-4">{{ t('home.slide1.title') }}</h1>
            <p class="text-body-1 text-secondary mb-3">{{ t('home.slide1.text1') }}</p>
            <p class="text-body-1 text-secondary mb-4">{{ t('home.slide1.text2') }}</p>

            <!-- Mini Vue Flow -->
            <div style="position: relative; width: 100%; height: 100%;" class="flow-wrapper mb-4">
              <VueFlow v-model:nodes="nodes" v-model:edges="edges" fit-view-on-init :min-zoom="0.5" :max-zoom="3"
                class="vue-flow-container">

                <Background :gap="64" pattern-color="#ccc" :line-width="1" variant="lines" />

              </VueFlow>
            </div>

            <v-btn color="primary" variant="outlined" to="/design-workflow" size="large">
              {{ t('home.slide1.button') }}
            </v-btn>
          </div>
        </v-carousel-item>


        <!-- Slide 2 -->
        <v-carousel-item>
          <div class="slide-content">

            <h1 class="text-h4 font-weight-bold text-primary mb-4">{{ t('home.slide2.title') }}</h1>
            <p class="text-body-1 text-secondary mb-4">{{ t('home.slide2.text') }}</p>
            <v-row dense justify="center" class="slide-2-row slide-content">
              <v-col cols="12" md="5">
                <v-card class="usage-card" outlined>
                  <v-card-title class="text-h6">{{ t('home.slide2.operations') }}</v-card-title>
                  <v-card-text>{{ t('home.slide2.operationsText') }}</v-card-text>
                </v-card>
              </v-col>
              <v-col cols="12" md="5">
                <v-card class="usage-card" outlined>
                  <v-card-title class="text-h6">{{ t('home.slide2.flows') }}</v-card-title>
                  <v-card-text>{{ t('home.slide2.flowsText') }}</v-card-text>
                </v-card>
              </v-col>
            </v-row>
          </div>
        </v-carousel-item>


        <!-- Slide 3 -->
        <v-carousel-item>
          <div class="slide-content">
            <h1 class="text-h4 font-weight-bold text-primary mb-6">{{ t('home.slide3.title') }}</h1>
            <v-row dense justify="center" class="tool-grid">
              <v-col v-for="tool in tools" :key="tool.name" cols="6" md="3">
                <v-card class="tool-card d-flex flex-column align-center pa-4" outlined>
                  <img :src="tool.image" height="60" class="mb-3" contain />

                  <div class="font-weight-medium mb-2">{{ tool.name }}</div>
                  <v-btn :href="tool.link" target="_blank" variant="outlined" color="primary" size="small">
                    {{ t('home.slide3.visit') }}
                  </v-btn>
                </v-card>
              </v-col>
            </v-row>
          </div>
        </v-carousel-item>

        <!-- Slide 4 -->
        <v-carousel-item>
          <div class="slide-content">
            <h1 class="text-h4 font-weight-bold text-primary mb-4">{{ t('home.slide4.title') }}</h1>
            <p class="text-body-1 text-secondary mb-3">{{ t('home.slide4.p1') }}</p>
            <p class="text-body-1 text-secondary mb-3">{{ t('home.slide4.p2') }}</p>
            <p class="text-body-1 text-secondary mb-3">{{ t('home.slide4.p3') }}</p>
          </div>
        </v-carousel-item>
      </v-carousel>

      <!-- Dots -->
      <div class="dot-indicators mt-4 d-flex justify-center">
        <v-btn v-for="(n, i) in 4" :key="i" icon size="x-small" class="mx-1"
          :color="currentSlide === i ? 'primary' : 'grey'" @click="currentSlide = i">
          <v-icon size="10">mdi-circle</v-icon>
        </v-btn>
      </div>
    </v-card>
  </v-container>
</template>


<script setup lang="ts">
/* these are necessary styles for vue flow */
import '@vue-flow/core/dist/style.css';
import '@vue-flow/core/dist/theme-default.css';

import { ref } from 'vue'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()
const currentSlide = ref(0)

const tools = [
  {
    name: 'Vue.js',
    image: 'https://help.wnpower.com/hc/article_attachments/24443330766477',
    link: 'https://vuejs.org/',
  },
  {
    name: 'Vuetify',
    image: 'https://miro.medium.com/v2/resize:fit:960/1*nXbnY0Hk8OFwQGTVkruy0g.png',
    link: 'https://vuetifyjs.com/',
  },
  {
    name: 'VueFlow',
    image: 'https://vueflow.dev/favicons/android-chrome-512x512.png',
    link: 'https://vueflow.dev/',
  },
  {
    name: 'TypeScript',
    image: 'https://miro.medium.com/v2/resize:fit:1358/1*moJeTvW97yShLB7URRj5Kg.png',
    link: 'https://www.typescriptlang.org/',
  },
  {
    name: 'Java',
    image: 'https://www.oracle.com/img/tech/cb88-java-logo-001.jpg',
    link: 'https://www.java.com/es/',
  },
  {
    name: 'MongoDB',
    image: 'https://www.gtech.com.tr/wp-content/uploads/2020/09/mongodb-nedir-1.png',
    link: 'https://www.mongodb.com/',
  },
]


// Vue FLow setup
import { VueFlow, useVueFlow, SetViewport } from '@vue-flow/core'
import { Background } from '@vue-flow/background';

const nodes = ref([
  { id: '1', label: 'Node 1', position: { x: 0, y: 0 } },
  { id: '2', label: 'Node 2', position: { x: 50, y: 200 } },
])

const edges = ref([{ id: 'e1-2', source: '1', target: '2' }])

</script>

<style scoped>
.home-container {
  background-color: #f5f9ff;
}

.carousel-card {
  max-width: 1100px;
  width: 100%;
  padding: 32px;
  border-radius: 20px;
  background-color: #ffffff;
}

.slide-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
  padding: 24px;
  max-width: 900px;
  margin: 0 auto;
  text-align: center;
}

.slide-2-row {
  width: 100%;
  max-width: 900px;
}


.usage-card {
  background-color: #eef3ff;
  border-radius: 12px;
  min-height: 150px;
}

.tool-card {
  background-color: #f9fbff;
  border-radius: 12px;
  transition: transform 0.2s;
}

.tool-card:hover {
  transform: scale(1.05);
}

.tool-grid {
  gap: 16px;
}


.flow-wrapper {
  height: 300px;
  max-width: 600px;
  width: 100%;
  margin: 0 auto;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.05);
}

.vue-flow-container {
  height: 100%;
  width: 100%;
  background: white;
  border: 2px dashed #ccc;
  border-radius: 8px;
}
</style>
