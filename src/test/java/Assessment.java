
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class Assessment {
    public WebDriver driver;
    public WebDriverWait wait;

    private static final By opinion = By.xpath("(//a[@href=\"https://elpais.com/opinion/\"])[1]");
    private static final By first_article = By.cssSelector("a[href=\"https://elpais.com/opinion/2025-07-24/podemos-y-el-estigma-de-los-catalanes-racistas.html\"]");
    private static final By extarct_Title = By.cssSelector("h1[class=\"a_t\"]");
    private static final By second_article = By.cssSelector("a[href=\"https://elpais.com/opinion/2025-07-24/campo-de-distorsion-de-la-realidad.html\"]");
    private static final By paragraph_content = By.tagName("p");
    private static final By third_article = By.cssSelector("a[href=\"https://elpais.com/opinion/2025-07-24/el-espacio-esta-muy-vacio-y-encima-faltan-autobuses.html\"]");
    private static final By fourth_article = By.cssSelector("a[href=\"https://elpais.com/opinion/2025-07-24/lecciones-de-verano.html\"]");
    private static final By fifth_aricle = By.xpath("(//a[@href=\"https://elpais.com/opinion/2025-07-24/por-la-gloria-de-moscoso.html\"])[2]");
    private static final By Accep_cookie = By.cssSelector("button[id=\"didomi-notice-agree-button\"]");

    // Static list to accumulate translated headers across all tests
    private static List<String> allTranslatedHeaders = new ArrayList<>();

    private WebElement waitAndGetElement(By locator, WebDriverWait wait) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    @BeforeTest
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\dell\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 20);
    }

    private String translateText(String text) {
        String apiUrl = "https://api.lecto.ai/v1/translate/text";
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("X-API-Key", "NTVF13Z-HJ444A8-GSZWQ77-1K1Z8QE");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            String jsonInput = "{\"texts\":[\"" + text.replace("\"", "\\\"") + "\"],\"to\":[\"en\"],\"from\":\"es\"}";
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }
            br.close();
            connection.disconnect();

            return response.toString().replaceAll(".*\"translated\":\\[\"([^\"]+)\"\\].*", "$1");
        } catch (Exception e) {
            System.err.println("Translation error: " + e.getMessage());
            return "Translation failed";
        }
    }

    @Test(priority = 0)
    public void Aricle_First() {
        driver.get("https://elpais.com/");

        try {
            waitAndGetElement(Accep_cookie, wait).click();
            System.out.println("Cookies accepted.");
        } catch (Exception e) {
            System.out.println("No cookies pop-up found or failed to accept: " + e.getMessage());
        }

        waitAndGetElement(opinion, wait).click();

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", waitAndGetElement(first_article, wait));
            js.executeScript("window.scrollBy(0, -50);");
        } catch (Exception e) {
            System.out.println("not scrolled");
            throw new RuntimeException(e);
        }

        waitAndGetElement(first_article, wait).click();

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", waitAndGetElement(extarct_Title, wait));
            js.executeScript("window.scrollBy(0, -50);");
        } catch (Exception e) {
            System.out.println("not scrolled");
            throw new RuntimeException(e);
        }

        String extracted_header = waitAndGetElement(extarct_Title, wait).getText();
        System.out.println("Article 1  : -");
        System.out.println("Header 1" + "  " + extracted_header);

        String translated_header = translateText(extracted_header);
        System.out.println("Translated Header1: " + translated_header);
        allTranslatedHeaders.add(translated_header);

        String extracted_content = waitAndGetElement(paragraph_content, wait).getText();
        System.out.println("Paragraph content " + "  " + extracted_content);

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

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", waitAndGetElement(extarct_Title, wait));
            js.executeScript("window.scrollBy(0, -50);");
        } catch (Exception e) {
            System.out.println("not scrolled");
            throw new RuntimeException(e);
        }

        String extracted_header = wait.until(visibilityOfElementLocated(extarct_Title)).getText().trim();
        System.out.println("Article 2:");
        System.out.println("Header 2: " + extracted_header);

        String translated_header = translateText(extracted_header);
        System.out.println("Translated Header2: " + translated_header);
        allTranslatedHeaders.add(translated_header);

        String extracted_content = wait.until(visibilityOfElementLocated(paragraph_content)).getText().trim();
        System.out.println("Paragraph content " + extracted_content);

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

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", waitAndGetElement(extarct_Title, wait));
            js.executeScript("window.scrollBy(0, -50);");
        } catch (Exception e) {
            System.out.println("not scrolled");
            throw new RuntimeException(e);
        }

        String extracted_header = wait.until(ExpectedConditions.visibilityOfElementLocated(extarct_Title)).getText().trim();
        System.out.println("Article 3:");
        System.out.println("Header 3: " + extracted_header);

        String translated_header = translateText(extracted_header);
        System.out.println("Translated Header 3: " + translated_header);
        allTranslatedHeaders.add(translated_header);

        String extracted_paragraph = wait.until(ExpectedConditions.visibilityOfElementLocated(paragraph_content)).getText().trim();
        System.out.println("Paragraph Content: " + extracted_paragraph);

        String imageUrl = "https://imagenes.elpais.com/resizer/v2/IQGRZBIDOBCXPKMPQETU65HBKY.jpg?auth=bf50ecea10d8ce6606a4da720e5a1888a943b4c62287d106cea5eec2541f177a&width=414";
        String imagePath = "C:\\Users\\DELL\\Pictures\\Saved Pictures\\article_third_image.jpg";

        try {
            Files.copy(new URL(imageUrl).openStream(), Paths.get(imagePath));
            System.out.println("Image saved to: " + imagePath);
        } catch (Exception e) {
            System.err.println("Error downloading image: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void Aricle_fourth() {
        driver.navigate().back();
        waitAndGetElement(fourth_article, wait).click();

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", waitAndGetElement(extarct_Title, wait));
            js.executeScript("window.scrollBy(0, -50);");
        } catch (Exception e) {
            System.out.println("not scrolled");
            throw new RuntimeException(e);
        }

        String extracted_header = wait.until(ExpectedConditions.visibilityOfElementLocated(extarct_Title)).getText().trim();
        System.out.println("Article 4:");
        System.out.println("Header 4: " + extracted_header);

        String translated_header = translateText(extracted_header);
        System.out.println("Translated Header 4: " + translated_header);
        allTranslatedHeaders.add(translated_header);

        String extracted_paragraph = wait.until(ExpectedConditions.visibilityOfElementLocated(paragraph_content)).getText().trim();
        System.out.println("Paragraph Content: " + extracted_paragraph);

        String imageUrl = "https://imagenes.elpais.com/resizer/v2/JH4DNMXA5NPYLM4GDSTMBSRUZI.jpg?auth=9b0a0a94f954eca828c0765572484051929bc717d7c51aa5a69004de240566ba&width=414";
        String imagePath = "C:\\Users\\DELL\\Pictures\\Saved Pictures\\article_fourth_image.jpg";

        try {
            Files.copy(new URL(imageUrl).openStream(), Paths.get(imagePath));
            System.out.println("Image saved to: " + imagePath);
        } catch (Exception e) {
            System.err.println("Error downloading image: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void Aricle_five() {
        driver.navigate().back();
        waitAndGetElement(fifth_aricle, wait).click();

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", waitAndGetElement(extarct_Title, wait));
            js.executeScript("window.scrollBy(0, -50);");
        } catch (Exception e) {
            System.out.println("not scrolled");
            throw new RuntimeException(e);
        }

        String extracted_header = wait.until(ExpectedConditions.visibilityOfElementLocated(extarct_Title)).getText().trim();
        System.out.println("Article 5:");
        System.out.println("Header 5: " + extracted_header);

        String translated_header = translateText(extracted_header);
        System.out.println("Translated Header 5: " + translated_header);
        allTranslatedHeaders.add(translated_header);

        String extracted_paragraph = wait.until(ExpectedConditions.visibilityOfElementLocated(paragraph_content)).getText().trim();
        System.out.println("Paragraph Content: " + extracted_paragraph);

        String imageUrl = "https://imagenes.elpais.com/resizer/v2/VROFFG55DZCWFISXQUZXT3Y2O4.jpg?auth=1427ba5dd11cb6039a0176b2473fd14198ce98cc173b4bcbf18fbe8f3212cca3&width=414";
        String imagePath = "C:\\Users\\DELL\\Pictures\\Saved Pictures\\article_five_image.jpg";

        try {
            Files.copy(new URL(imageUrl).openStream(), Paths.get(imagePath));
            System.out.println("Image saved to: " + imagePath);
        } catch (Exception e) {
            System.err.println("Error downloading image: " + e.getMessage());
        }
    }

    @AfterTest
    public void analyzeAllHeaders() {
        analyzeAndPrintRepeatedWords(allTranslatedHeaders);
        driver.quit();
    }

    private void analyzeAndPrintRepeatedWords(List<String> translatedHeaders) {
        // Combine all headers into a single string
        String combinedHeaders = String.join(" ", translatedHeaders);

        // Split into words (ignoring case and non-alphanumeric characters)
        String[] words = combinedHeaders.toLowerCase().replaceAll("[^a-zA-Z\\s]", "").split("\\s+");

        // Count occurrences using a HashMap
        Map<String, Integer> wordCount = new HashMap<>();
        for (String word : words) {
            if (!word.isEmpty()) {
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            }
        }

        // Find and print words repeated more than twice across all headers
        boolean found = false;
        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            if (entry.getValue() > 2) {
                if (!found) {
                    System.out.println("Words repeated more than twice across all headers:");
                    found = true;
                }
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }

        if (!found) {
            System.out.println("No words repeated more than twice across all headers.");
        }
    }
}
