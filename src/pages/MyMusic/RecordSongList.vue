<template>
  <div style="margin: 20px 60px">
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

import ContentList from "../../components/ContentList.vue";
import {getRecentSongByUserId, getSingerById} from "../../api";
import {mapGetters} from "vuex";
import {mixin} from "../../mixins";

export default {
  components: {ContentList},
  data() {
    return {
      singerList: [],
      currentPage: 1,
      pageSize: 15,
      listOfRecordedArtists: [],
    }
  },
  mixins: [mixin],
  computed: {
    ...mapGetters([
      'userId',
    ]),
    currentLists() {
      return this.singerList.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize);
    },
  },
  mounted() {
    getRecentSongByUserId(this.userId).then(res => {
      res.data.forEach(item => {
        if (!this.listOfRecordedArtists.includes(item.singerId)) {
          getSingerById(item.singerId).then(res2 => {
            this.singerList.push(res2);
          })
          this.listOfRecordedArtists.push(item.singerId);
        }
      })
    });
  },
  methods: {
    handlePageChange(newPage) {
      this.currentPage = newPage;
    },
  }
}
</script>


<style scoped lang="scss">
.el-pagination {
  text-align: center;
}
</style>
