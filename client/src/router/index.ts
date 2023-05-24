import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/Home.vue'

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'homeView',
      component: HomeView
    },
    {
      path: '/dragons',
      name: 'dragons',
      component: () => import(/* webpackChunkName: "view" */ '@/views/Dragons/View.vue')
    },
    {
      path: '/dragons/create',
      name: 'dragonsCreate',
      component: () => import(/* webpackChunkName: "create" */ '@/views/Dragons/Create.vue')
    },
    {
      path: '/dragons/edit/:name',
      name: 'dragonsEdit',
      component: () => import(/* webpackChunkName: "edit" */ '@/views/Dragons/Edit.vue')
    },
  ]
})

export default router
