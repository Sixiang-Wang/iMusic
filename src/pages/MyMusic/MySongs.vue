<template>
  <div class = "my-songs">
    <el-button class = "create-btn" @click = "upload()">上传歌曲</el-button>
    <el-table :data="this.songs" stripe style="width: 95%;margin-top: 30px">
      <el-table-column prop="pic" label="歌曲图片" width="200px">
        <template slot-scope="scope">
          <img :src="attachImageUrl(scope.row.pic)" alt="歌曲图片" style="width: 80px; height: 80px;">
        </template>
      </el-table-column>
      <el-table-column prop="name" label="歌手-歌名" width="300px">
      </el-table-column>
      <el-table-column prop="style" label="歌曲风格" width="300px">
      </el-table-column>
      <el-table-column prop="introduction" label="专辑" width="200px">
      </el-table-column>
      <el-table-column label="资源更新" align="center" width="150">
        <template slot-scope="scope">
          <el-upload :action="uploadUrl(scope.row.id)" :before-upload="beforeAvatarUpload"
                     :on-success="handleAvatarSuccess" :show-file-list="false">
            <el-button size="mini">更新图片</el-button>
          </el-upload>
          <br/>
          <el-upload :action="uploadSongUrl(scope.row.id)" :before-upload="beforeSongUpload"
                     :on-success="handleSongSuccess" :show-file-list="false">
            <el-button size="mini">更新歌曲</el-button>
          </el-upload>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
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
          <el-input type = "text" v-model="addForm.songName" name = "songName"></el-input>
        </div>
        <div>
          <label>专辑</label>
          <el-input type = "text" v-model="addForm.introduction" name = "introduction"></el-input>
        </div>
        <div>
          <label>风格</label>
          <el-input type = "text" v-model="addForm.style" name = "style"></el-input>
        </div>
        <div>
          <label>歌词</label>
          <el-input type = "textarea" v-model="addForm.lyric" placeholder="请务必严格按照lrc歌词规范，否则将无法识别" rows="8" name = "lyric"></el-input>
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

    <el-dialog title="修改歌曲" :visible.sync="editVisible" width="400px" center>
      <el-form :model="form" ref="form" label-width="80px">
        <el-form-item prop="name" label="歌名" size="mini">
          <el-input v-model="form.name" placeholder="歌手-歌名"></el-input>
        </el-form-item>
        <el-form-item prop="introduction" label="专辑" size="mini">
          <el-input v-model="form.introduction" placeholder="专辑"></el-input>
        </el-form-item>
        <el-form-item prop="style" label="风格" size="mini">
          <el-input v-model="form.style" placeholder="风格"></el-input>
        </el-form-item>
        <el-form-item prop="lyric" label="歌词" size="mini">
          <el-input v-model="form.lyric" placeholder="歌词" type="textarea"></el-input>
        </el-form-item>

      </el-form>
      <span slot="footer">
                <el-button size="mini" @click="editVisible = false">取消</el-button>
                <el-button size="mini" @click="editSave">确定</el-button>
            </span>
    </el-dialog>

    <el-dialog title="删除歌曲" :visible.sync="delVisible" top="20%" width="300px" center>
      <div align="center">删除不可恢复，是否确定删除？</div>
      <span slot="footer">
                <el-button size="mini" @click="delVisible = false">取消</el-button>
                <el-button size="mini" @click="confirmDelete()">确定</el-button>
            </span>
    </el-dialog>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import {delSong, getUserOfId, songOfSingerId, updateSong} from "../../api";
