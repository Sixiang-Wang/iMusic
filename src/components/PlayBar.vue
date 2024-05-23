<template>
  <div class="play-bar">
    <div class="kongjian">
<!--      上一首-->
      <div class="item" @click="prev">
        <svg class="icon">
          <use xlink:href="#icon-ziyuanldpi"></use>
        </svg>
      </div>
      <!-- 播放-->
      <div class="item" @click="togglePlay">
        <svg class="icon">
          <use :xlink:href = "playButtonUrl"></use>
        </svg>
      </div>
      <!--      下一首-->
      <div class="item" @click="next">
        <svg class="icon">
          <use xlink:href="#icon-ziyuanldpi1"></use>
        </svg>
      </div>
      <!--    歌曲图片-->
      <div class="item-img" @click="toLyric">
        <img :src="picUrl" />
      </div>
      <!--    播放进度-->
      <div class="playing-speed">
<!--        播放开始时间-->
        <div class="current-time">{{nowTime}}</div>
        <div class="progress-box">
          <div class="item-song-title">
            <div>{{this.title}}</div>
            <div>{{this.artist}}</div>
          </div>
          <div ref="progress" class="progress" @mousemove="mousemove">
<!--            进度条-->
            <div ref="bg" class="bg" @click="updatemove">
              <div ref="curProgress" class="cur-progress" :style="{width: curLength + '%'}"></div>
            </div>
<!--            拖动点-->
            <div ref="idot" class="idot" :style="{left: curLength -1 + '%' }" @mousedown="mousedown" @mouseup="mouseup"></div>
          </div>
        </div>
<!--        播放结束时间-->
        <div class="left-time">{{songTime}}</div>
<!--        音量-->
        <div class="item item-volume">
          <svg v-if="volume === 0" class="icon">
            <use xlink:href="#icon-yinliangjingyinheix"></use>
          </svg>
          <svg v-else class="icon">
            <use xlink:href="#icon-yinliang1"></use>
          </svg>
          <el-slider class="volume" v-model="volume" :vertical="true"></el-slider>
        </div>
<!--        收藏-->
        <div class="item">
          <svg class="icon">
            <use xlink:href="#icon-xihuan-shi"></use>
          </svg>
        </div>
<!--        下载-->
        <div class="item" @click="download">
          <svg class="icon">
            <use xlink:href="#icon-xiazai"></use>
          </svg>
        </div>
<!--        当前播放的歌曲列表-->
        <div class="item" @click="changeAside">
          <svg class="icon">
            <use xlink:href="#icon-liebiao"></use>
          </svg>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import {download} from "../api/index";

