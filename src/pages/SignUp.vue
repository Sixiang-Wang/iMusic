<template>
  <div class="gradient-background">
  <!--  <loginLogo />-->
    <div class="signUp">
      <div class="signUp-head">
        <span>用户注册</span>
      </div>
      <el-form :model="registerForm" ref="registerForm" label-width="80px" class="demo-ruleForm" :rules="rules">
        <el-form-item prop="username" label="用户名">
          <el-input v-model="registerForm.username" placeholder="用户名（只支持英文大小写与数字，长度3~16）"></el-input>
        </el-form-item>
        <el-form-item prop="name" label="昵称">
          <el-input v-model="registerForm.name" placeholder="昵称"></el-input>
        </el-form-item>
        <el-form-item prop="email" label="邮箱">
          <el-input v-model="registerForm.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item prop="code" label="验证码">
          <div class="code-input-group">
            <el-input v-model="code" placeholder="验证码"></el-input>
            <el-button class="input-with-button" :loading="codeLoading" :disabled="isDisable" @click="sendMsg">
              {{ statusMsg }}
            </el-button>
          </div>
        </el-form-item>
        <el-form-item prop="password" label="密码">
          <el-input
            type="password"
            v-model="registerForm.password"
            placeholder="密码（只支持英文大小写与数字，长度8~16）"
            :show-password="true"
          ></el-input>
          <i @click="togglePasswordVisibility"></i>
        </el-form-item>
        <el-form-item prop="duplicatePassword" label="确认密码">
          <el-input type="password" v-model="duplicatePassword" placeholder="确认密码"></el-input>
        </el-form-item>
        <div class="login-btn">
          <el-button @click="goback(-1)">取消</el-button>
          <el-button type="primary" @click="SignUp">确定</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>


<script>
import loginLogo from '../components/LoginLogo.vue';
import {rules,cities} from "../assets/data/form.js";
import {mixin} from '../mixins';
import {preLogin, SignUp, validate} from '../api/index'
export default {
  name : 'sign-up',
  mixins : [mixin],
  components: {
    loginLogo
  },
  data(){
    return  {
      statusMsg: '',      // 验证码有关
      isDisable: false,
      codeLoading: false,
      verifyCode : '',    // 标准验证码
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
      duplicatePassword : '', // 重复密码
      cities: [],     // 所有的地区 -- 省
      rules: {},   // 表单提交的规则
      code : '',    // 验证码
      passwordVisible: false
    }
  },
  created() {
    this.statusMsg = '获取验证码';
    this.rules = rules;
    this.cities = cities;
  },
  methods: {
    SignUp(){
      if(!this.isValidUsername(this.registerForm.username)){
        this.notify('用户名不符合要求','error');
        return;
      }
      if(!this.isValidSecret(this.registerForm.password)){
        this.notify('密码不符合要求','error');
        return;
      }
      if(this.registerForm.password !== this.duplicatePassword){
        this.notify('两次输入密码不一致','error');
        return;
      }
      if(!this.isValidVerificationCode(this.verifyCode) || this.verifyCode !== this.code){
        this.notify('验证码不正确','error');
        return;
      }
      let _this = this;
      let d = this.registerForm.birth
      let datetime
      if (d) {
        datetime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
      }
      let params = new URLSearchParams();
      params.append('username',this.registerForm.username);
      params.append('name',this.registerForm.name);
      params.append('password',this.registerForm.password);
      this.registerForm.sex = 3; //  保密
      params.append('sex',this.registerForm.sex);
      params.append('phoneNum',this.registerForm.phoneNum);
      params.append('email',this.registerForm.email);
      params.append('birth', datetime);
      params.append('introduction',this.registerForm.introduction);
      params.append('location',this.registerForm.location);
      params.append('profilePicture','/img/Pic/default_avatar.jpg');
      SignUp(params)
        .then(res => {
          if(res.code === 1){
            _this.notify('注册成功', 'success');
            setTimeout(function (){
              _this.changeIndex('登录');
              _this.$router.push({path : `/login-in`});
            }, 2000);
          }
          else{
            _this.notify(`注册失败:${res.msg}`, 'error');
          }
        })
        .catch(err => {
          _this.notify('注册失败', 'error');
        })
    },
    sendMsg: function() {
      // 22373297@buaa.edu.cn
      const self = this
      let timerId;
      console.log(timerId)
      if (timerId) {
        return false;
      }
      this.codeLoading = true;
      let errorTimerId = window.setTimeout(() => {
        this.notify('请求超时，请稍后再试', 'error');
        window.clearTimeout(errorTimerId);
        self.verifyCode = "******";
        self.codeLoading = false
        self.isDisable = false;
        self.statusMsg = '获取验证码';
      }, 6000); // 6秒后触发

      validate(this.registerForm.email).then(res => {
        this.notify('发送成功，验证码有效期 1 分钟', 'success');
        let count = 60
        this.verifyCode = res.verifyCode;
        self.codeLoading = false
        self.isDisable = true
        self.statusMsg = `${count--} 秒后重新发送`
        timerId = window.setInterval(function () {
          self.statusMsg = `${count--} 秒后重新发送`
          window.clearTimeout(errorTimerId);
          if (count <= -1) {
            console.log('clear' + timerId);
            window.clearInterval(timerId);
            self.verifyCode = "******";
            self.isDisable = false;
            self.statusMsg = '获取验证码';
          }
        }, 1000)
      }).catch(err => {
        this.isDisable = false
        this.statusMsg = '获取验证码'
        this.codeLoading = false
        window.clearTimeout(errorTimerId);
      })
    },
    goback(index){
      this.$router.go(index);
    },
    changeIndex(value){
      this.$store.commit('setActiveName' , value);
    },
    togglePasswordVisibility() {
      this.passwordVisible = !this.passwordVisible;
    },
    // 对用户名进行检验
    isValidUsername(username) {
      var regex = /^[a-zA-Z0-9]{3,16}$/; // 正则表达式
      return regex.test(username);
    },
    // 对密码进行检验
    isValidSecret(secret) {
      var regex = /^[a-zA-Z0-9]{8,16}$/; // 正则表达式
      return regex.test(secret);
    },
    // 对验证码进行检验
    isValidVerificationCode(code) {
      // 正则表达式解释：
      // ^ 表示字符串的开始
      // [0-9] 表示匹配数字
      // $ 表示字符串的结束
      var regex = /^[0-9]{6}$/;
      return regex.test(code);
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/sign-up.scss';
</style>
