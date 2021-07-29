package com.tanhua.domain.vo;

import lombok.Builder;
import lombok.Data;

/*
* 1.作为exception的Message参数
* 2.作为ResponseEntity.status(...).body(中的返回值,表示error)
* */
@Data
@Builder // 加上构造者模式
public class ErrorResult {

    private String errCode;
    private String errMessage;

    public static ErrorResult error(String errCode,String errMessage){
        return ErrorResult.builder().errCode(errCode).errMessage(errMessage).build();
    }

    public static ErrorResult error(){
        return ErrorResult.builder().errCode("999999").errMessage("系统异常,稍后再试").build();
    }

    public static ErrorResult fail(){
        ErrorResultBuilder builder = ErrorResult.builder();
        builder = builder.errCode("000000");
        builder = builder.errMessage("发送验证码失败");
        return builder.build();
    }

    // duplicate:复制
    public  static ErrorResult duplicate(){
        return ErrorResult.builder().errCode("000001").errMessage("上一次发送的验证码还未失效").build();
    }

    public static ErrorResult loginError(){
        return ErrorResult.builder().errCode("000002").errMessage("验证码失效").build();
    }

    public static ErrorResult faceError(){
        return ErrorResult.builder().errCode("000003").errMessage("图片非人像,请重新上传!").build();
    }

    public static ErrorResult validateCodeError(){
        return ErrorResult.builder().errCode("000004").errMessage("验证码不正确").build();
    }

}
