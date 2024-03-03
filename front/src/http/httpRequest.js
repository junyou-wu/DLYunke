import axios from 'axios'

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