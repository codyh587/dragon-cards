import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import DragonView from '../views/Dragons/View.vue'
import DragonCreate from '../views/Dragons/Create.vue'
import DragonEdit from '../views/Dragons/Edit.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/dragons',
      name: 'dragons',
      component: DragonView
    },
    {
      path: '/dragons/create',
      name: 'dragonsCreate',
      component: DragonCreate
    },
    {
      path: '/dragons/edit/:name',
      name: 'dragonsEdit',
      component: DragonEdit
    },
  ]
})

export default router
