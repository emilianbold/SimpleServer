/*
 * Copyright 2018-2019 Niels Gron and Contributors All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package quicksilver.commons.app;

import quicksilver.commons.config.ConfigWebServer;
import spark.Response;
import spark.Service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SimpleWebServer {

    protected final Service webServer = Service.ignite();

    protected SimpleApplication application;
    protected ConfigWebServer configWebServer;

    public SimpleWebServer(SimpleApplication app) {

        application = app;
        configWebServer = app.getConfigWebServer();

    }

    protected void initServer() {

        webServer.staticFiles.location("/public");

        int port = Integer.valueOf(configWebServer.getPort(ConfigWebServer.DEFAULT_WEBSERVER_PORT));
        int maxThreads = Integer.valueOf(configWebServer.getMaxThreads(ConfigWebServer.DEFAULT_WEBSERVER_MAX_THREADS));
        int minThreads = Integer.valueOf(configWebServer.getMinThreads(ConfigWebServer.DEFAULT_WEBSERVER_MIN_THREADS));
        int timeOutMillis = Integer.valueOf(configWebServer.getTimeOut(ConfigWebServer.DEFAULT_WEBSERVER_TIMEOUT));

        webServer.port(port);
        webServer.threadPool(maxThreads, minThreads, timeOutMillis);

    }

    protected void initRoutes() {

    }

    protected void initBeforeFilters() {

    }

    protected void initAfterFilters() {
        if ( enableGzipResponses() ) {
            webServer.after("*", (request, response) -> {
                response.header("Content-Encoding", "gzip");
            });
        }
    }

    protected boolean enableGzipResponses() {
        return false;
    }

    protected SimpleApplication getApplication() {
        return application;
    }

    public void startServer() {

        initServer();
        initBeforeFilters();
        initRoutes();
        initAfterFilters();

        webServer.init();
    }

    public static void writeBinaryToResponse(byte[] byteArray, String acceptType, String fileName, Response response, boolean bCompressed)
        throws IOException {

        response.raw().setContentType(acceptType);

        if ( bCompressed ) {
            response.raw().setHeader("Content-Disposition", "attachment; filename=" + fileName + ".zip");

            ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(response.raw().getOutputStream()));
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(byteArray));

            ZipEntry zipEntry = new ZipEntry(fileName);

            zipOutputStream.putNextEntry(zipEntry);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bufferedInputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, len);
            }

            zipOutputStream.flush();
            zipOutputStream.close();
        } else {
            response.raw().setHeader("Content-Disposition", "attachment; filename=" + fileName);

            response.raw().getOutputStream().write(byteArray);
            response.raw().flushBuffer();
        }

    }

}
