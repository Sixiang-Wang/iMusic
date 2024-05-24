<template>
  <div class="container">
    <div class="left-side">
      <img class="album-image" :src=this.picUrl alt="音乐封面">
      <div class="music-info">
        <h2>音乐：{{ this.title }}</h2>
        <p>歌手：{{ this.artist }}</p>
      </div>
    </div>

    <div class="right-side">
      <h1 class="lyric-title">歌词</h1>
      <!--    有歌词-->
      <ul class="has-lyric" v-if="lyr.length" key="index">
        <li v-for="(item,index) in lyr" v-bind:key="index">
          {{ item[1] }}
        </li>
      </ul>
      <!--    无歌词-->
      <div v-else class="no-lyric" key="no-lyric">
        <span>暂无歌词</span>
      </div>
    </div>
  </div>
</template>


<script>
import {mixin} from '../mixins'
import {mapGetters} from 'vuex'

export default {
  name: 'lyric',
  mixins: [mixin],
  data() {
    return {
      lyr: [], // 当前歌曲的歌词
      lastScrollTime: null, // 记录最近一次手动滚动的时间
    }
  },
  computed: {
    ...mapGetters([
      'curTime', // 当前歌曲播放到的位置
      'id', //  当前播放的歌曲id
      'lyric', //  歌词
      'listIndex', // 当前歌曲在歌单中的位置
      'listOfSongs', // 当前歌曲列表
      'title', // 歌名
      'artist', // 歌手名
      'picUrl', // 正在播放的音乐的图片
    ])
  },
  created() {
    this.lyr = this.lyric
    // console.log(this.lyric)
  },

  watch: {
    id: function () {
      this.lyr = this.parseLyric(this.listOfSongs[this.listIndex].lyric)
    },
    curTime: function () {
      if (this.lyr.length > 0) {
        for (let i = 0; i < this.lyr.length; i++) {
          if (this.curTime >= this.lyr[i][0]) {
            const lyrics = document.querySelectorAll('.has-lyric li');
            lyrics.forEach((lyric, index) => {
              lyric.style.color = '#000';
              lyric.style.fontSize = '15px';
              lyric.classList.remove('highlight'); // 移除高亮类
            });
            if (i >= 0) {
              const currentLyric = document.querySelectorAll('.has-lyric li')[i];
              if (currentLyric) {
                currentLyric.style.color = '#1e66d5';
                currentLyric.style.fontSize = '25px';
                currentLyric.style.border = true;
                currentLyric.classList.add('highlight');

              }
            }
          }
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../assets/css/lyric.scss";
</style>
