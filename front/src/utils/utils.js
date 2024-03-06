//使用的el的消息提醒组件

import {ElMessage, ElMessageBox} from "element-plus";

export const messageTip = (message, type)=>{
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

export function messageConfirm(msg) {
    return ElMessageBox.confirm(
        msg, //提示语
        '系统提醒', //提示的标题
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
}