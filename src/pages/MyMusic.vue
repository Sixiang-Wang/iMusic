<template>
  <div class="my-music">
    <div class="album-slide">
      <div class="album-img">
        <img :src="attachImageUrl(avatar)">
      </div>
      <ul class="album-info">
        <li>用户名: {{username}}</li>
        <li>昵称: {{name}}</li>
        <li>性别: {{userSex}}</li>
        <li>生日: {{birth}}</li>
        <li>故乡: {{location}}</li>
      </ul>
    </div>
    <div class="album-content">
      <div class="album-title">
        个性签名: {{introduction}}
      </div>
    </div>
  </div>
</template>

<script>
import {mixin} from '../mixins';
import {mapGetters} from "vuex";
import {getUserOfId , getCollectOfUserId , songOfSongId} from "../api";
import AlbumContent from "../components/AlbumContent.vue";
export default {
  name: 'myMusic',
  mixins : [mixin],
  components:{
    AlbumContent
  },
  data() {
    return {
      avatar : '',    // 用户头像
      username : '',     // 用户名
      name : '',        // 昵称
      userSex : '' ,      // 性别
      birth: '',          //  生日
      location: '',       //  故乡
      introduction: '' ,   //  个性签名
      collection: [] ,     // 收藏的歌曲列表
      collectList : [],     // 收藏的歌曲列表（带歌曲详情）
    }
  },
  computed:{
    ...mapGetters([
      'listOfSongs',    // 当前的播放列表
      'userId'          // 当前登录用户id
    ])
  },
  mounted() {
    this.getMsg(this.userId);
  },
  methods:{
    getMsg(userId){
      getUserOfId(userId)
        .then(res => {
          this.avatar = res.avatar;
          this.username = res.username;
          this.name = res.name;
          if(res.sex === 0){
            this.userSex = '女';
          }
          else if(res.sex === 1){
            this.userSex = '男';
          }
          this.birth = this.attachBirth(res.birth);
          this.location = res.location;
          this.introduction = res.introduction;

        })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/my-music.scss';
</style>
