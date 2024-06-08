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
            <span @click = "toplay(item.id,item.url,item.pic,index,item.name,item.lyric,songList)">
              {{ index + 1 }}
            </span>
          </span>
          <span class = "item-title">
              <span @click = "toplay(item.id,item.url,item.pic,index,item.name,item.lyric,songList)">
                {{ replaceFName(item.name) }}
              </span>
          </span>
          <span class = "item-name">
            <span @click = "goSinger(replaceLName(item.name))">
              {{ replaceLName(item.name) }}
            </span>
          </span>
          <span class = "item-intro">
            <span @click = "goSong(item.id)">
              {{ item.introduction }}
            </span>
          </span>
          <span class = "icons" @click = "showSelect($event,index,item.id)" ref = "add">
            <add-icon></add-icon>
          </span>
          <span class = "icons" v-if = "isMySongList" @click = "removeSong(item.id)">
            <delete-icon></delete-icon>
          </span>
          <span class = "icons" @click = "handleCollect(item)">
            <svg :class = "{active: item.collection}" class = "icon">
              <use xlink:href = "#icon-xihuan-shi"></use>
            </svg>
          </span>
          <span class = "icons" @click = "showComplaintModal">
          <!-- 目前是把矢量图当作图片插入的，不能修改颜色，后续进行调整 -->
            <img src = '../assets/js/icon/icon-complain.svg' class = "icon">
          </span>
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
                <button class = "btn btn-blue" @click = "submitComplaint(item.id)">提交</button>
                <button class = "btn btn-gray" @click = "showModal = false">取消</button>
              </div>
            </div>
          </div>
        </div>
      </li>
    </ul>
    <div class = "select-song-list" v-bind:style = "selectPosition" ref = "select" v-if = "isShowSelect">
      <div class = "select-item"
           v-for = "(item, index) in this.selectData" :key = "index"
           @click = "addToSongList(item.id)">
        <span class = "img-container">
          <img class = "img" :src = "attachImageUrl(item.pic)" alt = "">
        </span>
        <div class = "select-title">{{ item.title }}</div>
      </div>
    </div>
    <el-dialog
      :visible.sync = "this.deleteDialog"
      center
      top = "20%"
      width = "400px"
    >
      <span style = "margin-left: 30%;font-size: 20px">确定删除吗?</span>
      <span slot = "footer">
      <el-button @click = "cancelDelete()" size = "small">取消</el-button>
      <el-button @click = "confirmDelete()" size = "small" type = "primary">确定</el-button>
    </span>
    </el-dialog>
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
  addListSong, getSongListById, deleteListSong, addComplaint
} from "../api";
import {mapGetters} from "vuex";
import AddIcon from "../assets/icon/addIcon.vue";
import DeleteIcon from "../assets/icon/deleteIcon.vue";
import song from "../store/song";

