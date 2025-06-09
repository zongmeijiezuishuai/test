package com.example.idea_test.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.idea_test.Adapter.CardAdapter;
import com.example.idea_test.Handler.DBHelper;
import com.example.idea_test.R;
import com.example.idea_test.SharedViewModel;
import com.example.idea_test.http.Token;
import com.example.idea_test.onenet.Data;
import com.example.idea_test.onenet.Datapoints;
import com.example.idea_test.onenet.Datastreams;
import com.example.idea_test.onenet.JsonRootBean;
import com.google.gson.Gson;
import okhttp3.*;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DataFragment extends Fragment  {
    private static final String TAG = "DataFragment";
    private TextView tempView, humiView, flameView, airView;
    private String Device_Name="BD-2";//设备名
    private String Pe_ID="346rKQ533s";//产品id
    private String ok_key="";
    String path="https://iot-api.heclouds.com/thingmodel/query-device-property?product_id="+Pe_ID+"&device_name="+Device_Name;//获取属性

    private Handler handler;
    boolean switchs;
    String temp_value,humi_value,flame_value,air_value,light_value,soil_value;
    private EditText temp_et,humi_et,flame_et,air_et,light_et,soil_et;

    private EditText tempThresholdEt, humiThresholdEt, flameThresholdEt, airThresholdEt, soilThresholdEt,lightThresholdEt;

    private Button no_off;
    private TextView peopleStatusText;
    private View peopleIndicator;
    private boolean peopleDetected = false;
    private static final boolean DEBUG = false; // 发布时改为false
    private ScheduledExecutorService dataSaver;

    private RecyclerView cardRecyclerView;
    private CardAdapter cardAdapter;
    private List<String> cardNumbers = new ArrayList<>(); // 存储卡号的列表

    private SharedViewModel sharedViewModel;

    private Runnable task = new Runnable() {
        public void run() {
            handler.postDelayed(this,5000);//设置循环时间
            run_data();//查询云端数据
            if(switchs){
                no_off.setText("刷新数据");
            }else{
                no_off.setText("刷新数据");
            }
            temp_et.setText(temp_value);
            humi_et.setText(humi_value);
            flame_et.setText(flame_value);
            air_et.setText(air_value);
            light_et.setText(light_value);
            soil_et.setText(soil_value);

            saveSensorDataToDB();
        }

    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);

        // 创建单线程定时器
        dataSaver = Executors.newSingleThreadScheduledExecutor();
        // 每10秒执行一次，首次延迟10秒
        dataSaver.scheduleAtFixedRate(task, 10, 10, TimeUnit.SECONDS);

        Token tk = new Token();
        try {
            ok_key = tk.key();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Token生成失败", Toast.LENGTH_SHORT).show();
        }
        // 初始化视图
        temp_et = view.findViewById(R.id.temp_value);
        humi_et= view.findViewById(R.id.humi_value);
        flame_et = view.findViewById(R.id.flame_value);
        air_et = view.findViewById(R.id.air_value);
        light_et=view.findViewById(R.id.light_value);
        soil_et=view.findViewById(R.id.soil_value);
        no_off = view.findViewById(R.id.switchs);

        tempThresholdEt = view.findViewById(R.id.temp_threshold);
        humiThresholdEt = view.findViewById(R.id.humi_threshold);
        flameThresholdEt = view.findViewById(R.id.flame_threshold);
        airThresholdEt = view.findViewById(R.id.air_threshold);
        soilThresholdEt = view.findViewById(R.id.soil_threshold);
        lightThresholdEt=view.findViewById(R.id.light_threshold);

        // 初始化人体检测视图
        peopleStatusText = view.findViewById(R.id.people_value);
        peopleIndicator = view.findViewById(R.id.people_indicator);

        Switch ledSwitch = view.findViewById(R.id.led_switch);

        // 初始化卡号列表RecyclerView
        cardRecyclerView = view.findViewById(R.id.card_list);
        cardRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cardAdapter = new CardAdapter(cardNumbers);
        cardRecyclerView.setAdapter(cardAdapter);

        ledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String command = "{\"LED\":" + (isChecked ? "true" : "false") + "}";
                sendCommandToDevice(command);
            }
        });

        // 设置按钮点击监听
        no_off.setOnClickListener(v -> {
            switchs = !switchs;
            doHttpRequest();
        });

        // Setup handler for periodic updates
        handler = new Handler();
        handler.postDelayed(task,1000);//延时
        handler.post(task);//执行

        //指令下发的代码插在这里


        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        Log.d(TAG, "ViewModel 初始化完成");


        addTestData(); //测试

        return view;
    }

    private void saveSensorDataToDB() {
        new Thread(() -> {
            try {
                // 保存温度数据
                if (temp_value != null) {
                    float temp = Float.parseFloat(temp_value.replace("°C", ""));
                    DBHelper.insertTempData(temp);
                }

                // 保存湿度数据
                if (humi_value != null) {
                    float humi = Float.parseFloat(humi_value.replace("%", ""));
                    DBHelper.insertHumiData(humi);
                }

                // 保存火焰数据
                if (flame_value != null) {
                    DBHelper.insertData("flame", Float.parseFloat(flame_value));
                }

                // 保存空气质量数据
                if (air_value != null) {
                    float air = Float.parseFloat(air_value.replace("ppm", ""));
                    DBHelper.insertData("air", air);
                }

                // 保存光照数据
                if (light_value != null) {
                    float light = Float.parseFloat(light_value.replace("lve", ""));
                    DBHelper.insertData("light", light);
                }

                // 保存土壤湿度数据
                if (soil_value != null) {
                    float soil = Float.parseFloat(soil_value.replace("%", ""));
                    DBHelper.insertData("soil", soil);
                }

                Log.d("DB", "数据保存成功: " + DBHelper.getCurrentTime());
            } catch (NumberFormatException e) {
                Log.e("DB", "数据格式转换失败", e);
            } catch (SQLException e) {
                Log.e("DB", "数据库操作失败", e);
                e.printStackTrace();  // 打印完整错误堆栈
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 关闭定时器
        if (dataSaver != null && !dataSaver.isShutdown()) {
            dataSaver.shutdown();
        }
    }


    private void debugLog(String tag, String message) {
        if (DEBUG) {
            Log.d(tag, message);
        }
    }

    private void sendCommandToDevice(String command) {
        // 构建HTTP请求，向OneNET平台发送命令
        // 这里简化了实际过程，具体实现取决于您的网络请求方式（如使用OkHttp、Retrofit等）
        // 确保替换URL、DEVICE_ID、API_KEY为您的实际值
        String url = "http://api.heclouds.com/cmds?device_id=BD-2";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), command);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("api-key", "y168z0xaivhgOJLLajCAHzJkh/am+BtX4JuEjFdYNPLGAQZsjaJLxagjN2CILawR")
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    // 成功处理响应
                }
            }

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void doHttpRequest() {
        try {
            URL url = new URL("https://iot-api.heclouds.com/thingmodel/set-device-property");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Connection","keep-Alive");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("authorization", ok_key);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);//输入输出使能
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.connect();//连接

            String json = getJsonContent();                                     //获取请求格式
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes(StandardCharsets.UTF_8));                    //写入请求格式
            os.flush();   //刷新
            os.close();     //关闭
            int responseCode = conn.getResponseCode();                              //获取请求状态
            Log.e("tag", "responseCode = " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream input = conn.getInputStream();              //获取输入流
                StringBuilder sb = new StringBuilder();
                int ss;
                while ((ss = input.read()) != -1) {
                    sb.append((char) ss);
                }
                Log.e("tag", "请求结果 = " + sb.toString());
                input.close();
            }
            conn.disconnect();
        } catch (Exception e) {
            Log.e("tag", "出现异常: " + e.toString());
            e.printStackTrace();
        }
    }

    private String getJsonContent() {
        JSONObject json = new JSONObject();
        try {
            json.put("product_id", Pe_ID);
            json.put("device_name", Device_Name);

            JSONObject params = new JSONObject();
            // 使用实际存在的标识符（根据API返回）
            params.put("LED", !switchs); // 使用LED而不是power_switch

            json.put("params", params);
            debugLog("JSON", "Request: " + json.toString());
        } catch (JSONException e) {
            Log.e("JSON", "Build error", e);
        }
        return json.toString();
    }

    public void run_data() {
        new Thread(() -> {
            HttpsURLConnection connection = null;
            try {
                URL url = new URL(path);
                connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setRequestProperty("Authorization", ok_key);

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    // 安全读取响应流
                    InputStream is = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // 调试输出
                    Log.d("Network", "Response: " + response.toString());

                    // 解析数据
                    if (isAdded()) {
                        getActivity().runOnUiThread(() -> {
                            try {
                                JSONObject json = new JSONObject(response.toString());
                                if (json.getInt("code") == 0) {
                                    parseData(json.getJSONArray("data"));
                                } else {
                                    safeToast("Error: " + json.getString("msg"));
                                }
                            } catch (JSONException e) {
                                safeToast("数据解析错误");
                            }
                        });
                    }
                } else {
                    safeToast("HTTP错误: " + responseCode);
                }
            } catch (Exception e) {
                Log.e("Network", "请求异常", e);
                safeToast("网络连接失败");
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    private void parseData(JSONArray data) throws JSONException {
        boolean dataUpdated = false;
        for (int i = 0; i < data.length(); i++) {
            JSONObject item = data.getJSONObject(i);
            String identifier = item.getString("identifier");
            Log.d("Identifier", identifier);
            switch (identifier) {
                case "temp":
                    temp_value = item.getString("value");
                    break;
                case "humi":
                    humi_value = item.getString("value");
                    break;
                case "fire":
                    flame_value = item.getString("value");
                    break;
                case "mq135":
                   air_value = item.getString("value");
                    break;
                case "light":
                    light_value=item.getString("value");
                    break;
                case "Soil":
                    soil_value=item.getString("value");
                    break;
                case "RFID":
                    String rfidValue = item.getString("value");
                    if (!cardNumbers.contains(rfidValue)) {
                        cardNumbers.add(rfidValue);
                        sharedViewModel.addCardNumber(rfidValue);
                        dataUpdated = true;
                    }
                    break;

            }
        }
        updateUI(); // 更新界面
    }

    private void parseResponse(String response) throws JSONException {
        JSONObject json = new JSONObject(response);
        if (json.getInt("code") != 0) {
            safeToast("服务器错误: " + json.getString("msg"));
            return;
        }

        JSONArray data = json.optJSONArray("data");
        if (data != null) {
            for (int i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                String identifier = item.getString("identifier");
                String value = item.optString("value", "N/A");

                // 根据实际物模型标识符修改
                switch (identifier) {
                    case "temperature":
                        temp_value = value;
                        break;
                    case "humidity":
                        humi_value = value;
                        break;
                    case "fire":
                        flame_value = value;
                        break;
                    case "air":
                        air_value = value;
                        break;
                    case "light":
                        light_value=value;
                        break;
                    case "Soil":
                        soil_value=value;
                        break;
                    case "RFID":
                        String rfidValue = item.getString("value");
                        cardNumbers.add(rfidValue);
                        break;
                    default:
                        break;
                }
            }
            updateUI();
        }
    }

    private void safeToast(final String message) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() ->
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show());
        }
    }
    private void updateUI() {
        if (!isAdded() || getActivity() == null) return;

        getActivity().runOnUiThread(() -> {
            try {
                // 设置默认值"N/A"防止空指针
                temp_et.setText(temp_value != null ? temp_value : "N/A");
                humi_et.setText(humi_value != null ? humi_value : "N/A");
                flame_et.setText(flame_value != null ? flame_value : "N/A");
                air_et.setText(air_value != null ? air_value : "N/A");
                light_et.setText(light_value!= null ? light_value : "N/A");
                soil_et.setText(soil_value!= null ? soil_value : "N/A");

                // 专门更新RecyclerView
                if (cardAdapter != null) {
                    cardAdapter.notifyDataSetChanged();
                    Log.d("RecyclerView", "适配器已刷新，数据量: " + cardNumbers.size());
                }

                // 调试输出
                debugLog("UI", String.format("更新数据: 温度=%s, 湿度=%s, 火焰=%s, 空气质量=%s,光照值=%s,土壤湿度=%s",
                        temp_value, humi_value, flame_value, air_value,light_value,soil_value));
            } catch (Exception e) {
                Log.e("UI", "更新失败", e);
            }
        });

        checkThresholds();

    }


    private void checkThresholds() {
        try {
            float temperature = Float.parseFloat(temp_value);
            float humidity = Float.parseFloat(humi_value);
            float fire = Float.parseFloat(flame_value);
            float mq = Float.parseFloat(air_value);
            float soil = Float.parseFloat(soil_value);
            float light=Float.parseFloat(light_value);

            float tempThreshold = Float.parseFloat(tempThresholdEt.getText().toString());
            float humiThreshold = Float.parseFloat(humiThresholdEt.getText().toString());
            float fireThreshold = Float.parseFloat(flameThresholdEt.getText().toString());
            float mqThreshold = Float.parseFloat(airThresholdEt.getText().toString());
            float soilThreshold = Float.parseFloat(soilThresholdEt.getText().toString());
            float lightThreshold=Float.parseFloat(lightThresholdEt.getText().toString());

            StringBuilder alertMessage = new StringBuilder();

            if (temperature > tempThreshold) {
                alertMessage.append("温度超过阈值！\n");
            }
            if (humidity > humiThreshold) {
                alertMessage.append("湿度超过阈值！\n");
            }
            if (fire < fireThreshold) {
                alertMessage.append("检测到火焰！\n");
            }
            if (mq > mqThreshold) {
                alertMessage.append("空气质量恶化！\n");
            }
            if (soil > soilThreshold) {
                alertMessage.append("土壤过湿！\n");
            }
            if (light > lightThreshold) {
                alertMessage.append("光线过亮！！\n");
            }

            if (alertMessage.length() > 0) {
                new AlertDialog.Builder(getContext())
                        .setTitle("阈值报警")
                        .setMessage(alertMessage.toString())
                        .setPositiveButton("确定", null)
                        .show();
            }

        } catch (Exception e) {
            Log.e("Threshold", "报警检测出错", e);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        // 重新创建定时器如果被关闭了
        if (dataSaver == null || dataSaver.isShutdown()) {
            dataSaver = Executors.newSingleThreadScheduledExecutor();
            dataSaver.scheduleAtFixedRate(task, 10, 10, TimeUnit.SECONDS);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // 取消所有网络请求
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
    private void showToast(String message) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() ->
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show());
        }
    }
    private void updatePeopleStatus(boolean detected) {
        if (!isAdded()) return;

        getActivity().runOnUiThread(() -> {
            peopleDetected = detected;
            if (detected) {
                peopleStatusText.setText("检测到人体");
                peopleStatusText.setTextColor(ContextCompat.getColor(getContext(), R.color.green_dark));
                peopleIndicator.setBackgroundResource(R.drawable.circle_indicator_on);
            } else {
                peopleStatusText.setText("未检测到人体");
                peopleStatusText.setTextColor(ContextCompat.getColor(getContext(), R.color.red_dark));
                peopleIndicator.setBackgroundResource(R.drawable.circle_indicator_off);
            }
        });
    }
    void json_data(String JSON) throws JSONException {
        JSONObject jsonObject = new JSONObject(JSON);
        if (jsonObject.getInt("code") != 0) {
            Log.e("API", "Server error: " + jsonObject.getString("msg"));
            return;
        }

        JSONArray data = jsonObject.getJSONArray("data");
        for (int i = 0; i < data.length(); i++) {
            JSONObject item = data.getJSONObject(i);
            String identifier = item.getString("identifier");

            // 根据实际返回的标识符修改
            if (item.has("value")) {
                String value = item.getString("value");
                switch (identifier) {
                    case "temp":
                        temp_value = value + "°C";  // 添加单位
                        break;
                    case "humi":
                        humi_value = value + "%";
                        break;
                    case "fire":
                        flame_value = value;
                        break;
                    case "mq135":
                        air_value = value + "ppm";
                        break;
                    case "light":
                        light_value=value+"lve";
                    case "Soil":
                        soil_value=value+"lve";
                    case "people":  // 人体检测状态
                        updatePeopleStatus("1".equals(value));
                        break;
                    case "RFID":
                        String rfidValue = item.getString("value");
                        cardNumbers.add(rfidValue);
                        break;
                }
            }
        }
        updateUI(); // 统一更新界面
    }

    public void addCardNumber(String cardNumber) {
        sharedViewModel.addCardNumber(cardNumber);
    }


    // 示例方法：模拟添加RFID数据（测试用）
    public void addTestData() {
        if (sharedViewModel != null) {
            sharedViewModel.addCardNumber("RFID_123456");
            Log.d(TAG, "测试数据已添加到 ViewModel");
        }
    }

    // 提供给外部获取数据的方法
    public List<String> getCardNumbers() {
        if (sharedViewModel == null) {
            Log.e(TAG, "sharedViewModel 为 null");
            return new ArrayList<>();
        }
        List<String> data = sharedViewModel.getCardNumbers().getValue();
        Log.d(TAG, "返回 ViewModel 中的数据: " + data);
        return data;
    }
}