package Campus;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class SchoolSetupLocation {

    Faker f = new Faker();
    RequestSpecification reqSpec;
    Map <String, String> SSLocation = new HashMap<>();

    String SSID;
    String SSName;
    String SSShortName;
    String SSCapacity;

    @BeforeClass
    public void Setup() {

        baseURI = "https://test.mersys.io/";

        Map<String, String> userCredential = new HashMap<>();
        userCredential.put("username", "turkeyts");
        userCredential.put("password", "TechnoStudy123");
        userCredential.put("rememberMe", "true");

        Cookies cookies =
                given()
                        .contentType(ContentType.JSON)
                        .body(userCredential)

                        .when()
                        .post("/auth/login")

                        .then()
                        .statusCode(200)
                        .extract().response().getDetailedCookies();

        reqSpec = new RequestSpecBuilder()
                .addCookies(cookies)
                .setContentType(ContentType.JSON)
                .build();
    }
    @Test
    public void createSchoolSetupLocation() {

        SSLocation = new HashMap<>();

        SSName = f.name().firstName() + f.number().digits(1);
        SSShortName = f.name().lastName() + f.number().digits(1);
        SSCapacity = f.number().digits(1);

        SSLocation.put("name", SSName);
        SSLocation.put("shortName", SSShortName);
        SSLocation.put("capacity", SSCapacity);
        SSLocation.put("type", "CLASS");
        SSLocation.put("active", "true");
        SSLocation.put("school", "646cbb07acf2ee0d37c6d984");

        SSID =

                given()

                        .spec(reqSpec)
                        .body(SSLocation)
                        .log().body()

                        .when()
                        .post("/school-service/api/location")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("LocationID = " + SSID);
    }

    @Test(dependsOnMethods = "createSchoolSetupLocation")
    public void createSchoolSetupLocationNegative() {

        SSLocation.put("name", SSName);
        SSLocation.put("shortName", SSShortName);

        given()

                .spec(reqSpec)
                .body(SSLocation)
                .log().body()

                .when()
                .post("/school-service/api/location")

                .then()
                .log().body()
                .statusCode(400)
        ;
    }

    @Test(dependsOnMethods = "createSchoolSetupLocationNegative")
    public void updateSchoolSetupLocation() {

        SSLocation.put("id", SSID);
        SSName = ("Team8" + f.number().digits(1));
        SSLocation.put("name", SSName);
        SSLocation.put("shortName", SSShortName);

        given()

                .spec(reqSpec)
                .body(SSLocation)

                .when()
                .put("/school-service/api/location")

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(SSID))
        ;
    }

    @Test(dependsOnMethods = "updateSchoolSetupLocation")
    public void deleteSchoolSetupLocation() {

        given()

                .spec(reqSpec)
                .pathParam("SchoolSetupLocationID", SSID)
                .log().uri()

                .when()
                .delete("/school-service/api/location/{SchoolSetupLocationID}")

                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "deleteSchoolSetupLocation")
    public void deleteSchoolSetupLocationNegative() {
        given()

                .spec(reqSpec)
                .pathParam("SchoolSetupLocationID", SSID)
                .log().uri()

                .when()
                .delete("/school-service/api/location/{SchoolSetupLocationID}")

                .then()
                .log().body()
                .statusCode(400)
        ;
    }
}
