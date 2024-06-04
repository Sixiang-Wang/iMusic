<template>
  <div class = "song-list-album">
    <div class = "album-slide">
      <div class = "album-img">
        <img :src = 'attachImageUrl(songListAlbum.pic)' alt = "">
      </div>
      <div class = "album-info">
        <h2>简 介:</h2>
        <span>{{ songListAlbum.introduction }}</span>
      </div>
    </div>
    <div class = "album-content">
      <ul>
        <li>
          <div class = "album-title">
            <p>{{ songListAlbum.title }}</p>
          </div>
        </li>
        <li>
          <div class = "collect" @click = "handleCollect()">
            <collect-icon :class = "{'have-collected':this.isCollect==='已收藏'}"
                          style = "margin-left: 25px;">
            </collect-icon>
            <span style = "margin-top: 3px;margin-left: 5px">{{ this.isCollect }}</span>
          </div>
        </li>

      </ul>

      <div class = "album-score">
        <div>
          <h3>评分</h3>
          <div>
            <el-rate v-model = "star" disabled></el-rate>
          </div>
        </div>
        <span>{{ average }}</span>
        <div>
          <h3>评价</h3>
          <div @click = "addRank()">
            <el-rate v-model = "rank" allow-half show-text></el-rate>
          </div>
        </div>
        <span>{{ selfRank }}</span>
      </div>

      <div class = "songs-body">
        <album-content :songList = "listOfSongs">
          <template slot = "title">歌 单</template>
        </album-content>
      </div>
    </div>
    <comment :type = "1"></comment>
  </div>
</template>

<script>
import {mixin} from "../mixins";
import {mapGetters} from "vuex";
import {
  listSongDetail,
  songOfSongId,
  commitRank,
  getRankOfSongListId,
  getRankOfSongListIdAndUserId,
  setCollect, deleteCollectSongList, selectByPrimaryKey, existCollectSongList
} from "../api";
import AlbumContent from "../components/AlbumContent.vue";
import Comment from "../components/Comment.vue";
import CollectIcon from "../assets/icon/collectIcon.vue";

export default {
  name: 'song-list-album',
  components: {CollectIcon, AlbumContent, Comment},
  mixins: [mixin],
  data() {
    return {
      songList: [],
      songListId: '',
      songListAlbum: [],
      average: 0,
      rank: 0,
      selfRank: 0,
      isCollect: '收藏',
      star: 0,
    }
  },
  computed: {
    ...mapGetters(
      [
        "listOfSongs",  //当前歌单歌曲列表
        "loginIn",
        "username",
        "userId",
      ]
    ),
  },
  mounted() {
    this.songListId = this.$route.params.id;
    selectByPrimaryKey(this.songListId).then(res =>
    {
      this.songListAlbum = res;
    })
    this.getSongList();
    this.getRank(this.songListId);
    if (this.loginIn)
    {
      existCollectSongList(this.userId, this.songListId).then(res =>
      {
        if (res)
        {
          this.isCollect = '已收藏';
        }
      })
    }

  },
  methods: {
    //获取当前歌单的歌曲列表
    getSongList() {
      listSongDetail(this.songListId).then(res =>
      {
        for (let item of res)
        {
          songOfSongId(item.songId).then(res2 =>
          {
            // console.log("res2: ")
            // console.log(res2);
            this.songList.push(res2);
          })
        }
        this.$store.commit('setListOfSongs', this.songList);
      }).catch(error =>
      {
        console.log(error);
      })
    },
    //收藏处理
    handleCollect() {
      if (!this.loginIn)
      {
        this.notify('请先登录');
      }
      else {
        let params = new URLSearchParams();
        params.append('userId', this.userId);
        params.append('type', 1);
        params.append('songListId', this.songListId);
        this.notify(params)
        setCollect(params).then(res =>
        {
          if (res.code === 1)
          {
            this.isCollect = '已收藏';
            this.notify('收藏成功', 'success');
          }
          else if (res.code === 2)
          {
            deleteCollectSongList(this.userId, this.songListId);
            this.isCollect = '收藏';
            this.notify('取消成功收藏', 'success');
          }
          else {
            this.notify(this.userId + this.songListId);
            this.notify('收藏错误', 'error');
          }
        });
      }
    },
    //获取歌单评分
    getRank(id) {
      getRankOfSongListId(id).then(res =>
      {
        if (res === -1)
        {
          this.average = '暂无';
        }
        else {
          this.average = res;
          this.star = this.average / 2;
        }
      }).catch(error =>
      {
        console.log('error in get rank of songList\n' + error);
      })

      let params = new URLSearchParams();
      params.append('songListId', this.songListId);
      params.append('userId', this.userId);
      getRankOfSongListIdAndUserId(params).then(res =>
      {
        this.selfRank = res.rank;
        this.rank = this.selfRank / 2;
      })
    },
    // 评价
    addRank() {
      if (!this.loginIn)
      {
        this.notify('请先登录', 'r');
      }
      else {
        let params = new URLSearchParams();
        params.append('songListId', this.songListId);
        params.append('userId', this.userId);
        params.append('score', this.rank * 2);
        commitRank(params).then(res =>
        {
          if (res.code === 1)
          {
            this.notify('评分成功', 'success');
            this.getRank(this.songListId);
          }
          else {
            this.notify(`评分失败:${res.msg}`, 'error');
          }
        }).catch(error =>
        {
          console.log('error in commit rank\n' + error);
        })
      }
    }
  },
}
</script>
<style lang = "scss" scoped>
@import "../assets/css/song-list-album.scss";
</style>
