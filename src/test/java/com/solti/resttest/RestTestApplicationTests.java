package com.solti.resttest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.solti.resttest.model.User;
import com.solti.resttest.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest
class RestTestApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        userRepository.saveAll(getUsers());
        System.out.println("getUser count: " + (long) getUsers().size());
        System.out.println("User count: " + userRepository.count());
        List<User> userList = userRepository.findAll();
        for (User user: userList)
        {
            System.out.println(user.getId());
        }
        Assert.assertEquals(userRepository.count(),(long) getUsers().size());
    }

    private List<User> getUsers() {
        try {
			HttpURLConnection con = getHttpURLConnection();
			BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print in String

            String jsonString = response.toString();

            // Creating a Gson Object
            Gson gson = new Gson();
            Type userListType = new TypeToken<ArrayList<User>>() {
            }.getType();
            List<User> userList = gson.fromJson(jsonString, userListType);
            return userList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	private HttpURLConnection getHttpURLConnection(){
        try {
		String url = "https://jsonplaceholder.typicode.com/users";
		URL obj = null;
            obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
        Logger logger = LoggerFactory.getLogger(RestTestApplicationTests.class);
            logger.info("responsecode");
        logger.info(String.valueOf(responseCode));
            logger.info("headerFields");
        logger.info(String.valueOf(con.getHeaderFields()));
            logger.info("URL");
        logger.info(String.valueOf(con.getURL()));
		return con;
        } catch (IOException e) {
           throw new RuntimeException(e);
       }
    }

	@Test
    public void testResponseStatusCode() {
        List<User> userList = getUsers();
        try {
            Assert.assertEquals(getHttpURLConnection().getResponseCode(), 200);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(userList);
    }

    public boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    private boolean validateEmail(String emailAddress){
        String regexPattern = "^(.+)@(\\S+)$";
        return patternMatches(emailAddress, regexPattern);
    }

    @Test
    public void validateEmailsTest(){
        // GIVEN
        List<User> userList = getUsers();
        // WHEN
        // THEN
        for (User user:userList){
            Assert.assertTrue(validateEmail(user.getEmail()));
        }
    }


}
