package com.example.mlkittest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity {

    Button LogOut;
    TextView UsernameShow;
    String UsernameHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        LogOut = (Button)findViewById(R.id.button);
        UsernameShow = (TextView)findViewById(R.id.UsernameShow);


        Intent intent = getIntent();
        UsernameHolder = intent.getStringExtra(UserLoginActivity.UserName);
        UsernameShow.setText(UsernameHolder);


        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

                Intent intent = new Intent(DashboardActivity.this, UserLoginActivity.class);

                startActivity(intent);

                Toast.makeText(DashboardActivity.this, "Log Out Successfully", Toast.LENGTH_LONG).show();


            }
        });
    }
}
