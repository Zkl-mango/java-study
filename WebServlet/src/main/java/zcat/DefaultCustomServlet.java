package zcat;

import servlet.HeroRequest;
import servlet.HeroResponse;
import servlet.HeroServlet;

public class DefaultCustomServlet  extends HeroServlet {
    @Override
    public void doGet(HeroRequest request, HeroResponse response) throws Exception {
        String uri = request.getUri();
        response.write("404 - " + (uri.contains("?")?uri.substring(0,uri.lastIndexOf("?")):uri));
    }


    @Override
    public void doPost(HeroRequest request, HeroResponse response) throws Exception {
        doGet(request, response);
    }
}
