package com.m.mvvmkotlin.sample

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @Created by majian
 * @Date : 2018/12/20
 * @Describe :
 * 话题-摘要 {
followCount (integer, optional): 关注人数 ,
followed (integer, optional): 是否已经关注，0：未关注，1：已经关注 ,
memberCount (integer, optional): 参与人数 ,
publishTime (integer, optional): 发布时间 ,
status (integer, optional): 状态 1:发布 2:下架 ,
title (string, optional): 标题 ,
topicId (integer, optional): 话题ID ,
topicImageUrl (string, optional): 话题图片URL
}
 */
@Parcelize
data class DemoBean(var followCount: Int,
                    var followed: Int,
                    var memberCount: Int,
                    var publishTime: Long,
                    var status: Int,
                    var title: String,
                    var topicId: Int,
                    var topicImageUrl: String) : Parcelable