package rest.client;

import app.model.CharityCase;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import app.service.rest.ServiceException;

import java.util.concurrent.Callable;

public class CharityCasesClient {
    public static final String URL = "http://localhost:8080/app/charityCases";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public CharityCase[] getAll() {
        return execute(() -> restTemplate.getForObject(URL, CharityCase[].class));
    }

    public CharityCase getById(Integer id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), CharityCase.class));
    }

    public CharityCase create(CharityCase charityCase) {
        return execute(() -> restTemplate.postForObject(URL, charityCase, CharityCase.class));
    }

    public void update(CharityCase charityCase) {
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, charityCase.getID()), charityCase);
            return null;
        });
    }

    public void delete(Integer id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }
}
