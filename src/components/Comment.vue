<template>
  <div class = "comment">
    <h2>评论</h2>
    <el-input
      class = "input"
      v-model = "inputValue"
      placeholder = "说说你的看法吧"
      type = "textarea"
      :rows = "6"
      clearable>
    </el-input>

    <el-button class = "sub-btn" @click = "sendComment()">发表评论</el-button>


    <div>
      <h2>精彩评论: 共{{ this.commentList.length }}条</h2>
      <ul class = "popular" v-for = "(item, index) in this.commentList" :key = "index">
        <li>
          <div class = "popular-img">
            <img :src = "attachImageUrl(avatars[item.id])" alt = "">
          </div>
          <div class = "popular-msg">
            <ul>
              <li>{{ usernames[item.id] }}</li>
              <li class = "time">{{ item.createTime }}</li>
              <li class = "content">{{ item.content }}</li>
              <li class = "up" ref = "isUp" @click = "handleUp(item.id, index)">
                {{ existUp(item.id, index) }}
                <up-icon></up-icon>
                <span>{{up[item.id]}}</span>
              </li>

            </ul>
          </div>
          <div class = "delete-icon" @click = "delComment(item.id)" v-if = "ownComment(item.userId)">
            <delete-icon></delete-icon>
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
  </div>
</template>

<script>
import {mixin} from "../mixins";
import {
  addCommentUp, deleteComment,
  deleteCommentUp,
  existCommentUp,
  getCommentOfSong,
  getCommentOfSongList, getUpCount,
  getUserOfId,
  setComment, setLike
} from "../api";
import {mapGetters} from "vuex";
import testIcon from '../assets/icon/deleteIcon.vue'
import DeleteIcon from "../assets/icon/deleteIcon.vue";
import UpIcon from "../assets/icon/upIcon.vue";

export default {
  name: 'comment',
  mixins: [mixin],
  props: ['type'],
  components: {UpIcon, DeleteIcon, testIcon},
  data() {
    return {
      deleteDialog: false,
      deleteId: '',
      inputValue: '',
      commentList: [],
      avatars: [],
      usernames: [],
      songOrSongListId: '',
      up: [],
    }
  },
  computed: {
    ...mapGetters([
      'loginIn',
      "userId",
    ]),

  },
  mounted() {
    this.songOrSongListId = this.$route.params.id;
    this.getCommentList();
  },


  methods: {
    sendComment() {
      if (!this.loginIn)
      {
        this.notify('请先登录', 'r');
      }
      else if (!this.inputValue)
      {
        this.notify('请输入内容');
      }
      else {
        let params = new URLSearchParams();
        params.append('userId', this.userId);
        params.append('type', this.type);
        params.append(this.type === 0 ? 'songId' : 'songListId', this.songOrSongListId);
        params.append('content', this.inputValue.trim());
        setComment(params).then(res =>
        {
          if (res.code === 1)
          {
            this.notify('评论成功');
            this.inputValue = '';
            this.getCommentList();
          }
          else {
            this.notify('评论失败');
          }
        })
      }
    },
    getCommentList() {
      if (this.type === 0)
      {
        getCommentOfSong(this.songOrSongListId).then(res =>
        {
          this.commentList = res;
          this.getMsg();
        })
      }
      else {
        getCommentOfSongList(this.songOrSongListId).then(res =>
        {
          this.commentList = res;
          this.getMsg();
        })
      }
    },
    delComment(commentId) {
      this.deleteDialog = true;
      this.deleteId = commentId;
    },
    confirmDelete() {
      deleteComment(this.deleteId).then(res =>
      {
        if (res) {
          this.getCommentList();
          this.notify('删除成功')
        }
        else {
          this.notify('删除失败')
        }
        this.deleteDialog = false;
        this.deleteId = '';
      })
    },
    getMsg() {
      this.commentList.forEach(item =>
      {
        this.up[item.id] = item.up;
        getUserOfId(item.userId).then(res =>
        {
          this.avatars[item.id] = res.profilePicture;
          this.usernames[item.id] = res.username;
          this.$forceUpdate();
        });
      });
    },
    existUp(commentId, index) {
      existCommentUp(this.userId, commentId).then(res =>
      {
        if (res) {
          this.$refs.isUp[index].children[0].classList.add('active');
        }
        else {
          this.$refs.isUp[index].children[0].classList.remove('active');
        }
      })
    },
    ownComment(userId) {
      return this.userId === userId;
    },
    handleUp(commentId, index) {
      if (!this.loginIn)
      {
        this.notify('请先登录');
      }
      else {
        existCommentUp(this.userId, commentId).then(res =>
        {
          if (res)
          {
            deleteCommentUp(this.userId, commentId);
            this.$set(this.up,commentId,this.up[commentId]-1);
            this.existUp(commentId,index);
            this.notify('取消点赞成功');
          }
          else {
            let params = new URLSearchParams();
            params.append('userId', this.userId);
            params.append('commentId', commentId);
            addCommentUp(params);
            this.$set(this.up,commentId,this.up[commentId]+1);
            this.existUp(commentId,index);
            this.notify('点赞成功');
          }
        });
      }
    },
  }
}
</script>

<style scoped lang = "scss">
@import "../assets/css/comment.scss";
</style>