export default {
  name: 'album-content',
  components: {DeleteIcon, AddIcon},
  mixins: [mixin],
  props: ['songList'],
  computed: {
    ...mapGetters([
      'loginIn',
      'userId',
    ]),
  },
  data() {
    return {
      deleteDialog: false,
      isMySongList: false,
      isShowSelect: false,
      isSidebarVisible: false,
      deleteSongId: '',
      addIndex: '',
      selectSongId: '',
      selectData: [],
      selectPosition: {
        position: 'fixed',
        top: 0,
        left: 0,
      },
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
  mounted() {
    if (this.loginIn)
    {
      selectSongListByUserId(this.userId).then(res =>
      {
        res.forEach(item =>
        {
          this.selectData.push({id: item.id, pic: item.pic, title: item.title})
        })
      })
      if (this.$route.params.id)
      {
        getSongListById(this.$route.params.id).then(res =>
        {
          if (res.userId === this.userId && this.$route.path.includes('song-list-album'))
          {
            this.isMySongList = true;
          }
        })
      }
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
    goSong(songId) {
      this.$router.push({path: `/song-album/${songId}`});
    },
    addToSongList(songListId) {
      let params = new URLSearchParams();
      params.append('songId', this.selectSongId);
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
    showSelect(event, index, songId) {
      if (!this.loginIn)
      {
        this.notify('请先登录', 'warning');
      }
      else {
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
    },
    removeSong(songId) {
      this.deleteSongId = songId;
      this.deleteDialog = true;
    },
    cancelDelete() {
      this.deleteDialog = false;
      this.deleteSongId = '';
    },
    confirmDelete() {
      deleteListSong(this.deleteSongId, this.$route.params.id).then(res =>
      {
        if (res)
        {
          let list = this.songList.filter(song => song.id !== this.deleteSongId);
          this.$store.commit('setListOfSongs', list);
          this.deleteDialog = false;
          this.deleteSongId = '';
          this.notify('删除成功', 'success');
        }
      })
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
  },
}
</script>

<style scoped>
/* 弹窗样式 */
.complaint-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000; /* 确保弹窗在其他内容之上 */
}

.modal-overlay {
  background: rgba(0, 0, 0, 0.5);
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.modal-content {
  background: #fff;
  padding: 30px; /* 增大内边距 */
  border-radius: 8px; /* 增大边框圆角 */
  width: 80%; /* 弹窗宽度为视口的80% */
  max-width: 80vh; /* 限制最大宽度，防止在小屏幕上过大 */
  max-height: 60vh; /* 设置最大高度为视口高度的80% */
  position: relative; /* 相对于弹窗定位 */
  z-index: 1001; /* 确保内容在overlay之上 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
  overflow: auto; /* 如果内容过多，允许滚动 */
  display: flex;
  flex-direction: column;
  height: 100%; /* 假设弹窗有一个固定的高度或者填充了父元素的高度 */
}

.modal-body {
  /* 确保body部分不会占满整个.modal-content的高度，以便footer可以定位到底部 */
  flex: 1; /* 占据剩余空间 */
}

.reason-group {
  margin-bottom: 20px; /* 每个原因组之间的间距 */
}

.reason-list input[type="checkbox"] {
  margin-right: 10px; /* 复选框和标签之间的间距 */
}

.description-textarea {
  height: 200px;
  width: 800px;
  max-width: 100%;
  padding: 10px; /* 添加内边距 */
  border: 1px solid #ccc; /* 添加边框 */
  border-radius: 5px; /* 添加圆角 */
  font-size: 16px; /* 设置字体大小 */
  font-family: inherit; /* 继承父元素的字体 */
  resize: none; /* 禁止用户调整大小 */
  transition: border-color 0.3s ease; /* 添加边框颜色过渡效果 */
}

.description-textarea:focus {
  border-color: #007bff; /* 当文本框获取焦点时改变边框颜色 */
  outline: none; /* 去除默认的外框 */
  box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, .25); /* 添加阴影效果 */
}

.modal-footer {
  display: flex;
  justify-content: center; /* 或者使用space-between来在按钮之间添加空间 */
  align-items: center; /* 垂直居中（可选） */
  padding: 10px; /* 可选，为底部添加一些内边距 */
  background-color: #f5f5f5; /* 可选，为footer添加背景色 */
  margin-top: auto;
}

.btn {
  padding: 10px 20px; /* 增大按钮的点击区域 */
  margin-left: 10px; /* 按钮之间的间距 */
  font-size: 16px; /* 可选，调整按钮上的文字大小 */
  border: none; /* 去除默认边框 */
  border-radius: 4px; /* 可选，为按钮添加圆角 */
  cursor: pointer; /* 鼠标悬停时显示为手形图标 */
}

.btn-blue {
  background-color: #66ccff; /* 设置蓝色背景 */
  color: white; /* 设置文字颜色为白色 */
}

.btn-gray {
  background-color: gray; /* 设置灰色背景 */
  color: white; /* 设置文字颜色为白色 */
}
</style>

<style lang = "scss" scoped>
@import "../assets/css/album-content.scss";
</style>
