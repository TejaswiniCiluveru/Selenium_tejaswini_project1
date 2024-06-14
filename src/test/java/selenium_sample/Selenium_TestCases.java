package selenium_sample;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Selenium_TestCases {

    @Test
    public void testOmnifoodWebsite() {
        WebDriver driver = new SafariDriver();

        String url = "https://omnifood-off.netlify.app/";
        driver.get(url);

        threadSleep(1);
        driver.manage().window().minimize();
        threadSleep(3);
        driver.manage().window().maximize();
        threadSleep(3);

        // Scroll and verify key elements
        scrollAndVerifyElements(driver);
//
//        // Test the navigation bar links
       testNavigationBar(driver);

        // Test the subscription form
        testSubscriptionForm(driver);

        // Test the "How it works" section
        testHowItWorksSection(driver);

        // Test the Testimonials section
        testTestimonialsSection(driver);

        threadSleep(1);
        driver.quit(); // Ensure the driver is properly closed
    }

    private void scrollAndVerifyElements(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String textToFind = "Sign up now";
        String text2ToFind = "Get your first meal for free";
        boolean isTextVisible = false;

        while (!isTextVisible) {
            try {
                WebElement element = driver.findElement(By.xpath("//button[@class='btn btn--form' and text()='" + textToFind + "']"));
                WebElement element2 = driver.findElement(By.xpath("//h2[text()='" + text2ToFind + "']"));
                if (isElementInViewport(element, js) && isElementInViewport(element2, js)) {
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

        // Assertions to verify elements
        WebElement signUpButton = driver.findElement(By.xpath("//button[@class='btn btn--form' and text()='" + textToFind + "']"));
        Assert.assertTrue(signUpButton.isDisplayed(), "Sign up now button is not displayed");

        WebElement firstMealFreeHeading = driver.findElement(By.xpath("//h2[text()='" + text2ToFind + "']"));
        Assert.assertTrue(firstMealFreeHeading.isDisplayed(), "Get your first meal for free heading is not displayed");

        WebElement featuresSection = driver.findElement(By.id("pricing"));
        Assert.assertTrue(featuresSection.isDisplayed(), "Pricing section is not displayed");

        WebElement howItWorksSection = driver.findElement(By.id("how"));
        Assert.assertTrue(howItWorksSection.isDisplayed(), "How it works section is not displayed");

        WebElement citiesSection = driver.findElement(By.id("meals"));
        Assert.assertTrue(citiesSection.isDisplayed(), "Meals section is not displayed");

        WebElement testimonialsSection = driver.findElement(By.id("testimonials"));
        Assert.assertTrue(testimonialsSection.isDisplayed(), "Testimonials section is not displayed");
    }

    private void testNavigationBar(WebDriver driver) {
        WebElement featuresLink = driver.findElement(By.linkText("Pricing"));
        featuresLink.click();
        threadSleep(2);
        Assert.assertTrue(driver.findElement(By.id("pricing")).isDisplayed(), "Failed to navigate to pricing section");

        WebElement howItWorksLink = driver.findElement(By.linkText("How it works"));
        howItWorksLink.click();
        threadSleep(2);
        Assert.assertTrue(driver.findElement(By.id("how")).isDisplayed(), "Failed to navigate to How it works section");

        WebElement citiesLink = driver.findElement(By.linkText("Meals"));
        citiesLink.click();
        threadSleep(2);
        Assert.assertTrue(driver.findElement(By.id("meals")).isDisplayed(), "Failed to navigate to Meals section");

        WebElement testimonialsLink = driver.findElement(By.linkText("Testimonials"));
        testimonialsLink.click();
        threadSleep(2);
        Assert.assertTrue(driver.findElement(By.id("testimonials")).isDisplayed(), "Failed to navigate to Testimonials section");
    }

    private void testSubscriptionForm(WebDriver driver) {
        WebElement nameField = driver.findElement(By.id("full-name"));
        nameField.sendKeys("Test User");

        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("test@example.com");

        WebElement selectPlan = driver.findElement(By.id("select-where"));
        selectPlan.click();
        WebElement planOption = driver.findElement(By.xpath("//option[2]")); // Selecting the second option as an example
        planOption.click();

        WebElement submitButton = driver.findElement(By.xpath("//button[text()='Sign up now']"));
        submitButton.click();

        threadSleep(2);
        WebElement successMessage = driver.findElement(By.xpath("//p"));
        Assert.assertTrue(successMessage.isDisplayed(), "Your form submission has been received.");
        
        driver.findElement(By.linkText("← Back to our site")).click();
        threadSleep(2);
    }


    private void testHowItWorksSection(WebDriver driver) {
        WebElement howItWorksLink = driver.findElement(By.linkText("How it works"));
        howItWorksLink.click();
        threadSleep(2);

        WebElement step1 = driver.findElement(By.xpath("//h3[contains(text(),'Tell us what you like (and what not)')]"));
        Assert.assertTrue(step1.isDisplayed(), "Step 1 in 'How it works' section is not displayed");

        WebElement step2 = driver.findElement(By.xpath("//h3[contains(text(),'Approve your weekly meal plan')]"));
        Assert.assertTrue(step2.isDisplayed(), "Step 2 in 'How it works' section is not displayed");

        WebElement step3 = driver.findElement(By.xpath("//h3[contains(text(),'Receive meals at convenient time')]"));
        Assert.assertTrue(step3.isDisplayed(), "Step 3 in 'How it works' section is not displayed");
    }

    private void testMealsSection(WebDriver driver) {
        WebElement citiesLink = driver.findElement(By.linkText("Meals"));
        citiesLink.click();
        threadSleep(2);

        WebElement city1 = driver.findElement(By.xpath("//h3[text()='Lisbon']"));
        Assert.assertTrue(city1.isDisplayed(), "City Lisbon is not displayed");

        WebElement city2 = driver.findElement(By.xpath("//h3[text()='San Francisco']"));
        Assert.assertTrue(city2.isDisplayed(), "City San Francisco is not displayed");

        WebElement city3 = driver.findElement(By.xpath("//h3[text()='Berlin']"));
        Assert.assertTrue(city3.isDisplayed(), "City Berlin is not displayed");

        WebElement city4 = driver.findElement(By.xpath("//h3[text()='London']"));
        Assert.assertTrue(city4.isDisplayed(), "City London is not displayed");
    }

    private void testTestimonialsSection(WebDriver driver) {
        WebElement testimonialsLink = driver.findElement(By.linkText("Testimonials"));
        testimonialsLink.click();
        threadSleep(2);

        WebElement testimonial1 = driver.findElement(By.xpath("//p[contains(text(),'— Dave Bryson')]"));
        Assert.assertTrue(testimonial1.isDisplayed(), "First testimonial is not displayed");

        WebElement testimonial2 = driver.findElement(By.xpath("//p[contains(text(),'— Hannah Smith')]"));
        Assert.assertTrue(testimonial2.isDisplayed(), "Second testimonial is not displayed");

        WebElement testimonial3 = driver.findElement(By.xpath("//p[contains(text(),'— Steve Miller')]"));
        Assert.assertTrue(testimonial3.isDisplayed(), "Third testimonial is not displayed");
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

    public void threadSleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
