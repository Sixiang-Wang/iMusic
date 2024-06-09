<template>
  <div>
    <div class = "style-select">
      <style-select></style-select>
    </div>
    <div class="song-list">
      <content-list :contentList = "currentLists"></content-list>
      <div class = "pagination">
        <el-pagination
          layout = "prev, pager, next, total "
          background
          :total = "songLists.length"
          :page-size = "pageSize"
          @current-change = "handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script>
import StyleSelect from "../components/Style.vue";
import ContentList from "../components/ContentList.vue";
import {mapGetters} from "vuex";
import {mixin} from "../mixins";
import {getTopSong, songLikeStyle} from "../api";

export default {
  name: 'song',
  components: {ContentList, StyleSelect},
  mixins: [mixin],
  data() {
    return {
      songLists: [],
      currentPage: 1,
      pageSize: 25,
    }
  },
  computed: {
    currentLists() {
      return this.songLists.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize);
    },
    ...mapGetters([
        'loginIn',
        'userId',
      ]
    )
  },
  methods: {
    getSongList () {
      if (this.$route.query.hasOwnProperty('style'))
      {
        songLikeStyle(this.$route.query.style).then(res => {
          this.songLists = res;
        })
      }
      else {
        getTopSong().then(res =>
        {
          this.songLists = res;
        });
      }
    },
    handlePageChange(newPage) {
      this.currentPage = newPage;
    },
  },
  watch: {
    '$route.query': {
      immediate: true,
      handler() {
        this.getSongList();
      }
    }
  },

}
</script>

<style scoped lang = "scss">
@import "../assets/css/song.scss";
@import "../assets/css/styles";

.el-pagination {
  text-align: center;
}
</style>
