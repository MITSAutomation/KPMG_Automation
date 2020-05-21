package com.kpmg.test;

import static com.kpmg.test.DriverScript.APP_LOGS;
import static com.kpmg.test.DriverScript.CONFIG;
import static com.kpmg.test.DriverScript.OR;
import static com.kpmg.test.DriverScript.currentBrowser;
import static com.kpmg.test.DriverScript.currentExcellColumnName;
import static com.kpmg.test.DriverScript.currentTestCaseName;
import static com.kpmg.test.DriverScript.currentTestDataSetID;
import static com.kpmg.test.DriverScript.currentTestSuiteXLS;

import java.awt.AWTException;
import java.awt.Desktop.Action;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.Screen;

public class Keywords {

	public WebDriver driver;
	public WebDriverWait wait;
	// FileInputStream fs = new
	// FileInputStream(System.getProperty("user.dir")+"//src//com//kpmg//config//config.properties");

	// Open browser
	public String openBrowser(String object, String data) throws IOException, InterruptedException {
		APP_LOGS.debug("Browser Opening Method Start");
		String os = System.getProperty("os.name");
		Runtime.getRuntime().exec("taskkill /F /IM safari.exe");
//Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
		Runtime.getRuntime().exec("taskkill /F /IM opera.exe");
		if (CONFIG.getProperty("browserType").contains(",")) {
			data = currentBrowser;
		}
		if (os.contains("Windows")) {
			if (data.equals("Mozilla")) {

				APP_LOGS.debug("Mozilla Browser Opening Method Start");
				// Runtime.getRuntime().exec("taskkill /F /IM firefox.exe");
				Thread.sleep(10000);
				driver = new FirefoxDriver();
				driver.manage().window().maximize();
			} else if (data.equals("IE")) {
				APP_LOGS.debug("IE Browser Opening Method START");
				Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
				System.setProperty("webdriver.ie.driver", CONFIG.getProperty("IEDriver"));
				driver = new InternetExplorerDriver();
				driver.manage().window().maximize();
			} else if (data.equals("Chrome")) {
				APP_LOGS.debug("Chrome Browser Opening Method Start");
				DesiredCapabilities capability = DesiredCapabilities.chrome();
				// To Accept SSL certificate
				capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

				System.setProperty("webdriver.chrome.driver", CONFIG.getProperty("ChromeDriver"));
				Thread.sleep(10000);

				driver = new ChromeDriver();
				driver.manage().window().maximize();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				APP_LOGS.debug("Browser Opening Method End");
			}

			long implicitWaitTime = Long.parseLong(CONFIG.getProperty("implicitwait"));
			driver.manage().timeouts().implicitlyWait(implicitWaitTime, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			return Constants.KEYWORD_PASS;

		}
		return os;
	}

	// Navigate
	public String navigate(String object, String data) {
		APP_LOGS.info("Navigating to URL");
		try {
			driver.navigate().to(data);
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " -- Not able to navigate";
		}
		return Constants.KEYWORD_PASS;
	}

	// For selecting date
	public String selectDate(String object, String data) {
		APP_LOGS.info("selecting current date");
		try {
			driver.findElement(By.id(object)).click();
			System.out.println("mail date clicked");
			Thread.sleep(10000);
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_ENTER);
			System.out.println("pressed enter key");
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " -- Not able to navigate";
		}
		return Constants.KEYWORD_PASS;
	}

	public String clickLink(String object, String data) {
		APP_LOGS.info("Clicking on link ");
		try {
			driver.findElement(By.xpath(object)).click();
			System.out.println("Link Clicked");
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " -- Not able to click on link" + e.getMessage();
		}

		return Constants.KEYWORD_PASS;
	}

	public String clickLink_linkText(String object, String data) {
		APP_LOGS.info("Clicking on link ");
		driver.findElement(By.linkText(object)).click();

		return Constants.KEYWORD_PASS;
	}

	/*public String mouseHover1(String object, String data) {
		APP_LOGS.debug("MouseHover on any element");
		try {
			WebElement ProductImage = driver
					.findElement(By.cssSelector("section.image_block[data-prod='" + ProdutType + "']"));
			String ProductImagename = ProductImage.getAttribute("name");
			String[] productid = ProductImagename.split("-");
			String productidvalue = productid[1];
			WebElement GreenTick = driver
					.findElement(By.xpath("//*[@id='personalize-" + productidvalue + "-UNDEFINED-false']"));
			Actions builder = new Actions(driver);
			org.openqa.selenium.interactions.Action mouseOverHome = builder.moveToElement(ProductImage).build();
			mouseOverHome.perform();
			Thread.sleep(2000);
			GreenTick.click();
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Not able to click";
		}
		return Constants.KEYWORD_PASS;
	}*/

