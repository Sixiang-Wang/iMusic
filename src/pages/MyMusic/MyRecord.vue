<template>
  <div>
    <div class="album-menu">
      <ul>
        <li :class="{current: this.activeName === 'songs' }"
            @click="goPage('songs','/my-music/my-record/songs')">历史歌曲</li>
        <li :class="{current: this.activeName === 'songList' }"
            @click="goPage('songList','/my-music/my-record/songList')">歌手回顾</li>
      </ul>
    </div>
    <router-view></router-view>
  </div>
</template>


<script>

import AlbumContent from "../../components/AlbumContent.vue";
import {mapGetters} from "vuex";

export default {
  data() {
    return {
      activeName: 'init',
    }
  },
  computed: {
    ...mapGetters([
      'userId'          // 当前登录用户id
    ]),
  },
  created() {
    this.goPage('songs','/my-music/my-record/songs');
  },
  methods: {
    goPage(name,path){
      if(this.activeName === name) return;
      this.activeName = name;
      this.$router.push({path: path})
    },
  }
}
</script>

<style scoped lang = "scss">
@import "../../assets/css/my-record";
</style>
