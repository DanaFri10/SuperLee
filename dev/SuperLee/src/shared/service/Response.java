package shared.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import shared.LocalDateAdapter;

import java.time.LocalDate;

public class Response {
    private String data;
    private boolean error;
    private String errorMessage;
    public Response(String data, boolean error, String errorMessage)
    {
        this.data = data;
        this.error = error;
        this.errorMessage = errorMessage;
    }

    public String getData()
    {
        return data;
    }

    public boolean isError()
    {
        return error;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }
}
