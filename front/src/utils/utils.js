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

export function getToken() {
    let token = window.sessionStorage.getItem(getTokenName());
    if (!token) { //前面加了一个！，表示token不存在，token是空的，token没有值，这个意思
        token = window.localStorage.getItem(getTokenName());
    }
    if (token) { //表示token存在，token不是空的，token有值，这个意思
        return token;
    } else {
        messageConfirm("请求token为空，是否重新去登录？").then(() => { //用户点击“确定”按钮就会触发then函数
            //既然后端验证token未通过，那么前端的token肯定是有问题的，那没必要存储在浏览器中，直接删除一下
            removeToken();
            //跳到登录页
            window.location.href = "/";
        }).catch(() => { //用户点击“取消”按钮就会触发catch函数
            messageTip("取消去登录", "warning");
        })
    }
}