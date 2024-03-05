//使用的el的消息提醒组件

import {ElMessage} from "element-plus";

export const messageTips = (message, type)=>{
    ElMessage({
        showClose: true,
        message: message,
        type: type,
    })
}

export function getTokenName() {
    return "Authorization";
}

export function removeToken() {
    window.sessionStorage.removeItem(getTokenName());
    window.localStorage.removeItem(getTokenName());
}