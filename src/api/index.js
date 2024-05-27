import {get, post} from './http'

// 判断管理员是否登录成功
export const getLoginStatus = (params) => post(`admin/login/status`, params)

export const preLogin = () => post(`admin/login/preLogin`)
export const logout = () => post(`admin/login/logout`)
// ============歌手相关================
// 查询歌手
export const getAllSinger = () => get(`singer/allSinger`)

export const getSingerById = (singerId) => get(`singer/selectByPrimaryKey?id=${singerId}`)

export const oneSingerOfName = (name) => get(`singer/oneSingerOfName?name=${name}`)
// 添加歌手
export const setSinger = (params) => post(`singer/add`, params)
// 编辑歌手
export const updateSinger = (params) => post(`singer/update`, params)
// 删除歌手
export const delSinger = (id) => get(`singer/delete?id=${id}`)

// ============歌曲相关================
// 根据歌手id查询歌曲
export const songOfSingerId = (id) => get(`song/singer/detail?singerId=${id}`)
// 编辑歌曲
export const updateSong = (params) => post(`song/update`, params)
// 删除歌曲
export const delSong = (id) => get(`song/delete?id=${id}`)
// 根据歌曲id查询歌曲对象
export const songOfSongId = (id) => get(`song/detail?songId=${id}`)
// 根据歌曲名获取歌曲对象
export const songOfSongName = (songName) => get(`song/songOfSongName?songName=${songName}`)
// 查询所有歌曲
export const allSong = () => get(`song/allSong`)
export const allInvisible = () => get(`song/allInvisible`)
export const songOfStyle = (style) => get(`song/songOfStyle?style=${style}`)
export const invisibleSong = (id) => get(`song/invisible?id=${id}`)
export const visibleSong = (id) => get(`song/visible?id=${id}`)

// ============歌单相关================
// 查询歌单
export const getAllSongList = () => get(`songList/allSongList`)
// 添加歌单
export const setSongList = (params) => post(`songList/add`, params)
// 编辑歌单
export const updateSongList = (params) => post(`songList/update`, params)
// 删除歌单
export const delSongList = (id) => get(`songList/delete?id=${id}`)

// ============歌单的歌曲相关============
// 根据歌单id查询歌曲列表
export const listSongDetail = (songListId) => get(`listSong/detail?songListId=${songListId}`)
// 给歌单增加歌曲
export const listSongAdd = (params) => post(`listSong/add`, params)
// 删除歌单的歌曲
export const delListSong = (songId, songListId) => get(`listSong/delete?songId=${songId}&songListId=${songListId}`)

// ============用户相关================
// 查询用户
export const getAllUser = () => get(`user/allUser`)
// 添加用户
export const setUser = (params) => post(`user/add`, params)
// 编辑用户
export const updateUser = (params) => post(`user/update`, params)
// 删除用户
export const delUser = (id) => get(`user/delete?id=${id}`)
// 根据用户id查询该用户的详细信息
export const getUserOfId = (id) => get(`/user/selectByPrimaryKey?id=${id}`)

// ===============收藏===================
// 指定用户的收藏列表
export const getCollectOfUserId = (userId) => get(`/collect/collectOfUserId?userId=${userId}`)
export const getCollectSongOfUserId = (userId) => get(`/collect/collectSongOfUserId?userId=${userId}`)
// 删除用户收藏的歌曲
export const deleteCollection = (userId, songId) => get(`collect/delete?userId=${userId}&songId=${songId}`)

// ===============评论===================
// 指定歌单的评论列表
export const getCommentOfSongListId = (songListId) => get(`/comment/commentOfSongListId?songListId=${songListId}`)
// 删除评论
export const deleteComment = (id) => get(`comment/delete?id=${id}`)

// =========mail
//
export const sendMail = (to) => get(`/mail/sendMail?to=${to}`)

export const getFollowByUserId = (userId) => get(`/follow/getByUserId?userId=${userId}`)
export const deleteFollow = (id) => get(`/follow/delete?id=${id}`)
export const existFollow = (userId, singerId) => get(`/follow/existFollow?userId=${userId}&singerId=${singerId}`)
