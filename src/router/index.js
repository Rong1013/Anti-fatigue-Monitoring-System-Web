import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/login',
    children: [{
      path: 'home',
      name: 'Home',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'dashboard' }
    }]
  },

  {
    path: '/user',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'UserManagement',
        component: () => import('@/views/user/index'),
        meta: { title: '用户管理', icon: 'el-icon-user' }
      }
    ]
  },

  {
    path: '/device',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'DeviceManagement',
        component: () => import('@/views/device/index'),
        meta: { title: '设备管理', icon: 'el-icon-monitor' }
      }
    ]
  },

  {
    path: '/product',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'ProductManagement',
        component: () => import('@/views/product/index'),
        meta: { title: '产品管理', icon: 'el-icon-box' }
      }
    ]
  },

  {
    path: '/digital-product',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'DigitalProduct',
        component: () => import('@/views/digital-product/index'),
        meta: { title: '数字产品', icon: 'el-icon-s-cooperation' }
      }
    ]
  },

  {
    path: '/task',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'TaskManagement',
        component: () => import('@/views/task/index'),
        meta: { title: '任务管理', icon: 'el-icon-check' }
      }
    ]
  },

  {
    path: '/permission',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'PermissionManagement',
        component: () => import('@/views/permission/index'),
        meta: { title: '权限管理', icon: 'el-icon-key' }
      }
    ]
  },

  {
    path: '/alarm',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'AlarmManagement',
        component: () => import('@/views/alarm/index'),
        meta: { title: '告警管理', icon: 'el-icon-bell' }
      }
    ]
  },

  {
    path: '/visualization',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'DataVisualization',
        component: () => import('@/views/visualization/index'),
        meta: { title: '数据可视化', icon: 'el-icon-pie-chart' }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
