import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import pojo.GetCourse.Api;
import pojo.GetCourse.GetCourse;
import pojo.GetCourse.WebAutomation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static io.restassured.RestAssured.*;

public class OathTest {

    public static void main(String[] args) {

        //GetCodeUrl = "https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php";
        String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AX4XfWj2iJKmfe-JgzsBl5tsp4cAN1EmjgwjG-h0fmlRr_fxuQTaB_yqMf38NWxGAzuHAQ&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=consent";
        String partialCode = url.split("code=")[1];
        String code = partialCode.split("&scope")[0];

        String accessTokenResponse = given().urlEncodingEnabled(false)
                .queryParams("code", code)
                .queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
                .queryParams("grant_type","authorization_code")
                .when().log().all()
                .post("https://www.googleapis.com/oauth2/v4/token").asString();

        JsonPath js = new JsonPath(accessTokenResponse);

        String accessToken = js.getString("access_token");

        GetCourse getCourse = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
                .when()
                .get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);

        System.out.println(getCourse.getInstructor());
        System.out.println(getCourse.getLinkedIn());
        System.out.println(getCourse.getCourses().getApi().get(1).getCourseTitle());

        List<Api> apiCourses = getCourse.getCourses().getApi();

        for (Api api : apiCourses) {
            if(api.getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
                System.out.println(api.getPrice());
        }

        //store courses from webAutomations in one List type variable
        ArrayList<String> webAutomationCourses = new ArrayList<String>();

        List<WebAutomation> webAutomations = getCourse.getCourses().getWebAutomation();
        for (WebAutomation webAutomation : webAutomations) {
            webAutomationCourses.add(webAutomation.getCourseTitle());
        }

        //Creating a simple array with new courses and then coverting array into ArrayList in order to add to List of courses
        String[] courseTitles = {"Selenium Webdriver Java", "Cypress", "Protractor"};
        List<String> expectedList = Arrays.asList(courseTitles);

        Assert.assertTrue(webAutomationCourses.equals(expectedList));

    }
}
