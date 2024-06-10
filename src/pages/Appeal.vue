<template>
  <div class="container">
    <div class="appeal-container">
      <span>申诉</span>
    </div>
    <div>
      <ul v-for="(value, index) in complaintList" :key="index" class="appeal-item">
        <li class="appeal-content">
          您发布的 <b>{{songInfo[index]}}</b> 已被举报<br>
          <br>
          {{value.content}}
        </li>
        <span class="appeal-entry" @click="openInterface">点击进行申诉 >></span>
        <span class="appeal-time">{{ value.createTime }}</span>
        <span class="appeal-src">系统消息</span>
        <li>
          <div class = "complaint-modal" v-if = "showModal">
            <div class = "modal-overlay" @click="showModal = false"></div>
            <div class = "modal-content">
              <div class = "reason-group">
                <h2 style="margin-bottom: 10px;">进行申诉</h2>
                <textarea class = "description-textarea" v-model = "description" placeholder = "请填写申诉理由~"></textarea>
              </div>
              <div class = "modal-footer">
                <button class = "btn btn-blue" @click = "appealToComplaint(value.id)">提交</button>
                <button class = "btn btn-gray" @click = "showModal = false">取消</button>
              </div>
            </div>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import {mixin} from "../mixins";
import {mapGetters} from "vuex";
import {allComplaintAgainstUser, appealComplaint, getSongListById, songOfSongId} from "../api";
import complaint from "../assets/icon/complaint.vue";
export default {
  name: 'appeal',
  mixins : [mixin],
  data() {
    return {
      complaintList : [],   // 举报列表
      description: '', // 申诉描述
      showModal: false,
      songInfo : []
    }
  },
  computed: {
    ...mapGetters([
      'userId',
    ])
  },
  mounted() {
    this.getAllComplaintAgainstUser(this.userId);
  },
  methods: {
    getAllComplaintAgainstUser(userId) {
      allComplaintAgainstUser(userId)
        .then(res => {
          this.complaintList = res.map(complaint => {
            complaint.content = complaint.content.match(/^\s*\S+\s*/)[0].trim();
            return complaint;
          });
          this.getInfo(this.complaintList.map(complaint => ([complaint.type,(complaint.songId || complaint.songListId)])));
          // console.log(this.complaintList)
        })
        .catch(error => {
          console.error('Error fetching complaints:', error);
          // 可能需要调用错误处理逻辑
        });
    },
    getInfo(songIds) {
      if(songIds === null){
        return;
      }
      const promises = songIds.map(songId => {
        if(songId[0]===1){//是歌单
          return getSongListById(songId[1]);
        }
        else{
          return songOfSongId(songId[1]);
        }
      });
      Promise.all(promises)
        .then(songsInfo => {
          songsInfo.forEach((song, index) => {
            if(song.name !== null && song.name !== undefined){
              this.songInfo.push('歌曲：'+song.name);
            }
            else{
              this.songInfo.push('歌单：' +song.title);
            }
            // console.log(song.name)
          });
        })
        .catch(error => {
          console.error('Error fetching song info:', error);
          // 可能需要调用错误处理逻辑
        });
    },
    appealToComplaint(complaintId) {
      let params = new URLSearchParams();
      params.append('id', complaintId);
      params.append('appeal', this.description);
      appealComplaint(params)
        .then(res => {
          this.notify('申诉成功', 'success');
        })
        .catch(error => {
          this.notify('申诉失败', 'error');
          console.error('Appeal error:', error);
        });
      this.showModal = false;
    },
    openInterface() {
      this.showModal = true;
      this.description = '';
    },
  }
}
</script>

<style scoped lang="scss">
@import "../assets/css/appeal.scss";
</style>
