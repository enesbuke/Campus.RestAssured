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


public class GradeLevels {

    Faker f = new Faker();
    RequestSpecification reqSpec;
    Map<String, String> gLevel= new HashMap<>();

    String GLevelID;
    String GLevelName;
    String GLevelShortName;

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
    public void createGradeLevel() {

        gLevel = new HashMap<>();
        GLevelName = f.name().firstName() + f.number().digits(1);
        GLevelShortName = f.name().lastName() + f.number().digits(1);

        gLevel.put("name", GLevelName);
        gLevel.put("shortName", GLevelShortName);
        gLevel.put("active", "true");

        GLevelID =

                given()

                        .spec(reqSpec)
                        .body(gLevel)
                        .log().body()

                        .when()
                        .post("school-service/api/grade-levels")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");


    }


    @Test(dependsOnMethods = "createGradeLevel")
    public void createGradeLevelNegative() {

        gLevel.put("name", GLevelName);
        gLevel.put("shortName", GLevelShortName);

        given()

                .spec(reqSpec)
                .body(gLevel)
                .log().body()

                .when()
                .post("school-service/api/grade-levels")

                .then()
                .log().body()
                .statusCode(400)

        ;
    }

    @Test(dependsOnMethods = "createGradeLevelNegative")
    public void updateGradeLevel() {

        gLevel.put("id", GLevelID);
        GLevelName = ("Team8" + f.number().digits(1));
        gLevel.put("name", GLevelShortName);
        gLevel.put("shortName", GLevelShortName);

        given()

                .spec(reqSpec)
                .body(gLevel)

                .when()
                .put("school-service/api/grade-levels")

                .then()
                .log().body()
                .statusCode(200)

        ;

    }

    @Test(dependsOnMethods = "updateGradeLevel")
    public void deleteGradeLevel() {

        given()

                .spec(reqSpec)
                .pathParam("levelID", GLevelID)
                .log().uri()

                .when()
                .delete("school-service/api/grade-levels/{levelID}")

                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "deleteGradeLevel")
    public void deleteGradeLevelNegative() {

        given()

                .spec(reqSpec)
                .pathParam("levelID", GLevelID)
                .log().uri()

                .when()
                .delete("school-service/api/grade-levels/{levelID}")

                .then()
                .log().body()
                .statusCode(400)

        ;
    }
}
