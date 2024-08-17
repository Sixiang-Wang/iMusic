<template>
  <div class = "song-album">
    <div class = "album-slide">
      <div class = "singer-img">
        <img v-if = "this.imageUrl" :src = 'this.imageUrl' alt = "">
        <img alt = "" v-else src = "../assets/img/tubiao.jpg"/>
      </div>
    </div>

    <div class = "album-content">
      <div class = "album-title">
        <span>{{ replaceFName(song.name) }}</span>
      </div>
      <div class = "info">
        <ul>
          <li>歌手: {{ replaceLName(song.name) }}</li>
          <li>专辑: {{ song.introduction }}</li>
        </ul>
        <br>
        <ul>
          <li>风格: {{ song.style }}</li>
          <li>热度: {{ favor }}</li>
        </ul>
        <p style = "margin-top: 20px;font-size: 16px">发行时间: {{ this.createTime }}</p>
      </div>
      <div style = "display: flex">
        <div class = "play-icon" @click = "play">
          <play-icon style = "margin: 3px 12px"></play-icon>
          <span style = "margin-top: 8px;">播放</span>
        </div>

        <div class = "collect" @click = "handleCollect()">
          <like-icon style = "margin: 6px 10px" :class = "{'have-collected':this.isCollect==='已收藏'}">
          </like-icon>
          <span style = "margin-top: 7px;margin-left: 2px">{{ this.isCollect }}</span>
        </div>

        <div class="button-box" @click = "showSelect()" ref = "add">
          <add-icon style="width: 28px;height: 28px;margin: 6px 10px">
          </add-icon>
          <span style="margin: 7px 0">添加到</span>
        </div>

        <div class = "button-box" @click = "showComplaintModal()">
          <complain-icon style = "margin: 8px 13px;width: 22px;height: 22px">
          </complain-icon>
          <span style = "margin-top: 8px">举报</span>
        </div>
      </div>
    </div>
    <div class = "lyric">
      <h2>歌词:</h2>
      <div class = "lyric-context" v-for = "(item,index) in this.lyric" :key = "index">
        <p>{{ item[1] }}</p>
      </div>
    </div>
    <comment :type = "0"></comment>

    <div class = "select-song-list" v-bind:style="addPosition" v-if = "isShowSelect" ref="select">
      <div class = "select-item"
           v-for = "(item, index) in this.selectData" :key = "index"
           @click = "addToSongList(item.id)">
        <span class = "img-container">
          <img class = "img" :src = "attachImageUrl(item.pic)" alt = "">
        </span>
        <div class = "select-title">{{ item.title }}</div>
      </div>
    </div>


    <div class = "complaint-modal" v-if = "showModal">
      <div class = "modal-overlay" @click = "showModal = false"></div>
      <div class = "modal-content" style="font-size: 16px">
        <h3>请选择举报原因</h3>
        <div class = "reason-group" v-for = "(group, index) in reasonGroups" :key = "index">
          <h4>{{ group.title }}</h4>
          <div class = "reason-list">
            <div style="margin: 15px" v-for = "(reason, reasonIndex) in group.reasons" :key = "reasonIndex">
              <input
                type = "checkbox"
                :id = "`reason${index}${reasonIndex}`"
                :value = "reason.value"
                v-model = "selectedReasons"
                @change = "checkMaxReasons"
              >
              <label :for = "`reason${index}${reasonIndex}`">{{ reason.label }}</label>
            </div>
          </div>
        </div>
        <div class = "reason-group">
          <h4>详细描述您遇到的问题</h4>
          <textarea class = "description-textarea" v-model = "description" placeholder = "详细描述"></textarea>
        </div>
        <div class = "modal-footer">
          <button class = "btn btn-blue" @click = "submitComplaint(songId)">提交</button>
          <button class = "btn btn-gray" @click = "showModal = false">取消</button>
        </div>
      </div>
    </div>


  </div>

</template>

