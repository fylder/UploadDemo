package fylder.upload.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.demo_btn)
    void upload() {
        Intent intent = new Intent(this, UploadActivity.class);
        startActivity(intent);
    }
}
