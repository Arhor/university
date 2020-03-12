import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '@/views/home'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('@/views/about')
  },
  {
    path: '/faculties',
    name: 'Faculties',
    component: () => import('@/views/faculties')
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
