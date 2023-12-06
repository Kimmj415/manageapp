package com.teamproject.manageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamproject.manageapp.models.Post;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsPostDetailActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView dateTextView;
    private TextView contentTextView;
    private TextView useridTextView;
    private FirebaseFirestore db;
    private Button reg_button;
    private EditText comment_et;
    private String target;

    private ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cs_post_detail);

        titleTextView = findViewById(R.id.title_tv);
        dateTextView = findViewById(R.id.date_tv);
        useridTextView=findViewById(R.id.userid_tv);
        contentTextView = findViewById(R.id.content_tv);
        db = FirebaseFirestore.getInstance();
        reg_button=findViewById(R.id.reg_button);
        comment_et=findViewById(R.id.comment_et);
        backbutton=findViewById(R.id.backbutton);


        String authorId = getIntent().getStringExtra("AUTHOR_ID");
        String date = getIntent().getStringExtra("DATE");
        String postId = getIntent().getStringExtra("POST_ID");
        target=getIntent().getStringExtra("TARGET");
        String collectionPath = "csboard/";

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CsPostDetailActivity.this, CsActivity.class));
            }
        });


        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost(postId);
                addComment(postId,authorId);
            }
        });

        db.collection(collectionPath)
                .whereEqualTo("postId", postId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // 일치하는 문서가 하나 이상 있다면, 첫 번째 문서를 사용합니다.
                                DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                // DocumentSnapshot으로부터 Post 객체 가져오기
                                Post post = document.toObject(Post.class);
                                if (post != null) {
                                    titleTextView.setText(post.getTitle().toString());
                                    dateTextView.setText(post.getTimestamp().toString());
                                    contentTextView.setText(post.getContents().toString());
                                    useridTextView.setText(post.getuserId().toString());
                                }
                            } else {
                                Toast.makeText(CsPostDetailActivity.this, "일치하는 게시물 없음", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CsPostDetailActivity.this, "데이터 로딩 실패.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void deletePost(String postId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("csboard").document(postId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CsPostDetailActivity.this, "답변을 등록했습니다.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CsPostDetailActivity.this, CsActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CsPostDetailActivity.this, "답변 등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addComment(String postId, String authorId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("user").document(authorId)
                .collection("cs").document(target);

        Map<String, Object> updates = new HashMap<>();
        updates.put("comment", comment_et.getText().toString());

        docRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}