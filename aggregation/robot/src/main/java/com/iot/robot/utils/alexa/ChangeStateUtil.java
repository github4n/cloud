package com.iot.robot.utils.alexa;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iot.common.beans.CommonResponse;
import com.iot.util.HttpsUtil;

public class ChangeStateUtil {

    private static final String AUTH_API = "https://api.amazonalexa.com/v3/events";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    public static CommonResponse send(String token, String data) {
        CommonResponse commonResponse = null;
        try {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put(HEADER_AUTHORIZATION, "Bearer " + token);
            commonResponse = HttpsUtil.sendPostForJson(AUTH_API, data, headers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commonResponse;
    }
    
    public static void main(String[] args) {
        //String body = "{ \"content\": { \"properties\": [] }, \"event\": { \"endpoint\": { \"endpointId\": \"f845355ec6014fe4ac9e881a8c8579fd\", \"scope\": { \"token\": \"Atza|IwEBIKSYjMY5Ly3MpZseiwcb72K07ywuW3wKy6lNq2EI9AW9IlPKMUSMU6zdVFeHMX_eNkwckuXltkZTCeXuGvrK2GhpDRfUFpAFRlfhZYeoCG6hWble0cujQAjtI9fdl-OqjMROhULVQwVMtipHef1jHEYd0f1RV0xBML7BE0Owq6_FxnlkNsB3dCc2Op2JOMmYMQb2kCuT7S3xZi-_IaXtP4FH_8vhP5Lk55yvymwSMkrROIrCMWIZgXPvUmDl12TUuONGBg9XL-utNbfPrr0vpprADVJnzDi-Sp2LMPd0asDaZFrWX4-J2GEfXnYDQ30sJE9fFGeat4DAAl1NKMEN3DToVObG1kua59NKMI6pBUX8eUcwfkWtMVHMxz7G97ZnymcsSkToKhDq0qe2YBcML8qPhzyqiIvahoTErGGBc5d0RwilJViCpkATE6RKVhfjq8AmavoOGNfnDb5xMXTFe_ZqkZ5h7KK_S_QypRK5cFcK4HFj-JstKR9rELCHAq5TAn-X8PKHTF0yMzEH2E7IRQ3U\", \"type\": \"BearerToken\" } }, \"header\": { \"messageId\": \"9f50e62f-b8eb-4507-8d94-cbc3d9abab48\", \"name\": \"ChangeReport\", \"namespace\": \"Alexa\", \"payloadVersion\": \"3\" }, \"payload\": { \"change\": { \"cause\": { \"type\": \"APP_INTERACTION\" }, \"properties\": [{ \"name\": \"powerState\", \"namespace\": \"Alexa.PowerController\", \"timeOfSample\": \"2018-07-23T09:33:44Z\", \"uncertaintyInMilliseconds\": \"60000\", \"value\": \"ON\" }] } } } }";
        //send("Atza|IwEBIKSYjMY5Ly3MpZseiwcb72K07ywuW3wKy6lNq2EI9AW9IlPKMUSMU6zdVFeHMX_eNkwckuXltkZTCeXuGvrK2GhpDRfUFpAFRlfhZYeoCG6hWble0cujQAjtI9fdl-OqjMROhULVQwVMtipHef1jHEYd0f1RV0xBML7BE0Owq6_FxnlkNsB3dCc2Op2JOMmYMQb2kCuT7S3xZi-_IaXtP4FH_8vhP5Lk55yvymwSMkrROIrCMWIZgXPvUmDl12TUuONGBg9XL-utNbfPrr0vpprADVJnzDi-Sp2LMPd0asDaZFrWX4-J2GEfXnYDQ30sJE9fFGeat4DAAl1NKMEN3DToVObG1kua59NKMI6pBUX8eUcwfkWtMVHMxz7G97ZnymcsSkToKhDq0qe2YBcML8qPhzyqiIvahoTErGGBc5d0RwilJViCpkATE6RKVhfjq8AmavoOGNfnDb5xMXTFe_ZqkZ5h7KK_S_QypRK5cFcK4HFj-JstKR9rELCHAq5TAn-X8PKHTF0yMzEH2E7IRQ3U",
        //        JSONObject.parse(body).toString());

        /*String str = "{\"header\":{\"namespace\":\"System\",\"name\":\"Exception\",\"messageId\":\"093df362-c45a-437c-adf2-eb7bbc8bdf12\"},\"payload\":{\"code\":\"SKILL_DISABLED_EXCEPTION\",\"description\":\"The user does not have a valid enablement for your skill.\"}}";
        JSONObject jsonObject = JSON.parseObject(str);
        System.out.println(jsonObject);

        JSONObject payload = jsonObject.getJSONObject("payload");
        if (payload != null) {
            String code = payload.getString("code");
            String description = payload.getString("description");

            System.out.println(code);
            System.out.println(description);
        }*/
    }
}
