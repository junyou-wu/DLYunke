import { createRouter,createWebHistory} from 'vue-router'
let router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            component: ()=>import("../view/LoginView.vue")
        },
        {
            path: '/dashboard',
            component: ()=>import('../view/DashboardView.vue')
        }
    ]
})

export default router
