package zcat;


import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import servlet.HeroRequest;

import java.util.List;
import java.util.Map;

/**
 * HeroCat中对HeroRequest规范的默认实现
 */
public class HttpCustomRequest implements HeroRequest {

    private HttpRequest request;

    public HttpCustomRequest(HttpRequest request) {
        this.request = request;
    }

    @Override
    public String getUri() {
        return request.uri();
    }

    @Override
    public String getPath() {
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        return decoder.path();
    }

    @Override
    public String getMethod() {
        return request.method().name();
    }

    @Override
    public Map<String, List<String>> getParameters() {
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        return decoder.parameters();
    }

    @Override
    public List<String> getParameters(String name) {
        return getParameters().get(name);
    }

    @Override
    public String getParameter(String name) {
        List<String> parameters = getParameters(name);
        if (parameters == null || parameters.size() == 0) {
            return null;
        }
        return parameters.get(0);
    }
}
