package id.sch.smktelkom_mlg.privateassignment.xirpl114.sammovie;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.sch.smktelkom_mlg.privateassignment.xirpl114.sammovie.model.FavouriteItem;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class DetailActivity extends AppCompatActivity {
    private Integer mPostkey = null;
    private String jenis = null;

    private  String URL_DATA = "https://api.themoviedb.org/3/movie/popular?api_key=cc2b705c11164d940874ff87f19e62f4&language=en-US&page=1";

    public TextView textViewHeadet;
    public TextView textViewDescet;
    public TextView textViewReview;
    public ImageView imageViewDetail, imageViewPoster;
    public String url;
    public String urlGambar;
    FavouriteItem favouriteItem;
    boolean isPressed = true;
    FloatingActionButton fab;
    boolean isNew;
    ArrayList<FavouriteItem> fItem;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //SugarContext.init(this);

        mPostkey = getIntent().getExtras().getInt("blog_id");
        jenis = getIntent().getExtras().getString("jenis");

        if(jenis.equals("NP")){
            URL_DATA = "https://api.themoviedb.org/3/movie/now_playing?api_key=cc2b705c11164d940874ff87f19e62f4&language=en-US&page=1";
        } else if(jenis.equals("MA")){
            URL_DATA = "https://api.themoviedb.org/3/movie/popular?api_key=cc2b705c11164d940874ff87f19e62f4&language=en-US&page=1";
        } else if(jenis.equals("TR")){
            URL_DATA = "https://api.themoviedb.org/3/movie/top_rated?api_key=cc2b705c11164d940874ff87f19e62f4&language=en-US&page=1";
        } else if(jenis.equals("UC")){
            URL_DATA = "https://api.themoviedb.org/3/movie/upcoming?api_key=cc2b705c11164d940874ff87f19e62f4&language=en-US&page=1";
        }



        loadRecyclerViewData();

        textViewHeadet = (TextView) findViewById(R.id.textViewHeadet);
        textViewDescet = (TextView) findViewById(R.id.textViewDescet);
        textViewReview = (TextView) findViewById(R.id.textViewReview);
        imageViewDetail = (ImageView) findViewById(R.id.imageViewDetail);
        imageViewPoster = (ImageView) findViewById(R.id.imageViewPos);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        new MaterialShowcaseView.Builder(this)
                .setTarget(fab)
                .setDismissText("GOT IT")
                .setContentText("Tambahkan film ini film favorite mu!")
                .setDelay(200) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse("Yes") // provide a unique ID used to ensure it is only shown once
                .show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPressed) {
                    doSave();
                    Snackbar.make(view, "Berhasil ditambahkan ke favorit", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    doDelete();
                }
                else{

                    Snackbar.make(view, "Artikel favorit anda", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                isPressed = !isPressed;
            }
        });


        SharedPreferences.Editor editor = getSharedPreferences("Pos", MODE_PRIVATE).edit();
        editor.putInt("posisi", mPostkey);
        editor.commit();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        new MaterialShowcaseView.Builder(this)
                .setTarget(textViewHeadet)
                .setDismissText("GOT IT")
                .setContentText("Tambahkan film ini film favorite mu!")
                .setDelay(200) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse("Yes") // provide a unique ID used to ensure it is only shown once
                .show();


    }

    private void doDelete() {
        String judul = textViewHeadet.getText().toString();
        SharedPreferences.Editor editor = getSharedPreferences(judul, MODE_PRIVATE).edit();
        editor.putBoolean("isNew", false);
        editor.commit();
    }

    private void doSave() {
        String judul = textViewHeadet.getText().toString();
        String deskripsi = textViewDescet.getText().toString();
        String urlgambar = urlGambar;
        favouriteItem = new FavouriteItem(judul, deskripsi, urlgambar);
        favouriteItem.save();

        SharedPreferences.Editor editor = getSharedPreferences(judul, MODE_PRIVATE).edit();
        editor.putBoolean("isNew", true);
        editor.commit();
    }

    private void loadRecyclerViewData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {                        progressDialog.dismiss();
                        try{
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("results");
                            JSONObject o = array.getJSONObject(mPostkey);

                            setTitle(" ");

                            textViewHeadet.setText(o.getString("title"));
                            textViewDescet.setText(o.getString("release_date"));
                            textViewReview.setText(o.getString("overview"));
                            urlGambar = "https://image.tmdb.org/t/p/w500"+o.getString("backdrop_path");

                            Glide
                                    .with(DetailActivity.this)
                                    .load(urlGambar)
                                    .into(imageViewDetail);

                            Glide
                                    .with(DetailActivity.this)
                                    .load("https://image.tmdb.org/t/p/w500"+o.getString("poster_path"))
                                    .into(imageViewPoster);

                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
