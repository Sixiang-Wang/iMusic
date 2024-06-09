<template>
  <div class="the-header">
    <div class="header-logo" @click="goHome()">
      <svg class="icon" :style="{'margin-left':marginleftPx + 'px'}" style="margin-top: -5px;scale: 1.1">
        <i-music></i-music>
      </svg>
      <span style="margin-left: 10px">iMusic</span>
    </div>

    <ul class="navbar">
      <li :class="{active: item.name === activeName}" v-for="item in navMsg" :key="item.path" @click="goPage(item.path, item.name)">
        {{item.name}}
      </li>
      <li>
        <div class="header-search">
          <input type="text"  placeholder="搜索音乐" @keyup.enter="goSearch()" v-model="keywords">
            <div class="search-btn" @click="goSearch()">
              <svg class="icon">
                <use xlink:href="#icon-sousuo"></use>
              </svg>
            </div>
        </div>
      </li>
      <li v-show="!loginIn" :class="{active: item.name === activeName}" v-for="item in loginMsg" :key="item.path" @click="goPage(item.path, item.name)">
        {{item.name}}
      </li>
    </ul>
    <div class="header-right" v-show="loginIn">
      <div id="user">
        <img :src = 'attachImageUrl(avatar)'>
      </div>
      <ul class="menu">
        <li v-for="(item,index) in menuList" :key="index" @click="goMenuList(item.path)">{{item.name}}</li>
      </ul>
      <div class="container" @click="goMessage">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="message-icon" viewBox="0 0 16 16" >
          <path d="M0 4a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V4Zm2-1a1 1 0 0 0-1 1v.217l7 4.2 7-4.2V4a1 1 0 0 0-1-1H2Zm13 2.383-4.708 2.825L15 11.105V5.383Zm-.034 6.876-5.64-3.471L8 9.583l-1.326-.795-5.64 3.47A1 1 0 0 0 2 13h12a1 1 0 0 0 .966-.741ZM1 11.105l4.708-2.897L1 5.383v5.722Z"/>
        </svg>
        <span class="message-text">消息</span>
      </div>
      <div v-if="messageUnreadNum !== 0" class="red-dot">
        {{ messageUnreadNum }}
      </div>
    </div>
  </div>
</template>

<script>
import {mapGetters} from 'vuex';
import {navMsg , loginMsg, menuList} from '../assets/data/header';
import {getMessageUnreadNum, logout} from "../api";
import IMusic from "../assets/icon/iMusic.vue";

export default {
  name: 'the-header',
  components: {IMusic},
  data () {
    return {
      navMsg: [], // 左侧导航栏
      keywords: '', // 搜索关键字
      loginMsg : [],   // 右侧导航栏
      menuList : [],     // 用户下拉菜单栏
      messageUnreadNum : 0,//未读消息条数
      marginleftPx:0
    }
  },
  created () {
    this.navMsg = navMsg;
    this.loginMsg = loginMsg;
    this.menuList = menuList;
  },
  computed: {
    ...mapGetters([
      'activeName',
      'loginIn',
      'avatar',
      'userId'
    ])
  },
  mounted() {
    this.marginleftPx = window.innerWidth / 3 - 400;
    window.onresize = () => {
      this.marginleftPx = window.innerWidth / 3 - 400;
    };
    document.querySelector('#user').addEventListener('click', function (e){
      document.querySelector('.menu').classList.add("show");
      e.stopPropagation();    // 关键在于阻止冒泡
    }, false);
    document.querySelector('.menu').addEventListener('click', function (e){
      e.stopPropagation();
      //   点击菜单内部时，阻止时间冒泡，这样，点击内部时，菜单不会丢失
    }, false);
    document.addEventListener('click' , function (){
      document.querySelector('.menu').classList.remove('show');
    },false);
  },
  watch: {
    userId(){
      this.getUserMessageUnreadNum(this.userId);
    }
  },
  methods: {
    goHome () {
      this.$store.commit('setActiveName', '首页')
      this.$router.push({path: '/'})
    },
    goPage (path, name) {
      if(!this.loginIn && path==='/my-music'){
        this.$notify({title: '请先进行登录' , type: 'warning'});
      }
      else{
        this.$store.commit('setActiveName', name)
        this.$router.push({path: path})
      }
    },
    goSearch () {
      this.$router.push({path: '/search', query: {keywords: this.keywords}})
    },
    // 获取图片地址
    attachImageUrl (srcurl){
      // console.log(srcurl);
      if(srcurl){
        return this.$store.state.configure.HOST + srcurl;
      }
      else{
        return this.$store.state.configure.HOST + '/img/Pic/default_avatar.jpg';
      }
    },
    goMenuList(path) {
      if(path === 0){
        this.$store.commit('setLoginIn' , false);
        this.$store.commit('setIsActive' , false);
        logout()
        if(this.$route.path !== '/'){
          setTimeout(() => {
            this.$router.push({ path: '/' });
            this.$router.go(0);
          }, 500);
        } // 延迟1秒执行路由跳转
        this.$notify({title : '退出成功' , type : 'success'});

      }
      else{
        this.$router.push({path : path});
      }
    },
    goMessage(){
      this.messageUnreadNum = 0;
      this.$router.push({path : '/message'})
    },
    getUserMessageUnreadNum(userId){
      // console.log(userId);
      if(userId===null){
        return;
      }
      getMessageUnreadNum(userId)
        .then(res => {
          this.messageUnreadNum = res;
          if(res>99){
            this.messageUnreadNum = '99+';
          }
        })
        .catch(error => {
          console.log(error);
        })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/the-header.scss';
</style>
