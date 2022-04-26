import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;

public class OathTest {

    public static void main(String[] args) throws InterruptedException {

        String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AX4XfWhrJJdycox6VCfFEUwb-VsdSnCoQI6udhoNibDLJ2RUa3R-zUKds47W8rVAnt_bSQ&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=consent";
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

        String response = given().queryParam("access_token", accessToken)
                .when().log().all()
                .get("https://rahulshettyacademy.com/getCourse.php").asString();
    }
}
