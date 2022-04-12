import files.payload;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.io.File;

import static io.restassured.RestAssured.*;

public class JiraTest {

    public static void main(String[] args) {
        baseURI = "http://localhost:8080";

        //Create a variable which will store the sessionid from the auth method
        SessionFilter session = new SessionFilter();

        //Login scenario with cookie session based Authentication
        //relaxedHTTPSValidation() for http certification
        given().relaxedHTTPSValidation().header("Content-Type", "application/json")
                .body(payload.JiraAuthentication("ronicapela", "jira123")).log().all().filter(session)
        .when().post("/rest/auth/1/session").then().extract().response().asString();

        //Add comment
        //passing session id captured on authentication through filter
        String comment = "This is my comment";
        String addCommentResponse = given().pathParam("key", "10101").log().all()
                .header("Content-Type", "application/json")
                .body(payload.AddComment(comment)).filter(session)
        .when().post("rest/api/2/issue/{key}/comment").then()
                .assertThat().statusCode(201).extract().response().asString();

        JsonPath js = new JsonPath(addCommentResponse);
        String commentId = js.getString("id");

        //Add attachment
        given().header("Atlassisn-Token", "no-check").filter(session)
                .pathParam("key", "10101")
                .header("Content-Type", "multipart/form-data")
                .multiPart("file", new File("attachments/testFile_JiraApiAddAttachment.txt"))
                .when().post(" rest/api/2/issue/{issueIdOrKey}/attachments")
                .then().log().all().assertThat().statusCode(200);

        //Get issue
        String issueDetails = given().filter(session)
                .pathParam("key", "10101")
                .queryParam("fields", "comment")
                .when().get("rest/api/2/issue/{key}")
                .then().log().all().extract().response().asString();

        js = new JsonPath(issueDetails);
        int commentsCount = js.getInt("fields.comment.comments.size()");

        for(int i = 0; i < commentsCount; i++ )
        {
            String commentIdIssue = js.get("fields.comment.comments[" + i + "].id").toString();
            if(commentIdIssue.equalsIgnoreCase(commentId))
            {
                String message = js.get("fields.comment.comments[" + i + "].body").toString();
                Assert.assertEquals(message, comment);
            }
        }
    }
}