export default {
  name: 'play-bar',

  data () {
    return {
      nowTime: '00:00', // 当前播放进度的时间
      songTime: '00:00', // 当前歌曲总时间
      curLength: 0, // 进度条的位置
      progressLength: 0, // 进度条的总长度
      mouseStartX: 0, // 拖拽开始位置
      tag: false, // 拖拽开始结束的标志,当开始拖拽值为true
      volume: 50 // 音量默认为50
    }
  },
  computed: {
    ...mapGetters([
      'id', // 歌曲id
      'url', // 歌曲地址
      'isPlay', // 播放状态
      'playButtonUrl', // 播放状态图标
      'picUrl', // 正在播放的音乐的图片
      'title', // 歌名
      'artist', // 歌手名
      'duration', // 歌曲总时长
      'curTime', // 当前音乐的播放位置
      'showAside', // 是否显示播放中的歌曲列表
      'listIndex', // 当前歌曲在歌单中的位置
      'listOfSongs', // 当前歌单列表
      'autoNext' // 自动播放下一首
    ])
  },
  mounted () {
    this.progressLength = this.$refs.progress.getBoundingClientRect().width
    document.querySelector('.item-volume').addEventListener('click', function (e) {
      document.querySelector('.volume').classList.add('show-volume')
      e.stopPropagation()
    }, false)
    document.querySelector('.volume').addEventListener('click', function (e) {
      e.stopPropagation()
    }, false)
    document.addEventListener('click', function () {
      document.querySelector('.volume').classList.remove('show-volume')
    }, false)
  },
  watch: {
    // 切换播放状态的图标
    isPlay () {
      if (this.isPlay) {
        this.$store.commit('setPlayButtonUrl', '#icon-zanting')
      } else {
        this.$store.commit('setPlayButtonUrl', '#icon-bofang')
      }
    },
    curTime () {
      this.nowTime = this.formatSeconds(this.curTime)
      this.songTime = this.formatSeconds(this.duration)
      this.curLength = (this.curTime / this.duration) * 100
    },
    // 音量变化
    volume () {
      this.$store.commit('setVolume', this.volume / 100)
    },
    // 自动播放下一首
    autoNext () {
      this.next()
    }
  },
  methods: {
    // 控制音乐播放与暂停
    togglePlay () {
      if (this.isPlay) {
        this.$store.commit('setIsPlay', false)
      } else {
        this.$store.commit('setIsPlay', true)
      }
    },
    // 解析时间
    formatSeconds (value) {
      let theTime = parseInt(value)
      let result = ''
      let hour = Math.floor(theTime / 3600)
      let minute = Math.floor((theTime / 60) % 60)
      let second = Math.floor(theTime % 60)
      if (hour > 0) {
        if (hour < 10) {
          result = '0' + hour + ':'
        } else {
          result = hour + ':'
        }
      }
      if (minute > 0) {
        if (minute < 10) {
          result += '0' + minute + ':'
        } else {
          result += minute + ':'
        }
      } else {
        result += '00:'
      }
      if (second > 0) {
        if (second < 10) {
          result += '0' + second
        } else {
          result += second
        }
      } else {
        result += '00'
      }
      return result
    },
    // 拖拽开始
    mousedown (e) {
      this.mouseStartX = e.clientX
      this.tag = true
    },
    // 拖拽结束
    mouseup () {
      this.tag = false
    },
    // 拖拽中
    mousemove (e) {
      if (!this.duration) {
        // console.log("开发:现无歌曲，无法拖动");
        return false
      }
      if (this.tag) {
        let movementX = e.clientX - this.mouseStartX // 移动的距离
        let curLength = this.$refs.curProgress.getBoundingClientRect().width
        let newPercent = ((movementX + curLength) / this.progressLength) * 100
        if (newPercent > 100) {
          newPercent = 100
        } else if (newPercent < 0) {
          newPercent = 0
        }
        this.curLength = newPercent
        this.mouseStartX = e.clientX
        this.changeTime(newPercent)
      }
    },
    // 点击播放条切换播放进度
    updatemove (e) {
      if (!this.tag) {
        // 进度条的左侧坐标
        let curLength = this.$refs.bg.offsetLeft
        let newPercent = (e.clientX - curLength) / this.progressLength * 100
        if (newPercent > 100) {
          newPercent = 100
        } else if (newPercent < 0) {
          newPercent = 0
        }
        this.curLength = newPercent
        this.changeTime(newPercent)
      }
    },
    // 更改歌曲进度
    changeTime (percent) {
      let newCurTime = percent * 0.01 * this.duration
      this.$store.commit('setChangeTime', newCurTime)
    },
    // 显示播放中的歌曲列表
    changeAside () {
      this.$store.commit('setShowAside', true)
    },
    // 上一首
    prev () {
      // 当前处于不可能状态或者只有一首音乐
      if (this.listIndex !== -1 && this.listOfSongs.length > 1) {
        if (this.listIndex > 0) {
          // 不是第一首音乐
          this.$store.commit('setListIndex', this.listIndex - 1)
        } else {
          // 切换到倒数第一首
          this.$store.commit('setListIndex', this.listOfSongs.length - 1)
        }
        this.toplay(this.listOfSongs[this.listIndex].url)
      }
    },
    // 下一首
    next () {
      // 当前处于不可能状态或者只有一首音乐
      if (this.listIndex !== -1 && this.listOfSongs.length > 1) {
        if (this.listIndex < this.listOfSongs.length - 1) {
          // 不是第一首音乐
          this.$store.commit('setListIndex', this.listIndex + 1)
        } else {
          // 切换到第一首
          this.$store.commit('setListIndex', 0)
        }
        this.toplay(this.listOfSongs[this.listIndex].url)
      }
    },
    // 播放音乐
    toplay: function (url) {
      if (url && url !== this.url) {
        this.$store.commit('setId', this.listOfSongs[this.listIndex].id)
        this.$store.commit('setUrl', this.$store.configure.HOST + url)
        this.$store.commit('setPicUrl', this.$store.configure.HOST + this.listOfSongs[this.listIndex].pic)
        this.$store.commit('setTitle', this.replaceFName(this.listOfSongs[this.listIndex].name))
        this.$store.commit('setArtist', this.replaceLName(this.listOfSongs[this.listIndex].name))
        console.log(this.listOfSongs[this.listIndex].lyric)
        console.log('PlayBar')
        this.$store.commit('setLyric', this.parseLyric(this.listOfSongs[this.listIndex].lyric))
      }
    },
    // 获取名字前半部分 -- 歌手名
    replaceLName (str) {
      let arr = str.split('-')
      return arr[0]
    },
    // 获取名字后半部分-- 歌名
    replaceFName (str) {
      let arr = str.split('-')
      return arr[1]
    },
    parseLyric (text) {
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
    },
    // 转向歌词页面
    toLyric () {
      this.$router.push({path: '/lyric'})
    },
    // 下载音乐
    download() {
      console.log("download");
      download(this.url)
        .then(res=>{
          let content = res.data;
          let eleLink= document.createElement('a');
          // 可考虑添加下载音乐格式的选项
          eleLink.download = `${this.artist}-${this.title}.mp3`;
          eleLink.style.dislpay = 'none';
          // 把字符内容转换成blob地址
          let blob = new Blob([content]);
          eleLink.href = URL.createObjectURL(blob);
          // 把链接地址加到document里
          document.body.appendChild(eleLink);
          // 触发点击
          eleLink.click();
          // 然后移除这个新加的控件
          document.body.removeChild(eleLink);

        })
        .catch(err => {
          console.log(err);
        })
    }

  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/play-bar.scss';
</style>