	public String ElementDisplayed(String object, String data) {
		try {
			boolean Cityvalue = driver.findElement(By.xpath(object)).isDisplayed();
			if (Cityvalue == true) {
				// return Constants.KEYWORD_PASS;
			}
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Elemetn not displayed " + e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}

	public String ElementNotDisplayed(String object, String data) {
		try {
			boolean Cityvalue = driver.findElement(By.xpath(object)).isDisplayed();
			if (Cityvalue == true) {
				return Constants.KEYWORD_FAIL + " Elemetn is displayed ";
				// return Constants.KEYWORD_PASS;
			} else {
				return Constants.KEYWORD_PASS;
			}
		} catch (Exception e) {
			return Constants.KEYWORD_PASS;
		}
	}

	public String verifyLinkText(String object, String data) {
		APP_LOGS.info("Verifying link Text");
		try {
			String actual = driver.findElement(By.xpath(object)).getText();
			String expected = data;

			if (actual.equals(expected))
				return Constants.KEYWORD_PASS;
			else
				return Constants.KEYWORD_FAIL + " -- Link text not verified";

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " -- Link text not verified" + e.getMessage();

		}
	}

	public String verifyTextBoxText(String object, String data) {
		APP_LOGS.info("Verifying link Text");
		try {
			String actual = driver.findElement(By.xpath(object)).getAttribute("value");
			String expected = data;

			if (actual.equals(expected))
				return Constants.KEYWORD_PASS;
			else
				return Constants.KEYWORD_FAIL + " --  text not verified";

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " --  text not verified" + e.getMessage();

		}

	}

	public String clickButton(String object, String data) {
		APP_LOGS.info("Clicking on Button");
		try {
			driver.findElement(By.xpath(object)).click();
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " -- Not able to click on Button" + e.getMessage();
		}

		return Constants.KEYWORD_PASS;
	}

	public String KeyboardUp(String object, String data) {
		APP_LOGS.info("Click on keyboard up arrow");
		try {
			// driver.findElement(By.xpath(object)).click();
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_UP);
			Thread.sleep(1000);
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " -- Click on Up Arrow" + e.getMessage();
		}

		return Constants.KEYWORD_PASS;
	}

	// clicking down arrow
	public String KeyboardDown(String object, String data) {
		APP_LOGS.info("Click on keyboard Down arrow");
		try {
			// driver.findElement(By.xpath(object)).click();
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_DOWN);
			Thread.sleep(1000);
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " -- Click on Down Arrow" + e.getMessage();
		}

		return Constants.KEYWORD_PASS;
	}

	public String DropDownSelect(String object, String data) {
		APP_LOGS.info("Click on keyboard up arrow");
		try {

			Robot r = new Robot();
			for (int i = 0; i <= 100; i++) {
				driver.findElement(By.xpath(object)).click();
				String SelectedValue = driver.findElement(By.xpath(object)).getText();
				if (SelectedValue.equalsIgnoreCase(data)) {
					r.keyPress(KeyEvent.VK_ENTER);
					break;
				}
				r.keyPress(KeyEvent.VK_DOWN);
				Thread.sleep(1000);
				r.keyPress(KeyEvent.VK_ENTER);
			}
			Thread.sleep(1000);
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " -- Unable to select value in drop down" + e.getMessage();
		}

		return Constants.KEYWORD_PASS;
	}

	public String verifyButtonText(String object, String data) {
		APP_LOGS.info("Verifying the button text");
		try {
			String actual = driver.findElement(By.xpath(object)).getText();
			String expected = data;

			if (actual.equals(expected))
				return Constants.KEYWORD_PASS;
			else
				return Constants.KEYWORD_FAIL + " -- Button text not verified " + actual + " -- " + expected;
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Object not found " + e.getMessage();
		}

	}

	public String select(String object, String data) {
		APP_LOGS.info("Selecting the value");
		try {
			new Select(driver.findElement(By.xpath(object))).selectByVisibleText(data);

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " - Could not select from list. " + e.getMessage();
		}

		return Constants.KEYWORD_PASS;
	}

	public String selectList(String object, String data) {
		APP_LOGS.info("Selecting from list");
		try {
			if (!data.equals(Constants.RANDOM_VALUE)) {
				driver.findElement(By.xpath(object)).sendKeys(data);
			} else {
				// logic to find a random value in list
				WebElement droplist = driver.findElement(By.xpath(object));
				List<WebElement> droplist_cotents = droplist.findElements(By.tagName("option"));
				Random num = new Random();
				int index = num.nextInt(droplist_cotents.size());
				String selectedVal = droplist_cotents.get(index).getText();

				driver.findElement(By.xpath(object)).sendKeys(selectedVal);
			}
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " - Could not select from list. " + e.getMessage();

		}

		return Constants.KEYWORD_PASS;
	}

