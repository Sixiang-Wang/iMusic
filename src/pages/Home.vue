<template>
  <div class="home">
    <swiper/>
    <div class="section" v-for="(item,index) in songsList" :key="index">
      <div class="section-title">{{ item.name }}</div>
      <content-list :contentList="item.list"></content-list>
    </div>
  </div>
</template>

<script>
import Swiper from '../components/Swiper.vue'
import {getAllSinger, getAllSongList, getTopSong, preLogin} from '../api/index'
import ContentList from '../components/ContentList'
import {mixin} from '../mixins'
import {mapGetters} from "vuex";

export default {
  name: 'home',
  components: {
    ContentList,
    Swiper
  },
  data() {
    return {
      songsList: [
        {name: '热 歌 榜', list: []},
        {name: '精 选 歌 单', list: []},
        {name: '人 气 歌 手', list: []},
      ]
    }
  },
  computed: {
    ...mapGetters([
      'loginIn',
    ])
  },
  created() {
    this.preLogin()
    this.getSongListOfTen()
    this.getSingerOfTen()
    this.getSongOfTen()
  },
  methods: {
    preLogin() {
      if(!this.loginIn){
        preLogin().then((res => {
          if(res.code===1){
            this.$store.commit('setLoginIn' , true);
            this.$store.commit('setUserId' , res.userId);
            this.$store.commit('setUsername', res.username);
            this.$store.commit('setAvatar', res.avatar);
            this.$notify({
              title: '自动登陆成功'
            })
          }
        }))
      }
    },
    getSongOfTen() { // 获取前十首歌曲
      getTopSong().then((res) => {
        this.songsList[0].list = res.slice(0, 10)
      }).catch((err) => {
        console.log(err)
      })
    },
    getSongListOfTen() { // 获取前十条歌单
      getAllSongList().then((res) => {
        this.songsList[1].list = res.slice(0, 10)
      }).catch((err) => {
        console.log(err)
      })
    },
    getSingerOfTen() { // 获取前十名歌手
      getAllSinger().then((res) => {
        this.songsList[2].list = res.slice(0, 10)
      }).catch((err) => {
        console.log(err)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/home.scss';
</style>
