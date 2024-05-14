<template>
  <div class = "song-list">
    <content-list :contentList = "currentLists"></content-list>
    <div class="pagination">
      <el-pagination
        layout="prev, pager, next, total "
        background
        :total="songLists.length"
        :page-size="pageSize"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script>
import {getAllSongList} from "../api";
import ContentList from "../components/ContentList.vue";

export default {
  name: 'song-list',
  components: {ContentList},
  data() {
    return {
      songLists: [],
      currentPage: 1,
      pageSize: 15,
    }
  },
  computed: {
    currentLists() {
      return this.songLists.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize);
    },
  },
  mounted() {
    getAllSongList().then(res =>
    {
      this.songLists = res;
    });
  },
  methods: {
    handlePageChange(newPage) {
      this.currentPage = newPage;
    }
  }
}
</script>

<style lang = "scss" scoped>
@import "../assets/css/song-list";
.el-pagination {
  text-align: center;
}
</style>
