<template>
  <div class="search-song-Lists">
    <content-list :contentList="songLists"></content-list>
  </div>
</template>

<script>
import {likeTitle} from "../../api";
import ContentList from "../ContentList.vue";
import {mixin} from '../../mixins'
export default {
  name: 'search-song-lists',
  components: {ContentList},
  data(){
    return{
      songLists: [],
    }
  },
  mixins: [mixin],
  computed: {
    keywords(){
      return this.$route.query.keywords;
    }
  },
  mounted () {
    this.getSongList()
  },
  methods:{
    getSongList(){
      if (!this.$route.query.keywords)
      {
        this.notify('null input','warning');
      }
      else
      {
        likeTitle(this.keywords).then(res => {
          this.songLists = res;
          if (!res.length)
          {
            this.notify('没有找到相关歌单','warning');
          }
        }).catch(error => {
          console.log('获取出错',error);
        });
      }
    },
  },
  watch: {
    keywords(newVal, oldVal){
      if (newVal !== oldVal)
      {
        this.getSongList();
      }
    }
  }
}
</script>
<style lang="scss">
@import "../../assets/css/search-song-Lists.scss";
</style>
