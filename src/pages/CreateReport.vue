<template>
  <div class="weekly-report">
    <h1>我的创作周报</h1>
    <!--     http://localhost:8081/#/createReport-->
    <div class="most-played-song">
      <h2>用户播放量最多的歌曲</h2>
      <div v-if="mostPopularSong.title!==''" class="song-info">
        <p>歌曲名: {{ mostPopularSong.title }}</p>
        <img :src="mostPopularSong.picUrl" alt="歌曲封面">
        <p>播放量: {{ mostPopularSong.playCount }}</p>
      </div>
      <div>
        <p>暂时还没有歌曲哦~</p>
      </div>
    </div>

    <div class="most-favorited-playlist">
      <h2>用户被收藏最多的歌曲</h2>
      <p>{{ mostFavoriteSongTitle }}</p>
    </div>

    <div class="highest-rated-playlist">
      <h2>用户当前评分最高的歌单</h2>
<!--      //歌单为空-->
      <div v-if="highestRatedPlaylist.title===''">
        <p>该用户暂无歌单</p>
      </div>
      <div v-else>
        <p>{{highestRatedPlaylist.title}}</p>
        <div class="image-container">
          <img :src="highestRatedPlaylist.picUrl" class="rounded">
        </div>
      </div>
    </div>

    <div class="user-followers">
      <h2>用户粉丝数量</h2>
      <p>{{ userFollowersCount }}</p>
    </div>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import {mixin} from "../mixins";
import {getBestSongListOfUser, getFansCountByUserId, getPopularCollectedSongOfUser, getPopularSongOfUser} from "../api";
export default {
  name: 'CreateReport',
  mixins : [mixin],
  data() {
    return {
      mostPopularSong: {
        title: '',
        picUrl: '',
        playCount: 0
      },
      mostFavoriteSongTitle: '',
      highestRatedPlaylist: {
        title: '',
        picUrl : ''
      },
      userFollowersCount: '' // 用户粉丝数
    };
  },
  computed: {
    ...mapGetters([
      'userId'
    ])
  },
  created() {
    this.getMostPopularSong(this.userId);
    this.getMostFavoriteSong(this.userId);
    this.getHighestRatedPlaylist(this.userId);
    this.getUserFollowersCount(this.userId);
  },
  methods: {
    getMostPopularSong(userId){
      getPopularSongOfUser(userId)
        .then(res =>{
          if(res!==''){
            this.mostPopularSong.title = this.replaceFName(res.name);
            this.mostPopularSong.playCount = res.nums;
            this.mostPopularSong.picUrl = this.attachImageUrl(res.pic);
          }
        })
        .catch(error => {
          console.log("获取用户的最火歌曲失败");
        })

    },
    getMostFavoriteSong(userId){
      getPopularCollectedSongOfUser(userId)
        .then(res =>{
          // console.log(res);
          if(res === 0){
            this.mostFavoriteSongTitle = '暂时还没有被收藏的歌曲哦~'
          }
          else if(res === -1){
            this.mostFavoriteSongTitle = '暂时还没有被创作的歌曲哦~'
          }
          else{
            this.mostFavoriteSongTitle = this.replaceFName(res);
          }
        })
        .catch(error => {
          console.log("获取用户被收藏最多的歌曲失败"+error);
        })
    },
    getHighestRatedPlaylist(userId){
      getBestSongListOfUser(userId)
        .then(res =>{
          // console.log(res);
          if(res === 0){
            this.highestRatedPlaylist.title = '';
          }
          else{
            this.highestRatedPlaylist.title = res.title;
            this.highestRatedPlaylist.picUrl = this.attachImageUrl(res.pic);
          }
        })
        .catch(error => {
          console.log("获取用户被收藏最多的歌曲失败");
        })
    },
    getUserFollowersCount(userId) {
      getFansCountByUserId(userId)
        .then(res =>{
          if(res!==0)
            this.userFollowersCount = res;
          else{
            this.userFollowersCount = '暂时还没有粉丝哦~'
          }
        })
        .catch(error => {
          console.log("获取用户被收藏最多的歌曲失败");
        })
    }
  }
}
</script>

<style lang="css" scoped>
@import "../assets/css/createReport.css";
</style>
