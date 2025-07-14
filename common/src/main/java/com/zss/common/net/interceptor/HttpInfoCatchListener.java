package com.zss.common.net.interceptor;

/**
 * http info catch callback
 * Created by linxiao on 2017-01-05.
 */
public interface HttpInfoCatchListener {

    void onInfoCaught(HttpInfoEntity entity);
}
