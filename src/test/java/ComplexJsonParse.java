import files.payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {
    public static void main(String[] args){

        JsonPath js = new JsonPath(payload.CoursePrice());


        //get number of courses
        int count = js.getInt("courses.size()");

        //get total amount from purchase amount
        int totalAmount = js.getInt("dashboard.purchaseAmount");

        //get the title of the first course present in courses
        String firstCourseTitle = js.get("courses[0].title");

        //get all courses title present in courses
        for(int i = 0; i < count; i++)
        {
            js.get("courses["+ i +"].title");
        }

    }
}
