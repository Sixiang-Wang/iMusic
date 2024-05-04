<template>
  <transition name="slide-fade">
    <div class="the-aside" v-if="showAside">
      <h2 class="title">播放列表</h2>
      <ul class="menus">
        <li v-for="{item,index} in listOfSongs" :key="index" :class="{'is-play' :id === item.id}"
        @click="toplay(item.id,item.url,item.pic,item.index,item.name,item.lyric)">
          {{replaceFName(item.name)}}
        </li>
      </ul>
    </div>
  </transition>

</template>


<script>
import {mapGetters} from "vuex";
export default {
  name: 'the-aside',
  computed: {
    ...mapGetters([
      'showAside',      // 是否显示播放中的歌曲列表
      'listOfSongs',    // 当前歌曲列表
      'id',             // 播放中的音乐id
    ])
  },
  mounted() {
    let _this = this;
    document.addEventListener('click',function (){
      _this.$store.commit('setShowAside',false)
    },true);
  },
  methods:{
    // 获取名字前半部分 -- 歌手名
    replaceLName(str){
      let arr = str.split('-');
      return arr[0];
    },
    // 获取名字后半部分-- 歌名
    replaceFName(str){
      let arr = str.split('-');
      return arr[1];
    },
    // 播放
    toplay: function (id,url,pic,index,name,lyric){
      this.$store.commit('setId',id);
      this.$store.commit('setUrl' ,this.$store.configure + url);
      this.$store.commit('setPicUrl',this.$store.configure + pic);
      this.$store.commit('setListIndex',index);
      this.$store.commit('setTitle',this.replaceFName(name));
      this.$store.commit('setArtist',this.replaceLName(name));
      this.$store.commit('setLyric',lyric);
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../assets/css/the-aside.scss";
</style>
