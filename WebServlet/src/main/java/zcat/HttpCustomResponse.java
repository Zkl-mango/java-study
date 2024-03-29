package zcat;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.internal.StringUtil;
import servlet.HeroRequest;
import servlet.HeroResponse;
import servlet.HeroServlet;

public class HttpCustomResponse implements HeroResponse {
    private HttpRequest request;
    private ChannelHandlerContext context;

    public HttpCustomResponse(HttpRequest request, ChannelHandlerContext context) {
        this.request = request;
        this.context = context;
    }

    @Override
    public void write(String content) throws Exception {
        if (StringUtil.isNullOrEmpty(content)) {
            return;
        }

        // 创建响应对象
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                // 根据响应体内容大小为response对象分配存储空间
                Unpooled.wrappedBuffer(content.getBytes("UTF-8")));

        // 获取响应头
        HttpHeaders headers = response.headers();
        String uri = request.uri();
        if(uri.contains(".html") || uri.contains(".js") || uri.contains(".css") || uri.contains(".image")) {
            headers.set(HttpHeaderNames.CONTENT_TYPE, "text/html");
        } else {
            // 设置响应体类型
            headers.set(HttpHeaderNames.CONTENT_TYPE, "text/json");
        }
        // 设置响应体长度
        headers.set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        // 设置缓存过期时间
        headers.set(HttpHeaderNames.EXPIRES, 0);
        // 若HTTP请求是长连接，则响应也使用长连接
        if (HttpUtil.isKeepAlive(request)) {
            headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        // 将响应写入到Channel
        context.writeAndFlush(response);


    }
}
