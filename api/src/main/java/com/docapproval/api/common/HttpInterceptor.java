package com.docapproval.api.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

public class HttpInterceptor implements HandlerInterceptor {

    static final String X_REQID = "x-reqid";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String reqId = getReqId(request);
        response.addHeader(X_REQID, reqId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception exception) throws Exception {
        MDC.put(X_REQID, "-");
    }

    private String getReqId(HttpServletRequest request) {
        String reqId = request.getHeader(X_REQID);
        if(reqId == null) {
            reqId = UUID.randomUUID().toString();
        }
        MDC.put(X_REQID, reqId);
        return reqId;
    }
}
