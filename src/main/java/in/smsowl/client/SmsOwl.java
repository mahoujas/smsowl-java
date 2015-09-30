package in.smsowl.client;


import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class SmsOwl {

    private final String accountId;
    private final String apiKey;
    private final String url = "https://api.smsowl.in/v1/sms";

    public SmsOwl(String accountId,String apiKey){
        this.accountId = accountId;
        this.apiKey = apiKey;
    }


    public String sendPromotionalSms(String senderId,String to,String message) throws Exception{
        return sendPromotionalSms(senderId, to, message, SmsType.NORMAL);
    }

    public String[] sendPromotionalSms(String senderId,String [] to,String message) throws Exception{
        return sendPromotionalSms(senderId, to, message, SmsType.NORMAL);
    }

    @SuppressWarnings( "deprecation" )
    public String sendPromotionalSms(String senderId,String to,String message,SmsType smsType) throws Exception{
        HttpClient httpClient = HttpClientBuilder.create().build();
        try{
            Gson gson= new Gson();
            HttpPost post = new HttpPost(url);
            String smsTypeString = smsType == SmsType.NORMAL ? "normal" : "flash";
            SinglePromoDto dto = new SinglePromoDto(accountId, apiKey, "promotional", smsTypeString, senderId, to, message);
            StringEntity  postingString =new StringEntity(gson.toJson(dto));//convert your pojo to   json
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(post);
            int status = response.getStatusLine().getStatusCode();
            Reader reader = new InputStreamReader(response.getEntity().getContent());
            if(status == 200){                
                SuccessResponse respObj = gson.fromJson(reader,SuccessResponse.class);
                return respObj.getSmsId();
            }else{
                ErrorResponse respObj = gson.fromJson(reader,ErrorResponse.class);
                throw new Exception(respObj.getMessage());
            }
        }catch (Exception ex) {
            throw ex;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    @SuppressWarnings( "deprecation" )
    public String [] sendPromotionalSms(String senderId,String [] to,String message,SmsType smsType) throws Exception{
        HttpClient httpClient = HttpClientBuilder.create().build();
        try{
            Gson gson= new Gson();
            HttpPost post = new HttpPost(url);
            String smsTypeString = smsType == SmsType.NORMAL ? "normal" : "flash";
            MultiPromoDto dto = new MultiPromoDto(accountId, apiKey, "promotional", smsTypeString, senderId, to, message);
            StringEntity  postingString =new StringEntity(gson.toJson(dto));//convert your pojo to   json
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(post);
            int status = response.getStatusLine().getStatusCode();
            Reader reader = new InputStreamReader(response.getEntity().getContent());
            if(status == 200){                
                SuccessResponseMulti respObj = gson.fromJson(reader,SuccessResponseMulti.class);
                return respObj.getSmsIds();
            }else{
                ErrorResponse respObj = gson.fromJson(reader,ErrorResponse.class);
                throw new Exception(respObj.getMessage());
            }
        }catch (Exception ex) {
            throw ex;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    @SuppressWarnings( "deprecation" )
    public String sendTransactionalSms(String senderId,String to,String templateId,HashMap<String, String> placeholderMap) throws Exception{
        HttpClient httpClient = HttpClientBuilder.create().build();
        try{
            Gson gson= new Gson();
            HttpPost post = new HttpPost(url);
            SingleTransDto dto = new SingleTransDto(accountId, apiKey, "transactional", "normal",senderId,to, templateId, placeholderMap);
            StringEntity  postingString =new StringEntity(gson.toJson(dto));
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(post);
            int status = response.getStatusLine().getStatusCode();
            Reader reader = new InputStreamReader(response.getEntity().getContent());
            if(status == 200){                
                SuccessResponse respObj = gson.fromJson(reader,SuccessResponse.class);
                return respObj.getSmsId();
            }else{
                ErrorResponse respObj = gson.fromJson(reader,ErrorResponse.class);
                throw new Exception(respObj.getMessage());
            }
        }catch (Exception ex) {
            throw ex;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }


    private class DtoCommon{

        public DtoCommon(String accountId, String apiKey, String dndType, String smsType, String senderId) {
            this.accountId = accountId;
            this.apiKey = apiKey;
            this.dndType = dndType;
            this.smsType = smsType;
            this.senderId = senderId;
        }


        private String accountId;
        private String apiKey;
        private String dndType;
        private String smsType;
        private String senderId;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getDndType() {
            return dndType;
        }

        public void setDndType(String dndType) {
            this.dndType = dndType;
        }

        public String getSmsType() {
            return smsType;
        }

        public void setSmsType(String smsType) {
            this.smsType = smsType;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }


    }

    private class SinglePromoDto extends DtoCommon{

        public SinglePromoDto(String accountId, String apiKey, String dndType, String smsType, String senderId,String to, String message) {
            super(accountId, apiKey, dndType, smsType, senderId);
            this.to = to;
            this.message = message;
        }


        private String to;
        private String message;

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }


    }

    private class MultiPromoDto extends DtoCommon{

        public MultiPromoDto(String accountId, String apiKey, String dndType, String smsType, String senderId,String[] to, String message) {
            super(accountId, apiKey, dndType, smsType, senderId);
            this.to = to;
            this.message = message;
        }



        private String [] to;
        private String message;

        public String[] getTo() {
            return to;
        }

        public void setTo(String[] to) {
            this.to = to;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }


    }

    private class SingleTransDto extends DtoCommon{

        public SingleTransDto(String accountId, String apiKey, String dndType, String smsType, String senderId,String to,String templateId, HashMap<String, String> placeholders) {
            super(accountId, apiKey, dndType, smsType, senderId);
            this.to = to;
            this.templateId = templateId;
            this.placeholders = placeholders;
        }



        private String to;
        private String templateId;
        private HashMap<String,String> placeholders;

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getTemplateId() {
            return templateId;
        }

        public void setTemplateId(String templateId) {
            this.templateId = templateId;
        }

        public HashMap<String, String> getPlaceholders() {
            return placeholders;
        }

        public void setPlaceholders(HashMap<String, String> placeholders) {
            this.placeholders = placeholders;
        }


    }
    
    private class ResponseCommon {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
        
        
    }

    private class ErrorResponse extends ResponseCommon{
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
    
    private class SuccessResponse extends ResponseCommon{
        private String smsId;

        public String getSmsId() {
            return smsId;
        }

        public void setSmsId(String smsId) {
            this.smsId = smsId;
        }
    }
    
    private class SuccessResponseMulti extends ResponseCommon{
        private String [] smsIds;

        public String[] getSmsIds() {
            return smsIds;
        }

        public void setSmsIds(String[] smsIds) {
            this.smsIds = smsIds;
        }
    }

}