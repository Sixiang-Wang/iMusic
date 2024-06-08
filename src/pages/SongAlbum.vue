<template>
  <div class = "song-album">
    <div class = "album-slide">
      <div class = "singer-img">
        <img v-if = "this.imageUrl" :src = 'this.imageUrl' alt = "">
        <img alt = "" v-else src = "../assets/img/tubiao.jpg"/>
      </div>
    </div>

    <div class = "album-content">
      <div class = "album-title">
        <span>{{ replaceFName(song.name) }}</span>
      </div>
      <div class = "info">
        <ul>
          <li>歌手: {{ replaceLName(song.name) }}</li>
          <li>专辑: {{ song.introduction }}</li>
        </ul>
        <br>
        <ul>
          <li>风格: {{ song.style }}</li>
          <li>热度: {{ favor }}</li>
        </ul>
        <p style = "margin-top: 20px;font-size: 16px">发行时间: {{ this.createTime }}</p>
      </div>
      <div style="display: flex">
        <div class = "play-icon" @click = "play">
          <play-icon style = "margin: 3px 12px"></play-icon>
          <span style = "margin-top: 8px;">播放</span>
        </div>

        <div class = "collect" @click = "handleCollect()">
          <like-icon style="margin: 6px 10px" :class = "{'have-collected':this.isCollect==='已收藏'}">
          </like-icon>
          <span style = "margin-top: 7px;margin-left: 2px">{{ this.isCollect }}</span>
        </div>
      </div>

    </div>
    <div class = "lyric">
      <h2>歌词:</h2>
      <div class = "lyric-context" v-for = "(item,index) in this.lyric" :key = "index">
        <p>{{ item[1] }}</p>
      </div>
    </div>
    <comment :type = "0"></comment>

  </div>

</template>

<script>
import {mixin} from "../mixins";
import {mapGetters} from "vuex";
import {
  collectNumOfSong,
  deleteCollectSong,
  deleteCollectSongList,
  existCollectSong,
  setCollect,
  songOfSongId
} from "../api";
import AlbumContent from "../components/AlbumContent.vue";
import Comment from "../components/Comment.vue";
import PlayIcon from "../assets/icon/playIcon.vue";
import LikeIcon from "../assets/icon/likeIcon.vue";
import CollectIcon from "../assets/icon/collectIcon.vue";

export default {
  name: 'singer-album',
  components: {CollectIcon, LikeIcon, PlayIcon, AlbumContent, Comment},
  mixins: [mixin],
  data() {
    return {
      songId: '',
      song: '',
      imageUrl: null,
      pic: "",
      favor: '',
      lyric: [],
      isCollect: '收藏',
    }
  },
  computed: {
    ...mapGetters(
      [
        'loginIn',
        'userId',
        'curTime'
      ]
    ),
    createTime() {
      return this.song.createTime.slice(0, 10);
    }
  },
  created() {
    this.songId = this.$route.params.id;
    songOfSongId(this.songId).then(res =>
    {
      this.song = res;
      this.pic = this.song.pic;
      this.initialize();
    })
    collectNumOfSong(this.songId).then(res =>
    {
      this.favor = res
    })
    if (this.loginIn)
    {
      existCollectSong(this.userId, this.songId).then(res =>
      {
        if (res)
        {
          this.isCollect = '已收藏';
        }
      })
    }

  },
  methods: {
    async attachImageUrl(srcurl) {
      if (srcurl) {
        const imageUrl = this.$store.state.configure.HOST + srcurl;
        try {
          const response = await fetch(imageUrl, {method: 'HEAD'});
          if (response.ok) {
            this.imageUrl = imageUrl;
          }
          else {
            this.imageUrl = null; // 图片不存在，设置为null
          }
        } catch (_) {
          this.imageUrl = null; // 请求错误，设置为null
        }
      }
      else {
        this.imageUrl = null; // 未提供srcurl，设置为null
      }
    },
    async initialize() {
      await this.attachImageUrl(this.pic);
      this.lyric = this.parseLyric(this.song.lyric);
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

    play() {
      let song = this.song;
      this.toplay(song.id, song.url, song.pic, 0, song.name, song.lyric,[song]);
      this.$store.commit('setPlayList', [song]);
    },
    handleCollect() {
      if (!this.loginIn)
      {
        this.notify('请先登录');
      }
      else {
        let params = new URLSearchParams();
        params.append('userId', this.userId);
        params.append('type', 0);
        params.append('songId', this.songId);
        setCollect(params).then(res =>
        {
          if (res.code === 1)
          {
            this.isCollect = '已收藏';
            this.favor++;
            this.notify('收藏成功', 'success');
          }
          else if (res.code === 2)
          {
            deleteCollectSong(this.userId, this.songId);
            this.isCollect = '收藏';
            this.favor--;
            this.notify('取消成功收藏', 'success');
          }
          else {
            this.notify('收藏错误', 'error');
          }
        });
      }
    }
  }
}
</script>

<style lang = "scss" scoped>
@import "../assets/css/song-album.scss";
</style>
