package dao.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import config.SupabaseClient;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractDAODecorator<T> implements Serializable
{
    protected SupabaseClient client;
    protected ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

    public AbstractDAODecorator()
    {
    }

    public void initClient(SupabaseClient client)
    {
        this.client = client;
    }

    // Chaque DAO concret doit fournir le nom exact de la table
    protected abstract String getTableName();

    // Chaque DAO concret doit fournir la classe Java correspondante
    protected abstract Class<T> getClazz();

    // GET all
    public LinkedList<T> findAll() throws IOException
    {
        String response = client.get("/" + getTableName());

        JavaType type = mapper.getTypeFactory()
                .constructCollectionType(LinkedList.class, getClazz());

        return mapper.readValue(response, type);
    }

    // GET by column=value
    public LinkedList<T> findBy(String column, String value) throws IOException
    {
        String response = client.get("/" + getTableName() + "?" + column + "=eq." + value);

        JavaType type = mapper.getTypeFactory()
                .constructCollectionType(LinkedList.class, getClazz());

        return mapper.readValue(response, type);
    }

    public LinkedList<T> findByFilter(String filter) throws IOException
    {
        String response = client.get("/" + getTableName() + filter);

        JavaType type = mapper.getTypeFactory()
                .constructCollectionType(LinkedList.class, getClazz());

        return mapper.readValue(response, type);
    }

    // POST (insert)
    public T insert(T obj) throws IOException
    {
        String json = mapper.writeValueAsString(obj);
        String response = client.post("/" + getTableName(), json);

        JavaType listType = mapper.getTypeFactory()
                .constructCollectionType(List.class, getClazz());

        List<T> insertedList = mapper.readValue(response, listType);

        if (insertedList.isEmpty())
        {
            throw new IOException("Insert erfolgreich, aber keine Daten zurückgegeben");
        }

        return insertedList.get(0);
    }

    // PATCH by column=value
    public T update(String column, String value, T obj) throws IOException
    {
        String json = mapper.writeValueAsString(obj);
        String response = client.patch("/" + getTableName() + "?" + column + "=eq." + value, json);

        List<T> list = mapper.readValue(response,
                mapper.getTypeFactory().constructCollectionType(List.class, getClazz()));

        return list.isEmpty() ? null : list.get(0);
    }

    // DELETE by column=value
    public void delete(String column, String value) throws IOException
    {
        client.delete("/" + getTableName() + "?" + column + "=eq." + value);
    }
}
