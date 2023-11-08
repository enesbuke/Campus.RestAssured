package TestCases;

import Utilities.Utility;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class SubjectCategories extends Utility {
    Map<String, String> scKeys = new HashMap<>();
    String subjectId;
    String name;
    String code = rndProd.number().digits(5);
    boolean active = true;

    @Test
    public void createSubjectCategories() {
        name = rndProd.name().firstName();


        scKeys.put("name", name);
        scKeys.put("code", code);
        scKeys.put("active", String.valueOf(active));


        subjectId = given()
                .spec(reqSpec)
                .body(scKeys)

                .when()
                .post("school-service/api/subject-categories")

                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id")
        ;
        System.out.println("subjectId = " + subjectId);
    }

    @Test(dependsOnMethods = "createSubjectCategories")
    public void createSubjectCategoriesNegative() {
        name = rndProd.name().firstName();

        scKeys.put("name", name);
        scKeys.put("code", code);
        scKeys.put("active", String.valueOf(active));


        given()
                .spec(reqSpec)
                .body(scKeys)

                .when()
                .post("school-service/api/subject-categories")

                .then()
                .log().body()
                .statusCode(400)
        ;
        System.out.println("subjectId = " + subjectId);
    }

    @Test(dependsOnMethods = "createSubjectCategoriesNegative")
    public void updateSubjectCategories() {

        name = rndProd.name().fullName();
        scKeys.put("id", subjectId);
        scKeys.put("name", name);
        scKeys.put("code", code);
        scKeys.put("active", String.valueOf(active));

        given().spec(reqSpec)
                .body(scKeys)
                .when()
                .put("school-service/api/subject-categories")
                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(subjectId))
        ;

    }


    @Test(dependsOnMethods = "updateSubjectCategories")
    public void deleteSubjectCategories() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/subject-categories/" + subjectId)
                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "deleteSubjectCategories")
    public void deleteSubjectCategoriesNegative() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/subject-categories/" + subjectId)
                .then()
                .log().body()
                .statusCode(400)
        ;
    }
}
