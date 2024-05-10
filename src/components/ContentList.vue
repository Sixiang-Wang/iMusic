<template>
  <div class="content-list">
    <ul class="section-content">
      <li class="content-item" v-for="(item,index) in contentList" :key="index">
          <div class="kuo" @click="goAlbum(item)">
            <img class="item-img" :src="attachImageUrl(item.pic)">
            <div class="mask">
              <svg class="icon">
                <use xlink:href="#icon-bofang"></use>
              </svg>
            </div>
          </div>
        <p class="item-name">{{item.name || item.title}}</p>
      </li>
    </ul>
  </div>
</template>

<script>
import {mixin} from '../mixins/index'

export default {
  name: 'content-list',
  mixins: [mixin],
  props: ['contentList'],
  methods: {
    goAlbum(item){
      this.$store.commit('setTempList',item);
      if (item.name)
      {
        this.$router.push({path:`singer-album/${item.id}`});
      }
      else
      {
        this.$router.push({path:`song-list-album/${item.id}`});
      }
    }
  }
}

</script>

<style lang="scss" scoped>
@import '../assets/css/content-list.scss';
</style>
