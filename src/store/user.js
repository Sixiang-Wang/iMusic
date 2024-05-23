const user = {
  state: {
      userId : '',       // 用户id
      username : '',     // 用户名
      avatar : '',       // 头像
  },
  getters: {
    userId: state => {
      let userId = state.userId
      if(!userId){
        userId = JSON.parse(window.sessionStorage.getItem('userId'));
      }
      return userId;
    },
    username: state => {
      let username = state.username
      if(!username){
        username = JSON.parse(window.sessionStorage.getItem('username'));
      }
      return username
    },
    avatar: state => {
      let avatar = state.avatar
      //console.log(window.sessionStorage.getItem('avatar'));
      if(!avatar){
        if(window.sessionStorage.getItem('avatar') !== "undefined")
          avatar = JSON.parse(window.sessionStorage.getItem('avatar'))
      }
      return avatar
    }
  },
  mutations: {
    setUserId: (state,userId) => {
      state.userId = userId
      window.sessionStorage.setItem('userId',JSON.stringify(userId))
    },
    setUsername: (state,username) => {
      state.username = username
      window.sessionStorage.setItem('username',JSON.stringify(username))
    },
    setAvatar: (state,avatar) => {
      state.avatar = avatar
      window.sessionStorage.setItem('avatar',JSON.stringify(avatar))
    },
  }

}

export default user
