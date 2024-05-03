<template>
  <div class="song-audio">
    <audio ref="player"
           :src = "url"
           controls = "controls"
           preload= "true"
           @canplay="startPlay"
           @ended="ended"
           ></audio>
  </div>
</template>

<script>
import {mapGetters} from "vuex";

export default {
  name: 'song-audio',
  computed: {
    ...mapGetters([
      'id',
      'url',
      'isPlay'
    ])
  },
  watch:{
    // 监听播放还是暂停
    isPlay: function (){
      this.togglePlay();
    }
  },
  methods:{
    // 获取链接后准备播放
    startPlay(){
      let player = this.$refs.player;
      // 开始播放
      player.play();
    },
    ended(){
      this.isPlay = false
    },
    // 开始 暂停
    togglePlay(){
      let player = this.$refs.player;
      if(this.isPlay){
        player.play();
      }
      else{
        player.pause();
      }
    }
  }
}
</script>

<style>
  .song-audio{
    display: inline;
  }
</style>
