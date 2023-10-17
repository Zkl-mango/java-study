package zcat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import servlet.HeroRequest;
import servlet.HeroResponse;
import servlet.HeroServlet;

import java.util.Map;

public class CustomHandler extends ChannelInboundHandlerAdapter {

    private Map<String, HeroServlet> nameToServletMap;
    private Map<String, String> nameToClassNameMap;

    public CustomHandler(Map<String, HeroServlet> nameToServletMap, Map<String, String> nameToClassNameMap) {
        this.nameToServletMap = nameToServletMap;
        this.nameToClassNameMap = nameToClassNameMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            String uri = request.uri();
            // 解析出要访问的Servlet名称
            String servletName = "";

            if(uri.contains("/") ){
                servletName= uri.substring(uri.lastIndexOf("/") + 1);
                if(uri.contains("?")) {
                    servletName = servletName.substring(0,servletName.indexOf("?"));
                }
                if(servletName.contains(".html") || servletName.contains(".js") || servletName.contains(".css") || servletName.contains(".image")) {
                    //如果为静态资源
                    System.out.println(uri);
                    HeroServlet servlet = (HeroServlet) Class.forName("zcat.webapp.ResourceServlet").newInstance();
                    // 代码走到这里，servlet肯定不空
                    HeroRequest req = new HttpCustomRequest(request);
                    HeroResponse res = new HttpCustomResponse(request, ctx);
                    // 根据不同的请求类型，调用servlet实例的不同方法
                    if (request.method().name().equalsIgnoreCase("GET")) {
                        servlet.doGet(req, res);
                    } else if(request.method().name().equalsIgnoreCase("POST")) {
                        servlet.doPost(req, res);
                    }
                    ctx.close();
                    return;
                } else if(uri.contains("?") && uri.contains("/")) {
                    //如果不是静态资源
                    //去掉后缀，大小写转换为小写
                    servletName = servletName.replace(".do","").replace(".action","").toLowerCase();
                }
            }
            HeroServlet servlet = new DefaultCustomServlet();
            //第一次访问，Servlet是不会被加载的
            //初始化加载的只是类全限定名称，懒加载
            //如果访问Servlet才会去初始化它对象
            if (nameToServletMap.containsKey(servletName)) {
                servlet = nameToServletMap.get(servletName);
            } else if (nameToClassNameMap.containsKey(servletName)) {
                if (nameToServletMap.get(servletName) == null) {
                    synchronized (this) {
                        if (nameToServletMap.get(servletName) == null) {
                            // 获取当前Servlet的全限定性类名
                            String className = nameToClassNameMap.get(servletName);
                            // 使用反射机制创建Servlet实例
                            servlet = (HeroServlet) Class.forName(className).newInstance();
                            // 将Servlet实例写入到nameToServletMap
                            nameToServletMap.put(servletName, servlet);
                        }
                    }
                }
            } //  end-else if

            // 代码走到这里，servlet肯定不空
            HeroRequest req = new HttpCustomRequest(request);
            HeroResponse res = new HttpCustomResponse(request, ctx);
            // 根据不同的请求类型，调用servlet实例的不同方法
            if (request.method().name().equalsIgnoreCase("GET")) {
                servlet.doGet(req, res);
            } else if(request.method().name().equalsIgnoreCase("POST")) {
                servlet.doPost(req, res);
            }
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
