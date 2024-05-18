<template>
  <div class = "song-list-album">
    <div class = "album-slide">
      <div class = "album-img">
        <img :src = 'attachImageUrl(tempList.pic)' alt = "">
      </div>
      <div class = "album-info">
        <h2>简 介:</h2>
        <span>{{ tempList.introduction }}</span>
      </div>
    </div>
    <div class = "album-content">

      <div class = "album-title">
        <p>{{ tempList.title }}</p>
      </div>

      <div class = "album-score">
        <div>
          <h3>评分</h3>
          <div>
            <el-rate v-model = "star" disabled></el-rate>
          </div>
        </div>
        <!--        <span>{{ average }}</span>-->
        <span>{{ average }}</span>
        <div>
          <h3>评价</h3>
          <div @click = "addRank()">
            <el-rate v-model = "rank" allow-half show-text ></el-rate>
          </div>
        </div>

      </div>

      <div class = "songs-body">
        <album-content :songList = "listOfSongs">
          <template slot = "title">歌 单</template>
        </album-content>
      </div>

      <template slot = "title">歌 单</template>
    </div>
  </div>
</template>

<script>
import {mixin} from "../mixins";
import {mapGetters} from "vuex";
import {listSongDetail, songOfSongId, commitRank, getRankOfSongListId} from "../api";
import AlbumContent from "../components/AlbumContent.vue";
import th from "element-ui/src/locale/lang/th";

export default {
  name: 'song-list-album',
  components: {AlbumContent},
  mixins: [mixin],
  data() {
    return {
      songList: [],
      songListId: '',
      average: 0,
      rank: 0,
    }
  },
  computed: {
    ...mapGetters(
      [
        "listOfSongs",  //当前歌单歌曲列表
        "tempList",     //当前歌单
        "loginIn",
        "username",
        "userId"
      ]
    ),
    star: this.average / 2,
  },
  created() {
    this.songListId = this.$route.params.id;
    this.getSongList();
    this.getRank(this.songListId);
  },
  methods: {
    //获取当前歌单的歌曲列表
    getSongList() {
      listSongDetail(this.songListId).then(res =>
      {
        for (let item of res)
        {
          songOfSongId(item.songId).then(res2 =>
          {
            this.songList.push(res2);
          })
        }
        this.$store.commit('setListOfSongs', this.songList);
      }).catch(error =>
      {
        console.log(error);
      })
    },
    //获取歌单评分
    getRank(id) {
      getRankOfSongListId(id).then(res =>
      {

        this.average = res;
      }).catch(error =>
      {
        console.log('error in get rank of songList\n' + error);
      })
    },
    addRank() {
      if (!this.loginIn)
      {
        this.notify('请先登录', 'r');
      }
      else {

        let params = new URLSearchParams();
        params.append('songListId',this.songListId);

        params.append('userId', this.userId);

        params.append('score', this.rank * 2);
        this.notify(1111)
        commitRank(params).then(res => {

          if (res.code === 1)
          {
            this.notify('评分成功','success');
            this.getRank(this.songListId);
          }
          else
          {
            this.notify(`评分失败+${res.msg}`,'error');
          }
        }).catch(error =>
        {
          console.log('error in commit rank\n' + error);
        })
      }
    }
  }
}
</script>
<style lang = "scss" scoped>
@import "../assets/css/song-list-album.scss";
</style>
