import Vue from 'vue'
import VueRouter from 'vue-router'
import Index from '../views/Index.vue'

Vue.use(VueRouter);

const routes = [
    {
        path: '/**',
        name: 'Index',
        component: Index,
        meta:  {title: '远程链接终端'},
    },
    {
        path: '/index',
        name: 'Index',
        component: Index,
        meta:  {title: '远程链接终端'},
    },
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

const originalPush = VueRouter.prototype.push
  VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

export default router
