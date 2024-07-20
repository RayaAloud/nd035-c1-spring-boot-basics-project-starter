package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;
	private WebDriver driver;
	@Autowired
	private CredentialMapper credentialMapper;
	@Autowired
	private EncryptionService encryptionService;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.firefoxdriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new FirefoxDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("operation-success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}


	@Test
	public void homePageAccess(){
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotEquals("Home",driver.getTitle());
	}


	@Test
	public void unauthorizedAccess(){
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login",driver.getTitle());

		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up",driver.getTitle());

		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login",driver.getTitle());
	}
	@Test
	public void flowVerification(){
		doMockSignUp("Flow Verification","Test","FV","123");
		doLogIn("FV", "123");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.titleContains("Home"));
		Assertions.assertEquals("Home",driver.getTitle());
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout-button")));
		driver.findElement(By.id("logout-button")).click();
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotEquals("Home",driver.getTitle());

	}
	@Test
	public void addNote(){
		doMockSignUp("Add Note","Test","AN","123");
		doLogIn("AN", "123");

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		addNote("Test note title","Test note description" );
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement tab = driver.findElement(By.id("nav-notes-tab"));
		tab.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notes-table-body")));
		WebElement tableBody = driver.findElement(By.id("notes-table-body"));
		Assertions.assertTrue(tableBody.getText().contains("Test note title")&&
				tableBody.getText().contains("Test note description"));

	}
	@Test
	public void editNote(){
		doMockSignUp("Edit Note","Test","EN","123");
		doLogIn("EN", "123");

		addNote("Test note title 1","Test note description 2");
		addNote("Test note title 2","Test note description 2");

		String title= "Title updated";
		String description= "Description updated";
		WebDriverWait webDriverWait = new WebDriverWait(driver, 3);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement tab = driver.findElement(By.id("nav-notes-tab"));
		tab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notes-table-edit")));
		WebElement editButton = driver.findElement(By.id("notes-table-edit"));
		editButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.click();
		noteTitle.clear();
		noteTitle.sendKeys(title);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.click();
		noteDescription.clear();
		noteDescription.sendKeys(description);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-note")));
		WebElement submitButton = driver.findElement(By.id("save-note"));
		submitButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		tab = driver.findElement(By.id("nav-notes-tab"));
		tab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notes-table-body")));
		WebElement tableBody = driver.findElement(By.id("notes-table-body"));
		Assertions.assertTrue(tableBody.getText().contains(title)&&
				tableBody.getText().contains(description));

	}
	@Test
	public void deleteNote(){
		doMockSignUp("Delete Note","Test","DN","123");
		doLogIn("DN", "123");

		addNote("Test note title 1","Test note description 1");
		addNote("Test note title 2","Test note description 2");


		WebDriverWait webDriverWait = new WebDriverWait(driver, 3);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement tab = driver.findElement(By.id("nav-notes-tab"));
		tab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notes-table-delete")));
		WebElement deleteButton = driver.findElement(By.id("notes-table-delete"));
		deleteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		tab = driver.findElement(By.id("nav-notes-tab"));
		tab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notes-table-body")));
		WebElement tableBody = driver.findElement(By.id("notes-table-body"));
		Assertions.assertFalse(tableBody.getText().contains("Test note title 1")||
				tableBody.getText().contains("Test note description 1"));
	}
	private void addNote(String title ,String desc){
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement tab = driver.findElement(By.id("nav-notes-tab"));
		tab.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-note-button")));
		WebElement addNoteButton = driver.findElement(By.id("add-note-button"));
		addNoteButton.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.click();
		noteTitle.sendKeys(title);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.click();
		noteDescription.sendKeys(desc);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-note")));
		WebElement submitButton = driver.findElement(By.id("save-note"));
		submitButton.click();
	}
	@Test
	public void addCredential(){
		doMockSignUp("Add Credential","Test","AC","123");
		doLogIn("AC", "123");
		addCredential("URL","Username","Password");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 3);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement tab = driver.findElement(By.id("nav-credentials-tab"));
		tab.click();
		WebElement credentialId = driver.findElement(By.id("credential-table-id"));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentials-table-body")));
		WebElement tableBody = driver.findElement(By.id("credentials-table-body"));
		Assertions.assertTrue(tableBody.getText().contains("URL")&&
				tableBody.getText().contains("Username")
				&& tableBody.getText().contains(encryptionService.encryptValue("Password",credentialMapper.getKeyByCredentialId(credentialId.getAttribute("textContent")))));
	}
	@Test
	public void editCredential(){
		doMockSignUp("Edit Credential","Test","EC","123");
		doLogIn("EC", "123");

		addCredential("URL 1","Username 1","Password 1");
		addCredential("URL 2","Username 2","Password 2");

		WebDriverWait webDriverWait = new WebDriverWait(driver, 3);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement tab = driver.findElement(By.id("nav-credentials-tab"));
		tab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-credential")));
		WebElement addCredentialButton = driver.findElement(By.id("edit-credential"));
		addCredentialButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement urlElement = driver.findElement(By.id("credential-url"));
		urlElement.click();
		urlElement.clear();
		urlElement.sendKeys("Url updated");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement userNameElement = driver.findElement(By.id("credential-username"));
		userNameElement.click();
		userNameElement.clear();
		userNameElement.sendKeys("Username updated");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement passwordElement = driver.findElement(By.id("credential-password"));
		String viewablePassword=passwordElement.getAttribute("value");
		passwordElement.click();
		passwordElement.clear();
		passwordElement.sendKeys("Password updated");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-save")));
		WebElement submit = driver.findElement(By.id("credential-save"));
		submit.click();

		Assertions.assertEquals("Password 1",viewablePassword);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement navTab = driver.findElement(By.id("nav-credentials-tab"));
		navTab.click();

		WebElement credentialId = driver.findElement(By.id("credential-table-id"));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentials-table-body")));
		WebElement tableBody = driver.findElement(By.id("credentials-table-body"));
		Assertions.assertTrue(tableBody.getText().contains("Url updated")&&
				tableBody.getText().contains("Username updated")
				&& tableBody.getText().contains(encryptionService.encryptValue("Password updated",credentialMapper.getKeyByCredentialId(credentialId.getAttribute("textContent")))));

	}
	@Test
	public void deleteCredential(){
		doMockSignUp("Delete Credential","Test","DC","123");
		doLogIn("DC", "123");
		addCredential("URL 1","Username 1","Password 1");
		addCredential("URL 2","Username 2","Password 2");

		WebDriverWait webDriverWait = new WebDriverWait(driver, 3);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement tab = driver.findElement(By.id("nav-credentials-tab"));
		tab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pass")));
		WebElement credentialPass = driver.findElement(By.id("pass"));
		String encryptedPass=credentialPass.getText();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-credential")));
		WebElement delete = driver.findElement(By.id("delete-credential"));
		delete.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		tab = driver.findElement(By.id("nav-credentials-tab"));
		tab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentials-table-body")));
		WebElement tableBody = driver.findElement(By.id("credentials-table-body"));
		Assertions.assertFalse(tableBody.getText().contains("URL 1")||
				tableBody.getText().contains("Username 1")||
				tableBody.getText().contains(encryptedPass));
	}

	private void addCredential(String url, String username, String password){
		WebDriverWait webDriverWait = new WebDriverWait(driver, 3);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement tab = driver.findElement(By.id("nav-credentials-tab"));
		tab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-credential-button")));
		WebElement addCredentialButton = driver.findElement(By.id("add-credential-button"));
		addCredentialButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement urlElement = driver.findElement(By.id("credential-url"));
		urlElement.click();
		urlElement.sendKeys(url);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement userNameElement = driver.findElement(By.id("credential-username"));
		userNameElement.click();
		userNameElement.sendKeys(username);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement passwordElement = driver.findElement(By.id("credential-password"));
		passwordElement.click();
		passwordElement.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-save")));
		WebElement submit = driver.findElement(By.id("credential-save"));
		submit.click();
	}



}
