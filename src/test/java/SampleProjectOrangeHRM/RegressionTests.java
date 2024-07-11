package SampleProjectOrangeHRM;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RegressionTests {

    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        //system.setProperty("webdriver.chrome.driver","src/main/resources/chromedriver.exe")
        driver = new SafariDriver();
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        
        System.out.println("Browser closed");
    }

    @Test(priority=1,enabled=true)
    public void orangeHRMTests() {
        String url = "https://awesomeqa.com/ui/index.php?route=common/home";
        driver.get(url);

        threadSleep(1);
        

        uiChecks(driver);

        MacbookClick(driver);
        checkViewCart(driver);
        checkout(driver);

        System.out.println("test is finished");
    }

    // Other methods (uiChecks, MacbookClick, etc.) remain unchanged

    private void uiChecks(WebDriver driver) {
        Assert.assertTrue(driver.findElement(By.id("logo")).isDisplayed(), "Failed to navigate to pricing section");

        try {
            // Specific URL of the image to check
            String imageUrl = "https://awesomeqa.com/ui/image/catalog/opencart-logo.png"; // Replace with the actual image URL

            // Find the div containing the image
            WebElement divElement = driver.findElement(By.cssSelector("div"));

            // Check if the div contains an img element with the specific src
            WebElement imgElement = divElement.findElement(By.cssSelector("img[src='" + imageUrl + "']"));

            if (imgElement != null) {
                System.out.println("Image is present in the div tag with the specified URL.");
            } else {
                System.out.println("Image is not present in the div tag with the specified URL.");
            }

        } catch (Exception e) {
            System.out.println("Image is not present in the div tag with the specified URL.");
            e.printStackTrace();
        }
        Assert.assertTrue(driver.findElement(By.id("cart-total")).isDisplayed(), "Failed to navigate to pricing section");
        WebElement noCartItemsElement = driver.findElement(By.id("cart-total"));
        String expectedText = "0 item(s) - $0.00"; // Replace with the actual expected text
        String actualText = noCartItemsElement.getText();
        Assert.assertEquals(actualText, expectedText, "Text does not match expected value.");
        WebElement dropdownButton = driver.findElement(By.xpath("/html//span[@id='cart-total']"));

        dropdownButton.click();

        // Verify the dropdown options are visible
        WebElement dropdownMenu = driver.findElement(By.xpath("//div[@id='cart']/ul[@class='dropdown-menu pull-right']//p[@class='text-center']"));
        String expectedText1 = "Your shopping cart is empty!"; // Replace with the actual expected text
        String actualText1 = dropdownMenu.getText();
        Assert.assertEquals(actualText1, expectedText1, "Dropdown text doesn't match");
        Assert.assertTrue(dropdownMenu.isDisplayed(), "Dropdown menu is not visible after clicking the dropdown button");

        Actions actions = new Actions(driver);

        // Move to an arbitrary position (e.g., x = 100, y = 100) and click
        actions.moveByOffset(100, 100).click().perform();
        Assert.assertFalse(dropdownMenu.isDisplayed(), "Dropdown menu is not visible after clicking the dropdown button");

        WebElement navBar = driver.findElement(By.xpath("//nav[@id='menu']/div[@class='collapse navbar-collapse navbar-ex1-collapse']/ul"));

        List<WebElement> navBarElements = navBar.findElements(By.xpath("./child::li"));
        List<String> names = new ArrayList<>();

        for (WebElement li : navBarElements) {

            WebElement aTag = li.findElement(By.tagName("a"));

            String name = aTag.getText().trim();
            names.add(name);

            Actions hovering = new Actions(driver);

            hovering.moveToElement(aTag).perform();

            threadSleep(1);
        }

        List<String> expectedNames = getExpectedNamesFromFile("/Users/ciluverutejaswini/selenium_project1/selenium_sample/src/test/java/SampleProjectOrangeHRM/navigationBarNames");
        Assert.assertEquals(names, expectedNames, "Names list does not match expected names list");

        int expectedNumberOfElements = 8;
        Assert.assertEquals(navBarElements.size(), expectedNumberOfElements, "The navigation bar does not contain exactly " + expectedNumberOfElements + " elements.");
    }

    private void MacbookClick(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Actions hovering = new Actions(driver);
        boolean isTextVisible = false;

        while (!isTextVisible) {
            try {
                WebElement element = driver.findElement(By.cssSelector("div.carousel.swiper-viewport"));
                if (isElementInViewport(element, js)) {
                    isTextVisible = true;
                    hovering.moveToElement(driver.findElement(By.cssSelector("div.carousel.swiper-viewport div.swiper-button-next"))).perform();
                    driver.findElement(By.cssSelector("div.carousel.swiper-viewport div.swiper-button-next")).click();
                    threadSleep(1);
                    hovering.moveToElement(driver.findElement(By.cssSelector("div.carousel.swiper-viewport div.swiper-button-prev"))).perform();
                    driver.findElement(By.cssSelector("div.carousel.swiper-viewport div.swiper-button-prev")).click();
                    threadSleep(1);
                    break;
                }

            } catch (Exception e) {
                // Optionally log the exception
            }
            if (!isTextVisible) {
                js.executeScript("window.scrollBy(0,200)"); // Scroll down by 200 pixels // Wait a bit before checking again
            }
        }

        WebElement wishlist = driver.findElement(By.xpath("/html//div[@id='content']/div[@class='row']/div[1]/div[@class='product-thumb transition']/div[@class='button-group']/button[2]"));

        // Perform hover action
        hovering.moveToElement(wishlist).perform();
        String tooltipId = wishlist.getAttribute("aria-describedby");
        Assert.assertNotNull(tooltipId, "aria-describedby attribute is not present");

        // Locate the tooltip element using the retrieved id
        WebElement tooltipElement = driver.findElement(By.id(tooltipId));

        // Verify the tooltip is displayed (if applicable)
        Assert.assertTrue(tooltipElement.isDisplayed(), "Tooltip is not displayed");

        // Retrieve and print the tooltip text
        String tooltipText = tooltipElement.getText();
        System.out.println("Tooltip text: " + tooltipText);
        Assert.assertEquals(tooltipText, "Add to Wish List", "Add to wishlist tooltip did not match");

        WebElement compareIcon = driver.findElement(By.xpath("/html//div[@id='content']/div[@class='row']/div[1]/div[@class='product-thumb transition']/div[@class='button-group']/button[3]/i[@class='fa fa-exchange']"));

        // Perform hover action
        hovering.moveToElement(compareIcon).perform();
        threadSleep(1);

        WebElement cart = driver.findElement(By.xpath("/html//div[@id='content']/div[@class='row']/div[1]/div[@class='product-thumb transition']/div[@class='button-group']/button[1]"));
        cart.click();
        threadSleep(1);

        Assert.assertTrue(driver.findElement(By.id("cart-total")).isDisplayed(), "Failed to navigate to cart section");
        WebElement oneCartItemsElement = driver.findElement(By.id("cart-total"));
        String expectedText = " 1 item(s) - $602.00"; // Replace with the actual expected text
        String actualText = oneCartItemsElement.getText();
        Assert.assertEquals(actualText, expectedText, "Text does not match expected value.");
        WebElement dropdownButton = driver.findElement(By.xpath("/html//span[@id='cart-total']"));

        dropdownButton.click();
        threadSleep(2);

        WebElement dropdownMenu = driver.findElement(By.cssSelector("#cart td.text-left>a"));
        String expectedText1 = "MacBook"; // Replace with the actual expected text
        String actualText1 = dropdownMenu.getText();
        Assert.assertEquals(actualText1, expectedText1, "Dropdown text doesn't match");
        Assert.assertTrue(dropdownMenu.isDisplayed(), "Dropdown menu is not visible after clicking the dropdown button");

        WebElement viewcart = driver.findElement(By.cssSelector("a:nth-of-type(1) > strong"));
        String expectedviewText = " View Cart"; // Replace with the actual expected text
        String actualviewText = viewcart.getText();
        Assert.assertEquals(actualviewText, expectedviewText, "view cart Text matched the expected value.");

        WebElement checkout = driver.findElement(By.cssSelector("a:nth-of-type(2) > strong"));
        String expectedcheckoutText = " Checkout"; // Replace with the actual expected text
        String actualcheckoutviewText = checkout.getText();
        Assert.assertEquals(actualcheckoutviewText, expectedcheckoutText, "view cart Text matched the expected value.");

        WebElement subtotal = driver.findElement(By.cssSelector(".table.table-bordered"));
        Assert.assertTrue(subtotal.isDisplayed(), "Step 1 in 'How it works' section is not displayed");
        String expectedsubText = "Sub-Total";// Replace with the actual expected text
        String actualsubText = subtotal.getText();
        Assert.assertTrue(actualsubText.contains(expectedsubText), "subtotal doesn't match the expected value.");

        WebElement suceesAlert = driver.findElement(By.cssSelector("div.alert.alert-success.alert-dismissible"));
        Assert.assertTrue(suceesAlert.isDisplayed(), "Step 1 in 'How it works' section is not displayed");
        String expectedAlertText = " Success: You have added MacBook to your shopping cart! ×"; // Replace with the actual expected text
        String actualalertText = suceesAlert.getText();
        Assert.assertEquals(actualalertText, expectedAlertText, "Success alert didn't match the expected value.");
        

        WebElement removeCartItems = driver.findElement(By.xpath("//button[@title='Remove']"));
        removeCartItems.click();
        threadSleep(2);

        WebElement noCartItemsElement = driver.findElement(By.id("cart-total"));
        String expectedTextRemove = " 0 item(s) - $0.00"; // Replace with the actual expected text
        String actualTextremove = noCartItemsElement.getText();
        Assert.assertEquals(actualTextremove, expectedTextRemove, "removed the cart not match expected value.");
        WebElement dropdownButtonempty = driver.findElement(By.xpath("/html//span[@id='cart-total']"));

        dropdownButtonempty.click();
        threadSleep(2);

        // Verify the dropdown options are visible
        WebElement dropdownMenu2 = driver.findElement(By.xpath("//div[@id='cart']/ul[@class='dropdown-menu pull-right']//p[@class='text-center']"));
        String expectedemptyText = "Your shopping cart is empty!"; // Replace with the actual expected text
        String actualEmptyText = dropdownMenu2.getText();
        Assert.assertEquals(actualEmptyText, expectedemptyText, "Dropdown text doesn't match");
        Assert.assertTrue(dropdownMenu2.isDisplayed(), "Dropdown menu is not visible after clicking the dropdown button");

        Actions actions = new Actions(driver);

        // Move to an arbitrary position (e.g., x = 100, y = 100) and click
        actions.moveByOffset(100, 100).click().perform();
        Assert.assertFalse(dropdownMenu2.isDisplayed(), "Dropdown menu is not visible after clicking the dropdown button");
        WebElement closeAlert = suceesAlert.findElement(By.xpath("//button[@class=\"close\"]"));
        closeAlert.click();
        threadSleep(1);

        try {
            WebElement alertElement = driver.findElement(By.cssSelector("div.alert.alert-success.alert-dismissible"));
            Assert.assertFalse(alertElement.isDisplayed(), "Alert dialogue is still displayed");
        } catch (NoSuchElementException e) {
            // If the element is not found, this is the expected outcome for this assertion
            Assert.assertTrue(true, "Alert dialogue is not closed");
        }
    }

    private static void checkViewCart(WebDriver driver) {
    	
    	JavascriptExecutor js = (JavascriptExecutor) driver;
        Actions hovering = new Actions(driver);
        boolean isTextVisible = false;

        while (!isTextVisible) {
            try {
                WebElement element = driver.findElement(By.cssSelector("div.carousel.swiper-viewport"));
                if (isElementInViewport(element, js)) {
                    isTextVisible = true;
                    threadSleep(1);
                    break;
                }

            } catch (Exception e) {
                // Optionally log the exception
            }
            if (!isTextVisible) {
                js.executeScript("window.scrollBy(0,200)"); // Scroll down by 200 pixels // Wait a bit before checking again
            }
        }
        
        WebElement cart = driver.findElement(By.xpath("/html//div[@id='content']/div[@class='row']/div[1]/div[@class='product-thumb transition']/div[@class='button-group']/button[1]"));
        cart.click();
        threadSleep(5);
        
        WebElement dropdownButtonagain = driver.findElement(By.xpath("/html//span[@id='cart-total']"));

        dropdownButtonagain.click();
        threadSleep(1);
        
    	
    	WebElement viewCart = driver.findElement(By.cssSelector(".fa.fa-shopping-cart"));
        viewCart.click();
        threadSleep(5);
        WebElement shoppingCart = driver.findElement(By.cssSelector("#checkout-cart>ul>li:nth-child(2)>a"));
        Assert.assertEquals(shoppingCart.getText(), "Shopping Cart","Shopping cart is not getting displayed");
    }
    
    private static void checkout(WebDriver driver) {
    	JavascriptExecutor js = (JavascriptExecutor) driver;
    	WebElement element;
        boolean isTextVisible = false;

        while (!isTextVisible) {
            try {
                element = driver.findElement(By.cssSelector("div.buttons a.btn-primary"));
                if (isElementInViewport(element, js)) {
                    isTextVisible = true;
                }
            } catch (Exception e) {
                // Optionally log the exception
            }
            if (!isTextVisible) {
                js.executeScript("window.scrollBy(0,200)"); // Scroll down by 200 pixels
                threadSleep(1); // Wait a bit before checking again
            }
        }
        
        element = driver.findElement(By.cssSelector("div.buttons a.btn-primary"));
        element.click();
        threadSleep(5);
        
        WebElement checkout = driver.findElement(By.cssSelector("#checkout-checkout>ul>li:nth-child(3)>a"));
        Assert.assertEquals(checkout.getText(), "Checkout","Checkout is not getting displayed");
        WebElement emailtext=driver.findElement(By.xpath("/html//input[@id='input-email']"));
        emailtext.sendKeys("TejaswiniCiluveru@outlook.com");
        threadSleep(1);
        
        WebElement passwordtext=driver.findElement(By.xpath("/html//input[@id='input-password']"));
        passwordtext.sendKeys("Teju@#1108");
        
        WebElement login=driver.findElement(By.xpath("//input[@id='button-login']"));
        login.click();
        threadSleep(1);
        //
        WebElement errorAlert = driver.findElement(By.cssSelector("div.alert.alert-danger.alert-dismissible"));
        Assert.assertTrue(errorAlert.isDisplayed(), "error is displayed");
        String expectederrorAlertText = " Warning: No match for E-Mail Address and/or Password.×";
        String actualerroralertText = errorAlert.getText();
        Assert.assertEquals(actualerroralertText, expectederrorAlertText, "error alert didn't match the expected value.");
        
    }
    
    private static boolean isElementInViewport(WebElement element, JavascriptExecutor js) {
        return (Boolean) js.executeScript(
            "var rect = arguments[0].getBoundingClientRect();" +
            "return (" +
            "rect.top >= 0 && rect.left >= 0 && " +
            "rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) && " +
            "rect.right <= (window.innerWidth || document.documentElement.clientWidth)" +
            ");", element);
    }

    private List<String> getExpectedNamesFromFile(String filePath) {
        List<String> expectedNames = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                expectedNames.add(line.trim()); // Trim each line to remove leading/trailing whitespace
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception appropriately
        }
        return expectedNames;
    }

    public static void threadSleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
