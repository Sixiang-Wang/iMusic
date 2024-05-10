<template>
  <div class = "song-list-album">
    <div class = "album-slide">
      <div class = "album-img">
        <img :src = 'attachImageUrl(tempList.pic)' alt="">
      </div>
      <div class="album-info">
        <h2>简 介:</h2>
        <span>{{tempList.introduction}}</span>
      </div>
    </div>
    <div class="album-content">
      <div class="album-title">
        <p>{{tempList.title}}</p>
      </div>
      <div class="songs-body">
        <album-content :songList="listOfSongs">
          <template slot="title">歌 单</template>
        </album-content>
      </div>
    </div>
  </div>
</template>

<script>
import {mixin} from "../mixins";
import {mapGetters} from "vuex";
import {listSongDetail,songOfSongId} from "../api";
import AlbumContent from "../components/AlbumContent.vue";

export default {
  name: 'song-list-album',
  components: {AlbumContent},
  mixins: [mixin],
  data() {
    return {
      songList: [],
      songListId: '',
    }
  },
  computed: {
    ...mapGetters(
      [
        "listOfSongs",  //当前播放列表
        "tempList",     //当前歌单
      ]
    ),
  },
  created() {
    this.songListId = this.$route.params.id;
    this.getSongList();
  },
  methods: {
    //获取当前歌单的歌曲列表
    getSongList(){
      listSongDetail(this.songListId).then(res => {
        for (let item of res)
        {
          songOfSongId(item.songId).then(res2 => {
            this.songList.push(res2);
          })
        }
        this.$store.commit('setListOfSongs',this.songList);
      }).catch(error =>{
        console.log(error);
      })
    },

  }
}
</script>
<style lang = "scss" scoped>
@import "../assets/css/song-list-album.scss";
</style>
