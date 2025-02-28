import {createRouter, createWebHashHistory} from 'vue-router'

const routes = [
    {
        path: '',
        component: () => import('@/views/login/login.vue')
    },
    {
        path: '/login',
        component: () => import('@/views/login/login.vue')
    },
    {
        path: '/main_top',
        component: () => import('@/views/main/top.vue')
    },
    {
        path: '/main_left',
        component: () => import('@/views/main/left.vue')
    },
    {
        path: '/menu',
        component: () => import('@/views/system/Menu.vue')
    },
    {
        path: '/role',
        component: () => import('@/views/system/Role.vue')
    },
    {
        path: '/user',
        component: () => import('@/views/system/User.vue')
    },
    {
        path: '/ai/silicon_flow',
        component: () => import('@/views/ai/SiliconFlow_NoOverflow.vue')
    },
    {
        path: '/404',
        component: () => import('@/views/system/404.vue')
    },
    {
        path: '/500',
        component: () => import('@/views/system/500.vue')
    }
]

const router = createRouter({
    // history: createWebHistory(process.env.BASE_URL),
    history: createWebHashHistory(process.env.BASE_URL),
    routes
})

export default router