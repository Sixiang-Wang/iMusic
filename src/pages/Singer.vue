<template>
  <div class="singer">
    <content-list :contentList="currentLists"></content-list>
    <div class="pagination">
      <el-pagination
        style="text-align: center"
        layout="prev, pager, next, total "
        background
        :current-page.sync="currentPage"
        :total="singerList.length"
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
      singerList: [],
      currentPage: 1,
      pageSize: 15,
      isFollow: '收藏',
    }
  },
  computed: {
    currentLists() {
      return this.singerList.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize);
    },
  },
  mounted() {
    getAllSinger().then(res => {
      this.singerList = res;
    });
  },
  methods: {
    handlePageChange(newPage) {
      this.currentPage = newPage;
    },
  }
}
</script>

<style lang="scss" scoped>
@import "../assets/css/singer";
</style>
