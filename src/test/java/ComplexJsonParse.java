import files.payload;
import io.restassured.path.json.JsonPath;
import org.apache.http.io.SessionOutputBuffer;

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
            String courseTitles = js.get("courses["+ i +"].title");
            String coursePrices = js.get("courses["+ i +"].price");

            System.out.println(courseTitles);
            System.out.println(coursePrices);
        }

        //Print number of copies once course title equal to RPA
        for(int i = 0; i < count; i++)
        {
            String courseTitles = js.get("courses["+ i +"].title");

            if(courseTitles.equalsIgnoreCase("RPA"))
            {
                int copies = js.get("courses["+ i +"].copies");
                System.out.println(copies);
                break;
            }


        }

    }
}
