package org.d3ifcool.debtreminder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.util.ArrayList;
import adapter.PeminjamanListAdapter;
import database.DBHelper;
import entities.Peminjaman;
import function.FunctionPeminjaman;


public class MainActivity extends AppCompatActivity{

    DBHelper dbHelper = new DBHelper(this);
    Cursor cursor;
    int amount;
    int pay;
    RecyclerView recyclerView;

    @Override
    protected void onStart() {
        super.onStart();

        // TODO: 03/03/2019 total lend
        // TODO: 03/03/2019 harusnya dipindahkan ke kelas FunctionPeminjaman

        TextView totalLend = findViewById(R.id.total_lend);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.isOpen();

        cursor = sqLiteDatabase.rawQuery("SELECT SUM(amount) FROM peminjaman",null);
        if (cursor.moveToFirst()){
            amount = cursor.getInt(0);
        }else {
            amount = -1;
        }
        cursor.close();

        totalLend.setText(String.valueOf(amount));

        // TODO: 25/03/2019 collected
        cursor = sqLiteDatabase.rawQuery("SELECT SUM (pay) FROM pembayaran",null);
        if (cursor.moveToFirst()){
            pay = cursor.getInt(0);
        }else {
            pay = -1;
        }
        cursor.close();
        TextView collected = findViewById(R.id.collected);
        collected.setText(String.valueOf(pay));

        TextView remaining = findViewById(R.id.remaining);
        int result;
        result = amount - pay;
        remaining.setText(String.valueOf(result));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.list_view_peminjaman);
        FunctionPeminjaman functionPeminjaman =  new FunctionPeminjaman(this);
        ArrayList<Peminjaman> listPeminjaman = new ArrayList<>();
        listPeminjaman.addAll(functionPeminjaman.findAll());
        PeminjamanListAdapter adapter = new PeminjamanListAdapter(this,listPeminjaman);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(this,layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_debt_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_password:
                break;
            case R.id.action_export:
                break;
            default:
                new IllegalArgumentException("Ups");
        }
        return super.onOptionsItemSelected(item);
    }
}
