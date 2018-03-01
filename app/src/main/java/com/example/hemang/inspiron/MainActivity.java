package com.example.hemang.inspiron;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText quote, author;
    private Button save;
    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("sampleData/inspiron");
    private TextView quoteTv, authorTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quote = findViewById(R.id.quoteField);
        author = findViewById(R.id.authorField);
        save = findViewById(R.id.saveBtn);
        quoteTv = findViewById(R.id.quoteTv);
        authorTv = findViewById(R.id.authorTv);

    }

    @Override
    protected void onStart(){
        super.onStart();
        mDocRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(documentSnapshot.exists()) {
                    String gotQuote = documentSnapshot.getString("quote");
                    String gotAuthor = documentSnapshot.getString("author");

                    quoteTv.setText(gotQuote);
                    authorTv.setText("-" + gotAuthor);

                    Toast.makeText(getApplicationContext(), "Retrieved", Toast.LENGTH_SHORT).show();
                }
                else if (e != null){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void save(View view){
        String quoteText, authorText;

        quoteText = quote.getText().toString();
        authorText = author.getText().toString();

        if(TextUtils.isEmpty(quoteText) && TextUtils.isEmpty(authorText)){

            Toast.makeText(this, "Please enter the text!", Toast.LENGTH_SHORT).show();

        }
        else{

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("quote", quoteText);
            data.put("author", authorText);

            mDocRef.set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Did not upload!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    public void retrieve(View view){
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    String gotQuote = documentSnapshot.getString("quote");
                    String gotAuthor = documentSnapshot.getString("author");

                    quoteTv.setText(gotQuote);
                    authorTv.setText("-" + gotAuthor);

                    Toast.makeText(getApplicationContext(), "Retrieved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
