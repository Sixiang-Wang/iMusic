<template>
  <album-content :song-list="collectList"></album-content>
</template>

<script>
import AlbumContent from "../../components/AlbumContent.vue";
import {mapGetters} from "vuex";
import {collectSongOfUserId} from "../../api";
import {mixin} from "../../mixins";
export default {
  components: {AlbumContent},
  data() {
    return {
      collectList : [],     // 收藏的歌曲列表（带歌曲详情）
    }
  },
  mixins:[mixin],
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
      collectSongOfUserId(userId).then(res => {
        // console.log("res:",res);
        this.collectList = res;
      }).catch(error => {
        console.log('get collection of songs fails\n' + error);
      })
    }
  }
}
</script>

<style scoped lang = "scss">

</style>
