import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      component: resolve => require(['../pages/Login.vue'], resolve)
    },

    {
      path: '/Home',
      component: resolve => require(['../components/Home.vue'], resolve),
      children: [
        {
          path: '/Info',
          component: resolve => require(['../pages/InfoPage.vue'], resolve)
        },
        {
          path: '/User',
          component: resolve => require(['../pages/UserPage.vue'], resolve)
        },
        {
          path: '/Singer',
          component: resolve => require(['../pages/SingerPage.vue'], resolve)
        },
        {
          path: '/SongList',
          component: resolve => require(['../pages/SongListPage.vue'], resolve)
        },
        {
          path: '/Song',
          component: resolve => require(['../pages/SongPage.vue'], resolve)
        },
        {
          path: '/AllSong',
          component: resolve => require(['../pages/AllSongPage.vue'], resolve)
        },
        {
          path: '/ListSong',
          component: resolve => require(['../pages/ListSongPage.vue'], resolve)
        },
        {
          path: '/Collect',
          component: resolve => require(['../pages/CollectPage.vue'], resolve)
        },
        {
          path: '/Comment',
          component: resolve => require(['../pages/CommentPage.vue'], resolve)
        },
        {
          path: '/Follow',
          component: resolve => require(['../pages/FollowPage.vue'], resolve)
        },
        {
          path: '/Recovery',
          component: resolve => require(['../pages/Recovery.vue'], resolve)
        }

      ]
    }

  ]
})
