import { createApp } from 'vue'
import router from "./router/router.js";
import App from "./App.vue";
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import {doGet} from "./http/httpRequest.js";
const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.directive("hasPermission",  (el, binding) => {
    // 这会在 `mounted` 和 `updated` 时都调用
    doGet("/api/login/info", {}).then(resp => {
        let user = resp.data.data;
        let permissionList = user.permissionList;

        let flag = false;

        for (let key in permissionList) {
            if (permissionList[key] === binding.value) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            //没有权限，把页面元素隐藏掉
            //el.style.display = 'none';
            //把没有权限的按钮dom元素删除
            el.parentNode && el.parentNode.removeChild(el)
        }
    })
})

app.use(router).use(ElementPlus,{locale:zhCn}).mount('#app')
