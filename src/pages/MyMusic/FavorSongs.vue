<template>
  <album-content :song-list="collectList"></album-content>
</template>

<script>
import AlbumContent from "../../components/AlbumContent.vue";
import {mapGetters} from "vuex";
import {getCollectOfUserId, songOfSongId} from "../../api";
export default {
  components: {AlbumContent},
  data() {
    return {
      collection: [] ,     // 收藏的歌曲列表
      collectList : [],     // 收藏的歌曲列表（带歌曲详情）
    }
  },
  computed: {
    ...mapGetters([
      'userId'          // 当前登录用户id
    ]),
  },
  mounted() {
    this.getCollection(this.userId);
  },
  methods: {
    // 获取我的收藏列表
    getCollection(userId){
      getCollectOfUserId(userId)
        .then(res => {
          this.collection = res;
          // 通过歌曲id获取歌曲信息
          for(let item of this.collection){
            if(item.songId !== null){
              this.getSongsOfId(item.songId);
            }
          }
        })
        .catch(err => {
          console.log(err);
        })
    },
    //通过歌曲id获取歌曲信息
    getSongsOfId(id){
      songOfSongId(id)
        .then(res => {
          this.collectList.push(res);
        })
        .catch(err => {
          console.log(err);
        })
    }
  }
}
</script>

<style scoped lang = "scss">

</style>
