package com.teamproject.manageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamproject.manageapp.adapters.CsPostAdapter;
import com.teamproject.manageapp.models.Post;

import org.apache.commons.text.similarity.JaccardSimilarity;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
public class CsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CsPostAdapter adapter;
    private List<Post> posts = new ArrayList<>();
    private FirebaseFirestore db;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cs);
        recyclerView = findViewById(R.id.postRecyclerView);

        fetchPostsFromFirestore();


        recyclerView.setLayoutManager(new LinearLayoutManager(CsActivity.this));
        adapter = new CsPostAdapter(posts, CsActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private void fetchPostsFromFirestore() {
        db = FirebaseFirestore.getInstance();
        String collectionPath = "csboard/";
        Query query = db.collection(collectionPath)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Post post = documentSnapshot.toObject(Post.class);
                            posts.add(post);
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
}