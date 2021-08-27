package com.hc.nanohttpd.http;

import com.hc.nanohttpd.utils.SharedFile;

import fi.iki.elonen.NanoHTTPD;

import static com.hc.nanohttpd.utils.GlobalData.audioList;
import static com.hc.nanohttpd.utils.GlobalData.fileList;
import static com.hc.nanohttpd.utils.GlobalData.imageList;
import static com.hc.nanohttpd.utils.GlobalData.videoList;

public class HTTPServer extends NanoHTTPD {

    public HTTPServer(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        session.getHeaders();
        System.out.println(session.getUri());
        switch (session.getUri()) {
            case "":
            case "/":
                return responseIndex();
            case "/image":
                return responseList("image");
            case "/audio":
                return responseList("audio");
            case "/video":
                return responseList("video");
            case "/file":
                return responseList("file");
            case "/favicon.ico":
                return newFixedLengthResponse("no icon");
        }
        return response404(session);
    }

    public Response responseIndex() {
        String html = "<!DOCTYPER html>" +
                "<html><body>" +
                "<div style='margin-top:150px'>" +
                "<div style='text-align:center;font-size:50px;margin-top:10px'><a href='/image'> 图片 </a></div>" +
                "<div style='text-align:center;font-size:50px;margin-top:10px'><a href='/audio'> 音频 </a></div>" +
                "<div style='text-align:center;font-size:50px;margin-top:10px'><a href='/video'> 视频 </a></div>" +
                "<div style='text-align:center;font-size:50px;margin-top:10px'><a href='/file'> PDF </a></div>" +
                "</div>" +
                "</body></html>";
        return newFixedLengthResponse(html);
    }

    public Response responseList(String content) {
        StringBuilder html = new StringBuilder("<!DOCTYPER html>" +
                "<html><body>" +
                "<style>" +
                "td{padding:0 30px 0 10px}" +
                "</style>" +
                "<div style='margin-top:150px'>" +
                "<h1 align='center'>文件列表</h1>" +
                "<table align='center' border='1' cellspacing='0'>" +
                "<tr><th>名称</th><th>路径</th></tr>");
        switch (content) {
            case "image":
                for (SharedFile sharedFile : imageList) {
                    html.append("<tr><td>").append(sharedFile.getFilename()).append("</td><td>").append(sharedFile.getFilepath()).append("</td></tr>");
                }
                break;
            case "audio":
                for (SharedFile sharedFile : audioList) {
                    html.append("<tr><td>").append(sharedFile.getFilename()).append("</td><td>").append(sharedFile.getFilepath()).append("</td></tr>");
                }
                break;
            case "video":
                for (SharedFile sharedFile : videoList) {
                    html.append("<tr><td>").append(sharedFile.getFilename()).append("</td><td>").append(sharedFile.getFilepath()).append("</td></tr>");
                }
                break;
            case "file":
                for (SharedFile sharedFile : fileList) {
                    html.append("<tr><td>").append(sharedFile.getFilename()).append("</td><td>").append(sharedFile.getFilepath()).append("</td></tr>");
                }
                break;
        }
        html.append("</table>");
        html.append("</div>" + "</body></html>");
        return newFixedLengthResponse(html.toString());
    }

    //页面不存在，或者文件不存在时
    public Response response404(IHTTPSession session) {
        String builder = "<!DOCTYPE html><html><body>" +
                "404 -- Sorry, Can't Found " + session.getUri() + " !" +
                "</body></html>";
        return newFixedLengthResponse(builder);
    }
}
