<template>
  <div class="my-music">
    <div class="album-header">
      <div class="album-img">
        <img :src="attachImageUrl(avatar)" alt="">
      </div>
      <div class="basic-info-under-avatar">
        {{ name }}
        <br>
      </div>
      <div class="album-menu">
        <ul>
          <li :class="{current: item.name === activeName}" v-for="item in navMsg" :key="item.path" @click="goPage(item.path, item.name)">
            {{item.name}}
          </li>
        </ul>
      </div>
    </div>
    <div>
      <router-view></router-view>
<!--    </div>-->
<!--    <div class="album-content">-->
<!--      <div class="album-title">-->
<!--        个性签名: {{introduction}}-->
<!--      </div>-->
<!--      <div class="songs-body">-->

<!--      </div>-->
    </div>
  </div>
</template>

<script>
import {mixin} from '../../mixins';
import {mapGetters} from "vuex";
import {getUserOfId} from "../../api";
import AlbumContent from "../../components/AlbumContent.vue";
import {navMsg} from "../../assets/data/menuOfMyMusic";
export default {
  name: 'myMusic',
  mixins : [mixin],
  components:{
    AlbumContent
  },
  data() {
    return {
      navMsg: [],     //菜单栏
      activeName: '我的收藏',
      avatar : '',    // 用户头像
      username : '',     // 用户名
      name : '',        // 昵称
      userSex : '' ,      // 性别
      birth: '',          //  生日
      location: '',       //  故乡
      introduction: '' ,   //  个性签名
      collection: [] ,     // 收藏的歌曲列表
      // collectList : [],     // 收藏的歌曲列表（带歌曲详情）
    }
  },
  computed:{
    ...mapGetters([
      'listOfSongs',    // 当前的播放列表
      'userId'          // 当前登录用户id
    ])
  },
  created() {
    this.navMsg = navMsg;
  },
  mounted() {
    this.getMsg(this.userId);
    this.getCollection(this.userId);
  },
  methods:{
    goPage(path, name) {
      this.activeName = name;
      this.$router.push({path: path})
    },
    getMsg(userId){
      // console.log('userid:'+this.userId);
      getUserOfId(userId)
        .then(res => {
          this.avatar = res.profilePicture;
          this.username = res.username;
          this.name = res.name;
          if(res.sex === 0){
            this.userSex = '女';
          }
          else if(res.sex === 1){
            this.userSex = '男';
          }
          this.birth = this.getBirth(res.birth);
          this.location = res.location;
          this.introduction = res.introduction;

        })
        .catch(err => {
          console.log(err);
        })
    },


  }
}
</script>

<style lang="scss" scoped>
@import '../../assets/css/my-music';
</style>
<style lang="scss" scoped>
@import "../../assets/css/the-header";
</style>
