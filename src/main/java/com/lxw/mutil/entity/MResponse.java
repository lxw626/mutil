package com.lxw.mutil.entity;

import lombok.Data;

/**
 * @author lixiewen
 * @create 2019-10-13 18:22
 */
@Data
public class MResponse {
    /**
     * 100表示失败 200表示成功
     */
    private Integer code;
    private Object data;
    private String msg;
    public MResponse success() {
        MResponse mResponse = new MResponse();
        mResponse.setCode(200);
        return mResponse;
    }
    public MResponse success(Object data) {
        MResponse mResponse = new MResponse();
        mResponse.setCode(200);
        mResponse.setData(data);
        return mResponse;
    }
    public MResponse success(Object data,String msg) {
        MResponse mResponse = new MResponse();
        mResponse.setCode(200);
        mResponse.setData(data);
        mResponse.setMsg(msg);
        return mResponse;
    }

    public MResponse failed() {
        MResponse mResponse = new MResponse();
        mResponse.setCode(100);
        return mResponse;
    }
    public MResponse failed(Object data) {
        MResponse mResponse = new MResponse();
        mResponse.setCode(100);
        mResponse.setData(data);
        return mResponse;
    }
    public MResponse failed(Object data,String msg) {
        MResponse mResponse = new MResponse();
        mResponse.setCode(100);
        mResponse.setData(data);
        mResponse.setMsg(msg);
        return mResponse;
    }

    public MResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
