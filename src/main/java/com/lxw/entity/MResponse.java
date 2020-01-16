package com.lxw.entity;

import lombok.Data;

/**
 * @author lixiewen
 * @create 2019-10-13 18:22
 */
@Data
public class MResponse {
    /**
     * 0表示成功 1表示失败
     */
    private Integer code;
    private Object data;
    private String msg;

    public MResponse success(Object data) {
        MResponse mResponse = new MResponse();
        mResponse.setCode(0);
        mResponse.setData(data);
        return mResponse;
    }
    public MResponse success(Object data,String msg) {
        MResponse mResponse = new MResponse();
        mResponse.setCode(0);
        mResponse.setData(data);
        mResponse.setMsg(msg);
        return mResponse;
    }

    public MResponse failed(Object data) {
        MResponse mResponse = new MResponse();
        mResponse.setCode(1);
        mResponse.setData(data);
        return mResponse;
    }
    public MResponse failed(Object data,String msg) {
        MResponse mResponse = new MResponse();
        mResponse.setCode(1);
        mResponse.setData(data);
        mResponse.setMsg(msg);
        return mResponse;
    }

}
