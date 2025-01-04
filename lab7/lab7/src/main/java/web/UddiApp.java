package web;

import org.apache.juddi.v3.client.config.UDDIClient;
import org.apache.juddi.v3.client.transport.Transport;
import org.uddi.api_v3.*;
import org.uddi.v3_service.UDDISecurityPortType;
import org.uddi.v3_service.UDDIInquiryPortType;
import org.uddi.v3_service.UDDIPublicationPortType;

import java.util.Scanner;

public class UddiApp {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Выберите действие:");
            System.out.println("1. Зарегистрировать сервис");
            System.out.println("2. Найти и обратиться к сервису");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Регистрация сервиса в UDDI...");
                    registerService();
                    break;
                case 2:
                    System.out.println("Поиск сервиса в UDDI...");
                    searchAndInvokeService();
                    break;
                default:
                    System.out.println("Неверный выбор.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void registerService() throws Exception {
        UDDIClient uddiClient = new UDDIClient("juddi-client.xml");
        Transport transport = uddiClient.getTransport("default");

        UDDISecurityPortType security = transport.getUDDISecurityService();
        UDDIPublicationPortType publish = transport.getUDDIPublishService();

        // Аутентификация
        GetAuthToken authRequest = new GetAuthToken();
        authRequest.setUserID("admin");
        authRequest.setCred("admin");
        AuthToken authToken = security.getAuthToken(authRequest);
        System.out.println("Аутентификация успешна. Token: " + authToken.getAuthInfo());

        // Регистрация бизнеса
        BusinessEntity business = new BusinessEntity();
        // Удаляем явное указание ключа
        business.getName().add(new Name("My Business", null));

        SaveBusiness saveBusiness = new SaveBusiness();
        saveBusiness.setAuthInfo(authToken.getAuthInfo());
        saveBusiness.getBusinessEntity().add(business);
        BusinessDetail businessDetail = publish.saveBusiness(saveBusiness);
        String businessKey = businessDetail.getBusinessEntity().get(0).getBusinessKey();
        System.out.println("Бизнес зарегистрирован с ключом: " + businessKey);

        // Регистрация сервиса
        BusinessService service = new BusinessService();
        service.setBusinessKey(businessKey);
        // Не указываем ключ сервиса
        service.getName().add(new Name("My Service", null));

        BindingTemplate bindingTemplate = new BindingTemplate();
        bindingTemplate.setAccessPoint(new AccessPoint("http://localhost:8080/myService?wsdl", "wsdl"));
        BindingTemplates bindingTemplates = new BindingTemplates();
        bindingTemplates.getBindingTemplate().add(bindingTemplate);
        service.setBindingTemplates(bindingTemplates);

        SaveService saveService = new SaveService();
        saveService.setAuthInfo(authToken.getAuthInfo());
        saveService.getBusinessService().add(service);
        ServiceDetail serviceDetail = publish.saveService(saveService);
        System.out.println("Сервис зарегистрирован с ключом: " + serviceDetail.getBusinessService().get(0).getServiceKey());
    }

    private static void searchAndInvokeService() throws Exception {
        UDDIClient uddiClient = new UDDIClient("juddi-client.xml");
        Transport transport = uddiClient.getTransport("default");

        UDDISecurityPortType security = transport.getUDDISecurityService();
        UDDIInquiryPortType inquiry = transport.getUDDIInquiryService();

        // Получение токена аутентификации
        GetAuthToken authRequest = new GetAuthToken();
        authRequest.setUserID("admin");
        authRequest.setCred("admin");
        AuthToken authToken = security.getAuthToken(authRequest);
        System.out.println("Аутентификация успешна. Token: " + authToken.getAuthInfo());

        // Поиск сервиса
        FindService findService = new FindService();
        findService.setAuthInfo(authToken.getAuthInfo());
        findService.getName().add(new Name("My Service", null));

        ServiceList serviceList = inquiry.findService(findService);

        if (serviceList.getServiceInfos() == null || serviceList.getServiceInfos().getServiceInfo().isEmpty()) {
            System.out.println("Сервис не найден!");
            return;
        }

        String serviceKey = serviceList.getServiceInfos().getServiceInfo().get(0).getServiceKey();
        System.out.println("Найден ключ сервиса: " + serviceKey);

        // Получение деталей сервиса
        GetServiceDetail getServiceDetail = new GetServiceDetail();
        getServiceDetail.setAuthInfo(authToken.getAuthInfo());
        getServiceDetail.getServiceKey().add(serviceKey);

        ServiceDetail serviceDetail = inquiry.getServiceDetail(getServiceDetail);

        if (serviceDetail.getBusinessService().isEmpty()) {
            System.out.println("Привязки не найдены для сервиса!");
            return;
        }

        BindingTemplates bindingTemplates = serviceDetail.getBusinessService().get(0).getBindingTemplates();
        if (bindingTemplates == null || bindingTemplates.getBindingTemplate().isEmpty()) {
            System.out.println("Привязки отсутствуют!");
            return;
        }

        BindingTemplate bindingTemplate = bindingTemplates.getBindingTemplate().get(0);
        String accessPoint = bindingTemplate.getAccessPoint().getValue();
        System.out.println("URL WSDL: " + accessPoint);
    }
}
