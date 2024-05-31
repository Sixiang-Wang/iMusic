<template>
  <div class = "my-song-list">
    <div class="icons-above">
      <el-button class = "create-btn" @click = "createSongList()">创建歌单</el-button>
      <div class="change-style" @click = "changeStyle()">
        <change-style-icon></change-style-icon>
      </div>
    </div>

    <div v-if = "this.listStyle === 1">
      <content-list :contentList = "currentLists"></content-list>
      <div class = "pagination">
        <el-pagination
          layout = "prev, pager, next, total "
          background
          :total = "songLists.length"
          :page-size = "pageSize"
          @current-change = "handlePageChange"
        />
      </div>
    </div>

    <div v-else>
      <ul>
        <li>
          <div class = "menu">
            <span class = "menu-songList">歌单</span>
            <span class = "menu-introduction">简介</span>
            <span class = "menu-style">风格</span>
          </div>
        </li>
        <li v-for = "(item, index) in songLists" :key = index>
          <div class = "item-content" :class = "{'pink' : index % 2}">
            <div class = "item">
              <span class = "img-container">
                <img class = "img" :src = "attachImageUrl(item.pic)" alt = "" @click = "goAlbum(item)">
              </span>
              <span class = "item-title">
                {{ item.title }}
              </span>
              <span class = "item-introduction">
               {{ item.introduction }}
              </span>
              <span class = "item-style">
               {{ item.style }}
              </span>
              <span class = "delete-icon" @click = "deleteItem(item.id)">
                <delete-icon></delete-icon>
              </span>
              <span class = "edit-icon" @click = "editItem(item)">
                <edit-icon></edit-icon>
              </span>
            </div>
          </div>
        </li>
      </ul>
    </div>

    <el-dialog
      title = "确认删除"
      :visible.sync = "deleteDialog"
      width = "30%"
    >
      <span>确定要删除吗？</span>
      <span slot = "footer" style = "text-align: center">
            <el-button @click = "deleteDialog = false;deleteId='' ">取消</el-button>
            <el-button type = "primary" @click = "confirmDelete()">确定</el-button>
          </span>
    </el-dialog>

    <el-dialog title = "添加歌单" :visible.sync = "centerDialogVisible" width = "400px" center>
      <el-form :model = "addForm" ref = "registerForm" label-width = "80px">
        <el-form-item label = "标题" size = "mini">
          <el-input v-model = "addForm.title" placeholder = "标题"></el-input>
        </el-form-item>
        <el-form-item prop = "introduction" label = "简介" size = "mini">
          <el-input v-model = "addForm.introduction" placeholder = "简介" type = "textarea"></el-input>
        </el-form-item>
        <el-form-item prop = "style" label = "风格" size = "mini">
          <el-input v-model = "addForm.style" placeholder = "风格"></el-input>
        </el-form-item>
      </el-form>
      <span slot = "footer">
                <el-button size = "mini" @click = "centerDialogVisible = false">取消</el-button>
                <el-button size = "mini" @click = "addSongList">确定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="修改歌单" :visible.sync="editDialog" width="400px" center>
      <el-form :model="editForm" ref="form" label-width="80px">
        <el-form-item prop="title" label="标题" size="mini">
          <el-input v-model="editForm.title" placeholder="标题"></el-input>
        </el-form-item>
        <el-form-item prop="introduction" label="简介" size="mini">
          <el-input v-model="editForm.introduction" placeholder="简介" type="textarea"></el-input>
        </el-form-item>
        <el-form-item prop="style" label="风格" size="mini">
          <el-input v-model="editForm.style" placeholder="风格"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
                <el-button size="mini" @click="editDialog = false">取消</el-button>
                <el-button size="mini" @click="editSave">确定</el-button>
            </span>
    </el-dialog>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import {mixin} from "../../mixins";
import ContentList from "../../components/ContentList.vue";
import {deleteSongList, selectSongListByUserId, setSongList, updateSongList} from "../../api";
import DeleteIcon from "../../assets/icon/deleteIcon.vue";
import EditIcon from "../../assets/icon/editIcon.vue";
import ChangeStyleIcon from "../../assets/icon/changeStyleIcon.vue";

export default {
  components: {ChangeStyleIcon, EditIcon, DeleteIcon, ContentList},
  data() {
    return {
      songLists: [],
      currentPage: 1,
      pageSize: 15,
      centerDialogVisible: false,
      listStyle: 1,
      deleteDialog: false,
      deleteId: '',
      editDialog: false,
      addForm: {      // 添加歌单的框
        title: '',
        introduction: '',
        style: '',
      },
      editForm: {      //编辑框
        id: '',
        title: '',
        introduction: '',
        style: ''
      },
    }
  },
  mixins: [mixin],
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
  mounted() {
    selectSongListByUserId(this.userId).then(res =>
    {
      this.songLists = res;
    })
  },
  methods: {
    handlePageChange(newPage) {
      this.currentPage = newPage;
    },
    changeStyle() {
      this.listStyle = this.listStyle === 1 ? 0 : 1;
    },
    createSongList() {
      if (!this.loginIn)
      {
        this.notify('请先登录', 'r');
      }
      else {
        this.centerDialogVisible = true;
      }
    },
    addSongList() {
      let params = new URLSearchParams()
      params.append('title', this.addForm.title)
      params.append('pic', '/img/songListPic/default.jpg')
      params.append('introduction', this.addForm.introduction)
      params.append('style', this.addForm.style)
      params.append('userId', this.userId)
      setSongList(params)
        .then(res =>
        {
          if (res.code === 1) {
            this.notify('添加成功', 'success')
            selectSongListByUserId(this.userId).then(res =>
            {
              this.songLists = res;
            });
          }
          else {
            this.notify('添加失败', 'error')
          }

        })
        .catch(err =>
        {
          console.log(err)
        })
      this.centerDialogVisible = false
    },
    goAlbum(item) {
      this.$store.commit('setTempList', item);
      this.$router.push({path: `/song-list-album/${item.id}`});
    },
    deleteItem(id) {
      this.deleteDialog = true;
      this.deleteId = id;
    },
    confirmDelete() {
      deleteSongList(this.deleteId).then(res =>
      {
        if (res)
        {
          this.notify('删除成功');
          this.deleteDialog = false;
          this.deleteId = '';
          selectSongListByUserId(this.userId).then(res =>
          {
            this.songLists = res;
          });
        }
      })
    },
    editItem(item) {
      this.editDialog = true;
      this.editForm.id = item.id;
      this.editForm.title = item.title;
      this.editForm.introduction = item.introduction;
      this.editForm.style = item.style;
    },
    editSave() {
      let params = new URLSearchParams()
      params.append('id', this.editForm.id)
      params.append('title', this.editForm.title)
      params.append('introduction', this.editForm.introduction)
      params.append('style', this.editForm.style)

      updateSongList(params)
        .then(res => {
          if (res.code === 1) {
            selectSongListByUserId(this.userId).then(res =>
            {
              this.songLists = res;
            })
            this.notify('修改成功', 'success')
          } else {
            this.notify('修改失败', 'error')
          }
        })
        .catch(err => {
          console.log(err)
        })
      this.editDialog = false
    }

  }
}
</script>

<style scoped lang = "scss">
@import "../../assets/css/my-song-list.scss";

.el-pagination {
  text-align: center;
}
</style>
