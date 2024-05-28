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
import {getFollow, getSingerById} from "../../api";
import {mapGetters} from "vuex";
import {mixin} from "../../mixins";
export default {
  components: {ContentList},
  data() {
    return {
      singerList: [],
      currentPage: 1,
      pageSize: 15,
      isFollow: '收藏',
    }
  },
  mixins:[mixin],
  computed: {
    ...mapGetters([
      'userId',
    ]),
    currentLists() {
      return this.singerList.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize);
    },
  },
  mounted() {
    getFollow(this.userId).then(res => {
      res.forEach(item => {
        getSingerById(item.singerId).then(res2 => {
          this.singerList.push(res2);
        })
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


<style scoped lang = "scss">
.el-pagination {
  text-align: center;
}
</style>
