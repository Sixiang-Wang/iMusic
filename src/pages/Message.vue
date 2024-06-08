<template>
  <div class="container">
    <div class="message-container">
      <span >我的消息</span>
      <span @click="readAllMessage">一键已读</span>
    </div>
    <div>
      <ul v-for="(item, index) in messageList" :key="item.id" class="message-item">
        <li class="message-content-1" v-if="item.type===0">{{ item.text }}</li>
        <li class="message-content-2" v-else-if="item.type===1">{{ item.text }}</li>
        <span class="message-link" @click="goAppeal" v-if="item.type===1">跳转到申诉界面</span>
        <span class="message-time">{{ item.createTime }}</span>
        <span class="message-src" v-if="item.from===-1">系统消息</span>
        <div class="message-read" @click="readThisMessage(item)">
          <span v-if="item.isRead === 1" style="color: #00aced">已读</span>
          <span v-else>设为已读</span>
        </div>
        <span class="message-delete" @click="confirmDelete(item.text)">删除</span>
        <!-- 确认删除的对话框 -->
        <div class = "complaint-modal" v-if = "confirmDeleteMessage">
          <div class = "modal-overlay" @click="confirmDeleteMessage = false"></div>
          <div class = "modal-content">
            <li>
              确定要删除<br><br><b>{{ content }}</b><br><br>这一消息吗<br>
            </li>
            <div class = "modal-footer">
              <button class = "btn btn-blue" @click = "deleteThisMessage(item.id)">确认</button>
              <button class = "btn btn-gray" @click = "confirmDeleteMessage = false">取消</button>
            </div>
          </div>
        </div>
      </ul>
    </div>
  </div>
</template>

<script>
import {mixin} from "../mixins";
import {mapGetters} from "vuex";
import {deleteMessage, getAllMessage, readMessage} from "../api";

export default {
  name: 'message',
  mixins: [mixin],
  data(){
    return {
      messageList: [],//总的消息列表
      confirmDeleteMessage : false,
      confirmDeleteAllMessage :false,
      content : ''
    }
  },
  computed: {
    ...mapGetters([
      'userId',
    ])
  },
  created() {
    this.getUserAllMessage(this.userId);
  },
  methods: {
    readThisMessage(item){
      if(item.isRead===1){
        return;
      }
      readMessage(item.id)
        .then(res => {
          item.isRead = 1;
        })
        .catch(error =>{
          console.log(error);
        })
    },
    getUserAllMessage(userId){
      getAllMessage(userId)
        .then(res => {
          this.messageList = res;
          // console.log(res);
        })
        .catch(error =>{
          console.log(error);
        })
      // { "id": 11, "to": 19, "from": -1,
      // "text": "您关注的歌手 Guns N' Roses 发布了新歌《Guns N' Roses-Knockin' On Heaven's Door》!",
      // "isRead": 0,
      // "createTime": "2024-06-08 14:43:08" }
    },
    readAllMessage(){
      for(let message of this.messageList){
        if(message.isRead===1){
          continue;
        }
        readMessage(message.id)
          .then(res => {
            message.isRead = 1;
          })
          .catch(error =>{
            console.log(error);
          })
      }
    },
    goAppeal(){
      this.$router.push({path: '/appeal'});
    },
    confirmDelete(text){
      this.content = text;
      this.confirmDeleteMessage = true;
    },
    deleteThisMessage(messageId){
      deleteMessage(messageId)
        .then(res => {
          this.notify('删除成功','success');
        })
        .catch(error => {
          this.notify('删除失败','error');
        })
      this.confirmDeleteMessage = false;
      window.location.reload();
    }
  }
}
</script>

<style scoped lang="scss">
@import "../assets/css/message.scss";
</style>
