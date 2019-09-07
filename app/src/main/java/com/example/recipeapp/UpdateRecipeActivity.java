package com.example.recipeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class UpdateRecipeActivity extends AppCompatActivity {

    ImageView recipeImage;
    Uri uri;
    EditText txt_name,txt_description,txt_price;
    String imageUrl;
    String key,oldImageUrl;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String recipename,recipeDescription,recipePrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recipe);

        recipeImage = (ImageView)findViewById(R.id.iv_foodImage);
        txt_name = (EditText)findViewById(R.id.txt_recipe_name);
        txt_description = (EditText)findViewById(R.id.text_description);
        txt_price = (EditText)findViewById(R.id.text_price);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){

            Glide.with(UpdateRecipeActivity.this)
                    .load(bundle.getString("oldimageUrl"))
                    .into(recipeImage);
            txt_name.setText(bundle.getString("recipeNameKey"));
            txt_description.setText(bundle.getString("descriptionKey"));
            txt_price.setText(bundle.getString("priceKey"));
            key = bundle.getString("key");
            oldImageUrl = bundle.getString("oldimageUrl");
        }


        databaseReference = FirebaseDatabase.getInstance().getReference("Recipe").child(key);



    }

    public void btnSelectImage(View view) {
        Intent photoPicker = new Intent(Intent.ACTION_PICK);
        photoPicker.setType("image/*");
        startActivityForResult(photoPicker,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            uri = data.getData();
            recipeImage.setImageURI(uri);

        }
        else Toast.makeText(this, "You haven't picked image", Toast.LENGTH_SHORT).show();

    }


    public void btnUpdateRecipe(View view) {

         recipename = txt_name.getText().toString().trim();
         recipeDescription = txt_description.getText().toString().trim();
         recipePrice = txt_price.getText().toString();


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Recipe Uplading....");
        progressDialog.show();
        storageReference = FirebaseStorage.getInstance()
                .getReference().child("RecipeImage").child(uri.getLastPathSegment());
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();
                uploadRecipe();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });


    }

    public void uploadRecipe(){



        FoodData foodData = new FoodData(
               recipename,
                recipeDescription,
                recipePrice,
                imageUrl
        );


       databaseReference.setValue(foodData).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               StorageReference storageReferenceNew  = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl);
               storageReferenceNew.delete();
               Toast.makeText(UpdateRecipeActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
           }
       });




    }

}
