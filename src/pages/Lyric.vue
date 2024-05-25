<template>
  <div class="container" :style="{ '--bg-url': this.backgroundImage }">
    <div class="left-side">
      <img class="album-image" :src=this.picUrl alt="音乐封面">
      <div class="music-info">
        <h2>音乐：{{ this.title }}</h2>
        <p>歌手：{{ this.artist }}</p>
      </div>
    </div>

    <div class="right-side" ref="rightSide">
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
      backgroundImage: '', // 背景图片

      isManuallyScrolled: false, // 跟踪手动滚动状态
      scrollTimer: null, // 用于存储setTimeout的引用
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
    this.backgroundImage = this.picUrl
    // console.log(this.lyric)
  },

  methods: {

    handleManualScroll() {
      this.isManuallyScrolled = true;
      clearTimeout(this.scrollTimer); // 清除可能存在的旧定时器
      this.scrollTimer = setTimeout(() => {
        this.isManuallyScrolled = false; // 1.0秒后重置手动滚动状态
      }, 1000);
    },

    scrollLeftSideToHighlightedLyric() {
      if (!this.isManuallyScrolled) {
        const highlightedLyricElement = document.querySelector('.has-lyric .highlight');
        if (highlightedLyricElement) {
          const container = this.$refs.rightSide;
          const containerRect = container.getBoundingClientRect();
          const elementRect = highlightedLyricElement.getBoundingClientRect();

          // 计算容器可视区域的中间点
          const middlePoint = containerRect.height / 2;

          // 计算高亮歌词元素的中间点相对于容器顶部的位置
          const elementMiddle = elementRect.top + elementRect.height / 2 - containerRect.top;

          // 设置scrollTop使得高亮歌词居中
          container.scrollTop = container.scrollTop + (elementMiddle - middlePoint);

          // 确保滚动不会超出容器范围（可选，根据需求调整）
          const maxScrollTop = container.scrollHeight - containerRect.height;
          if (container.scrollTop > maxScrollTop) {
            container.scrollTop = maxScrollTop;
          }
        }
      }
    },
  },

  mounted() {
    // 绑定滚动事件监听器以检测手动滚动
    this.$refs.rightSide.addEventListener('scroll', this.handleManualScroll);
  },

  beforeDestroy() {
    // 在组件销毁前移除滚动事件监听器
    this.$refs.rightSide.removeEventListener('scroll', this.handleManualScroll);
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
              lyric.style.color = '#000000';
              lyric.style.fontSize = '15px';
              lyric.classList.remove('highlight'); // 移除高亮类
            });
            if (i >= 0) {
              const currentLyric = document.querySelectorAll('.has-lyric li')[i];
              if (currentLyric) {
                currentLyric.style.color = '#1e66d5';
                currentLyric.style.fontSize = '20px';
                currentLyric.style.border = true;
                currentLyric.classList.add('highlight');

                this.scrollLeftSideToHighlightedLyric();
              }
            }
          }
        }
      }
    },
  }


}
</script>

<style lang="scss" scoped>
@import "../assets/css/lyric.scss";
</style>
