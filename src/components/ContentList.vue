<template>
  <div class="content-list">
    <ul class="section-content">
      <li class="content-item" v-for="(item,index) in contentList" :key="index">
        <div class="kuo" @click="goAlbum(item)">
          <img class="item-img" :src="attachImageUrl(item.pic || item.profilePicture)" alt="">
          <div class="mask">
            <svg class="icon">
              <use xlink:href="#icon-bofang"></use>
            </svg>
          </div>
        </div>
        <p class="item-name">{{ item.name || item.title }}</p>
      </li>
    </ul>
  </div>
</template>

<script>
import {mixin} from '../mixins/index'

export default {
  type: '',
  name: 'content-list',
  mixins: [mixin],
  props: ['contentList'],
  methods: {
    goAlbum(item) {
      this.$store.commit('setTempList', item);
      console.log("res:", item);
      this.notify(item);
      if (item.lyric) {
        this.$router.push({path: `/song-album/${item.id}`});
      } else if (item.title) {
        this.$router.push({path: `/song-list-album/${item.id}`});
      } else {
        this.$router.push({path: `/singer-album/${item.id}`});
      }
    },
    // 获取名字后半部分-- 歌名
    replaceFName(str) {
      let arr = str.split('-')
      return arr[1]
    },

  }
}

</script>

<style lang="scss" scoped>
@import '../assets/css/content-list.scss';
</style>
