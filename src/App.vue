<template>
  <div id="app">
    <the-header />
    <BoneDemo />
    <router-view class="music-content"/>
    <song-audio />
    <the-aside />
    <play-bar />
    <scroll-top></scroll-top>
    <the-footer />
  </div>
</template>

<script>

import TheHeader from "./components/TheHeader.vue";
import BoneDemo  from "./components/BoneDemo.vue";
import ScrollTop from "./components/ScrollTop.vue";
import TheFooter from "./components/TheFooter.vue";
import SongAudio from "./components/SongAudio.vue";
import PlayBar from "./components/PlayBar.vue";
import TheAside from "./components/TheAside.vue";
export default {
  name: 'App',
  components: {
    TheAside,
    PlayBar,
    TheFooter,
    ScrollTop,
    TheHeader,
    SongAudio,
    BoneDemo
  }
}
</script>

<style  lang="scss" scoped>
@import './assets/css/app.scss';
</style>
