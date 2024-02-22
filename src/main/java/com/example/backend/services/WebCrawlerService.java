package com.example.backend.services;

import com.example.backend.utils.Utils;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class WebCrawlerService {

    public void downloadImages(Map<String, String> params, HttpServletResponse response) {
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment;filename=File.zip");
        try {
            AtomicInteger i = new AtomicInteger();
            if (!Files.exists(Path.of("./images"))) {
                Utils.createDirectory("./images");
            }
            findImageLinks(params.get("url"), params.get("selector"), params.get("imageAttribute")).forEach(link -> {
                try {
                    FileUtils.copyURLToFile(new URL(link), new File("./images/" + i + ".jpg"), 20000, 20000);
                    i.getAndIncrement();
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                }
            });
            List<String> allFiles = new ArrayList<>();
            Stream.of(Objects.requireNonNull(new File("./images").listFiles())).forEach(file -> {
                allFiles.add(file.getPath());
            });
            zipping("./images");
            byte[] byteArr = FileUtils.readFileToByteArray(new File("./images/File.zip"));
            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(byteArr));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
            FileUtils.deleteDirectory(new File("./images"));
            IOUtils.closeQuietly(response.getOutputStream());
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    public List<String> findImageLinks(String url, String selector, String imageAttribute) {
        try {
            Document document = Jsoup
                    .connect(url)
                    .userAgent("client")
                    .timeout(20000)
                    .get();
            return document
                    .select(selector != null && !selector.isEmpty() ? selector.concat(" img") : "img")
                    .eachAttr(imageAttribute != null && !imageAttribute.isEmpty() ? imageAttribute : "src")
                    .stream().map(each -> Arrays.stream(each.split(",")).map(String::trim).collect(Collectors.toList()))
                    .flatMap(Collection::stream)
                    .toList()
                    .stream()
                    .filter(link -> !link.startsWith("data:") && !link.contains(".png"))
                    .map(link -> {
                        String http = url.split("//")[0];
                        if (link.startsWith("//")) {
                            link = http.concat(link);
                        } else if (link.startsWith("/")) {
                            try {
                                link = http.concat("//") + new URL(url).getHost().concat(link);
                            } catch (MalformedURLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        link = link.replaceAll(" ", "%20");
                        if (link.contains(".jpg")) {
                            link = link.substring(0, link.lastIndexOf(".jpg") + 4);
                        } else if (link.contains(".jpeg")) {
                            link = link.substring(0, link.lastIndexOf(".jpeg") + 5);
                        }
                        return link;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

    private void zipping(String folderPath) {
        try {
            List<String> allFiles = new ArrayList<>();
            Stream.of(Objects.requireNonNull(new File(folderPath).listFiles())).forEach(file -> {
                allFiles.add(file.getPath());
            });
            FileOutputStream fos = new FileOutputStream(folderPath + "/File.zip");
            ZipOutputStream zipOutputStream = new ZipOutputStream(fos);
            allFiles.forEach(path -> {
                try {
                    final ZipEntry ze = new ZipEntry(path.substring(path.lastIndexOf("\\") + 1));
                    if (!ze.getName().contains(".zip")) {
                        zipOutputStream.putNextEntry(ze);
                        zipOutputStream.write(Files.readAllBytes(Path.of(path)));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            zipOutputStream.closeEntry();
            zipOutputStream.finish();
            zipOutputStream.flush();
            IOUtils.closeQuietly(zipOutputStream);
//            return Files.readAllBytes(Path.of(folderPath + "/File.zip"));
        } catch (Exception e) {
            LOG.error(e.getMessage());
//            return new byte[0];
        }
    }

    public List<String> getLinks(String url, Integer size, String selector, String imageSelector, String imageAttribute) {
        List<String> links = new ArrayList<>();
        List<String> imageLinks = new ArrayList<>();
        try {
            for (int i = 1; i <= size; i++) {
                Document document = Jsoup
                        .connect(url + "/page/" + i)
                        .userAgent("client")
                        .timeout(20000)
                        .get();
                links.addAll(document
                        .select(selector != null && !selector.isEmpty() ? selector : "a")
                        .eachAttr("href"));
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        links.forEach(link -> {
            imageLinks.addAll(findImageLinks(link, imageSelector, imageAttribute));
            Map<String, String> params = new HashMap<>();
            params.put("url", link);
            params.put("selector", imageSelector);
            saveImages(params);
        });
//        zipping(imageLinks, "./images");
        return new ArrayList<>();
    }

    public void saveImages(Map<String, String> params) {
        try {
            AtomicInteger i = new AtomicInteger();
            if (!Files.exists(Path.of("./images"))) {
                Utils.createDirectory("./images");
            }
            findImageLinks(params.get("url"), params.get("selector"), params.get("imageAttribute")).forEach(link -> {
                try {
                    FileUtils.copyURLToFile(new URL(link), new File("./images/" + new Date().getTime() + ".jpg"), 10000, 10000);
                    i.getAndIncrement();
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                }
            });
//            List<String> allFiles = new ArrayList<>();
//            Stream.of(Objects.requireNonNull(new File("./images").listFiles())).forEach(file -> {
//                allFiles.add(file.getPath());
//            });
//            FileOutputStream fos = new FileOutputStream("./images/File.zip");
//            ZipOutputStream zipOutputStream = new ZipOutputStream(fos);
//            allFiles.forEach(path -> {
//                try {
//                    final ZipEntry ze = new ZipEntry(path.substring(path.lastIndexOf("\\") + 1));
//                    zipOutputStream.putNextEntry(ze);
//                    zipOutputStream.write(Files.readAllBytes(Path.of(path)));
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//            zipOutputStream.closeEntry();
//            zipOutputStream.finish();
//            zipOutputStream.flush();
//            IOUtils.closeQuietly(zipOutputStream);
//            FileUtils.deleteDirectory(new File("./images"));
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    public List<String> getText(String url, Integer size, String selector) {
        List<String> results = new ArrayList<>();
        try {
            for (int i = 0; i < size; i++) {
                Document document = Jsoup
                        .connect(url + "/" + i)
                        .userAgent("client")
                        .timeout(20000)
                        .get();
                results.addAll(document.select(selector).eachText());
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        return results;
    }

    public List<String> getText(String url, String selector) {
        List<String> results = new ArrayList<>();
        try {
            Document document = Jsoup
                    .connect(url)
                    .userAgent("client")
                    .timeout(20000)
                    .get();
            results.addAll(document.select(selector).eachText());
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        return results;
    }
    private final Logger LOG = LoggerFactory.getLogger(getClass());
}
