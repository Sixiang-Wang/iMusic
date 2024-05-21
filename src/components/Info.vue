<template>
  <div>
    <div class="info">
      <div class="title">
        <span>编辑个人资料</span>
      </div>
      <hr />
      <div class="personal">
        <el-form :model="registerForm" ref="registerForm" label-width="70px" class="demo-ruleForm" :rules="rules">
          <el-form-item prop="username" label="用户名">
            <el-input v-model="registerForm.username" placeholder="用户名"></el-input>
          </el-form-item>
          <el-form-item prop="name" label="昵称">
            <el-input v-model="registerForm.name" placeholder="昵称"></el-input>
          </el-form-item>
          <el-form-item prop="password" label="密码">
            <el-input type="password" v-model="registerForm.password" placeholder="密码"></el-input>
          </el-form-item>
          <el-form-item prop="sex" label="性别">
            <el-radio-group v-model="registerForm.sex">
              <el-radio :label="0">女</el-radio>
              <el-radio :label="1">男</el-radio>
              <el-radio :label="3">保密</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item prop="phoneNum" label="手机">
            <el-input v-model="registerForm.phoneNum" placeholder="手机"></el-input>
          </el-form-item>
          <el-form-item prop="email" label="邮箱">
            <el-input v-model="registerForm.email" placeholder="邮箱"></el-input>
          </el-form-item>
          <el-form-item prop="birth" label="生日">
            <el-date-picker type='date' :editable="false" v-model="registerForm.birth" placeholder="选择日期" style="width: 100%;"></el-date-picker>
          </el-form-item>
          <el-form-item prop="introduction" label="个性签名">
            <el-input v-model="registerForm.introduction" placeholder="个性签名"></el-input>
          </el-form-item>
          <el-form-item prop="location" label="地区">
            <el-select v-model="registerForm.location" placeholder="地区" style="width: 100%;">
              <el-option v-for="item in cities" :key="item.index" :label="item.label" :value="item.value"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div class="btn">
          <div @click="saveMsg">保存</div>
          <div @click="goback(-1)">取消</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import {rules,cities} from "../assets/data/form.js";
import {mixin} from '../mixins';
import {getUserOfId, updateUserMsg} from '../api/index'

export default {
  name: 'info',
  mixins: [mixin],
  data(){
    return  {
      registerForm: {
        username: '',     //用户名
        name: '',         // 昵称
        password: '',     // 密码
        sex: '',          // 性别
        phoneNum: '',     // 手机
        email: '',        // 邮箱
        birth: '',        // 生日
        introduction: '',    //签名
        location: ''      // 地区
      },
      usernameOrigin : '', // 原始用户名
      nameOrigin : '',      // 原始昵称
      cities: [],     // 所有的地区 -- 省
      rules: {}   // 表单提交的规则
    }
  },
  mounted() {
    this.getMsg(this.userId);
  },
  computed:{
    ...mapGetters([
      'userId'
    ])
  },
  created() {
    this.rules = rules;
    this.cities = cities;
  },
  methods: {
    getMsg(userId){
      getUserOfId(userId)
        .then(res => {
          this.registerForm.username = res.username;
          this.registerForm.name = res.name;
          this.registerForm.password = res.password;
          this.registerForm.sex = res.sex ;
          this.registerForm.phoneNum = res.phoneNum;
          this.registerForm.email = res.email;
          this.registerForm.birth = res.birth;
          this.registerForm.introduction = res.introduction;
          this.registerForm.location = res.location
          // 获得用户的原始账号（用户名）
          this.usernameOrigin = this.registerForm.username;
          // 获得用户的原始昵称
          this.nameOrigin = this.registerForm.name;
        })
        .catch(err => {
          console.log(err);
        })
    },
    saveMsg() {
      let _this = this;
      let d = new Date(this.registerForm.birth);
      let datetime
      if (d) {
        datetime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
      }
      let params = new URLSearchParams();
      params.append('id' , this.userId);
      params.append('username', this.registerForm.username);
      params.append('usernameOrigin',this.usernameOrigin);
      params.append('name', this.registerForm.name);
      params.append('nameOrigin', this.nameOrigin);
      params.append('password', this.registerForm.password);
      params.append('sex', this.registerForm.sex);
      params.append('phoneNum', this.registerForm.phoneNum);
      params.append('email', this.registerForm.email);
      params.append('birth', datetime);
      params.append('introduction', this.registerForm.introduction);
      params.append('location', this.registerForm.location);
      // params.forEach((value, key)=>{
      //   console.log(key, value);
      // })
      updateUserMsg(params)
        .then(res => {
          if (res.code === 1) {
            this.notify('dd')
            this.$store.commit('setUsername',this.registerForm.username);
            _this.notify('修改成功', 'success');
            setTimeout(function () {
              _this.changeIndex('首页');
              _this.$router.push({path : '/'});
            }, 1000);
          } else {
            _this.notify('修改失败', 'error');
          }
        })
        .catch(error => {
          _this.notify('修改失败', 'error');
        })
    },
    goback(index) {
      this.$router.go(index);
    },
    changeIndex(value) {
      this.$store.commit('setActiveName', value);
    }
  }
}

</script>

<style lang="scss" scoped>
@import '../assets/css/info.scss';
</style>