import {mixin} from "../../mixins";
export default {
  data() {
    return {
      addDialog: false,
      editVisible: false,         //编辑弹窗是否显示
      delVisible: false,          //删除弹窗是否显示
      songs: [],
      songId: '',
      name: '', //用户昵称
      singerId: '',
      addForm: {      // 添加歌曲的框
        songName: '',
        introduction: '',
        style: '',
        lyric: '',
      },
      form: {      //编辑框
        id: '',
        name: '',
        introduction: '',
        lyric: '',
        style: ''
      },
    }
  },
  mixins:[mixin],
  computed: {
    ...mapGetters([
      'loginIn',
      'userId',
      'username',
    ])
  },
  mounted() {
    getUserOfId(this.userId).then(res => {
      this.singerId = res.singerId;
      this.name = res.name;
      songOfSingerId(this.singerId).then(res => {
        this.songs = res;
      });
    })
  },
  methods: {
    upload() {
      this.addDialog = true
    },
    uploadSongUrl (id) {
      return `${this.$store.state.configure.HOST}/song/updateSongUrl?id=${id}`
    },
    uploadUrl (id) {
      return `${this.$store.state.configure.HOST}/song/updateSongPic?id=${id}`
    },
    beforeSongUpload (file) {
      let testMsg = file.name.substring(file.name.lastIndexOf('.') + 1)
      if (testMsg !== 'mp3' && testMsg !== 'MP3' && testMsg !== 'wav' && testMsg !== 'WAV' && testMsg !== 'flac' && testMsg !== 'FLAC') {
        this.$message({
          message: '请上传mp3、wav或flac文件',
          type: 'error'
        })
        return false
      }
      return true
    },
    beforeAvatarUpload (file) {
      const isJPG = (file.type === 'image/jpg') || (file.type === 'image/jpeg') || (file.type === 'image/png')
      if (!isJPG) {
        this.$message.error('上传头像图片只能是jpg或png格式')
        return false
      }
      const isLt2M = (file.size / 1024 / 1024) < 10
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过10MB')
        return false
      }
      return true
    },
    handleSongSuccess (res) {
      let self = this;
      if (res.code === 1) {
        songOfSingerId(self.singerId).then(res => {
          self.songs = res
        });
        self.notify('上传成功', 'success')
      } else {
        self.notify('上传失败', 'error')
      }
    },
    handleAvatarSuccess (res) {
      if (res.code === 1) {
        songOfSingerId(this.singerId).then(res => {
          this.songs = res
        });
        this.notify('上传成功', 'success')
      } else {
        this.notify(`上传失败: ${res.msg}`, 'error')
      }
    },
    handleEdit (row) {
      this.editVisible = true
      this.form = {
        id: row.id,
        name: row.name,
        introduction: row.introduction,
        lyric: row.lyric,
        style: row.style
      }
    },
    handleDelete(id){
      this.delVisible = true;
      this.songId = id;
    },
    confirmDelete() {
      delSong(this.songId).then(res => {
        if (res)
        {
          this.songId = '';
          this.delVisible = false;
          songOfSingerId(this.singerId).then(res => {
            this.songs = res
          });
          this.notify('删除成功','success')
        }
        else
        {
          this.songId = '';
          this.delVisible = false;
          this.notify('删除失败','success')
        }
      })
    },
    editSave () {
      let params = new URLSearchParams()
      params.append('id', this.form.id)
      params.append('name', this.form.name)
      params.append('introduction', this.form.introduction)
      params.append('lyric', this.form.lyric)
      params.append('style', this.form.style)
      updateSong(params)
        .then(res => {
          if (res.code === 1) {
            songOfSingerId(this.singerId).then(res => {
              this.songs = res;
            });
            this.notify('修改成功', 'success')
          } else {
            this.notify('修改失败', 'error')
          }
        })
        .catch(err => {
          console.log(err)
        })
      this.editVisible = false
    },
    addSong() {
      let self = this;
      let form = new FormData(document.getElementById('tf'));
      form.append('singerId', this.singerId);
      form.set('name', this.name+ '-' + form.get('songName'));
      if (!form.get('lyric')) {
        form.set('lyric', '[00:00:00]暂无歌词');
      }
      var req = new XMLHttpRequest();
      req.open('post', `${self.$store.state.configure.HOST}/song/add`, false);
      req.onreadystatechange = function ()
      {
        /**
         req.readyState == 4 获取到返回的完整数据
         req.status == 200 和后台正常交互完成
         **/
        if (req.readyState === 4 && req.status === 200) {
          let res = JSON.parse(req.response)
          if (res.code) {
            songOfSingerId(self.singerId).then(res => {
              self.songs = res
            });
            self.addForm = {};
            self.notify(res.msg, 'success')
          }
          else {
            self.notify('保存失败', 'error')
          }
        }
      };

      req.send(form);
      self.addDialog = false;
    },
  }
}
</script>

<style scoped lang = "scss">
@import "../../assets/css/my-songs.scss";
</style>
