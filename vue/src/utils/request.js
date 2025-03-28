import axios from 'axios'
import ElementUI from "element-ui";
import router from '../router'

const request = axios.create({
    baseURL: 'http://localhost:8888',
    timeout: 5000
})


request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    let user = JSON.parse(localStorage.getItem("user"))
    if(user){
        config.headers['token'] = user.token;
    }
    return config
}, error => {
    return Promise.reject(error)
});


request.interceptors.response.use(
    response => {
        let res = response.data;
        if (response.config.responseType === 'blob') {
            return res
        }
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        if(res.code==='402'){
            ElementUI.MessageBox({
                title: 'error',
                message: res.msg
            }).then(() =>{
                router.push('/login')
            } )
        }
        return res;
    },
    error => {
        return Promise.reject(error)
    }
)


export default request

