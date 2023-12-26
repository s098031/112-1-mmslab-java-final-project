package com.example.a112_1_mmslab_java_final_project;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Note extends AppCompatActivity {

    private ArrayList<String> items = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private SQLiteDatabase dbrw;
    private  Button btn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        dbrw = new MyDBHelper(this).getWritableDatabase();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        ((ListView) findViewById(R.id.listView)).setAdapter(adapter);


        btn_back=findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Note.this, MainActivity.class);
                // 啟動 Note Activity 的 Intent
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Note.this.startActivity(intent);

                // 結束 MainActivity
                finish();
            }
        });


        setListener();
    }


    @Override
    protected void onDestroy() {
        dbrw.close();
        super.onDestroy();
    }

    int datecheck=0;
    private void setListener() {
        final EditText ed_month = findViewById(R.id.ed_month);
        final EditText ed_date = findViewById(R.id.ed_date);
        final EditText ed_thing = findViewById(R.id.ed_thing);

        findViewById(R.id.btn_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_month.length() < 1 || ed_date.length() < 1 || ed_thing.length() < 1) {
                    showToast("欄位請勿留空");
                }else {
                    switch (Integer.parseInt(ed_month.getText().toString())) {
                        case 1:
                        case 3:
                        case 5:
                        case 7:
                        case 8:
                        case 10:
                        case 12:
                            if (Integer.parseInt(ed_date.getText().toString()) < 1 || Integer.parseInt(ed_date.getText().toString()) > 31) {
                                showToast("請輸入正確月份與日期");
                                datecheck = 0;
                            }else datecheck=1;
                            break;
                        case 4:
                        case 6:
                        case 9:
                        case 11:
                            if (Integer.parseInt(ed_date.getText().toString()) < 1 || Integer.parseInt(ed_date.getText().toString()) > 30) {
                                showToast("請輸入正確月份與日期");
                                datecheck = 0;
                            }else datecheck=1;
                            break;
                        case 2:
                            if (Integer.parseInt(ed_date.getText().toString()) < 1 || Integer.parseInt(ed_date.getText().toString()) > 29) {
                                showToast("請輸入正確月份與日期");
                                datecheck = 0;
                            }else datecheck=1;
                            break;
                        default:
                            showToast("請輸入正確月份與日期");
                            break;
                    }
                    if(datecheck==1){
                        try {
                            dbrw.execSQL(
                                    "INSERT INTO myTable(month, date,thing) VALUES(?,?,?)",
                                    new String[]{ed_month.getText().toString(), ed_date.getText().toString(), ed_thing.getText().toString()});
                            showToast("新增:" + ed_month.getText() + "月" + ed_date.getText() + "日   事項:" +ed_thing.getText());
                            cleanEditText();
                        } catch (Exception e) {
                            showToast("新增失敗:" + e);
                        }
                    }

                }
            }
        });

        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                if (ed_date.length() < 1 || ed_thing.length() < 1) {
                    showToast("欄位請勿留空");
                } else {
                    try {
                        dbrw.execSQL("UPDATE myTable SET price = " + ed_thing.getText() + " WHERE book LIKE '" + ed_date.getText() + "'");
                        showToast("更新:" + ed_date.getText() + ",價格:" + ed_thing.getText());
                        cleanEditText();
                    } catch (Exception e) {
                        showToast("更新失敗:" + e);
                    }
                }
                */
            }
        });

        findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                if (ed_date.length() < 1) {
                    showToast("書名請勿留空");
                } else {
                    try {
                        dbrw.execSQL("DELETE FROM myTable WHERE book LIKE '" + ed_date.getText() + "'");
                        showToast("刪除:" + ed_date.getText());
                        cleanEditText();
                    } catch (Exception e) {
                        showToast("刪除失敗:" + e);
                    }
                }

                 */
            }
        });

        findViewById(R.id.btn_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String queryString = (ed_month.length() < 1 || ed_date.length() < 1) ? "SELECT * FROM myTable" : "SELECT * FROM myTable WHERE month LIKE '" + ed_month.getText() + "' AND date LIKE '"+ed_date.getText()+"'";
                Cursor c = dbrw.rawQuery(queryString,null, null);
                c.moveToFirst();
                items.clear();
                showToast("共有" + c.getCount() + "筆資料");
                for (int i = 0; i < c.getCount(); i++) {
                    items.add( c.getInt(0) + "月\t\t\t\t" + c.getInt(1)+"日 : "+c.getString(2));
                    c.moveToNext();
                }
                adapter.notifyDataSetChanged();
                c.close();

            }
        });



    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void cleanEditText() {
        ((EditText) findViewById(R.id.ed_date)).setText("");
        ((EditText) findViewById(R.id.ed_thing)).setText("");
    }

}
