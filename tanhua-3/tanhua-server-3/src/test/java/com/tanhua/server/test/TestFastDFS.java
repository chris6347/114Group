package com.tanhua.server.test;

import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFastDFS {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private FdfsWebServer fdfsWebServer;

    @Test
    public void testUpload() throws IOException {
        File file = new File("/Users/chris/files/video01.mp4");
        StorePath storePath = fastFileStorageClient.uploadFile(FileUtils.openInputStream(file), file.length(), "mp4", null);
        String url =fdfsWebServer.getWebServerUrl()+"/"+ storePath.getFullPath();
        System.out.println(url);
    }

}
