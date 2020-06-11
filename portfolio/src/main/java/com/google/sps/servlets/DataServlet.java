// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// Add imports
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.sps.data.Comment;
// DataStore Imports
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;



/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    ArrayList<String> messages = new ArrayList<String>();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Query DataStore for all comments
        Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);

        List<Comment> comments = new ArrayList<>();

        for (Entity entity : results.asIterable()) {
            String user_name = (String) entity.getProperty("username");
            String date = (String) entity.getProperty("date");
            String comment_text = (String) entity.getProperty("text");

            comments.add(new Comment(user_name, comment_text, date));
        }
        
        Gson gson = new Gson();
        String jsonString = gson.toJson(comments);

        response.setContentType("application/json;");
        response.getWriter().println(jsonString);

    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String comment_text = getParameter(request, "text-input", "");
        String user_name = getParameter(request, "fname", "");

        response.setContentType("text/html;");
        response.getWriter().println(comment_text);
        messages.add(comment_text);

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date(System.currentTimeMillis());
        String comment_date = formatter.format(date);

        // Create comment entity
        Entity commentEntity = new Entity("Comment");

        // Enter properties into comment entity
        commentEntity.setProperty("text", comment_text);
        commentEntity.setProperty("date", comment_date);
        commentEntity.setProperty("timestamp", date);
        commentEntity.setProperty("username", user_name);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(commentEntity);

        // Redirect back to home-page
        response.sendRedirect("/index.html");
    }


    private String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }


}
