<template>
  <div class="box">
    <div class="the-aside">
      <div class="img">
        <img style="width: 100%" :src="picUrl" alt="音乐图片"/>
      </div>
      <div class="show">
        <h3>
          {{ "歌手:  " }}<span>{{ artist }}</span>
        </h3>
        <h3 style="margin-top: 20px">
          {{ "所属专辑:  " }}<span>暂无专辑</span>
        </h3>
      </div>
    </div>
    <div class="content">
      <div class="lyric-title">
        <h1>{{ title }}</h1>
        <h3 style="font-weight: normal; color: #fff">
          {{ artist + "-" + title }}
        </h3>
      </div>
      <div class="song-lyric" ref="lyr">
        <ul class="has-lyric" v-if="lyr.length">
          <li v-for="(item, index) in lyr" :key="index">
            {{ item[1] }}
          </li>
        </ul>
        <ul class="no-lyric" v-if="lyr.length === 0">
          <li>暂无歌词</li>
        </ul>
      </div>
      <div class="comment">
        <song-comment :play-id="id" :type="0"></song-comment>
      </div>
    </div>
  </div>
</template>


<script>
import {mixin} from '../mixins'
import {mapGetters} from 'vuex'
// import Comment from "../components/Comment";
// import SongComment from "../components/SongComment";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Lyric",
  // eslint-disable-next-line vue/no-unused-components
  // components: { SongComment, Comment },
  mixins: [mixin],
  data() {
    return {
      lyr: [],
      manualScrolling: false, // 标志跟踪手动滚动
      scrollTimer: null, // 定时器变量
    };
  },
  computed: {
    ...mapGetters([
      "id",
      "url",
      "curTime",
      "lyric",
      "listIndex",
      "listOfSongs",
      "title",
      "artist",
      "picUrl",
    ]),
  },
  created() {
    this.lyr = this.lyric;
    this.$store.commit("setCurTime", 0);
  },
  watch: {
    url() {
      this.$store.commit("setCurTime", 0);
      this.lyr = this.parseLyric(this.listOfSongs[this.listIndex].lyric);
    },
    curTime(val) {
      if (val === 0 || !this.manualScrolling) {
        this.scrollToCurrentLine(val);
      }
    },
    // 当手动滚动时，设置标志并清除定时器
    manualScrolling(newVal) {
      if (newVal) {
        clearTimeout(this.scrollTimer);
        this.scrollTimer = setTimeout(() => {
          this.manualScrolling = false;
        }, 2000);
      }
    },
  },
  methods: {
    scrollToCurrentLine(currentTime) {
      if (this.lyr.length > 0) {
        for (let i = 0; i < this.lyr.length; i++) {
          if (currentTime >= this.lyr[i][0]) {
            let top = this.$refs.lyr.children[0].children[i].offsetTop;
            let halfViewHeight = 200;
            let lyricHeight = 40;

            // 重置所有歌词颜色和样式
            for (let j = 0; j < this.lyr.length; j++) {
              document.querySelectorAll(".has-lyric li")[j].style.color = "#fff";
              document.querySelectorAll(".has-lyric li")[j].style.fontSize = "16px";
              document.querySelectorAll(".has-lyric li")[j].style.fontWeight = "normal";
              document.querySelectorAll(".has-lyric li")[j].style.opacity = "0.5";
            }

            if (i >= 0) {
              document.querySelectorAll(".has-lyric li")[i].style.color = "#31c27c";
              document.querySelectorAll(".has-lyric li")[i].style.fontSize = "20px";
              document.querySelectorAll(".has-lyric li")[i].style.fontWeight = "bold";
              document.querySelectorAll(".has-lyric li")[i].style.opacity = "1";

              if (top > halfViewHeight + lyricHeight) {
                this.$refs.lyr.scrollTop = top - halfViewHeight;
              }
            }
          }
        }
      }
    }
  },
  // 在mounted生命周期钩子中添加监听滚动事件
  mounted() {
    this.$refs.lyr.addEventListener('scroll', () => {
      this.manualScrolling = true;
    });
  },
};
</script>

<style scoped lang="scss">
@import "../assets/css/lyric";
</style>