	public String verifyAllListElements(String object, String data) {
		APP_LOGS.info("Verifying the selection of the list");
		try {
			WebElement droplist = driver.findElement(By.xpath(object));
			List<WebElement> droplist_cotents = droplist.findElements(By.tagName("option"));

			// extract the expected values from OR. properties
			String temp = data;
			String allElements[] = temp.split(",");
			// check if size of array == size if list
			if (allElements.length != droplist_cotents.size())
				return Constants.KEYWORD_FAIL + "- size of lists do not match";

			for (int i = 0; i < droplist_cotents.size(); i++) {
				if (!allElements[i].equals(droplist_cotents.get(i).getText())) {
					return Constants.KEYWORD_FAIL + "- Element not found - " + allElements[i];
				}
			}
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " - Could not select from list. " + e.getMessage();

		}

		return Constants.KEYWORD_PASS;
	}

	public String verifyListSelection(String object, String data) {
		APP_LOGS.info("Verifying all the list elements");
		try {
			String expectedVal = data;
			// System.out.println(driver.findElement(By.xpath(object)).getText());
			WebElement droplist = driver.findElement(By.xpath(object));
			List<WebElement> droplist_cotents = droplist.findElements(By.tagName("option"));
			String actualVal = null;
			for (int i = 0; i < droplist_cotents.size(); i++) {
				String selected_status = droplist_cotents.get(i).getAttribute("selected");
				if (selected_status != null)
					actualVal = droplist_cotents.get(i).getText();
			}

			if (!actualVal.equals(expectedVal))
				return Constants.KEYWORD_FAIL + "Value not in list - " + expectedVal;

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " - Could not find list. " + e.getMessage();

		}
		return Constants.KEYWORD_PASS;

	}

	public String selectRadio(String object, String data) {
		APP_LOGS.info("Selecting a radio button");
		try {
			String temp[] = object.split(Constants.DATA_SPLIT);
			driver.findElement(By.xpath((temp[0]) + data + (temp[1]))).click();
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + "- Not able to find radio button";

		}

		return Constants.KEYWORD_PASS;

	}

	public String verifyRadioSelected(String object, String data) {
		APP_LOGS.info("Verify Radio Selected");
		try {
			String temp[] = object.split(Constants.DATA_SPLIT);
			String checked = driver.findElement(By.xpath((temp[0]) + data + (temp[1]))).getAttribute("checked");
			if (checked == null)
				return Constants.KEYWORD_FAIL + "- Radio not selected";

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + "- Not able to find radio button";

		}

		return Constants.KEYWORD_PASS;

	}

	public String checkCheckBox(String object, String data) {
		APP_LOGS.info("Checking checkbox");
		try {
			// true or null
			String checked = driver.findElement(By.xpath(object)).getAttribute("checked");
			if (checked == null)// checkbox is unchecked
				driver.findElement(By.xpath(OR.getProperty(object))).click();
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " - Could not find checkbo";
		}
		return Constants.KEYWORD_PASS;

	}

	public String unCheckCheckBox(String object, String data) {
		APP_LOGS.info("Unchecking checkBox");
		try {
			String checked = driver.findElement(By.xpath(object)).getAttribute("checked");
			if (checked != null)
				driver.findElement(By.xpath(object)).click();
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " - Could not find checkbox";
		}
		return Constants.KEYWORD_PASS;

	}

	public String verifyCheckBoxSelected(String object, String data) {
		APP_LOGS.info("Verifying checkbox selected");
		try {
			String checked = driver.findElement(By.xpath(object)).getAttribute("checked");
			if (checked != null)
				return Constants.KEYWORD_PASS;
			else
				return Constants.KEYWORD_FAIL + " - Not selected";

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " - Could not find checkbox";

		}

	}

	public String verifyText(String object, String data) {
		APP_LOGS.info("Verifying the text");
		try {
			String actual = driver.findElement(By.xpath(object)).getText();
			System.out.println("actual....." + actual);
			String expected = data;
			System.out.println("expected....." + expected);
			if (actual.equals(expected))
				return Constants.KEYWORD_PASS;
			else
				return Constants.KEYWORD_FAIL + " -- text not verified " + actual + " -- " + expected;
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Object not found " + e.getMessage();
		}

	}

	public String verifyTextContains(String object, String data) {
		APP_LOGS.info("Verifying the text");
		try {
			String actual = driver.findElement(By.xpath(object)).getText();
			System.out.println("actual....." + actual);
			String expected = data;
			System.out.println("expected....." + expected);
			if (actual.contains(expected))
				return Constants.KEYWORD_PASS;
			else
				return Constants.KEYWORD_FAIL + " -- text not verified " + actual + " -- " + expected;
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Object not found " + e.getMessage();
		}

	}

