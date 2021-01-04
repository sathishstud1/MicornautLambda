package com.example;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.example.dbconnect.DBUtils;
import com.example.dbconnect.DynamoDBConnect;
import com.example.exceptions.BadInputException;
import com.example.models.Event;
import com.example.models.EventSaved;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import io.micronaut.http.annotation.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EventController {
    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Inject
    private EventService eventService;

    @Inject
    private DBUtils dbUtils;

    @Post("/events")
    public EventSaved save(@Valid @Body Event event) {
        return new EventSaved(eventService.save(event));
    }

    @Post("/save-app-details")
    public Map<String, Object> saveAppData(@Body String data)
    {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("status", true);
        try{
            int newId = dbUtils.getNewId("newid_counter","custid");
            System.out.println(newId);
            System.out.println(data);
            Table table = DynamoDBConnect.getDBConnection().getTable("EventTable");
            PutItemOutcome outcome = table
                    .putItem(new Item().withPrimaryKey("id", newId,"title", "title").withJSON("info", data));
            response.put("message", "Details saved with App Id: "+newId );
        }catch(Exception e){
            response.put("status", false);
            response.put("message", e.getMessage());
        }
        return response;

    }
    @Post("/getJson")
    public Map<String, String> getJson(@Body String data)
    {
        Map<String, String> response = new HashMap<String, String>();
        response.put("status", "true");
        JSONObject postData = new JSONObject(data);
        int id = Integer.parseInt(postData.get("appId").toString());
        System.out.println(id);
        try{
            Table table = DynamoDBConnect.getDBConnection().getTable("EventTable");
            GetItemSpec spec = new GetItemSpec().withPrimaryKey("id", id, "title", "title");
            Item outcome = table.getItem(spec);
            response.put("message", outcome.toString());
        }catch(Exception e){
            response.put("status", "false");
            response.put("message", e.getMessage());
        }
        return response;

    }
    @Get("/getByAppID/{id}")
    public Map<String, String> getEvent(@QueryValue String id)
    {
        Map<String, String> response = new HashMap<String, String>();
        response.put("status", "true");
        try{
            Table table = DynamoDBConnect.getDBConnection().getTable("EventTable");
            GetItemSpec spec = new GetItemSpec().withPrimaryKey("id", Integer.parseInt(id), "title", "title");
            Item outcome = table.getItem(spec);
            response.put("message", outcome.toString());
        }catch(Exception e){
            response.put("status", "false");
            response.put("message", e.getMessage());
        }
        return response;

    }

    @Get("/events")
    public List<EventSaved> get() {
        return eventService.getAll();
    }

    @Get("/events/{eventId}")
    public Event get(@PathVariable String eventId) {
        logger.info("GET eventId: {}", eventId);
        Event event = eventService.get(eventId);
        if (event == null) {
            throw new BadInputException(String.format("eventId not found: %s", eventId));
        }
        return event;
    }

    @Put("/events/{eventId}")
    public Event update(@PathVariable String eventId, @Valid @Body Event event) {
        logger.info("PUT eventId: {}", eventId);
        return eventService.update(eventId, event);
    }
}
