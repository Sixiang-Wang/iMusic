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
              <li class="up" ref="up" @click="handleUp(item.id, item.up, index)">
                <svg class="icon">
                  <use xlink:href="#icon-zan"></use>

                </svg>
              </li>
            </ul>

          </div>

        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import {mixin} from "../mixins";
import {
  addCommentUp,
  deleteCommentUp,
  existCommentUp,
  getCommentOfSong,
  getCommentOfSongList,
  getUserOfId,
  setComment, setLike
} from "../api";
import {mapGetters} from "vuex";
import testIcon from '../assets/icon/deleteIcon.vue'
export default {
  name: 'comment',
  mixins: [mixin],
  props: ['type'],
  components:{testIcon},
  data() {
    return {
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
    getMsg() {
      this.commentList.forEach(item =>
      {
        getUserOfId(item.userId).then(res =>
        {
          this.avatars[item.id] = res.profilePicture;
          this.usernames[item.id] = res.username;
          this.$forceUpdate();
        });
      });
    },
    handleUp(commentId, up, index) {
      if (!this.loginIn)
      {
        this.notify('请先登录');
      }
      else
      {
        existCommentUp(this.userId, commentId).then(res =>
        {
          if (res)
          {
            deleteCommentUp(this.userId, commentId);
            this.up[commentId]--;
            this.$refs.up[index].children[0].classList.remove('active');
            this.notify('取消点赞成功');
          }
          else {
            let params = new URLSearchParams();
            params.append('userId', this.userId);
            params.append('commentId', commentId);
            addCommentUp(params);
            this.up[commentId]++;
            this.$refs.up[index].children[0].classList.add('active');
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
