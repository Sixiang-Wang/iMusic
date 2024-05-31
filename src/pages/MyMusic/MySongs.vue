<template>
  <div class = "my-songs">
    <el-button class = "create-btn" @click = "upload()">上传歌曲</el-button>
    <el-table :data="this.songs" style="width: 100%">
      <el-table-column prop="pic" label="歌曲图片" width="120">
        <template slot-scope="scope">
          <img :src="attachImageUrl(scope.row.pic)" alt="歌曲图片" style="width: 80px; height: 80px;">
        </template>
      </el-table-column>
      <el-table-column prop="name" label="歌名" width="100px">
      </el-table-column>
      <el-table-column prop="style" label="歌曲风格" width="100px">
      </el-table-column>
      <el-table-column prop="introduction" label="专辑" width="100px">
      </el-table-column>
    </el-table>

    <el-dialog
      :visible.sync = "addDialog"
      title = "添加新歌曲"
      width = "50%"
      center
    >
      <el-form :model = "addForm" ref = "addForm" label-width = "80px" action = "" id = "tf">
        <div>
          <label>歌名</label>
          <el-input type = "text" name = "songName"></el-input>
        </div>
        <div>
          <label>专辑</label>
          <el-input type = "text" name = "introduction"></el-input>
        </div>
        <div>
          <label>风格</label>
          <el-input type = "text" name = "style"></el-input>
        </div>
        <div>
          <label>歌词</label>
          <el-input type = "textarea" rows="8" name = "lyric"></el-input>
        </div>
        <div>
          <label>歌曲上传</label>
          <br>
          <input type = "file" accept="audio/*" name = "file">
        </div>
      </el-form>
      <span slot = "footer">
        <el-button size = "mini" @click = "addDialog = false">取消</el-button>
        <el-button size = "mini" type = "primary" @click = "addSong">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import {songOfSingerId} from "../../api";
import {mixin} from "../../mixins";
export default {
  data() {
    return {
      addDialog: false,
      songs: [],
      addForm: {      // 添加歌曲的框
        songName: '',
        introduction: '',
        style: '',
        lyric: '',
      },
    }
  },
  mixins:[mixin],
  computed: {
    ...mapGetters([
      'loginIn',
      'userId',
      'username'
    ])
  },
  mounted() {
    this.songs = songOfSingerId(this.userId);
  },
  methods: {
    upload() {
      this.addDialog = true
    },
    addSong() {
      let form = new FormData(document.getElementById('tf'));
      form.append('singerId', this.userId);
      form.set('name', this.username + '-' + form.get('songName'));
      if (!form.get('lyric')) {
        form.set('lyric', '[00:00:00]暂无歌词');
      }
      let req = new XMLHttpRequest();
      req.onreadystatechange = function ()
      {
        /**
         req.readyState == 4 获取到返回的完整数据
         req.status == 200 和后台正常交互完成
         **/
        if (req.readyState === 4 && req.status === 200) {
          let res = JSON.parse(req.response)
          if (res.code) {
            // this.getData()
            this.addForm = {};
            this.notify(res.msg, 'success')
          }
          else {
            this.notify('保存失败', 'error')
          }
        }
      };
      req.open('post', `${this.$store.state.HOST}/song/add`, false);
      req.send(form);
      this.addDialog = false;
    },
  }
}
</script>

<style scoped lang = "scss">
@import "../../assets/css/my-songs.scss";
</style>
