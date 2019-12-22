package com.example.retrofittest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_request_first;
    private Button btn_request_second;
    TextView tv_responseBody;
    ImageView iv_picture;
    GithubService githubService;
    Call<List<ReposBean>> call;
    Call<ResponseBody> call_second;
    private static final String TAG="MainAcyivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn_request_first=(Button) findViewById(R.id.request_first);
        btn_request_first.setOnClickListener(this);
        btn_request_second =(Button) findViewById(R.id.request_second);
        btn_request_second.setOnClickListener(this);
        tv_responseBody=(TextView) findViewById(R.id.responseBody);
        iv_picture=(ImageView) findViewById(R.id.image_View);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.request_first:
                request();
                break;
            case R.id.request_second:
                requestImage();
                break;

                default:
                    break;


        }

    }

    private void requestImage() {
        Retrofit retrofit=APIClient.getClient();
        githubService=retrofit.create(GithubService.class);
        call_second=githubService.getPicture();
        call_second.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //该代码是在主线程运行
                ResponseBody body = response.body();
                InputStream inputStream = body.byteStream();
                Drawable drawable = Drawable.createFromStream(inputStream, "pic.png");
                iv_picture.setBackground(drawable);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "网络请求失败，失败原因：" + t.getMessage());
            }
        });
    }

    private void request() {
        Retrofit retrofit=APIClient.getClient();
        githubService=retrofit.create(GithubService.class);
        call= githubService.listRepos("octocat");
        call.enqueue(new Callback<List<ReposBean>>() {
            @Override
            public void onResponse(Call<List<ReposBean>> call, Response<List<ReposBean>> response) {
                List<ReposBean> reposBeans= response.body();
                for (ReposBean reposBean:reposBeans) {
                    tv_responseBody.setText(reposBean.toString());
                }
            }

            @Override
            public void onFailure(Call<List<ReposBean>> call, Throwable t) {

            }
        });

    }
}
