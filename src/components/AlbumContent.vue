<template>
  <div class = "content">
    <h1 class = "title">
      <slot name = "title"></slot>
    </h1>
    <ul>
      <li>
        <div class = "song-item">
          <span class = "item-index"></span>
          <span class = "item-title">歌曲名</span>
          <span class = "item-name">歌手</span>
          <span class = "item-intro">专辑</span>
        </div>
      </li>
      <li v-for = "(item,index) in songList" :key = "index">
        {{ isCollected(item) }}
        <div class = "song-item">
          <span class = "item-index">
            <span @click = "toplay(item.id,item.url,item.pic,index,item.name,item.lyric)">
              {{ index + 1 }}
            </span>
          </span>
          <span class = "item-title">
              <span @click = "toplay(item.id,item.url,item.pic,index,item.name,item.lyric)">
                {{ replaceFName(item.name) }}
              </span>
          </span>
          <span class = "item-name">
            <span @click = "goSinger(replaceLName(item.name))">
              {{ replaceLName(item.name) }}
            </span>
          </span>
          <span class = "item-intro">
            <span>
              {{ item.introduction }}
            </span>
          </span>
          <span @click = "showSelect($event,index,item.id)" ref="add" >
            <add-icon></add-icon>
          </span>
          <span @click = "handleCollect(item)">
            <svg :class = "{active: item.collection}" class = "icon">
              <use xlink:href = "#icon-xihuan-shi"></use>
            </svg>
          </span>
        </div>
      </li>
    </ul>
    <div class = "select-song-list" v-bind:style="selectPosition" ref="select" v-if = "isShowSelect">
      <div class = "select-item"
           v-for = "(item, index) in this.selectData" :key = "index"
           @click="addToSongList(item.id)" >
        <span class = "img-container">
          <img class = "img" :src = "attachImageUrl(item.pic)" alt = "">
        </span>
        <div class="select-title">{{item.title}}</div>
      </div>
    </div>
  </div>
</template>

<script>
import {mixin} from '../mixins'
import {
  deleteCollectSong,
  getOneSingerByName,
  setCollect,
  existCollectSong,
  selectSongListByUserId,
  addListSong, selectByPrimaryKey
} from "../api";
import {mapGetters} from "vuex";
import AddIcon from "../assets/icon/addIcon.vue";

export default {
  data() {
    return {
      isMySongList: false,
      isShowSelect: false,
      addIndex: '',
      selectSongId: '',
      selectData: [],
      selectPosition: {
        position: 'fixed',
        top: 0,
        left: 0,
      }
    }
  },
  name: 'album-content',
  components: {AddIcon},
  mixins: [mixin],
  props: [
    'songList'
  ],
  computed: {
    ...mapGetters([
      'loginIn',
      'userId'
    ])
  },
  mounted() {
    if (this.loginIn)
    {
      selectSongListByUserId(this.userId).then(res =>
      {
        res.forEach(item =>
        {
          this.selectData.push({id: item.id,pic: item.pic, title: item.title})
        })
      })
      selectByPrimaryKey(this.$route.params.id).then(res => {
        if (res.userId === this.userId)
        {
          this.isMySongList = true;
        }
      })
    }
  },
  methods: {
    isCollected(item) {
      if (!this.loginIn)
      {
        this.$set(item, 'collection', false);
      }
      else {
        existCollectSong(this.userId, item.id).then(res =>
        {
          this.$set(item, 'collection', res);
        })
      }
    },
    handleCollect(item) {
      let songId = item.id;
      if (this.loginIn) {
        let params = new URLSearchParams();
        params.append('userId', this.userId);
        params.append('type', 0);
        params.append('songId', songId);
        setCollect(params)
          .then(res =>
          {
            if (res.code === 1) {
              item.collection = true;
              this.notify('收藏成功', 'success');
            }
            else if (res.code === 2) {
              deleteCollectSong(this.userId, songId);
              item.collection = false;
              this.notify('取消成功', 'success');
            }
            else {
              this.notify('收藏失败', 'error');
            }
          })
      }
      else {
        this.notify('请先进行登录', 'warning');
      }
    },
    goSinger(name) {
      getOneSingerByName(name).then(res =>
      {
        this.$store.commit('setTempList', res);
        this.$router.push({path: `/singer-album/${res.id}`});
      })
    },
    addToSongList(songListId) {

        let params = new URLSearchParams();
        params.append('songId',this.selectSongId);
        params.append('songListId', songListId);
        addListSong(params).then(res => {
          if (res.code)
          {
            this.hideSelectBox();
            this.notify('添加成功','success');
          }
        })
    },
    showSelect(event,index,songId) {
      if (!this.loginIn)
      {
        this.notify('请先登录','warning');
      }
      else
      {
        this.isShowSelect = true;
        this.addIndex = index;
        this.selectSongId = songId;
        const svgRect = event.target.getBoundingClientRect();
        this.selectPosition.top = svgRect.top + 20 + 'px';
        this.selectPosition.left = svgRect.right + 20 + 'px';
        window.addEventListener('click', this.handleOutsideClick);
      }
    },
    handleOutsideClick(event) {
      if (!this.$refs.add[this.addIndex].contains(event.target) && !this.$refs.select.contains(event.target)) {
        this.hideSelectBox();
      }
    },
    hideSelectBox() {
      this.isShowSelect = false;
      this.addIndex = '';
      this.selectSongId = '';
      window.removeEventListener('click', this.handleOutsideClick);
    }
  },
}
</script>

<style lang = "scss" scoped>
@import "../assets/css/album-content.scss";
</style>
