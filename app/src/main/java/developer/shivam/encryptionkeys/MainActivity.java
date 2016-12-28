package developer.shivam.encryptionkeys;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    EditText etUserName;
    EditText etPassword;
    Button btnEncryptString;
    private Context mContext = MainActivity.this;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = new AuthTokenPreferences(mContext, this.getSharedPreferences("auth_credential", MODE_PRIVATE));

        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btnEncryptString = (Button) findViewById(R.id.btnSaveCredential);
        btnEncryptString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putString("username", etUserName.getText().toString());
                sharedPreferences.edit().putString("password", etPassword.getText().toString());
                sharedPreferences.edit().apply();
            }
        });
    }
}
