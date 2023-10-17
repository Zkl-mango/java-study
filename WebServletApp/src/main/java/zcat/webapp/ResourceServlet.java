package zcat.webapp;

import servlet.HeroRequest;
import servlet.HeroResponse;
import servlet.HeroServlet;
import zcat.CustomServer;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

//获取静态资源
public class ResourceServlet extends HeroServlet {
    @Override
    public void doGet(HeroRequest request, HeroResponse response) throws Exception {
        String uri = request.getUri();
        String path = request.getPath();
        String method = request.getMethod();
        String name = request.getParameter("name");

        String content = "uri = " + uri + "\n" +
                "path = " + path + "\n" +
                "method = " + method + "\n" +
                "param = " + name;
        path = path.replace("/","");
        String path1 = this.getClass().getClassLoader().getResource(path).getPath();
        FileInputStream fis = new FileInputStream(path1);
        InputStream in = fis;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        String fileContent = out.toString();
        reader.close();
        response.write(fileContent);
    }

    @Override
    public void doPost(HeroRequest request, HeroResponse response) throws Exception {

    }
}
