package com.example.myapplication.network.util

/**
 * @author wyl
 * @description:
 * @date :2020/9/29 15:35
 */
class Constant {
    companion object {
        const val BASE_URL = "http://192.168.1.2:8080/"

        /**
         * 正常返回
         */
        const val SUCCESS = 200

        /**
         * 未知错误
         */
        const val UNKNOWN = 1000

        /**
         * 解析错误
         */
        const val PARSE_ERROR = 1001

        /**
         * 网络错误
         */
        const val NETWORK_ERROR = 400

        /**
         * 服务器内部错误
         */
        const val HTTP_ERROR = 500

        /**
         * 解析错误
         */
        const val S_PARSE_ERROR = "数据解析错误"

        /**
         * 网络错误
         */
        const val S_NETWORK_ERROR = "网络错误"

        /**
         * 服务器内部错误
         */
        const val S_HTTP_ERROR = "无法连接服务器"

        /**
         * 未知错误
         */
        const val S_UNKNOWN = "未知错误"



    }
}

class Constant {
    companion object{
        val BASE_URL = ""
    }
}