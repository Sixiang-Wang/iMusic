import {get, post} from "./http"
import songList from "../pages/SongList.vue";
import Axios from "axios";
import style from "../components/Style.vue";


// ============歌手相关=============
// 查询歌手
export const getAllSinger = () => get(`user/allUser`);

export const getSingerById = (id) => get(`user/selectByPrimaryKey?id=${id}`)

export const getOneSingerByName = (name) => get(`user/getOneUserWithName?name=${name}`)
// ============歌曲相关=============
// 根据歌手id查询歌曲
export const songOfUserId = (id) => get(`song/songOfUserId?userId=${id}`);
// 根据歌曲id查询歌曲对象
export const songOfSongId = (id) => get(`song/detail/addPrefix?songId=${id}`)
// 编辑歌曲
export const updateSong = (params) => post(`song/update`, params);
// 删除歌曲
export const delSong = (id) => get(`song/delete?id=${id}`)
// 根据歌曲名字模糊查询歌曲
export const likeSongOfName = (keywords) => get(`song/likeSongOfName?songName=${keywords}`);
// 添加歌曲播放次数
export const addNums = (songId) => get(`/song/addNums?songId=${songId}`);
// 查询热门歌曲
export const getTopSong = () => get(`song/topSong`);
// 根据风格查询歌曲
export const songLikeStyle = (style) => get(`song/songOfStyle?style=${style}`);

// ===================== 创作周报 =======================

// 用户播放量最多的歌
export const getPopularSongOfUser = (userId) => get(`song/popularSongOfUser?userId=${userId}`);

// 用户被收藏最多的歌
export const getPopularCollectedSongOfUser = (userId) => get(`song/popularCollectedSongOfUser?userId=${userId}`);
// 用户创作的评分最高的歌单
export const getBestSongListOfUser = (userId) => get(`songList/bestSongListOfUser?userId=${userId}`);

// 用户粉丝数量
export const getFansCountByUserId = (userId) => get(`follow/getFansCountByUserId?userId=${userId}`);
// ============歌单相关==============
// 查询歌曲
export const getAllSongList = () => get(`songList/allSongList`);//获得所有歌单
// 根据id获取歌单
export const getSongListById = (id) => get(`songList/selectByPrimaryKey?id=${id}`);

export const selectSongListByUserId = (userId) => get(`songList/selectByUserId?userId=${userId}`);
//添加歌单
export const setSongList = (params) => post(`songList/add`, params);

//根据歌单标题模糊查询歌单
export const likeTitle = (keywords) => get(`songList/likeTitle?title=${keywords}`);
//删除歌单
export const deleteSongList = (id) => get(`songList/delete?id=${id}`)
// 编辑歌单
export const updateSongList = (params) => post(`songList/update`, params);

export const songListLikeStyle = (style) => get(`songList/likeStyle?style=${style}`);

// =============歌单的歌曲相关========
// 根据歌曲id查询歌曲列表
export const listSongDetail = (songListId) => get(`listSong/detail?songListId=${songListId}`);
// 给歌单中添加歌曲
export const addListSong = (params) => post(`listSong/add`, params);
// 删除歌单中的歌曲
export const deleteListSong = (songId, songListId) => get(`listSong/delete?songId=${songId}&songListId=${songListId}`);
// ============用户相关==============

// 查询用户
export const getAllUser = () => get(`user/allUser`);

// 注册
export const SignUp = params => post(`user/add`, params);

// 发送验证码
export const validate = email => get(`mail/sendMail?to=${email}`);

// 登录
export const LoginIn = params => post(`user/login`, params);
export const preLogin = () => post(`user/preLogin`)
export const logout = () => post(`user/logout`)
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
export const commitRank = (param) => post(`rank/add`, param);
//获取歌单的平均分
export const getRankOfSongListId = (songListId) => get(`rank/rankOfSongListId?songListId=${songListId}`)
export const getRankOfSongListIdAndUserId = (params) => post(`rank/rankOfSongListIdAndUserId`, params)
// =============== 评论 ==================
// 提交评论
export const setComment = (params) => post(`comment/add`, params);
// 点赞
export const setLike = (params) => post(`comment/like`, params);
// 返回当前歌单或歌曲的评论区列表
export const getCommentOfSong = (songId) => get(`comment/commentOfSongId?songId=${songId}`);