<script>
import {mixin} from "../mixins";
import {mapGetters} from "vuex";
import {
  addComplaint, addListSong,
  collectNumOfSong,
  deleteCollectSong,
  existCollectSong, selectSongListByUserId,
  setCollect,
  songOfSongId
} from "../api";
import AlbumContent from "../components/AlbumContent.vue";
import Comment from "../components/Comment.vue";
import PlayIcon from "../assets/icon/playIcon.vue";
import LikeIcon from "../assets/icon/likeIcon.vue";
import CollectIcon from "../assets/icon/collectIcon.vue";
import ComplainIcon from "../assets/icon/complaint.vue";
import AddIcon from "../assets/icon/addIcon.vue";
import {reasonGroup} from "../assets/data/reasonGroup";
export default {
  name: 'singer-album',
  components: {AddIcon, ComplainIcon, CollectIcon, LikeIcon, PlayIcon, AlbumContent, Comment,reasonGroup},
  mixins: [mixin],
  data() {
    return {
      songId: '',
      song: '',
      imageUrl: null,
      pic: "",
      favor: '',
      lyric: [],
      isCollect: '收藏',
      isShowSelect: false,
      selectData: [],
      addPosition: {
        position: 'fixed',
        top: 0,
        left :0,
      },
      showModal: false,
      selectedReasons: [],
      description: '',
      reasonGroups: reasonGroup,
    }
  },
  computed: {
    ...mapGetters(
      [
        'loginIn',
        'userId',
        'curTime'
      ]
    ),
    createTime() {
      return this.song.createTime.slice(0, 10);
    }
  },
  created() {
    this.songId = this.$route.params.id;
    songOfSongId(this.songId).then(res =>
    {
      this.song = res;
      this.pic = this.song.pic;
      this.initialize();
    })
    collectNumOfSong(this.songId).then(res =>
    {
      this.favor = res
    })
    if (this.loginIn)
    {
      existCollectSong(this.userId, this.songId).then(res =>
      {
        if (res)
        {
          this.isCollect = '已收藏';
        }
      });
      selectSongListByUserId(this.userId).then(res =>
      {
        res.forEach(item =>
        {
          this.selectData.push({id: item.id, pic: item.pic, title: item.title})
        })
      });
    }
  },
  methods: {
    async attachImgUrl(srcurl) {
      if (srcurl) {
        const imageUrl = this.$store.state.configure.HOST + srcurl;
        try {
          const response = await fetch(imageUrl, {method: 'HEAD'});
          if (response.ok) {
            this.imageUrl = imageUrl;
          }
          else {
            this.imageUrl = null; // 图片不存在，设置为null
          }
        } catch (_) {
          this.imageUrl = null; // 请求错误，设置为null
        }
      }
      else {
        this.imageUrl = null; // 未提供srcurl，设置为null
      }
    },
    async initialize() {
      await this.attachImgUrl(this.pic);
      this.lyric = this.parseLyric(this.song.lyric);
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

    play() {
      let song = this.song;
      this.toplay(song.id, song.url, song.pic, 0, song.name, song.lyric, [song]);
      this.$store.commit('setPlayList', [song]);
    },
    handleCollect() {
      if (!this.loginIn)
      {
        this.notify('请先登录');
      }
      else {
        let params = new URLSearchParams();
        params.append('userId', this.userId);
        params.append('type', 0);
        params.append('songId', this.songId);
        setCollect(params).then(res =>
        {
          if (res.code === 1)
          {
            this.isCollect = '已收藏';
            this.favor++;
            this.notify('收藏成功', 'success');
          }
          else if (res.code === 2)
          {
            deleteCollectSong(this.userId, this.songId);
            this.isCollect = '收藏';
            this.favor--;
          }
          else {
            this.notify('收藏错误', 'error');
          }
        });
      }
    },
    addToSongList(songListId) {
      let params = new URLSearchParams();
      params.append('songId', this.songId);
      params.append('songListId', songListId);
      addListSong(params).then(res =>
      {
        if (res.code)
        {
          this.hideSelectBox();
          this.notify('添加成功', 'success');
        }
      })
    },
    showSelect() {
      if (!this.loginIn)
      {
        this.notify('请先登录', 'warning');
      }
      else {
        this.isShowSelect = true;
        const svgRect = this.$refs.add.getBoundingClientRect();
        this.addPosition.top = svgRect.top + 40 + 'px';
        this.addPosition.left = svgRect.left - 30 + 'px';
        window.addEventListener('click', this.handleOutsideClick);
      }
    },
    handleOutsideClick(event) {
      if (!this.$refs.add.contains(event.target) && !this.$refs.select.contains(event.target)) {
        this.hideSelectBox();
      }
    },
    hideSelectBox() {
      this.isShowSelect = false;
      window.removeEventListener('click', this.handleOutsideClick);
    },

    showComplaintModal() {
      this.showModal = true; // 显示弹窗
      this.selectedReasons = []; // 重置选择的举报原因
      this.description = ''; // 重置描述
    },
    submitComplaint(Id) {
      if (this.selectedReasons.length === 0) {
        this.notify('请至少选择一个举报原因');
        return;
      }
      // 准备要发送的数据
      let params = new URLSearchParams();
      params.append("userId", this.userId);
      params.append("type", '0');
      params.append("songId", Id);
      params.append("songListId", null);
      // params.append("content", this.description);
      // 将选择的举报原因以逗号分隔的字符串形式发送
      params.append("content", '举报原因:' + this.selectedReasons.join(',') + '     详细描述:' + this.description);
      // console.log('提交举报:' + this.selectedReasons.join(',')+'\n提交举报内容:'+this.description);
      // 发送请求
      addComplaint(params)
        .then(res =>
        {
          this.$notify({title: '投诉成功', type: 'success'});
          setTimeout(() =>
          {
            this.showModal = false;
          }, 1000); // 1000毫秒即1秒
        })
        .catch(error =>
        {
          console.log(error);
          this.$notify({title: '投诉失败', type: 'error'}); // 可以增加一个通知函数用于显示错误通知
          setTimeout(() =>
          {
            this.showModal = false;
          }, 1000); // 1000毫秒即1秒
        });
    },
    checkMaxReasons() {
      if (this.selectedReasons.length > 3) {
        // 你可以在这里给出一些反馈，比如弹出警告或禁用更多的checkbox
        this.notify('最多只能选择三种原因！', 'warning');
        // 如果你想要禁用更多的选择，你可以添加一个计算属性或方法来动态绑定disabled属性到checkbox上
        // 但这通常不是最佳的用户体验，因为用户可能不知道为什么不能选择更多
        // 另一个选择是移除数组中超出三种的最后一个元素
        this.selectedReasons.splice(3);
      }
    },
  }
}
</script>

<style lang = "scss" scoped>
@import "../assets/css/song-album.scss";
@import "../assets/css/complain.scss";
</style>
