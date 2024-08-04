<template>
  <div class="table">
    <div class="sub-title">
      <div v-if="toggle != false">
        <i class="el-icon-tickets"></i>歌曲信息-{{ singerName }}正在播放: {{ toggle }}
      </div>
      <div v-if="toggle == false">
        <i class="el-icon-tickets"></i>歌曲信息-{{ singerName }}正在播放: 无
      </div>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-button type="primary" size="mini" @click="delAll">批量删除</el-button>
        <el-input v-model="select_word" size="mini" placeholder="请输入歌曲名" class="handle-input"></el-input>
      </div>
    </div>
    <el-table size="mini" ref="multipleTable" border style="width:100%" height="680px" :data="data"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40"></el-table-column>
      <el-table-column label="歌曲图片" width="110" align="center">
        <template slot-scope="scope">
          <div class="song-img">
            <img :src="getUrl(scope.row.pic)" style="width:100%"/>
          </div>
          <div class="play" @click="setSongUrl(scope.row.url,scope.row.name)">
            <div v-if="toggle == scope.row.name&&isPlay">
              <svg class="icon">
                <use xlink:href="#icon-zanting"></use>
              </svg>
            </div>
            <div v-if="toggle != scope.row.name||!isPlay">
              <svg class="icon">
                <use xlink:href="#icon-bofanganniu"></use>
              </svg>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column  prop="name" label="歌手-歌名" width="150" align="center"></el-table-column>
      <el-table-column prop="introduction" label="专辑" width="120" align="center"></el-table-column>
      <el-table-column prop="style" label="风格" width="90" align="center"></el-table-column>
      <el-table-column label="歌词" min-width="130" align="left">
        <template slot-scope="scope">
          <ul style="height:100px;overflow:scroll;white-space: nowrap">
            <li v-for="(item,index) in parseLyric(scope.row.lyric)" :key="index">
              {{ item }}
            </li>
          </ul>
        </template>
      </el-table-column>

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
    <el-dialog title="删除歌曲" :visible.sync="delVisible" width="300px" center>
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
import {delSong, visibleSong, allInvisible} from '../api/index'

export default {
  mixins: [mixin],
  data () {
    return {
      singerId: '',               // 歌手id
      singerName: '',             //歌手名
      centerDialogVisible: false, //添加弹窗是否显示
      editVisible: false,         //编辑弹窗是否显示
      delVisible: false,          //删除弹窗是否显示
      registerForm: {      //添加框
        name: '',
        singerName: '',
        introduction: '',
        lyric: '',
        style: ''
      },
      form: {      //编辑框
        id: '',
        name: '',
        introduction: '',
        lyric: '',
        style: ''
      },
      tableData: [],
      tempData: [],
      select_word: '',
      pageSize: 5,    //分页每页大小
      currentPage: 1,  //当前页
      idx: -1,          //当前选择项
      multipleSelection: [],   //哪些项已经打勾
      toggle: false,           //播放器的图标状态
      toggle_tmp: false        //防止不暂停直接切换出现bug
    }
  },
  computed: {
    ...mapGetters([
      'isPlay'
    ]),
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
          if (item.name.includes(this.select_word)) {
            this.tableData.push(item)
          }
        }
      }
    }
  },
  created () {
    this.singerId = this.$route.query.id
    this.singerName = this.$route.query.name
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
      allInvisible().then(res => {
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
      delSong(this.idx)
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
      visibleSong(id)
        .then(res => {
          if (res) {
            this.getData(this.currentPage)
            this.notify('恢复成功', 'success')
          } else {
            this.notify('恢复失败', 'error')
          }
        })
    },
    //解析歌词
    parseLyric (text) {
      let lines = text.split('\n')
      let pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})\]/g
      let result = []
      for (let item of lines) {
        let value = item.replace(pattern, '')
        result.push(value)
      }
      return result
    },
    //切换播放歌曲
    setSongUrl (url, name) {
      this.toggle_tmp = this.toggle
      this.toggle = name
      this.$store.commit('setUrl', this.$store.state.HOST + url)
      if (this.isPlay && this.toggle_tmp === name) {
        this.$store.commit('setIsPlay', false)
      } else {
        this.$store.commit('setIsPlay', true)
      }
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
