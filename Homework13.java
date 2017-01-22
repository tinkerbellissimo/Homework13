import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Random;

import static org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElement;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;


/**
 * Created by tinkerbellissimo on 1/20/17.
 */
public class Homework13 {
    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void start() {
        System.setProperty("webdriver.chrome.driver", "/Users/tinkerbellissimo/Downloads/chromedriver");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    public void test() {

        driver.get("http://localhost/litecart/en/");

        int itemCount;
        for (int i = 0; i < 3; i++) {
            clickRandomItem();
            itemCount = getItemCount();
            itemCount++;
            addItem();
            wait.until(textToBePresentInElement(driver.findElement(By.className("quantity")),
                    String.valueOf(itemCount)));
            driver.navigate().back();
        }

        driver.findElement(By.partialLinkText("Checkout")).click();
        for (int i=0; i<3; i++) {
            WebElement table = driver.findElement(By.id("order_confirmation-wrapper"));
            driver.findElement(By.name("remove_cart_item")).click();
            wait.until(stalenessOf(table));
            if (!isElementPresent(driver, By.id("order_confirmation-wrapper"))) {
                break;
            }
            wait.until(visibilityOf(driver.findElement(By.id("order_confirmation-wrapper"))));
        }
    }

    public void clickRandomItem() {
        List<WebElement> items = driver.findElements(By.className("product"));
        Random r = new Random();
        int randomItem = r.nextInt(items.size());
        items.get(randomItem).click();
    }

    public void addItem() {
        By sizeSelectLocator=By.name("options[Size]");
        if(isElementPresent(driver, sizeSelectLocator)) {
            Select dropdown = new Select(driver.findElement(sizeSelectLocator));
            dropdown.selectByIndex(1);
        }
        driver.findElement(By.name("add_cart_product")).click();

    }

    int getItemCount() {
        String itemCount = driver.findElement(By.className("quantity")).getText();
        int count = Integer.valueOf(itemCount);
        return count;
    }

    boolean isElementPresent(WebDriver driver, By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}

