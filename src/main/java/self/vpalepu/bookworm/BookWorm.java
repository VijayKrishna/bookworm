package self.vpalepu.bookworm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

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
      
      try {
        final String targetUrl = stripTrailingSlash(getArgument("worm.page"));
        Document doc = Jsoup.connect(targetUrl).get();
        Elements links = doc.getElementsByTag("a");
        
        final String extensionRegEx = getArgument("worm.extn");
        ArrayList<String> fullHrefs = new ArrayList<>();
        
        for(Element link : links) {
          final String href = link.attr("href");
          if(href == null || href.isEmpty()) {
            continue;
          }
          
          final String fullHref = expandHref(targetUrl, href);
          if(shouldDownload(fullHref, extensionRegEx)) {
            fullHrefs.add(fullHref);
          }
        }
        
        int count = 1;
        final int linkCount = fullHrefs.size();
        final String downloadStore = createDownloadStore(getArgument("worm.downloads"));
        for(String fullHref : fullHrefs) {
          downloadFile(fullHref, downloadStore, count, linkCount);
          count += 1;
        }
        
        
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    /**
     * @param targetUrl
     * @param href
     * @return
     */
    public static String expandHref(final String targetUrl, final String href) {
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
      return fullHref;
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
    
    public static boolean shouldDownload(final String urlString, 
        final String extension) {
      if(urlString == null || !urlString.matches(extension)) {
        return false;
      }
      
      return true;
    }
    
    public static void downloadFile(final String urlString, 
        final String downloadStoreName, final int ith, final int total) {
      
      final String countStatus = ith + "/" + total; 
      
      
      
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
