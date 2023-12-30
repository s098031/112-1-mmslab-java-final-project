package com.example.a112_1_mmslab_java_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.AlertDialog;


import java.util.ArrayList;


public class DigitalWallet extends AppCompatActivity {
    private String selectedItemToDelete = "";

    private EditText ed_item, ed_date,ed_price;
    private Button btn_insert, btn_query, btn_update, btn_delete,btn_back,btn_monthly_summary,btn_tutorial;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items = new ArrayList<>();
    private SQLiteDatabase dbrw2;
    private String id_item,year,month;
    private boolean SummaryMonth = false;
    private int currentTotal;
    private int newAmount;

    private boolean isValidDateFormat(String date) {
        String regex = "^(\\d{4})(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$";
        return date.matches(regex);
    }
    private boolean isValidDateFormatMM(String date) {
        String regex = "^(\\d{4})(0[1-9]|1[0-2])$";
        return date.matches(regex);
    }

    private void showMonthlySummaryDialog() {
        // 在這裡構建並顯示月結的彈出窗口
        // 使用AlertDialog.Builder來構建窗口
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("月結");

        // 創建一個ListView，用來顯示每個月的花費總額
        ListView listView = new ListView(this);
        builder.setView(listView);

        // 計算每個月的花費總額
        ArrayList<String> monthlySummaries = calculateMonthlySummaries();

        // 使用ArrayAdapter將每個月的花費總額添加到ListView中
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, monthlySummaries);
        listView.setAdapter(adapter);

        // 設置確定按鈕
        builder.setPositiveButton("確定", (dialog, which) -> {
            // 在這裡處理按下確定按鈕的邏輯
            dialog.dismiss();
        });

