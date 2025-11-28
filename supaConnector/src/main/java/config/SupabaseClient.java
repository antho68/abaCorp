package config;

import okhttp3.*;

import java.io.IOException;

public class SupabaseClient
{
    private final OkHttpClient client;
    private final String baseUrl;
    private final String apiKey;

    public SupabaseClient(String baseUrl, String apiKey)
    {
        this.client = new OkHttpClient();
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    private Request.Builder addHeaders(Request.Builder builder)
    {
        return builder
                .addHeader("apikey", apiKey)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "return=representation");
    }

    public String get(String endpoint) throws IOException
    {
        Request request = addHeaders(new Request.Builder().url(baseUrl + endpoint)).get().build();
        try (Response response = client.newCall(request).execute())
        {
            if (!response.isSuccessful())
            {
                throw new IOException("GET failed: " + response);
            }
            return response.body().string();
        }
    }

    public String post(String endpoint, String jsonBody) throws IOException
    {
        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json"));
        Request request = addHeaders(new Request.Builder().url(baseUrl + endpoint)).post(body).build();

        try (Response response = client.newCall(request).execute()) {
            String respBody = response.body() != null ? response.body().string() : "";

            if (!response.isSuccessful()) {
                System.err.println("Request JSON: " + jsonBody);
                System.err.println("Response: " + respBody);
                throw new IOException("POST failed [" + response.code() + "]: " + respBody);
            }

            return respBody;
        }
    }

    public String patch(String endpoint, String jsonBody) throws IOException
    {
        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json"));
        Request request = addHeaders(new Request.Builder().url(baseUrl + endpoint)).patch(body).build();
        try (Response response = client.newCall(request).execute())
        {
            if (!response.isSuccessful())
            {
                throw new IOException("PATCH failed: " + response);
            }
            return response.body().string();
        }
    }

    public String delete(String endpoint) throws IOException
    {
        Request request = addHeaders(new Request.Builder().url(baseUrl + endpoint)).delete().build();
        try (Response response = client.newCall(request).execute())
        {
            if (!response.isSuccessful())
            {
                throw new IOException("DELETE failed: " + response);
            }
            return response.body().string();
        }
    }
}