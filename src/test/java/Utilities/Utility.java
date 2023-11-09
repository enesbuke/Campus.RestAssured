package Utilities;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class Utility {
    public Faker rndProd = new Faker();
    public RequestSpecification reqSpec;

    @BeforeClass
    public void Setup() {
        baseURI = "https://test.mersys.io/";

        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("username", "turkeyts");
        loginMap.put("password", "TechnoStudy123");
        loginMap.put("rememberMe", "true");

        Cookies cookies =
                given()
                        .body(loginMap).contentType(ContentType.JSON)
                        .when()
                        .post("/auth/login")
                        .then()
                        .statusCode(200)
                        .extract().response().getDetailedCookies();

        reqSpec = new RequestSpecBuilder().
                addCookies(cookies).setContentType(ContentType.JSON)
                .build();

    }

}