        // 顯示AlertDialog
        builder.show();
    }

    private ArrayList<String> calculateMonthlySummaries() {
        // 在這裡計算每個月的花費總額，然後返回一個包含結果的ArrayList
        // 你可以遍歷所有記錄，根據日期計算每個月的花費
        // 這裡提供一個簡單的例子，實際情況可能需要更複雜的邏輯

        ListAdapter adapter = listView.getAdapter();

        ArrayList<String> monthlySummaries = new ArrayList<>();
        currentTotal=0;
        for (int i = 0; i < adapter.getCount(); i++) {
            String itemText = (String) adapter.getItem(i);
            String[] item_parts = itemText.split("\t\t\t\t");
            String date = item_parts[1].replace("日期:", "");
            year = date.substring(0, 4);
            month = date.substring(4, 6);
            String money = item_parts[2].replace("價格:", "");
            currentTotal = currentTotal + Integer.parseInt(money);
        }
        if(SummaryMonth == true){
            monthlySummaries.add(year + "年" + month + "月: " + currentTotal + "元");
        }else{
            monthlySummaries.add("截至"+year + "年" + month + "月: " + currentTotal + "元");
        }
        return monthlySummaries;
    }


    private String buildSearchQuery(String input) {
        // 如果輸入的格式為 yyyyMM，則表示要按年/月搜尋
        String regex = "^(\\d{4})(0[1-9]|1[0-2])$";
        if (input.matches(regex)) {
            String year = input.substring(0, 4);
            String month = input.substring(4);
            // 構造 SQL 查詢
            return "SELECT * FROM myTable WHERE date LIKE '%" +year +month+ "%' ORDER BY date";
        } else {
            // 其他情況視為無效輸入，顯示全部記錄
            return "SELECT * FROM myTable ORDER BY date";
        }
    }

    private void performQuery(String query) {
        Cursor c = dbrw2.rawQuery(query, null);
        processQueryResult(c);
    }

    private void performQuery() {
        // 如果沒有提供查詢語句，預設顯示全部記錄
        performQuery("SELECT * FROM myTable ORDER BY date");
    }

    private void processQueryResult(Cursor c) {
        c.moveToFirst();
        items.clear();
        Toast.makeText(DigitalWallet.this, "共有" + c.getCount() + "筆", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < c.getCount(); i++) {
            items.add("項目:" + c.getString(1) + "\t\t\t\t日期:" + c.getString(3) + "\t\t\t\t價格:" + c.getString(2));
            c.moveToNext();
        }

        // 使用 runOnUiThread 在主線程執行
        runOnUiThread(() -> adapter.notifyDataSetChanged());

        c.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbrw2.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digitalwallet);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ed_item = findViewById(R.id.ed_item);
        ed_date = findViewById(R.id.ed_date);
        ed_price = findViewById(R.id.ed_price);
        btn_query = findViewById(R.id.btn_query);
        btn_insert = findViewById(R.id.btn_insert);
        btn_monthly_summary = findViewById(R.id.btn_monthly_summary);

        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        listView = findViewById(R.id.listView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        dbrw2 = new MyDBHelper2(this).getWritableDatabase();

        btn_back=findViewById(R.id.btn_back2);
        btn_tutorial=findViewById(R.id.btn_tutorial);

        performQuery();
        btn_tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DigitalWallet.this);

                // 设置对话框标题和消息
                builder.setTitle("使用說明")
                        .setMessage("目前只能記錄支出的部分呦~\n\n查詢功能使用 yyyymm 不需要打日期也可以搜索到該月份資料\n\n    月結   或   總結  請在日期輸入\nyyyymm或   (空)   即可加總花費");

                // 设置按钮（这里只设置了一个“确定”按钮）
                builder.setPositiveButton("我知道了!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 创建并显示对话框
                        dialogInterface.dismiss(); // 关闭对话框
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DigitalWallet.this, MainActivity.class);
                // 啟動 Note Activity 的 Intent
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                DigitalWallet.this.startActivity(intent);

                // 結束 MainActivity
                finish();
            }
        });

        btn_insert.setOnClickListener(view -> {
            if (ed_item.length() < 1 || ed_date.length() < 1 || ed_price.length() < 1)
                Toast.makeText(DigitalWallet.this, "欄位請勿留空", Toast.LENGTH_SHORT).show();
            else {
                // 檢查日期格式
                if (!isValidDateFormat(ed_date.getText().toString())) {
                    Toast.makeText(DigitalWallet.this, "日期格式應為 yyyy/mm/dd", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    dbrw2.execSQL("INSERT INTO myTable(item, price, date) values(?,?,?)",
                            new Object[]{ed_item.getText().toString(), ed_price.getText().toString(), ed_date.getText().toString()});
                    Toast.makeText(DigitalWallet.this, "新增項目" + ed_item.getText().toString() +
                            "      日期" + ed_date.getText().toString() + "      價格" + ed_price.getText().toString(), Toast.LENGTH_SHORT).show();
                    ed_item.setText("");
                    ed_date.setText("");
                    ed_price.setText("");
                    performQuery();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(DigitalWallet.this, "新增失敗:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });




        btn_update.setOnClickListener(view -> {
            if (ed_item.length() < 1 || ed_date.length() < 1)
                Toast.makeText(DigitalWallet.this, "欄位請勿留空", Toast.LENGTH_SHORT).show();
            else {
                try {
                    if (!isValidDateFormat(ed_date.getText().toString())) {
                        Toast.makeText(DigitalWallet.this, "日期格式應為 yyyy/mm/dd", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 刪除原來的項目
                    dbrw2.execSQL("DELETE FROM myTable WHERE item = ? AND _id = ?", new String[]{selectedItemToDelete,id_item});

                    // 插入新的項目
                    dbrw2.execSQL("INSERT INTO myTable(item, price, date) values(?,?,?)",
                            new Object[]{ed_item.getText().toString(), ed_price.getText().toString(), ed_date.getText().toString()});

                    Toast.makeText(DigitalWallet.this, "更新項目" + ed_item.getText().toString() +
                            "      日期" + ed_date.getText().toString() + "      價格" + ed_price.getText().toString(), Toast.LENGTH_SHORT).show();

                    ed_item.setText("");
                    ed_date.setText("");
                    ed_price.setText("");

                    // 確保更新後刷新列表
                    performQuery();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(DigitalWallet.this, "更新失敗:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });




        btn_delete.setOnClickListener(view -> {
            if (ed_item.length() < 1)
                Toast.makeText(DigitalWallet.this, "品項請勿留空", Toast.LENGTH_SHORT).show();
            else {
                try {
                    String itemToDelete = ed_item.getText().toString();
                    dbrw2.execSQL("DELETE FROM myTable WHERE item = ?", new String[]{itemToDelete});
                    Toast.makeText(DigitalWallet.this, "刪除項目" + itemToDelete, Toast.LENGTH_SHORT).show();
                    ed_item.setText("");
                    ed_date.setText("");
                    ed_price.setText("");
                    performQuery();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(DigitalWallet.this, "刪除失敗:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_query.setOnClickListener(view -> {
            String input = ed_date.getText().toString().trim();

            if (input.isEmpty()) {
                // 如果輸入為空，則顯示全部記錄
                performQuery("SELECT * FROM myTable ORDER BY date");
            }
            else if(!isValidDateFormatMM(ed_date.getText().toString()))
            {
                Toast.makeText(DigitalWallet.this, "日期格式應為 yyyy/mm", Toast.LENGTH_SHORT).show();
                items.clear();
                adapter.notifyDataSetChanged();
                return;
            }
            else {
                // 解析輸入，構造 SQL 查詢
                String sqlQuery = buildSearchQuery(input);

                // 執行查詢
                performQuery(sqlQuery);
            }
        });


        listView.setOnItemClickListener((parent, view, position, id) -> {
            // 點擊 ListView 上的項目時執行的代碼
            String selectedItem = items.get(position);
            String[] parts = selectedItem.split("\t\t\t\t");

            // 將點擊的項目信息填充到對應的 EditText 中
            ed_item.setText(parts[0].replace("項目:", ""));
            ed_date.setText(parts[1].replace("日期:", ""));
            ed_price.setText(parts[2].replace("價格:", ""));

            // 記錄點擊的項目
            selectedItemToDelete = ed_item.getText().toString();

            Cursor c= dbrw2.rawQuery("SELECT * FROM myTable WHERE date LIKE '" + ed_date.getText().toString() +
                    "' AND price LIKE '"+ed_price.getText().toString()+ "' AND item LIKE '"+ed_item.getText().toString()+ "' ORDER BY date", null);
            c.moveToFirst();
            id_item = c.getString(0);
            c.close();

        });

        btn_monthly_summary.setOnClickListener(view -> {
            // 在這裡處理月結的邏輯
            SummaryMonth =(ed_date.length()<1) ? false : true;
            // 在這裡處理月結的邏輯
            if (ed_date.length() < 1) {
                showMonthlySummaryDialog();
            }else if (!isValidDateFormatMM(ed_date.getText().toString())) {
                Toast.makeText(DigitalWallet.this, "日期格式應為 yyyy/mm", Toast.LENGTH_SHORT).show();
                return;
            }else {
                showMonthlySummaryDialog();
            }
        });



    }
}
