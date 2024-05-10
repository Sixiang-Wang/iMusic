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
    </ul>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import {navMsg} from '../assets/data/header'
export default {
  name: 'the-header',
  data () {
    return {
      navMsg: [], // 导航栏
      keywords: '' // 搜索关键字
    }
  },
  created () {
    this.navMsg = navMsg
  },
  computed: {
    ...mapGetters([
      'activeName'
    ])
  },
  methods: {
    goHome () {
      this.$store.commit('setActive-Name', '首页')
      this.$router.push({path: '/'})
    },
    goPage (path, name) {
      this.$store.commit('setActiveName', name)
      this.$router.push({path: path})
    },
    goSearch () {
      this.$router.push({path: '/search', query: {keywords: this.keywords}})
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/the-header.scss';
</style>
