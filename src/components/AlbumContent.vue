<template>
  <div >
    歌曲列表
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import {mixin} from "../mixins";

export default {
  name: 'album-content',
  mixins: [mixin],
  props:[
    'songList'
  ]
}
</script>

<style lang="scss" scoped>
@import "../assets/css/album-content.scss";
</style>
