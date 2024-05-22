<template>
  <div class="table">
    <div class="crumbs">
      <i class="el-icon-tickets"></i>用户关注
    </div>
    <div class="container">
      <div class="handle-box">
        <el-button type="primary" size="mini" @click="delAll">批量删除</el-button>
        <el-input v-model="select_word" size="mini" placeholder="筛选关键词" class="handle-input"></el-input>
      </div>
    </div>
    <el-table size="mini" ref="multipleTable" border style="width:100%" height="680px" :data="tableData"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40"></el-table-column>
      <el-table-column prop="name" label="歌手" align="center"></el-table-column>
      <el-table-column label="操作" width="150" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">取消关注</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="取消关注" :visible.sync="delVisible" width="300px" center>
      <div align="center">确认取消？</div>
      <span slot="footer">
                <el-button size="mini" @click="delVisible = false">取消</el-button>
                <el-button size="mini" @click="deleteRow">确定</el-button>
            </span>
    </el-dialog>
  </div>
</template>

<script>
import {mixin} from '../mixins/index'
import {
  songOfSongId,
  getCollectSongOfUserId,
  deleteCollection,
  getFollowByUserId,
  getUserOfId,
  getSingerById, deleteFollow
} from '../api/index'

export default {
  mixins: [mixin],
  props: ['id'],
  data () {
    return {
      delVisible: false,          //删除弹窗是否显示
      tableData: [],
      tempData: [],
      select_word: '',
      idx: -1,          //当前选择项
      multipleSelection: [],   //哪些项已经打勾
    }
  },
  watch: {
    //搜索框里面的内容发生变化的时候，搜索结果table列表的内容跟着它的内容发生变化
    select_word: function () {
      if (this.select_word === '') {
        this.tableData = this.tempData
      } else {
        this.tableData = []
        for (let item of this.tempData) {
          if (item.name.includes(this.select_word)) {
            this.tableData.push(item)
          }
        }
      }
    }
  },
  created () {
    this.getData()
  },
  methods: {
    //查询该用户所有收藏的歌曲
    getData () {
      this.tempData = []
      this.tableData = []
      getFollowByUserId(this.$route.query.id).then(res => {
        for (let item of res) {
          getSingerById(item.singerId).then(singer => {
            item.name = singer.name
            this.tempData.push(item)
            this.tableData.push(item)
          })
        }
      })
    },

    //删除一条歌曲
    deleteRow () {
      deleteFollow(this.idx.id)
        .then(res => {
          if (res.code === 1) {
            this.notify('取消成功', 'success')
          } else {
            this.notify('取消失败', 'error')
          }
          this.getData()
        })
        .catch(err => {
          console.log(err)
        })
      this.delVisible = false
    },
    //批量删除已经选择的项
    delAll () {
      for (let item of this.multipleSelection) {
        this.handleDelete(item)
        this.deleteRow()
      }
      this.multipleSelection = []
    }
  }
}
</script>

<style scoped>
.handle-box {
  margin-bottom: 5px;
}

.handle-input {
  width: 300px;
  display: inline-block;
}
</style>
