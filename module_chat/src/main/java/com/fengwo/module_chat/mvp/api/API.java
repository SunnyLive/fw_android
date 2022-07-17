package com.fengwo.module_chat.mvp.api;

public interface API {
    String GET_CONTACT_LIST = "/api/community/user/getUserFriendList"; // 获取通讯录列表

    String GET_RECOMMEND_CIRCLE_LIST = "/api/community/discuss_circle_info"; // 获取推荐圈子列表名

    String GET_CIRCLE_LIST = "/api/community/discuss_circle_card"; // 获取某一个圈子的信息列表
    String DELETE_BLACK = "/api/user/blacklist/blacklist/{id}";//取消黑名单
    String GET_CHAT_HOME_BANNER = "/api/base/base_adverts"; // 获取首页轮播图信息
    String ADD_BLACKLIST = "api/user/blacklist/blacklist/{id}";//拉黑
    String SEARCH_HOT = "api/community/search_hot"; // 热门搜索
    String SEARCH_CARD = "api/community/search_card"; // 搜索
    String IS_BLACK = "api/user/blacklist/is_black/{id}";//是否已拉黑
    String GET_CARD_LIST = "/api/community/discuss_circle_card/infos"; // 获取卡片列表
    String GET_CIRCLE_MEMBER = "/api/community/discuss_im_group_info_preview"; // 卡片获取群成员信息
    String GET_ALL_CIRCLES = "/api/community/discuss_circle_infos"; // 获取所有圈子
    String SET_USER_LOVE_CIRCLE = "/api/community/discuss_circle_user_love"; // 设置用户喜欢的圈子
    String GET_TAG_BY_CIRCLE = "/api/community/discuss_circle_tag_info/{circleId}"; // 根据圈子获取Tag列表
    String POST_CARD = "/api/community/discuss_circle_card"; // 发布卡片/存草稿
    String CARD_LIKE = "/api/community/CardAction/card_like/{id}"; // 卡片点赞
    String ADD_ATTENTION = "api/user/attention/attention/{id}"; // 关注用户
    String BUNKO_2042002 = "/api/bunko/2042002"; // 关注用户
    String REMOVE_ATTENTION = "api/user/attention/attention/{id}";//取消关注
    String GET_COMMENT_LIST = "/api/community/CardAction/comments"; // 获取评论列表
    String LIKE_COMMENT = "/api/community/CardAction/comment_like/{id}"; // 评论点赞/取消点赞
    String GET_SECOND_COMMENT = "/api/community/CardAction/second_comments"; // 获取二级评论列表
    String COMMENT = "/api/community/CardAction/comment"; // 发表评论
    String GET_GROUP_INFO = "/api/community/groupAction/group_users/{id}"; // 获取群消息
    String FORBIDDEN_GROUP_TALK = "/api/community/groupAction/forbidden/{id}"; // 群禁言
    String UNFORBIDDEN_GROUP_TALK = "/api/community/groupAction/unforbidden/{id}"; // 取消群禁言
    String GET_GROUP_MEMBER_LIST = "/api/community/groupAction/group_users"; // 获取群成员列表
    String ENTER_GROUP = "/api/community/discuss_im_group_users/{groupId}"; // 进入群聊
    String DELETE_GROUP = "/api/community/groupAction/group_user_leave/{id}"; // 进入群聊
    String DELETE_GROUP_MEMBER = "/api/community/groupAction/group_delete_user"; // 删除群成员
    String CREATE_GROUP = "/api/community/groupAction/small_group"; // 创建私群
    String EDIT_NOTICE = "/api/community/groupAction/group_notice"; // 修改群公告
    String GET_MERCHANT_LIST = "/api/community/merchant/getDiscussMerchantInfoPage"; // 获取商家信息
    String GROUP_DISTURB = "/api/community/groupAction/group_disturb"; // 群消息免打扰

    String GET_CARD_TAG_LIST = "/api/community/discuss_circle_card_tag_info/{cardId}"; // 获取卡片对应的标签列表
    String GET_CARD_FROM_TAGS = "/api/community/discuss_circle_card/from_tags"; // 通过标签获取卡片（相似卡片）

    String GET_LIST_CARD_COMMENT = "/api/community/listCardCommentMsg"; //互动通知(评论)
    String GET_LIST_CARD_LIKE = "/api/community/listCardLikeMsg"; // 互动通知(点赞)
    String DELETE_CARD_COMMENT = "api/community/CardAction/deleteComment/{id}"; //互动通知(删除评论)
    String DELETE_CARD_LIKE = "/api/community/CardAction/card_like/{id}"; //互动通知(删除点赞或者添加点赞)
    //    String ADD_VISITOR_RECORD = "/api/community/discussVistorLogs/insertVistorLog"; // 添加访客记录
    String GET_LIST_VISITOR_RECORD = "/api/community/discussVistorLogs/listVistorLog"; // 最近访客
    String GET_LIST_SYSTEM_MESSAGE = "/api/community/discussSystemMsg/listSystemMsg"; //系统消息
    String CLEAR_LIST_SYSTEM_MESSAGE = "/api/community/discussSystemMsg/clearSystemMsg"; //清空系统消息
    String DELETE_LIST_SYSTEM_MESSAGE = "/api/community/discussSystemMsg/deleteInteractMsg/{id}"; //删除系统消息（互动通知赞/评论）
    String GET_LIST_GREET_MESSAGE = "/api/community/getMyDiscussGreet"; //打招呼列表消息
    String CLEAR_LIST_GREET_MESSAGE = "/api/community/cleareDiscussGreet"; //清空打招呼列表


    String GET_MY_ATTENTION_NUM = "/api/user/attention/attention_num"; //我的关注数
    String GET_MY_FANS_NUM = "/api/user/attention/fans"; // 我的粉丝数
    String GET_MY_FRIENDS_NUM = "/api/user/attention/friendNum"; // 我的好友数目

    String CARD_TOP = "/api/community/discuss_circle_card_top"; // 卡片置顶/取消置顶
    String CARD_POWER = "/api/community/discuss_circle_card_power"; // 卡片权限
    String CARD_DELETE = "/api/community/discuss_circle_card_remove"; // 卡片删除
    String CARD_DRAFT_POST = "api/community/discuss_circle_card_draft_publish"; // 草稿 发布
    String CARD_DETAIL_EDIT = "api/community/discuss_circle_card/{id}"; // 获取草稿 卡片详情



    String OFFICIAL_NEW_POST = "api/base/app/official_news/page"; //分页获取官方消息接口
    String OFFICIAL_MARK_POST = "api/base/app/official_news/mark_read"; //官方消息标记已读



    String MARK_GREET_MESSAGE = "api/base/app/official_news/clean_all"; //清空系统消息列表

//    String GET_TAG_INFO = "/api/community/discuss_circle_tag_info/{circleId}"; // 获取某圈标签列表

}
