<template>
  <div class = "basicInfo">
    <ul>
      <li v-for="item in info"></li>
    </ul>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import {getUserOfId} from "../api";
import {mixin} from "../mixins";

export default {
  data() {
    return {
      activeName: '个人信息',
      username: '',     // 用户名
      name: '',        // 昵称
      userSex: '',      // 性别
      birth: '',          //  生日
      location: '',       //  故乡
      introduction: '',   //  个性签名
      info:[],
    }
  },
  mixins: [mixin],
  computed: {
    ...mapGetters([
      'listOfSongs',    // 当前的播放列表
      'userId'          // 当前登录用户id
    ])
  },
  mounted() {
    this.getMsg(this.userId);
  },
  methods: {
    getMsg(userId) {
      // console.log('userid:'+this.userId);
      getUserOfId(userId)
        .then(res =>
        {
          this.username = res.username;
          this.info.push({title:'用户名',content:this.username});
          this.name = res.name;
          this.info.push({title:'昵称',content:this.name});
          if (res.sex === 0) {
            this.userSex = '女';
          }
          else if (res.sex === 1) {
            this.userSex = '男';
          }
          this.info.push({title:'性别',content:this.userSex});
          this.birth = this.getBirth(res.birth);
          this.info.push({title:'生日',content:this.birth});
          this.location = res.location;
          this.info.push({title:'故乡',content:this.location});
          this.introduction = res.introduction;
        })
        .catch(err =>
        {
          console.log(err);
        })
    },
  }
}
</script>

<style scoped lang = "scss">
@import "../assets/css/personal.scss";
</style>
