package self.vpalepu.bookworm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * BookWorm!
 *
 */
public class BookWorm {
    public static void main( String[] args ) {
      final String targetUrl = stripTrailingSlash(getArgument("worm.page"));
      final String downloadStore = getArgument("worm.downloads");
      final String extensionRegEx = getArgument("worm.extn");
      
      try {
        
        Document doc = Jsoup.connect(targetUrl).get();
        Elements links = doc.getElementsByTag("a");
        final int linkCount = links.size();
        int count = 1;
        for(Element link : links) {
          final String href = link.attr("href");
          String fullHref;
          if(!href.startsWith(targetUrl)) {
            if(href.startsWith("/") || href.startsWith("#")) {
              fullHref = targetUrl + href;
            } else {
              fullHref = targetUrl + '/' + href;
            }
          } else {
            fullHref = href;
          }
          downloadFile(fullHref, createDownloadStore(downloadStore), 
              extensionRegEx, count,  linkCount);
          count += 1;
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    
    public static String getArgument(final String argName) {
      final String argValue = System.getProperty(argName);
      if(argValue == null) {
        System.err.println("Provide the following argument: -D" + argName);
        System.exit(1);
      }
      return argValue;
    }
    
    public static String createDownloadStore(final String downloadStoreName) {
      File downloadStore = new File(downloadStoreName);
      if(!downloadStore.exists()) {
        downloadStore.mkdirs();
      }
      
      if(!downloadStore.exists() || !downloadStore.isDirectory()) {
        return null;
      }
      
      return stripTrailingSlash(downloadStoreName);
    }
    
    public static void downloadFile(final String urlString, 
        final String downloadStoreName, final String extension, 
        final int ith, final int total) {
      
      final String countStatus = ith + "/" + total; 
      
      if(urlString == null || !urlString.matches(extension)) {
        System.out.println(countStatus + "!!! Skipping dowload: " + urlString);
        return;
      }
      
      String[] split = urlString.split("/");
      String fileName = split[split.length - 1];
      try {
        URL url = new URL(urlString);
        InputStream in = url.openStream();
        Files.copy(in, Paths.get(downloadStoreName + "/" + fileName), 
            StandardCopyOption.REPLACE_EXISTING);
        in.close();
        System.out.println(countStatus + "... Downloaded: " + urlString);
      } catch (IOException e) {
        System.out.println(countStatus + "!!! Error while downloading: " + urlString);
        e.printStackTrace();
      }
      
      
    }
    
    public static String stripTrailingSlash(final String in) {
      if(in.endsWith("/")) {
        return in.substring(0, in.length() - 1);
      }
      return in;
    }
}
