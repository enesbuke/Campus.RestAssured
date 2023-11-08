package Campus;

import Utilities.Utility;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.github.javafaker.Faker;

import java.util.HashMap;
import java.util.Map;


public class HR_Positions extends Utility {

    Map<String, String> newPositions = new HashMap<>();

    String positionsID = " ";
    String positionsName = " ";
    String positionsShort = " ";


    @Test
    public void createPositions() {

        positionsName = "cobidik" + rndProd.number().digits(1);
        positionsShort = "cobidik" + rndProd.number().digits(1);

        newPositions.put("name", positionsName);
        newPositions.put("shortName", positionsShort);
        newPositions.put("tenantId", "646cb816433c0f46e7d44cb0");
        newPositions.put("active", "true");

        positionsID =
                given()


                        .spec(reqSpec)
                        .body(newPositions)

                        .when()
                        .post("school-service/api/employee-position")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");
        ;
    }

    @Test(dependsOnMethods = "createPositions")
    public void createPositionsNegative() {

        given()

                .spec(reqSpec)
                .body(newPositions)
                .log().body()

                .when()
                .post("/school-service/api/employee-position")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"))
        ;
    }

    @Test(dependsOnMethods = "createPositionsNegative")
    public void updatePositions() {

        newPositions.put("id", positionsID);

        positionsName = ("Sinem" + rndProd.number().digits(1));
        newPositions.put("name", positionsName);
        newPositions.put("shortName", positionsShort);

        given()

                .spec(reqSpec)
                .body(newPositions)

                .when()
                .put("/school-service/api/employee-position/")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(positionsName))
        ;
    }

    @Test(dependsOnMethods = "updatePositions")
    public void deletePositions() {

        given()

                .spec(reqSpec)
                .pathParam("positionsID", positionsID)
                .log().uri()

                .when()
                .delete("/school-service/api/employee-position/{positionsID}")

                .then()
                .log().body()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "deletePositions")
    public void deletePositionsNegative() {

        given()

                .spec(reqSpec)
                .pathParam("positionsID", positionsID)
                .log().uri()

                .when()
                .delete("/school-service/api/employee-position/{positionsID}")

                .then()
                .log().body()
                .statusCode(204)
        ;
    }
}

