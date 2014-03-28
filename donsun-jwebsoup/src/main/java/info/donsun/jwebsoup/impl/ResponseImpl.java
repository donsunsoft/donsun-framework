/*
 * Copyright (c) 2013, FPX and/or its affiliates. All rights reserved.
 * Use, Copy is subject to authorized license.
 */
package info.donsun.jwebsoup.impl;

import info.donsun.jwebsoup.Connection.Parser;
import info.donsun.jwebsoup.Connection.Request;
import info.donsun.jwebsoup.Connection.Response;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Implementation of {@link Response}.
 * 
 * @author Steven Deng
 * @version 1.0
 * @since 1.0
 * @date 2013-12-30
 * 
 */
public class ResponseImpl extends BaseImpl<Response> implements Response {

    private int statusCode;
    private String statusMessage;
    private byte[] bodyAsBytes;
    private String contentType;
    private long contentLength;

    private Request request;

    public static ResponseImpl create(Request request) {
        return new ResponseImpl(request);
    }

    public ResponseImpl(Request request) {
        this.request = request;
    }

    public int statusCode() {
        return statusCode;
    }

    @Override
    public Response statusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public String statusMessage() {
        return statusMessage;
    }

    @Override
    public Response statusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
        return this;
    }

    public String contentType() {
        return contentType;
    }

    @Override
    public Response contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public long contentLength() {
        return contentLength;
    }

    @Override
    public Response contentLength(long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    public <T> T parse(Parser<T> parser) throws IOException {
        return parser.parse(request, this);
    }

    public String body() {
        if (bodyAsBytes == null) {
            return null;
        }

        // charset gets set from header on execute, and from meta-equiv on
        // parse. parse may not have happened yet
        return charset != null ? new String(bodyAsBytes, Charset.forName(charset)) : new String(bodyAsBytes);
    }

    public byte[] bodyAsBytes() {
        return bodyAsBytes;
    }

    @Override
    public Response bodyAsBytes(byte[] bodyAsBytes) {
        this.bodyAsBytes = bodyAsBytes;
        return this;
    }

    @Override
    public String toString() {
        return body();
    }

}
