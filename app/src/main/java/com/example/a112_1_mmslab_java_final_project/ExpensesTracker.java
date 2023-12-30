package com.example.a112_1_mmslab_java_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import java.util.ArrayList;


public class ExpensesTracker extends AppCompatActivity {
    private EditText ed_item, ed_date, ed_price;
    private Button btn_insert, btn_query, btn_update, btn_delete, btn_back;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items = new ArrayList<>();
    private SQLiteDatabase dbrw3;

    private boolean isValidDateFormat(String date) {
        String regex = "^(\\d{4})(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$";
        return date.matches(regex);
    }

    private void performQuery() {
        Cursor c;
        if (ed_item.length() < 1) {
            // 使用 ORDER BY 子句按日期排序
            c = dbrw3.rawQuery("SELECT * FROM myTable ORDER BY date", null);
        } else {
            // 使用 ORDER BY 子句按日期排序
            c = dbrw3.rawQuery("SELECT * FROM myTable WHERE item  LIKE '" +
                    ed_item.getText().toString() + "' ORDER BY date", null);
        }

        /*
        c.moveToFirst();
        items.clear();
        Toast.makeText(ExpensesTracker.this, "共有" + c.getCount() + "筆", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < c.getCount(); i++) {
            items.add("項目:" + c.getString(0) + "\t\t\t\t日期:" + c.getString(2) + "\t\t\t\t價格:" + c.getString(1));
            c.moveToNext();
        }
        adapter.notifyDataSetChanged();
        c.close();
         */

        // 在需要顯示的地方
        c.moveToFirst();
        ArrayList<String> items = new ArrayList<>();

        for (int i = 0; i < c.getCount(); i++) {
            items.add("項目:" + c.getString(0) + "\t\t\t\t日期:" + c.getString(2) + "\t\t\t\t價格:" + c.getString(1));
            c.moveToNext();
        }

        c.close();

// 將 items 轉換成 String[] 以供 AlertDialog 使用
        String[] itemsArray = new String[items.size()];
        itemsArray = items.toArray(itemsArray);

// 使用 AlertDialog 顯示資料
        AlertDialog.Builder builder = new AlertDialog.Builder(ExpensesTracker.this);
        builder.setTitle("資料列表")
                .setItems(itemsArray, null)
                .setPositiveButton("確定", null) // 這裡添加確定按鈕，也可以根據需要添加其他按鈕
                .show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbrw3.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_tracker);

        dbrw3 = new MyDBHelper3(this).getWritableDatabase();

        ed_item = findViewById(R.id.ed_item);
        ed_date = findViewById(R.id.ed_date);
        ed_price = findViewById(R.id.ed_price);
        btn_query = findViewById(R.id.btn_query);
        btn_insert = findViewById(R.id.btn_insert);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        dbrw3 = new MyDBHelper2(this).getWritableDatabase();

