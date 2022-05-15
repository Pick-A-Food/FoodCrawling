import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;

public class SeleniumScrapper {
  
  public static final String WEB_DRIVER_ID = "webdriver.chrome.driver"; // 드라이버 ID
  public static final String WEB_DRIVER_PATH = "/Users/suhong/Downloads/chromedriver"; // 드라이버 경로
  
  public static void main(String[] args)throws Exception {
    System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
    
    ChromeOptions options = new ChromeOptions();
    // 브라우저 보이지 않기
    // options.addArguments("headless");
    
    WebDriver driver = new ChromeDriver(options);
    String mainUrl = "https://www.samyangfoods.com/kor/brand/list.do";
    driver.get(mainUrl);
    
    // 브라우저 닫기
    if (driver == null) {
      driver.close();
      driver.quit();
    }
    
    Thread.sleep(5000);
    List<Product> totalObjs = new ArrayList<>();
    
    int pageNum = driver.findElement(By.className("paging")).findElement(By.tagName("p")).findElements(By.tagName("a")).size();
    
    for (int i = 0; i < pageNum; i++){
      List<Product> productObjs = new ArrayList<>();
      
      
      List<WebElement> pages = driver.findElement(By.className("paging")).findElement(By.tagName("p")).findElements(By.tagName("a"));
      pages.get(i).click();
      
      Thread.sleep(1000);
      
      List<WebElement> productList = driver.findElement(By.className("product-list")).findElements(By.tagName("li"));
      
      
      productList.forEach(productLi -> {
        Product product = new Product();
        product.setName(productLi.getText());
        String strContainingNum = productLi.findElement(By.tagName("a")).getAttribute("onclick");
        // strContainingNum = "javascript:fnView('./view.do','545')"
        String temp1 = strContainingNum.substring(0, strContainingNum.lastIndexOf("'"));
        // temp1 = "javascript:fnView('./view.do','545"
        String temp2 = temp1.substring(temp1.lastIndexOf("'") + 1);
        // temp2 = "545"
        product.setNum(temp2);
        productObjs.add(product);
        System.out.println(product);
      });
      
      Thread.sleep(3000);
      
      productObjs.forEach(product -> {
        driver.get(product.getURL());
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {}
        
        List<WebElement> pTags = driver.findElements(By.className("product-view-text")).get(1).findElements(By.tagName("p"));
        StringBuilder builder = new StringBuilder();
        pTags.forEach(p -> {
          builder.append(p.getText());
        });
        product.setIngredients(builder.toString());
        totalObjs.add(product);
      });
      driver.get(mainUrl);
      Thread.sleep(3000);
    }
    
    totalObjs.forEach(product -> {
      System.out.println(product.getIngredients());
    });
    
    System.out.println(totalObjs.size());
    
  }
}

@Data
class Product {
  private static final String pageUrl = "https://www.samyangfoods.com/kor/brand/view.do";
  private String name;
  private String num;
  
  private String ingredients;
  
  public String getURL() {
    return pageUrl + "?seq=" + num;
  }
}