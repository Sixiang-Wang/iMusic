import {get,post} from "./http"
import songList from "../pages/SongList.vue";
import Axios from "axios";


// ============歌手相关=============
// 查询歌手
export const getAllSinger =() =>get(`singer/allSinger`);

export const getSingerById = (id) => get(`singer/selectByPrimaryKey?id=${id}`)

export const getOneSingerByName = (name) => get(`singer/oneSingerOfName?name=${name}`)
// ============歌曲相关=============
// 根据歌手id查询歌曲
export const songOfSingerId =(id) =>get(`song/singer/detail?singerId=${id}`);
// 根据歌曲id查询歌曲对象
export const songOfSongId =(id) => get(`song/detail?songId=${id}`);
// 根据歌曲名字模糊查询歌曲
export const likeSongOfName =(keywords) => get(`song/likeSongOfName?songName=${keywords}`);
// 添加歌曲播放次数
export const addNums = (songId) => get(`/song/addNums?songId=${songId}`);
// ============歌单相关==============
// 查询歌曲
export const getAllSongList =() => get(`songList/allSongList`);//获得所有歌单

//添加歌单
export const setSongList = (params) => post(`songList/add`, params);

//根据歌单标题模糊查询歌单
export const likeTitle =(keywords) => get(`songList/likeTitle?title=${keywords}`);

// =============歌单的歌曲相关========
// 根据歌曲id查询歌曲列表
export const listSongDetail = (songListId) => get(`listSong/detail?songListId=${songListId}`);

// ============用户相关==============

// 查询用户
export const getAllUser=()=>get(`user/allUser`);

// 注册
export const SignUp = params => post(`user/add`, params);

// 发送验证码
export const validate = email => get(`mail/sendMail?to=${email}`);

// 登录
export const LoginIn = params => post(`user/login`, params);
// 查询该用户的详细信息
export const getUserOfId = (id) => get(`user/selectByPrimaryKey?id=${id}`);

// 更新
export const updateUserMsg = params => post(`user/update`, params);
// 下载音乐
export const download = (url) => Axios({
  method: 'get',
  url: url,
  responseType: 'blob'
});
// =============== 评价 ==================
//提交评分
export const commitRank = (param) => post(`rank/add`,param);
//获取歌单的平均分
export const getRankOfSongListId = (songListId) => get(`rank/rankOfSongListId?songListId=${songListId}`)
export const getRankOfSongListIdAndUserId = (params) => post(`rank/rankOfSongListIdAndUserId`,params)
// =============== 评论 ==================
// 提交评
export const setComment = (params) => post(`comment/add`, params);
// 点赞
export const setLike = (params) => post(`comment/like`, params);
// 返回当前歌单或歌曲的评论区列表

// =============== 收藏 ==================
// 判断歌曲是否已收藏
export const existCollectSong = (userId, songId) => get(`collect/existCollectSong?userId=${userId}&songId=${songId}`);

// 判断歌单是否已收藏
export const existCollectSongList = (userId, songListId) => get(`collect/existCollectSongList?userId=${userId}&songListId=${songListId}`);
// 新增收藏
export const setCollect = (params) => post(`collect/add` ,params);
// 取消收藏
export const deleteCollectSong = (userId,songId) => get(`collect/deleteCollectSong?userId=${userId}&songId=${songId}`);
export const deleteCollectSongList = (userId,songListId) => get(`collect/deleteCollectSongList?userId=${userId}&songListId=${songListId}`);
// 指定用户的收藏列表
export const getCollectOfUserId = (userId) => get(`collect/collectOfUserId?userId=${userId}`);

//获取用户的收藏歌曲，返回一个list
export const collectSongOfUserId = (userId) => get(`collect/collectSongOfUserId?userId=${userId}`);
//获取用户的收藏歌单，返回一个list
export const collectSongListOfUserId = (userId) => get(`collect/collectSongListOfUserId?userId=${userId}`);

// ===================关注==================
// 添加关注
export const addFollow = (params) => post(`follow/add`,params);

// 取消关注
export const deleteFollow = (userId, singerId) => get(`follow/deleteByUserIdAndSingerId?userId=${userId}&singerId=${singerId}`);

// 是否关注
export const existFollow = (userId, singerId) => get(`follow/existFollow?userId=${userId}&singerId=${singerId};`)

// ===================播放记录==================
// 添加歌曲最近播放记录
export const addRecentSong = (RecentSong) => post(`recentSong/add`,RecentSong);
// 获取当前用户最近播放列表
export const getRecentSongByUserId = (id) => get(`recentSong/recentSongOfUserId/${id}`);
// 获取指定用户最近播放列表，按播放量降序排列
export const getRecentSongOrderByCount = (id) => get(`recentSong/recentSongOrderByCount/${id}`);
// 推荐歌单
export const recommendSongList = (id) => get(`recentSong/recommendSongList/${id}`);
// 推荐歌手
export const recommendSinger = (id) => get(`recentSong/recommendSinger/${id}`);
