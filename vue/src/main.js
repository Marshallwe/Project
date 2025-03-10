import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import locale from 'element-ui/lib/locale/lang/en'
import axios from "axios";
import VueRouter from 'vue-router';
import router from './router';
import store from './store';


Vue.use(ElementUI, {
    locale,
    size: 'small'
})


Vue.prototype.$axios = axios
Vue.prototype.$httpUrl = 'http://localhost:8090'

Vue.use(VueRouter)

Vue.config.productionTip = false

new Vue({
    router,
    store,
    render: h => h(App),
}).$mount('#app')