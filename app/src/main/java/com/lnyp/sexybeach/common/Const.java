package com.lnyp.sexybeach.common;

/**
 * img字段返回的是不完整的图片路径src，
 * 需要在前面添加【http://tnfs.tngou.net/image】或者【http://tnfs.tngou.net/img】
 * 前者可以再图片后面添加宽度和高度，如：http://tnfs.tngou.net/image/top/default.jpg_180x120
 * <p/>
 * 网页分享，这里我们有专门的网页版【跨平台支持手机浏览器】
 * 需要在id添加【http://www.tngou.net/tnfs/show/】
 * 结构 http://www.tngou.net/tnfs/show/+id
 * <p/>
 * 如：http://www.tngou.net/tnfs/show/1 http://www.tngou.net/tnfs/show/10
 */
public class Const {

    public static final String BASE_IMG_URL1 = "http://tnfs.tngou.net/image";

    public static final String BASE_IMG_URL2 = "http://tnfs.tngou.net/img";

    public static final int PAGE_SIZE = 30;
}
