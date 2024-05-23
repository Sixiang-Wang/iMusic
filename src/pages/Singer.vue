<template>
  <div class = "singer">
    <content-list :contentList = "currentLists"></content-list>
    <div class="pagination">
      <el-pagination
        style="text-align: center"
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
import {getAllSinger} from "../api";
import ContentList from "../components/ContentList.vue";

export default {
  name: 'singer',
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
    getAllSinger().then(res =>
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
@import "../assets/css/singer";
</style>
