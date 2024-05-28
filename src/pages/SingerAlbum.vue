<template>
  <div class = "singer-album">
    <div class = "album-slide">
      <div class = "singer-img">
        <img v-if = "this.imageUrl" :src = 'this.imageUrl' alt = "">
        <img alt = "" v-else src = "../assets/img/singer.png"/>
      </div>
      <ul class = "info">
        <li>{{ singer.name }}</li>
        <li>{{ getSex(singer.sex) }}</li>
        <li v-if = "singer.birth != null">生日: {{ getBirth(singer.birth) }}</li>
        <li>简介: {{ singer.introduction }}</li>
      </ul>
    </div>

    <div class = "album-content">
      <div :class = "this.followClass" @click = "handleFollow()">
        <span>{{ this.isFollow }}</span>
      </div>

      <div class = "songs-body">
        <album-content :songList = "listOfSongs">
          <template slot = "title">歌 单</template>
        </album-content>
      </div>
    </div>
  </div>

</template>

<script>
import {mixin} from "../mixins";
import {mapGetters} from "vuex";
import {addFollow, deleteFollow, existFollow, getSingerById, songOfSingerId} from "../api";
import AlbumContent from "../components/AlbumContent.vue";

export default {
  name: 'singer-album',
  components: {AlbumContent},
  // components: {AlbumContent},
  mixins: [mixin],
  data() {
    return {
      singerId: '',
      singer: {},
      isFollow: '关注',
      imageUrl: null,
      pic: "",
    }
  },
  computed: {
    ...mapGetters(
      [
        'loginIn',
        'userId',
        'listOfSongs',
        'tempList', //当前歌手
      ]
    ),
    followClass() {
      return this.loginIn && this.isFollow === '关注' ? 'follow' : 'haveFollowed';
    },
  },
  created() {
    this.singerId = this.$route.params.id;
    getSingerById(this.singerId).then(res =>
    {
      this.singer = res;
      this.pic = this.singer.pic;
      this.initialize();
    })
    this.getSongList();
    existFollow(this.userId, this.singerId).then(res =>
    {
      if (res)
      {
        this.isFollow = '已关注';
      }
      else {
        this.isFollow = '关注';
      }
    })

  },
  methods: {
    handleFollow() {
      if (!this.loginIn)
      {
        this.notify('请先登录');
      }
      else if (this.isFollow === '关注')
      {
        let params = new URLSearchParams();
        params.append('userId', this.userId);
        params.append('singerId', this.singerId)
        addFollow(params);
        this.isFollow = '已关注';
        this.notify('关注成功', 'success');
      }
      else if (this.isFollow === '已关注')
      {
        deleteFollow(this.userId, this.singerId);
        this.isFollow = '关注';
        this.notify('取消关注成功', 'success');
      }
      else {
        this.notify('关注错误', 'error');
      }
    },
    getSongList() {
      songOfSingerId(this.singerId).then(res =>
      {
        this.$store.commit('setListOfSongs', res);
      }).catch(error =>
      {
        console.log(error);
      })
    },
    getSex(value) {
      if (value === 0)
      {
        return '女';
      }
      else if (value === 1)
      {
        return '男';
      }
      else if (value === 2) {
        return '组合';
      }
      return '';
    },

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
    },

  }

}
</script>

<style lang = "scss" scoped>
@import "../assets/css/singer-album.scss";
</style>
