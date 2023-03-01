import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarGurus {

    @Test
    public void locateAnElement() throws InterruptedException {

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.cargurus.com/");

        driver.findElement(By.xpath("/html/body/main/div[2]/div[2]/div[1]/section/div/div/label[2]")).click();
        String actual = new Select(driver.findElement(By.id("carPickerUsed_makerSelect"))).getFirstSelectedOption().getText();
        Assert.assertEquals(actual, "All Makes");

        Select dropdown = new Select(driver.findElement(By.id("carPickerUsed_makerSelect")));
        dropdown.selectByVisibleText("Lamborghini");

        WebElement selectedOptionMakes1 = new Select(driver.findElement(By.xpath("//select[@id='carPickerUsed_modelSelect']"))).getFirstSelectedOption();
        Assert.assertEquals(selectedOptionMakes1.getText(), "All Models", "Default selected option");

        List<String> expectedModels = List.of("All Models", "Aventador", "Huracan", "Urus", "400GT",
                "Centenario", "Countach", "Diablo", "Espada", "Gallardo", "Murcielago");
        List<WebElement> modelsOptions = selectedOptionMakes1.findElements(By.xpath("//select[@id='carPickerUsed_modelSelect']//option"));
        List<String> actualModels = new ArrayList<>();
        for (WebElement option : modelsOptions) {
            actualModels.add(option.getText());
        }
        Assert.assertEquals(actualModels, expectedModels);


        Thread.sleep(4000);
        Select dropdown2 = new Select(driver.findElement(By.id("carPickerUsed_modelSelect")));
        dropdown2.selectByVisibleText("Gallardo");

        Thread.sleep(4000);

        driver.findElement(By.id("dealFinderZipUsedId_dealFinderForm")).sendKeys("22031");
        driver.findElement(By.id("dealFinderForm_0")).click();

        List<WebElement> searchResults = driver.findElements(By.xpath("//a[@data-cg-ft='car-blade-link'][not(contains(@href, 'FEATURED'))]"));
        Assert.assertEquals(15, searchResults.size());

        for (WebElement searchResult : searchResults) {
            String resultTitle = searchResult.getText();
            Assert.assertTrue(resultTitle.contains("Lamborghini Gallardo"));
        }

        Thread.sleep(4000);
        driver.findElement(By.xpath("//a[@data-cg-ft='car-blade-link'][not(contains(@href, 'FEATURED'))]"));


        Thread.sleep(4000);
        Select sortDropdown = new Select(driver.findElement(By.xpath("//select[@id='sort-listing']")));
        sortDropdown.selectByVisibleText("Lowest price first");
        List<WebElement> priceElements = driver.findElements(By.xpath("//span[@class='cg-dealFinder-result-stats']/div[2]/div"));
        List<Integer> prices = new ArrayList<>();
        for (WebElement priceElement : priceElements) {
            prices.add(Integer.parseInt(priceElement.getText().replaceAll("[^\\d.]", "")));
        }
        List<Integer> sortedPrices = new ArrayList<>(prices);
        Collections.sort(sortedPrices);
        Assert.assertEquals(prices, sortedPrices, "lowest price failed");

        Thread.sleep(4000);
        sortDropdown.selectByVisibleText("Highest mileage first");
        List<WebElement> mileageElements = driver.findElements(By.xpath("//span[@class='cg-dealFinder-result-stats']/div[1]/div"));
        List<Integer> mileages = new ArrayList<>();
        for (WebElement mileageElement : mileageElements) {
            mileages.add(Integer.parseInt(mileageElement.getText().replaceAll("[^\\d.]", "")));
        }
        List<Integer> sortedMileages = new ArrayList<>(mileages);
        Collections.sort(sortedMileages, Collections.reverseOrder());
        Assert.assertEquals(mileages, sortedMileages, "highest mileage failed");

        Thread.sleep(4000);
        driver.findElement(By.xpath("//fieldset[5]//ul[1]//li[1]//label[1]//p[1]")).click();

        List<WebElement> resultTitleElements = driver.findElements(By.xpath("//a[@data-cg-ft='car-blade-link'][not(contains(@href, 'FEATURED'))]//h4[contains(@class, 'cg-listingDetail-model')]"));
        for (WebElement resultTitleElement : resultTitleElements) {
            String resultTitle = resultTitleElement.getText().toLowerCase();
            Assert.assertTrue(resultTitle.contains("Lamborghini Gallardo") && resultTitle.contains("Coupe Awd"));
        }


        Thread.sleep(4000);
        List<WebElement> resultListElements = driver.findElements(By.xpath("//img[@class='C6f2e2 bmTmAy']"));
        WebElement lastCheck = resultListElements.get(resultListElements.size() - 1);
        if (!lastCheck.isSelected()) {
            lastCheck.click();
        }

        // how do you select a dynamic search for the last result?? the classnames have multiple things assigned to them - oh ok now it works

        Thread.sleep(4000);
        driver.navigate().back();
        String viewedText = driver.findElement(By.xpath("//div[@class='HWinWE x1gK4I']")).getText();
        Assert.assertTrue(viewedText.contains("Viewed"));


    }
}