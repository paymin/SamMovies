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
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.sch.smktelkom_mlg.privateassignment.xirpl114.sammovie.adapter.MainAdapter;
import id.sch.smktelkom_mlg.privateassignment.xirpl114.sammovie.adapter.TRAdapter;
import id.sch.smktelkom_mlg.privateassignment.xirpl114.sammovie.adapter.UCAdapter;
import id.sch.smktelkom_mlg.privateassignment.xirpl114.sammovie.model.MainListItem;
import id.sch.smktelkom_mlg.privateassignment.xirpl114.sammovie.model.NowPlaying;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

import static android.content.Context.MODE_PRIVATE;
import static id.sch.smktelkom_mlg.privateassignment.xirpl114.sammovie.R.id.recyclerViewtra;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopRatedFragment extends Fragment {

    private static final String URL_DATA = "https://api.themoviedb.org/3/movie/top_rated?api_key=cc2b705c11164d940874ff87f19e62f4&language=en-US&page=1";

    private RecyclerView recyclerViewtr;
    private RecyclerView.Adapter adapter;

    private List<NowPlaying> listItems;

    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;

    TextView textViewHeadetrec, textViewDescetrec, textViewReviewrec ;

    ImageView ivRec;

    Integer idName;
    public TopRatedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_rated, container, false);

        NestedScrollView scroll = (NestedScrollView) view.findViewById(R.id.scrollView);

        MaterialViewPagerHelper.registerScrollView(getActivity(), scroll);

        recyclerViewtr = (RecyclerView) view.findViewById(recyclerViewtra);
        recyclerViewtr.setHasFixedSize(true);
        recyclerViewtr.setLayoutManager(new LinearLayoutManager(getActivity()));

        listItems = new ArrayList<>();

        loadRecyclerViewData();

        SharedPreferences prefs = getActivity().getSharedPreferences("Pos", MODE_PRIVATE);
        idName = prefs.getInt("posisi", 0);



        return view;
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
                                        o.getString("vote_average")
                                );
                                listItems.add(item);
                            }
                            adapter = new TRAdapter(listItems, getActivity().getApplicationContext());
                            recyclerViewtr.addItemDecoration(new MaterialViewPagerHeaderDecorator());
                            recyclerViewtr.setAdapter(adapter);

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
