package com.example.a112_1_mmslab_java_final_project;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.INotificationSideChannel;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


public class MainActivity extends AppCompatActivity {
    private TextView tv_func;
    private Button btn_start;
    private int Dol,cont=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this).asGif().load(R.drawable.giphy).into(imageView);

        String[] messageArray = new String[]{
                "記事本", "電子記帳",
                "債務表", "敬請期待",
                "敬請期待", "敬請期待",
                "敬請期待","","","","","","","沒功能了~不要往下了~~",
                "","","","","","","","","","","","",
                "","你很奇怪耶!"
        };

        ArrayAdapter<String> messageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messageArray);

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(messageAdapter);

        tv_func = findViewById(R.id.tv_func);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 使用者點擊了 ListView 中的項目
                String selectedMessage = messageArray[position];
                if(selectedMessage == ""){
                    tv_func.setText("選擇功能:無");
                }else if(selectedMessage =="沒功能了~不要往下了~~"){
                    tv_func.setText("沒功能了~不要往下了~~");
                }else if(selectedMessage =="你很奇怪耶!"){
                    tv_func.setText("你很奇怪耶!");

                    if(cont<=1)Toast.makeText(MainActivity.this, "不要點我了!", Toast.LENGTH_SHORT).show();
                    if(cont >=4 && cont<=5)Toast.makeText(MainActivity.this, "真的不要點了，這裡沒有功能", Toast.LENGTH_SHORT).show();
                    if(cont>=8){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                        // 设置对话框标题和消息
                        builder.setTitle("恭喜你打開了潘朵拉的魔盒")
                                .setMessage("本功能請謹慎使用\n\n請擁有心臟病、癲癇、被害妄想症、聖母、常見宗教之狂熱者退避三舍!!\n請擁有心臟病、癲癇、被害妄想症、聖母、常見宗教之狂熱者退避三舍!!\n請擁有心臟病、癲癇、被害妄想症、聖母、常見宗教之狂熱者退避三舍!!\n\n軟體版本不支援更新!");

                        // 设置按钮（这里只设置了一个“确定”按钮）
                        builder.setPositiveButton("我知道了!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 创建并显示对话框


                                Intent intent = new Intent(MainActivity.this, GodGame.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                MainActivity.this.startActivity(intent);
                                finish();
                                dialogInterface.dismiss(); // 关闭对话框
                            }
                        });
                        builder.setNegativeButton("我現在離開!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 创建并显示对话框
                                finish();
                                dialogInterface.dismiss(); // 关闭对话框
                            }
                        });
                        builder.setNeutralButton("我現在離開!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 创建并显示对话框
                                finish();
                                dialogInterface.dismiss(); // 关闭对话框
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                    cont++;
                }else{
                    tv_func.setText("選擇功能:"+selectedMessage);
                }

            }
        });


        btn_start=findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent;
                String str1=tv_func.getText().toString();
                switch (str1){
                    case "選擇功能:記事本":
                         intent = new Intent(MainActivity.this, Note.class);
                        Toast.makeText(MainActivity.this, "Service ! 啟動 !!", Toast.LENGTH_SHORT).show();
                        break;

                    case "選擇功能:電子記帳":
                         intent = new Intent(MainActivity.this, DigitalWallet.class);
                        Toast.makeText(MainActivity.this, "Service ! 啟動 !!", Toast.LENGTH_SHORT).show();
                        break;

                    case "選擇功能:債務表":
                         intent = new Intent(MainActivity.this, ExpensesTracker.class);
                        Toast.makeText(MainActivity.this, "Service ! 啟動 !!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        intent = new Intent(MainActivity.this, MainActivity.class);
                        Toast.makeText(MainActivity.this, "請選擇一項功能", Toast.LENGTH_SHORT).show();
                        break;
                }

                // 啟動 Note Activity 的 Intent
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.this.startActivity(intent);

                // 結束 MainActivity
                finish();
            }
        });
    }
}



