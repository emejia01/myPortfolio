package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/authenticate")
public class AuthenticateServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String json;
        UserService userService = UserServiceFactory.getUserService();
        
        // Check if user is logged in successfully
        if (userService.isUserLoggedIn()) {
            String user_email = userService.getCurrentUser().getEmail();
            json = convertToJson(true, user_email);
        }
        else {
            json = convertToJson(false, "N/A");
        }

        response.setContentType("application/json;");
        response.getWriter().println(json);
    }

    // Temp method to return Json. Will change later on to use GSON dependency instead for custom class
    private String convertToJson(boolean isLoggedIn, String email) {
        String json = "{";
        json += "\"LoggedIn\":";
        json += ";";
        json += "\"email\": ";
        json += "\"" + email + "\"";

        return json;
    }
}