export const getCommentOfSongList = (songListId) => get(`comment/commentOfSongListId?songListId=${songListId}`);
// 是否已点赞
export const existCommentUp = (userId, commentId) => get(`commentUp/exist?userId=${userId}&commentId=${commentId}`);
// 点赞提交
export const addCommentUp = (params) => post(`commentUp/add`, params);

export const deleteComment = (id) => get(`comment/delete?id=${id}`);
// 取消点赞
export const deleteCommentUp = (userId, commentId) => get(`commentUp/delete?userId=${userId}&commentId=${commentId}`);
// 评论的点赞数量
export const getUpCount = (commentId) => get(`commentUp/sumUp?commentId=${commentId}`);

// =============== 收藏 ==================
// 判断歌曲是否已收藏
export const existCollectSong = (userId, songId) => get(`collect/existCollectSong?userId=${userId}&songId=${songId}`);

// 判断歌单是否已收藏
export const existCollectSongList = (userId, songListId) => get(`collect/existCollectSongList?userId=${userId}&songListId=${songListId}`);
// 新增收藏
export const setCollect = (params) => post(`collect/add`, params);
// 取消收藏
export const deleteCollectSong = (userId, songId) => get(`collect/deleteCollectSong?userId=${userId}&songId=${songId}`);
export const deleteCollectSongList = (userId, songListId) => get(`collect/deleteCollectSongList?userId=${userId}&songListId=${songListId}`);
// 指定用户的收藏列表
export const getCollectOfUserId = (userId) => get(`collect/collectOfUserId?userId=${userId}`);

//获取用户的收藏歌曲，返回一个list
export const collectSongOfUserId = (userId) => get(`collect/collectSongOfUserId?userId=${userId}`);
//获取用户的收藏歌单，返回一个list
export const collectSongListOfUserId = (userId) => get(`collect/collectSongListOfUserId?userId=${userId}`);
// 歌单的收藏量
export const collectNumOfSongList = (songListId) => get(`collect/songListCollectNum?songListId=${songListId}`);
// 歌曲的收藏量
export const collectNumOfSong = (songId) => get(`collect/songCollectNum?songId=${songId}`);
// ===================关注==================
// 添加关注
export const addFollow = (params) => post(`follow/add`, params);

// 查找用户的关注歌手
export const getFollow = (userId) => get(`follow/getByUserId?userId=${userId}`);

// 取消关注
export const deleteFollow = (userId, singerId) => get(`follow/deleteByUserIdAndSingerId?userId=${userId}&singerId=${singerId}`);

// 是否关注
export const existFollow = (userId, singerId) => get(`follow/existFollow?userId=${userId}&singerId=${singerId}`);

// 获取歌手的粉丝量
export const getFansCountBySingerId = (singerId) => get(`follow/getCountBySingerId?singerId=${singerId}`);


// ===================播放记录==================
// 添加歌曲最近播放记录
export const addRecentSong = (RecentSong) => post(`recentSong/add`, RecentSong);
// 获取当前用户最近播放列表
export const getRecentSongByUserId = (id) => get(`recentSong/recentSongOfUserId/${id}`);
// 获取指定用户最近播放列表，按播放量降序排列
export const getRecentSongOrderByCount = (id) => get(`recentSong/recentSongOrderByCount/${id}`);
// 推荐歌单
export const recommendSongList = (id) => get(`recentSong/recommendSongList/${id}`);
// 推荐歌手
export const recommendSinger = (id) => get(`recentSong/recommendSinger/${id}`);

// ================== 投诉与申诉 ===============

// 投诉歌单、歌曲
export const addComplaint = (params) => post(`complaint/add`, params);

// 用户所有被投诉的（歌曲
export const allComplaintAgainstUser = (userId) => get(`/complaint/allComplaintAgainstUser?userId=${userId}`);

// 申诉
export const appealComplaint = (params) => post(`/complaint/appealComplaint`, params);

// ================ 消息提醒 ==============

// 添加信息
export const addMessage = (params) => post(`message/add`, params);

// 使消息变为已读

export const readMessage = (id) => post(`message/read?id=${id}`);

// 删除消息

export const deleteMessage = (id) => get(`message/delete?id=${id}`);

// 查找所有信息

export const getAllMessage = (to) => get(`message/allMessage?to=${to}`);

// 查找所有未读信息
export const getAllMessageUnread = (to) => get(`message/allMessageUnread?to=${to}`);

// 得到所有未读消息的数量
export const getMessageUnreadNum = (to) => get(`message/messageUnreadNum?to=${to}`);

