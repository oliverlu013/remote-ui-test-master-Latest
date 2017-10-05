package net.latipay.ui.test.common;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import net.latipay.ui.test.domain.Currency;
import net.latipay.ui.test.domain.LatipayUser;

/**
 * @author jasonlu 2:36:37 PM
 */
public class DataUtils {

    private static final String DATA_URL  = "http://47.52.29.49/data/submit";
    private static final String GET_VALUE = "http://47.52.29.49/redis/key";
    private static final String GET_KEY   = "http://47.52.29.49/redis/value";

    /**
     * 获取一个全新的admin账号
     * 
     * @param email
     * @param isIndividual
     * @param currency
     * @return
     */
    public static LatipayUser getAccount(String email, boolean isIndividual, Currency currency) {
        JSONObject params = new JSONObject();
        params.put("id", 1);
        params.put("1", email);
        params.put("2", isIndividual ? "Individual" : "Company");
        params.put("3", currency.name());
        JSONObject response = HttpServiceUtils.jsonPost(DATA_URL, params);
        String[] result = response.getString("result").split("\\|");
        if (result.length == 3) {
            LatipayUser user = new LatipayUser();
            user.setEmail(result[0].trim());
            user.setUserId(result[1].trim());
            user.setApiKey(result[2].trim());
            user.setCurrency(currency.name());
            user.setIndividual(isIndividual);
            user.setPassword("1234567a");
            user.setRole("admin");
            getWallet(email, currency);
            return user;
        } else {
            throw new RuntimeException(response.getString("result"));
        }
    }

    /**
     * 获取一个全新的钱包，返回钱包ID
     * 
     * @param email
     * @param currency
     * @return
     */
    public static String getWallet(String email, Currency currency) {
        JSONObject params = new JSONObject();
        params.put("id", 2);
        params.put("1", email);
        params.put("2", currency.name());
        JSONObject response = HttpServiceUtils.jsonPost(DATA_URL, params);
        String[] result = response.getString("result").split("\\|");
        if (result.length == 2) {
            return result[0].trim();
        } else {
            throw new RuntimeException(response.getString("result"));
        }
    }

    /**
     * 获取一个全新的银行账号，返回银行账号
     * 
     * @param email
     * @param currency
     * @return
     */
    public static String getBankAccount(String email, Currency currency) {
        JSONObject params = new JSONObject();
        params.put("id", 4);
        params.put("1", email);
        params.put("2", currency.name());
        JSONObject response = HttpServiceUtils.jsonPost(DATA_URL, params);
        String[] result = response.getString("result").split("\\|");
        if (result.length == 3) {
            return result[1].trim();
        } else {
            throw new RuntimeException(response.getString("result"));
        }
    }

    /**
     * 支付订单
     * 
     * @param orderId
     * @return
     */
    public static boolean commitTransaction(String orderId) {
        JSONObject params = new JSONObject();
        params.put("id", 3);
        params.put("1", orderId);
        HttpServiceUtils.jsonPost(DATA_URL, params);
        return true;
    }

    /**
     * 获取Redis里key的值
     * 
     * @param key
     * @return
     */
    public static String getRedisValue(String key) {
        Map<String, String> params = new HashMap<>();
        params.put("key", key);
        JSONObject response = HttpServiceUtils.jsonGet(GET_VALUE, params, null);
        return response.getString("result");
    }

    /**
     * 通过keyPattern获取Redis有相同value的值
     * 
     * @param keyPattern
     * @param value
     * @return
     * @throws Exception
     */
    public static String getRedisKey(String keyPattern, String value) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("keyPattern", keyPattern);
        params.put("value", value);
        JSONObject response = HttpServiceUtils.jsonGet(GET_KEY, params, null);
        return response.getString("result");
    }

}
