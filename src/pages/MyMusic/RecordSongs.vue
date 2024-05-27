<template>
  <album-content :song-list="recentList">dd</album-content>

</template>

<script>
import AlbumContent from "../../components/AlbumContent.vue";
import {mapGetters} from "vuex";
import { getRecentSongByUserId } from "../../api/index";

export default {
  components: {AlbumContent},
  data() {
    return {
      recentList : [],     // 收藏的歌曲列表（带歌曲详情）
    }
  },
  computed: {
    ...mapGetters([
      'userId'          // 当前登录用户id
    ]),
  },
  created() {
    this.getCollection(this.userId);
  },
  methods: {
    getCollection(userId) {
      getRecentSongByUserId(userId).then(res => {
        console.log("res:",res.data);
        this.recentList = res.data;
      }).catch(error => {
        console.log('get recent songs fails\n' + error);
      })
    }
  }
}
</script>

<style scoped lang = "scss">

</style>
