import axios from 'axios'
import {getTokenName, messageConfirm, messageTip, removeToken} from "../utils/utils.js";
import {ElMessageBox} from "element-plus";

/*httpRequest文件，用于封装常用的http方法*/

axios.defaults.baseURL="http://127.0.0.1:8081";

export function doGet(url,params){
    return axios({
        method: 'get',
        url: url,
        params: params,
        dataType: 'json'
    })
}

export function doDelete(url,params){
    return axios({
        method: 'delete',
        url: url,
        params: params,
        dataType: 'json'
    })
}

export function doPost(url,data){
    return axios({
        method: 'post',
        url: url,
        data: data,
        dataType: 'json'
    })
}

export function doPut(url,data){
    return axios({
        method: 'get',
        url: url,
        data: data,
        dataType: 'json'
    })
}
//axios拦截器
axios.interceptors.request.use( (config) => {
    // 在请求头中放一个token（jwt），传给后端接口
    let token = window.sessionStorage.getItem(getTokenName())
    if (!token) { //前面加了一个！，表示token不存在，token是空的，token没有值，这个意思
        token = window.localStorage.getItem(getTokenName());
        if (token) {
            //设置一个请求头，方便后续token续期
            config.headers['rememberMe'] = true;
        }
    }
    if (token) { //表示token存在，token不是空的
        config.headers[getTokenName()] = token;
    }
    return config;
},  (error) => {
    return Promise.reject(error);
});


// 添加响应拦截器
axios.interceptors.response.use( (response) => {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么，拦截token验证的结果，进行相应的提示和页面跳转
    // var flag = (response.data.code > 900)
    // console.log(flag)
    if (response.data.code > 900) { //code大于900说明是token验证未通过
        ElMessageBox.confirm(
            "登录状态已失效，请重新登录", //提示语
            '系统提醒', //提示的标题
            {
                confirmButtonText: '确定',
                cancelButtonText: '退出',
                type: 'warning',
            }
        ).then(() => { //用户点击“确定”按钮就会触发then函数
            //后端验证token未通过,那没必要存储在浏览器中，直接删除
            removeToken();
            //跳到登录页
            window.location.href = "/";
        }).catch(() => { //无论如何都会跳转到登录界面
            window.location.href = "/";
        })
        return ;
    }
    return response;
}, function (error) {

    return Promise.reject(error);
});
