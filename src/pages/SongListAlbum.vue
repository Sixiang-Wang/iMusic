<template>
  <div class = "song-list-album">
    <div class = "album-slide">
      <div class = "album-img">
        <img :src = 'attachImageUrl(songListAlbum.pic)' alt = "">
      </div>
      <div class = "album-info">
        <h2 style = "font-size: 20px">简 介:</h2>
        <span style = "font-size: 16px">{{ songListAlbum.introduction }}</span>
      </div>
    </div>

    <div class = "album-content">
      <div class = "album-title">
        <p>{{ songListAlbum.title }}</p>
      </div>
      <div style="margin-top: 15px">
        <p>风格: {{ songListAlbum.style}}</p>
      </div>
      <div style="margin-top: 15px">
        <p>收藏量: {{collectNum}}</p>
      </div>
      <div class = "collect" @click = "handleCollect()">
        <collect-icon :class = "{'have-collected':this.isCollect==='已收藏'}"
                      style = "margin-left: 25px;">
        </collect-icon>
        <span style = "margin-top: 3px;margin-left: 5px">{{ this.isCollect }}</span>
      </div>
      <div class="collect" @click="showComplaintModal">
        <complain-icon style="color: #646464;width:25px;height: 25px;margin-left: 25px"/>
        <span style = "margin-top: 3px;margin-left: 5px">投诉</span>
      </div>
      <div class = "complaint-modal" v-if = "showModal">
        <div class = "modal-overlay" @click="showModal = false"></div>
        <div class = "modal-content">
          <h3>请选择举报原因</h3>
          <div class = "reason-group" v-for = "(group, index) in reasonGroups" :key = "index">
            <h4>{{ group.title }}</h4>
            <div class = "reason-list">
              <div v-for = "(reason, reasonIndex) in group.reasons" :key = "reasonIndex">
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
            <button class = "btn btn-blue" @click = "submitComplaint">提交</button>
            <button class = "btn btn-gray" @click = "showModal = false">取消</button>
          </div>
        </div>
      </div>
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
  setCollect, deleteCollectSongList, getSongListById, existCollectSongList, collectNumOfSongList, addComplaint
} from "../api";
import AlbumContent from "../components/AlbumContent.vue";
import Comment from "../components/Comment.vue";
import CollectIcon from "../assets/icon/collectIcon.vue";
import ComplainIcon from "../assets/icon/complaint.vue";

export default {
  name: 'song-list-album',
  components: {ComplainIcon, CollectIcon, AlbumContent, Comment},
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
      collectNum: '',
      showModal: false,
      selectedReasons: [],
      description: '',
      reasonGroups: [
        {
          title: '违反法律法规',
          reasons: [
            {value: '违法违禁', label: '违法违禁'},
            {value: '赌博诈骗', label: '赌博诈骗'},
            {value: '盗搬我的稿件', label: '盗搬我的稿件'},
            {value: '侵权申诉', label: '侵权申诉'},
            // ... 可以继续添加更多
          ],
        },
        {
          title: '谣言及不实信息',
          reasons: [
            {value: '涉政谣言', label: '涉政谣言'},
            {value: '涉社会事件谣言', label: '涉社会事件谣言'},
            {value: '虚假不实信息', label: '虚假不实信息'},
            // ... 可以继续添加更多
          ],
        },
        {
          title: '不规范行为',
          reasons: [
            {value: '违规推广', label: '违规推广'},
            {value: '转载', label: '转载'},
            {value: '自制错误', label: '自制错误'},
            {value: '其他不规范行为', label: '其他不规范行为'},
            // ... 可以继续添加更多
          ],
        },
        {
          title: '不友好行为',
          reasons: [
            {value: '人身攻击', label: '人身攻击'},
            {value: '引战', label: '引战'},
            // ... 可以继续添加更多不友好行为
          ],
        },
        {
          title: '公共秩序与道德',
          reasons: [
            {value: '色情低俗', label: '色情低俗'},
            {value: '危险行为', label: '危险行为'},
            {value: '观感不适', label: '观感不适'},
            {value: '血腥暴力', label: '血腥暴力'},
            {value: '青少年不良信息', label: '青少年不良信息'},
            {value: '其他', label: '其他'},
            // ... 可以继续添加更多公共秩序与道德问题
          ],
        },
      ],
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
    getSongListById(this.songListId).then(res =>
    {
      this.songListAlbum = res;
    })
    collectNumOfSongList(this.songListId).then(res => {
      this.collectNum = res;
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
            this.collectNum++;
            this.notify('收藏成功', 'success');
          }
          else if (res.code === 2)
          {
            deleteCollectSongList(this.userId, this.songListId);
            this.isCollect = '收藏';
            this.collectNum--;
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
    },
    showComplaintModal() {
      this.showModal = true; // 显示弹窗
      this.selectedReasons = []; // 重置选择的举报原因
      this.description = ''; // 重置描述
    },
    submitComplaint() {
      if (this.selectedReasons.length === 0) {
        this.notify('请至少选择一个举报原因');
        return;
      }
      // 准备要发送的数据
      let params = new URLSearchParams();
      params.append("userId", this.userId);
      params.append("type", '1');
      params.append("songId", null);
      params.append("songListId", this.songListId);
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
  },
}
</script>
<style lang = "scss" scoped>
@import "../assets/css/song-list-album.scss";
</style>
