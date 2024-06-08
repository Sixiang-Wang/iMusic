<template>
  <div class="container">
    <div class="message-container">
      <span >我的消息</span>
      <span @click="readAllMessage">一键已读</span>
    </div>
    <div>
      <ul v-for="(item, index) in messageList" :key="index" class="message-item">
        <li class="message-content">{{ item.text }}</li>
        <span class="message-time">{{ item.createTime }}</span>
        <span class="message-src" v-if="item.from===-1">系统消息</span>
        <div class="message-read" @click="readThisMessage(item)">
          <span v-if="item.isRead === 1" style="color: #00aced">已读</span>
          <span v-else>设为已读</span>
        </div>
      </ul>
    </div>
  </div>
</template>

<script>
import {mixin} from "../mixins";
import {mapGetters} from "vuex";
import {getAllMessage, readMessage} from "../api";

export default {
  name: 'message',
  mixins: [mixin],
  data(){
    return {
      messageList: [],//总的消息列表
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
          console.log(res);
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
    }
  }
}
</script>

<style scoped lang="scss">
@import "../assets/css/message.scss";
</style>
