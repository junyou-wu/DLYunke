//使用的el的消息提醒组件

export const messageTips = (message,type)=>{
    ElMessage({
        showClose: true,
        message: message,
        type: type,
    })
}