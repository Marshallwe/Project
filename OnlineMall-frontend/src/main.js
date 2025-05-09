import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import '@/resource/global.css'
import ElementUI from 'element-ui';
import locale from 'element-ui/lib/locale/lang/en';
import request from './utils/request';
import 'element-ui/lib/theme-chalk/index.css';

Vue.config.productionTip = false

Vue.use(ElementUI, {
  locale
});
Vue.prototype.request = request;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
