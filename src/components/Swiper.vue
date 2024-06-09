<template>
  <div class="swiper">
    <el-carousel
      :interval="4000"
      type="card"
      height="280px"
      :autoplay="true"
      :indicator-position="'none'"
      :loop="true"
      class="custom-carousel"
    >
        <el-carousel-item v-for="(item,index) in swiperList" :key="index">
          <img :src="item.picImg"
               alt="item.altText || '轮播图'"
               class="carousel-image"
          />
        </el-carousel-item>
    </el-carousel>
    <div class="custom-indicators">
      <span class="indicator-dot" v-for="(item, index) in swiperList" :key="index" :class="{'active': index === activeIndex}" @click="switchIndicator(index)"></span>
    </div>
  </div>
</template>

<script>
import {swiperList} from '../assets/data/swiper'

export default {
  name: 'Swiper',
  data () {
    return {
      swiperList: []
    }
  },
  created () {
    this.swiperList = swiperList
  }
}

</script>

<style lang="scss" scoped>
  @import "../assets/css/swiper.scss";
</style>
