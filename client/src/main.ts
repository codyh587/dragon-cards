import './assets/main.css'
import axios from 'axios'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

axios.defaults.baseURL = process.env.VUE_APP_API_ENDPOINT

createApp(App).use(store).use(router).mount('#app')
