package Campus;

import Utilities.Utility;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;


public class GradeLevels extends Utility {
    Map<String, String> gLevel = new HashMap<>();

    String GLevelID;
    String GLevelName;
    String GLevelShortName;

    @Test
    public void createGradeLevel() {

        gLevel = new HashMap<>();
        GLevelName = rndProd.name().firstName() + rndProd.number().digits(1);
        GLevelShortName = rndProd.name().lastName() + rndProd.number().digits(1);

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
                        .extract().path("id")
        ;
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
        GLevelName = ("Team8" + rndProd.number().digits(1));
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
