<template>
  <div class = "singer-album">
    <div class = "album-slide">
      <div class = "singer-img">
        <img v-if="this.imageUrl" :src = 'this.imageUrl' alt = "">
        <img alt="" v-else  src="../assets/img/tubiao.jpg" />
      </div>
      <ul class = "info">
        <li>歌手: </li>
        <li>{{ replaceLName(song.name) }}</li>
        <li>专辑: </li>
        <li>{{ song.introduction }}</li>
      </ul>
    </div>

    <div class = "album-content">
      <div class = "album-title">
        <p>{{ replaceFName(song.name) }}</p>
      </div>

      <div class = "songs-body">
        <album-content :songList = "[song]">
          <template slot = "title">播 放</template>
        </album-content>
      </div>
    </div>
  </div>

</template>

<script>
import {mixin} from "../mixins";
import {mapGetters} from "vuex";
import {songOfSongId} from "../api";
import AlbumContent from "../components/AlbumContent.vue";

export default {
  name: 'singer-album',
  components: {AlbumContent},
  mixins: [mixin],
  data() {
    return {
      songId: '',
      song: '',
      imageUrl: null,
      pic:"",
    }
  },
  computed: {
    ...mapGetters(
      [
        'loginIn',
        'userId',
      ]
    ),
  },
  created() {
    this.songId = this.$route.params.id;
    songOfSongId(this.songId).then(res =>
    {
      this.song = res;
      this.pic=this.song.pic;
      this.initialize();
    })
  },
  methods: {
    async attachImageUrl(srcurl) {
      if (srcurl) {
        const imageUrl = this.$store.state.configure.HOST + srcurl;
        try {
          const response = await fetch(imageUrl, { method: 'HEAD' });
          if (response.ok) {
            this.imageUrl = imageUrl;
          } else {
            this.imageUrl = null; // 图片不存在，设置为null
          }
        } catch (_) {
          this.imageUrl = null; // 请求错误，设置为null
        }
      } else {
        this.imageUrl = null; // 未提供srcurl，设置为null
      }
    },
    async initialize() {
      await this.attachImageUrl(this.pic);
    },
    // 获取名字前半部分 -- 歌手名
    replaceLName(str) {
      let arr = str.split('-')
      return arr[0]
    },
    // 获取名字后半部分-- 歌名
    replaceFName(str) {
      let arr = str.split('-')
      return arr[1]
    },
  }

}
</script>

<style lang = "scss" scoped>
@import "../assets/css/song-album.scss";
</style>