	public String writeInInput(String object, String data) {
		APP_LOGS.info("Writing in text box");

		try {
			driver.findElement(By.xpath(object)).clear();
			Thread.sleep(500);
			driver.findElement(By.xpath(object)).sendKeys(data);
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Unable to write " + e.getMessage();

		}
		return Constants.KEYWORD_PASS;

	}

	public String verifyTextinInput(String object, String data) {
		APP_LOGS.info("Verifying the text in input box");
		try {
			String actual = driver.findElement(By.xpath(object)).getAttribute("value");
			String expected = data;

			if (actual.equals(expected)) {
				return Constants.KEYWORD_PASS;
			} else {
				return Constants.KEYWORD_FAIL + " Not matching ";
			}

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Unable to find input box " + e.getMessage();

		}
	}

	public String clickImage() {
		APP_LOGS.info("Clicking the image");

		return Constants.KEYWORD_PASS;
	}

	public String verifyFileName() {
		APP_LOGS.info("Verifying inage filename");

		return Constants.KEYWORD_PASS;
	}

	public String verifyTitle(String object, String data) {
		APP_LOGS.info("Verifying title");
		try {
			String actualTitle = driver.getTitle();
			String expectedTitle = data;
			if (actualTitle.equals(expectedTitle))
				return Constants.KEYWORD_PASS;
			else
				return Constants.KEYWORD_FAIL + " -- Title not verified " + expectedTitle + " -- " + actualTitle;
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Error in retrieving title";
		}
	}

	public String getTextStore(String object, String data) {
		APP_LOGS.info("Get text and store the value");
		try {
			String actualTitle = driver.findElement(By.xpath(object)).getText();
			currentTestSuiteXLS.setCellData(currentTestCaseName, currentExcellColumnName, currentTestDataSetID,
					actualTitle);

			return Constants.KEYWORD_PASS;

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Error in retrieving title";
		}
	}

	public String exist(String object, String data) {
		APP_LOGS.info("Checking existance of element");
		try {
			driver.findElement(By.xpath(object));
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Object doest not exist";
		}

		return Constants.KEYWORD_PASS;
	}

	public String click(String object, String data) {
		APP_LOGS.info("Clicking on any element");
		try {
			driver.findElement(By.xpath(object)).click();
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Not able to click";
		}
		return Constants.KEYWORD_PASS;
	}

