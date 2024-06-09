<template>
  <div class="my-music">
    <div class="album-header">
      <div class="album-img">
        <img :src="imageUrl" :alt="avatar" v-if="imageUrl"/>
        <img alt="" v-else src="../../assets/img/user.jpg"/>
      </div>
      <div class="basic-info-under-avatar">
        {{ name }}
        <br>
      </div>
      <div class="album-menu">
        <ul>
          <li :class="{current: item.name === activeName}" v-for="item in navMsg" :key="item.path"
              @click="goPage(item.path, item.name)">
            {{ item.name }}
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
  mixins: [mixin],
  components: {
    AlbumContent
  },
  data() {
    return {
      navMsg: [],     //菜单栏
      activeName: '我的收藏',
      avatar: '',    // 用户头像
      username: '',     // 用户名
      name: '',        // 昵称
      userSex: '',      // 性别
      birth: '',          //  生日
      location: '',       //  故乡
      introduction: '',   //  个性签名
      collection: [],     // 收藏的歌曲列表

      imageUrl: null,

    }
  },
  computed: {
    ...mapGetters([
      'userId'          // 当前登录用户id
    ])
  },
  created() {
    this.navMsg = navMsg;
  },
  mounted() {
    this.getMsg(this.userId);
    this.getCollection(this.userId);
    this.initialize();
  },
  methods: {
    goPage(path, name) {
      this.activeName = name;
      this.$router.push({path: path})
    },
    getMsg(userId) {
      // console.log('userid:'+this.userId);
      getUserOfId(userId)
        .then(res => {
          this.avatar = res.profilePicture;
          this.username = res.username;
          this.name = res.name;
          if (res.sex === 0) {
            this.userSex = '女';
          } else if (res.sex === 1) {
            this.userSex = '男';
          }
          this.birth = this.getBirth(res.birth);
          this.location = res.location;
          this.introduction = res.introduction;

          this.attachImageUrl(this.avatar);

        })
        .catch(err => {
          console.log(err);
        })
    },


    async attachImageUrl(srcurl) {
      if (srcurl) {
        const imageUrl = this.$store.state.configure.HOST + srcurl;

        try {
          const response = await fetch(imageUrl, {method: 'HEAD'});
          if (response.ok) {
            this.imageUrl = imageUrl;
          } else {
            this.imageUrl = null; // 图片不存在，设置为null
          }
        } catch (_) {
          this.imageUrl = null; // 请求错误，设置为null
        }
      } else {
        this.imageUrl = null; // 未提供srcurl，设置为null
      }
    },
    async initialize() {
      await this.attachImageUrl(this.avatar);
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
