<template>
  <content-list style="margin: 20px 60px" :contentList="collectList"></content-list>
</template>

<script>

import ContentList from "../../components/ContentList.vue";
import {mapGetters} from "vuex";
import {collectSongListOfUserId} from "../../api";

export default {
  components: {ContentList},
  data(){
    return{
      collectList: [],
    }
  },
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

</style>
