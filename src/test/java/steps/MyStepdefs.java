package steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.UserController;
import org.json.simple.JSONObject;
import org.testng.Assert;
import utils.Endpoints;
import utils.Jsoninput;
import utils.QAEnvProps;
import utils.TestNGListener;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class MyStepdefs {
    int userID;
    UserController user;
    JsonPath jsonpath;
    JSONObject jsonObject;
    ObjectMapper objectMapper=new ObjectMapper();
    Response response,putResponse;
    UserController responseUser,responseUser1;

 //CREATE A USER
    @Given("user details")
    public void userDetails() {

        jsonObject = (JSONObject) TestNGListener.data.get("createRequest");
    }

    @When("creating a user")
    public void creatingAUser() throws JsonProcessingException {
        user= new UserController((String) jsonObject.get("name"),
                (String)jsonObject.get("address"),
                (Long)jsonObject.get("marks"));
        response=given().body(user)
                .when().post(Endpoints.userEndpoints1)
                .then().statusCode(200).extract().response();
        responseUser=objectMapper.readValue(response.asString(),UserController.class);


    }
    @Then("user must be created")
    public void userMustBeCreated() throws JsonProcessingException{
        Assert.assertEquals(user.getName(),responseUser.getName());
        Assert.assertEquals(user.getAddress(),responseUser.getAddress());
        Assert.assertEquals(user.getMarks(),responseUser.getMarks());
    }

    //CREATE A USER WITH NO MARKS
    @When("creating a user with no marks")
    public void creatingAUserWithNoMarks() throws JsonProcessingException {
        user= new UserController((String) jsonObject.get("name"),
                (String)jsonObject.get("address"),
                0);
        response=given().body(user)
                .when().post(Endpoints.userEndpoints1)
                .then().statusCode(200).extract().response();
        responseUser=objectMapper.readValue(response.asString(),UserController.class);
        
    }

    @Then("user can be created with zero marks")
    public void userCanBeCreatedWithZeroMarks() {
        Assert.assertEquals(user.getName(),responseUser.getName());
        Assert.assertEquals(user.getAddress(),responseUser.getAddress());
    }
    //CREATE A USER WITH NO NAME
    @When("creating a user with no name")
    public void creatingAUserWithNoName() {
        user= new UserController(null,
                (String)jsonObject.get("address"),
                (Long)jsonObject.get("marks"));
        response=given().body(user)
                .when().post(Endpoints.userEndpoints1)
                .then().statusCode(400).extract().response();

    }

    @Then("Name is required error message thrown")
    public void nameIsRequiredErrorMessageThrown() {
        jsonpath = new JsonPath(response.asString());
        Assert.assertEquals(jsonpath.getString("message"),"Name is required");
    }
    //CREATE A USER WITH NO ADDRESS
    @When("creating a user with no address")
    public void creatingAUserWithNoAddress() {
        user= new UserController((String) jsonObject.get("name"),
                null,
                (Long)jsonObject.get("marks"));
        response=given().body(user)
                .when().post(Endpoints.userEndpoints1)
                .then().statusCode(400).extract().response();

    }

    @Then("Address is required error message thrown")
    public void addressIsRequiredErrorMessageThrown() {
        jsonpath = new JsonPath(response.asString());
        Assert.assertEquals(jsonpath.getString("message"),"Address is required");
    }


// USER NAME UPDATE
   @When("updating a user name")
    public void updatingAUser() throws JsonProcessingException {
       jsonObject= (JSONObject) TestNGListener.data.get("createRequest");
       user = new UserController((String) jsonObject.get("name"),
               (String) jsonObject.get("address"),
               (Long) jsonObject.get("marks"));

       response = given()
               .body(user)
               .when().post(Endpoints.userEndpoints1)
               .then()
               .statusCode(200).extract().response();
       objectMapper = new ObjectMapper();
       responseUser = objectMapper.readValue(response.asString(), UserController.class);

       jsonpath = new JsonPath(response.asString());


    }

    @Then("user name is updated")
    public void userIsUpdated() throws JsonProcessingException {
        jsonObject= (JSONObject) TestNGListener.data.get("updateRequest");
        user =  new UserController(jsonpath.getInt("id"),(String) jsonObject.get("name"),
                (String) jsonObject.get("address"),
                (Long) jsonObject.get("marks"));

        Response putresponse = given()
                .body(user)
                .when().put(Endpoints.userEndpoints2)
                .then()
                .statusCode(200).extract().response();
        jsonpath = new JsonPath(putresponse.asString());
        objectMapper = new ObjectMapper();
        responseUser = objectMapper.readValue(putresponse.asString(), UserController.class);
        Assert.assertEquals(user.getName(),responseUser.getName());
        Assert.assertEquals(user.getId(),responseUser.getId());
    }

    //DELETE THE USER

    @Then("user got deleted")
    public void userGotDeleted() {
        jsonpath = new JsonPath(response.asString());
        int userID;
        userID = jsonpath.getInt("id");
        response = given()
                .body(user)
                .when().delete(Endpoints.userEndpoints5 + "/" + userID)
                .then()
                .statusCode(200).extract().response();
    }

    // UPDATE THE USER ADDRESS
    @When("updating a user address")
    public void updatingAUserAddress() throws JsonProcessingException {
        jsonObject= (JSONObject) TestNGListener.data.get("createRequest");
        user = new UserController((String) jsonObject.get("name"),
                (String) jsonObject.get("address"),
                (Long) jsonObject.get("marks"));

        response = given()
                .body(user)
                .when().post(Endpoints.userEndpoints1)
                .then()
                .statusCode(200).extract().response();
        objectMapper = new ObjectMapper();
        responseUser = objectMapper.readValue(response.asString(), UserController.class);

        jsonpath = new JsonPath(response.asString());

    }

    @Then("user address is updated")
    public void userAddressIsUpdated() throws JsonProcessingException {
        jsonObject= (JSONObject) TestNGListener.data.get("updateRequest");
        user =  new UserController(jsonpath.getInt("id"),(String) jsonObject.get("name"),
                (String) jsonObject.get("address"),
                (Long) jsonObject.get("marks"));

        Response putresponse = given()
                .body(user)
                .when().put(Endpoints.userEndpoints2)
                .then()
                .statusCode(200).extract().response();
        jsonpath = new JsonPath(putresponse.asString());
        objectMapper = new ObjectMapper();
        responseUser = objectMapper.readValue(putresponse.asString(), UserController.class);
        Assert.assertEquals(user.getAddress(),responseUser.getAddress());
        Assert.assertEquals(user.getId(),responseUser.getId());
    }

    // UPDATE THE USER MARKS
    @When("updating a user marks")
    public void updatingAUserMarks() throws JsonProcessingException {
        jsonObject= (JSONObject) TestNGListener.data.get("createRequest");
        user = new UserController((String) jsonObject.get("name"),
                (String) jsonObject.get("address"),
                (Long) jsonObject.get("marks"));

        response = given()
                .body(user)
                .when().post(Endpoints.userEndpoints1)
                .then()
                .statusCode(200).extract().response();
        objectMapper = new ObjectMapper();
        responseUser = objectMapper.readValue(response.asString(), UserController.class);

        jsonpath = new JsonPath(response.asString());

    }

    @Then("user marks is updated")
    public void userMarksIsUpdated() throws JsonProcessingException {
        jsonObject= (JSONObject) TestNGListener.data.get("updateRequest");
        user =  new UserController(jsonpath.getInt("id"),(String) jsonObject.get("name"),
                (String) jsonObject.get("address"),
                (Long) jsonObject.get("marks"));

        Response putresponse = given()
                .body(user)
                .when().put(Endpoints.userEndpoints2)
                .then()
                .statusCode(200).extract().response();
        jsonpath = new JsonPath(putresponse.asString());
        objectMapper = new ObjectMapper();
        responseUser = objectMapper.readValue(putresponse.asString(), UserController.class);
        Assert.assertEquals(user.getMarks(),responseUser.getMarks());
        Assert.assertEquals(user.getId(),responseUser.getId());
    }

     // LIST OF USER DISPLAYED
    @Then("user list displayed")
    public void userListDisplayed() {
        given().when().get(Endpoints.userEndpoints3).then().statusCode(200).extract().response();
    }

     // PARTICULAR USER IN THE LIST IS DISPLAYED
    @Then("user with particular id must be displayed")
    public void userWithParticularIdMustBeDisplayed() {
        given().when().get(Endpoints.userEndpoints4).then().statusCode(200).extract().response();
    }

    @Then("Blank page must be displayed")
    public void blankPageMustBeDisplayed() {
        given().when().get(Endpoints.userEndpoints6).then().statusCode(200).extract().response();
    }
}
