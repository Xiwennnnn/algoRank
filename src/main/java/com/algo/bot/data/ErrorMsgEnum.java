package com.algo.bot.data;

public enum ErrorMsgEnum {
    SERVER_ERROR("😿服务器出现错误啦，待会再试试哦~"),
    FORMAT_ERROR("😿输入的格式有问题，请检查输入是否正确，也可以输入【/帮助】查看帮助哦~"),
    NOT_SUPPORTED_MUTI_QUERY("🥲该功能暂时不支持多人查询，请一个一个查询吧~"),
    EMPTY_ARGS("😣参数不能为空，请检查输入是否正确~"),
    QUERY_FAILED("😿查询失败，请检查输入是否正确~"),
    AUTH_NOT_HANDLED("😿你没有权限执行该操作，有问题请联系管理员哦~"),
    USER_MODIFY_FAILED("👻用户信息修改失败了，原因是："),
    USER_ADD_FAILED("👻用户添加失败了，原因是："),
    USER_DELETE_FAILED("👻用户删除失败了，原因是："),
    USER_QQ_CANNOT_MODIFIED("😿QQ号码不能修改哦~，想要修改请联系管理员或者删除后再添加~"),
    UNDEFINE_CMD("😿未定义的命令，请检查输入是否正确~"),
    UNKOWN_OJ("🥲这个OJ我不认识，或者还没有支持哦~"),
    GPT_ERROR("😣GPT出错啦，请稍后再试~"),
    NETWORK_ERROR("😿目标网站连接异常，请再试试吧~");

    public final String msg;
    ErrorMsgEnum(String msg) {
        this.msg = msg;
    }
}
