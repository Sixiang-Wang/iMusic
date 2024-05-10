<template>
  <div class="search-songs">
    <album-content :songList="listOfSongs"></album-content>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import {mixin} from '../../mixins'
import AlbumContent from '../AlbumContent.vue'

export default {
  name: 'search-songs',
  components: {
    AlbumContent
  },
  mixins: [mixin],
  computed: {
    ...mapGetters([
      'listOfSongs'
    ]),
    keywords(){
      return this.$route.query.keywords || '';
    }
  },
  mounted () {
    this.getSong()
  },
  watch: {
    keywords(newVal, oldVal){
      if (newVal !== oldVal)
      {
        this.getSong();
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../../assets/css/search-songs.scss";
</style>
