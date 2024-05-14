<template>
  <div class = "singer-album">
    <div class = "album-slide">
      <div class = "singer-img">
        <img :src = 'attachImageUrl(singer.pic)' alt = "">
      </div>
      <ul class = "info">
        <li>{{ singer.name }}</li>
        <li v-if="singer.sex === 0 || singer.sex === 1">
          {{ getSex(singer.sex) }}
        </li>
<!--        <li>生日: {{ getBirth(singer.birth) }}</li>-->
        <li>简介: {{ singer.introduction }}</li>
      </ul>
    </div>
    <div class="album-content">
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
import {songOfSingerId} from "../api";
import AlbumContent from "../components/AlbumContent.vue";
import singer from "./Singer.vue";

export default {
  name: 'singer-album',
  components: {AlbumContent},
  // components: {AlbumContent},
  mixins: [mixin],
  data() {
    return {
      singerId: '',
      singer: {},
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
  },
  created() {
    this.singerId = this.$route.params.id;
    this.singer = this.tempList;
    this.getSongList();
  },
  methods: {
    getSongList() {
      songOfSingerId(this.singerId).then(res =>
      {
        this.$store.commit('setListOfSongs', res);
      }).catch(error =>
      {
        console.log(error);
      })
    },
    getSex(value){
      if (value === 0)
      {
        return '女';
      }
      else if (value === 1)
      {
        return '男';
      }
      return '';
    }
  }

}
</script>

<style lang="scss" scoped>
@import "../assets/css/singer-album.scss";
</style>
