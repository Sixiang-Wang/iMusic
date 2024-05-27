<template>
  <div class = "song-list">
    <div class="corner-plus-button">
      <span class="plus-sign" @click="createSongList()">+</span>
    </div>
    <el-dialog title="添加歌单" :visible.sync="centerDialogVisible" width="400px" center>
      <el-form :model="addForm" ref="registerForm" label-width="80px">
        <el-form-item label="标题" size="mini">
          <el-input v-model="addForm.title" placeholder="标题"></el-input>
        </el-form-item>
        <el-form-item prop="introduction" label="简介" size="mini">
          <el-input v-model="addForm.introduction" placeholder="简介" type="textarea"></el-input>
        </el-form-item>
        <el-form-item prop="style" label="风格" size="mini">
          <el-input v-model="addForm.style" placeholder="风格"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
                <el-button size="mini" @click="centerDialogVisible = false">取消</el-button>
                <el-button size="mini" @click="addSongList">确定</el-button>
      </span>
    </el-dialog>
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
import {getAllSongList,setSongList} from "../api";
import ContentList from "../components/ContentList.vue";
import {mapGetters} from "vuex";
import {mixin} from '../mixins';
export default {
  name: 'song-list',
  components: {ContentList},
  data() {
    return {
      songLists: [],
      currentPage: 1,
      pageSize: 15,
      centerDialogVisible: false,
      addForm: {      // 添加歌单的框
        title: '',
        introduction: '',
        style: ''
      },
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
  mixins: [mixin],
  mounted() {
    getAllSongList().then(res =>
    {
      this.songLists = res;
    });
  },
  methods: {
    handlePageChange(newPage) {
      this.currentPage = newPage;
    },
    createSongList() {
      if (!this.loginIn)
      {
        this.notify('请先登录','r');
      }
      else
      {
        this.centerDialogVisible = true;
      }
    },
    addSongList () {
      let params = new URLSearchParams()
      params.append('title', this.addForm.title)
      params.append('pic', '/img/songListPic/default.jpg')
      params.append('introduction', this.addForm.introduction)
      params.append('style', this.addForm.style)
      params.append('userId',this.userId)
      setSongList(params)
        .then(res => {
          if (res.code === 1) {
            this.notify('添加成功', 'success')
            getAllSongList().then(res =>
            {
              this.songLists = res;
            });
          } else {
            this.notify('添加失败', 'error')
          }

        })
        .catch(err => {
          console.log(err)
        })
      this.centerDialogVisible = false
    },
  }
}
</script>

<style lang = "scss" scoped>
@import "../assets/css/song-list";
.el-pagination {
  text-align: center;
}
</style>
