package com.natasha.googlebooksapp.activities;
import android.content.Intent;
import android.os.Bundle;
import com.natasha.googlebooksapp.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private EditText bookName;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Submit Button
        bookName = (EditText) findViewById(R.id.bookEditText);
        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent submitInput = new Intent(MainActivity.this, DisplayBooksActivity.class);
                if (!bookName.getText().toString().isEmpty()){
                    String bkName = bookName.getText().toString();
                    submitInput.putExtra("UserInput", bkName);
                    startActivity(submitInput);
                }
                else{
                    Toast.makeText(getApplicationContext(), "No empty fields allowed!", Toast.LENGTH_LONG).show();
                }
                bookName.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //favorites menu button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorites) {
            Intent goToNextActivity = new Intent(getApplicationContext(), DisplayBooksFavoritesActivity.class);
            startActivity(goToNextActivity);
        }
        return super.onOptionsItemSelected(item);
    }

}
