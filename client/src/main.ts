import './assets/main.css'
import axios from 'axios'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

axios.defaults.baseURL = import.meta.env.VITE_API_ENDPOINT

const app = createApp(App)

app.config.devtools = true

app.use(router)

app.use(store)

app.mount('#app')
