<template>
  <transition name="slide-fade">
    <div class="the-aside" v-if="showAside">
      <h2 class="title">播放列表</h2>

      <div class="menus">
        <ul v-if="playList.length">
          <li v-for="(item,index) in playList" :key="index" :class="{'is-play': id === item.id}"
              @click="toplay(item.id, item.url, item.pic, item.index, item.name, item.lyric)">
            <img
              :src="getUrl(item.pic)"
              alt="歌曲封面"
              :class="{ 'song-cover': true, 'is-play-song-cover': id === item.id }"
            />
            {{ replaceFName(item.name) }}
          </li>
        </ul>
        <p v-else class="no-music">当前播放列表无音乐</p>
      </div>

    </div>
  </transition>

</template>

<script>
import {mapGetters} from 'vuex'
import {getCollectOfUserId} from "../api";
import {mixin} from "../mixins";
import {url} from "nightwatch/examples/pages/home";

export default {
  name: 'the-aside',
  mixins: [mixin],
  computed: {
    ...mapGetters([
      'showAside', // 是否显示播放中的歌曲列表
      'playList', // 当前歌曲列表
      'id',          // 播放中的音乐id
      'loginIn',   // 用户是否已登录
      'userId',    // 当前登录用户的id
      'isActive'    // 当前播放歌曲是否已收藏
    ])
  },

  mounted() {
    let _this = this
    document.addEventListener('click', function () {
      _this.$store.commit('setShowAside', false)
    }, true)

    if (!this.playList) {
      this.$store.commit("setPlayList", [])
    }

  },

  methods: {
    url() {
      return url
    },
    // 获取名字前半部分 -- 歌手名
    replaceLName(str) {
      let arr = str.split('-')
      return arr[0]
    },
    // 获取名字后半部分-- 歌名
    replaceFName(str) {
      let arr = str.split('-')
      return arr[1]
    },

    getUrl(str) {
      return this.$store.state.configure.HOST + str
    },

    // 播放
    toplay: function (id, url, pic, index, name, lyric) {
      this.$store.commit('setId', id)
      this.$store.commit('setUrl', this.$store.state.configure.HOST + url)
      this.$store.commit('setPicUrl', this.$store.state.configure.HOST + pic)
      this.$store.commit('setListIndex', index)
      this.$store.commit('setTitle', this.replaceFName(name))
      this.$store.commit('setArtist', this.replaceLName(name))
      this.$store.commit('setLyric', this.parseLyric(lyric))

      if (this.loginIn) {
        getCollectOfUserId(this.userId)
          .then(res => {
            // 日后优化
            for (let item of res) {
              if (item.songId === id) {
                this.$store.commit('setIsActive', true);
                break;
              }
            }
          })
      }
    },
    // 解析歌词
    parseLyric(text) {
      let lines = text.split('\n') // 将歌词按行分解成数组
      let pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})]/g // 时间格式的正则表达式
      let result = []
      // 对于歌词格式不对的直接返回
      if (!(/\[.+]/.test(text))) {
        return [[0, text]]
      }
      // 去掉前面不正确的行
      while (lines.length !== 0 && !pattern.test(lines[0])) {
        lines = lines.slice(1)
      }
      // 遍历每一行，形成一个带着两个元素的数组，第一个元素是以秒为计算单位的时间，第二个元素是歌词
      for (let item of lines) {
        let time = item.match(pattern) // 前面的时间段
        let value = item.replace(pattern, '') // 存后面的歌词
        for (let item1 of time) {
          let t = item1.slice(1, -1).split(':') // 取出时间，换算成数组
          if (value !== '') {
            result.push([parseInt(t[0], 10) * 60 + parseInt(t[1]), value])
          }
        }
      }
      // 按照第一个元素 -- 时间 -- 排序
      result.sort(function (a, b) {
        return a[0] - b[0]
      })
      return result
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../assets/css/the-aside.scss";
</style>
