package id.sch.smktelkom_mlg.privateassignment.xirpl114.sammovie;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.sch.smktelkom_mlg.privateassignment.xirpl114.sammovie.adapter.MainAdapter;
import id.sch.smktelkom_mlg.privateassignment.xirpl114.sammovie.adapter.UCAdapter;
import id.sch.smktelkom_mlg.privateassignment.xirpl114.sammovie.model.MainListItem;
import id.sch.smktelkom_mlg.privateassignment.xirpl114.sammovie.model.NowPlaying;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

import static android.content.Context.MODE_PRIVATE;
import static id.sch.smktelkom_mlg.privateassignment.xirpl114.sammovie.R.id.recyclerView;
import static id.sch.smktelkom_mlg.privateassignment.xirpl114.sammovie.R.id.recyclerViewuc;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingFragment extends Fragment {

    private static final String URL_DATA = "https://api.themoviedb.org/3/movie/upcoming?api_key=cc2b705c11164d940874ff87f19e62f4&language=en-US&page=1";
    //private static final String URL_DATA = "https://api.nytimes.com/svc/movies/v2/reviews/search.json?api-key=f5abb6679ae54501a9be8139e683bebc";

    private RecyclerView recyclerView;
    private RecyclerView recyclerViewBanner;
    private RecyclerView.Adapter adapter;
    private RecyclerView.Adapter adaptera;

    private List<NowPlaying> listItems;
    //private List<BannerItem> bannerItemList;

    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;

    TextView textViewHeadetrec, textViewDescetrec, textViewReviewrec ;

    ImageView ivRec;

    Integer idName;

    public UpcomingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);

        NestedScrollView scroll = (NestedScrollView) view.findViewById(R.id.scrollView);

        MaterialViewPagerHelper.registerScrollView(getActivity(), scroll);

        recyclerView = (RecyclerView) view.findViewById(recyclerViewuc);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        listItems = new ArrayList<>();

        loadRecyclerViewData();


        textViewHeadetrec = (TextView) view.findViewById(R.id.textViewHeadfavrec);
        textViewDescetrec = (TextView) view.findViewById(R.id.textViewDescfavrec);
        ivRec = (ImageView) view.findViewById(R.id.imageViewOtofrec);

        SharedPreferences prefs = getActivity().getSharedPreferences("Pos", MODE_PRIVATE);
        idName = prefs.getInt("posisi", 0);



        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        SharedPreferences prefs = getActivity().getSharedPreferences("Pos", MODE_PRIVATE);
        idName = prefs.getInt("posisi", 0);
    }


    private void loadRecyclerViewData() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try{
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("results");

                            //JSONArray array = jsonObject.getJSONObject("data").getJSONArray("results");
                            //JSONArray array2 = jsonObject.getJSONArray("multimedia");

                            for(int i = 0; i<array.length(); i++){
                                JSONObject o = array.getJSONObject(i);
                                NowPlaying item = new NowPlaying(
                                        "https://image.tmdb.org/t/p/w500"+o.getString("backdrop_path"),
                                        o.getString("title"),
                                        o.getString("popularity")
                                );
                                listItems.add(item);
                            }
                            adapter = new UCAdapter(listItems, getActivity().getApplicationContext());
                            recyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity().getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}
