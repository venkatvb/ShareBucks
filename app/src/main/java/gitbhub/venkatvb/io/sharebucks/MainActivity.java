package gitbhub.venkatvb.io.sharebucks;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> owe = new ArrayList<String>();

    ArrayAdapter<String> mOweAdapter;

    ListView mListView;

    Button addExpenseButton;

    public static final int RESULT_PICK_CONTACT = 3029;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.owe_list);

        for(int i=0; i<100; i ++ ) {
            owe.add("Test");
        }

        addExpenseButton = (Button) findViewById(R.id.addExpense);

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getApplicationContext(), "Pick a contact", Toast.LENGTH_LONG).show();
                pickContact();
            }
        });
        mOweAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, owe);

        mListView.setAdapter(mOweAdapter);

        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, 1);

    }

    public void pickContact() {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("VB", "Came inside on activity result");
        if(resultCode == RESULT_OK ) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("Error", "Failed to pick contact");
        }
    }

    public void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            Log.d("VB", "Inside the correct data item");
            String phoneNo = null ;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            // Set the value to the textviews
            Log.d("VB:Message", "Name : " + name + ", Phone Number : " + phoneNo);
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Log.d("VB", "failed because of some reasons");
            e.printStackTrace();
        }
    }
}
