package Campus;

import Utilities.Utility;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.*;

public class DocumentType extends Utility {
        String  documentId="";
        String name;
        List<String> stages = new ArrayList<>();


        @Test
        public void createDocumentType(){

            name = rndProd.name().fullName().toUpperCase();
            stages.add("EXAMINATION");


            Map<String, Object> keys = new HashMap<>();
            keys.put("name",name);
            keys.put("attachmentStages",stages);
            keys.put("schoolId","6390f3207a3bcb6a7ac977f9");


            documentId = given()
                    .spec(reqSpec)
                    .body(keys)

                    .when()
                    .post("/school-service/api/attachments/create")

                    .then()
                    .statusCode(201)

                    .log().body()
                    .extract().path("id")
            ;


            System.out.println(documentId);
        }
    @Test
    public void createDocumentTypeNegative(){


        Map<String, Object> keys = new HashMap<>();
        keys.put("name",name);
        keys.put("attachmentStages",stages);
        keys.put("schoolId","6390f3207a3bcb6a7ac977f9");


        documentId = given()
                .spec(reqSpec)
                .body(keys)

                .when()
                .post("/school-service/api/attachments/create")

                .then()
                .statusCode(400)

                .log().body()
                .extract().path("id")
        ;

    }
}

