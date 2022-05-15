import lombok.Data;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class HaitaiScrapper {
  
  public static final String WEB_DRIVER_ID = "webdriver.chrome.driver"; // 드라이버 ID
  
  public static final String WEB_DRIVER_PATH = "/Users/eunjin/Downloads/chromedriver"; // 드라이버 경로
  
  public HaitaiService service;
  
  public static void main(String[] args)throws Exception {
    System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
    
    ChromeOptions options = new ChromeOptions();
    // 브라우저 보이지 않기
    // options.addArguments("headless");
    
    WebDriver driver = new ChromeDriver(options);
    String mainUrl = "https://www.haitaimall.co.kr/product/list.html?cate_no=26";
  
   
    driver.get(mainUrl);
    
    // 브라우저 닫기
    if (driver == null) {
      driver.close();
      driver.quit();
    }
    
    Thread.sleep(5000);
    List<ProductHaitai> totalObjs = new ArrayList<>();
  
    int pageNum = driver.findElement(By.className("ec-base-paginate")).findElement(By.tagName("ol")).findElements(By.tagName("a")).size();
    
    for (int i = 0; i < pageNum; i++){
      List<ProductHaitai> productObjs = new ArrayList<>();
  
  
      List<WebElement> pages = driver.findElement(By.className("ec-base-paginate")).findElement(By.tagName("ol"))
              .findElements(By.tagName("a"));
      pages.get(i).click();
      
      Thread.sleep(1000);
      
      
      
      List<WebElement> productList = driver.findElement(By.className("prdList")).findElements(By.className("name"));
      
      productList.forEach(productLi -> {
     //   System.out.println(productLi.getText());
        ProductHaitai product = new ProductHaitai();
        product.setName(productLi.getText());
        product.setUrl(productLi.findElement(By.tagName("a")).getAttribute("href"));
        productObjs.add(product);
        //System.out.println(product);
      });
//
      Thread.sleep(3000);
//
      productObjs.forEach(product -> {
        driver.get(product.getUrl());
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {}
        
        try {
            WebElement image = driver.findElement(By.id("prdDetailContentLazy")).findElement(By.tagName("img"));
            product.setIngredients(image.getAttribute("src"));
//          System.out.println("33333" + "https://www.haitaimall.co.kr"+ image.getAttribute("src"));
          //*[@id="prdDetailContentLazy"]/div/img
//          System.out.println("driverUrl : " + driver.getCurrentUrl());
//          System.out.println(product.getUrl());
            totalObjs.add(product);
        }catch (Exception e){
          e.printStackTrace();
        }
      });
      driver.get(mainUrl);
      Thread.sleep(3000);
    }
//
    totalObjs.forEach(product -> {
      System.out.println(product);
      System.out.println(product.getIngredients());
    });
//
//    System.out.println(totalObjs.size());
  
  }
}

@Data
class ProductHaitai {
  private static final String pageUrl = "https://www.haitaimall.co.kr/product/list.html?cate_no=26";
  private String name;
  private String num;
  
  private String ingredients;
  private String url;
  
}