        btn_back = findViewById(R.id.btn_back2);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExpensesTracker.this, MainActivity.class);
                // 啟動 Note Activity 的 Intent
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ExpensesTracker.this.startActivity(intent);

                // 結束 MainActivity
                finish();
            }
        });

        btn_insert.setOnClickListener(view -> {
            if (ed_item.length() < 1 || ed_date.length() < 1 || ed_price.length() < 1)
                Toast.makeText(ExpensesTracker.this, "欄位請勿留空", Toast.LENGTH_SHORT).show();
            else {
                // 檢查日期格式
                if (!isValidDateFormat(ed_date.getText().toString())) {
                    Toast.makeText(ExpensesTracker.this, "日期格式應為 yyyy/mm/dd", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    dbrw3.execSQL("INSERT INTO myTable(item, price, date) values(?,?,?)",
                            new Object[]{ed_item.getText().toString(), ed_price.getText().toString(), ed_date.getText().toString()});
                    Toast.makeText(ExpensesTracker.this, "新增項目" + ed_item.getText().toString() +
                            "      日期" + ed_date.getText().toString() + "      價格" + ed_price.getText().toString(), Toast.LENGTH_SHORT).show();
                    ed_item.setText("");
                    ed_date.setText("");
                    ed_price.setText("");
                    performQuery();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ExpensesTracker.this, "新增失敗:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_update.setOnClickListener(view -> {
            if (ed_item.length() < 1 || ed_date.length() < 1)
                Toast.makeText(ExpensesTracker.this, "欄位請勿留空", Toast.LENGTH_SHORT).show();
            else {
                try {
                    if (!isValidDateFormat(ed_date.getText().toString())) {
                        Toast.makeText(ExpensesTracker.this, "日期格式應為 yyyy/mm/dd", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String itemToUpdate = ed_item.getText().toString();
                    String newDate = ed_date.getText().toString();
                    String newPrice = ed_price.getText().toString();

                    // 使用 ? 占位符，并提供相应的值
                    dbrw3.execSQL("UPDATE myTable SET date = ?, price = ? WHERE item  LIKE ?",
                            new String[]{newDate, newPrice, "%" + itemToUpdate + "%"});

                    Toast.makeText(ExpensesTracker.this, "更新項目" + itemToUpdate +
                            "      日期" + newDate + "      價格" + newPrice, Toast.LENGTH_SHORT).show();

                    ed_item.setText("");
                    ed_date.setText("");
                    ed_price.setText("");
                    performQuery();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ExpensesTracker.this, "更新失敗:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_delete.setOnClickListener(view -> {
            if (ed_item.length() < 1)
                Toast.makeText(ExpensesTracker.this, "品項請勿留空", Toast.LENGTH_SHORT).show();
            else {
                try {
                    String itemToDelete = ed_item.getText().toString();
                    dbrw3.execSQL("DELETE FROM myTable WHERE item = ?", new String[]{itemToDelete});
                    Toast.makeText(ExpensesTracker.this, "刪除項目" + itemToDelete, Toast.LENGTH_SHORT).show();
                    ed_item.setText("");
                    ed_date.setText("");
                    ed_price.setText("");
                    performQuery();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ExpensesTracker.this, "刪除失敗:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_query.setOnClickListener(view -> {
            Cursor c;
            if (ed_item.length() < 1) {
                // 使用 ORDER BY 子句按日期排序
                c = dbrw3.rawQuery("SELECT * FROM myTable ORDER BY date", null);
            } else {
                // 使用 ORDER BY 子句按日期排序
                c = dbrw3.rawQuery("SELECT * FROM myTable WHERE item  LIKE '" +
                        ed_item.getText().toString() + "' ORDER BY date", null);
            }





            c.moveToFirst();
            items.clear();
            for (int i = 0; i < c.getCount(); i++) {
                items.add("項目:" + c.getString(0) + "\t\t\t\t日期:" + c.getString(2) + "\t\t\t\t價格:" + c.getString(1));
                c.moveToNext();
            }
            c.close();
            adapter.notifyDataSetChanged();
            // 將 items 轉換成 String[] 以供 AlertDialog 使用
            String[] itemsArray = new String[items.size()];
            itemsArray = items.toArray(itemsArray);

            // 使用 AlertDialog 顯示資料
            AlertDialog.Builder builder = new AlertDialog.Builder(ExpensesTracker.this);
            builder.setTitle("資料列表")
                    .setItems(itemsArray, null)
                    .setPositiveButton("確定", null) // 這裡添加確定按鈕，也可以根據需要添加其他按鈕
                    .show();

        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // 點擊 ListView 上的項目時執行的代碼
            String selectedItem = items.get(position);
            String[] parts = selectedItem.split("\t\t\t\t");

            // 將點擊的項目信息填充到對應的 EditText 中
            ed_item.setText(parts[0].replace("項目:", ""));
            ed_date.setText(parts[1].replace("日期:", ""));
            ed_price.setText(parts[2].replace("價格:", ""));
        });


    }
}
