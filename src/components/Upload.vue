<template>
  <div class="upload">
    <p class="title">修改头像</p>
    <hr />
    <div class="section">
      <el-upload drag :action="uploadUrl()" :show-file-list="false" :on-success="handleAvatarSuccess"
                 :before-remove="beforeAvatarUpload">
        <i class="el-icon-upload"></i>
        <div>
          将文件拖到此处，或 <span style="color: blue">修改头像</span>
        </div>
        <div slot="tip">
          只能上传jpg/png文件，且不能超过10MB
        </div>
      </el-upload>
    </div>
  </div>
</template>
<script>
import {mapGetters} from "vuex";
import {mixin} from "../mixins";
import avatar from "element-ui/packages/avatar";

export default {
  name: 'upload',
  mixins: [mixin],
  computed:{
    ...mapGetters([
      'userId'
    ])
  },
  methods:{
    // 上传地址
    uploadUrl(){
      return `${this.$store.state.configure.HOST}/user/updateUserPic?id=${this.userId}`;
    },
    // 上传成功
    handleAvatarSuccess(res){
      // console.log(res);
      if(res.code === 1){
        this.$store.commit('setAvatar' ,res.profilePicture);
        this.$notify({title:'修改成功',type:'success'});
      }
      else{
        this.$notify({title:'修改失败',type:'error'});
      }
    },
    // 上传之前的校验
    beforeAvatarUpload(file){
      // console.log(file.type);
      const isJPG = file.type==='image/jpg';
      const isLt10M = file.size / 1024 / 1024 < 10;
      if(!isJPG){
        this.$notify({title:'上传头像图片只能是JPG格式',type:'error'});
        return false;
      }
      if(!isLt10M){
        this.$notify({title:'上传头像图片不能大于10MB',type:'error'});
        return false;
      }
      return true;
    }
  }
}
</script>
<style lang="scss" scoped>
@import "../assets/css/upload.scss";
</style>