	public String clickEnter(String object, String data) {
		APP_LOGS.info("Clicking Enter");
		try {
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_ENTER);
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Not able to click";
		}
		return Constants.KEYWORD_PASS;
	}

	public String uploadList(String object, String data) {
		APP_LOGS.info("Clicking on any element");
		try {
			if (DriverScript.CONFIG.getProperty("browserType").equalsIgnoreCase("Chrome")) {
				System.out.println("Chrome");
				if (data.equalsIgnoreCase("CSV")) {
					System.out.println("CSV");
					try {
						System.out.println("ypUpload_Browser");
						driver.findElement(By.cssSelector(OR.getProperty("ypUpload_Browser"))).click();
						Thread.sleep(3000);
					} catch (Exception e) {

						try {
							System.out.println("ypUpload_Browser41");
							driver.findElement(By.cssSelector(OR.getProperty("ypUpload_Browser41"))).click();

							Thread.sleep(3000);
						} catch (Exception E2) {
							try {
								System.out.println("ypUpload_Browser12");
								driver.findElement(By.cssSelector(OR.getProperty("ypUpload_Browser12"))).click();
								Thread.sleep(3000);
							} catch (Exception E3) {
								driver.findElement(By.cssSelector(OR.getProperty("ypUpload_Browser32"))).click();

								Thread.sleep(3000);

							}
						}
					}
					// FIle upload logic
					String[] commands = new String[2];
					// File Upload Choose File to
					// Upload FileUpload
					commands = new String[] { DriverScript.CONFIG.getProperty("ChromeUploadExe") };
					Process proc = Runtime.getRuntime().exec(commands);
					Thread.sleep(7000);
				}
			}
			driver.findElement(By.xpath(object)).click();
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Not able to click";
		}
		return Constants.KEYWORD_PASS;
	}

	public String clear(String object, String data) {
		APP_LOGS.info("Clear  any element");
		try {

			WebElement toClear = driver.findElement(By.xpath(object));
			toClear.sendKeys(Keys.chord(Keys.CONTROL, "a"));
			Thread.sleep(1000);
			// toClear.sendKeys(Keys.DELETE);
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_DELETE);
			// driver.findElements(By.xpath(OR.getProperty(object))).clear();

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Not able to clear";
		}
		return Constants.KEYWORD_PASS;
	}

	public String delete(String object, String data) throws InterruptedException {
		APP_LOGS.info("Delete the record");
		try {

			String Contactscount = driver.findElement(By.xpath(object)).getText();
			Thread.sleep(1000);
			System.out.println(Contactscount);

			if (Contactscount.contains("View")) {
				Thread.sleep(3000);
				driver.findElement(By.xpath(OR.getProperty("ypdContacts_DeleteAllCheckbox"))).click();
				Thread.sleep(3000);
				driver.findElement(By.xpath(OR.getProperty("ypdContacts_Delete"))).click();
				Thread.sleep(3000);
				driver.findElement(By.xpath(OR.getProperty("ypdContacts_DeletePOPOK"))).click();
				Thread.sleep(3000);
				driver.findElement(By.xpath(OR.getProperty("ypdContacts_DeleteOk"))).click();
				Thread.sleep(3000);
			}

			else {
				if (!Contactscount.contains("My Lists (0)")) {
					System.out.println("deleting started fomr list2");
					Thread.sleep(3000);
					driver.findElement(By.xpath(OR.getProperty("ypdContacts_DeleteAllCheckbox"))).click();
					Thread.sleep(3000);
					driver.findElement(By.xpath(OR.getProperty("ypdContacts_Delete"))).click();
					Thread.sleep(3000);
					driver.findElement(By.xpath(OR.getProperty("ypdContacts_DeletePOPOK"))).click();
					Thread.sleep(3000);
					driver.findElement(By.xpath(OR.getProperty("ypdContacts_DeleteOk"))).click();
					Thread.sleep(3000);
				}
			}

		} catch (Exception e) {

			return Constants.KEYWORD_FAIL + " Not able to Delete the record";
		}
		return Constants.KEYWORD_PASS;
	}

	public String synchronize(String object, String data) {
		APP_LOGS.info("Waiting for page to load");
		((JavascriptExecutor) driver).executeScript("function pageloadingtime()" + "{"
				+ "return 'Page has completely loaded'" + "}" + "return (window.onload=pageloadingtime());");

		return Constants.KEYWORD_PASS;
	}

	public String waitForElementVisibility(String object, String data) {
		APP_LOGS.info("Waiting for an element to be visible");
		int start = 0;
		int time = (int) Double.parseDouble(data);
		try {
			while (time == start) {
				if (driver.findElements(By.xpath(object)).size() == 0) {
					Thread.sleep(1000L);
					start++;
				} else {
					break;
				}
			}
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + "Unable to close browser. Check if its open" + e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}

	public String waitForElementLocated(String object, String data) {
		APP_LOGS.info("Waiting for an element to be Located");

		try {
			String internalwait = CONFIG.getProperty("ImplicitWaitForElement");
			int internalwaitNum = Integer.parseInt(internalwait);
			new WebDriverWait(driver, internalwaitNum);
			System.out.println("Level A");
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty("LogoutLink"))));
			System.out.println("Level B");
		} catch (Exception e) {
			e.printStackTrace();
			return Constants.KEYWORD_FAIL + ":	Wait for element Located " + e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}

	public String closeBrowser(String object, String data) {
		APP_LOGS.info("Closing the browser");
		try {
			// driver.close();
			driver.quit();
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + "Unable to close browser. Check if its open" + e.getMessage();
		}
		return Constants.KEYWORD_PASS;

	}

	/*
	 * public String pause(String object, String data) throws NumberFormatException,
	 * InterruptedException{ long time = (long)Double.parseDouble(object);
	 * Thread.sleep(time*1000L); return Constants.KEYWORD_PASS; }
	 */

	public String pause(String object, String data) throws NumberFormatException, InterruptedException {
		// double time = Double.parseDouble(object);
		// String Datavalue1 = String.format("%.0f", new BigDecimal(object));
		long time = Long.parseLong(object);
		// int time = Integer.parseInt(Datavalue1);
		Thread.sleep(time * 1000l);
		return Constants.KEYWORD_PASS;
	}

	/************************
	 * APPLICATION SPECIFIC KEYWORDS
	 ********************************/

	public String validateLogin(String object, String data) {
		// object of the current test XLS
		// name of my current test case
		System.out.println("xxxxxxxxxxxxxxxxxxxxx");
		String data_flag = currentTestSuiteXLS.getCellData(currentTestCaseName, "Data_correctness",
				currentTestDataSetID);
		while (driver.findElements(By.xpath(OR.getProperty("image_login_process"))).size() != 0) {
			try {
				String visiblity = driver.findElement(By.xpath(OR.getProperty("image_login_process")))
						.getAttribute("style");
				System.out.println("System Processing request - " + visiblity);
				if (visiblity.indexOf("hidden") != -1) {
					// error message on screen
					// YOUR WORK
					String actualErrMsg = driver.findElement(By.xpath(OR.getProperty("error_login"))).getText();
					// String expected=OR;
					if (data_flag.equals(Constants.POSITIVE_DATA))
						return Constants.KEYWORD_FAIL;
					else
						return Constants.KEYWORD_PASS;
				}

			} catch (Exception e) {

			}
		}

		// check for page title
		if (data_flag.equals(Constants.POSITIVE_DATA))
			return Constants.KEYWORD_PASS;
		else
			return Constants.KEYWORD_FAIL;
	}

	public String verifySearchResults(String object, String data) {
		APP_LOGS.info("Verifying the Search Results");

		try {
			data = data.toLowerCase();
			for (int i = 3; i <= 5; i++) {
				String text = driver.findElement(By.xpath(OR.getProperty("search_result_heading_start") + i
						+ OR.getProperty("search_result_heading_end"))).getText().toLowerCase();
				if (text.indexOf(data) == -1) {
					return Constants.KEYWORD_FAIL + " Got the text - " + text;
				}
			}

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + "Error -->" + e.getMessage();
		}

		return Constants.KEYWORD_PASS;

	}

	// not a keyword

	public void captureScreenshot(String filename, String keyword_execution_result) throws IOException {
		// take screen shots
		if (CONFIG.getProperty("screenshot_everystep").equals("Y")) {
			// capturescreen

			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile,
					new File(System.getProperty("user.dir") + "//screenshots//" + filename + ".jpg"));

		} else if (keyword_execution_result.startsWith(Constants.KEYWORD_FAIL)
				&& CONFIG.getProperty("screenshot_error").equals("Y")) {
			// capture screenshot
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile,
					new File(System.getProperty("user.dir") + "//screenshots//" + filename + ".jpg"));
		}
	}

