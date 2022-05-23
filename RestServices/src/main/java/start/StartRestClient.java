package start;

import rest.client.CharityCasesClient;
import app.model.CharityCase;
import org.springframework.web.client.RestClientException;
import app.service.rest.ServiceException;

public class StartRestClient {
    private final static CharityCasesClient charityCasesClient = new CharityCasesClient();

    public static void main(String[] args) {
        CharityCase charityCaseTest = new CharityCase("testChCase1", 99);
        try {
            show(() -> System.out.println(charityCasesClient.create(charityCaseTest)));
            show(() -> {
                CharityCase[] result = charityCasesClient.getAll();
                for (CharityCase charityCase : result) {
                    System.out.println(charityCase.getID() + ": " + charityCase.getName()+ "; " + charityCase.getSum());
                }
            });
        } catch (RestClientException ex) {
            System.out.println("Exception ... " + ex.getMessage());
        }
        show(() -> System.out.println(charityCasesClient.getById(3)));
    }

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            System.out.println("Service exception" + e);
        }
    }
}
