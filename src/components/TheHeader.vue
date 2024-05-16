<template>
  <div class="the-header">
    <div class="header-logo" @click="goHome">
      <svg class="icon">
        <use xlink:href="#icon-erji"></use>
      </svg>
      <span>music</span>
    </div>
    <ul class="navbar">
      <li :class="{active: item.name === activeName}" v-for="item in navMsg" :key="item.path" @click="goPage(item.path, item.name)">
        {{item.name}}
      </li>
      <li>
        <div class="header-search">
          <input type="text" placeholder="搜索音乐" @keyup.enter="goSearch()" v-model="keywords">
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
    </div>
  </div>
</template>

<script>
import {mapGetters} from 'vuex';
import {navMsg , loginMsg, menuList} from '../assets/data/header';

export default {
  name: 'the-header',
  data () {
    return {
      navMsg: [], // 左侧导航栏
      keywords: '', // 搜索关键字
      loginMsg : [],   // 右侧导航栏
      menuList : []     // 用户下拉菜单栏
    }
  },
  created () {
    this.navMsg = navMsg
    this.loginMsg = loginMsg
    this.menuList = menuList
  },
  computed: {
    ...mapGetters([
      'activeName',
      'loginIn',
      'avatar'
    ])
  },
  mounted() {
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
  methods: {
    goHome () {
      this.$store.commit('setActiveName', '首页')
      this.$router.push({path: '/'})
    },
    goPage (path, name) {
      if(!this.loginIn && path==='/my-music'){
        this.$notify({title: '请先登录' , type: 'success'});
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
      if(srcurl){
        return this.$store.state.configure.HOST + srcurl;
      }
      else{
        return '../assets/img/user.jpg';
      }
    },
    goMenuList(path) {
      if(path === 0){
        this.$store.commit('setLoginIn' , false);
        this.$store.commit('setIsActive' , false);
        this.$router.go(0);
      }
      else{
        this.$router.push({path : path});git
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/the-header.scss';
</style>
