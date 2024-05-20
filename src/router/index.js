import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/pages/Home'
import MyMusic from "@/pages/MyMusic";
import Singer from "@/pages/Singer";
import SongList from "@/pages/SongList";
import Search from "@/pages/Search"
import Lyric from "@/pages/Lyric";
import SingerAlbum from "@/pages/SingerAlbum.vue";
import SongListAlbum from "@/pages/SongListAlbum.vue";
import SignUp from "@/pages/SignUp";
import LoginIn from "@/pages/LoginIn";
import Setting from "@/pages/Setting";
import Personal from "@/pages/Personal.vue";
import MyFavor from "@/pages/MyFavor.vue";
import MySongList from "@/pages/MySongList.vue";
import MySongs from "@/pages/MySongs.vue";
import MyCare from "@/pages/MyCare.vue";
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/my-music',
      name: 'my-music',
      component: MyMusic,
      children: [
        {
          path: '',
          name: 'personal-information',
          component: Personal
        },
        {
          path: 'personal',
          name: 'personal-information',
          component: Personal
        },
        {
          path: 'my-favor',
          name: 'my-favor',
          component: MyFavor
        },
        {
          path: 'my-songList',
          name: 'my-songList',
          component: MySongList
        },
        {
          path: 'my-songs',
          name: 'my-songs',
          component: MySongs
        },
        {
          path: 'my-care',
          name: 'my-care',
          component: MyCare
        },
      ]
    },
    {
      path: '/singer',
      name: 'singer',
      component: Singer
    },
    {
      path: '/song-list',
      name: 'song-list',
      component: SongList
    },
    {
      path: '/search',
      name: 'search',
      component: Search
    },
    {
      path: '/lyric',
      name: 'lyric',
      component: Lyric
    },
    {
      path: '/sign-up',
      name: 'sign-up',
      component: SignUp
    },
    {
      path: '/login-in',
      name: 'login-in',
      component: LoginIn
    },
    {
      path: '/setting',
      name: 'setting',
      component: Setting
    },
    {
      path: '/singer-album/:id',
      name: 'singer-album',
      component: SingerAlbum
    },
    {
      path: '/song-list-album/:id',
      name: 'song-list-album',
      component: SongListAlbum
    }
  ],
  scrollBehavior (to, from, savedPosition) {
    return { x: 0, y: 0 }
  }
})
