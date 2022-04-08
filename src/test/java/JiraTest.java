import files.payload;
import io.restassured.filter.session.SessionFilter;

import java.io.File;

import static io.restassured.RestAssured.*;

public class JiraTest {

    public static void main(String[] args) {
        baseURI = "http://localhost:8080";

        //Create a variable which will store the sessionid from the auth method
        SessionFilter session = new SessionFilter();

        //Cookie session based Authentication
        given().header("Content-Type", "application/json")
                .body(payload.JiraAuthentication("ronicapela", "jira123")).log().all().filter(session)
        .when().post("/rest/auth/1/session").then().extract().response().asString();

        //Add comment
        //passing session id captured on authentication through filter
        given().pathParam("key", "10101").log().all()
                .header("Content-Type", "application/json")
                .body(payload.AddComment()).filter(session)
        .when().post("rest/api/2/issue/{key}/comment").then()
                .assertThat().statusCode(201);

        //Add attachment
        given().header("Atlassisn-Token", "no-check").filter(session)
                .pathParam("key", "10101")
                .header("Content-Type", "multipart/form-data")
                .multiPart("file", new File("attachments/testFile_JiraApiAddAttachment.txt"))
                .when().post(" rest/api/2/issue/{issueIdOrKey}/attachments")
                .then().log().all().assertThat().statusCode(200);


    }
}
