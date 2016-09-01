package com.waean.asus.android_qbank;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ContentView(value = R.layout.activity_register)
public class RegisterActivity extends AppCompatActivity {

    private static final String url = "http://192.168.136.2:8080/Android_qBank_web/myServlet";

    @ViewInject(value = R.id.et_username)
    private EditText username;
    @ViewInject(value = R.id.et_password)
    private EditText password;
    @ViewInject(value = R.id.et_name)
    private EditText name;
    @ViewInject(value = R.id.et_phone)
    private EditText phone;
    @ViewInject(value = R.id.btn_register)
    private EditText register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
        x.view().inject(this);
    }

    @Event(value = R.id.btn_register, type = View.OnClickListener.class)
    private void OnClick(View view) {
       /*注册*/
        String mUsername = username.getText().toString();
        String mPassword = password.getText().toString();
        String mName = name.getText().toString();
        String mPhone = phone.getText().toString();
        Boolean isUsername = checkUsername(mUsername);/*检查用户名，只能用字母及3位以上*/
        if (!isUsername) {
            Toast.makeText(RegisterActivity.this, "检查用户名，只能用字母及3位以上", Toast.LENGTH_SHORT).show();
        }
        Boolean isPassword = checkPassword(mPassword);/*检查密码，密码是数字及至少4位以上！*/
        if (!isPassword) {
            Toast.makeText(RegisterActivity.this, "密码是数字及至少4位以上", Toast.LENGTH_SHORT).show();
        }
        Boolean isName = checkName(mName);/*检查昵称*/
        if (!isName) {
            Toast.makeText(RegisterActivity.this, "请输入昵称", Toast.LENGTH_SHORT).show();
        }

        boolean isPhone = checkPhoneNumber(mPhone);/*检查手机号码*/
        if (!isPhone) {
            Toast.makeText(RegisterActivity.this, "手机号码有误，请检查手机号码", Toast.LENGTH_SHORT).show();
        }

        if (isUsername && isPassword && isPhone && isName) {
            registerAccount(mUsername, mPassword, mName, mPhone);
        }


    }

    /**
     * 验证昵称
     *
     * @param mName
     * @return
     */
    private Boolean checkName(String mName) {
        if (!mName.equals("") && mName != null) {
            return true;
        }
        return false;
    }

    /**
     * 验证手机号码
     *
     * @param mPhone 手机号码
     * @return boolean
     */
    public static boolean checkPhoneNumber(String mPhone) {
        Pattern pattern = Pattern.compile("^1[0-9]{10}$");
        Matcher matcher = pattern.matcher(mPhone);
        return matcher.matches();
    }

    /**
     * 验证密码
     *
     * @param mPassword
     * @return
     */

    private Boolean checkPassword(String mPassword) {
        if (mPassword.length() >= 4) {
            Pattern pattern = Pattern.compile("^[0-9]*$");
            Matcher matcher = pattern.matcher(mPassword);
            return matcher.matches();
        }
        return false;
    }

    /**
     * 验证用户名
     *
     * @param mUsername
     * @return
     */
    private Boolean checkUsername(String mUsername) {
        if (mUsername.length() > 3) {
            Pattern pattern = Pattern.compile("^[A-Za-z]+$");
            Matcher matcher = pattern.matcher(mUsername);
            return matcher.matches();
        }
        return false;
    }

    /**
     * 注册
     *
     * @param mUsername
     * @param mPassword
     * @param mName
     * @param mPhone
     */
    private void registerAccount(String mUsername, String mPassword, String mName, String mPhone) {
        RequestParams params = new RequestParams(url);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("flag", 1);

            JSONObject object = new JSONObject();
            object.put("username", mUsername);
            object.put("password", mPassword);
            object.put("nickname", mName);
            object.put("telephone", mPhone);

            jsonObject.put("info", object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.addParameter("params", jsonObject.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    //{"":"",}
                    JSONObject object = new JSONObject(result);
                    String resultString = object.getString("success");
                    switch (resultString) {
                        case "true":
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            break;
                        case "false":
                            String reason = object.getString("reason");
                            Toast.makeText(RegisterActivity.this, "注册失败" + reason, Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("info", "=========");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }
}
