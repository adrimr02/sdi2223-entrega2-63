package com.uniovi.sdi2223entrega2test63;

import com.uniovi.sdi2223entrega2test63.pageobjects.*;
import com.uniovi.sdi2223entrega2test63.util.MongoDB;
import com.uniovi.sdi2223entrega2test63.util.SeleniumUtils;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Sdi2223Entrega2TestApplicationTests {
    static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";

    //static String Geckodriver= "C:\\Users\\Daniel Alonso\\Desktop\\PL-SDI-Sesión5-material\\geckodriver-v0.30.0-win64.exe";
    static String Geckodriver = "E:\\ADRIAN\\Uniovi\\Curso 3\\SDI\\drivers\\geckodriver.exe";
    //static String Geckodriver = "C:\\Users\\larry\\Desktop\\UNI\\SDI\\PL-SDI-Sesio╠ün5-material\\geckodriver-v0.30.0-win64.exe";

    //static String Geckodriver = "C:\\Dev\\tools\\selenium\\geckodriver-v0.30.0-win64.exe";
    //static String PathFirefox = "/Applications/Firefox.app/Contents/MacOS/firefox-bin";
    //static String Geckodriver = "/Users/USUARIO/selenium/geckodriver-v0.30.0-macos";
    static WebDriver driver = getDriver(PathFirefox, Geckodriver);
    static String URL = "http://localhost:8080";

    public static WebDriver getDriver(String PathFirefox, String Geckodriver) {
        System.setProperty("webdriver.firefox.bin", PathFirefox);
        System.setProperty("webdriver.gecko.driver", Geckodriver);
        driver = new FirefoxDriver();
        return driver;
    }

    @BeforeEach
    public void setUp() {
        driver.navigate().to(URL);
    }

    //Después de cada prueba se borran las cookies del navegador
    @AfterEach
    public void tearDown() {
        driver.manage().deleteAllCookies();
    }

    //Antes de la primera prueba
    @BeforeAll
    static public void begin() {
        MongoDB m = new MongoDB();
        m.resetMongo();
    }

    //Al finalizar la última prueba
    @AfterAll
    static public void end() {
        //Cerramos el navegador al finalizar las pruebas
        driver.quit();
    }

    /*
     * ###################
     * Pruebas de registro de usuario
     * ###################
     */

    /**
     * Registro de Usuario con datos válidos
     */
    @Test
    @Order( 1 )
    void P1() {
        //Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_SignUpView.fillForm(driver, "newuser1", "20", "newuser1@email.com",
                "10/12/2002", "newpass1", "newpass1");
        //Comprobamos que entramos en la sección privada y nos nuestra el texto a buscar
        List<WebElement> result = PO_View.checkElementBy(driver, "text", "Mis ofertas");
        Assertions.assertEquals("Mis ofertas", result.get(0).getText());
    }

    /**
     * Registro de Usuario con datos inválidos (campos vacíos)
     */
    @Test
    @Order(2)
    void P2() {
        //Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Pulsamos el botón para enviar el formulario sin haberlo rellenado
        By boton = By.className("btn");
        driver.findElement(boton).click();
        List<WebElement> result = PO_SignUpView.checkElementBy(driver, "text", "Es obligatorio rellenar todos los campos." );
        Assertions.assertEquals(1, result.size());
    }


    /**
     * Registro de Usuario con datos inválidos (repetición de contraseña inválida)
     */
    @Test
    @Order(3)
    void P3() {
        //Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_SignUpView.fillForm(driver, "newuser2", "20", "newuser2@email.com",
                "10/12/2002", "newpass2", "newpass1");
        //Comprobamos que entramos en la sección privada y nos nuestra el texto a buscar
        List<WebElement> result = PO_View.checkElementBy(driver, "text", "Las contraseñas no coinciden.");
        Assertions.assertEquals(1, result.size());
    }

    /**
     * Registro de Usuario con datos inválidos (email existente).
     */
    @Test
    @Order(4)
    void P4() {
        //Vamos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_SignUpView.fillForm(driver, "newuser3", "20", "user01@email.com",
                "10/12/2002", "newpass3", "newpass3");
        //Comprobamos que entramos en la sección privada y nos nuestra el texto a buscar
        List<WebElement> result = PO_View.checkElementBy(driver, "text", "El email ya está en uso.");
        Assertions.assertEquals(1, result.size());
    }

    /*
     * ###################
     * Pruebas de Login
     * ###################
     */

    /**
     * Inicio de sesión con datos válidos (administrador)
     */
    @Test
    @Order(5)
    void P5() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "admin@email.com", "admin");
        //Comprobamos que entramos en la pagina privada del administrador
        List<WebElement> result = PO_View.checkElementBy(driver, "text", "Usuarios:");
        Assertions.assertEquals("Usuarios:", result.get(0).getText());
    }

    /**
     * Inicio de sesión con datos válidos (usuario estándar)
     */
    @Test
    @Order(6)
    void P6() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Comprobamos que entramos en la pagina privada del usuario
        List<WebElement> result = PO_View.checkElementBy(driver, "text", "Mis ofertas");
        Assertions.assertEquals("Mis ofertas", result.get(0).getText());
    }

    /**
     * Inicio de sesión con datos inválidos (usuario estándar, email existente, pero contraseña
     * incorrecta).
     */
    @Test
    @Order(7)
    void P7() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user02");
        List<WebElement> result = PO_LoginView.checkElementBy(driver, "text", "Email o contraseña inválidos.");
        Assertions.assertEquals(1, result.size());
    }

    /**
     * Inicio de sesión con datos inválidos (campo email o contraseña vacíos)
     */
    @Test
    @Order(8)
    void P8() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Pulsamos el botón para enviar el formulario sin haberlo rellenado
        By boton = By.className("btn");
        driver.findElement(boton).click();
        List<WebElement> result = PO_LoginView.checkElementBy(driver, "text", "Es obligatorio rellenar todos los campos.");
        Assertions.assertEquals(1, result.size());
    }

    /*
     * ###################
     * Pruebas de Logout
     * ###################
     */

    /**
     * Hacer click en la opción de salir de sesión y comprobar que se redirige a la página de inicio
     * de sesión (Login)
     */
    @Test
    @Order( 9 )
    void P9() {
        //Vamos al formulario de logueo.
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos el formulario
        PO_LoginView.fillLoginForm(driver, "user01@email.com", "user01");
        //Comprobamos que entramos en la pagina privada del usuario
        List<WebElement> result = PO_View.checkElementBy(driver, "text", "Mis ofertas");
        Assertions.assertEquals(2, result.size());
        //Ahora nos desconectamos comprobamas que aparece el menu de login
        PO_UserPrivateView.logout(driver);
        List<WebElement> resultlogin = PO_View.checkElementBy(driver, "free", "/html/body/div/h2");
        Assertions.assertEquals("Iniciar Sesión", resultlogin.get(0).getText());
    }

    /**
     * Comprobar que el botón cerrar sesión no está visible si el usuario no está autenticado
     */
    @Test
    @Order( 10 )
    public void P10() {
        // Comprueba que no existe el dropdown con la informacion del usuario,
        // incluyendo el boton de logout
        SeleniumUtils.textIsNotPresentOnPage( driver, "Cerrar sesión");
    }

    /* Ejemplos de pruebas de llamada a una API-REST */
    /* ---- Probamos a obtener lista de canciones sin token ---- */
    @Test
    @Order(11)
    public void PR11() {
        final String RestAssuredURL = "http://localhost:8081/api/v1.0/songs";
        Response response = RestAssured.get(RestAssuredURL);
        Assertions.assertEquals(403, response.getStatusCode());
    }

    @Test
    @Order(38)
    public void PR38() {
        final String RestAssuredURL = "http://localhost:8081/api/v1.0/users/login";
        //2. Preparamos el parámetro en formato JSON
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", "prueba1@prueba1.com");
        requestParams.put("password", "prueba1");
        request.header("Content-Type", "application/json");
        request.body(requestParams.toJSONString());
        //3. Hacemos la petición
        Response response = request.post(RestAssuredURL);
        //4. Comprobamos que el servicio ha tenido exito
        Assertions.assertEquals(200, response.getStatusCode());
    }
}