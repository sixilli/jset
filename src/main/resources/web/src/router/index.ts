import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ClipsView from '../views/ClipsView.vue'
import SitesView from '../views/SitesView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/clips',
      name: 'clips',
      component: ClipsView,
    },
    {
      path: '/sites',
      name: 'sites',
      component: SitesView,
    },
  ],
})

export default router
