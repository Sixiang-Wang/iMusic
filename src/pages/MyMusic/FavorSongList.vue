<template>
  <div class="body-content">
    <content-list :contentList="collectList"></content-list>
  </div>

</template>

<script>

import ContentList from "../../components/ContentList.vue";
import {mapGetters} from "vuex";
import {collectSongListOfUserId} from "../../api";
import {mixin} from "../../mixins";

export default {
  components: {ContentList},
  data(){
    return{
      collectList: [],
    }
  },
  mixins: [mixin],
  computed:{
    ...mapGetters([
      'userId',
    ])
  },
  mounted() {
    this.getCollection(this.userId);
  },
  methods: {
    getCollection(userId) {
      collectSongListOfUserId(userId).then(res => {
        this.collectList = res;
      }).catch(error => {
        console.log('get collection of songList fails\n' + error);
      })
    }
  }
}
</script>

<style scoped lang = "scss">
@import "../../assets/css/favorSongList.scss";
</style>