// ********************************** AccuCoonect Project related Methods *****************************************

	public String VerifyIsElementClickable(String object, String data) {
		String internalwait = CONFIG.getProperty("ImplicitWaitForElement");
		int internalwaitNum = Integer.parseInt(internalwait);
		wait = new WebDriverWait(driver, internalwaitNum);
		try {
			APP_LOGS.info("Wait until Element is clickable");
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(object)));
			Thread.sleep(4000);
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Element Not Found or Clickable " + e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}

	public String EmptyShoppingCart(String object, String data) {
		try {
			boolean ErrorMsgStatus = driver.findElement(By.xpath(OR.getProperty("NoCartItemsErrorMsg"))).isDisplayed();
			if (ErrorMsgStatus == false) {
				driver.findElement(By.xpath(OR.getProperty("EmptycartLowerLink"))).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(OR.getProperty("EmptyCartConfirmOK"))).click();
				Thread.sleep(2000);
			}
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Unable to empty the cart properly " + e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}

	public String SelectDropDown(String object, String data) throws InterruptedException, AWTException {
		String SelectedText;
		SelectedText = driver.findElement(By.xpath(object)).getText();
		System.out.println("SelectedText :" + SelectedText);
		System.out.println("Data :" + data);
		if (data.contains(SelectedText)) {
			System.out.println("Expected values already Selected, so no need to change");
			APP_LOGS.info("Expected values already Selected, so no need to change");
		} else {
			Robot r = new Robot();
			/*
			 * for(int j=0; j<=25;j++) {
			 * driver.findElement(By.xpath(OR.getProperty(object))).click();
			 * Thread.sleep(50); r.keyPress(KeyEvent.VK_UP); Thread.sleep(50); }
			 * 
			 * r.keyPress(KeyEvent.VK_ENTER); Thread.sleep(5000);
			 */

			for (int i = 0; i <= 5; i++) {
				driver.findElement(By.xpath(object)).click();
				Thread.sleep(1000);
				r.keyPress(KeyEvent.VK_DOWN);
				Thread.sleep(1000);
				r.keyPress(KeyEvent.VK_ENTER);
				Thread.sleep(5000);
				SelectedText = driver.findElement(By.xpath(object)).getText();
				System.out.println("SelectedText2 :" + SelectedText);
				Thread.sleep(5000);
				if (SelectedText.contains(data)) {
					System.out.println("Expected values Selected");
					APP_LOGS.debug("Expected values Selected");
					break;
				}

			}
		}

		return Constants.KEYWORD_PASS;
	}

	/*
	 * public String EnterDataWithKeywords(String object,String data) { try{
	 * System.out.println("Enter in to the Keyboard values");
	 * driver.findElement(By.xpath(object)).click(); Thread.sleep(1000);
	 * org.openqa.selenium.Keyboard kb = ((RemoteWebDriver) driver).getKeyboard();
	 * kb.sendKeys(data);
	 * 
	 * } catch(Exception e) { return Constants.KEYWORD_FAIL
	 * +" Unable to empty the cart properly "+ e.getMessage(); } return
	 * Constants.KEYWORD_PASS; }
	 */
	public String mouseHover(String object, String data) {
		try {

			WebElement w = driver.findElement(By.xpath(object));
			Actions action = new Actions(driver);
			action.moveToElement(w).build().perform();
			Thread.sleep(3000);
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Unable to mousehover " + e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}

	public String swithToFrame(String object, String data) {
		driver.switchTo().frame(object);

		return Constants.KEYWORD_PASS;

	}

	public String sikuliClick(String object, String data) {
		Screen s = new Screen();
		try {
			s.click(data);
			Thread.sleep(3000);
			driver.findElement(By.xpath(object)).sendKeys("test");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return Constants.KEYWORD_FAIL + " Unable to empty the cart properly " + e.getMessage();
		}
		return Constants.KEYWORD_PASS;

	}

	public String sikuliwrite(String object, String data) {

		System.out.println("Object:" + object);
		System.out.println("Data:" + data);
		// String username=Config.getSystemProperty(UserName);
		Screen s = new Screen();
		try {
			// s.type("object", "data");
			s.click(data);
			s.type(object);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return Constants.KEYWORD_FAIL + " Unable to empty the cart properly " + e.getMessage();
		}
		return Constants.KEYWORD_PASS;

	}

	public String sikuliclickandwrite(String object[], String data[]) {

		System.out.println("Object:" + object[0]);
		System.out.println("Data:" + data[0]);
		System.out.println("Data:" + data[1]);
		Screen s = new Screen();
		try {
			s.click(data[0]);
			s.type(data[1]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return Constants.KEYWORD_FAIL + " Unable to empty the cart properly " + e.getMessage();
		}
		return Constants.KEYWORD_PASS;

	}
	/*
	 * public String sikulihover(String object,String data) { Screen s=new Screen();
	 * try { s.hover(data); } catch (FindFailed e) { // TODO Auto-generated catch
	 * block return Constants.KEYWORD_FAIL +" Unable to empty the cart properly "+
	 * e.getMessage(); } return Constants.KEYWORD_PASS;
	 * 
	 * }
	 */

	public String getCheckboxCount(String object, String data) {
		List<WebElement> boxes = driver.findElements(By.className("tdwidth5"));
		int numberOfBoxes = boxes.size();
		System.out.println("number of chech boxes" + numberOfBoxes);
		String str = String.valueOf(numberOfBoxes);
		return str;

	}
	// selectdropdownbytext

	public String selectdropdownbytext(String object, String data) {

		APP_LOGS.debug("selectdropdownbytext Method START");
		try {

			Select select = new Select(driver.findElement(By.xpath(object)));
			// Select select = new
			// Select(By.xpath("//*[@id='divCategory']/ul/span/span").tagName("Option"));
			List<WebElement> list = select.getOptions();
			APP_LOGS.debug("list... " + list);
			for (WebElement option : list) {

				String fullText = option.getText();

				if (fullText.contains(data)) {

					select.selectByVisibleText(fullText);
					APP_LOGS.debug("Data... " + data);
					APP_LOGS.debug("FullText... " + fullText);
					break;
				}
			}

		}

		catch (Exception e) {
			APP_LOGS.debug("selectdropdownbytext Method END");
			return Constants.KEYWORD_FAIL + " Unable to select value from dropdown" + e.getMessage();

		}
		APP_LOGS.debug("selectdropdownbytext Method END");
		return Constants.KEYWORD_PASS;

	}

	// selectdropdownbyindex
	public String selectdropdownbyindex(String object, String data) {
		APP_LOGS.debug("Selecting the value");
		try {
			Integer index = Integer.valueOf(data);
			new Select(driver.findElement(By.xpath(object))).selectByIndex(index);

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " - Could not select from list. " + e.getMessage();
		}

		return Constants.KEYWORD_PASS;

	}

	public String EnterKey(String object, String data) {
		APP_LOGS.debug("Clicking on Enter Key");
		try {

			Actions action = new Actions(driver);

			action.sendKeys(Keys.ENTER).build().perform();

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " - Failed entering";
		}
		return Constants.KEYWORD_PASS;

	}

	// Get all options from dropdown

	public String getDropdownValues(String object, String data) {
		APP_LOGS.debug("Clicking on Enter Key");
		try {
			Select dropdown = new Select(driver.findElement(By.xpath(object)));

			// Get all options
			List<WebElement> paymentmethods = dropdown.getOptions();

			// Get the length
			System.out.println("Available options:" + paymentmethods.size());

			// Loop to print one by one
			for (int j = 0; j < paymentmethods.size(); j++) {
				System.out.println(paymentmethods.get(j).getText());

			}
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " - Failed entering";
		}
		return Constants.KEYWORD_PASS;

	}

	// get order number

	public String getordernumber(String object, String data) {
		APP_LOGS.debug("Get order number");
		try {

			String orderno = driver.findElement(By.xpath("object")).getText();
			System.out.println("Ordernumber:" + orderno);
			File file = new File("D:\\kpmg1.0\\src\\com\\kpmg\\config\\config1.properties");
			String text = "ordernumber= " + orderno;
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(text.getBytes());
			fileOutputStream.close();
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " - Failed entering";
		}
		return Constants.KEYWORD_PASS;
	}

	// select
	public String selectV(String object, String data) {
		APP_LOGS.debug("Selecting the value");
		try {
			new Select(driver.findElement(By.xpath(object))).selectByVisibleText(data);

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " - Could not select from list. " + e.getMessage();
		}

		return Constants.KEYWORD_PASS;
	}

	/*
	 * public String store_value(String object, String data) {
	 * APP_LOGS.debug("Store value Method Start"); try { String actual = driver
	 * .findElement(By.xpath(object)).getText(); System.out.println("test:"+actual);
	 * APP_LOGS.debug("actual..."+actual);
	 * currentTestSuiteXLS.setCellData(currentTestCaseName, currentExcellColumnName,
	 * currentTestDataSetID, actual);
	 * 
	 * } catch (Exception e) { APP_LOGS.debug("Store value Method End"); return
	 * Constants.KEYWORD_FAIL + " Unable to Store value in excell" + e.getMessage();
	 * 
	 * } APP_LOGS.debug("Store value Method End"); return Constants.KEYWORD_PASS; }
	 */
	// geting current date with time
	public String VariablewithDate(String object, String data) {
		APP_LOGS.debug("Adding date extention Method Start");
		try {
			DateFormat dateFormat = new SimpleDateFormat("MM:dd:yyy:HH:mm:ss");
			// MM/dd/yyyy/HH:mm
			Date date = new Date();

			String Name = data + dateFormat.format(date);
			// Name = dateFormat.format(date);

			driver.findElement(By.xpath(object)).sendKeys(Name);

		} catch (Exception e) {
			APP_LOGS.debug("Adding date extention Method End");
			return Constants.KEYWORD_FAIL + "Error -->Count not matched" + e.getMessage();
		}
		APP_LOGS.debug("Adding date extention Method End");

		return Constants.KEYWORD_PASS;
	}

	// store values
	public String store_value(String object, String data) {
		APP_LOGS.debug("Store value Method Start");
		try {
			String actual = driver.findElement(By.xpath(object)).getText();
			System.out.println("test:" + actual);
			APP_LOGS.debug("actual..." + actual);

			currentTestSuiteXLS.setCellData(currentTestCaseName, currentExcellColumnName, currentTestDataSetID, actual);

		} catch (Exception e) {
			APP_LOGS.debug("Store value Method End");
			return Constants.KEYWORD_FAIL + " Unable to Store value in excell" + e.getMessage();

		}
		APP_LOGS.debug("Store value Method End");
		return Constants.KEYWORD_PASS;
	}

	// to upload file
	public String file_upload(String object, String data) throws Exception {

		APP_LOGS.debug("upload file");

		try {
			driver.findElement(By.xpath(object)).click();

			Thread.sleep(2000);

			Robot rb = new Robot();

			// Enter user name by ctrl-v

			StringSelection fileupload = new StringSelection(System.getProperty("user.dir") + data);

			System.out.println("fileupload :" + fileupload);

			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(fileupload, null);

			rb.keyPress(KeyEvent.VK_CONTROL);
			Thread.sleep(2000);
			rb.keyPress(KeyEvent.VK_V);
			rb.keyRelease(KeyEvent.VK_V);
			rb.keyRelease(KeyEvent.VK_CONTROL);
			Thread.sleep(2000);
			rb.keyPress(KeyEvent.VK_ENTER);
			rb.keyRelease(KeyEvent.VK_ENTER);
			Thread.sleep(2000);

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Unable to upload file" + e.getMessage();
		}
		APP_LOGS.debug("unable to upload file");
		return Constants.KEYWORD_PASS;

	}

	public String StringCon(String object[], String data[]) {
		APP_LOGS.debug("String concaninate");
		try {
			System.out.println(data[0]);
			System.out.println(data[1]);
			String s = data[0] + "-" + data[1];
			System.out.println("New string:" + s);
			currentTestSuiteXLS.setCellData(currentTestCaseName, currentExcellColumnName, currentTestDataSetID, s);

		} catch (Exception e) {
			APP_LOGS.debug("Navigating to URL Method End");
			return Constants.KEYWORD_FAIL + " -- Not able to navigate" + e.getMessage();
		}
		APP_LOGS.debug("String concanination Method End");
		return Constants.KEYWORD_PASS;
	}
}
