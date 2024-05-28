<template>
  <div class="table">
    <div class="container">
      <div class="handle-box">
        <el-button type="primary" size="mini" @click="delAll">批量删除</el-button>
        <el-input v-model="select_word" size="mini" placeholder="请输入歌单名" class="handle-input"></el-input>
      </div>
    </div>
    <el-table size="mini" ref="multipleTable" border style="width:100%" height="680px" :data="data"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40"></el-table-column>
      <el-table-column label="歌单图片" width="110" align="center">
        <template slot-scope="scope">
          <div class="song-img">
            <img :src="getUrl(scope.row.pic)" style="width:100%"/>
          </div>
        </template>
      </el-table-column>
      <el-table-column  prop="title" label="歌单名" width="150" align="center"></el-table-column>
      <el-table-column  prop="introduction" label="简介" align="center"></el-table-column>
      <el-table-column prop="style" label="风格" width="90" align="center"></el-table-column>
      <el-table-column label="操作" width="100" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          <br>
          <el-button size="mini"  @click="visibleRow(scope.row.id)">恢复</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination">
      <el-pagination
        background
        layout="total,prev,pager,next"
        :current-page.sync="currentPage"
        :page-size="pageSize"
        :total="tableData.length"
        @current-change="handleCurrentChange"
      >
      </el-pagination>
    </div>
    <el-dialog title="彻底删除歌单" :visible.sync="delVisible" width="300px" center>
      <div align="center">彻底删除不可恢复，是否确定删除？</div>
      <span slot="footer">
                <el-button size="mini" @click="delVisible = false">取消</el-button>
                <el-button size="mini" @click="deleteRow">确定</el-button>
            </span>
    </el-dialog>
  </div>
</template>

<script>
import {mixin} from '../mixins/index'
import {mapGetters} from 'vuex'
import '@/assets/js/iconfont.js'
import {
  updateSong,
  delSong,
  allSong,
  oneSingerOfName,
  invisibleSong,
  visibleSong,
  allInvisible,
  allInvisibleSongList, delSongList, visibleSongList
} from '../api/index'

export default {
  mixins: [mixin],
  data () {
    return {
      delVisible: false,          //删除弹窗是否显示
      tableData: [],
      tempData: [],
      select_word: '',
      pageSize: 5,    //分页每页大小
      currentPage: 1,  //当前页
      idx: -1,          //当前选择项
      multipleSelection: [],   //哪些项已经打勾
    }
  },
  computed: {
    /* 计算当前搜索结果表里的数据 */
    data () {
      return this.tableData.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize)
    }
  },
  watch: {
    /* 搜索框里面的内容发生变化的时候，搜索结果table列表的内容跟着它的内容发生变化 */
    select_word: function () {
      if (this.select_word === '') {
        this.tableData = this.tempData
      } else {
        this.tableData = []
        for (let item of this.tempData) {
          if (item.title.includes(this.select_word)) {
            this.tableData.push(item)
          }
        }
      }
    }
  },
  created () {
    this.getData()
  },
  destroyed () {
    this.$store.commit('setIsPlay', false)
  },
  methods: {
    /* 获取当前页 */
    handleCurrentChange (val) {
      this.currentPage = val
    },
    /* 查询 */
    getData (cur) {
      if (!cur) {
        cur = 1
      }
      this.tempData = []
      this.tableData = []
      allInvisibleSongList().then(res => {
        this.tempData = res
        this.tableData = res
        let len = res.length
        this.currentPage = cur
        if (len / this.pageSize <= cur - 1) {
          this.currentPage = 1
        }
      })
    },
    //删除
    deleteRow () {
      delSongList(this.idx)
        .then(res => {
          if (res) {
            this.getData(this.currentPage)
            this.notify('删除成功', 'success')
          } else {
            this.notify('删除失败', 'error')
          }
        })
        .catch(err => {
          console.log(err)
        })
      this.delVisible = false
    },
    visibleRow (id) {
      visibleSongList(id)
        .then(res => {
          if (res) {
            this.getData(this.currentPage)
            this.notify('恢复成功', 'success')
          } else {
            this.notify('恢复失败', 'error')
          }
        })
    }
  }
}
</script>

<style scoped>
.handle-box {
  margin-bottom: 5px;
}

.song-img {
  width: 100%;
  height: 80px;
  border-radius: 5px;
  margin-bottom: 5px;
  overflow: hidden;
}

.handle-input {
  width: 300px;
  display: inline-block;
}

.pagination {
  display: flex;
  justify-content: center;
}

.play {
  position: absolute;
  z-index: 100;
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  top: 18px;
  left: 15px;
}

.icon {
  width: 2em;
  height: 2em;
  color: white;
  fill: currentColor;
  overflow: hidden;
}

.flexible-column {
  min-width: 200px; /* 设置最小宽度为 150px */
  width: auto; /* 允许列宽自动调整 */
}
.song-inf-crumbs{
  margin-bottom: 5px;
}
</style>
