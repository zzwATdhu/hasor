import Vue from 'vue'
import App from './App.vue'
import router from './router'
import splitPane from 'vue-splitpane'
import VueClipboard from 'vue-clipboard2'
import codemirror from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/public.css'

Vue.config.productionTip = false;
Vue.use(ElementUI);
Vue.use(codemirror);
Vue.component('SplitPane', splitPane);
Vue.use(VueClipboard);
new Vue({
    router,
    render: h => h(App)
}).$mount('#app');
