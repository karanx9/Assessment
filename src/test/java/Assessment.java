
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;


import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;


public class Assessment {
    public WebDriver driver;
    public WebDriverWait wait;

    private static final By opinion = By.xpath("(//a[@href=\"https://elpais.com/opinion/\"])[1]");
    private static final By first_article = By.cssSelector("a[href=\"https://elpais.com/opinion/2025-07-24/podemos-y-el-estigma-de-los-catalanes-racistas.html\"]");
    private static final By extarct_Title = By.cssSelector("h1[class=\"a_t\"]");
   // private static final By extarct_content = By.cssSelector("h2[class=\"a_st\"]");
    private static final By second_article = By.cssSelector("a[href=\"https://elpais.com/opinion/2025-07-24/campo-de-distorsion-de-la-realidad.html\"]");
    private static final By paragraph_content = By.tagName("p");
    private static final By third_article = By.cssSelector("a[href=\"https://elpais.com/opinion/2025-07-24/el-espacio-esta-muy-vacio-y-encima-faltan-autobuses.html\"]");


    private WebElement waitAndGetElement(By locator, WebDriverWait wait) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    @BeforeTest
    public void setup() {
        // Set the path to the ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\dell\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");
//        options.addArguments("--lang=es");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, (20));
    }

    @Test(priority = 0)
    public void Aricle_First() {

        driver.get("https://elpais.com/");
        waitAndGetElement(opinion,wait).click();
        waitAndGetElement(first_article,wait).click();
        String extracted_header = waitAndGetElement(extarct_Title,wait).getText();


        System.out.println("Article 1  : -");

        System.out.println("Header1" + "  " +   extracted_header);


        String extracted_contenet = waitAndGetElement(paragraph_content,wait).getText();

        System.out.println("Paragraph content " +"  " + extracted_contenet);


        // Example: Download the specified image
        String imageUrl = "https://imagenes.elpais.com/resizer/v2/LFTYXC5XMVJV7M6SAQIJQLTNTI.jpg?auth=4062b257315d50ea108e996da454704f9e0a65150b0bb502446863f022e53f85&width=414";
        String imagePath = "C:\\Users\\DELL\\Pictures\\Saved Pictures\\article_image.jpg";

        try {
            Files.copy(new URL(imageUrl).openStream(), Paths.get(imagePath));
            System.out.println("Image saved to: " + imagePath);
        } catch (Exception e) {
            System.err.println("Error downloading image: " + e.getMessage());
        }

    }



    @Test(priority = 1)
    public void Aricle_second() {
        driver.navigate().back();

        waitAndGetElement(second_article, wait).click();
        String extracted_header = wait.until(visibilityOfElementLocated(extarct_Title)).getText().trim();

        System.out.println("Article 2:");
        System.out.println("Header2: " + extracted_header);

        String extracted_content = wait.until(visibilityOfElementLocated(paragraph_content)).getText().trim();
        System.out.println("Paragraph content " + extracted_content);

        // Save the image
        String imageUrl = "https://imagenes.elpais.com/resizer/v2/NYUEPFGWAJEHBB4MOVH7RSCFTQ.jpg?auth=e8a9a4e639b6aa47568bae2a71fa29c0573c25c462f47e6191218a835a357e99&width=414";
        String imagePath = "C:\\Users\\DELL\\Pictures\\Saved Pictures\\article_second_image.jpg";

        try {
            Files.copy(new URL(imageUrl).openStream(), Paths.get(imagePath));
            System.out.println("Image saved to: " + imagePath);
        } catch (Exception e) {
            System.err.println("Error downloading image: " + e.getMessage());
        }
    }



    @Test(priority = 2)
    public void third_article() {
        driver.navigate().back();

        waitAndGetElement(third_article, wait).click();
        String extracted_header = wait.until(ExpectedConditions.visibilityOfElementLocated(extarct_Title)).getText().trim();

        System.out.println("Article 3:");
        System.out.println("Header3: " + extracted_header);

        String extracted_paragraph = wait.until(ExpectedConditions.visibilityOfElementLocated(paragraph_content)).getText().trim();
        System.out.println("Paragraph Content: " + extracted_paragraph);

        // Save the image
        String imageUrl = "https://imagenes.elpais.com/resizer/v2/IQGRZBIDOBCXPKMPQETU65HBKY.jpg?auth=bf50ecea10d8ce6606a4da720e5a1888a943b4c62287d106cea5eec2541f177a&width=414";
        String imagePath = "C:\\Users\\DELL\\Pictures\\Saved Pictures\\article_third_image.jpg";

        try {
            Files.copy(new URL(imageUrl).openStream(), Paths.get(imagePath));
            System.out.println("Image saved to: " + imagePath);
        } catch (Exception e) {
            System.err.println("Error downloading image: " + e.getMessage());
        }
    }


}