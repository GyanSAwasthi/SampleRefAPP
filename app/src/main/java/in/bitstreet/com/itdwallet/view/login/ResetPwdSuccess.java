package in.bitstreet.com.itdwallet.view.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import in.bitstreet.com.itdwallet.R;

public class ResetPwdSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd_success);
        Button btnLogin = (Button) findViewById(R.id.btn_Login_reset_pwd);
        TextView tvPwdResetSuccess = (TextView) findViewById(R.id.tv_reset_pwd_success);

       initActionBar();

        setTypeFaceFont(tvPwdResetSuccess);

        listerns(btnLogin);

    }

    private void initActionBar() {

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setElevation(0);

    }

    private void setTypeFaceFont(TextView tvPwdResetSuccess) {

        Typeface typeFaceForgotPwd = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
        tvPwdResetSuccess.setTypeface(typeFaceForgotPwd);
    }

    private void listerns(Button btnLogin) {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ResetPwdSuccess.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ResetPwdSuccess.this,LoginActivity.class);
        startActivity(intent);
        finish();

    }
}
