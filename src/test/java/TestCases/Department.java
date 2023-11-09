package TestCases;

import Utilities.Utility;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

public class Department extends Utility {


    String departmentID = "";
    private String departmentName = "";
    private String departmentCode = "";

    @Test
    public void createDepartment() {
        String rndDepartmentName = rndProd.name().firstName();
        String rndDepartmentCode = rndProd.number().digits(3);

        departmentName = rndDepartmentName;
        departmentCode = rndDepartmentCode;

        Map<String, Object> newDepartment = new HashMap<>();
        newDepartment.put("name", rndDepartmentName);
        newDepartment.put("code", rndDepartmentCode);
        newDepartment.put("active", true);
        newDepartment.put("school", "646cbb07acf2ee0d37c6d984");

        departmentID =
                given()
                        .spec(reqSpec)
                        .body(newDepartment)
                        .when()
                        .post("school-service/api/department")
                        .then()
                        .contentType(ContentType.JSON)
                        .statusCode(201)
                        .extract().path("id");
    }

    @Test(dependsOnMethods = "createDepartment")
    public void createDepartmentNegative() {
        Map<String, Object> newDepartment = new HashMap<>();
        newDepartment.put("id", departmentID);
        newDepartment.put("name", departmentName);
        newDepartment.put("code", departmentCode);
        newDepartment.put("active", true);
        newDepartment.put("school", "646cbb07acf2ee0d37c6d984");

        given()
                .spec(reqSpec)
                .body(newDepartment)
                .when()
                .post("school-service/api/department")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(400)
                .body("message", containsString("already created"));
    }

    @Test(dependsOnMethods = "createDepartmentNegative")
    public void updateDepartment() {
        Map<String, Object> updateDepartments = new HashMap<>();
        updateDepartments.put("id", departmentID);
        updateDepartments.put("name", "team3edit1002");
        updateDepartments.put("code", departmentCode);
        updateDepartments.put("active", true);
        updateDepartments.put("school", "646cbb07acf2ee0d37c6d984");

        given()
                .spec(reqSpec)
                .body(updateDepartments)
                .when()
                .put("school-service/api/department")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("name", equalTo("team3edit1002"));
    }

    @Test(dependsOnMethods = "updateDepartment")
    public void deleteDepartment() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/department/" + departmentID)
                .then()
                .statusCode(204);
    }

    @Test(dependsOnMethods = "deleteDepartment")
    public void deleteDepartmentsNegative() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/department/" + departmentID)
                .then()
                .statusCode(204)
                .log().body();
    }
}