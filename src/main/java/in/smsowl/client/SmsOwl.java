/**
 * The MIT License (MIT)
 *
 * Copyright 2015 Mahoujas Technologies (OPC) Private Limited.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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

/**
 *
 * @author Mahoujas Technologies
 */
public class SmsOwl {

    private final String accountId;
    private final String apiKey;
    private final String url = "https://api.smsowl.in/v1/sms";

    /**
     * Constructor to construct client object.
     *
     * @param accountId Your account id as in portal
     * @param apiKey Your API key as in portal
     */
    public SmsOwl(String accountId, String apiKey) {
        this.accountId = accountId;
        this.apiKey = apiKey;
    }

    /**
     * Will send promotional SMS and returns the smsId.
     *
     * @param senderId Approved sender id
     * @param to Recipient phone number with country code
     * @param message A message to be sent
     * @return Unique smsId for this message
     * @throws Exception
     */
    public String sendPromotionalSms(String senderId, String to, String message) throws Exception {
        return sendPromotionalSms(senderId, to, message, SmsType.NORMAL);
    }

    /**
     * Will send promotional SMS to multiple recipient and returns array of
     * unique smsIds.
     *
     * @param senderId Approved sender id
     * @param to Recipient phone numbers with country code in array max 1000
     * @param message A message to be sent
     * @return Array of unique smsId per recipient
     * @throws Exception
     */
    public String[] sendPromotionalSms(String senderId, String[] to, String message) throws Exception {
        return sendPromotionalSms(senderId, to, message, SmsType.NORMAL);
    }

    /**
     * Will send promotional SMS and returns the smsId.
     *
     * @param senderId Approved sender id
     * @param to Recipient phone number with country code
     * @param message A message to be sent
     * @param smsType Can be normal or flash SMS
     * @return Unique smsId for this message
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    public String sendPromotionalSms(String senderId, String to, String message, SmsType smsType) throws Exception {
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            Gson gson = new Gson();
            HttpPost post = new HttpPost(url);
            String smsTypeString = smsType == SmsType.NORMAL ? "normal" : "flash";
            SinglePromoDto dto = new SinglePromoDto(accountId, apiKey, "promotional", smsTypeString, senderId, to, message);
            StringEntity postingString = new StringEntity(gson.toJson(dto));//convert your pojo to   json
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(post);
            int status = response.getStatusLine().getStatusCode();
            Reader reader = new InputStreamReader(response.getEntity().getContent());
            if (status == 200) {
                SuccessResponse respObj = gson.fromJson(reader, SuccessResponse.class);
                return respObj.getSmsId();
            } else {
                ErrorResponse respObj = gson.fromJson(reader, ErrorResponse.class);
                throw new Exception(respObj.getMessage());
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    /**
     * Will send promotional SMS to multiple recipient and returns array of
     * unique smsIds.
     *
     * @param senderId Approved sender id
     * @param to Recipient phone numbers with country code in array max 1000
     * @param message A message to be sent
     * @param smsType Can be normal or flash SMS
     * @return Array of unique smsId per recipient
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    public String[] sendPromotionalSms(String senderId, String[] to, String message, SmsType smsType) throws Exception {
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            Gson gson = new Gson();
            HttpPost post = new HttpPost(url);
            String smsTypeString = smsType == SmsType.NORMAL ? "normal" : "flash";
            MultiPromoDto dto = new MultiPromoDto(accountId, apiKey, "promotional", smsTypeString, senderId, to, message);
            StringEntity postingString = new StringEntity(gson.toJson(dto));//convert your pojo to   json
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(post);
            int status = response.getStatusLine().getStatusCode();
            Reader reader = new InputStreamReader(response.getEntity().getContent());
            if (status == 200) {
                SuccessResponseMulti respObj = gson.fromJson(reader, SuccessResponseMulti.class);
                return respObj.getSmsIds();
            } else {
                ErrorResponse respObj = gson.fromJson(reader, ErrorResponse.class);
                throw new Exception(respObj.getMessage());
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    /**
     * Will send transactional SMS to a recipient.
     *
     * @param senderId Approved sender id
     * @param to Recipient phone number with country code
     * @param templateId Approved template id generated via portal
     * @param placeholderMap Map containing keys and values for placeholder
     * @return Unique smsId
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    public String sendTransactionalSms(String senderId, String to, String templateId, HashMap<String, String> placeholderMap) throws Exception {
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            Gson gson = new Gson();
            HttpPost post = new HttpPost(url);
            SingleTransDto dto = new SingleTransDto(accountId, apiKey, "transactional", "normal", senderId, to, templateId, placeholderMap);
            StringEntity postingString = new StringEntity(gson.toJson(dto));
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(post);
            int status = response.getStatusLine().getStatusCode();
            Reader reader = new InputStreamReader(response.getEntity().getContent());
            if (status == 200) {
                SuccessResponse respObj = gson.fromJson(reader, SuccessResponse.class);
                return respObj.getSmsId();
            } else {
                ErrorResponse respObj = gson.fromJson(reader, ErrorResponse.class);
                throw new Exception(respObj.getMessage());
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    private class DtoCommon {

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

    private class SinglePromoDto extends DtoCommon {

        public SinglePromoDto(String accountId, String apiKey, String dndType, String smsType, String senderId, String to, String message) {
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

    private class MultiPromoDto extends DtoCommon {

        public MultiPromoDto(String accountId, String apiKey, String dndType, String smsType, String senderId, String[] to, String message) {
            super(accountId, apiKey, dndType, smsType, senderId);
            this.to = to;
            this.message = message;
        }

        private String[] to;
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

    private class SingleTransDto extends DtoCommon {

        public SingleTransDto(String accountId, String apiKey, String dndType, String smsType, String senderId, String to, String templateId, HashMap<String, String> placeholders) {
            super(accountId, apiKey, dndType, smsType, senderId);
            this.to = to;
            this.templateId = templateId;
            this.placeholders = placeholders;
        }

        private String to;
        private String templateId;
        private HashMap<String, String> placeholders;

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

    private class ErrorResponse extends ResponseCommon {

        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    private class SuccessResponse extends ResponseCommon {

        private String smsId;

        public String getSmsId() {
            return smsId;
        }

        public void setSmsId(String smsId) {
            this.smsId = smsId;
        }
    }

    private class SuccessResponseMulti extends ResponseCommon {

        private String[] smsIds;

        public String[] getSmsIds() {
            return smsIds;
        }

        public void setSmsIds(String[] smsIds) {
            this.smsIds = smsIds;
        }
    }

}
