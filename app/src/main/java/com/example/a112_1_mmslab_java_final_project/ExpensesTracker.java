package com.example.a112_1_mmslab_java_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
    private EditText ed_item, ed_date, ed_price,ed_lend,ed_borrow;
    private Button btn_insert, btn_query, btn_update, btn_delete, btn_back, btn_tutorial;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items = new ArrayList<>();
    private SQLiteDatabase dbrw3;
    private String oldLend,oldBorrow,oldItem,oldDate,oldPrice,id_item;


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
        c.moveToFirst();
        items.clear();
        Toast.makeText(ExpensesTracker.this, "共有" + c.getCount() + "筆", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < c.getCount(); i++) {
            items.add("借方:" + c.getString(1) + "\t\t\t\t貸方:" + c.getString(2) + "\n項目:" + c.getString(3)+"\n價格:"+c.getString(4)+"\n日期:"+c.getString(5));
            c.moveToNext();
        }
        adapter.notifyDataSetChanged();
        c.close();
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ed_item = findViewById(R.id.ed_item);
        ed_date = findViewById(R.id.ed_date);
        ed_price = findViewById(R.id.ed_price);
        ed_lend = findViewById(R.id.ed_lend);
        ed_borrow = findViewById(R.id.ed_borrow);
        btn_query = findViewById(R.id.btn_query);
        btn_insert = findViewById(R.id.btn_insert);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        dbrw3 = new MyDBHelper3(this).getWritableDatabase();
        btn_back = findViewById(R.id.btn_back2);

        performQuery();

        btn_tutorial=findViewById(R.id.btn_tutorial);
        btn_tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ExpensesTracker.this);

                // 设置对话框标题和消息
                builder.setTitle("使用說明")
                        .setMessage("支持單項目搜索\n查詢功能只有項目可以使用模糊搜索\n其他都要完整輸入內容才可以查詢");

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
                Intent intent = new Intent(ExpensesTracker.this, MainActivity.class);
                // 啟動 Note Activity 的 Intent
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ExpensesTracker.this.startActivity(intent);

                // 結束 MainActivity
                finish();
            }
        });

        btn_insert.setOnClickListener(view -> {
            if (ed_item.length() < 1 || ed_date.length() < 1 || ed_price.length() < 1 || ed_borrow.length() < 1 || ed_lend.length() < 1)
                Toast.makeText(ExpensesTracker.this, "欄位請勿留空", Toast.LENGTH_SHORT).show();
            else {
                // 檢查日期格式
                if (!isValidDateFormat(ed_date.getText().toString())) {
                    Toast.makeText(ExpensesTracker.this, "日期格式應為 yyyy/mm/dd", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    dbrw3.execSQL("INSERT INTO myTable(lend, borrow, item, price, date) values(?,?,?,?,?)",
                            new Object[]{ed_lend.getText().toString(), ed_borrow.getText().toString(), ed_item.getText().toString(), ed_price.getText().toString(), ed_date.getText().toString()});
                    Toast.makeText(ExpensesTracker.this, "新增借方:"+ed_lend.getText().toString()+"\t\t\t\t貸方:"+ed_borrow.getText().toString()+"\n項目:" + ed_item.getText().toString() +
                            "\n日期:" + ed_date.getText().toString() + "\n價格:" + ed_price.getText().toString(), Toast.LENGTH_LONG).show();
                    ed_lend.setText("");
                    ed_borrow.setText("");
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
            if (ed_date.length() < 1 || ed_price.length() < 1 || ed_borrow.length() < 1 || ed_lend.length() < 1 || ed_item.length() < 1)
                Toast.makeText(ExpensesTracker.this, "欄位請勿留空", Toast.LENGTH_SHORT).show();
            else {
                try {
                    if (!isValidDateFormat(ed_date.getText().toString())) {
                        Toast.makeText(ExpensesTracker.this, "日期格式應為 yyyy/mm/dd", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String newLend= ed_lend.getText().toString();
                    String newBorrow= ed_borrow.getText().toString();
                    String newItem = ed_item.getText().toString();
                    String newDate = ed_date.getText().toString();
                    String newPrice = ed_price.getText().toString();

                    // 使用 ? 占位符，并提供相应的值
                    dbrw3.execSQL("UPDATE myTable SET lend=?, borrow=?, item=?, price = ?,  date= ? WHERE _id LIKE ? AND lend LIKE ? AND borrow LIKE ? AND item LIKE ?  AND price LIKE ? AND date LIKE ?",
                            new String[]{newLend,newBorrow, newItem, newPrice ,newDate,id_item,oldLend,oldBorrow,oldItem,oldPrice,oldDate});

                    Toast.makeText(ExpensesTracker.this, "修改後的資料:\n借方:"+ed_lend.getText().toString()+"\t\t\t\t貸方:"+ed_borrow.getText().toString()+"\n項目:" + ed_item.getText().toString() +
                            "\n日期:" + ed_date.getText().toString() + "\n價格:" + ed_price.getText().toString(), Toast.LENGTH_LONG).show();
                    ed_lend.setText("");
                    ed_borrow.setText("");
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
            if (ed_lend.length() < 1 &&ed_borrow.length() < 1 &&ed_date.length() < 1 && ed_price.length() < 1 && ed_item.length() >= 1) {                  //刪除全部的某種事項
                dbrw3.execSQL("DELETE FROM myTable WHERE thing LIKE '%" + ed_item.getText() + "%'");
                Toast.makeText(ExpensesTracker.this, "刪除事項內容含有「" + ed_item.getText() + "」的項目", Toast.LENGTH_SHORT).show();

            }else{
                dbrw3.execSQL("DELETE FROM myTable WHERE lend LIKE '" + ed_lend.getText() + "' AND borrow LIKE '" + ed_borrow.getText() + "' AND item LIKE '" + ed_item.getText() + "' AND date LIKE '" + ed_date.getText() + "' AND price LIKE '" + ed_price.getText() + "'");
                Toast.makeText(ExpensesTracker.this, "刪除的資料:\n借方:"+ed_lend.getText().toString()+"\t\t\t\t貸方:"+ed_borrow.getText().toString()+"\n項目:" + ed_item.getText().toString() +
                        "\n日期:" + ed_date.getText().toString() + "\n價格:" + ed_price.getText().toString(), Toast.LENGTH_LONG).show();
            }
            performQuery();
            ed_lend.setText("");
            ed_borrow.setText("");
            ed_item.setText("");
            ed_date.setText("");
            ed_price.setText("");
        });


        btn_query.setOnClickListener(view -> {
            Cursor c;
            if(ed_lend.length() > 0 && ed_borrow.length() > 0 && ed_item.length() > 0 && ed_date.length() > 0 && ed_price.length() > 0) {

                c= dbrw3.rawQuery("SELECT * FROM myTable WHERE lend LIKE '" + oldLend + "' AND borrow LIKE '" +
                        oldBorrow+ "' AND item LIKE '"+oldItem+ "' AND date LIKE '"+oldDate+ "' AND price LIKE '"+oldPrice+"' ORDER BY date", null);  //列出特定

            }else if (ed_borrow.length() < 1 && ed_item.length() < 1 && ed_price.length() < 1 && ed_date.length() < 1 && ed_lend.length() > 0) {

                c = dbrw3.rawQuery("SELECT * FROM myTable WHERE lend  LIKE '" + ed_lend.getText().toString() + "' ORDER BY date", null); //列出某借方

            }else if(ed_lend.length() < 1 && ed_item.length() < 1 && ed_date.length() < 1 && ed_price.length() < 1 && ed_borrow.length() > 0) {

                c = dbrw3.rawQuery("SELECT * FROM myTable WHERE borrow  LIKE '" + ed_borrow.getText().toString() + "' ORDER BY date", null); //列出某貸方

            }else if(ed_lend.length() < 1 && ed_borrow.length() < 1 && ed_date.length() < 1 && ed_price.length() < 1 && ed_item.length() > 0) {

                c = dbrw3.rawQuery("SELECT * FROM myTable WHERE item  LIKE '%" + ed_item.getText().toString() + "%' ORDER BY date", null); //列出含有「某」事項

            }else if(ed_lend.length() < 1 && ed_borrow.length() < 1 && ed_item.length() < 1 && ed_price.length() < 1 && ed_date.length() > 0) {

                c = dbrw3.rawQuery("SELECT * FROM myTable WHERE date  LIKE '" + ed_date.getText().toString() + "' ORDER BY date", null); //列出某天

            }else if(ed_lend.length() < 1 && ed_borrow.length() < 1 && ed_item.length() < 1 && ed_date.length() < 1 && ed_price.length() > 0) {

                c = dbrw3.rawQuery("SELECT * FROM myTable WHERE price  LIKE '" + ed_price.getText().toString() + "' ORDER BY date", null); //列出某價格

            }else{

                c = dbrw3.rawQuery("SELECT * FROM myTable ORDER BY date", null); //全部列出
            }
            c.moveToFirst();
            items.clear();
            Toast.makeText(ExpensesTracker.this, "共有" + c.getCount() + "筆", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < c.getCount(); i++) {
                items.add("借方:" + c.getString(1) + "\t\t\t\t貸方:" + c.getString(2) + "\n項目:" + c.getString(3)+"\n價格:"+c.getString(4)+"\n日期:"+c.getString(5));
                c.moveToNext();
            }
            adapter.notifyDataSetChanged();
            c.close();
            ed_lend.setText("");
            ed_borrow.setText("");
            ed_item.setText("");
            ed_date.setText("");
            ed_price.setText("");

        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // 點擊 ListView 上的項目時執行的代碼
            String selectedItem = items.get(position);
            String[] parts = selectedItem.split("\t\t\t\t|\n");
            // 將點擊的項目信息填充到對應的 EditText 中
            ed_lend.setText(parts[0].replace("借方:", ""));
            ed_borrow.setText(parts[1].replace("貸方:", ""));
            ed_item.setText(parts[2].replace("項目:", ""));
            ed_price.setText(parts[3].replace("價格:", ""));
            ed_date.setText(parts[4].replace("日期:", ""));
            oldLend= ed_lend.getText().toString();
            oldBorrow= ed_borrow.getText().toString();
            oldItem = ed_item.getText().toString();
            oldDate = ed_date.getText().toString();
            oldPrice = ed_price.getText().toString();
            Cursor c= dbrw3.rawQuery("SELECT * FROM myTable WHERE lend LIKE '" + oldLend + "' AND borrow LIKE '"+oldBorrow+ "' AND item LIKE '"+oldItem+ "' AND date LIKE '"+oldDate+ "' AND price LIKE '"+oldPrice+"' ORDER BY date", null);
            c.moveToFirst();
            id_item = c.getString(0);
            c.close();
        });


    }
}
